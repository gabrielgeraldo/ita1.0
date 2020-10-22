package br.com.ita.controle.config;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
//import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Configuracao;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.util.ControleNumeros;

@Named("configuracaoMB")
@ViewScoped
public class ConfiguracaoMB implements Serializable {

	private List<ControleNumeros> numerosNfe = null;

	private List<ControleNumeros> numerosPedidoVenda = null;

	private List<ControleNumeros> numerosNfce = null;

	@Inject
	private ControleNumerosDAO daoControleNumeros = null;

	private static final long serialVersionUID = 1L;

	// @Inject
	private String senhaCert;

	private String idCodigoSegurancaContribuinte;

	// @Inject
	private String codigoSegurancaContribuinte;

	// @Inject
	private String cnpj;

	// @Inject
	private String inscricaoEstadual;

	// @Inject
	private String razaoSocial;

	// @Inject
	private String nomeFantasia;

	private String ambiente;

	private String tpImp;

	private String imposto;

	@Inject
	private Configuracao configuracao;

	@Inject
	private ConfiguracaoDAO daoConfiguracao;

	private String tpImpVenda;

	private String codClient;

	@PostConstruct
	public void init() {
		senhaCert = Config.propertiesLoader().getProperty("senhaCert");
		idCodigoSegurancaContribuinte = Config.propertiesLoader().getProperty("idCodigoSegurancaContribuinte");
		codigoSegurancaContribuinte = Config.propertiesLoader().getProperty("codigoSegurancaContribuinte");
		cnpj = Config.propertiesLoader().getProperty("cnpj");
		inscricaoEstadual = Config.propertiesLoader().getProperty("inscricaoEstadual");
		razaoSocial = Config.propertiesLoader().getProperty("razaoSocial");
		nomeFantasia = Config.propertiesLoader().getProperty("nomeFantasia");
		ambiente = Config.propertiesLoader().getProperty("ambiente");
		tpImp = Config.propertiesLoader().getProperty("tpImp");
		tpImpVenda = Config.propertiesLoader().getProperty("tpImpVenda");
		imposto = Config.propertiesLoader().getProperty("imposto");
		codClient = Config.propertiesLoader().getProperty("codClient");

		configuracao = (daoConfiguracao.lerPorId(new Long(1)) != null ? daoConfiguracao.lerPorId(new Long(1))
				: configuracao);
	}

	public String listar() {
		return "/Configuracao/configuracaoEditar.xhtml?faces-redirect=true";
	}

	public String salvar() {

		Config.atualizaProperties("senhaCert", senhaCert);
		Config.atualizaProperties("idCodigoSegurancaContribuinte", idCodigoSegurancaContribuinte);
		Config.atualizaProperties("codigoSegurancaContribuinte", codigoSegurancaContribuinte);
		Config.atualizaProperties("ambiente", ambiente);
		Config.atualizaProperties("tpImp", tpImp);
		Config.atualizaProperties("tpImpVenda", tpImpVenda);
		Config.atualizaProperties("imposto", imposto);

		configuracao.setCodigo(new Long(1));
		daoConfiguracao.merge(configuracao);

		JSFUtil.retornarMensagemInfo("Configura��es atualizadas!", null, null);

		return "/home.xhtml?faces-redirect=true";
	}

	public void onRowEdit(RowEditEvent event) {

		try {

			daoControleNumeros.merge((ControleNumeros) event.getObject());

			JSFUtil.retornarMensagemInfo("N�mero atualizado.", null, null);

		} catch (Exception e) {

			JSFUtil.retornarMensagemErro("Error! N�mero n�o atualizado.", null, null);

		}

	}

	public List<ControleNumeros> getNumerosNfe() {

		if (numerosNfe == null) {
			numerosNfe = daoControleNumeros.buscaNumerosPorTabela("nfe");
		}

		return numerosNfe;
	}

	public void setNumerosNfe(List<ControleNumeros> numerosNfe) {
		this.numerosNfe = numerosNfe;
	}

	public ControleNumerosDAO getDaoControleNumeros() {
		return daoControleNumeros;
	}

	public void setDaoControleNumeros(ControleNumerosDAO daoControleNumeros) {
		this.daoControleNumeros = daoControleNumeros;
	}

	public String getSenhaCert() {
		return senhaCert;
	}

	public void setSenhaCert(String senhaCert) {
		this.senhaCert = senhaCert;
	}

	public String getIdCodigoSegurancaContribuinte() {
		return idCodigoSegurancaContribuinte;
	}

	public void setIdCodigoSegurancaContribuinte(String idCodigoSegurancaContribuinte) {
		this.idCodigoSegurancaContribuinte = idCodigoSegurancaContribuinte;
	}

	public String getCodigoSegurancaContribuinte() {
		return codigoSegurancaContribuinte;
	}

	public void setCodigoSegurancaContribuinte(String codigoSegurancaContribuinte) {
		this.codigoSegurancaContribuinte = codigoSegurancaContribuinte;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public List<ControleNumeros> getNumerosNfce() {

		if (numerosNfce == null) {
			numerosNfce = daoControleNumeros.buscaNumerosPorTabela("nfce");
		}

		return numerosNfce;
	}

	public void setNumerosNfce(List<ControleNumeros> numerosNfce) {
		this.numerosNfce = numerosNfce;
	}

	public String getTpImp() {
		return tpImp;
	}

	public void setTpImp(String tpImp) {
		this.tpImp = tpImp;
	}

	public List<ControleNumeros> getNumerosPedidoVenda() {

		if (numerosPedidoVenda == null) {
			numerosPedidoVenda = daoControleNumeros.buscaNumerosPorTabela("orc");
		}

		return numerosPedidoVenda;
	}

	public void setNumerosPedidoVenda(List<ControleNumeros> numerosPedidoVenda) {
		this.numerosPedidoVenda = numerosPedidoVenda;
	}

	public String getImposto() {
		return imposto;
	}

	public void setImposto(String imposto) {
		this.imposto = imposto;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public ConfiguracaoDAO getDaoConfiguracao() {
		return daoConfiguracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

	public void setDaoConfiguracao(ConfiguracaoDAO daoConfiguracao) {
		this.daoConfiguracao = daoConfiguracao;
	}

	public String getTpImpVenda() {
		return tpImpVenda;
	}

	public void setTpImpVenda(String tpImpVenda) {
		this.tpImpVenda = tpImpVenda;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

}
