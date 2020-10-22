package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Usuario;

public class UsuarioDAO extends JpaDAO<Usuario> implements Serializable {

	private static final long serialVersionUID = 1L;

	public UsuarioDAO() {
		super();
	}

	public UsuarioDAO(EntityManager manager) {
		super(manager);
	}

	public Usuario lerPorUsuario(String usuario) {
		Usuario resultado;

		Query consulta = this.getEntityManager().createQuery("from Usuario u where u.usuario = :usuario");
		consulta.setParameter("usuario", usuario);

		try {
			resultado = (Usuario) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

	public void criaUsuarioPadrao() {

		Query query = this.getEntityManager().createNativeQuery("Select * from usuario");

		if (query.getResultList().isEmpty()) {

			try {

				boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

				if (!transacaoAtiva)
					this.abrirTransacao();

				this.getEntityManager()
						.createNativeQuery("INSERT INTO usuario (codigo, senha, usuario) VALUES (1, 'admin', 'admin');"
								+ "INSERT INTO usuario (codigo, senha, usuario) VALUES (2, 'gerente', 'gerente');"
								+ "ALTER SEQUENCE codigo_usuario RESTART WITH 3;")
						.executeUpdate();

				query = this.getEntityManager().createNativeQuery("Select * from usuario");

				if (!query.getResultList().isEmpty()) {
					JSFUtil.retornarMensagemInfo(null, "Criando dados de usuário padrão! Aguarde...", null);
					System.out.println("Criando dados de usuário padrão! Aguarde...");
				}

				if (!transacaoAtiva)
					this.gravarTransacao();

			} catch (Exception e) {

				this.desfazerTransacao();

				JSFUtil.retornarMensagemErro(null, "Erro ao criar usuário, entre em contato com suporte.", null);
				System.out.println("Erro ao criar usuário, entre em contato com suporte.");

				e.printStackTrace();
			}

		}

	}

}
