package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemOrcamento;
import br.com.ita.dominio.Orcamento;
import br.com.ita.dominio.dao.filtros.FiltroOrcamento;

public class OrcamentoDAO extends JpaDAO<Orcamento> implements Serializable {

	private static final long serialVersionUID = 1L;

	public OrcamentoDAO() {
		super();
	}

	public OrcamentoDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<Orcamento> consultar(FiltroOrcamento filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Orcamento.class);

		criteria.setMaxResults(50);

		if (filtro.getCodigo() != null) {
			criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}

		if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
		}

		return criteria.list();
	}

	public Orcamento salvarOrcamento(Orcamento orcamento, List<ItemOrcamento> itensOrcamento) throws Exception {

		Orcamento orcamentoSalvo = null;

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			orcamentoSalvo = this.getEntityManager().merge(orcamento);

			for (ItemOrcamento item : itensOrcamento) {
				item.setOrcamento(orcamentoSalvo);

				this.getEntityManager().merge(item);

			}

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (Exception e) {

			this.desfazerTransacao();

			System.out.println("Erro ao salvar orçamento: " + e);

			JSFUtil.retornarMensagemErro(null, "Erro ao salvar orçamento: " + e.getMessage(), null);

		}

		return orcamentoSalvo;

	}

	// Fonte: https://www.youtube.com/watch?v=JmpX_TLwBTs
	@SuppressWarnings("unchecked")
	public List<Orcamento> autoCompleteOrcamento(String orcamento) {

		Query query = null;
		List<Orcamento> results = new ArrayList<Orcamento>();

		if (StringUtils.isNoneBlank(orcamento)) {

			try {

				Long codigo = Long.parseLong(orcamento);

				query = this.getEntityManager().createNativeQuery("SELECT * "
						+ "FROM ORCAMENTO "
						+ "LEFT JOIN NFE ON "
						+ "         ORCAMENTO.CODIGO = NFE.ORCAMENTO_CODIGO "
						+ "LEFT JOIN NFCE ON "
						+ "         ORCAMENTO.CODIGO = NFCE.ORCAMENTO_CODIGO "
						+ "LEFT JOIN VENDA ON "
						+ "         ORCAMENTO.CODIGO = VENDA.ORCAMENTO_CODIGO "
						+ "WHERE NFE.ORCAMENTO_CODIGO IS NULL "
						+ "AND NFCE.ORCAMENTO_CODIGO IS NULL "
						+ "AND VENDA.ORCAMENTO_CODIGO IS NULL "
						+ "AND ORCAMENTO.CODIGO=:CODIGO ", Orcamento.class);
				query.setParameter("CODIGO", codigo);

				results = query.getResultList();

			} catch (Exception e) {
				e.getStackTrace();
			}

		}

		return results;

	}

	public Orcamento lerPorNumero(Integer numero) {
		Orcamento resultado;

		Query consulta = this.getEntityManager().createQuery("from Orcamento u where u.numero = :numero");
		consulta.setParameter("numero", numero);

		try {
			resultado = (Orcamento) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	public void excluirOrcamento(Orcamento orcamento, List<ItemOrcamento> itensOrcamento) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			for (ItemOrcamento item : itensOrcamento) {

				this.getEntityManager().remove(item);

			}

			this.getEntityManager().remove(orcamento);

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (PersistenceException e) {

			JSFUtil.retornarMensagemAviso(null, "N�o � poss�vel excluir, este registro pode estar associado a outro.",
					null);

			this.desfazerTransacao();

		} catch (Exception e) {

			// O Hibernate faz o rollback da n�mera��o.
			this.desfazerTransacao();
			System.out.println(e);

		}

	}
}
