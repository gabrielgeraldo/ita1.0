package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Fornecedor;

public class FornecedorDAO extends JpaDAO<Fornecedor> implements Serializable {

	private static final long serialVersionUID = 1L;

	public FornecedorDAO() {
		super();
	}

	public FornecedorDAO(EntityManager manager) {
		super(manager);
	}

	// Fonte: https://www.youtube.com/watch?v=JmpX_TLwBTs
	@SuppressWarnings("unchecked")
	public List<Fornecedor> autoCompleteFornecedor(String fornecedor) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Fornecedor.class);

		if (StringUtils.isNoneBlank(fornecedor)) {
			criteria.add(Restrictions.ilike("nomeFantasia", fornecedor.toUpperCase(), MatchMode.START));
		}

		return criteria.list();

	}

	public Fornecedor verificaSeFornecedorExiste(String cgc, String inscricaoEstadual, String razaoSocial) {
		Fornecedor resultado;

		Query consulta = this.getEntityManager()
				.createQuery("from Fornecedor u where u.cgc = :cgc or u.razaoSocial = :razaoSocial");
		consulta.setParameter("cgc", cgc);
		// consulta.setParameter("inscricaoEstadual", inscricaoEstadual);
		consulta.setParameter("razaoSocial", razaoSocial);

		try {
			resultado = (Fornecedor) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
