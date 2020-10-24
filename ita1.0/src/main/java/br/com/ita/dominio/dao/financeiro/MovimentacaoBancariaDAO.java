package br.com.ita.dominio.dao.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	public BigDecimal getTotalEntrada(String mesAno) {

		String hql = "select sum(valorEntrada) from MovimentacaoBancaria where to_char(dataMovimentacao, 'MM/YYYY') = :mesAno";
		Query query = this.getEntityManager().createQuery(hql);
		query.setParameter("mesAno", mesAno);

		return (BigDecimal) query.getSingleResult();

	}

	public BigDecimal getTotalSaida(String mesAno) {

		String hql = "select sum(valorSaida) from MovimentacaoBancaria where to_char(dataMovimentacao, 'MM/YYYY') = :mesAno";
		Query query = this.getEntityManager().createQuery(hql);
		query.setParameter("mesAno", mesAno);

		return (BigDecimal) query.getSingleResult();

	}
}
