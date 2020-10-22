package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.EventosNFe;
import br.com.ita.dominio.NTFe;

public class EventosNFeDAO extends JpaDAO<EventosNFe> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EventosNFeDAO() {
		super();
	}

	public EventosNFeDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<EventosNFe> buscaEventos(NTFe nfe) {

		String hql = "from EventosNFe eventos where eventos.nfe =:nfe";

		List<EventosNFe> lista = this.getEntityManager().createQuery(hql).setParameter("nfe", nfe).getResultList();

		return lista;
	}

}
