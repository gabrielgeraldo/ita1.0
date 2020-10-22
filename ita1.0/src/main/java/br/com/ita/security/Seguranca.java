package br.com.ita.security;

//import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
//import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

//@Named
//@RequestScoped
public class Seguranca {

	public String getNomeUsuario() {
		String nome = null;

		MyUserPrincipal usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsername();
		}

		System.out.println("Olá " + nome + "!");

		return nome;
	}

	private MyUserPrincipal getUsuarioLogado() {
		MyUserPrincipal usuario = null;

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal();

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (MyUserPrincipal) auth.getPrincipal();
		}

		return usuario;
	}

}