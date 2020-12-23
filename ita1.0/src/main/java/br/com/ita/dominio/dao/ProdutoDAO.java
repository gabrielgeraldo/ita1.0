package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.dao.filtros.FiltroProduto;

public class ProdutoDAO extends JpaDAO<Produto> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ProdutoDAO() {
		super();
	}

	public ProdutoDAO(EntityManager manager) {
		super(manager);
	}

	public void salvarProduto(Produto produto) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			this.getEntityManager().persist(produto);

			if (!transacaoAtiva)
				this.gravarTransacao();

			JSFUtil.retornarMensagemInfo("Registro salvo com sucesso!", null, null);

		} catch (Exception e) {

			this.desfazerTransacao();

			JSFUtil.retornarMensagemFatal("Falha ao excluir este registro.", null, null);

			System.out.println(e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<Produto> consultar(FiltroProduto filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		criteria.setMaxResults(50);

		if (filtro.getTipoPesquisaProduto() == TipoPesquisaProduto.CODIGO) {
			if (StringUtils.isNotEmpty(filtro.getCodigo())) {
				criteria.add(Restrictions.ilike("codigo", filtro.getCodigo(), MatchMode.START));
			}

		}

		if (filtro.getTipoPesquisaProduto() == TipoPesquisaProduto.CODIGOBARRAS) {
			if (StringUtils.isNotEmpty(filtro.getCodigoBarras())) {
				criteria.add(Restrictions.ilike("codigoBarras", filtro.getCodigoBarras(), MatchMode.START));
			}

		}

		if (filtro.getTipoPesquisaProduto() == TipoPesquisaProduto.DESCRICAO) {
			if (StringUtils.isNotEmpty(filtro.getDescricao())) {
				criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
			}

		}

		// return criteria.addOrder(Order.desc("codigo")).list();

		return criteria.list();
	}

	// Fonte: https://www.youtube.com/watch?v=JmpX_TLwBTs
	@SuppressWarnings("unchecked")
	public List<Produto> autoCompleteProdutoPorDescricao(String produto) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNoneBlank(produto)) {
			criteria.add(Restrictions.ilike("descricao", produto, MatchMode.ANYWHERE));
		}

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<Produto> autoCompleteProdutoPorCodigo(String produto) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNoneBlank(produto)) {
			criteria.add(Restrictions.ilike("codigo", produto.toUpperCase(), MatchMode.START));
		}

		return criteria.list();

	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> autoCompleteProdutoPorCodigoDeBarras(String produto) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNoneBlank(produto)) {
			criteria.add(Restrictions.ilike("codigoBarras", produto.toUpperCase(), MatchMode.START));
		}

		return criteria.list();

	}

	public Long contaRegistros() {

		Session session = this.getEntityManager().unwrap(Session.class);

		Long cont = (long) ((Long) session.createQuery("select count(*) from Produto").iterate().next()).intValue();

		return cont;

	}

	public Long contaProdutosSemNcm() {

		Session session = this.getEntityManager().unwrap(Session.class);

		Long cont = (long) ((Long) session.createQuery("select count(*) from Produto where ncm = ''").iterate().next())
				.intValue();

		return cont;

	}

}
