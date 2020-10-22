package br.com.ita.dominio.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Estado;

public class EstadoDAO extends JpaDAO<Estado> implements Serializable {

	private static final long serialVersionUID = 1L;

	public EstadoDAO() {
		super();
	}

	public EstadoDAO(EntityManager manager) {
		super(manager);
	}

	public void criaEstado() {

		Query query = this.getEntityManager().createNativeQuery("Select * from estado");

		// SE TABELA ESTADO VAZIA
		if (query.getResultList().isEmpty()) {

			// CARGA DO ESTADO.SQL
			String sql = null;

			try {
				sql = IOUtils.toString(new FileInputStream("c:/Ita/util/estado.sql"));
				// System.out.println(sql);

			} catch (FileNotFoundException e) {

				JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
				System.out.println(e.getMessage());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				JSFUtil.retornarMensagemErro(null, "Erro ao carregar ESTADO, entre em contato com suporte.", null);
				System.out.println("Erro ao carregar ESTADO, entre em contato com suporte.");

			}

			// SE CARREGROU SQL
			if (sql != null) {

				try {

					boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

					if (!transacaoAtiva)
						this.abrirTransacao();

					this.getEntityManager().createNativeQuery(sql).executeUpdate();

					query = this.getEntityManager().createNativeQuery("Select * from estado");

					if (!query.getResultList().isEmpty()) {
						JSFUtil.retornarMensagemInfo(null, "Carregando ESTADO! Aguarde...", null);
						System.out.println("Carregando ESTADO! Aguarde...");
					}

					if (!transacaoAtiva)
						this.gravarTransacao();

				} catch (Exception e) {

					this.desfazerTransacao();

					JSFUtil.retornarMensagemErro(null, "Erro carregar ESTADO, entre em contato com suporte.", null);
					System.out.println("Erro carregar ESTADO, entre em contato com suporte.");

					e.printStackTrace();
				}
			}

		}

	}

	public Estado lerEstadoPorUF(String uf) {

		Estado resultado;

		Query consulta = this.getEntityManager().createQuery("from Estado u where u.uf = :uf");
		consulta.setParameter("uf", uf);

		try {
			resultado = (Estado) consulta.getSingleResult();
		} catch (NoResultException e) {
			resultado = null;
		}

		return resultado;
	}

}
