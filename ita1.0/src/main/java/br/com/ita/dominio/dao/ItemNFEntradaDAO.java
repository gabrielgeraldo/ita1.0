package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemNFEntrada;
import br.com.ita.dominio.NFEntrada;

public class ItemNFEntradaDAO extends JpaDAO<ItemNFEntrada> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ItemNFEntradaDAO() {
		super();
	}

	public ItemNFEntradaDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<ItemNFEntrada> buscaItens(NFEntrada nfEntrada) {

		String hql = "from ItemNFEntrada item where item.nfEntrada =:nfEntrada";

		List<ItemNFEntrada> lista = this.getEntityManager().createQuery(hql).setParameter("nfEntrada", nfEntrada)
				.getResultList();

		return lista;
	}
	
}
