package br.com.ita.dominio.dao.financeiro;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.financeiro.Banco;

public class BancoDAO extends JpaDAO<Banco> implements Serializable {

	private static final long serialVersionUID = 1L;

	public BancoDAO() {
		super();
	}

	public BancoDAO(EntityManager manager) {
		super(manager);
	}

	public Banco verificaSeBancoExiste(int numeroAgencia, int numeroConta, String nomeAgencia) {
		Banco resultado;

		Query consulta = this.getEntityManager().createQuery(
				"from Banco u where u.numeroAgencia = :numeroAgencia or u.numeroConta = :numeroConta or u.nomeAgencia = :nomeAgencia");
		consulta.setParameter("numeroAgencia", numeroAgencia);
		consulta.setParameter("numeroConta", numeroConta);
		consulta.setParameter("nomeAgencia", nomeAgencia);

		try {
			resultado = (Banco) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
