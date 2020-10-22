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
import br.com.ita.dominio.Cliente;

public class ClienteDAO extends JpaDAO<Cliente> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ClienteDAO() {
		super();
	}

	public ClienteDAO(EntityManager manager) {
		super(manager);
	}

	// Fonte: https://www.youtube.com/watch?v=JmpX_TLwBTs
	@SuppressWarnings("unchecked")
	public List<Cliente> autoCompleteCliente(String cliente) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		if (StringUtils.isNoneBlank(cliente)) {
			criteria.add(Restrictions.ilike("nome", cliente.toUpperCase(), MatchMode.START));
		}

		return criteria.list();

	}

	public Cliente verificaSeClienteExiste(String cgc, String inscEst, String nome) {
		Cliente resultado;

		Query consulta = this.getEntityManager().createQuery(
				"from Cliente u where u.cgc = :cgc or u.nome = :nome");
		consulta.setParameter("cgc", cgc);
		//consulta.setParameter("inscEst", inscEst);
		consulta.setParameter("nome", nome);

		try {
			resultado = (Cliente) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
