package br.com.ita.controle.nfe;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import com.fincatto.documentofiscal.DFModelo;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFTipoEmissao;
import com.fincatto.documentofiscal.nfe400.classes.NFEndereco;
import com.fincatto.documentofiscal.nfe400.classes.NFFinalidade;
import com.fincatto.documentofiscal.nfe400.classes.NFIndicadorFormaPagamento;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.NFProcessoEmissor;
import com.fincatto.documentofiscal.nfe400.classes.NFProdutoCompoeValorNota;
import com.fincatto.documentofiscal.nfe400.classes.NFProtocolo;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;
import com.fincatto.documentofiscal.nfe400.classes.NFTipoImpressao;
import com.fincatto.documentofiscal.nfe400.classes.lote.consulta.NFLoteConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvio;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteIndicadorProcessamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFInfoReferenciada;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFMeioPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoCobranca;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoEmitente;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoFatura;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoFormaPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoICMSTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoIdentificacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoInformacoesAdicionais;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItem;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImposto;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSNaoTributavel;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN101;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN102;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN500;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoIPI;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoIPITributado;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPIS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPISNaoTributado;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPISOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemProduto;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoParcela;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTransporte;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoVolume;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.nfe400.classes.statusservico.consulta.NFStatusServicoConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.utils.NFGeraChave;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

import br.com.ita.controle.config.Config;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.XmlUtil;
import br.com.ita.dominio.Configuracao;
import br.com.ita.dominio.FormaPagamento;
import br.com.ita.dominio.ItemNTFe;
import br.com.ita.dominio.NTFe;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.ItemNFeDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.dao.NFeDAO;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

@Named("transmissaoNFeMB")
@ViewScoped
public class TransmissaoNFeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private NTFe nfe;

	@Inject
	private NFeDAO daoNFe;

	@Inject
	private ItemNFeDAO daoItemNFe;

	@Inject
	private NFeConfigIta config;

	private NFNotaInfoEmitente emitente = new NFNotaInfoEmitente();

	private NFEndereco emitenteEndereco = new NFEndereco();

	private NFNotaInfoDestinatario destinatario = new NFNotaInfoDestinatario();

	private NFEndereco destinatarioEndereco = new NFEndereco();

	private NFNotaInfoIdentificacao identificacao = new NFNotaInfoIdentificacao();

	private NFNotaInfo info = new NFNotaInfo();

	private NFNota nota = new NFNota();

	private NFNotaInfoItemProduto produto = new NFNotaInfoItemProduto();

	private NFNotaInfoItem item = new NFNotaInfoItem();

	private NFNotaInfoItemImposto imposto = new NFNotaInfoItemImposto();

	private NFNotaInfoItemImpostoICMS icms = new NFNotaInfoItemImpostoICMS();

	private NFNotaInfoItemImpostoIPI ipi = new NFNotaInfoItemImpostoIPI();

	private NFNotaInfoItemImpostoIPITributado ipiTributado = new NFNotaInfoItemImpostoIPITributado();

	private NFNotaInfoItemImpostoPIS pis = new NFNotaInfoItemImpostoPIS();

	private NFNotaInfoItemImpostoPISNaoTributado pisNaoTributado = new NFNotaInfoItemImpostoPISNaoTributado();

	private NFNotaInfoItemImpostoCOFINSNaoTributavel cofinsNaoTributado = new NFNotaInfoItemImpostoCOFINSNaoTributavel();

	private NFNotaInfoItemImpostoCOFINS cofins = new NFNotaInfoItemImpostoCOFINS();

	private NFNotaInfoICMSTotal icmsTotal = new NFNotaInfoICMSTotal();

	private NFNotaInfoTotal total = new NFNotaInfoTotal();

	private NFLoteEnvio lote = new NFLoteEnvio();

	private List<NFNotaInfoItem> itens = new ArrayList<NFNotaInfoItem>();

	private List<ItemNTFe> itensNTFe = null;

	private String statusSefaz;

	private List<NTFe> notasParaTransmitir = null;

	private List<NTFe> notasParaTransmitirFiltrados = null;

	private NFNotaInfoPagamento pagamento = new NFNotaInfoPagamento();

	private List<NFNotaInfoPagamento> pagamentos = new ArrayList<NFNotaInfoPagamento>();

	@Inject
	private ConfiguracaoDAO daoConfiguracao;

	private String ambienteConfigurado;

	private NFNotaInfoCobranca cobranca = new NFNotaInfoCobranca();

	private NFNotaInfoFatura fatura = new NFNotaInfoFatura();

	private NFNotaInfoParcela parcela = new NFNotaInfoParcela();

	private List<NFNotaInfoParcela> parcelas = new ArrayList<NFNotaInfoParcela>();

	@Inject
	private EstadoDAO daoEstado;

	@Inject
	private MunicipioDAO daoMunicipio;

	@PostConstruct
	public void init() {
		ambienteConfigurado = Config.propertiesLoader().getProperty("ambiente");
	}

	public String listar() {

		return "/NFe/nfeTransmitir?faces-redirect=true";

	}

	public void consultarStatusSefaz() {

		NFStatusServicoConsultaRetorno retornoStatus = null;
		try {
			retornoStatus = new WSFacade(config).consultaStatus(DFUnidadeFederativa.RJ, DFModelo.NFE);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemFatal(e.getMessage(), null, null);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
		}

		// System.out.println("Status SEFAZ: " + retornoStatus.getStatus());
		// System.out.println("Motivo SEFAZ: " + retornoStatus.getMotivo());

		// System.out.println("Ambiente Configurado: " + config.getAmbiente());

		this.setStatusSefaz(retornoStatus.getStatus() + " " + retornoStatus.getMotivo() + " " + config.getAmbiente());

	}

	public String gerarCodigoRandomico() {

		Random random = new Random();
		int i = random.nextInt(99999999) * 1;
		String cnf = StringUtils.leftPad(String.valueOf(i), 8, "0");

		return cnf;
	}

	public void geraEmitenteEndereco() {

		emitenteEndereco.setLogradouro(Config.propertiesLoader().getProperty("endereco"));
		emitenteEndereco.setNumero(Config.propertiesLoader().getProperty("numero"));
		emitenteEndereco.setComplemento(Config.propertiesLoader().getProperty("complemento"));
		emitenteEndereco.setBairro(Config.propertiesLoader().getProperty("bairro"));
		emitenteEndereco.setCodigoMunicipio(Config.propertiesLoader().getProperty("codigoMunicipio"));
		emitenteEndereco.setDescricaoMunicipio(Config.propertiesLoader().getProperty("descricaoMunicipio"));
		emitenteEndereco.setUf(DFUnidadeFederativa.valueOfCodigo(Config.propertiesLoader().getProperty("uf")));
		emitenteEndereco.setCep(Config.propertiesLoader().getProperty("cep"));
		emitenteEndereco.setCodigoPais(Config.propertiesLoader().getProperty("codigoPais"));
		emitenteEndereco.setDescricaoPais(Config.propertiesLoader().getProperty("descricaoPais"));
		emitenteEndereco.setTelefone(Config.propertiesLoader().getProperty("telefone"));

	}

	public void geraEmitente() {

		emitente.setCnpj(Config.propertiesLoader().getProperty("cnpj"));
		emitente.setRazaoSocial(Config.propertiesLoader().getProperty("razaoSocial"));
		emitente.setNomeFantasia(Config.propertiesLoader().getProperty("nomeFantasia"));
		emitente.setEndereco(emitenteEndereco);
		emitente.setInscricaoEstadual(Config.propertiesLoader().getProperty("inscricaoEstadual"));
		emitente.setRegimeTributario(NFRegimeTributario.SIMPLES_NACIONAL);

	}

	public void geraDestinatarioEndereco() {

		if (this.nfe.getNfFfinalidade() == NFFinalidade.NORMAL) {

			destinatarioEndereco.setLogradouro(nfe.getCliente().getEndereco());
			destinatarioEndereco.setNumero(nfe.getCliente().getNumero());
			destinatarioEndereco.setComplemento(nfe.getCliente().getComplemento());
			destinatarioEndereco.setBairro(nfe.getCliente().getBairro());
			destinatarioEndereco.setCodigoMunicipio(nfe.getCliente().getMunicipio().getCodigo().toString());
			destinatarioEndereco.setDescricaoMunicipio(nfe.getCliente().getMunicipio().getNome());
			destinatarioEndereco.setUf(DFUnidadeFederativa.valueOfCodigo(nfe.getCliente().getEstado().getUf()));
			destinatarioEndereco.setCep(nfe.getCliente().getCep());
			destinatarioEndereco.setCodigoPais("1058");
			destinatarioEndereco.setDescricaoPais("BRASIL");
			destinatarioEndereco.setTelefone(nfe.getCliente().getTelefone());

		}

		if (this.nfe.getNfFfinalidade() == NFFinalidade.DEVOLUCAO_OU_RETORNO) {

			destinatarioEndereco.setLogradouro(nfe.getNfEntrada().getFornecedor().getEndereco());
			destinatarioEndereco.setNumero(nfe.getNfEntrada().getFornecedor().getNumero());
			destinatarioEndereco.setComplemento(nfe.getNfEntrada().getFornecedor().getComplemento());
			destinatarioEndereco.setBairro(nfe.getNfEntrada().getFornecedor().getBairro());
			destinatarioEndereco
					.setCodigoMunicipio(nfe.getNfEntrada().getFornecedor().getMunicipio().getCodigo().toString());
			destinatarioEndereco.setDescricaoMunicipio(nfe.getNfEntrada().getFornecedor().getMunicipio().getNome());
			destinatarioEndereco
					.setUf(DFUnidadeFederativa.valueOfCodigo(nfe.getNfEntrada().getFornecedor().getEstado().getUf()));
			destinatarioEndereco.setCep(nfe.getNfEntrada().getFornecedor().getCep());
			destinatarioEndereco.setCodigoPais("1058");
			destinatarioEndereco.setDescricaoPais("BRASIL");
			destinatarioEndereco.setTelefone(nfe.getNfEntrada().getFornecedor().getTelefone());

		}

	}

	public void geraDestinatario() {

		if (this.nfe.getNfFfinalidade() == NFFinalidade.NORMAL) {

			if (nfe.getCliente().getTipo().equals("F")) {
				destinatario.setCpf(nfe.getCliente().getCgc());
			} else if (nfe.getCliente().getTipo().equals("J")) {
				destinatario.setCnpj(nfe.getCliente().getCgc());
			}

			if (!nfe.getCliente().getInscEst().isEmpty()) {
				destinatario.setInscricaoEstadual(nfe.getCliente().getInscEst());
			}

			destinatario.setRazaoSocial(nfe.getCliente().getNome());
			destinatario.setEndereco(destinatarioEndereco);
			destinatario.setIndicadorIEDestinatario(nfe.getCliente().getNfIndicadorIEDestinatario());
			destinatario.setEmail(nfe.getCliente().getEmail());

		}

		if (this.nfe.getNfFfinalidade() == NFFinalidade.DEVOLUCAO_OU_RETORNO) {

			if (nfe.getNfEntrada().getFornecedor().getTipo().equals("F")) {
				destinatario.setCpf(nfe.getCliente().getCgc());
			} else if (nfe.getNfEntrada().getFornecedor().getTipo().equals("J")) {
				destinatario.setCnpj(nfe.getNfEntrada().getFornecedor().getCgc());
			}

			if (!nfe.getNfEntrada().getFornecedor().getInscricaoEstadual().isEmpty()) {
				destinatario.setInscricaoEstadual(nfe.getNfEntrada().getFornecedor().getInscricaoEstadual());
			}

			destinatario.setRazaoSocial(nfe.getNfEntrada().getFornecedor().getRazaoSocial());
			destinatario.setEndereco(destinatarioEndereco);
			destinatario.setIndicadorIEDestinatario(nfe.getNfEntrada().getFornecedor().getNfIndicadorIEDestinatario());
			destinatario.setEmail(nfe.getNfEntrada().getFornecedor().getEmail());

		}

	}

	public void geraNFNotaInfoIdentificacao() {

		identificacao.setUf(DFUnidadeFederativa.valueOfCodigo(Config.propertiesLoader().getProperty("uf")));
		// identificacao.setCodigoRandomico(null); // Esse atributo � setado no
		// m�todo transmitirNfe().

		// Esse valaor e setado como "teste" e depois e atualizado no m�todo
		// transmitirNfe().
		identificacao.setCodigoRandomico(this.gerarCodigoRandomico());
		identificacao.setNaturezaOperacao(this.nfe.getNaturezaOperacao());
		// Removido 20180913 ao atualizar nfe40
		// identificacao.setFormaPagamento(this.nfe.getNfFormaPagamentoPrazo());
		identificacao.setModelo(DFModelo.valueOfCodigo("55"));
		identificacao.setSerie(Integer.toString(this.nfe.getSerie()));
		identificacao.setNumeroNota(Integer.toString(this.nfe.getNumero())); // VERIFICAR
		identificacao.setDataHoraEmissao(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
		identificacao.setDataHoraSaidaOuEntrada(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
		identificacao.setTipo(this.nfe.getTipo());
		identificacao.setIdentificadorLocalDestinoOperacao(this.nfe.getNfIdentificadorLocalDestinoOperacao());
		identificacao.setCodigoMunicipio(Config.propertiesLoader().getProperty("codigoMunicipio"));
		identificacao.setTipoImpressao(NFTipoImpressao.valueOfCodigo("1"));
		identificacao.setTipoEmissao(NFTipoEmissao.valueOfCodigo("1"));
		identificacao.setDigitoVerificador(1);
		identificacao.setAmbiente(config.getAmbiente());
		identificacao.setFinalidade(this.nfe.getNfFfinalidade());

		if (this.nfe.getNfFfinalidade() == NFFinalidade.DEVOLUCAO_OU_RETORNO) {

			NFInfoReferenciada referenciada = new NFInfoReferenciada();
			referenciada.setChaveAcesso(this.nfe.getNfEntrada().getChave());

			List<NFInfoReferenciada> referenciadas = new ArrayList<NFInfoReferenciada>();
			referenciadas.add(referenciada);

			identificacao.setReferenciadas(referenciadas);

		}

		identificacao.setOperacaoConsumidorFinal(this.nfe.getNfOperacaoConsumidorFinal());
		identificacao.setIndicadorPresencaComprador(this.nfe.getNfIndicadorPresencaComprador());
		identificacao.setProgramaEmissor(NFProcessoEmissor.valueOfCodigo("0"));
		identificacao.setVersaoEmissor("1.0");

	}

	public void geraPagamentos() {

		NFNotaInfoFormaPagamento fpgto = new NFNotaInfoFormaPagamento();

		switch (this.nfe.getCondicaoPagamento().getFormaPagamento()) {
		case AVISTA:
			fpgto.setIndicadorFormaPagamento(NFIndicadorFormaPagamento.A_VISTA);
			break;

		case APRAZO:
			fpgto.setIndicadorFormaPagamento(NFIndicadorFormaPagamento.A_PRAZO);
			break;

		default:
			fpgto.setIndicadorFormaPagamento(null);
			System.out.println("ERRO NO PREENCHIMENTO DA FORMA DE PAGAMENTO!");

		}

		switch (this.nfe.getCondicaoPagamento().getMeioPagamento()) {
		case DINHEIRO:
			fpgto.setMeioPagamento(NFMeioPagamento.DINHEIRO);
			break;

		case CHEQUE:
			fpgto.setMeioPagamento(NFMeioPagamento.CHEQUE);
			break;

		case CARTAO_CREDITO:
			fpgto.setMeioPagamento(NFMeioPagamento.CARTAO_CREDITO);
			break;

		case CARTAO_DEBITO:
			fpgto.setMeioPagamento(NFMeioPagamento.CARTAO_DEBITO);
			break;

		case CARTAO_LOJA:
			fpgto.setMeioPagamento(NFMeioPagamento.CARTAO_LOJA);
			break;

		case VALE_ALIMENTACAO:
			fpgto.setMeioPagamento(NFMeioPagamento.VALE_ALIMENTACAO);
			break;

		case VALE_REFEICAO:
			fpgto.setMeioPagamento(NFMeioPagamento.VALE_REFEICAO);
			break;

		case VALE_PRESENTE:
			fpgto.setMeioPagamento(NFMeioPagamento.VALE_PRESENTE);
			break;

		case VALE_COMBUSTIVEL:
			fpgto.setMeioPagamento(NFMeioPagamento.VALE_COMBUSTIVEL);
			break;

		case DUPLICATA_MERCANTIL:
			fpgto.setMeioPagamento(NFMeioPagamento.DUPLICATA_MERCANTIL);
			break;

		case BOLETO_BANCARIO:
			fpgto.setMeioPagamento(NFMeioPagamento.BOLETO_BANCARIO);
			break;

		case SEM_PAGAMENTO:
			fpgto.setMeioPagamento(NFMeioPagamento.SEM_PAGAMENTO);
			break;

		case OUTRO:
			fpgto.setMeioPagamento(NFMeioPagamento.OUTRO);
			break;

		default:
			fpgto.setMeioPagamento(null);
			System.out.println("ERRO NO PREENCHIMENTO DO MEIO DE PAGAMENTO!");

		}

		if (this.nfe.getNfFfinalidade() == NFFinalidade.NORMAL)
			fpgto.setValorPagamento(this.nfe.getTotal());

		if (this.nfe.getNfFfinalidade() == NFFinalidade.DEVOLUCAO_OU_RETORNO)
			fpgto.setValorPagamento(new java.math.BigDecimal("0.00"));

		List<NFNotaInfoFormaPagamento> lsFpgto = new ArrayList<NFNotaInfoFormaPagamento>();
		lsFpgto.add(fpgto);

		pagamento.setDetalhamentoFormasPagamento(lsFpgto);
		pagamentos.add(pagamento);

		if (this.nfe.getCondicaoPagamento().getFormaPagamento() == FormaPagamento.APRAZO) {

			fatura.setNumeroFatura(this.nfe.getContasReceber().get(0).getId().getNumeroCR().toString());
			fatura.setValorDesconto(new java.math.BigDecimal("0.00"));
			fatura.setValorLiquidoFatura(this.nfe.getTotal());
			fatura.setValorOriginalFatura(this.nfe.getTotal());

			String y = "";

			for (int x = 0; x < this.nfe.getContasReceber().size(); x++) {

				parcela.setDataVencimento(this.nfe.getContasReceber().get(x).getVencimento().toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate());

				if (x + 1 <= 9) {
					y = "00";
				} else if (x + 1 >= 10 || x + 1 <= 99) {
					y = "0";
				}

				parcela.setNumeroParcela(y + this.nfe.getContasReceber().get(x).getId().getParcelaCR().toString());

				parcela.setValorParcela(this.nfe.getContasReceber().get(x).getValor());

				parcelas.add(parcela);
				parcela = new NFNotaInfoParcela();

			}

			cobranca.setFatura(fatura);
			cobranca.setParcelas(parcelas);

		}

	}

	public void geraNFNotaInfo() {

		// Esse valaor e setado como "teste" e depois e atualizado no m�todo
		// transmitirNfe().
		info.setIdentificador("11111111111111111111111111111111111111111111");
		// info.setIdentificador(null); // Esse atributo � setado no m�todo
		// transmitirNfe().
		info.setVersao(new java.math.BigDecimal(config.getVersao()));
		info.setIdentificacao(identificacao);
		info.setEmitente(emitente);
		info.setDestinatario(destinatario);

		this.geraPagamentos();
		// info.setPagamentos(pagamentos);
		info.setPagamento(pagamento);
		info.setCobranca(cobranca);

	}

	public void geraProdutos() {

		List<ItemNTFe> lista = this.daoItemNFe.buscaItens(nfe);

		// Usado se a configuração do imposto estiver no sistema.
		Configuracao config = (Config.propertiesLoader().getProperty("imposto").equals("1")
				? daoConfiguracao.lerPorId(new Long(1)) : null);

		NFNotaInfoInformacoesAdicionais infoAdc = new NFNotaInfoInformacoesAdicionais();

		// Se a configuração estiver no produto.
		if (Config.propertiesLoader().getProperty("imposto").equals("2")) {

			switch (lista.get(0).getProduto().getCsosn()) {
			case "101":
				if (this.nfe.getInformacoesComplementares() == null) {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.PERMITE O APROVEITAMENTO DO *CRÉDITO DE ICMS NO VALOR DE R$......; CORRESPONDENTE À *ALÍQUOTA DE...%, NOS TERMOS DO ART. 23 DA LEI COMPLEMENTAR Nº 123, DE 2006.");
				} else {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.PERMITE O APROVEITAMENTO DO *CRÉDITO DE ICMS NO VALOR DE R$......; CORRESPONDENTE À *ALÍQUOTA DE...%, NOS TERMOS DO ART. 23 DA LEI COMPLEMENTAR Nº 123, DE 2006."
									+ this.nfe.getInformacoesComplementares());
				}
				break;
			case "102":
				if (this.nfe.getInformacoesComplementares() == null) {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.;");
				} else {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.;"
									+ this.nfe.getInformacoesComplementares());
				}
				break;
			case "500":
				if (this.nfe.getInformacoesComplementares() == null) {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.");
				} else {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI."
									+ this.nfe.getInformacoesComplementares());
				}
				break;
			default:
			}

		} else if (Config.propertiesLoader().getProperty("imposto").equals("1")) {

			switch (config.getCsosn()) {
			case "101":
				if (this.nfe.getInformacoesComplementares() == null) {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.PERMITE O APROVEITAMENTO DO *CRÉDITO DE ICMS NO VALOR DE R$......; CORRESPONDENTE À *ALÍQUOTA DE...%, NOS TERMOS DO ART. 23 DA LEI COMPLEMENTAR Nº 123, DE 2006.");
				} else {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.PERMITE O APROVEITAMENTO DO *CRÉDITO DE ICMS NO VALOR DE R$......; CORRESPONDENTE À *ALÍQUOTA DE...%, NOS TERMOS DO ART. 23 DA LEI COMPLEMENTAR Nº 123, DE 2006."
									+ this.nfe.getInformacoesComplementares());
				}
				break;
			case "102":
				if (this.nfe.getInformacoesComplementares() == null) {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.;");
				} else {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.;"
									+ this.nfe.getInformacoesComplementares());
				}
				break;
			case "500":
				if (this.nfe.getInformacoesComplementares() == null) {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI.");
				} else {
					infoAdc.setInformacoesComplementaresInteresseContribuinte(
							"DOCUMENTO EMITIDO POR ME OU EPP OPTANTE PELO SIMPLES NACIONAL;NÃO GERA DIREITO A CRÉDITO FISCAL DE IPI."
									+ this.nfe.getInformacoesComplementares());
				}
				break;
			default:
			}

		}
		info.setInformacoesAdicionais(infoAdc);

		for (int i = 0; i < lista.size(); i++) {

			item.setNumeroItem(i + 1);

			produto.setCodigo(lista.get(i).getProduto().getCodigo().toString() != null
					? lista.get(i).getProduto().getCodigo().toString() : "");

			produto.setCodigoDeBarras("SEM GTIN");

			produto.setCodigoDeBarrasTributavel("SEM GTIN");

			produto.setDescricao(
					lista.get(i).getProduto().getDescricao() != null ? lista.get(i).getProduto().getDescricao() : "");

			produto.setNcm(
					lista.get(i).getProduto().getNcm() != null ? lista.get(i).getProduto().getNcm().toString() : "");

			produto.setCfop(lista.get(i).getCfop().toString() != null ? lista.get(i).getCfop().toString() : "");

			produto.setUnidadeComercial(lista.get(i).getProduto().getUnidadeComercial() != null
					? lista.get(i).getProduto().getUnidadeComercial() : "");

			produto.setUnidadeTributavel(lista.get(i).getProduto().getUnidadeComercial() != null
					? lista.get(i).getProduto().getUnidadeComercial() : "");

			produto.setQuantidadeComercial(new BigDecimal(lista.get(i).getQuantidade()) != null
					? new BigDecimal(lista.get(i).getQuantidade()) : null);

			produto.setQuantidadeTributavel(new BigDecimal(lista.get(i).getQuantidade()) != null
					? new BigDecimal(lista.get(i).getQuantidade()) : null);

			produto.setValorUnitario(lista.get(i).getProduto().getPrecoUnitario() != null
					? lista.get(i).getProduto().getPrecoUnitario() : null);

			produto.setValorUnitarioTributavel(lista.get(i).getProduto().getPrecoUnitario() != null
					? lista.get(i).getProduto().getPrecoUnitario() : null);

			produto.setValorTotalBruto(lista.get(i).getProduto().getPrecoUnitario()
					.multiply(new BigDecimal(lista.get(i).getQuantidade())));

			// Todos os produtos compoe o valor da NF-e.
			produto.setCompoeValorNota(NFProdutoCompoeValorNota.SIM);

			// Se a configura��o estiver no produto.
			if (Config.propertiesLoader().getProperty("imposto").equals("2")) {

				switch (lista.get(i).getProduto().getCsosn()) {
				case "101":

					NFNotaInfoItemImpostoICMSSN101 icmssn101 = new NFNotaInfoItemImpostoICMSSN101();
					icmssn101.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_COM_PERMISSAO_CREDITO);
					icmssn101.setOrigem(NFOrigem.NACIONAL);
					icmssn101.setPercentualAliquotaAplicavelCalculoCreditoSN(lista.get(i).getProduto().getpCredSN());
					icmssn101.setValorCreditoICMSSN(lista.get(i).getProduto().getvCredICMSSN());
					icms.setIcmssn101(icmssn101);
					imposto.setIcms(icms);

					break;
				case "102":

					NFNotaInfoItemImpostoICMSSN102 icmssn102 = new NFNotaInfoItemImpostoICMSSN102();
					icmssn102.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SEM_PERMISSAO_CREDITO);
					icmssn102.setOrigem(NFOrigem.NACIONAL);
					icms.setIcmssn102(icmssn102);
					imposto.setIcms(icms);

					break;
				case "500":

					NFNotaInfoItemImpostoICMSSN500 icmssn500 = new NFNotaInfoItemImpostoICMSSN500();
					icmssn500.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.ICMS_COBRADO_ANTERIORMENTE_POR_SUBSTITUICAO_TRIBUTARIA_SUBSIDIO_OU_POR_ANTECIPACAO);
					icmssn500.setOrigem(NFOrigem.NACIONAL);
					icmssn500.setValorBCICMSSTRetido(lista.get(i).getProduto().getvBCSTRet());
					icmssn500.setValorICMSSTRetido(lista.get(i).getProduto().getvICMSSTRet());
					icmssn500.setPercentualICMSSTRetido(lista.get(i).getProduto().getpST());
					icms.setIcmssn500(icmssn500);
					imposto.setIcms(icms);

					produto.setCodigoEspecificadorSituacaoTributaria(lista.get(i).getProduto().getCest());

					break;
				default:

				}

			} else if (Config.propertiesLoader().getProperty("imposto").equals("1")) {

				switch (config.getCsosn()) {
				case "101":

					NFNotaInfoItemImpostoICMSSN101 icmssn101 = new NFNotaInfoItemImpostoICMSSN101();
					icmssn101.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_COM_PERMISSAO_CREDITO);
					icmssn101.setOrigem(NFOrigem.NACIONAL);
					icmssn101.setPercentualAliquotaAplicavelCalculoCreditoSN(config.getpCredSN());
					icmssn101.setValorCreditoICMSSN(config.getvCredICMSSN());
					icms.setIcmssn101(icmssn101);
					imposto.setIcms(icms);

					break;
				case "102":

					NFNotaInfoItemImpostoICMSSN102 icmssn102 = new NFNotaInfoItemImpostoICMSSN102();
					icmssn102.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SEM_PERMISSAO_CREDITO);
					icmssn102.setOrigem(NFOrigem.NACIONAL);
					icms.setIcmssn102(icmssn102);
					imposto.setIcms(icms);

					break;
				case "500":

					NFNotaInfoItemImpostoICMSSN500 icmssn500 = new NFNotaInfoItemImpostoICMSSN500();
					icmssn500.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.ICMS_COBRADO_ANTERIORMENTE_POR_SUBSTITUICAO_TRIBUTARIA_SUBSIDIO_OU_POR_ANTECIPACAO);
					icmssn500.setOrigem(NFOrigem.NACIONAL);
					icmssn500.setValorBCICMSSTRetido(config.getvBCSTRet());
					icmssn500.setValorICMSSTRetido(config.getvICMSSTRet());
					icmssn500.setPercentualICMSSTRetido(config.getpST());
					icms.setIcmssn500(icmssn500);
					imposto.setIcms(icms);

					produto.setCodigoEspecificadorSituacaoTributaria(lista.get(i).getProduto().getCest());

					break;
				default:
				}

			}

			NFNotaInfoItemImpostoPISOutrasOperacoes pis = new NFNotaInfoItemImpostoPISOutrasOperacoes();
			pis.setSituacaoTributaria(NFNotaInfoSituacaoTributariaPIS.OUTRAS_OPERACOES);
			pis.setValorBaseCalculo(new java.math.BigDecimal("0.00"));
			pis.setPercentualAliquota(new java.math.BigDecimal("0.00"));
			pis.setValorTributo(new java.math.BigDecimal("0.00"));
			this.pis.setOutrasOperacoes(pis);
			imposto.setPis(this.pis);

			NFNotaInfoItemImpostoCOFINSOutrasOperacoes cofins = new NFNotaInfoItemImpostoCOFINSOutrasOperacoes();
			cofins.setSituacaoTributaria(NFNotaInfoSituacaoTributariaCOFINS.OUTRAS_OPERACOES);
			cofins.setValorBaseCalculo(new java.math.BigDecimal("0.00"));
			cofins.setPercentualCOFINS(new java.math.BigDecimal("0.00"));
			cofins.setValorCOFINS(new java.math.BigDecimal("0.00"));
			this.cofins.setOutrasOperacoes(cofins);
			imposto.setCofins(this.cofins);

			item.setProduto(produto);

			item.setImposto(imposto);

			itens.add(item);

			this.produto = new NFNotaInfoItemProduto();

			this.icms = new NFNotaInfoItemImpostoICMS();

			this.imposto = new NFNotaInfoItemImposto();

			this.ipi = new NFNotaInfoItemImpostoIPI();

			this.ipiTributado = new NFNotaInfoItemImpostoIPITributado();

			this.pisNaoTributado = new NFNotaInfoItemImpostoPISNaoTributado();

			this.pis = new NFNotaInfoItemImpostoPIS();

			this.cofinsNaoTributado = new NFNotaInfoItemImpostoCOFINSNaoTributavel();

			this.cofins = new NFNotaInfoItemImpostoCOFINS();

			this.item = new NFNotaInfoItem();

		}

	}

	public void geraICMSTotal() {

		icmsTotal.setBaseCalculoICMS(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalICMS(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorICMSDesonerado(new java.math.BigDecimal("0.00"));
		icmsTotal.setBaseCalculoICMSST(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalICMSST(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalDosProdutosServicos(this.nfe.getTotal());
		icmsTotal.setValorTotalFrete(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalSeguro(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalDesconto(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalII(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalIPI(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorPIS(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorCOFINS(new java.math.BigDecimal("0.00"));
		icmsTotal.setOutrasDespesasAcessorias(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalNFe(this.nfe.getTotal());
		// icmsTotal.setValorTotalTributos(new java.math.BigDecimal("0.00"));

		// adicionado nfe40
		icmsTotal.setValorTotalFundoCombatePobreza(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalFundoCombatePobrezaST(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalFundoCombatePobrezaSTRetido(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalIPIDevolvido(new java.math.BigDecimal("0.00"));
		// adicionado nfe40

		total.setIcmsTotal(icmsTotal);

		info.setTotal(total);

	}

	public String transmitirNFe() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		this.nfe = this.daoNFe.lerPorId(codigo);

		try {

			List<NFNota> lstNF = new ArrayList<NFNota>();

			this.geraEmitenteEndereco();

			this.geraEmitente();

			this.geraDestinatarioEndereco();

			this.geraDestinatario();

			this.geraNFNotaInfoIdentificacao();

			this.geraNFNotaInfo();

			this.geraProdutos();

			this.geraICMSTotal();

			info.setItens(itens);

			NFNotaInfoTransporte transporte = new NFNotaInfoTransporte();
			transporte.setModalidadeFrete(this.nfe.getNfModalidadeFrete());

			List<NFNotaInfoVolume> vs = new ArrayList<NFNotaInfoVolume>();

			NFNotaInfoVolume vl = new NFNotaInfoVolume();
			vl.setPesoLiquido(new java.math.BigDecimal("0.00"));
			vl.setPesoBruto(new java.math.BigDecimal("0.00"));

			vs.add(vl);

			transporte.setVolumes(vs);

			info.setTransporte(transporte);

			// nota.setIdentificadorLocal(3106200);
			nota.setInfo(info);

			lstNF.add(nota);
			lote.setNotas(lstNF);

			lote.setIdLote(Integer.toString(this.nfe.getNumero())); // VERIFICAR
			lote.setVersao(config.getVersao());
			lote.setIndicadorProcessamento(NFLoteIndicadorProcessamento.PROCESSAMENTO_ASSINCRONO);

			// Gabiarra para gerar o c�digo Randomico e o Indentificador.
			NFGeraChave nfGeraChave = new NFGeraChave(nota);

			// Mostrando os valores gerados pela classe NFGeraChave.
			/*
			 * System.out.println(
			 * "----- Valores gerados pela classe NFGeraChave ----- ");
			 * System.out.println(
			 * "C�digo Randomico gerado pela classe NFGeraChave: " +
			 * nfGeraChave.geraCodigoRandomico()); System.out.println(
			 * "Identificador gerado pela classe NFGeraChave: " +
			 * nfGeraChave.getChaveAcesso());
			 */

			// Mostrando os valores de teste dos objetos.
			/*
			 * System.out.println("----- Valores de teste dos objetos ----- ");
			 * System.out.println("C�digo Randomico com valor de teste: " +
			 * identificacao.getCodigoRandomico()); System.out.println(
			 * "Identificador com valor de teste: " + info.getIdentificador());
			 */

			// Seteando os valores gerados pela classe NFGeraChave.
			identificacao.setCodigoRandomico(nfGeraChave.geraCodigoRandomico());
			info.setIdentificador(nfGeraChave.getChaveAcesso());

			// Mostrando os valores atualizados.
			/*
			 * System.out.println("----- Valores atualizados ----- ");
			 * System.out.println("C�digo Randomico com valor atualizado: " +
			 * identificacao.getCodigoRandomico()); System.out.println(
			 * "Identificador com valor atualizado: " +
			 * info.getIdentificador());
			 */

			// ENVIO DO LOTE GERADO PELO CODIGO ACIMA.
			NFLoteEnvioRetornoDados retorno = null;
			try {

				System.out.println(" ");

				System.out.println("ENVIANDO LOTE...");

				System.out.println(" ");

				System.out.println("----------DADOS CONTIDOS NO LOTE INICIO-----------");

				System.out.println(" ");

				System.out.println("Numero de NF no lote: " + lote.getNotas().size());

				System.out.println("Chave: " + lote.getNotas().get(0).getInfo().getChaveAcesso());
				System.out.println("Ambiente: " + lote.getNotas().get(0).getInfo().getIdentificacao().getAmbiente());
				System.out.println(
						"Data Hr Emissao: " + lote.getNotas().get(0).getInfo().getIdentificacao().getDataHoraEmissao());
				System.out.println("Numero NF: " + lote.getNotas().get(0).getInfo().getIdentificacao().getNumeroNota());
				System.out.println("Serie NF: " + lote.getNotas().get(0).getInfo().getIdentificacao().getSerie());

				System.out.println(" ");

				System.out.println("----------DADOS AP�S TRANSMISSAO DA NF-e FIM--------------");

				retorno = new WSFacade(config).enviaLote(lote);

				System.out.println(" ");

				System.out.println("LOTE ENVIADO!");

				System.out.println(" ");

			} catch (KeyManagementException e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			} catch (KeyStoreException e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			} catch (CertificateException e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			} catch (IOException e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			} catch (Exception e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemFatal(null, e.getMessage(), null);
			}

			// RETORNO DAS INFORMA��ES GERADAS AP�S TRANSMISS�O DA NF-E.
			try {

				NFLoteConsultaRetorno retc = new WSFacade(config)
						.consultaLote(retorno.getRetorno().getInfoRecebimento().getRecibo(), DFModelo.NFE);

				for (NFProtocolo prot : retc.getProtocolos()) {

					System.out.println("----------DADOS AP�S TRANSMISSAO DA NF-e INICIO-----------");
					System.out.println(" ");
					System.out.println("Chave: " + prot.getProtocoloInfo().getChave());
					System.out.println("N�mero Protocolo: " + prot.getProtocoloInfo().getNumeroProtocolo());
					System.out.println("Status: " + prot.getProtocoloInfo().getStatus());
					System.out.println("Motivo: " + prot.getProtocoloInfo().getMotivo());
					System.out.println("Ambiente: " + prot.getProtocoloInfo().getAmbiente().getCodigo());
					System.out.println("DataRecebimento: " + prot.getProtocoloInfo().getDataRecebimento());
					System.out.println(" ");
					System.out.println("----------DADOS AP�S TRANSMISSAO DA NF-e FIM--------------");

					// ATUALIZA A NFE COM AS INFORMA��ES AP�S TRANSMISS�O.
					this.nfe.setChave(prot.getProtocoloInfo().getChave());
					this.nfe.setNumProtocolo(prot.getProtocoloInfo().getNumeroProtocolo());
					this.nfe.setStatus(prot.getProtocoloInfo().getStatus());
					this.nfe.setRejeicaoMotivo(prot.getProtocoloInfo().getMotivo());
					this.nfe.setAmbienteRecebimento(prot.getProtocoloInfo().getAmbiente().getCodigo());
					this.nfe.setDataRecebimento(
							java.sql.Timestamp.valueOf(prot.getProtocoloInfo().getDataRecebimento()));

					this.daoNFe.merge(this.nfe);

					// Fonte: https://github.com/wmixvideo/nfe/issues/153
					// Armanezenando o Xml da nota autorizada ou processada.
					NFNotaProcessada notaProcessada = new NFNotaProcessada();
					notaProcessada.setNota(lote.getNotas().get(0));
					notaProcessada.setProtocolo(prot);
					notaProcessada.setVersao(new java.math.BigDecimal(config.getVersao()));
					String xml = notaProcessada.toString();

					System.out.println(" ");

					System.out.println("----------XML DA NF-e INICIO-----------");
					System.out.println(" ");
					System.out.println(xml);
					System.out.println(" ");
					System.out.println("----------XML DA NF-e FIM--------------");

					// O sistema s� dever� armazernar o Xml da nota, se a nota
					// for autorizada pela SEFAZ.
					if (nfe.getStatus().equals("100")) {

						try {

							XmlUtil.armazernarXmlLocalNfe(xml, prot.getProtocoloInfo().getChave());

							JSFUtil.retornarMensagemInfo(null, "Autorizado o uso da NF-e.", null);

							return "/NFe/nfeTransmitir";

						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TransformerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {

						JSFUtil.retornarMensagemErro(null, "Rejei��o. O XML n�o ser� armazenado.", null);

						return "/NFe/nfeTransmitir";

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(null, "Verifique a configura��o dos impostos", null);

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
		}

		return "/NFe/nfeTransmitir";

	}

	public NTFe getNfe() {
		return nfe;
	}

	public void setNfe(NTFe nfe) {
		this.nfe = nfe;
	}

	public NFeDAO getDaoNFe() {
		return daoNFe;
	}

	public void setDaoNFe(NFeDAO daoNFe) {
		this.daoNFe = daoNFe;
	}

	public ItemNFeDAO getDaoItemNFe() {
		return daoItemNFe;
	}

	public void setDaoItemNFe(ItemNFeDAO daoItemNFe) {
		this.daoItemNFe = daoItemNFe;
	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

	public NFNotaInfoEmitente getEmitente() {
		return emitente;
	}

	public void setEmitente(NFNotaInfoEmitente emitente) {
		this.emitente = emitente;
	}

	public NFEndereco getEmitenteEndereco() {
		return emitenteEndereco;
	}

	public void setEmitenteEndereco(NFEndereco emitenteEndereco) {
		this.emitenteEndereco = emitenteEndereco;
	}

	public NFNotaInfoDestinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(NFNotaInfoDestinatario destinatario) {
		this.destinatario = destinatario;
	}

	public NFEndereco getDestinatarioEndereco() {
		return destinatarioEndereco;
	}

	public void setDestinatarioEndereco(NFEndereco destinatarioEndereco) {
		this.destinatarioEndereco = destinatarioEndereco;
	}

	public NFNotaInfoIdentificacao getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(NFNotaInfoIdentificacao identificacao) {
		this.identificacao = identificacao;
	}

	public NFNotaInfo getInfo() {
		return info;
	}

	public void setInfo(NFNotaInfo info) {
		this.info = info;
	}

	public NFNota getNota() {
		return nota;
	}

	public void setNota(NFNota nota) {
		this.nota = nota;
	}

	public NFNotaInfoItemProduto getProduto() {
		return produto;
	}

	public void setProduto(NFNotaInfoItemProduto produto) {
		this.produto = produto;
	}

	public NFNotaInfoItem getItem() {
		return item;
	}

	public void setItem(NFNotaInfoItem item) {
		this.item = item;
	}

	public NFNotaInfoItemImposto getImposto() {
		return imposto;
	}

	public void setImposto(NFNotaInfoItemImposto imposto) {
		this.imposto = imposto;
	}

	public NFNotaInfoItemImpostoICMS getIcms() {
		return icms;
	}

	public void setIcms(NFNotaInfoItemImpostoICMS icms) {
		this.icms = icms;
	}

	public NFNotaInfoItemImpostoIPI getIpi() {
		return ipi;
	}

	public void setIpi(NFNotaInfoItemImpostoIPI ipi) {
		this.ipi = ipi;
	}

	public NFNotaInfoItemImpostoIPITributado getIpiTributado() {
		return ipiTributado;
	}

	public void setIpiTributado(NFNotaInfoItemImpostoIPITributado ipiTributado) {
		this.ipiTributado = ipiTributado;
	}

	public NFNotaInfoItemImpostoPIS getPis() {
		return pis;
	}

	public void setPis(NFNotaInfoItemImpostoPIS pis) {
		this.pis = pis;
	}

	public NFNotaInfoItemImpostoPISNaoTributado getPisNaoTributado() {
		return pisNaoTributado;
	}

	public void setPisNaoTributado(NFNotaInfoItemImpostoPISNaoTributado pisNaoTributado) {
		this.pisNaoTributado = pisNaoTributado;
	}

	public NFNotaInfoItemImpostoCOFINSNaoTributavel getCofinsNaoTributado() {
		return cofinsNaoTributado;
	}

	public void setCofinsNaoTributado(NFNotaInfoItemImpostoCOFINSNaoTributavel cofinsNaoTributado) {
		this.cofinsNaoTributado = cofinsNaoTributado;
	}

	public NFNotaInfoItemImpostoCOFINS getCofins() {
		return cofins;
	}

	public void setCofins(NFNotaInfoItemImpostoCOFINS cofins) {
		this.cofins = cofins;
	}

	public NFNotaInfoICMSTotal getIcmsTotal() {
		return icmsTotal;
	}

	public void setIcmsTotal(NFNotaInfoICMSTotal icmsTotal) {
		this.icmsTotal = icmsTotal;
	}

	public NFNotaInfoTotal getTotal() {
		return total;
	}

	public void setTotal(NFNotaInfoTotal total) {
		this.total = total;
	}

	public NFLoteEnvio getLote() {
		return lote;
	}

	public void setLote(NFLoteEnvio lote) {
		this.lote = lote;
	}

	public List<NFNotaInfoItem> getItens() {
		return itens;
	}

	public void setItens(List<NFNotaInfoItem> itens) {
		this.itens = itens;
	}

	public List<ItemNTFe> getItensNTFe() {
		return itensNTFe;
	}

	public void setItensNTFe(List<ItemNTFe> itensNTFe) {
		this.itensNTFe = itensNTFe;
	}

	public String getStatusSefaz() {
		return statusSefaz;
	}

	public void setStatusSefaz(String statusSefaz) {
		this.statusSefaz = statusSefaz;
	}

	public List<NTFe> getNotasParaTransmitir() {

		if (this.notasParaTransmitir == null) {

			this.daoNFe = new NFeDAO();
			this.notasParaTransmitir = this.daoNFe.buscaNotasNaoTramitidas();

		}

		return notasParaTransmitir;
	}

	public void setNotasParaTransmitir(List<NTFe> notasParaTransmitir) {
		this.notasParaTransmitir = notasParaTransmitir;
	}

	public List<NTFe> getNotasParaTransmitirFiltrados() {
		return notasParaTransmitirFiltrados;
	}

	public void setNotasParaTransmitirFiltrados(List<NTFe> notasParaTransmitirFiltrados) {
		this.notasParaTransmitirFiltrados = notasParaTransmitirFiltrados;
	}

	public NFNotaInfoPagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(NFNotaInfoPagamento pagamento) {
		this.pagamento = pagamento;
	}

	public List<NFNotaInfoPagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<NFNotaInfoPagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public ConfiguracaoDAO getDaoConfiguracao() {
		return daoConfiguracao;
	}

	public void setDaoConfiguracao(ConfiguracaoDAO daoConfiguracao) {
		this.daoConfiguracao = daoConfiguracao;
	}

	public String getAmbienteConfigurado() {
		return ambienteConfigurado;
	}

	public void setAmbienteConfigurado(String ambienteConfigurado) {
		this.ambienteConfigurado = ambienteConfigurado;
	}

	public NFNotaInfoCobranca getCobranca() {
		return cobranca;
	}

	public void setCobranca(NFNotaInfoCobranca cobranca) {
		this.cobranca = cobranca;
	}

	public NFNotaInfoFatura getFatura() {
		return fatura;
	}

	public void setFatura(NFNotaInfoFatura fatura) {
		this.fatura = fatura;
	}

	public NFNotaInfoParcela getParcela() {
		return parcela;
	}

	public void setParcela(NFNotaInfoParcela parcela) {
		this.parcela = parcela;
	}

	public List<NFNotaInfoParcela> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<NFNotaInfoParcela> parcelas) {
		this.parcelas = parcelas;
	}

	public EstadoDAO getDaoEstado() {
		return daoEstado;
	}

	public void setDaoEstado(EstadoDAO daoEstado) {
		this.daoEstado = daoEstado;
	}

	public MunicipioDAO getDaoMunicipio() {
		return daoMunicipio;
	}

	public void setDaoMunicipio(MunicipioDAO daoMunicipio) {
		this.daoMunicipio = daoMunicipio;
	}

}
