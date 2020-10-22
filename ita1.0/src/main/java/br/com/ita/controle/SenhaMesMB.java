package br.com.ita.controle;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.ita.controle.config.Config;

@ManagedBean(name = "senhaMesMB")
@ViewScoped
public class SenhaMesMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private String senha;

	public String salvar() {
		Config.atualizaProperties("senhaMes", senha);
		return "login.xhtml?faces-redirect=true";
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
