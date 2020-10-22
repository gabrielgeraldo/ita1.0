package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Categoria;

public class CategoriaDAO extends JpaDAO<Categoria> implements Serializable {

	private static final long serialVersionUID = 1L;

	public CategoriaDAO() {
		super();
	}

	public CategoriaDAO(EntityManager manager) {
		super(manager);
	}

	public Categoria lerPorDescricao(String descricao) {
		Categoria resultado;

		Query consulta = this.getEntityManager().createQuery("from Categoria u where u.descricao = :descricao");
		consulta.setParameter("descricao", descricao);

		try {
			resultado = (Categoria) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
