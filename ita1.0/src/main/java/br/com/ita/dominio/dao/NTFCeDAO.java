package br.com.ita.dominio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.ItemNTFCe;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.dao.filtros.FiltroNFCe;
import br.com.ita.dominio.dao.util.ControleNumerosDAO;
import br.com.ita.dominio.NTFCe;

public class NTFCeDAO extends JpaDAO<NTFCe> implements Serializable {

	private static final long serialVersionUID = 1L;

	public NTFCeDAO() {
		super();
	}

	public NTFCeDAO(EntityManager manager) {
		super(manager);
	}

	public void salvarNTFCe(NTFCe venda, List<ItemNTFCe> itensNTFCe) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			NTFCe vendaSalva = this.getEntityManager().merge(venda);
			// System.out.println("Codigo da venda salva: " +
			// vendaSalva.getCodigo());

			for (ItemNTFCe item : itensNTFCe) {
				item.setNfce(vendaSalva);

				this.getEntityManager().merge(item);

				Produto produto = item.getProduto();
				produto.setQtdEstq(produto.getQtdEstq() - item.getQuantidade());

				this.getEntityManager().merge(produto);

			}

			// Se for uma nova NF-e: atualiza o n�mero.
			if (venda.getCodigo() == null) {

				// System.out.println("----- Atualizando n�mera��o da NFC-e
				// -----");
				ControleNumerosDAO daoNumeracao = new ControleNumerosDAO();

				daoNumeracao.atualizaNumeroPorTabelaEChave("nfce", venda.getSerie().toString());

			}

			if (!transacaoAtiva)
				this.gravarTransacao();

			// System.out.println("NTFCe finalizada com sucesso!");

		} catch (Exception e) {

			this.desfazerTransacao();

			System.out.println("Erro ao salvar nfce: " + e);

			JSFUtil.retornarMensagemErro(null, "Erro ao salvar NFC-e: " + e.getMessage(), null);
		}

	}

	@SuppressWarnings("unchecked")
	public List<NTFCe> consultar(FiltroNFCe filtro) {

		Session session = this.getEntityManager().unwrap(Session.class);
		Criteria criteria = session.createCriteria(NTFCe.class);

		criteria.setMaxResults(50);

		if (filtro.getNumero() != null) {
			criteria.add(Restrictions.eq("numero", filtro.getNumero()));
		}

		if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
			criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
		}

		return criteria.list();
	}

}
