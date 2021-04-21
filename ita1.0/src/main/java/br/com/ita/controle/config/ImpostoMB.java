package br.com.ita.controle.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;

import br.com.ita.dominio.Imposto;

@Named("impostoMB")
@RequestScoped
public class ImpostoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Imposto imposto;

	private List<NFRegimeTributario> regimeTributarios = null;

	public String listar() {
		
		return "/Configuracao/impostoEditar.xhtml?faces-redirect=true";
	}

	public Imposto getImposto() {
		return imposto;
	}

	public void setImposto(Imposto imposto) {
		this.imposto = imposto;
	}

	public List<NFRegimeTributario> getRegimeTributarios() {

		if (this.regimeTributarios == null)
			this.regimeTributarios = Arrays.asList(NFRegimeTributario.values());

		return regimeTributarios;
	}

	public void setRegimeTributarios(List<NFRegimeTributario> regimeTributarios) {
		this.regimeTributarios = regimeTributarios;
	}

}
