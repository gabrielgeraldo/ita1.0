package br.com.ita.dominio.dao.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.util.Ncm;

public class NcmDAO extends JpaDAO<Ncm> implements Serializable {

	private static final long serialVersionUID = 1L;

	public NcmDAO() {
		super();
	}

	public NcmDAO(EntityManager manager) {
		super(manager);
	}

	// Fonte: https://www.youtube.com/watch?v=JmpX_TLwBTs
	@SuppressWarnings("unchecked")
	public List<Ncm> autoCompleteNcm(String ncm) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Ncm.class);

		if (StringUtils.isNoneBlank(ncm)) {
			// System.out.println("Ncm: " + ncm);
			criteria.add(Restrictions.ilike("ncm", ncm.toUpperCase(), MatchMode.START));
		}

		// System.out.println("autoCompleteNcm: " + criteria.list());

		return criteria.list();

	}

	public void criaNcm() {

		Query query = this.getEntityManager().createNativeQuery("Select * from ncm");

		// SE TABELA CFOP VAZIA
		if (query.getResultList().isEmpty()) {

			// CARGA DO CFOP.SQL
			String sql = null;

			try {
				sql = IOUtils.toString(new FileInputStream("c:/Ita/util/ncm.sql"));
				// System.out.println(sql);

			} catch (FileNotFoundException e) {

				JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
				System.out.println(e.getMessage());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				JSFUtil.retornarMensagemErro(null, "Erro ao carregar NCM, entre em contato com suporte.", null);
				System.out.println("Erro ao carregar NCM, entre em contato com suporte.");

			}

			// SE CARREGROU SQL
			if (sql != null) {

				try {

					boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

					if (!transacaoAtiva)
						this.abrirTransacao();

					this.getEntityManager().createNativeQuery(sql).executeUpdate();

					query = this.getEntityManager().createNativeQuery("Select * from ncm");

					if (!query.getResultList().isEmpty()) {
						JSFUtil.retornarMensagemInfo(null, "Carregando NCM! Aguarde...", null);
						System.out.println("Carregando NCM! Aguarde...");
					}

					if (!transacaoAtiva)
						this.gravarTransacao();

				} catch (Exception e) {

					this.desfazerTransacao();

					JSFUtil.retornarMensagemErro(null, "Erro carregar NCM, entre em contato com suporte.", null);
					System.out.println("Erro carregar NCM, entre em contato com suporte.");

					e.printStackTrace();
				}
			}

		}

	}
}
