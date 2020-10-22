package br.com.ita.controle.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

public class JPAUtil {

	/**
	 * Objeto do tipo Singleton para criar a EntityManagerFactory como instância
	 * única durante a inicialiação.
	 */
	private static EntityManagerFactory fabrica;

	static {
		JPAUtil.fabrica = Persistence.createEntityManagerFactory("banco");
	}

	public static EntityManagerFactory getFactory() {
		return JPAUtil.fabrica;
	}

	/**
	 * Objeto que armazena um único valor para cada Thread individualmente. O
	 * objetivo é criar apenas 1 objeto EntityManager para cada Thread, de forma
	 * que os DAO's possam compartilhar o mesmo objeto.
	 */
	private static ThreadLocal<EntityManager> CACHE = new ThreadLocal<EntityManager>();

	public static void limparCacheEntityManager() {
		// tenta ler o EntityManager da Thread atual
		EntityManager em = CACHE.get();

		// fecha o EntityManager caso exista
		if (em != null)
			em.close();

		CACHE.remove();
	}

	public static EntityManager getEntityManager() {
		// tenta ler o EntityManager da Thread atual
		EntityManager retorno = CACHE.get();

		// se tem um objeto e este objeto estiver fechado, deve descartá-lo e
		// criar outro
		if ((retorno != null) && (!retorno.isOpen()))
			retorno = null;

		// caso ainda não tenha sido criado, cria um novo e guarda reutilização
		if (retorno == null) {
			retorno = JPAUtil.fabrica.createEntityManager();
			// guarda o objeto para usar sempre o mesmo nesta Thread
			CACHE.set(retorno);
		}

		return retorno;
	}

	/**
	 * Método para transformar uma sessao Hibernate em uma Conection JDBC.
	 * Autor:Prof. Sérgio Roberto Delfino Método aprendido em uma aula no
	 * Youtube. https://www.youtube.com/watch?v=bi7-RoJLZk8 Alteração:Gabriel
	 * Geraldo 23/01/2017
	 */
	public static Connection getConexaoJDBC() {
		Session sessao = (Session) getEntityManager().getDelegate();

		Connection conexao = sessao.doReturningWork(new ReturningWork<Connection>() {
			@Override
			public Connection execute(Connection conn) throws SQLException {
				return conn;
			}
		});

		return conexao;
	}
}
