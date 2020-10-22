package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Papel;

public class PapelDAO extends JpaDAO<Papel> implements Serializable {

	private static final long serialVersionUID = 1L;

	public PapelDAO() {
		super();
	}

	public PapelDAO(EntityManager manager) {
		super(manager);
	}

	public void criaPapeisUsuarioPadrao() {

		Query papel = this.getEntityManager().createNativeQuery("Select * from papel");
		Query papel_usuario = this.getEntityManager().createNativeQuery("Select * from papel_usuario");

		if (papel.getResultList().isEmpty() && papel_usuario.getResultList().isEmpty()) {

			try {

				boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

				if (!transacaoAtiva)
					this.abrirTransacao();

				this.getEntityManager()
						.createNativeQuery(
								"INSERT INTO papel (codigo, descricao, nome) VALUES (1, 'GERENTE', 'GERENTE');"
										+ "INSERT INTO papel (codigo, descricao, nome) VALUES (2, 'VENDEDOR', 'VENDEDOR');"
										+ "INSERT INTO papel (codigo, descricao, nome) VALUES (3, 'CAIXA', 'CAIXA');"
										+ "INSERT INTO papel (codigo, descricao, nome) VALUES (4, 'ADMINISTRADOR', 'ADMINISTRADOR');"
										+ "INSERT INTO papel_usuario (usuario_codigo, papeis_codigo) VALUES (1, 4);"
										+ "INSERT INTO papel_usuario (usuario_codigo, papeis_codigo) VALUES (2, 1);"
										+ "ALTER SEQUENCE codigo_papel RESTART WITH 5;")
						.executeUpdate();

				papel = this.getEntityManager().createNativeQuery("Select * from papel");
				papel_usuario = this.getEntityManager().createNativeQuery("Select * from papel_usuario");

				if (!papel.getResultList().isEmpty() && !papel_usuario.getResultList().isEmpty()) {
					JSFUtil.retornarMensagemInfo(null, "Criando dados de papeis de usuário padrão! Aguarde...", null);
					System.out.println("Criando dados de papeis de usuário padrão! Aguarde...");
				}

				if (!transacaoAtiva)
					this.gravarTransacao();

			} catch (Exception e) {

				this.desfazerTransacao();

				JSFUtil.retornarMensagemErro(null, "Erro ao criar papeis de usuário, entre em contato com suporte.",
						null);
				System.out.println("Erro ao criar papeis de usuário, entre em contato com suporte.");

				e.printStackTrace();
			}

		}

	}
}
