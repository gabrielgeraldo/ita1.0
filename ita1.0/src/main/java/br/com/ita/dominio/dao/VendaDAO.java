package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemVenda;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.Venda;
import br.com.ita.dominio.dao.filtros.FiltroVenda;

public class VendaDAO extends JpaDAO<Venda> implements Serializable {

	private static final long serialVersionUID = 1L;

	public VendaDAO() {
		super();
	}

	public VendaDAO(EntityManager manager) {
		super(manager);
	}

	public Venda salvarVenda(Venda venda, List<ItemVenda> itensVenda) {

		Venda vendaSalva = null;

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			vendaSalva = this.getEntityManager().merge(venda);
			// System.out.println("Codigo da venda salva: " +
			// vendaSalva.getCodigo());

			for (ItemVenda item : itensVenda) {
				item.setVenda(vendaSalva);

				this.getEntityManager().merge(item);

				Produto produto = item.getProduto();
				produto.setQtdEstq(produto.getQtdEstq() - item.getQuantidade());

				this.getEntityManager().merge(produto);

			}

			if (!transacaoAtiva)
				this.gravarTransacao();

			// System.out.println("Venda finalizada com sucesso!");

		} catch (Exception e) {

			this.desfazerTransacao();

			System.out.println("Erro ao salvar venda: " + e);

			JSFUtil.retornarMensagemErro(null, "Erro ao salvar venda: " + e.getMessage(), null);

		}

		return vendaSalva;

	}

	public void cancelarVenda(Venda venda) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			Venda vendaCancelada = this.getEntityManager().merge(venda);
			System.out.println("Codigo da venda cancelada: " + vendaCancelada.getCodigo());

			Query query = this.getEntityManager().createQuery("FROM ItemVenda WHERE venda = :venda");
			query.setParameter("venda", venda);

			@SuppressWarnings("unchecked")
			List<ItemVenda> results = (List<ItemVenda>) query.getResultList();

			for (ItemVenda item : results) {

				Produto produto = item.getProduto();
				produto.setQtdEstq(produto.getQtdEstq() + item.getQuantidade());

				this.getEntityManager().merge(produto);

			}

			if (!transacaoAtiva)
				this.gravarTransacao();

			JSFUtil.retornarMensagemInfo("Venda cancelada com sucesso!", null, null);
			System.out.println("Venda cancelada com sucesso");

		} catch (Exception e) {

			this.desfazerTransacao();

			JSFUtil.retornarMensagemErro("Falha ao cancelar venda!", null, null);

			System.out.println("Falha ao cancelar venda: " + e);
		}
	}

	public List<Object[]> lerVendasDinheiro() {

		Query query = this.getEntityManager().createNativeQuery(
				"SELECT Sum(total) AS total, to_char((CURRENT_DATE), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 1"
						+ "AND    To_char(data, 'YYYY-MM-DD') = To_char( Now(), 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '1 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 1"
						+ "AND    To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '1 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '2 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 1"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '2 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '3 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 1"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '3 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '4 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 1"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '4 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA';");

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		return results;

	}

	public List<Object[]> lerNFe() {

		Query query = this.getEntityManager().createNativeQuery(
				"SELECT Sum(total) AS total, to_char((CURRENT_DATE), 'DD-MM-YYYY' ) AS data FROM nfe WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = To_char( Now(), 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '1 day'), 'DD-MM-YYYY' ) AS data FROM nfe WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '1 day', 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '2 day'), 'DD-MM-YYYY' ) AS data FROM nfe WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '2 day', 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '3 day'), 'DD-MM-YYYY' ) AS data FROM nfe WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '3 day', 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '4 day'), 'DD-MM-YYYY' ) AS data FROM nfe WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '4 day', 'YYYY-MM-DD' );");

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		return results;

	}

	public List<Object[]> lerNFCe() {

		Query query = this.getEntityManager().createNativeQuery(
				"SELECT Sum(total) AS total, to_char((CURRENT_DATE), 'DD-MM-YYYY' ) AS data FROM nfce WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = To_char( Now(), 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '1 day'), 'DD-MM-YYYY' ) AS data FROM nfce WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '1 day', 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '2 day'), 'DD-MM-YYYY' ) AS data FROM nfce WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '2 day', 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '3 day'), 'DD-MM-YYYY' ) AS data FROM nfce WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '3 day', 'YYYY-MM-DD' )"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '4 day'), 'DD-MM-YYYY' ) AS data FROM nfce WHERE"
						+ " To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '4 day', 'YYYY-MM-DD' );");

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		return results;

	}

	public List<Object[]> lerVenda() {

		Query query = this.getEntityManager().createNativeQuery(
				"SELECT Sum(total) AS total, to_char((CURRENT_DATE), 'DD-MM-YYYY' ) AS data FROM venda WHERE "
						+ "To_char(data, 'YYYY-MM-DD') = To_char( Now(), 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '1 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE "
						+ "To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '1 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '2 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE "
						+ "to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '2 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '3 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE "
						+ "to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '3 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '4 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE "
						+ "to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '4 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA';");

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		return results;

	}

	public List<Object[]> lerVendasCartao() {

		Query query = this.getEntityManager().createNativeQuery(
				"SELECT Sum(total) AS total, to_char((CURRENT_DATE), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 2"
						+ "AND    To_char(data, 'YYYY-MM-DD') = To_char( Now(), 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '1 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 2"
						+ "AND    To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '1 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '2 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 2"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '2 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '3 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 2"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '3 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '4 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo = 2"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '4 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA';");

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		return results;

	}

	public List<Object[]> lerVendasOutras() {

		Query query = this.getEntityManager().createNativeQuery(
				"SELECT Sum(total) AS total, to_char((CURRENT_DATE), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo <> 1"
						+ "AND    formapagamento_codigo <> 2"
						+ "AND    To_char(data, 'YYYY-MM-DD') = To_char( Now(), 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '1 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo <> 1"
						+ "AND    formapagamento_codigo <> 2"
						+ "AND    To_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '1 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '2 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo <> 1"
						+ "AND    formapagamento_codigo <> 2"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '2 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '3 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo <> 1"
						+ "AND    formapagamento_codigo <> 2"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '3 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL "

						+ "SELECT Sum(total), to_char((CURRENT_DATE - interval '4 day'), 'DD-MM-YYYY' ) AS data FROM venda WHERE formapagamento_codigo <> 1"
						+ "AND    formapagamento_codigo <> 2"
						+ "AND    to_char(data, 'YYYY-MM-DD') = to_char( CURRENT_DATE - interval '4 day', 'YYYY-MM-DD' )"
						+ "AND    situacao = 'FINALIZADA';");

		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();

		return results;
	}

	public List<Object> lerVendasTodasMes() {

		Query query = this.getEntityManager()
				.createNativeQuery("SELECT Sum(total) AS total" + " FROM   venda WHERE  formapagamento_codigo = 1"
						+ "AND    DATE_PART('MONTH', data) = DATE_PART('MONTH', CURRENT_TIMESTAMP)"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL"

						+ " SELECT Sum(total) AS total" + " FROM   venda WHERE  formapagamento_codigo = 2"
						+ "AND    DATE_PART('MONTH', data) = DATE_PART('MONTH', CURRENT_TIMESTAMP)"
						+ "AND    situacao = 'FINALIZADA'"

						+ "UNION ALL"

						+ " SELECT Sum(total) AS total"
						+ " FROM   venda WHERE  formapagamento_codigo <> 1 AND    formapagamento_codigo <> 2"
						+ "AND    DATE_PART('MONTH', data) = DATE_PART('MONTH', CURRENT_TIMESTAMP)"
						+ "AND    situacao = 'FINALIZADA'");

		@SuppressWarnings("unchecked")
		List<Object> results = query.getResultList();

		return results;
	}

	public List<Object> lerVendasTodasMesNFeNFCeVenda() {

		Query query = this.getEntityManager().createNativeQuery("SELECT Sum(total) AS total FROM nfe WHERE "
				+ "DATE_PART('MONTH', data) = DATE_PART('MONTH', CURRENT_TIMESTAMP)"

				+ "UNION ALL"

				+ " SELECT Sum(total) AS total FROM  nfce WHERE "
				+ "DATE_PART('MONTH', data) = DATE_PART('MONTH', CURRENT_TIMESTAMP)"

				+ "UNION ALL"

				+ " SELECT Sum(total) AS total FROM  venda WHERE "
				+ "DATE_PART('MONTH', data) = DATE_PART('MONTH', CURRENT_TIMESTAMP)"
				+ "AND    situacao = 'FINALIZADA'");

		@SuppressWarnings("unchecked")
		List<Object> results = query.getResultList();

		return results;
	}

	@SuppressWarnings("unchecked")
	public List<Venda> consultar(FiltroVenda filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Venda.class);

		criteria.setMaxResults(50);

		if (filtro.getCodigo() != null) {
			criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}

		if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
		}

		return criteria.list();
	}

}