package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.empresa.Empresa;

public class EmpresaDAO extends JpaDAO<Empresa> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EmpresaDAO() {
		super();
	}

	public EmpresaDAO(EntityManager manager) {
		super(manager);
	}

}
