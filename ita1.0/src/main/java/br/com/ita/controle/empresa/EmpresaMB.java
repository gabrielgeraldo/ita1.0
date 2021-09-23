package br.com.ita.controle.empresa;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.dominio.dao.EmpresaDAO;
import br.com.ita.dominio.empresa.Empresa;

@Named("empresaMB")
@ViewScoped
public class EmpresaMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmpresaDAO empresaDao;

	@Inject
	private Empresa empresa;
	
	@PostConstruct
	public void init() {

		Empresa objetoDoBanco = this.empresaDao.lerPorId(new Long(1));
		this.setEmpresa(objetoDoBanco);

	}

	public EmpresaDAO getEmpresaDao() {
		return empresaDao;
	}

	public void setEmpresaDao(EmpresaDAO empresaDao) {
		this.empresaDao = empresaDao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	
}
