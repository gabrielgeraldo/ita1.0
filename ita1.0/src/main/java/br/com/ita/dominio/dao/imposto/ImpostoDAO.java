package br.com.ita.dominio.dao.imposto;

import java.io.Serializable;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.imposto.Imposto;

public class ImpostoDAO extends JpaDAO<Imposto> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ImpostoDAO() {
		super();
	}

	public ImpostoDAO(EntityManager manager) {
		super(manager);
	}

}
