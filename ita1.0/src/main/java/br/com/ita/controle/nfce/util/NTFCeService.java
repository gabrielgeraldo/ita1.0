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
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.NFProcessoEmissor;
import com.fincatto.documentofiscal.nfe400.classes.NFProdutoCompoeValorNota;
import com.fincatto.documentofiscal.nfe400.classes.NFProtocolo;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;
import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.NFTipoImpressao;
import com.fincatto.documentofiscal.nfe400.classes.lote.consulta.NFLoteConsultaRetorno;
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
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoSuplementar;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoTransporte;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFOperacaoConsumidorFinal;
import com.fincatto.documentofiscal.nfe400.utils.NFGeraChave;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;
import com.fincatto.documentofiscal.persister.DFPersister;
import com.fincatto.documentofiscal.utils.DFAssinaturaDigital;

import br.com.ita.controle.config.Config;
import br.com.ita.dominio.Configuracao;
import br.com.ita.dominio.ItemNTFCe;
import br.com.ita.dominio.NTFCe;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
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

	private ConfiguracaoDAO daoConfiguracao = new ConfiguracaoDAO();

	private EstadoDAO daoEstado = new EstadoDAO();

	private MunicipioDAO daoMunicipio = new MunicipioDAO();

	public NTFCeService(NTFCe nfce, List<ItemNTFCe> itensNfce) {
		this.setNfce(nfce);
		this.setItensNfce(itensNfce);
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

	public ConfiguracaoDAO getDaoConfiguracao() {
		return daoConfiguracao;
	}

	public void setDaoConfiguracao(ConfiguracaoDAO daoConfiguracao) {
		this.daoConfiguracao = daoConfiguracao;
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

		identificacao.setUf(DFUnidadeFederativa.valueOfCodigo(Config.propertiesLoader().getProperty("uf")));
		identificacao.setCodigoRandomico(this.gerarCodigoRandomico());
		// VERIFICAR SE SEMPRE SERÁ VENDA.
		identificacao.setNaturezaOperacao("VENDA");
		// Removido 20180913 ao atualizar nfe40
		// identificacao.setFormaPagamento(NFFormaPagamentoPrazo.A_VISTA);
		identificacao.setModelo(DFModelo.NFCE);
		identificacao.setSerie(Integer.toString(nfce.getSerie()));
		identificacao.setNumeroNota(Integer.toString(nfce.getNumero()));
		identificacao.setDataHoraEmissao(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
		identificacao.setTipo(NFTipo.SAIDA);
		identificacao.setIdentificadorLocalDestinoOperacao(NFIdentificadorLocalDestinoOperacao.OPERACAO_INTERNA);
		identificacao.setCodigoMunicipio(Config.propertiesLoader().getProperty("codigoMunicipio"));
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
		 * fpgto.setMeioPagamento(NFMeioPagamento.DINHEIRO);
		 * fpgto.setValorPagamento(new java.math.BigDecimal("0.00"));
		 * 
		 * List<NFNotaInfoFormaPagamento> lsFpgto = new
		 * ArrayList<NFNotaInfoFormaPagamento>(); lsFpgto.add(fpgto);
		 * 
		 * pagamento.setDetalhamentoFormasPagamento(lsFpgto);
		 * pagamentos.add(pagamento);
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

		fpgto.setValorPagamento(this.nfce.getTotal());
		List<NFNotaInfoFormaPagamento> lsFpgto = new ArrayList<NFNotaInfoFormaPagamento>();
		lsFpgto.add(fpgto);

		pagamento.setDetalhamentoFormasPagamento(lsFpgto);
		pagamentos.add(pagamento);

	}

	public void geraNFNotaInfo() {

		info.setIdentificador("11111111111111111111111111111111111111111111");
		info.setVersao(new java.math.BigDecimal(config.getVersao()));
		info.setIdentificacao(identificacao);
		info.setTransporte(transporte);
		info.setEmitente(emitente);
		info.setDestinatario(destinatario);

		this.geraPagamentos();
		info.setPagamentos(pagamentos);

	}

	public void geraProdutos() {

		// Usado se a configuração do imposto estiver no sistema.
		Configuracao config = (Config.propertiesLoader().getProperty("imposto").equals("1")
				? daoConfiguracao.lerPorId(new Long(1)) : null);

		for (int i = 0; i < itensNfce.size(); i++) {

			item.setNumeroItem(i + 1);

			produto.setCodigo(itensNfce.get(i).getProduto().getCodigo().toString() != null
					? itensNfce.get(i).getProduto().getCodigo().toString() : "");

			produto.setCodigoDeBarras("SEM GTIN");

			produto.setCodigoDeBarrasTributavel("SEM GTIN");

			produto.setDescricao(itensNfce.get(i).getProduto().getDescricao() != null
					? itensNfce.get(i).getProduto().getDescricao() : "");

			produto.setNcm(itensNfce.get(i).getProduto().getNcm() != null
					? itensNfce.get(i).getProduto().getNcm().toString() : "");

			// produto.setCfop(itensNfce.get(i).getCfop().toString() != null ?
			// itensNfce.get(i).getCfop().toString() : "");

			produto.setCfop("5102");

			produto.setUnidadeComercial(itensNfce.get(i).getProduto().getUnidadeComercial() != null
					? itensNfce.get(i).getProduto().getUnidadeComercial() : "");

			produto.setUnidadeTributavel(itensNfce.get(i).getProduto().getUnidadeComercial() != null
					? itensNfce.get(i).getProduto().getUnidadeComercial() : "");

			produto.setQuantidadeComercial(new BigDecimal(itensNfce.get(i).getQuantidade()) != null
					? new BigDecimal(itensNfce.get(i).getQuantidade()) : null);

			produto.setQuantidadeTributavel(new BigDecimal(itensNfce.get(i).getQuantidade()) != null
					? new BigDecimal(itensNfce.get(i).getQuantidade()) : null);

			produto.setValorUnitario(itensNfce.get(i).getProduto().getPrecoUnitario() != null
					? itensNfce.get(i).getProduto().getPrecoUnitario() : null);

			produto.setValorUnitarioTributavel(itensNfce.get(i).getProduto().getPrecoUnitario() != null
					? itensNfce.get(i).getProduto().getPrecoUnitario() : null);

			produto.setValorTotalBruto(itensNfce.get(i).getProduto().getPrecoUnitario()
					.multiply(new BigDecimal(itensNfce.get(i).getQuantidade())));

			produto.setCompoeValorNota(NFProdutoCompoeValorNota.SIM);

			// Se a configuração estiver no produto.
			if (Config.propertiesLoader().getProperty("imposto").equals("2")) {
				switch (itensNfce.get(i).getProduto().getCsosn()) {
				case "101":

					NFNotaInfoItemImpostoICMSSN101 icmssn101 = new NFNotaInfoItemImpostoICMSSN101();
					icmssn101.setSituacaoOperacaoSN(
							NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_COM_PERMISSAO_CREDITO);
					icmssn101.setOrigem(NFOrigem.NACIONAL);
					icmssn101
							.setPercentualAliquotaAplicavelCalculoCreditoSN(itensNfce.get(i).getProduto().getpCredSN());
					icmssn101.setValorCreditoICMSSN(itensNfce.get(i).getProduto().getvCredICMSSN());
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
					icmssn500.setValorBCICMSSTRetido(itensNfce.get(i).getProduto().getvBCSTRet());
					icmssn500.setValorICMSSTRetido(itensNfce.get(i).getProduto().getvICMSSTRet());
					icmssn500.setPercentualICMSSTRetido(itensNfce.get(i).getProduto().getpST());
					icms.setIcmssn500(icmssn500);
					imposto.setIcms(icms);

					produto.setCodigoEspecificadorSituacaoTributaria(itensNfce.get(i).getProduto().getCest());

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

					produto.setCodigoEspecificadorSituacaoTributaria(itensNfce.get(i).getProduto().getCest());

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

		// VERIRICAR SE O ID SERÁ SEM 1.
		lote.setIdLote("1");
		lote.setVersao(config.getVersao());
		lote.setIndicadorProcessamento(NFLoteIndicadorProcessamento.PROCESSAMENTO_ASSINCRONO);

		// Gabiarra para gerar o código Randomico e o Indentificador.
		NFGeraChave nfGeraChave = new NFGeraChave(nota);

		// Mostrando os valores gerados pela classe NFGeraChave.
		// System.out.println("----- Valores gerados pela classe NFGeraChave
		// ----- ");
		// System.out.println("Código Randomico gerado pela classe NFGeraChave:
		// " + nfGeraChave.geraCodigoRandomico());
		// System.out.println("Identificador gerado pela classe NFGeraChave: " +
		// nfGeraChave.getChaveAcesso());

		// Mostrando os valores de teste dos objetos.
		// System.out.println("----- Valores de teste dos objetos ----- ");
		// System.out.println("Código Randomico com valor de teste: " +
		// identificacao.getCodigoRandomico());
		// System.out.println("Identificador com valor de teste: " +
		// info.getIdentificador());

		// Seteando os valores gerados pela classe NFGeraChave.
		identificacao.setCodigoRandomico(nfGeraChave.geraCodigoRandomico());
		info.setIdentificador(nfGeraChave.getChaveAcesso());

		// Mostrando os valores atualizados.
		// System.out.println("----- Valores atualizados ----- ");
		// System.out.println("Código Randomico com valor atualizado: " +
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
		System.out.println(
				"Data Hr Emissao: " + lote.getNotas().get(0).getInfo().getIdentificacao().getDataHoraEmissao());
		System.out.println("Numero NF: " + lote.getNotas().get(0).getInfo().getIdentificacao().getNumeroNota());
		System.out.println("Serie NF: " + lote.getNotas().get(0).getInfo().getIdentificacao().getSerie());

		System.out.println(" ");

		System.out.println("----------DADOS APÓS TRANSMISSAO DA NF-e FIM--------------");

		NFLoteEnvioRetornoDados retorno = new WSFacade(config).enviaLote(lote);

		System.out.println(" ");

		System.out.println("LOTE ENVIADO!");

		System.out.println(" ");

		// RETORNO DAS INFORMAÇÕES GERADAS APÓS TRANSMISSÃO DA NF-CE.
		NFLoteConsultaRetorno retc = new WSFacade(config)
				.consultaLote(retorno.getRetorno().getInfoRecebimento().getRecibo(), DFModelo.NFCE);

		for (NFProtocolo prot : retc.getProtocolos()) {

			System.out.println("----------DADOS APÓS TRANSMISSAO DA NFC-e INICIO-----------");
			System.out.println(" ");
			System.out.println("Chave: " + prot.getProtocoloInfo().getChave());
			System.out.println("Número Protocolo: " + prot.getProtocoloInfo().getNumeroProtocolo());
			System.out.println("Status: " + prot.getProtocoloInfo().getStatus());
			System.out.println("Motivo: " + prot.getProtocoloInfo().getMotivo());
			System.out.println("Ambiente: " + prot.getProtocoloInfo().getAmbiente().getCodigo());
			System.out.println("DataRecebimento: " + prot.getProtocoloInfo().getDataRecebimento());
			System.out.println(" ");
			System.out.println("----------DADOS APÓS TRANSMISSAO DA NFC-e FIM--------------");

			nfce.setChave(prot.getProtocoloInfo().getChave());
			nfce.setNumProtocolo(prot.getProtocoloInfo().getNumeroProtocolo());
			nfce.setStatus(prot.getProtocoloInfo().getStatus());
			nfce.setRejeicaoMotivo(prot.getProtocoloInfo().getMotivo());
			nfce.setAmbienteRecebimento(prot.getProtocoloInfo().getAmbiente().getCodigo());
			nfce.setDataRecebimento(java.sql.Timestamp.valueOf(prot.getProtocoloInfo().getDataRecebimento()));

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
			notaProcessada.setProtocolo(prot);
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
