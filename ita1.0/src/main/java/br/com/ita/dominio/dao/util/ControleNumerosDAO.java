package br.com.ita.dominio.dao.util;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.util.ControleNumeros;

public class ControleNumerosDAO extends JpaDAO<ControleNumeros> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ControleNumerosDAO() {
		super();
	}

	public ControleNumerosDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<ControleNumeros> buscaNumerosPorTabela(String tabela) {

		String hql = "from ControleNumeros where tabela =:tabela order by chave";

		List<ControleNumeros> lista = this.getEntityManager().createQuery(hql).setParameter("tabela", tabela)
				.getResultList();

		return lista;
	}

	public ControleNumeros buscaNumeroPorTabelaEChave(String tabela, String chave) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			String hql = "from ControleNumeros where tabela=:tabela and chave =:chave";

			ControleNumeros numero = (ControleNumeros) this.getEntityManager().createQuery(hql)
					.setParameter("tabela", tabela).setParameter("chave", chave).getSingleResult();

			if (!transacaoAtiva)
				this.gravarTransacao();

			return numero;

		} catch (Exception e) {

			this.desfazerTransacao();

		}

		return null;
	}

	public void atualizaNumeroPorTabelaEChave(String tabela, String chave) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			ControleNumeros numeroASerAtualizado = buscaNumeroPorTabelaEChave(tabela, chave);

			// System.out.println("Antes atualiza��o:");
			// System.out.println("Anterior:" +
			// numeroASerAtualizado.getNumeroAnterior());
			// System.out.println("Atual:" +
			// numeroASerAtualizado.getNumeroAtual());
			// System.out.println("Proximo:" +
			// numeroASerAtualizado.getProximoNumero());

			numeroASerAtualizado.setNumeroAnterior(numeroASerAtualizado.getNumeroAnterior() + 1);
			numeroASerAtualizado.setNumeroAtual(numeroASerAtualizado.getNumeroAtual() + 1);
			numeroASerAtualizado.setProximoNumero(numeroASerAtualizado.getProximoNumero() + 1);

			// System.out.println("Ap�s atualiza��o:");
			// System.out.println("Anterior:" +
			// numeroASerAtualizado.getNumeroAnterior());
			// System.out.println("Atual:" +
			// numeroASerAtualizado.getNumeroAtual());
			// System.out.println("Proximo:" +
			// numeroASerAtualizado.getProximoNumero());

			this.getEntityManager().merge(numeroASerAtualizado);

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (Exception e) {

			this.desfazerTransacao();

		}

	}

	public void criaNumerosPadrao() {

		Query query = this.getEntityManager().createNativeQuery("Select * from controle_numeros");

		if (query.getResultList().isEmpty()) {

			try {

				boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

				if (!transacaoAtiva)
					this.abrirTransacao();

				this.getEntityManager()
						.createNativeQuery(
								"INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (3, '3', 0, 1, 2, 'nfe');"
										+ "INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (2, '2', 0, 1, 2, 'nfe');"
										+ "INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (4, '4', 0, 1, 2, 'nfe');"
										+ "INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (1, '1', 0, 1, 2, 'nfe');"
										+ "INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (5, '1', 0, 1, 2, 'nfce');"
										+ "INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (6, '1', 0, 1, 2, 'orc');"
										+ "INSERT INTO controle_numeros (codigo, chave, numeroanterior, numeroatual, proximonumero, tabela) VALUES (7, '2', 0, 1, 2, 'nfce');")
						
						.executeUpdate();

				query = this.getEntityManager().createNativeQuery("Select * from controle_numeros");

				if (!query.getResultList().isEmpty()) {
					JSFUtil.retornarMensagemInfo(null, "Criando n�meros! Aguarde...", null);
					System.out.println("Criando n�meros! Aguarde...");
				}

				if (!transacaoAtiva)
					this.gravarTransacao();

			} catch (Exception e) {

				this.desfazerTransacao();

				JSFUtil.retornarMensagemErro(null, "Erro ao criar n�meros, entre em contato com suporte.", null);
				System.out.println("Erro ao criar n�meros, entre em contato com suporte.");

				e.printStackTrace();
			}

		}

	}

}
