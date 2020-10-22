package br.com.ita.dominio.dao.financeiro;

import java.io.Serializable;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.financeiro.MovimentacaoBancaria;

public class MovimentacaoBancariaDAO extends JpaDAO<MovimentacaoBancaria> implements Serializable {

	private static final long serialVersionUID = 1L;

	public MovimentacaoBancariaDAO() {
		super();
	}

	public MovimentacaoBancariaDAO(EntityManager manager) {
		super(manager);
	}
}
