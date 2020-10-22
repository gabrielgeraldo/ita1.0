package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemVenda;
import br.com.ita.dominio.Venda;

public class ItemVendaDAO extends JpaDAO<ItemVenda> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ItemVendaDAO() {
		super();
	}

	public ItemVendaDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<ItemVenda> buscaItens(Venda venda) {

		String hql = "from ItemVenda item where item.venda =:venda";

		List<ItemVenda> lista = this.getEntityManager().createQuery(hql).setParameter("venda", venda).getResultList();

		return lista;
	}
}
