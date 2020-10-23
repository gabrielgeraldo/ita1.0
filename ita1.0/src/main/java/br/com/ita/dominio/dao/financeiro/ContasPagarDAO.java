package br.com.ita.dominio.dao.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.DataParaConsulta;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.StatusTitulo;
import br.com.ita.dominio.dao.filtros.FiltroContasPagar;
import br.com.ita.dominio.financeiro.Banco;
import br.com.ita.dominio.financeiro.ContasPagar;
import br.com.ita.dominio.financeiro.MovimentacaoBancaria;

public class ContasPagarDAO extends JpaDAO<ContasPagar> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ContasPagarDAO() {
		super();
	}

	public ContasPagarDAO(EntityManager manager) {
		super(manager);
	}

	public ContasPagar lerPorNumeroFornecedor(Integer numero, Fornecedor fornecedor) {
		ContasPagar resultado;

		Query consulta = this.getEntityManager()
				.createQuery("from ContasPagar u where u.numero = :numero and u.fornecedor = :fornecedor");
		consulta.setParameter("numero", numero);
		consulta.setParameter("fornecedor", fornecedor);

		try {
			resultado = (ContasPagar) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<ContasPagar> buscaContasPagarNaoBaixadas() {

		String hql = "from ContasPagar cp where cp.saldo > 0";

		List<ContasPagar> lista = this.getEntityManager().createQuery(hql).getResultList();

		return lista;
	}

	public void baixar(MovimentacaoBancaria movimentacaoBancaria) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			// BigDecimal saldoAtual =
			// movimentacaoBancaria.getBanco().getSaldoAtual()
			// .subtract(movimentacaoBancaria.getValorSaida());

			BigDecimal saldoAtual = movimentacaoBancaria.getBanco().getSaldoAtual();

			movimentacaoBancaria.setSaldoAtual(saldoAtual);

			// MovimentacaoBancariaDAO movimentacaoBancariaDAO = new
			// MovimentacaoBancariaDAO();
			this.getEntityManager().merge(movimentacaoBancaria);

			BancoDAO bancoDAO = new BancoDAO();
			Banco banco = bancoDAO.lerPorId(movimentacaoBancaria.getBanco().getCodigo());

			BigDecimal saldoAtualBandoAposBaixa = saldoAtual.subtract(movimentacaoBancaria.getValorSaida());
			banco.setSaldoAtual(saldoAtualBandoAposBaixa);
			this.getEntityManager().merge(banco);

			ContasPagar cp = this.lerPorId(movimentacaoBancaria.getContasPagar().getId());
			BigDecimal saldoCP = cp.getSaldo().subtract(movimentacaoBancaria.getValorSaida());

			cp.setSaldo(saldoCP);
			cp.setBaixa(movimentacaoBancaria.getDataMovimentacao());
			this.getEntityManager().merge(cp);

			if (!transacaoAtiva) {
				this.gravarTransacao();

				JSFUtil.retornarMensagemInfo(null, "Baixa realizada com sucesso.", null);

			}

		} catch (OptimisticLockException e) {

			this.desfazerTransacao();

			System.out.println(e);
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

			JSFUtil.retornarMensagemFatal(null, "Erro de concorr�ncia. Esse registro j� foi alterado anteriormente.",
					null);

		} catch (Exception e) {

			this.desfazerTransacao();

			System.out.println(e);
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

			e.printStackTrace();

			JSFUtil.retornarMensagemFatal(null, "Erro ao realizar baixa", null);

		}

	}

	public void Salvar(ContasPagar objeto) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			this.getEntityManager().merge(objeto);

			if (!transacaoAtiva) {
				this.gravarTransacao();

				JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

			}

		} catch (PersistenceException e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemAviso(null, "Verifique o preenchimento da chave primaria.", null);
			this.desfazerTransacao();

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemFatal("Erro Merge", e.getMessage(), null);
			this.desfazerTransacao();

		}

	}

	@SuppressWarnings("unchecked")
	public List<ContasPagar> consultar(FiltroContasPagar filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(ContasPagar.class);

		criteria.setMaxResults(50);

		if (filtro.getNumero() != null) {
			criteria.add(Restrictions.eq("id.numeroCP", filtro.getNumero()));
		}

		if (filtro.getDataParaConsulta() == DataParaConsulta.EMISSAO) {

			if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
				criteria.add(Restrictions.between("emissao", filtro.getDataInicio(), filtro.getDataFim()));
			}

		}

		if (filtro.getDataParaConsulta() == DataParaConsulta.VENCIMENTO) {

			if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
				criteria.add(Restrictions.between("vencimento", filtro.getDataInicio(), filtro.getDataFim()));
			}

		}

		if (filtro.getStatusTitulo() == StatusTitulo.EMABERTO) {
			// hql = "from ContasPagar cr where cr.saldo > 0";
			criteria.add(Restrictions.gt("saldo", new java.math.BigDecimal("0.00")));
		}

		if (filtro.getStatusTitulo() == StatusTitulo.BAIXADO) {
			// hql = "from ContasPagar cr where cr.saldo = 0";
			criteria.add(Restrictions.eq("saldo", new java.math.BigDecimal("0.00")));
		}

		if (filtro.getStatusTitulo() == StatusTitulo.BAIXADOPARCIAL) {
			// hql = "from ContasPagar cr where cr.saldo > 0 and cr.saldo <
			// cr.valor";
			criteria.add(Restrictions.gt("saldo", new java.math.BigDecimal("0.00")));
			criteria.add(Restrictions.ltProperty("saldo", "valor"));
		}

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<ContasPagar> buscaContasPagarPorMes(String mesAno, FiltroContasPagar filtro) {

		List<ContasPagar> lista = null;

		if (filtro.getStatusTitulo() == StatusTitulo.TODOS) {

			String hql = "from ContasPagar cp where to_char(cp.vencimento, 'MM/YYYY') = :mesAno";
			Query query = this.getEntityManager().createQuery(hql);
			query.setParameter("mesAno", mesAno);

			lista = query.getResultList();

		}

		if (filtro.getStatusTitulo() == StatusTitulo.EMABERTO) {

			String hql = "from ContasPagar cp where to_char(cp.vencimento, 'MM/YYYY') = :mesAno and cp.saldo > 0";
			Query query = this.getEntityManager().createQuery(hql);
			query.setParameter("mesAno", mesAno);

			lista = query.getResultList();

		}

		if (filtro.getStatusTitulo() == StatusTitulo.BAIXADO) {

			String hql = "from ContasPagar cp where to_char(cp.vencimento, 'MM/YYYY') = :mesAno and cp.saldo = 0";
			Query query = this.getEntityManager().createQuery(hql);
			query.setParameter("mesAno", mesAno);

			lista = query.getResultList();

		}

		if (filtro.getStatusTitulo() == StatusTitulo.BAIXADOPARCIAL) {

			String hql = "from ContasPagar cp where to_char(cp.vencimento, 'MM/YYYY') = :mesAno and cp.saldo > 0 and cp.saldo < cp.valor";
			Query query = this.getEntityManager().createQuery(hql);
			query.setParameter("mesAno", mesAno);

			lista = query.getResultList();

		}

		return lista;
	}

}
