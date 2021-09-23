package br.com.ita.controle.config;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.ConfiguracaoDAO;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.util.ControleNumeros;

@Named("configuracaoMB")
@ViewScoped
public class ConfiguracaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ControleNumeros> numerosNfe = null;

	private List<ControleNumeros> numerosPedidoVenda = null;

	private List<ControleNumeros> numerosNfce = null;

	@Inject
	private ControleNumerosDAO daoControleNumeros = null;

	@Inject
	private ConfiguracaoDAO configuracaoDao;

	@Inject
	private Configuracao configuracao;

	private List<Configuracao> configuracoes = null;

	private List<Configuracao> configuracoesFiltrados = null;

	@PostConstruct
	public void init() {

		Configuracao objetoDoBanco = this.configuracaoDao.lerPorId(new Long(1));
		this.setConfiguracao(objetoDoBanco);

	}

	public String listar() {
		return "/Configuracao/configuracaoListar.xhtml?faces-redirect=true";
	}

	public String salvar() {

		this.configuracaoDao.merge(this.getConfiguracao());
		// limpa a lista
		this.configuracoes = null;

		// limpar o objeto da p�gina
		this.setConfiguracao(new Configuracao());

		JSFUtil.retornarMensagemInfo("Configuracoes atualizadas!", null, null);

		return "/Configuracao/configuracaoListar.xhtml";
	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Configuracao objetoDoBanco = this.configuracaoDao.lerPorId(codigo);
		this.setConfiguracao(objetoDoBanco);

		return "/Configuracao/configuracaoEditar.xhtml";

	}

	public void onRowEdit(RowEditEvent event) {

		try {

			daoControleNumeros.merge((ControleNumeros) event.getObject());

			JSFUtil.retornarMensagemInfo("Numero atualizado.", null, null);

		} catch (Exception e) {

			JSFUtil.retornarMensagemErro("Erro! Numero nao atualizado.", null, null);

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

	public List<ControleNumeros> getNumerosNfce() {

		if (numerosNfce == null) {
			numerosNfce = daoControleNumeros.buscaNumerosPorTabela("nfce");
		}

		return numerosNfce;
	}

	public void setNumerosNfce(List<ControleNumeros> numerosNfce) {
		this.numerosNfce = numerosNfce;
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

	public ConfiguracaoDAO getConfiguracaoDao() {
		return configuracaoDao;
	}

	public void setConfiguracaoDao(ConfiguracaoDAO configuracaoDao) {
		this.configuracaoDao = configuracaoDao;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

	public List<Configuracao> getConfiguracoes() {

		if (this.configuracoes == null)
			this.configuracoes = this.configuracaoDao.lerTodos();

		return configuracoes;
	}

	public void setConfiguracoes(List<Configuracao> configuracoes) {
		this.configuracoes = configuracoes;
	}

	public List<Configuracao> getConfiguracoesFiltrados() {
		return configuracoesFiltrados;
	}

	public void setConfiguracoesFiltrados(List<Configuracao> configuracoesFiltrados) {
		this.configuracoesFiltrados = configuracoesFiltrados;
	}

}
