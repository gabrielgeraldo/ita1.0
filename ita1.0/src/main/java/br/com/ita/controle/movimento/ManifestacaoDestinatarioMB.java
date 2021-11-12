package br.com.ita.controle.movimento;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.primefaces.PrimeFaces;
import org.simpleframework.xml.core.ElementException;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.classes.distribuicao.NFDistribuicaoDocumentoZip;
import com.fincatto.documentofiscal.nfe.classes.distribuicao.NFDistribuicaoIntRetorno;
import com.fincatto.documentofiscal.nfe.webservices.distribuicao.WSDistribuicaoNFe;
import com.fincatto.documentofiscal.nfe400.classes.evento.NFEnviaEventoRetorno;
import com.fincatto.documentofiscal.nfe400.classes.evento.NFEventoRetorno;
import com.fincatto.documentofiscal.nfe400.classes.evento.manifestacaodestinatario.NFTipoEventoManifestacaoDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaResumo;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;
import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.XmlUtil;
import br.com.ita.dominio.Estado;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.EmpresaDAO;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.empresa.Empresa;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

@Named("manifestacaoDestinatarioMB")
@RequestScoped
public class ManifestacaoDestinatarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NFeConfigIta config;

	@Inject
	private EstadoDAO daoEstado;

	private List<Estado> estados = null;

	private String nsu;

	@NotNull(message = "O último NSU é de preenchimento obrigatório")
	@Size(min = 15, max = 15, message = "O último NSU deve ter 15 dígitos")
	private String ultNsu;

	private String chave;

	@NotNull(message = "O estado é de preenchimento obrigatório")
	private Estado estado;

	@Inject
	private Empresa empresa;

	@Inject
	private EmpresaDAO empresaDao;

	private List<NFNotaResumo> notasParaManifestar = new ArrayList<NFNotaResumo>();

	private String ambienteConfigurado;

	@Inject
	private Configuracao configuracao;

	@Inject
	private ConfiguracaoDAO configuracaoDao;

	private NFTipoEventoManifestacaoDestinatario tipoEvento;

	private List<NFTipoEventoManifestacaoDestinatario> tiposDeEvento;

	private String chaveNotaSelecionada;

	private List<NFNotaProcessada> notasManifestadas = new ArrayList<NFNotaProcessada>();

	@PostConstruct
	public void init() {

		empresa = empresaDao.lerPorId(new Long(1));

		configuracao = configuracaoDao.lerPorId(new Long(1));

		ambienteConfigurado = configuracao.getAmbiente();

	}

	public String manifestarNota() {

		String chave = JSFUtil.getParametro("itemcodigo");
		this.chaveNotaSelecionada = chave;

		return "/ManifestacaoDestinatario/nfeManifestar";

	}

	public void realizarManifestacao() {

		System.out.println(chaveNotaSelecionada);
		System.out.println(tipoEvento);
		System.out.println(empresa.getCnpj());

		try {
			NFEnviaEventoRetorno retorno = new WSFacade(config).manifestaDestinatarioNota(chaveNotaSelecionada,
					tipoEvento, null, empresa.getCnpj());

			for (NFEventoRetorno ret : retorno.getEventoRetorno()) {
				System.out.println("Chave: " + ret.getInfoEventoRetorno().getChave());
				System.out.println("Codigo Status: " + ret.getInfoEventoRetorno().getCodigoStatus());
				System.out.println("Motivo: " + ret.getInfoEventoRetorno().getMotivo());
				System.out.println("Numero Protocolo: " + ret.getInfoEventoRetorno().getNumeroProtocolo());
				System.out
						.println("Numero Sequencial Evento: " + ret.getInfoEventoRetorno().getNumeroSequencialEvento());
				System.out.println("Tipo De Evento: " + ret.getInfoEventoRetorno().getTipoEvento());
				System.out.println("Descricao De Evento: " + ret.getInfoEventoRetorno().getDescricaoEvento());
				System.out.println("Ambiente: " + ret.getInfoEventoRetorno().getAmbiente().getCodigo());

				// SE EVENTO ALTORIZADO
				if (ret.getInfoEventoRetorno().getCodigoStatus().toString().equals("135")) {

					JSFUtil.retornarMensagemAviso(ret.getInfoEventoRetorno().getMotivo(), null, null);

				} else {

					JSFUtil.retornarMensagemAviso(ret.getInfoEventoRetorno().getMotivo(), null, null);

				}

			}

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(e.getMessage(), e.getMessage(), null);
		}

		boolean fecharDialogStatus = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialogStatus", fecharDialogStatus);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialogStatus", fecharDialogStatus);

	}

	public void consultar() {
		
		NFDistribuicaoIntRetorno retorno = null;
		try {
			retorno = new WSFacade(config).consultarDistribuicaoDFe(empresa.getCnpj(),
					DFUnidadeFederativa.valueOfCodigo(estado.getUf()), chave, nsu, ultNsu);

			System.out.println("------------- MOTIVO -----------------");
			System.out.println(retorno.getMotivo());
			System.out.println("------------- MOTIVO -----------------");

			JSFUtil.retornarMensagemInfo(null, retorno.getMotivo(), null);

			if (retorno.getLote() != null) {

				System.out.println("-------------  ------------");
				for (int i = 0; i < retorno.getLote().getDocZip().size(); i++) {
					System.out.println(retorno.getLote().getDocZip().get(i).getValue());
				}
				System.out.println("-------------  ------------");

				System.out.println("------------- GET DOC ZIP TO XML ------------");
				List<NFDistribuicaoDocumentoZip> docZipList = retorno.getLote().getDocZip();
				for (NFDistribuicaoDocumentoZip docZip : docZipList) {
					String resNFe = WSDistribuicaoNFe.decodeGZipToXml(docZip.getValue());

					System.out.println(resNFe);
					
					try {
						final NFNotaResumo nota = new DFPersister().read(NFNotaResumo.class, resNFe);
						System.out.println(" Chave acesso: " + nota.getChave());
						System.out.println(" CNPJ        : " + nota.getCNPJ());
						System.out.println(" Emit. Razao : " + nota.getNome());
						System.out.println(" Protoco.    : " + nota.getNumeroProtocolo());
						System.out.println(" Total       : " + nota.getValor());

						notasParaManifestar.add(nota);
					} catch (ElementException e1) {
						// e1.printStackTrace();
					}

					try {
						final NFNotaProcessada nota = new DFPersister().read(NFNotaProcessada.class, resNFe);
						System.out.println(" Chave acesso: " + nota.getNota().getInfo().getChaveAcesso());
						System.out.println(" CNPJ        : " + nota.getNota().getInfo().getEmitente().getCnpj());
						System.out.println(" Emit. Razao : " + nota.getNota().getInfo().getEmitente().getRazaoSocial());
						System.out.println(" Protoco.    : " + nota.getProtocolo());
						System.out.println(" Total       : " + nota.getNota().getInfo().getTotal());

						notasManifestadas.add(nota);
					} catch (ElementException e1) {
						// e1.printStackTrace();
					}
					
				}
				System.out.println("------------- GET DOC ZIP TO XML ------------");

			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			JSFUtil.retornarMensagemErro(null, e1.getMessage(), null);
		}

		for (int i = 0; i < notasParaManifestar.size(); i++) {
			System.out.println(notasParaManifestar.get(i).getChave());
		}
		/*
		NFNotaResumo nota = null;
		try {
			nota = new DFPersister().read(NFNotaResumo.class, "<resNFe xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" versao=\"1.01\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><chNFe>33211026080810000199550040000000021001454966</chNFe><CNPJ>26080810000199</CNPJ><xNome>DAKAR AUTO PECAS E SERVICOS LTDA</xNome><IE>87210880</IE><dhEmi>2021-10-21T13:38:07-03:00</dhEmi><tpNF>1</tpNF><vNF>1.00</vNF><digVal>rAuoyTlEO3quhiLEu8fE4MqMteU=</digVal><dhRecbto>2021-10-21T13:38:22-03:00</dhRecbto><nProt>333210000451925</nProt><cSitNFe>1</cSitNFe></resNFe>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notasParaManifestar.add(nota);
		
		NFNotaProcessada notaP = null;
		try {
			notaP = new DFPersister().read(NFNotaProcessada.class, "<nfeProc versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infNFe Id=\"NFe33210709437886000100550010002854351793261800\" versao=\"4.00\"><ide><cUF>33</cUF><cNF>79326180</cNF><natOp>BONIFICACAO, DOACAO OU BRINDE</natOp><mod>55</mod><serie>1</serie><nNF>285435</nNF><dhEmi>2021-07-22T09:51:00-03:00</dhEmi><dhSaiEnt>2021-07-22T09:51:00-03:00</dhSaiEnt><tpNF>1</tpNF><idDest>1</idDest><cMunFG>3304144</cMunFG><tpImp>1</tpImp><tpEmis>1</tpEmis><cDV>0</cDV><tpAmb>1</tpAmb><finNFe>1</finNFe><indFinal>0</indFinal><indPres>9</indPres><procEmi>0</procEmi><verProc>1.3.187</verProc></ide><emit><CNPJ>09437886000100</CNPJ><xNome>COMERCIAL HELLO BRASIL LTDA</xNome><xFant>COMERCIAL HELLO BRASIL LTDA</xFant><enderEmit><xLgr>AV GUANDU 1000</xLgr><nro>1000</nro><xBairro>QUEIMADOS</xBairro><cMun>3304144</cMun><xMun>QUEIMADOS</xMun><UF>RJ</UF><CEP>26373000</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>2124552035</fone></enderEmit><IE>79027936</IE><CRT>3</CRT></emit><dest><CNPJ>17358981000146</CNPJ><xNome>DUQUE FARMA LTDA</xNome><enderDest><xLgr>AVENIDA PRESIDENTE TANCREDO NEVES 391</xLgr><nro>391</nro><xCpl>QUADRA17 LOTE 27 ANT 71</xCpl><xBairro>VILA ITAMARATI</xBairro><cMun>3301702</cMun><xMun>DUQUE DE CAXIAS</xMun><UF>RJ</UF><CEP>25070096</CEP><cPais>1058</cPais><xPais>BRASIL</xPais><fone>2126717773</fone></enderDest><indIEDest>1</indIEDest><IE>79832316</IE><email>duquefarmacaxias@hotmail.com</email></dest><det nItem=\"1\"><prod><cProd>10232</cProd><cEAN>7898617522175</cEAN><xProd>KIT PROB COL SUPER B 2.8 PRET A PER 50GR</xProd><NCM>33059000</NCM><CEST>2002200</CEST><indEscala>S</indEscala><CFOP>5910</CFOP><uCom>UN</uCom><qCom>3.0000</qCom><vUnCom>6.9454000000</vUnCom><vProd>20.84</vProd><cEANTrib>7898617522175</cEANTrib><uTrib>UN</uTrib><qTrib>3.0000</qTrib><vUnTrib>6.9454000000</vUnTrib><indTot>1</indTot><xPed>121010203</xPed><nItemPed>1</nItemPed></prod><imposto><ICMS><ICMS60><orig>0</orig><CST>60</CST><vBCSTRet>0.00</vBCSTRet><pST>0.0000</pST><vICMSSubstituto>0.00</vICMSSubstituto><vICMSSTRet>0.00</vICMSSTRet></ICMS60></ICMS><IPI><cEnq>999</cEnq><IPITrib><CST>99</CST><vBC>0.00</vBC><pIPI>0.0000</pIPI><vIPI>0.00</vIPI></IPITrib></IPI><PIS><PISNT><CST>04</CST></PISNT></PIS><COFINS><COFINSNT><CST>04</CST></COFINSNT></COFINS></imposto></det><total><ICMSTot><vBC>0.00</vBC><vICMS>0.00</vICMS><vICMSDeson>0.00</vICMSDeson><vFCP>0.00</vFCP><vBCST>0.00</vBCST><vST>0.00</vST><vFCPST>0.00</vFCPST><vFCPSTRet>0.00</vFCPSTRet><vProd>20.84</vProd><vFrete>0.00</vFrete><vSeg>0.00</vSeg><vDesc>0.00</vDesc><vII>0.00</vII><vIPI>0.00</vIPI><vIPIDevol>0.00</vIPIDevol><vPIS>0.00</vPIS><vCOFINS>0.00</vCOFINS><vOutro>0.00</vOutro><vNF>20.84</vNF><vTotTrib>0.00</vTotTrib></ICMSTot></total><transp><modFrete>0</modFrete><transporta><CNPJ>13214075000117</CNPJ><xNome>PROLOG TRANSPORTES E LOGISTICA LTDA</xNome><xEnder>RUA SAO PAULO 43, - FLORESTA</xEnder><xMun>NOVA IGUACU</xMun><UF>RJ</UF></transporta><veicTransp><placa>PRL1234</placa><UF>RJ</UF></veicTransp><vol><qVol>1</qVol><nVol>1</nVol><pesoL>0.126</pesoL><pesoB>0.150</pesoB></vol></transp><pag><detPag><indPag>0</indPag><tPag>01</tPag><vPag>20.84</vPag></detPag></pag><infAdic><infCpl>PEDIDO: 121010203 CARREGAMENTO: 28433 CLIENTE: 3342 OBS://TELEFONE RCA: 21970028001//OBS ENTREGA1: QUADRA 17 LOTE 27 A, , SABADO SIM//NUM TRANSACAO: 504800 / COD COBRANCA: BNF - BONIFICACAO//ENDERECO DE ENTREGA : AVENIDA PRESIDENTE TANCREDO NEVES,391,391,VILA ITAMARATI,Duque de Caxias,RJ//**COMPLEMENTO / PONTO DE REFERENCIA : QUADRA17 LOTE 27 ANT 71//SABADO SIM//SEG A SEXTA ATE 17 HORAS//IMPOSTO ICMS RETIDO POR SUBSTITUICAO TRIBUTARIA CONFORME RICMS 27.427 / 2000</infCpl></infAdic><infRespTec><CNPJ>07577599000501</CNPJ><xContato>TOTVS BRASILIA SOFTWARE - UNIDADE GOIANIA LTDA</xContato><email>resp_tecnico_dfe_winthor@totvs.com.br</email><fone>06232500200</fone></infRespTec></infNFe><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" /><Reference URI=\"#NFe33210709437886000100550010002854351793261800\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" /><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\" /></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" /><DigestValue>KdsgQfeHyYu6yFRSAw8mUXiRec8=</DigestValue></Reference></SignedInfo><SignatureValue>WTKFfzSXecGagojFUbhBWQhYCKAhZA1NyIRHxjPqqS5vBWuRBbSgwvkcvFSOU2iaiC6SehODcaW7d2aXKFZtIFhVh9t5rXt9Labfn1cd/8Mg656W0KTzJhxKLfT/Ss8irWruWqW8Y3xLfM0gzjKmQ5cixyvGdahofos4PayvhAAO4G0huLBCVWPEN4kG0RWytyHH5cyydcn5/oCsLXu2Q8J1nq1Dnv3Ru/HPwAKNmk0whxOwNeb9aM/ik5jHGiU60YQ45v8vNG9Y7rjWWZQLI9RUFs1bKDc/G8eX4xfOACM7CpUZumNWHv6jIJqIwpS03YDoqJWreumCM/Y+uH2eKQ==</SignatureValue><KeyInfo><X509Data><X509Certificate>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</X509Certificate></X509Data></KeyInfo></Signature></NFe><protNFe versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><infProt><tpAmb>1</tpAmb><verAplic>SVRS202103300813</verAplic><chNFe>33210709437886000100550010002854351793261800</chNFe><dhRecbto>2021-07-22T09:51:46-03:00</dhRecbto><nProt>333210117645263</nProt><digVal>KdsgQfeHyYu6yFRSAw8mUXiRec8=</digVal><cStat>100</cStat><xMotivo>Autorizado o uso da NF-e</xMotivo></infProt></protNFe></nfeProc>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notasManifestadas.add(notaP);
		*/
	}
	
	public void downloadXml() {
		
		String xml = JSFUtil.getParametro("itemcodigo");
		
		NFNotaProcessada nota = null;
		try {
			nota = new DFPersister().read(NFNotaProcessada.class, xml);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		try {
			String requestSoap = xml;

			XmlUtil.downloadXml(requestSoap, nota.getNota().getInfo().getChaveAcesso());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemAviso("XML nao encontrado. O downloand nao sera realizado.", null, null);
		}
		
		
	}

	public String manifestar() {

		return "/ManifestacaoDestinatario/manifestar?faces-redirect=true";

	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

	public EstadoDAO getDaoEstado() {
		return daoEstado;
	}

	public void setDaoEstado(EstadoDAO daoEstado) {
		this.daoEstado = daoEstado;
	}

	public List<Estado> getEstados() {

		if (this.estados == null)
			this.estados = this.daoEstado.lerTodos();

		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public String getNsu() {
		return nsu;
	}

	public void setNsu(String nsu) {
		this.nsu = nsu;
	}

	public String getUltNsu() {
		return ultNsu;
	}

	public void setUltNsu(String ultNsu) {
		this.ultNsu = ultNsu;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public EmpresaDAO getEmpresaDao() {
		return empresaDao;
	}

	public void setEmpresaDao(EmpresaDAO empresaDao) {
		this.empresaDao = empresaDao;
	}

	public List<NFNotaResumo> getNotasParaManifestar() {
		return notasParaManifestar;
	}

	public void setNotasParaManifestar(List<NFNotaResumo> notasParaManifestar) {
		this.notasParaManifestar = notasParaManifestar;
	}

	public String getAmbienteConfigurado() {
		return ambienteConfigurado;
	}

	public void setAmbienteConfigurado(String ambienteConfigurado) {
		this.ambienteConfigurado = ambienteConfigurado;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

	public ConfiguracaoDAO getConfiguracaoDao() {
		return configuracaoDao;
	}

	public void setConfiguracaoDao(ConfiguracaoDAO configuracaoDao) {
		this.configuracaoDao = configuracaoDao;
	}

	public NFTipoEventoManifestacaoDestinatario getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(NFTipoEventoManifestacaoDestinatario tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public List<NFTipoEventoManifestacaoDestinatario> getTiposDeEvento() {

		if (this.tiposDeEvento == null)
			this.tiposDeEvento = Arrays.asList(NFTipoEventoManifestacaoDestinatario.values());

		return tiposDeEvento;
	}

	public void setTiposDeEvento(List<NFTipoEventoManifestacaoDestinatario> tiposDeEvento) {
		this.tiposDeEvento = tiposDeEvento;
	}

	public String getChaveNotaSelecionada() {
		return chaveNotaSelecionada;
	}

	public void setChaveNotaSelecionada(String chaveNotaSelecionada) {
		this.chaveNotaSelecionada = chaveNotaSelecionada;
	}

	public List<NFNotaProcessada> getNotasManifestadas() {
		return notasManifestadas;
	}

	public void setNotasManifestadas(List<NFNotaProcessada> notasManifestadas) {
		this.notasManifestadas = notasManifestadas;
	}

}
