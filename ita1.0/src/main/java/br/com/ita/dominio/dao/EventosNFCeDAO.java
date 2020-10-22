package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.EventosNFCe;
import br.com.ita.dominio.NTFCe;

public class EventosNFCeDAO extends JpaDAO<EventosNFCe> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EventosNFCeDAO() {
		super();
	}

	public EventosNFCeDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<EventosNFCe> buscaEventos(NTFCe nfce) {

		String hql = "from EventosNFCe eventos where eventos.nfce =:nfce";

		List<EventosNFCe> lista = this.getEntityManager().createQuery(hql).setParameter("nfce", nfce).getResultList();

		return lista;
	}

}
