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
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.DataParaConsulta;
import br.com.ita.dominio.StatusTitulo;
import br.com.ita.dominio.dao.filtros.FiltroContasReceber;
import br.com.ita.dominio.financeiro.Banco;
import br.com.ita.dominio.financeiro.ContasReceber;
import br.com.ita.dominio.financeiro.MovimentacaoBancaria;

public class ContasReceberDAO extends JpaDAO<ContasReceber> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ContasReceberDAO() {
		super();
	}

	public ContasReceberDAO(EntityManager manager) {
		super(manager);
	}

	public ContasReceber lerPorNumeroCliente(Integer numero, Cliente cliente) {
		ContasReceber resultado;

		Query consulta = this.getEntityManager()
				.createQuery("from ContasReceber u where u.numero = :numero and u.cliente = :cliente");
		consulta.setParameter("numero", numero);
		consulta.setParameter("cliente", cliente);

		try {
			resultado = (ContasReceber) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<ContasReceber> buscaContasReceberNaoBaixadas() {

		String hql = "from ContasReceber cr where cr.saldo > 0";

		List<ContasReceber> lista = this.getEntityManager().createQuery(hql).getResultList();

		return lista;
	}

	public void baixar(MovimentacaoBancaria movimentacaoBancaria) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			// BigDecimal saldoAtual =
			// movimentacaoBancaria.getBanco().getSaldoAtual()
			// .add(movimentacaoBancaria.getValorEntrada());

			BigDecimal saldoAtual = movimentacaoBancaria.getBanco().getSaldoAtual();

			movimentacaoBancaria.setSaldoAtual(saldoAtual);

			// MovimentacaoBancariaDAO movimentacaoBancariaDAO = new
			// MovimentacaoBancariaDAO();
			this.getEntityManager().merge(movimentacaoBancaria);

			BancoDAO bancoDAO = new BancoDAO();
			Banco banco = bancoDAO.lerPorId(movimentacaoBancaria.getBanco().getCodigo());

			BigDecimal saldoAtualBandoAposBaixa = saldoAtual.add(movimentacaoBancaria.getValorEntrada());
			banco.setSaldoAtual(saldoAtualBandoAposBaixa);
			this.getEntityManager().merge(banco);

			ContasReceber cr = this.lerPorId(movimentacaoBancaria.getContasReceber().getId());
			BigDecimal saldoCR = cr.getSaldo().subtract(movimentacaoBancaria.getValorEntrada());

			cr.setSaldo(saldoCR);
			cr.setBaixa(movimentacaoBancaria.getDataMovimentacao());
			this.getEntityManager().merge(cr);

			JSFUtil.retornarMensagemInfo(null, "Baixa realizada com sucesso.", null);

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (OptimisticLockException e) {

			this.desfazerTransacao();

			System.out.println(e);
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

			JSFUtil.retornarMensagemFatal(null, "Erro de concorrência. Esse registro já foi alterado anteriormente.",
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

	public void Salvar(ContasReceber objeto) {

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
	public List<ContasReceber> consultar(FiltroContasReceber filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(ContasReceber.class);

		criteria.setMaxResults(50);

		if (filtro.getNumero() != null) {
			criteria.add(Restrictions.eq("numero", filtro.getNumero()));
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
			// hql = "from ContasReceber cr where cr.saldo > 0";
			criteria.add(Restrictions.gt("saldo", new java.math.BigDecimal("0.00")));
		}

		if (filtro.getStatusTitulo() == StatusTitulo.BAIXADO) {
			// hql = "from ContasReceber cr where cr.saldo = 0";
			criteria.add(Restrictions.eq("saldo", new java.math.BigDecimal("0.00")));
		}

		if (filtro.getStatusTitulo() == StatusTitulo.BAIXADOPARCIAL) {
			// hql = "from ContasReceber cr where cr.saldo > 0 and cr.saldo <
			// cr.valor";
			criteria.add(Restrictions.gt("saldo", new java.math.BigDecimal("0.00")));
			criteria.add(Restrictions.ltProperty("saldo", "valor"));
		}

		return criteria.list();

	}

}
