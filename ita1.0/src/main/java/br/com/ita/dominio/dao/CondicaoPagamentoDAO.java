package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.CondicaoPagamento;

public class CondicaoPagamentoDAO extends JpaDAO<CondicaoPagamento> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CondicaoPagamentoDAO() {
		super();
	}

	public CondicaoPagamentoDAO(EntityManager manager) {
		super(manager);
	}

	public CondicaoPagamento lerPorDescricao(String descricao) {
		CondicaoPagamento resultado;

		Query consulta = this.getEntityManager().createQuery("from CondicaoPagamento u where u.descricao = :descricao");
		consulta.setParameter("descricao", descricao);

		try {
			resultado = (CondicaoPagamento) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
