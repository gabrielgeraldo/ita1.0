package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.Orcamento;

public class ItemOrcamentoDAO extends JpaDAO<ItemOrcamento> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ItemOrcamentoDAO() {
		super();
	}

	public ItemOrcamentoDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<ItemOrcamento> buscaItens(Orcamento orcamento) {

		String hql = "from ItemOrcamento item where item.orcamento =:orcamento";

		List<ItemOrcamento> lista = this.getEntityManager().createQuery(hql).setParameter("orcamento", orcamento)
				.getResultList();

		return lista;
	}

}
