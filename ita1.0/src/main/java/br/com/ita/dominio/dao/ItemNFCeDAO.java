package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemNTFCe;
import br.com.ita.dominio.NTFCe;

public class ItemNFCeDAO extends JpaDAO<ItemNTFCe> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ItemNFCeDAO() {
		super();
	}

	public ItemNFCeDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<ItemNTFCe> buscaItens(NTFCe nt) {

		String hql = "from ItemNTFCe item where item.nfce =:nt";

		List<ItemNTFCe> lista = this.getEntityManager().createQuery(hql).setParameter("nt", nt).getResultList();

		return lista;
	}

}
