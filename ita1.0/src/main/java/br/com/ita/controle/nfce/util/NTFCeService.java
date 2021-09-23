package br.com.ita.controle.nfce.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.fincatto.documentofiscal.DFModelo;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFTipoEmissao;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFEndereco;
import com.fincatto.documentofiscal.nfe400.classes.NFFinalidade;
import com.fincatto.documentofiscal.nfe400.classes.NFIndicadorFormaPagamento;
import com.fincatto.documentofiscal.nfe400.classes.NFModalidadeFrete;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoImpostoTributacaoICMS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFProcessoEmissor;
import com.fincatto.documentofiscal.nfe400.classes.NFProdutoCompoeValorNota;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;
import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.NFTipoImpressao;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvio;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteIndicadorProcessamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIdentificadorLocalDestinoOperacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorPresencaComprador;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFMeioPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoEmitente;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoFormaPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoICMSTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoIdentificacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItem;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImposto;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSNaoTributavel;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS00;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS10;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS20;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS30;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS40;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS51;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS60;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS70;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS90;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSPartilhado;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN101;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN102;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN201;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN202;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN500;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSSN900;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMSST;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoIPI;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoIPITributado;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPIS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPISNaoTributado;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoPISOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemProduto;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoPagamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoSuplementar;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTransporte;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFOperacaoConsumidorFinal;
import com.fincatto.documentofiscal.nfe400.classes.nota.consulta.NFNotaConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.utils.NFGeraChave;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;
import com.fincatto.documentofiscal.utils.DFAssinaturaDigital;
import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.ita.dominio.ItemNTFCe;
import br.com.ita.dominio.NTFCe;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.dao.imposto.ImpostoDAO;
import br.com.ita.dominio.empresa.Empresa;
import br.com.ita.dominio.imposto.Imposto;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

public class NTFCeService implements Serializable {

	private static final long serialVersionUID = 1L;

	private NFeConfigIta config = new NFeConfigIta();

	private NFEndereco emitenteEndereco = new NFEndereco();

	private NFNotaInfoEmitente emitente = new NFNotaInfoEmitente();

	private NFEndereco destinatarioEndereco = new NFEndereco();

	private NFNotaInfoDestinatario destinatario = new NFNotaInfoDestinatario();

	private NFNotaInfoIdentificacao identificacao = new NFNotaInfoIdentificacao();

	private NFNotaInfoTransporte transporte = new NFNotaInfoTransporte();

	private NFNotaInfo info = new NFNotaInfo();

	private NFNota nota = new NFNota();

	private NFNotaInfoItem item = new NFNotaInfoItem();

	private NFNotaInfoItemImposto imposto = new NFNotaInfoItemImposto();

	private NFNotaInfoItemImpostoICMS icms = new NFNotaInfoItemImpostoICMS();

	private NFNotaInfoItemImpostoICMSSN500 icmssn500 = new NFNotaInfoItemImpostoICMSSN500();

	private NFNotaInfoItemProduto produto = new NFNotaInfoItemProduto();

	private NFNotaInfoItemImpostoIPI ipi = new NFNotaInfoItemImpostoIPI();

	private NFNotaInfoItemImpostoIPITributado ipiTributado = new NFNotaInfoItemImpostoIPITributado();

	private NFNotaInfoItemImpostoPIS pis = new NFNotaInfoItemImpostoPIS();

	private NFNotaInfoItemImpostoPISNaoTributado pisNaoTributado = new NFNotaInfoItemImpostoPISNaoTributado();

	private NFNotaInfoItemImpostoCOFINSNaoTributavel cofinsNaoTributado = new NFNotaInfoItemImpostoCOFINSNaoTributavel();

	private NFNotaInfoItemImpostoCOFINS cofins = new NFNotaInfoItemImpostoCOFINS();

	private List<NFNotaInfoItem> itens = new ArrayList<NFNotaInfoItem>();

	private NFNotaInfoICMSTotal icmsTotal = new NFNotaInfoICMSTotal();

	private NFNotaInfoTotal total = new NFNotaInfoTotal();

	private NFLoteEnvio lote = new NFLoteEnvio();

	private NTFCe nfce = new NTFCe();

	private List<ItemNTFCe> itensNfce = new ArrayList<ItemNTFCe>();

	private NFNotaInfoPagamento pagamento = new NFNotaInfoPagamento();

	private List<NFNotaInfoPagamento> pagamentos = new ArrayList<NFNotaInfoPagamento>();

	private EstadoDAO daoEstado = new EstadoDAO();

	private MunicipioDAO daoMunicipio = new MunicipioDAO();

	private Empresa empresa = new Empresa();

	private Configuracao configuracao = new Configuracao();

	public NTFCeService(NTFCe nfce, List<ItemNTFCe> itensNfce, Empresa empresa, Configuracao configuracao) {
		this.setNfce(nfce);
		this.setItensNfce(itensNfce);
		this.setEmpresa(empresa);
		this.setConfiguracao(configuracao);
	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

	public NFEndereco getEmitenteEndereco() {
		return emitenteEndereco;
	}

	public void setEmitenteEndereco(NFEndereco emitenteEndereco) {
		this.emitenteEndereco = emitenteEndereco;
	}

	public NFNotaInfoEmitente getEmitente() {
		return emitente;
	}

	public void setEmitente(NFNotaInfoEmitente emitente) {
		this.emitente = emitente;
	}

	public NFEndereco getDestinatarioEndereco() {
		return destinatarioEndereco;
	}

	public void setDestinatarioEndereco(NFEndereco destinatarioEndereco) {
		this.destinatarioEndereco = destinatarioEndereco;
	}

	public NFNotaInfoDestinatario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(NFNotaInfoDestinatario destinatario) {
		this.destinatario = destinatario;
	}

	public NFNotaInfoIdentificacao getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(NFNotaInfoIdentificacao identificacao) {
		this.identificacao = identificacao;
	}

	public NFNotaInfoTransporte getTransporte() {
		return transporte;
	}

	public void setTransporte(NFNotaInfoTransporte transporte) {
		this.transporte = transporte;
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

	public NFNotaInfoItemImpostoICMSSN500 getIcmssn500() {
		return icmssn500;
	}

	public void setIcmssn500(NFNotaInfoItemImpostoICMSSN500 icmssn500) {
		this.icmssn500 = icmssn500;
	}

	public NFNotaInfoItemProduto getProduto() {
		return produto;
	}

	public void setProduto(NFNotaInfoItemProduto produto) {
		this.produto = produto;
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

	public List<NFNotaInfoItem> getItens() {
		return itens;
	}

	public void setItens(List<NFNotaInfoItem> itens) {
		this.itens = itens;
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

	public NTFCe getNfce() {
		return nfce;
	}

	public void setNfce(NTFCe nfce) {
		this.nfce = nfce;
	}

	public List<ItemNTFCe> getItensNfce() {
		return itensNfce;
	}

	public void setItensNfce(List<ItemNTFCe> itensNfce) {
		this.itensNfce = itensNfce;
	}

	public NFNotaInfoPagamento getPagamento() {
		return pagamento;
	}

	public List<NFNotaInfoPagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamento(NFNotaInfoPagamento pagamento) {
		this.pagamento = pagamento;
	}

	public void setPagamentos(List<NFNotaInfoPagamento> pagamentos) {
		this.pagamentos = pagamentos;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

	public String gerarCodigoRandomico() {

		Random random = new Random();
		int i = random.nextInt(99999999) * 1;
		String cnf = StringUtils.leftPad(String.valueOf(i), 8, "0");

		return cnf;
	}

	public void geraEmitenteEndereco() {

		emitenteEndereco.setLogradouro(empresa.getEndereco());
		emitenteEndereco.setNumero(empresa.getNumero());
		emitenteEndereco.setComplemento(empresa.getComplemento());
		emitenteEndereco.setBairro(empresa.getBairro());
		emitenteEndereco.setCodigoMunicipio(empresa.getCodigoMunicipio());
		emitenteEndereco.setDescricaoMunicipio(empresa.getDescricaoMunicipio());
		emitenteEndereco.setUf(DFUnidadeFederativa.valueOfCodigo(empresa.getUf()));
		emitenteEndereco.setCep(empresa.getCep());
		emitenteEndereco.setCodigoPais(empresa.getCodigoPais());
		emitenteEndereco.setDescricaoPais(empresa.getDescricaoPais());
		emitenteEndereco.setTelefone(empresa.getTelefone());

	}

	public void geraEmitente() {

		emitente.setCnpj(empresa.getCnpj());
		emitente.setRazaoSocial(empresa.getRazaoSocial());
		emitente.setNomeFantasia(empresa.getNomeFantasia());
		emitente.setEndereco(emitenteEndereco);
		emitente.setInscricaoEstadual(empresa.getInscricaoEstadual());
		emitente.setRegimeTributario(NFRegimeTributario.SIMPLES_NACIONAL);

	}

	public void geraDestinatarioEndereco() {

		if (nfce.getCliente() != null) {

			destinatarioEndereco.setLogradouro(nfce.getCliente().getEndereco());
			destinatarioEndereco.setNumero(nfce.getCliente().getNumero());
			destinatarioEndereco.setComplemento(nfce.getCliente().getComplemento());
			destinatarioEndereco.setBairro(nfce.getCliente().getBairro());
			destinatarioEndereco.setCodigoMunicipio(nfce.getCliente().getMunicipio().getCodigo().toString());
			destinatarioEndereco.setDescricaoMunicipio(nfce.getCliente().getMunicipio().getNome());
			destinatarioEndereco.setUf(DFUnidadeFederativa.valueOfCodigo(nfce.getCliente().getEstado().getUf()));
			destinatarioEndereco.setCep(nfce.getCliente().getCep());
			destinatarioEndereco.setCodigoPais("1058");
			destinatarioEndereco.setDescricaoPais("BRASIL");
			destinatarioEndereco.setTelefone(nfce.getCliente().getTelefone());

		}

	}

	public void geraDestinatario() {

		if (nfce.getCliente() != null) {

			if (nfce.getCliente().getTipo().equals("F")) {
				destinatario.setCpf(nfce.getCliente().getCgc());
			} else if (nfce.getCliente().getTipo().equals("J")) {
				destinatario.setCnpj(nfce.getCliente().getCgc());
			}

			if (!nfce.getCliente().getInscEst().isEmpty()) {
				destinatario.setInscricaoEstadual(nfce.getCliente().getInscEst());
			}

			destinatario.setRazaoSocial(nfce.getCliente().getNome());
			destinatario.setEndereco(destinatarioEndereco);
			destinatario.setIndicadorIEDestinatario(nfce.getCliente().getNfIndicadorIEDestinatario());
			destinatario.setEmail(nfce.getCliente().getEmail());

		}
	}

	public void geraNFNotaInfoIdentificacao() {

		identificacao.setUf(DFUnidadeFederativa.valueOfCodigo(empresa.getUf()));
		identificacao.setCodigoRandomico(this.gerarCodigoRandomico());

		// VERIFICAR SE SEMPRE SERA VENDA.
		identificacao.setNaturezaOperacao("VENDA");

		// Removido 20180913 ao atualizar nfe40
		// identificacao.setFormaPagamento(NFFormaPagamentoPrazo.A_VISTA);

		identificacao.setModelo(DFModelo.NFCE);
		identificacao.setSerie(Integer.toString(nfce.getSerie()));
		identificacao.setNumeroNota(Integer.toString(nfce.getNumero()));
		identificacao.setDataHoraEmissao(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
		identificacao.setTipo(NFTipo.SAIDA);
		identificacao.setIdentificadorLocalDestinoOperacao(NFIdentificadorLocalDestinoOperacao.OPERACAO_INTERNA);
		identificacao.setCodigoMunicipio(empresa.getCodigoMunicipio());
		identificacao.setTipoImpressao(NFTipoImpressao.DANFE_NFCE);
		identificacao.setTipoEmissao(NFTipoEmissao.EMISSAO_NORMAL);
		identificacao.setDigitoVerificador(1);
		identificacao.setAmbiente(config.getAmbiente());
		identificacao.setFinalidade(NFFinalidade.NORMAL);
		identificacao.setOperacaoConsumidorFinal(NFOperacaoConsumidorFinal.SIM);
		identificacao.setIndicadorPresencaComprador(NFIndicadorPresencaComprador.OPERACAO_PRESENCIAL);
		identificacao.setProgramaEmissor(NFProcessoEmissor.CONTRIBUINTE);
		identificacao.setVersaoEmissor("1.0");

	}

	public void geraTransporte() {

		transporte.setModalidadeFrete(NFModalidadeFrete.SEM_OCORRENCIA_TRANSPORTE);

	}

	public void geraPagamentos() {

		/*
		 * NFNotaInfoFormaPagamento fpgto = new NFNotaInfoFormaPagamento();
		 * fpgto.setMeioPagamento(NFMeioPagamento.DINHEIRO); fpgto.setValorPagamento(new
		 * java.math.BigDecimal("0.00"));
		 * 
		 * List<NFNotaInfoFormaPagamento> lsFpgto = new
		 * ArrayList<NFNotaInfoFormaPagamento>(); lsFpgto.add(fpgto);
		 * 
		 * pagamento.setDetalhamentoFormasPagamento(lsFpgto); pagamentos.add(pagamento);
		 */

		NFNotaInfoFormaPagamento fpgto = new NFNotaInfoFormaPagamento();

		switch (this.nfce.getCondicaoPagamento().getFormaPagamento()) {
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

		switch (this.nfce.getCondicaoPagamento().getMeioPagamento()) {
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

		fpgto.setValorPagamento(this.nfce.getValorPagamento());
		List<NFNotaInfoFormaPagamento> lsFpgto = new ArrayList<NFNotaInfoFormaPagamento>();
		lsFpgto.add(fpgto);

		pagamento.setDetalhamentoFormasPagamento(lsFpgto);
		pagamento.setValorTroco(this.nfce.getValorPagamento().subtract(this.nfce.getTotal()));
		pagamentos.add(pagamento);

	}

	public void geraNFNotaInfo() {

		info.setIdentificador("11111111111111111111111111111111111111111111");
		info.setVersao(new java.math.BigDecimal(config.getVersao()));
		info.setIdentificacao(identificacao);
		info.setTransporte(transporte);
		info.setEmitente(emitente);

		if (nfce.getCliente() != null) {
			info.setDestinatario(destinatario);
		}

		this.geraPagamentos();

		// info.setPagamentos(pagamentos);
		info.setPagamento(pagamento);

	}

	public void geraProdutos() {

		for (int i = 0; i < itensNfce.size(); i++) {

			item.setNumeroItem(i + 1);

			produto.setCodigo(itensNfce.get(i).getProduto().getCodigo().toString() != null
					? itensNfce.get(i).getProduto().getCodigo().toString()
					: "");

			produto.setCodigoDeBarras("SEM GTIN");

			produto.setCodigoDeBarrasTributavel("SEM GTIN");

			produto.setDescricao(
					itensNfce.get(i).getProduto().getDescricao() != null ? itensNfce.get(i).getProduto().getDescricao()
							: "");

			produto.setNcm(
					itensNfce.get(i).getProduto().getNcm() != null ? itensNfce.get(i).getProduto().getNcm().toString()
							: "");

			// produto.setCfop(itensNfce.get(i).getCfop().toString() != null ?
			// itensNfce.get(i).getCfop().toString() : "");

			produto.setCfop("5102");

			produto.setUnidadeComercial(itensNfce.get(i).getProduto().getUnidadeComercial() != null
					? itensNfce.get(i).getProduto().getUnidadeComercial()
					: "");

			produto.setUnidadeTributavel(itensNfce.get(i).getProduto().getUnidadeComercial() != null
					? itensNfce.get(i).getProduto().getUnidadeComercial()
					: "");

			produto.setQuantidadeComercial(new BigDecimal(itensNfce.get(i).getQuantidade()) != null
					? new BigDecimal(itensNfce.get(i).getQuantidade())
					: null);

			produto.setQuantidadeTributavel(new BigDecimal(itensNfce.get(i).getQuantidade()) != null
					? new BigDecimal(itensNfce.get(i).getQuantidade())
					: null);

			produto.setValorUnitario(itensNfce.get(i).getProduto().getPrecoUnitario() != null
					? itensNfce.get(i).getProduto().getPrecoUnitario()
					: null);

			produto.setValorUnitarioTributavel(itensNfce.get(i).getProduto().getPrecoUnitario() != null
					? itensNfce.get(i).getProduto().getPrecoUnitario()
					: null);

			produto.setValorTotalBruto(itensNfce.get(i).getProduto().getPrecoUnitario()
					.multiply(new BigDecimal(itensNfce.get(i).getQuantidade())));

			produto.setCompoeValorNota(NFProdutoCompoeValorNota.SIM);

			// PEGA O UNICO CADASTRO DE IMPOSTO.
			// O CADASTRO DE IMPOSTO NAO PERMITI MAIS DE UM REGISTRO.
			ImpostoDAO impostoDao = new ImpostoDAO();
			List<Imposto> objetoDoBanco = impostoDao.lerTodos();
			Imposto nfe = objetoDoBanco.get(0);

			if (nfe.getICMS() != null)
				switch (nfe.getICMS()) {
				case ICMS00:

					NFNotaInfoItemImpostoICMS00 icms00 = new NFNotaInfoItemImpostoICMS00();
					icms00.setModalidadeBCICMS(nfe.getModalidadeBCICMS());
					icms00.setOrigem(nfe.getOrigem());

					icms00.setPercentualAliquota(nfe.getPercentualAliquota().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquota()));

					icms00.setPercentualFundoCombatePobreza(nfe.getPercentualFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobreza()));

					icms00.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms00.setValorBaseCalculo(
							nfe.getValorBaseCalculo().equals("") ? null : new BigDecimal(nfe.getValorBaseCalculo()));

					icms00.setValorFundoCombatePobreza(nfe.getValorFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobreza()));

					icms00.setValorTributo(
							nfe.getValorTributo().equals("") ? null : new BigDecimal(nfe.getValorTributo()));

					icms.setIcms00(icms00);
					imposto.setIcms(icms);

					break;

				case ICMS10:

					NFNotaInfoItemImpostoICMS10 icms10 = new NFNotaInfoItemImpostoICMS10();

					icms10.setModalidadeBCICMS(nfe.getModalidadeBCICMS());

					icms10.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());

					icms10.setOrigem(nfe.getOrigem());

					icms10.setPercentualAliquota(nfe.getPercentualAliquota().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquota()));

					icms10.setPercentualAliquotaImpostoICMSST(nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icms10.setPercentualFundoCombatePobreza(nfe.getPercentualFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobreza()));

					icms10.setPercentualFundoCombatePobrezaST(nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

					icms10.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icms10.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icms10.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms10.setValorBaseCalculo(
							nfe.getValorBaseCalculo().equals("") ? null : new BigDecimal(nfe.getValorBaseCalculo()));

					icms10.setValorBaseCalculoFundoCombatePobreza(
							nfe.getValorBaseCalculoFundoCombatePobreza().equals("") ? null
									: new BigDecimal(nfe.getValorBaseCalculoFundoCombatePobreza()));

					icms10.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

					icms10.setValorBCICMSST(
							nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

					icms10.setValorFundoCombatePobreza(nfe.getValorFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobreza()));

					icms10.setValorFundoCombatePobrezaST(null);

					icms10.setValorICMSST(
							nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

					icms10.setValorTributo(
							nfe.getValorTributo().equals("") ? null : new BigDecimal(nfe.getValorTributo()));

					icms.setIcms10(icms10);
					imposto.setIcms(icms);

					break;

				case ICMS20:

					NFNotaInfoItemImpostoICMS20 icms20 = new NFNotaInfoItemImpostoICMS20();

					icms20.setDesoneracao(nfe.getDesoneracao());

					icms20.setModalidadeBCICMS(nfe.getModalidadeBCICMS());

					icms20.setOrigem(nfe.getOrigem());

					icms20.setPercentualAliquota(nfe.getPercentualAliquota().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquota()));

					icms20.setPercentualFundoCombatePobreza(nfe.getPercentualFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobreza()));

					icms20.setPercentualReducaoBC(nfe.getPercentualReducaoBC().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBC()));

					icms20.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms20.setValorBCFundoCombatePobreza(nfe.getValorBCFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobreza()));

					icms20.setValorBCICMS(
							nfe.getValorBCICMS().equals("") ? null : new BigDecimal(nfe.getValorBCICMS()));

					icms20.setValorFundoCombatePobreza(nfe.getValorFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobreza()));

					icms20.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
							: new BigDecimal(nfe.getValorICMSDesoneracao()));

					icms20.setValorTributo(
							nfe.getValorTributo().equals("") ? null : new BigDecimal(nfe.getValorTributo()));

					icms.setIcms20(icms20);
					imposto.setIcms(icms);

					break;

				case ICMS30:

					NFNotaInfoItemImpostoICMS30 icms30 = new NFNotaInfoItemImpostoICMS30();

					icms30.setDesoneracao(nfe.getDesoneracao());

					icms30.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());

					icms30.setOrigem(nfe.getOrigem());

					icms30.setPercentualAliquotaImpostoICMSST(nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icms30.setPercentualFundoCombatePobrezaST(nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

					icms30.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icms30.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icms30.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms30.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

					icms30.setValorBCICMSST(
							nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

					icms30.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

					icms30.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
							: new BigDecimal(nfe.getValorICMSDesoneracao()));

					icms30.setValorImpostoICMSST(nfe.getValorImpostoICMSST().equals("") ? null
							: new BigDecimal(nfe.getValorImpostoICMSST()));

					icms.setIcms30(icms30);
					imposto.setIcms(icms);

					break;

				case ICMS40:

					// 202 N06 ICMS40 Grupo Tributação ICMS = 40, 41, 50 CG N01 1-1 Tributação
					// Isenta, Não tributada ou Suspensão

					// ICMS 40
					if (nfe.getSituacaoTributaria() == NFNotaInfoImpostoTributacaoICMS.ISENTA) {

						NFNotaInfoItemImpostoICMS40 icms40 = new NFNotaInfoItemImpostoICMS40();

						icms40.setMotivoDesoneracaoICMS(nfe.getMotivoDesoneracaoICMS());
						icms40.setOrigem(nfe.getOrigem());
						icms40.setSituacaoTributaria(NFNotaInfoImpostoTributacaoICMS.ISENTA);
						icms40.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
								: new BigDecimal(nfe.getValorICMSDesoneracao()));

						icms.setIcms40(icms40);
						imposto.setIcms(icms);

					}

					// ICMS 41
					if (nfe.getSituacaoTributaria() == NFNotaInfoImpostoTributacaoICMS.NAO_TRIBUTADO) {

						NFNotaInfoItemImpostoICMS40 icms41 = new NFNotaInfoItemImpostoICMS40();

						icms41.setMotivoDesoneracaoICMS(nfe.getMotivoDesoneracaoICMS());
						icms41.setOrigem(nfe.getOrigem());
						icms41.setSituacaoTributaria(NFNotaInfoImpostoTributacaoICMS.NAO_TRIBUTADO);
						icms41.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
								: new BigDecimal(nfe.getValorICMSDesoneracao()));

						icms.setIcms40(icms41);
						imposto.setIcms(icms);

					}

					// ICMS 50
					if (nfe.getSituacaoTributaria() == NFNotaInfoImpostoTributacaoICMS.SUSPENSAO) {

						NFNotaInfoItemImpostoICMS40 icms50 = new NFNotaInfoItemImpostoICMS40();

						icms50.setMotivoDesoneracaoICMS(nfe.getMotivoDesoneracaoICMS());
						icms50.setOrigem(nfe.getOrigem());
						icms50.setSituacaoTributaria(NFNotaInfoImpostoTributacaoICMS.SUSPENSAO);
						icms50.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
								: new BigDecimal(nfe.getValorICMSDesoneracao()));

						icms.setIcms40(icms50);
						imposto.setIcms(icms);

					}

					break;

				case ICMS51:

					NFNotaInfoItemImpostoICMS51 icms51 = new NFNotaInfoItemImpostoICMS51();

					icms51.setModalidadeBCICMS(nfe.getModalidadeBCICMS());

					icms51.setOrigem(nfe.getOrigem());

					icms51.setPercentualDiferimento(nfe.getPercentualDiferimento().equals("") ? null
							: new BigDecimal(nfe.getPercentualDiferimento()));

					icms51.setPercentualFundoCombatePobreza(nfe.getPercentualFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobreza()));

					icms51.setPercentualICMS(
							nfe.getPercentualICMS().equals("") ? null : new BigDecimal(nfe.getPercentualICMS()));

					icms51.setPercentualReducaoBC(nfe.getPercentualReducaoBC().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBC()));

					icms51.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms51.setValorBCFundoCombatePobreza(nfe.getValorBCFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobreza()));

					icms51.setValorBCICMS(
							nfe.getValorBCICMS().equals("") ? null : new BigDecimal(nfe.getValorBCICMS()));

					icms51.setValorFundoCombatePobreza(nfe.getValorFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobreza()));

					icms51.setValorICMS(nfe.getValorICMS().equals("") ? null : new BigDecimal(nfe.getValorICMS()));

					icms51.setValorICMSDiferimento(nfe.getValorICMSDiferimento().equals("") ? null
							: new BigDecimal(nfe.getValorICMSDiferimento()));

					icms51.setValorICMSOperacao(
							nfe.getValorICMSOperacao().equals("") ? null : new BigDecimal(nfe.getValorICMSOperacao()));

					icms.setIcms51(icms51);
					imposto.setIcms(icms);

					break;

				case ICMS60:

					NFNotaInfoItemImpostoICMS60 icms60 = new NFNotaInfoItemImpostoICMS60();

					icms60.setOrigem(nfe.getOrigem());

					icms60.setPercentualAliquotaICMSEfetiva(nfe.getPercentualAliquotaICMSEfetiva().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaICMSEfetiva()));

					icms60.setPercentualAliquotaICMSSTConsumidorFinal(
							nfe.getPercentualAliquotaICMSSTConsumidorFinal().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaICMSSTConsumidorFinal()));

					icms60.setPercentualFundoCombatePobrezaRetidoST(
							nfe.getPercentualFundoCombatePobrezaRetidoST().equals("") ? null
									: new BigDecimal(nfe.getPercentualFundoCombatePobrezaRetidoST()));

					icms60.setPercentualReducaoBCEfetiva(nfe.getPercentualReducaoBCEfetiva().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCEfetiva()));

					icms60.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms60.setValorBCEfetiva(
							nfe.getValorBCEfetiva().equals("") ? null : new BigDecimal(nfe.getValorBCEfetiva()));

					icms60.setValorBCFundoCombatePobrezaRetidoST(
							nfe.getValorBCFundoCombatePobrezaRetidoST().equals("") ? null
									: new BigDecimal(nfe.getValorBCFundoCombatePobrezaRetidoST()));

					icms60.setValorBCICMSSTRetido(nfe.getValorBCICMSSTRetido().equals("") ? null
							: new BigDecimal(nfe.getValorBCICMSSTRetido()));

					icms60.setValorFundoCombatePobrezaRetidoST(
							nfe.getValorFundoCombatePobrezaRetidoST().equals("") ? null
									: new BigDecimal(nfe.getValorFundoCombatePobrezaRetidoST()));

					icms60.setValorICMSEfetivo(
							nfe.getValorICMSEfetivo().equals("") ? null : new BigDecimal(nfe.getValorICMSEfetivo()));

					icms60.setValorICMSSTRetido(
							nfe.getValorICMSSTRetido().equals("") ? null : new BigDecimal(nfe.getValorICMSSTRetido()));

					icms60.setValorICMSSubstituto(nfe.getValorICMSSubstituto().equals("") ? null
							: new BigDecimal(nfe.getValorICMSSubstituto()));

					icms.setIcms60(icms60);
					imposto.setIcms(icms);

					break;

				case ICMS70:

					NFNotaInfoItemImpostoICMS70 icms70 = new NFNotaInfoItemImpostoICMS70();

					icms70.setDesoneracao(nfe.getDesoneracao());

					icms70.setModalidadeBCICMS(nfe.getModalidadeBCICMS());

					icms70.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());

					icms70.setOrigem(nfe.getOrigem());

					icms70.setPercentualAliquota(nfe.getPercentualAliquota().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquota()));

					icms70.setPercentualAliquotaImpostoICMSST(nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icms70.setPercentualFundoCombatePobreza(nfe.getPercentualFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobreza()));

					icms70.setPercentualFundoCombatePobrezaST(nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

					icms70.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icms70.setPercentualReducaoBC(nfe.getPercentualReducaoBC().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBC()));

					icms70.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icms70.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms70.setValorBC(nfe.getValorBC().equals("") ? null : new BigDecimal(nfe.getValorBC()));

					icms70.setValorBCFundoCombatePobreza(nfe.getValorBCFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobreza()));

					icms70.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

					icms70.setValorBCST(nfe.getValorBCST().equals("") ? null : new BigDecimal(nfe.getValorBCST()));

					icms70.setValorFundoCombatePobreza(nfe.getValorFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobreza()));

					icms70.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

					icms70.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
							: new BigDecimal(nfe.getValorICMSDesoneracao()));

					icms70.setValorICMSST(
							nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

					icms70.setValorTributo(
							nfe.getValorTributo().equals("") ? null : new BigDecimal(nfe.getValorTributo()));

					icms.setIcms70(icms70);
					imposto.setIcms(icms);

					break;

				case ICMS90:

					NFNotaInfoItemImpostoICMS90 icms90 = new NFNotaInfoItemImpostoICMS90();

					icms90.setDesoneracao(nfe.getDesoneracao());

					icms90.setModalidadeBCICMS(nfe.getModalidadeBCICMS());

					icms90.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());

					icms90.setOrigem(nfe.getOrigem());

					icms90.setPercentualAliquota(nfe.getPercentualAliquota().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquota()));

					icms90.setPercentualAliquotaImpostoICMSST(nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icms90.setPercentualFundoCombatePobreza(nfe.getPercentualFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobreza()));

					icms90.setPercentualFundoCombatePobrezaST(nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

					icms90.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icms90.setPercentualReducaoBC(nfe.getPercentualReducaoBC().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBC()));

					icms90.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icms90.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icms90.setValorBC(nfe.getValorBC().equals("") ? null : new BigDecimal(nfe.getValorBC()));

					icms90.setValorBCFundoCombatePobreza(nfe.getValorBCFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobreza()));

					icms90.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

					icms90.setValorBCST(nfe.getValorBCST().equals("") ? null : new BigDecimal(nfe.getValorBCST()));

					icms90.setValorFundoCombatePobreza(nfe.getValorFundoCombatePobreza().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobreza()));

					icms90.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

					icms90.setValorICMSDesoneracao(nfe.getValorICMSDesoneracao().equals("") ? null
							: new BigDecimal(nfe.getValorICMSDesoneracao()));

					icms90.setValorICMSST(
							nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

					icms90.setValorTributo(
							nfe.getValorTributo().equals("") ? null : new BigDecimal(nfe.getValorTributo()));

					icms.setIcms90(icms90);
					imposto.setIcms(icms);

					break;

				case ICMSPart:

					NFNotaInfoItemImpostoICMSPartilhado icmsPartilhado = new NFNotaInfoItemImpostoICMSPartilhado();

					icmsPartilhado.setModalidadeBCICMS(nfe.getModalidadeBCICMS());

					icmsPartilhado.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());

					icmsPartilhado.setOrigem(nfe.getOrigem());

					icmsPartilhado.setPercentualAliquotaImposto(nfe.getPercentualAliquotaImposto().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaImposto()));

					icmsPartilhado.setPercentualAliquotaImpostoICMSST(
							nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icmsPartilhado.setPercentualBCOperacaoPropria(nfe.getPercentualBCOperacaoPropria().equals("") ? null
							: new BigDecimal(nfe.getPercentualBCOperacaoPropria()));

					icmsPartilhado.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icmsPartilhado.setPercentualReducaoBC(nfe.getPercentualReducaoBC().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBC()));

					icmsPartilhado.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icmsPartilhado.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icmsPartilhado.setUfICMSST(
							nfe.getUfICMSST().equals("") ? null : DFUnidadeFederativa.valueOfCodigo(nfe.getUfICMSST()));

					icmsPartilhado.setValorBCICMS(
							nfe.getValorBCICMS().equals("") ? null : new BigDecimal(nfe.getValorBCICMS()));

					icmsPartilhado.setValorBCICMSST(
							nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

					icmsPartilhado
							.setValorICMS(nfe.getValorICMS().equals("") ? null : new BigDecimal(nfe.getValorICMS()));

					icmsPartilhado.setValorICMSST(
							nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

					icms.setIcmsPartilhado(icmsPartilhado);
					imposto.setIcms(icms);

					break;

				case ICMSSN101:

					NFNotaInfoItemImpostoICMSSN101 icmssn101 = new NFNotaInfoItemImpostoICMSSN101();

					icmssn101.setSituacaoOperacaoSN(nfe.getSituacaoOperacaoSN());

					icmssn101.setOrigem(nfe.getOrigem());

					icmssn101.setPercentualAliquotaAplicavelCalculoCreditoSN(
							nfe.getPercentualAliquotaAplicavelCalculoCreditoSN().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaAplicavelCalculoCreditoSN()));

					icmssn101.setValorCreditoICMSSN(nfe.getValorCreditoICMSSN().equals("") ? null
							: new BigDecimal(nfe.getValorCreditoICMSSN()));

					icms.setIcmssn101(icmssn101);
					imposto.setIcms(icms);

					break;

				case ICMSSN102:

					// 245.24 N10d ICMSSN102 Grupo CRT=1 – Simples Nacional e CSOSN=102, 103, 300 ou
					// 400 CG N01 1-1 Tributação ICMS pelo Simples Nacional, CSOSN=102, 103, 300 ou
					// 400

					// CSOSN 102
					if (nfe.getSituacaoOperacaoSN() == NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SEM_PERMISSAO_CREDITO) {

						NFNotaInfoItemImpostoICMSSN102 icmssn102 = new NFNotaInfoItemImpostoICMSSN102();
						icmssn102.setSituacaoOperacaoSN(
								NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SEM_PERMISSAO_CREDITO);
						icmssn102.setOrigem(nfe.getOrigem());
						icms.setIcmssn102(icmssn102);
						imposto.setIcms(icms);

					}

					// CSOSN 103
					if (nfe.getSituacaoOperacaoSN() == NFNotaSituacaoOperacionalSimplesNacional.ISENCAO_ICMS_FAIXA_RECEITA_BRUTA) {

						NFNotaInfoItemImpostoICMSSN102 icmssn103 = new NFNotaInfoItemImpostoICMSSN102();
						icmssn103.setOrigem(nfe.getOrigem());
						icmssn103.setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional.IMUNE);
						icms.setIcmssn102(icmssn103);
						imposto.setIcms(icms);

					}

					// CSOSN 300
					if (nfe.getSituacaoOperacaoSN() == NFNotaSituacaoOperacionalSimplesNacional.IMUNE) {

						NFNotaInfoItemImpostoICMSSN102 icmssn300 = new NFNotaInfoItemImpostoICMSSN102();
						icmssn300.setOrigem(nfe.getOrigem());
						icmssn300.setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional.IMUNE);
						icms.setIcmssn102(icmssn300);
						imposto.setIcms(icms);

					}

					// CSOSN 400
					if (nfe.getSituacaoOperacaoSN() == NFNotaSituacaoOperacionalSimplesNacional.NAO_TRIBUTADA) {

						NFNotaInfoItemImpostoICMSSN102 icmssn400 = new NFNotaInfoItemImpostoICMSSN102();
						icmssn400.setOrigem(nfe.getOrigem());
						icmssn400.setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional.NAO_TRIBUTADA);
						icms.setIcmssn102(icmssn400);
						imposto.setIcms(icms);

					}

					break;

				case ICMSSN201:

					NFNotaInfoItemImpostoICMSSN201 icmssn201 = new NFNotaInfoItemImpostoICMSSN201();

					icmssn201.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());
					icmssn201.setOrigem(nfe.getOrigem());

					icmssn201.setPercentualAliquotaAplicavelCalculoCreditoSN(
							nfe.getPercentualAliquotaAplicavelCalculoCreditoSN().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaAplicavelCalculoCreditoSN()));

					icmssn201.setPercentualAliquotaImpostoICMSST(
							nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icmssn201.setPercentualFundoCombatePobrezaST(
							nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
									: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

					icmssn201.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icmssn201.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icmssn201.setSituacaoOperacaoSN(nfe.getSituacaoOperacaoSN());

					icmssn201.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

					icmssn201.setValorBCICMSST(
							nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

					icmssn201.setValorCreditoICMSSN(nfe.getValorCreditoICMSSN().equals("") ? null
							: new BigDecimal(nfe.getValorCreditoICMSSN()));

					icmssn201.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

					icmssn201.setValorICMSST(
							nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

					icms.setIcmssn201(icmssn201);
					imposto.setIcms(icms);

					break;

				case ICMSSN202:

					// 245.38 N10f ICMSSN202 Grupo CRT=1 – Simples Nacional e CSOSN=202 ou 203
					// CG N01 1-1 Tributação ICMS pelo Simples Nacional, CSOSN=202 ou 203

					// CSOSN 202
					if (nfe.getSituacaoOperacaoSN() == NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SIMPLES_NACIONAL_SEM_PERMISSAO_DE_CREDITO_E_COBRANCA_ICMS_SUBSTITUICAO_TRIBUTARIA) {

						NFNotaInfoItemImpostoICMSSN202 icmssn202 = new NFNotaInfoItemImpostoICMSSN202();

						icmssn202.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());
						icmssn202.setOrigem(nfe.getOrigem());

						icmssn202.setPercentualAliquotaImpostoICMSST(
								nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
										: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

						icmssn202.setPercentualFundoCombatePobrezaST(
								nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
										: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

						icmssn202.setPercentualMargemValorAdicionadoICMSST(
								nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
										: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

						icmssn202.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
								: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

						icmssn202.setSituacaoOperacaoSN(
								NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SIMPLES_NACIONAL_SEM_PERMISSAO_DE_CREDITO_E_COBRANCA_ICMS_SUBSTITUICAO_TRIBUTARIA);

						icmssn202
								.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
										: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

						icmssn202.setValorBCICMSST(
								nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

						icmssn202.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
								: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

						icmssn202.setValorICMSST(
								nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

						icms.setIcmssn202(icmssn202);
						imposto.setIcms(icms);

					}

					// CSOSN 203
					if (nfe.getSituacaoOperacaoSN() == NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SIMPLES_NACIONAL_PARA_FAIXA_RECEITA_BRUTA_E_COBRANCA_ICMS_SUBSTITUICAO_TRIBUTARIA) {

						NFNotaInfoItemImpostoICMSSN202 icmssn203 = new NFNotaInfoItemImpostoICMSSN202();

						icmssn203.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());
						icmssn203.setOrigem(nfe.getOrigem());

						icmssn203.setPercentualAliquotaImpostoICMSST(
								nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
										: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

						icmssn203.setPercentualFundoCombatePobrezaST(
								nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
										: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

						icmssn203.setPercentualMargemValorAdicionadoICMSST(
								nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
										: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

						icmssn203.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
								: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

						icmssn203.setSituacaoOperacaoSN(
								NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SIMPLES_NACIONAL_SEM_PERMISSAO_DE_CREDITO_E_COBRANCA_ICMS_SUBSTITUICAO_TRIBUTARIA);

						icmssn203
								.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
										: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

						icmssn203.setValorBCICMSST(
								nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

						icmssn203.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
								: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

						icmssn203.setValorICMSST(
								nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

						icms.setIcmssn202(icmssn203);
						imposto.setIcms(icms);

					}

					break;

				case ICMSSN500:

					NFNotaInfoItemImpostoICMSSN500 icmssn500 = new NFNotaInfoItemImpostoICMSSN500();
					icmssn500.setSituacaoOperacaoSN(nfe.getSituacaoOperacaoSN());
					icmssn500.setOrigem(nfe.getOrigem());
					icmssn500.setValorBCICMSSTRetido(new BigDecimal(nfe.getValorBCICMSSTRetido()));
					icmssn500.setValorICMSSTRetido(new BigDecimal(nfe.getValorICMSSTRetido()));
					icmssn500.setPercentualICMSSTRetido(new BigDecimal(nfe.getPercentualICMSSTRetido()));
					icms.setIcmssn500(icmssn500);
					imposto.setIcms(icms);

					break;

				case ICMSSN900:

					NFNotaInfoItemImpostoICMSSN900 icmssn900 = new NFNotaInfoItemImpostoICMSSN900();
					icmssn900.setModalidadeBCICMS(nfe.getModalidadeBCICMS());
					icmssn900.setModalidadeBCICMSST(nfe.getModalidadeBCICMSST());
					icmssn900.setOrigem(nfe.getOrigem());

					icmssn900.setPercentualAliquotaAplicavelCalculoCreditoSN(
							nfe.getPercentualAliquotaAplicavelCalculoCreditoSN().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaAplicavelCalculoCreditoSN()));

					icmssn900.setPercentualAliquotaImposto(nfe.getPercentualAliquotaImposto().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaImposto()));

					icmssn900.setPercentualAliquotaImpostoICMSST(
							nfe.getPercentualAliquotaImpostoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualAliquotaImpostoICMSST()));

					icmssn900.setPercentualFundoCombatePobrezaST(
							nfe.getPercentualFundoCombatePobrezaST().equals("") ? null
									: new BigDecimal(nfe.getPercentualFundoCombatePobrezaST()));

					icmssn900.setPercentualMargemValorAdicionadoICMSST(
							nfe.getPercentualMargemValorAdicionadoICMSST().equals("") ? null
									: new BigDecimal(nfe.getPercentualMargemValorAdicionadoICMSST()));

					icmssn900.setPercentualReducaoBC(nfe.getPercentualReducaoBC().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBC()));

					icmssn900.setPercentualReducaoBCICMSST(nfe.getPercentualReducaoBCICMSST().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCICMSST()));

					icmssn900.setSituacaoOperacaoSN(nfe.getSituacaoOperacaoSN());

					icmssn900.setValorBCFundoCombatePobrezaST(nfe.getValorBCFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorBCFundoCombatePobrezaST()));

					icmssn900.setValorBCICMS(
							nfe.getValorBCICMS().equals("") ? null : new BigDecimal(nfe.getValorBCICMS()));

					icmssn900.setValorBCICMSST(
							nfe.getValorBCICMSST().equals("") ? null : new BigDecimal(nfe.getValorBCICMSST()));

					icmssn900.setValorCreditoICMSSN(nfe.getValorCreditoICMSSN().equals("") ? null
							: new BigDecimal(nfe.getValorCreditoICMSSN()));

					icmssn900.setValorFundoCombatePobrezaST(nfe.getValorFundoCombatePobrezaST().equals("") ? null
							: new BigDecimal(nfe.getValorFundoCombatePobrezaST()));

					icmssn900.setValorICMS(nfe.getValorICMS().equals("") ? null : new BigDecimal(nfe.getValorICMS()));

					icmssn900.setValorICMSST(
							nfe.getValorICMSST().equals("") ? null : new BigDecimal(nfe.getValorICMSST()));

					icms.setIcmssn900(icmssn900);
					imposto.setIcms(icms);

					break;

				case ICMSST:

					NFNotaInfoItemImpostoICMSST icmsst = new NFNotaInfoItemImpostoICMSST();

					icmsst.setAliqSuportadaConsFinal(nfe.getAliqSuportadaConsFinal().equals("") ? null
							: new BigDecimal(nfe.getAliqSuportadaConsFinal()));

					icmsst.setOrigem(nfe.getOrigem());

					icmsst.setPercentualAliquotaICMSEfetiva(nfe.getPercentualAliquotaICMSEfetiva().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaICMSEfetiva()));

					icmsst.setPercentualFundoCombatePobrezaRetidoST(
							nfe.getPercentualFundoCombatePobrezaRetidoST().equals("") ? null
									: new BigDecimal(nfe.getPercentualFundoCombatePobrezaRetidoST()));

					icmsst.setPercentualReducaoBCEfetiva(nfe.getPercentualReducaoBCEfetiva().equals("") ? null
							: new BigDecimal(nfe.getPercentualReducaoBCEfetiva()));

					icmsst.setSituacaoTributaria(nfe.getSituacaoTributaria());

					icmsst.setValorBCEfetiva(
							nfe.getValorBCEfetiva().equals("") ? null : new BigDecimal(nfe.getValorBCEfetiva()));

					icmsst.setValorBCFundoCombatePobrezaRetidoST(
							nfe.getValorBCFundoCombatePobrezaRetidoST().equals("") ? null
									: new BigDecimal(nfe.getValorBCFundoCombatePobrezaRetidoST()));

					icmsst.setValorBCICMSSTRetidoUFRemetente(nfe.getValorBCICMSSTRetidoUFRemetente().equals("") ? null
							: new BigDecimal(nfe.getValorBCICMSSTRetidoUFRemetente()));

					icmsst.setValorBCICMSSTUFDestino(nfe.getValorBCICMSSTUFDestino().equals("") ? null
							: new BigDecimal(nfe.getValorBCICMSSTUFDestino()));

					icmsst.setValorFundoCombatePobrezaRetidoST(
							nfe.getValorFundoCombatePobrezaRetidoST().equals("") ? null
									: new BigDecimal(nfe.getValorFundoCombatePobrezaRetidoST()));

					icmsst.setValorICMSEfetivo(
							nfe.getValorICMSEfetivo().equals("") ? null : new BigDecimal(nfe.getValorICMSEfetivo()));

					icmsst.setValorICMSSTRetidoUFRemetente(nfe.getValorICMSSTRetidoUFRemetente().equals("") ? null
							: new BigDecimal(nfe.getValorICMSSTRetidoUFRemetente()));

					icmsst.setValorICMSSTUFDestino(nfe.getValorICMSSTUFDestino().equals("") ? null
							: new BigDecimal(nfe.getValorICMSSTUFDestino()));

					icmsst.setValorICMSSubstituto(nfe.getValorICMSSubstituto().equals("") ? null
							: new BigDecimal(nfe.getValorICMSSubstituto()));

					icms.setIcmsst(icmsst);
					imposto.setIcms(icms);

					break;

				default:
				}

			if (nfe.getSituacaoTributariaPIS() != null)
				switch (nfe.getSituacaoTributariaPIS()) {
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_EXCLUSIVAMENTE_A_RECEITA_NAO_TRIBUTADA_MERCADO_INTERNO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_EXCLUSIVAMENTE_RECEITA_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_EXCLUSIVAMENTE_RECEITA_TRIBUTADA_MERCADO_INTERNO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_NAO_TRIBUTADAS_MERCADO_INTERNO_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_TRIBUTADA_MERCADO_INTERNO_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_TRIBUTADAS_E_NAO_TRIBUTADAS_MERCADO_INTERNO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_TRIBUTADAS_E_NAO_TRIBUTADAS_MERCADO_INTERNO_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OUTRAS_OPERACOES:

					break;
				case OPERACAO_AQUISICAO_ALIQUOTA_ZERO:

					break;
				case OPERACAO_AQUISICAO_COM_ISENCAO:

					break;
				case OPERACAO_AQUISICAO_COM_SUSPENSAO:

					break;
				case OPERACAO_AQUISICAO_POR_SUBSTITUICAO_TRIBUTARIA:

					break;
				case OPERACAO_AQUISICAO_SEM_DIREITO_CREDITO:

					break;
				case OPERACAO_AQUISICAO_SEM_INCIDENCIA_CONTRIBUICAO:

					break;
				case OPERACAO_COM_SUSPENSAO_CONTRIBUICAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_EXCLUSIVAMENTE_RECEITA_EXPORTACAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_EXCLUSIVAMENTE_RECEITA_NAO_TRIBUTADA_MERCADO_INTERNO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_EXCLUSIVAMENTE_RECEITA_TRIBUTADA_MERCADO_INTERNO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_NAO_TRIBUTADA_NO_MERCADO_INTERNO_EXPORTACAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_TRIBUTADA_E_NAO_TRIBUTADA_MERCADO_INTERNO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_TRIBUTADAS_E_NAO_TRIBUTADAS_MERCADO_INTERNO_EXPORTACAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_TRIBUTADAS_NO_MERCADO_INTERNO_EXPORTACAO:

					break;
				case OPERACAO_ISENTA_CONTRIBUICAO:

					break;
				case OPERACAO_SEM_INCIDENCIA_CONTRIBUICAO:

					break;
				case OPERACAO_TRIBUTAVEL_ALIQUOTA_DIFERENCIADA:

					break;
				case OPERACAO_TRIBUTAVEL_ALIQUOTA_ZERO:

					break;
				case OPERACAO_TRIBUTAVEL_CUMULATIVO_NAO_CUMULATIVO:

					break;
				case OPERACAO_TRIBUTAVEL_MONOFASICA_ALIQUOTA_ZERO:

					break;
				case OPERACAO_TRIBUTAVEL_QUANTIDADE_VENDIDA_POR_ALIQUOTA_POR_UNIDADE_PRODUTO:

					break;
				case OPERACAO_TRIBUTAVEL_SUBSTITUICAO_TRIBUTARIA:

					break;
				case OUTRAS_OPERACOES:

					NFNotaInfoItemImpostoPISOutrasOperacoes pis = new NFNotaInfoItemImpostoPISOutrasOperacoes();
					pis.setSituacaoTributaria(NFNotaInfoSituacaoTributariaPIS.OUTRAS_OPERACOES);

					pis.setValorBaseCalculo(nfe.getValorBaseCalculoPIS().equals("") ? null
							: new BigDecimal(nfe.getValorBaseCalculoPIS()));

					pis.setPercentualAliquota(nfe.getPercentualAliquotaPIS().equals("") ? null
							: new BigDecimal(nfe.getPercentualAliquotaPIS()));

					pis.setValorTributo(
							nfe.getValorTributoPIS().equals("") ? null : new BigDecimal(nfe.getValorTributoPIS()));

					this.pis.setOutrasOperacoes(pis);
					imposto.setPis(this.pis);

					break;
				case OUTRAS_OPERACOES_ENTRADA:

					break;
				case OUTRAS_OPERACOES_SAIDA:

					break;

				default:
				}

			if (nfe.getSituacaoTributariaCOFINS() != null)
				switch (nfe.getSituacaoTributariaCOFINS()) {
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_EXCLUSIVAMENTE_A_RECEITA_NAO_TRIBUTADA_MERCADO_INTERNO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_EXCLUSIVAMENTE_RECEITA_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_EXCLUSIVAMENTE_RECEITA_TRIBUTADA_MERCADO_INTERNO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_NAO_TRIBUTADAS_MERCADO_INTERNO_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_TRIBUTADA_MERCADO_INTERNO_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_TRIBUTADAS_E_NAO_TRIBUTADAS_MERCADO_INTERNO:

					break;
				case CREDITO_PRESUMIDO_OPERACAO_AQUISICAO_VINCULADA_RECEITAS_TRIBUTADAS_E_NAO_TRIBUTADAS_MERCADO_INTERNO_EXPORTACAO:

					break;
				case CREDITO_PRESUMIDO_OUTRAS_OPERACOES:

					break;
				case OPERACAO_AQUISICAO_ALIQUOTA_ZERO:

					break;
				case OPERACAO_AQUISICAO_COM_ISENCAO:

					break;
				case OPERACAO_AQUISICAO_COM_SUSPENSAO:

					break;
				case OPERACAO_AQUISICAO_POR_SUBSTITUICAO_TRIBUTARIA:

					break;
				case OPERACAO_AQUISICAO_SEM_DIREITO_CREDITO:

					break;
				case OPERACAO_AQUISICAO_SEM_INCIDENCIA_CONTRIBUICAO:

					break;
				case OPERACAO_COM_SUSPENSAO_CONTRIBUICAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_EXCLUSIVAMENTE_RECEITA_EXPORTACAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_EXCLUSIVAMENTE_RECEITA_NAO_TRIBUTADA_MERCADO_INTERNO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_EXCLUSIVAMENTE_RECEITA_TRIBUTADA_MERCADO_INTERNO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_NAO_TRIBUTADA_NO_MERCADO_INTERNO_EXPORTACAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_TRIBUTADA_E_NAO_TRIBUTADA_MERCADO_INTERNO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_TRIBUTADAS_E_NAO_TRIBUTADAS_MERCADO_INTERNO_EXPORTACAO:

					break;
				case OPERACAO_DIREITO_CREDITO_VINCULADA_RECEITAS_TRIBUTADAS_NO_MERCADO_INTERNO_EXPORTACAO:

					break;
				case OPERACAO_ISENTA_CONTRIBUICAO:

					break;
				case OPERACAO_SEM_INCIDENCIA_CONTRIBUICAO:

					break;
				case OPERACAO_TRIBUTAVEL_ALIQUOTA_DIFERENCIADA:

					break;
				case OPERACAO_TRIBUTAVEL_ALIQUOTA_ZERO:

					break;
				case OPERACAO_TRIBUTAVEL_CUMULATIVO_NAO_CUMULATIVO:

					break;
				case OPERACAO_TRIBUTAVEL_MONOFASICA_ALIQUOTA_ZERO:

					break;
				case OPERACAO_TRIBUTAVEL_QUANTIDADE_VENDIDA_POR_ALIQUOTA_POR_UNIDADE_PRODUTO:

					break;
				case OPERACAO_TRIBUTAVEL_SUBSTITUICAO_TRIBUTARIA:

					break;
				case OUTRAS_OPERACOES:

					NFNotaInfoItemImpostoCOFINSOutrasOperacoes cofins = new NFNotaInfoItemImpostoCOFINSOutrasOperacoes();
					cofins.setSituacaoTributaria(NFNotaInfoSituacaoTributariaCOFINS.OUTRAS_OPERACOES);

					cofins.setValorBaseCalculo(nfe.getValorBaseCalculoCOFINS().equals("") ? null
							: new BigDecimal(nfe.getValorBaseCalculoCOFINS()));

					cofins.setPercentualCOFINS(
							nfe.getPercentualCOFINS().equals("") ? null : new BigDecimal(nfe.getPercentualCOFINS()));

					cofins.setValorCOFINS(
							nfe.getValorCOFINS().equals("") ? null : new BigDecimal(nfe.getValorCOFINS()));

					this.cofins.setOutrasOperacoes(cofins);
					imposto.setCofins(this.cofins);

					break;
				case OUTRAS_OPERACOES_ENTRADA:

					break;
				case OUTRAS_OPERACOES_SAIDA:

					break;

				default:
				}

			item.setProduto(produto);

			item.setImposto(imposto);

			itens.add(item);

			this.produto = new NFNotaInfoItemProduto();

			this.icmssn500 = new NFNotaInfoItemImpostoICMSSN500();

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
		icmsTotal.setValorTotalDosProdutosServicos(nfce.getTotal());
		icmsTotal.setValorTotalFrete(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalSeguro(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalDesconto(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalII(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalIPI(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorPIS(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorCOFINS(new java.math.BigDecimal("0.00"));
		icmsTotal.setOutrasDespesasAcessorias(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalNFe(nfce.getTotal());

		// adicionado nfe40
		icmsTotal.setValorTotalFundoCombatePobreza(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalFundoCombatePobrezaST(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalFundoCombatePobrezaSTRetido(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalIPIDevolvido(new java.math.BigDecimal("0.00"));
		// adicionado nfe40

		total.setIcmsTotal(icmsTotal);

		info.setTotal(total);

	}

	public void geraQRCode() throws Exception {

		// NFGeraQRCode geraQRCode = new NFGeraQRCode(nota, config);
		// NFNotaInfoSuplementar nfs = new NFNotaInfoSuplementar();
		// nfs.setUrlConsultaChaveAcesso(geraQRCode.urlConsultaChaveAcesso());
		// nfs.setQrCode(geraQRCode.getQRCode());
		// nota.setInfoSuplementar(nfs);

		QRCode geraQRCode20 = new QRCode(nota, config);
		NFNotaInfoSuplementar nfs = new NFNotaInfoSuplementar();
		nfs.setUrlConsultaChaveAcesso(geraQRCode20.urlConsultaChaveAcesso());
		nfs.setQrCode(geraQRCode20.getQRCode());
		nota.setInfoSuplementar(nfs);

	}

	public NTFCe transmitir() throws Exception {

		this.geraEmitenteEndereco();

		this.geraEmitente();

		this.geraDestinatarioEndereco();

		this.geraDestinatario();

		this.geraNFNotaInfoIdentificacao();

		this.geraTransporte();

		this.geraNFNotaInfo();

		this.geraProdutos();

		this.geraICMSTotal();

		List<NFNota> lstNF = new ArrayList<NFNota>();

		// VERIFICAR PORQUE ESSA LINHA ESTA NESTE METODO.
		info.setItens(itens);

		// VERIFICAR PORQUE ESSA LINHA ESTA NESTE METODO.
		nota.setInfo(info);

		lstNF.add(nota);

		lote.setNotas(lstNF);

		// VERIRICAR SE O ID SER� SEM 1.
		lote.setIdLote("1");
		lote.setVersao(config.getVersao());
		// lote.setIndicadorProcessamento(NFLoteIndicadorProcessamento.PROCESSAMENTO_ASSINCRONO);
		lote.setIndicadorProcessamento(NFLoteIndicadorProcessamento.PROCESSAMENTO_SINCRONO);

		// Gabiarra para gerar o codigo Randomico e o Indentificador.
		NFGeraChave nfGeraChave = new NFGeraChave(nota);

		// Mostrando os valores gerados pela classe NFGeraChave.
		// System.out.println("----- Valores gerados pela classe NFGeraChave
		// ----- ");
		// System.out.println("Codigo Randomico gerado pela classe NFGeraChave:
		// " + nfGeraChave.geraCodigoRandomico());
		// System.out.println("Identificador gerado pela classe NFGeraChave: " +
		// nfGeraChave.getChaveAcesso());

		// Mostrando os valores de teste dos objetos.
		// System.out.println("----- Valores de teste dos objetos ----- ");
		// System.out.println("Codigo Randomico com valor de teste: " +
		// identificacao.getCodigoRandomico());
		// System.out.println("Identificador com valor de teste: " +
		// info.getIdentificador());

		// Seteando os valores gerados pela classe NFGeraChave.
		identificacao.setCodigoRandomico(nfGeraChave.geraCodigoRandomico());
		info.setIdentificador(nfGeraChave.getChaveAcesso());

		// Mostrando os valores atualizados.
		// System.out.println("----- Valores atualizados ----- ");
		// System.out.println("Codigo Randomico com valor atualizado: " +
		// identificacao.getCodigoRandomico());
		// System.out.println("Identificador com valor atualizado: " +
		// info.getIdentificador());

		this.geraQRCode();

		// ENVIO DO LOTE GERADO PELO CODIGO ACIMA.

		System.out.println(" ");

		System.out.println("ENVIANDO LOTE...");

		System.out.println(" ");

		System.out.println("----------DADOS CONTIDOS NO LOTE INICIO-----------");

		System.out.println(" ");

		System.out.println("Numero de NF no lote: " + lote.getNotas().size());

		System.out.println("Chave: " + lote.getNotas().get(0).getInfo().getChaveAcesso());
		System.out.println("Ambiente: " + lote.getNotas().get(0).getInfo().getIdentificacao().getAmbiente());
		System.out.println("dt. Emissao: " + lote.getNotas().get(0).getInfo().getIdentificacao().getDataHoraEmissao());
		System.out.println("Numero NF: " + lote.getNotas().get(0).getInfo().getIdentificacao().getNumeroNota());
		System.out.println("Serie NF: " + lote.getNotas().get(0).getInfo().getIdentificacao().getSerie());

		System.out.println(" ");

		System.out.println("----------DADOS CONTIDOS NO LOTE FIM--------------");

		NFLoteEnvioRetornoDados retorno = new WSFacade(config).enviaLote(lote);

		System.out.println(" ");

		System.out.println("LOTE ENVIADO!");

		System.out.println(" ");

		System.out.println("----------DADOS DO RETORNO INICIO--------------");
		System.out.println("Motivo: " + retorno.getRetorno().getMotivo());
		System.out.println("Protocolo Recebimento Sincrono: " + retorno.getRetorno().getProtocoloRecebimentoSincrono());
		System.out.println("Status: " + retorno.getRetorno().getStatus());
		System.out.println("Versao: " + retorno.getRetorno().getVersao());
		System.out.println("Versao Aplicacao: " + retorno.getRetorno().getVersaoAplicacao());
		System.out.println("Ambiente: " + retorno.getRetorno().getAmbiente());
		System.out.println("Assinatura: " + retorno.getRetorno().getAssinatura());
		System.out.println("Data Recebimento: " + retorno.getRetorno().getDataRecebimento());
		System.out.println("Info Recebimento: " + retorno.getRetorno().getInfoRecebimento());
		System.out.println("Protocolo Info: " + retorno.getRetorno().getProtocoloInfo());
		System.out.println("Uf: " + retorno.getRetorno().getUf());

		System.out.println(" ");

		System.out.println("(Prot. Inf.) Chave: " + retorno.getRetorno().getProtocoloInfo().getChave());
		System.out.println("(Prot. Inf.) Codigo Msg: " + retorno.getRetorno().getProtocoloInfo().getCodigoMessage());
		System.out.println("(Prot. Inf.) Ident.r: " + retorno.getRetorno().getProtocoloInfo().getIdentificador());
		System.out.println("(Prot. Inf.) Mensagem: " + retorno.getRetorno().getProtocoloInfo().getMensagem());
		System.out.println("(Prot. Inf.) Motivo: " + retorno.getRetorno().getProtocoloInfo().getMotivo());
		System.out.println("(Prot. Inf.) N. Protoc.: " + retorno.getRetorno().getProtocoloInfo().getNumeroProtocolo());
		System.out.println("(Prot. Inf.) Status: " + retorno.getRetorno().getProtocoloInfo().getStatus());
		System.out.println("(Prot. Inf.) Validador: " + retorno.getRetorno().getProtocoloInfo().getValidador());
		System.out.println("(Prot. Inf.) V. Apli.: " + retorno.getRetorno().getProtocoloInfo().getVersaoAplicacao());
		System.out.println("(Prot. Inf.) Ambiente: " + retorno.getRetorno().getProtocoloInfo().getAmbiente());
		System.out.println("(Prot. Inf.) Dt. Receb.: " + retorno.getRetorno().getProtocoloInfo().getDataRecebimento());

		System.out.println(" ");

		System.out.println("----------DADOS DO RETORNO FIM--------------");

		System.out.println(" ");

		nfce.setChave(retorno.getRetorno().getProtocoloInfo().getChave());
		nfce.setNumProtocolo(retorno.getRetorno().getProtocoloInfo().getNumeroProtocolo());
		nfce.setStatus(retorno.getRetorno().getProtocoloInfo().getStatus());
		nfce.setRejeicaoMotivo(retorno.getRetorno().getProtocoloInfo().getMotivo());
		nfce.setAmbienteRecebimento(retorno.getRetorno().getProtocoloInfo().getAmbiente().getCodigo());
		nfce.setDataRecebimento(
				java.sql.Timestamp.valueOf(retorno.getRetorno().getProtocoloInfo().getDataRecebimento()));

		if (retorno.getRetorno().getProtocoloInfo().getStatus().equals("100")) {

			NFNotaConsultaRetorno prot = new WSFacade(config)
					.consultaNota(retorno.getRetorno().getProtocoloInfo().getChave());

			System.out.println("----------DADOS APOS TRANSMISSAO DA NFC-e INICIO-----------");
			System.out.println(" ");
			System.out.println("Chave: " + prot.getChave());
			System.out.println("Numero Protocolo: " + prot.getProtocolo().getProtocoloInfo().getNumeroProtocolo());
			System.out.println("Status: " + prot.getStatus());
			System.out.println("Motivo: " + prot.getMotivo());
			System.out.println("Ambiente: " + prot.getAmbiente().getCodigo());
			System.out.println("DataRecebimento: " + prot.getDataHoraRecibo());
			System.out.println(" ");
			System.out.println("----------DADOS APOS TRANSMISSAO DA NFC-e FIM--------------");

			nfce.setChave(prot.getChave());
			nfce.setNumProtocolo(prot.getProtocolo().getProtocoloInfo().getNumeroProtocolo());
			nfce.setStatus(prot.getStatus());
			nfce.setRejeicaoMotivo(prot.getMotivo());
			nfce.setAmbienteRecebimento(prot.getAmbiente().getCodigo());
			nfce.setDataRecebimento(java.sql.Timestamp.valueOf(prot.getDataHoraRecibo()));

			// Carregue o xml da nota do local que foi armazenado
			final String xmlNotaRecuperada = lote.getNotas().get(0).toString();
			// Assine a nota
			final String xmlNotaRecuperadaAssinada = new DFAssinaturaDigital(config)
					.assinarDocumento(xmlNotaRecuperada);
			// Converta para objeto java
			final NFNota notaRecuperadaAssinada = new DFPersister(false).read(NFNota.class, xmlNotaRecuperadaAssinada);

			nota = notaRecuperadaAssinada;

			// this.geraQRCode();

			// Fonte: https://github.com/wmixvideo/nfe/issues/153
			// Armanezenando o Xml da nota autorizada ou processada.
			NFNotaProcessada notaProcessada = new NFNotaProcessada();
			notaProcessada.setVersao(new java.math.BigDecimal(config.getVersao()));
			notaProcessada.setProtocolo(prot.getProtocolo());
			notaProcessada.setNota(nota);

			String xmlNotaProcessadaPeloSefaz = notaProcessada.toString();

			System.out.println(" ");

			System.out.println("----------XML DA NFC-e INICIO-----------");
			System.out.println(" ");
			System.out.println(xmlNotaProcessadaPeloSefaz);
			System.out.println(" ");
			System.out.println("----------XML DA NFC-e FIM--------------");

			nfce.setXml(xmlNotaProcessadaPeloSefaz);

		}

		return nfce;

	}

}