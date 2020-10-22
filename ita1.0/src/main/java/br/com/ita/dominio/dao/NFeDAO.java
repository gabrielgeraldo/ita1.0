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
import br.com.ita.dominio.ItemNTFe;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.dao.filtros.FiltroNfe;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.NTFe;
//import br.com.ita.dominio.PedidoVenda;

public class NFeDAO extends JpaDAO<NTFe> implements Serializable {

	private static final long serialVersionUID = 1L;

	public NFeDAO() {
		super();
	}

	public NFeDAO(EntityManager manager) {
		super(manager);
	}

	public void salvarNFe(NTFe nfe, List<ItemNTFe> itensNFeSaida) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			NTFe nfeSaidaSalva = this.getEntityManager().merge(nfe);
			// System.out.println("Codigo da NF-e que foi gravada: " +
			// nfeSaidaSalva.getCodigo());

			// Deletando os itens antes de utilizá-los.
			// Só exclui os itens se não for uma nova venda.
			if (nfe.getCodigo() != null) {

				// System.out.println("Deletando os itens antes de
				// utilizá-los.");

				// Deletandos os itens antes do merge.
				ItemNFeDAO dao = new ItemNFeDAO();
				dao.deletaItens(nfe);

			}

			// ATUALIZANDO A TABELA DE PEDIDOs COM A NOTA.
			//if (nfe.getPedidoVenda() != null) {

				// System.out.println("Atualizando o PV com a NF-e.");

				//PedidoVenda pv = nfe.getPedidoVenda();
				//pv.setNfe(nfe);

				//this.getEntityManager().merge(pv);
			//}

			for (ItemNTFe item : itensNFeSaida) {
				item.setNfe(nfeSaidaSalva);

				// System.out.println("Gravando item.");
				this.getEntityManager().merge(item);

				Produto produto = item.getProduto();
				produto.setQtdEstq(produto.getQtdEstq() - item.getQuantidade());

				// System.out.println("Atualizando produto.");
				this.getEntityManager().merge(produto);

			}

			// Se for uma nova NF-e, atualiza o número.
			if (nfe.getCodigo() == null) {

				// System.out.println("Atualizando númeração da NF-e");
				ControleNumerosDAO daoNumeracao = new ControleNumerosDAO();

				daoNumeracao.atualizaNumeroPorTabelaEChave("nfe", nfe.getSerie().toString());

			}

			JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (Exception e) {

			this.desfazerTransacao();

			System.out.println(e);
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

			JSFUtil.retornarMensagemFatal(null, "Erro ao salvar/alterar.", null);

		}

	}

	public void cancelarNFeSaida(NTFe nfeSaida) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			NTFe nfeSaidaCancelada = this.getEntityManager().merge(nfeSaida);
			System.out.println("Codigo da nfeSaida cancelada: " + nfeSaidaCancelada.getCodigo());

			Query query = this.getEntityManager().createQuery("FROM ItemNFeSaida WHERE nfeSaida = :nfeSaida");
			query.setParameter("nfeSaida", nfeSaida);

			@SuppressWarnings("unchecked")
			List<ItemNTFe> results = (List<ItemNTFe>) query.getResultList();

			for (ItemNTFe item : results) {

				Produto produto = item.getProduto();
				produto.setQtdEstq(produto.getQtdEstq() + item.getQuantidade());

				this.getEntityManager().merge(produto);

			}

			if (!transacaoAtiva)
				this.gravarTransacao();

			System.out.println("NFeSaida cancelada com sucesso");

		} catch (Exception e) {

			this.desfazerTransacao();

			System.out.println("Falha ao cancelar nfeSaida: " + e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<NTFe> buscaNotasNaoTramitidas() {

		// String hql = "from NTFe item where status <> '100' or status is null
		// ";
		String hql = "from NTFe item where numprotocolo is null order by numero desc";

		List<NTFe> lista = this.getEntityManager().createQuery(hql).getResultList();

		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<NTFe> buscaNotasTramitidas() {

		// String hql = "from NTFe item where status = '100'";
		String hql = "from NTFe item where numprotocolo is not null order by numero desc";

		List<NTFe> lista = this.getEntityManager().createQuery(hql).getResultList();

		return lista;

	}

	@SuppressWarnings("unchecked")
	public List<NTFe> consultar(FiltroNfe filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(NTFe.class);

		criteria.setMaxResults(50);

		// Só trás as nfs sem protocolo.
		criteria.add(Restrictions.isNotNull("numProtocolo"));

		if (filtro.getNumero() != null) {
			criteria.add(Restrictions.eq("numero", filtro.getNumero()));
		}

		if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
		}

		return criteria.list();
	}

}