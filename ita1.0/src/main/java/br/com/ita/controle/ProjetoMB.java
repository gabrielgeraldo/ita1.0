package br.com.ita.controle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class ProjetoMB {
	
	
	public String listar() {

		return "/Cliente/clienteListar?faces-redirect=true";

	}

}
