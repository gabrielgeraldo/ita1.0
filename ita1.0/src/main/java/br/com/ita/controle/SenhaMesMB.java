package br.com.ita.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.config.Configuracao;
import br.com.ita.dominio.dao.ConfiguracaoDAO;

@Named("senhaMesMB")
@RequestScoped
public class SenhaMesMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracaoDAO configuracaoDao;

	@Inject
	private Configuracao configuracao;
	
	@PostConstruct
	public void init() {

		Configuracao objetoDoBanco = this.configuracaoDao.lerPorId(new Long(1));
		this.setConfiguracao(objetoDoBanco);

		this.configuracao.setSenhaMes(null);
		
	}

	public String salvar() {
		
		this.configuracaoDao.merge(this.getConfiguracao());

		this.setConfiguracao(new Configuracao());

		JSFUtil.retornarMensagemInfo("Senha mês atualizada!", null, null);

		return "login.xhtml?faces-redirect=true";

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

}
