package br.com.ita.controle.orcamento.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.DFModelo;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFTipoEmissao;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaSituacaoOperacionalSimplesNacional;
import com.fincatto.documentofiscal.nfe400.classes.NFEndereco;
import com.fincatto.documentofiscal.nfe400.classes.NFFinalidade;
import com.fincatto.documentofiscal.nfe400.classes.NFModalidadeFrete;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.NFNotaInfoSituacaoTributariaPIS;
import com.fincatto.documentofiscal.nfe400.classes.NFOrigem;
import com.fincatto.documentofiscal.nfe400.classes.NFProcessoEmissor;
import com.fincatto.documentofiscal.nfe400.classes.NFProdutoCompoeValorNota;
import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;
import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.NFTipoImpressao;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteEnvio;
import com.fincatto.documentofiscal.nfe400.classes.lote.envio.NFLoteIndicadorProcessamento;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIdentificadorLocalDestinoOperacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorPresencaComprador;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNota;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoEmitente;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoICMSTotal;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoIdentificacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItem;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImposto;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINS;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSNaoTributavel;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoCOFINSOutrasOperacoes;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoItemImpostoICMS;
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
import com.fincatto.documentofiscal.nfe400.classes.nota.NFOperacaoConsumidorFinal;

import br.com.ita.controle.nfce.util.QRCode;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.Orcamento;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.empresa.Empresa;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

public class OrcamentoService implements Serializable {

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

	private NFNotaInfoPagamento pagamento = new NFNotaInfoPagamento();

	private List<NFNotaInfoPagamento> pagamentos = new ArrayList<NFNotaInfoPagamento>();

	private EstadoDAO daoEstado = new EstadoDAO();

	private MunicipioDAO daoMunicipio = new MunicipioDAO();

	private Orcamento orcamento = new Orcamento();

	private List<ItemOrcamento> itensOrcamento = new ArrayList<ItemOrcamento>();

	private Empresa empresa = new Empresa();

	private Configuracao configuracao = new Configuracao();

	public OrcamentoService(Orcamento orcamento, List<ItemOrcamento> itensOrcamento, Empresa empresa, Configuracao configuracao) {
		this.setOrcamento(orcamento);
		this.setItensOrcamento(itensOrcamento);
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

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<ItemOrcamento> getItensOrcamento() {
		return itensOrcamento;
	}

	public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
		this.itensOrcamento = itensOrcamento;
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

		if (orcamento.getCliente() != null) {

			destinatarioEndereco.setLogradouro(orcamento.getCliente().getEndereco());
			destinatarioEndereco.setNumero(orcamento.getCliente().getNumero());
			destinatarioEndereco.setComplemento(orcamento.getCliente().getComplemento());
			destinatarioEndereco.setBairro(orcamento.getCliente().getBairro());
			destinatarioEndereco.setCodigoMunicipio(orcamento.getCliente().getMunicipio().getCodigo().toString());
			destinatarioEndereco.setDescricaoMunicipio(orcamento.getCliente().getMunicipio().getNome());
			destinatarioEndereco.setUf(DFUnidadeFederativa.valueOfCodigo(orcamento.getCliente().getEstado().getUf()));
			destinatarioEndereco.setCep(orcamento.getCliente().getCep());
			destinatarioEndereco.setCodigoPais("1058");
			destinatarioEndereco.setDescricaoPais("BRASIL");
			destinatarioEndereco.setTelefone(orcamento.getCliente().getTelefone());

		}

	}

	public void geraDestinatario() {

		if (orcamento.getCliente() != null) {

			if (orcamento.getCliente().getTipo().equals("F")) {
				destinatario.setCpf(orcamento.getCliente().getCgc());
			} else if (orcamento.getCliente().getTipo().equals("J")) {
				destinatario.setCnpj(orcamento.getCliente().getCgc());
			}

			if (!orcamento.getCliente().getInscEst().isEmpty()) {
				destinatario.setInscricaoEstadual(orcamento.getCliente().getInscEst());
			}

			destinatario.setRazaoSocial(orcamento.getCliente().getNome());
			destinatario.setEndereco(destinatarioEndereco);
			destinatario.setIndicadorIEDestinatario(orcamento.getCliente().getNfIndicadorIEDestinatario());
			destinatario.setEmail(orcamento.getCliente().getEmail());

		}

	}

	public void geraNFNotaInfoIdentificacao() {

		identificacao.setUf(DFUnidadeFederativa.valueOfCodigo(empresa.getUf()));
		identificacao.setCodigoRandomico(this.gerarCodigoRandomico());
		identificacao.setNaturezaOperacao("ABCD");
		identificacao.setModelo(DFModelo.NFCE);
		identificacao.setSerie(String.valueOf(orcamento.getCodigo()));
		identificacao.setNumeroNota(String.valueOf(orcamento.getCodigo()));
		identificacao.setDataHoraEmissao(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
		identificacao.setTipo(NFTipo.SAIDA);
		identificacao.setIdentificadorLocalDestinoOperacao(NFIdentificadorLocalDestinoOperacao.OPERACAO_INTERNA);
		identificacao.setCodigoMunicipio("1111111");
		identificacao.setTipoImpressao(NFTipoImpressao.DANFE_NFCE);
		identificacao.setTipoEmissao(NFTipoEmissao.EMISSAO_NORMAL);
		identificacao.setDigitoVerificador(1);
		identificacao.setAmbiente(DFAmbiente.HOMOLOGACAO);
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

		pagamento.setDetalhamentoFormasPagamento(null);
		pagamentos.add(null);

	}

	public void geraNFNotaInfo() {

		info.setIdentificador("11111111111111111111111111111111111111111111");
		info.setVersao(new java.math.BigDecimal("3.10"));
		info.setIdentificacao(identificacao);
		info.setTransporte(transporte);
		info.setEmitente(emitente);

		if (orcamento.getCliente() != null)
			info.setDestinatario(destinatario);

		this.geraPagamentos();
		// info.setPagamentos(pagamentos);
		info.setPagamento(pagamento);

	}

	public void geraProdutos() {

		for (int i = 0; i < itensOrcamento.size(); i++) {

			item.setNumeroItem(i + 1);

			produto.setCodigo(itensOrcamento.get(i).getProduto().getCodigo().toString() != null
					? itensOrcamento.get(i).getProduto().getCodigo().toString()
					: "");

			produto.setCodigoDeBarras("SEM GTIN");

			produto.setCodigoDeBarrasTributavel("SEM GTIN");

			produto.setDescricao(itensOrcamento.get(i).getProduto().getDescricao() != null
					? itensOrcamento.get(i).getProduto().getDescricao()
					: "");

			produto.setNcm("00000000");

			produto.setCfop("5102");

			produto.setUnidadeComercial(itensOrcamento.get(i).getProduto().getUnidadeComercial() != null
					? itensOrcamento.get(i).getProduto().getUnidadeComercial()
					: "");

			produto.setUnidadeTributavel(itensOrcamento.get(i).getProduto().getUnidadeComercial() != null
					? itensOrcamento.get(i).getProduto().getUnidadeComercial()
					: "");

			produto.setQuantidadeComercial(new BigDecimal(itensOrcamento.get(i).getQuantidade()) != null
					? new BigDecimal(itensOrcamento.get(i).getQuantidade())
					: null);

			produto.setQuantidadeTributavel(new BigDecimal(itensOrcamento.get(i).getQuantidade()) != null
					? new BigDecimal(itensOrcamento.get(i).getQuantidade())
					: null);

			produto.setValorUnitario(itensOrcamento.get(i).getProduto().getPrecoUnitario() != null
					? itensOrcamento.get(i).getProduto().getPrecoUnitario()
					: null);

			produto.setValorUnitarioTributavel(itensOrcamento.get(i).getProduto().getPrecoUnitario() != null
					? itensOrcamento.get(i).getProduto().getPrecoUnitario()
					: null);

			produto.setValorTotalBruto(itensOrcamento.get(i).getProduto().getPrecoUnitario()
					.multiply(new BigDecimal(itensOrcamento.get(i).getQuantidade())));

			produto.setCompoeValorNota(NFProdutoCompoeValorNota.SIM);

			NFNotaInfoItemImpostoICMSSN102 icmssn102 = new NFNotaInfoItemImpostoICMSSN102();
			icmssn102.setSituacaoOperacaoSN(NFNotaSituacaoOperacionalSimplesNacional.TRIBUTADA_SEM_PERMISSAO_CREDITO);
			icmssn102.setOrigem(NFOrigem.NACIONAL);
			icms.setIcmssn102(icmssn102);
			imposto.setIcms(icms);

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
		icmsTotal.setValorTotalDosProdutosServicos(orcamento.getTotal());
		icmsTotal.setValorTotalFrete(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalSeguro(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalDesconto(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalII(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalIPI(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorPIS(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorCOFINS(new java.math.BigDecimal("0.00"));
		icmsTotal.setOutrasDespesasAcessorias(new java.math.BigDecimal("0.00"));
		icmsTotal.setValorTotalNFe(orcamento.getTotal());

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

		QRCode geraQRCode20 = new QRCode(nota, config);
		NFNotaInfoSuplementar nfs = new NFNotaInfoSuplementar();
		nfs.setUrlConsultaChaveAcesso(geraQRCode20.urlConsultaChaveAcesso());
		nfs.setQrCode(geraQRCode20.getQRCode());
		nota.setInfoSuplementar(nfs);

	}

	public String gerarXMLVenda() throws Exception {

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
		lote.setVersao("3.10");
		lote.setIndicadorProcessamento(NFLoteIndicadorProcessamento.PROCESSAMENTO_ASSINCRONO);

		// System.out.println("XML: " + lote.toString());

		return lote.toString();

	}

}
