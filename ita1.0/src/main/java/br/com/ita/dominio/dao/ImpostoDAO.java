package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Imposto;

public class ImpostoDAO extends JpaDAO<Imposto> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ImpostoDAO() {
		super();
	}

	public ImpostoDAO(EntityManager manager) {
		super(manager);
	}

	public Imposto lerPorDescricao(String descricao) {
		Imposto resultado;

		Query consulta = this.getEntityManager().createQuery("from Imposto u where u.descricao = :descricao");
		consulta.setParameter("descricao", descricao);

		try {
			resultado = (Imposto) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	public Imposto retornaImpostoUsado() {
		Imposto resultado;

		Query consulta = this.getEntityManager().createQuery("from Imposto u where u.codigo = 1");

		try {
			resultado = (Imposto) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
