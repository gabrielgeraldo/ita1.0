package br.com.ita.dominio.dao;

import java.io.Serializable;
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
import br.com.ita.dominio.NFEntrada;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.ItemNFEntrada;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.dao.filtros.FiltroNFEntrada;

public class NFEntradaDAO extends JpaDAO<NFEntrada> implements Serializable {

	private static final long serialVersionUID = 1L;

	public NFEntradaDAO() {
		super();
	}

	public NFEntradaDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<NFEntrada> consultar(FiltroNFEntrada filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(NFEntrada.class);

		criteria.setMaxResults(50);

		if (filtro.getNumero() != null) {
			criteria.add(Restrictions.eq("numero", filtro.getNumero()));
		}

		if (filtro.getDataEmissaoInicio() != null && filtro.getDataEmissaoFim() != null) {
			criteria.add(Restrictions.between("emissao", filtro.getDataEmissaoInicio(), filtro.getDataEmissaoFim()));
		}

		return criteria.list();
	}

	public void salvarNFEntrada(NFEntrada nfEntrada, List<ItemNFEntrada> itensNFEntrada) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			NFEntrada nfEntradaSalva = this.getEntityManager().merge(nfEntrada);

			for (ItemNFEntrada item : itensNFEntrada) {
				item.setNfEntrada(nfEntradaSalva);

				this.getEntityManager().merge(item);

				ProdutoDAO produtoDao = new ProdutoDAO();
				// Produto produto = item.getProduto();
				Produto produto = produtoDao.lerPorId(item.getProduto().getCodigo());
				produto.setQtdEstq(produto.getQtdEstq() + item.getQuantidade());
				produto.setPrecoCusto(item.getPrecoCusto());
				produto.setPrecoUnitario(item.getPrecoVenda());

				this.getEntityManager().merge(produto);

			}

			if (!transacaoAtiva) {
				this.gravarTransacao();

				JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

			}

		} catch (Exception e) {

			// O Hibernate faz o rollback da númeração.
			this.desfazerTransacao();
			System.out.println(e);

			e.printStackTrace();

			JSFUtil.retornarMensagemFatal(null, "Erro ao salvar! Entre em contato com suporte.", null);

		}

	}

	// Fonte: https://www.youtube.com/watch?v=JmpX_TLwBTs
	@SuppressWarnings("unchecked")
	public List<NFEntrada> autoCompleteNFEntrada(String nfEntrada) {
		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(NFEntrada.class);

		if (StringUtils.isNoneBlank(nfEntrada)) {

			try {

				Integer numero = Integer.valueOf(nfEntrada);

				criteria.add(Restrictions.eq("numero", numero));

			} catch (Exception e) {

			}

		}

		return criteria.list();

	}

	public NFEntrada lerPorChave(String chave) {
		NFEntrada resultado;

		Query consulta = this.getEntityManager().createQuery("from NFEntrada u where u.chave = :chave");
		consulta.setParameter("chave", chave);

		try {
			resultado = (NFEntrada) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	public NFEntrada lerPorNumeroSerieFornecedor(Integer numero, Integer serie, Fornecedor fornecedor) {
		NFEntrada resultado;

		Query consulta = this.getEntityManager().createQuery(
				"from NFEntrada u where u.numero = :numero and u.serie = :serie and u.fornecedor = :fornecedor");
		consulta.setParameter("numero", numero);
		consulta.setParameter("serie", serie);
		consulta.setParameter("fornecedor", fornecedor);

		try {
			resultado = (NFEntrada) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	public void excluirNFEntrada(NFEntrada nfEntrada, List<ItemNFEntrada> itensNFEntrada) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			for (ItemNFEntrada item : itensNFEntrada) {

				Produto produto = item.getProduto();
				produto.setQtdEstq(produto.getQtdEstq() - item.getQuantidade());

				this.getEntityManager().merge(produto);

				this.getEntityManager().remove(item);

			}
			
			this.getEntityManager().remove(nfEntrada);
			
			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (PersistenceException e) {

			JSFUtil.retornarMensagemAviso(null, "Não é possível excluir, este registro pode estar associado a outro.",
					null);

			e.printStackTrace();

			this.desfazerTransacao();

		} catch (Exception e) {

			// O Hibernate faz o rollback da númeração.
			this.desfazerTransacao();

			//System.out.println(e);
			//System.out.println(e.getMessage());
			//System.out.println(e.getStackTrace());

			e.printStackTrace();

			JSFUtil.retornarMensagemFatal(null, "Erro ao realizar exclusão.", null);

		}

	}
}
