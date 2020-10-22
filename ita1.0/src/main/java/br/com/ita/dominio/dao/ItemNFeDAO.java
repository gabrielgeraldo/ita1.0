package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemNTFe;
import br.com.ita.dominio.NTFe;

public class ItemNFeDAO extends JpaDAO<ItemNTFe> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ItemNFeDAO() {
		super();
	}

	public ItemNFeDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<ItemNTFe> buscaItens(NTFe nFeSaida) {

		String hql = "from ItemNTFe item where item.nfe =:nFeSaida";

		List<ItemNTFe> lista = this.getEntityManager().createQuery(hql).setParameter("nFeSaida", nFeSaida)
				.getResultList();

		return lista;
	}

	public void deletaItens(NTFe nFeSaida) {

		String hql = "delete ItemNTFe item where item.nfe =:nFeSaida";

		this.getEntityManager().createQuery(hql).setParameter("nFeSaida", nFeSaida).executeUpdate();

	}

}
