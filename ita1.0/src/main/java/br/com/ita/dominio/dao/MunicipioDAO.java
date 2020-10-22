package br.com.ita.dominio.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.Municipio;

public class MunicipioDAO extends JpaDAO<Municipio> implements Serializable {

	private static final long serialVersionUID = 1L;

	public MunicipioDAO() {
		super();
	}

	public MunicipioDAO(EntityManager manager) {
		super(manager);
	}

	@SuppressWarnings("unchecked")
	public List<Municipio> lerMunicipiosPorEstado(String uf) {

		try {
			this.getEntityManager().getTransaction().begin();

			List<Municipio> municipios = (List<Municipio>) this.getEntityManager()
					.createQuery("FROM Municipio WHERE uf = :uf").setParameter("uf", uf).getResultList();

			this.getEntityManager().getTransaction().commit();

			return municipios;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public Municipio lerMunicipiosPorCodigo(String codigo) {

		try {
			this.getEntityManager().getTransaction().begin();

			Municipio municipio = (Municipio) this.getEntityManager()
					.createQuery("FROM Municipio WHERE codigo = :codigo").setParameter("codigo", Long.parseLong(codigo))
					.getSingleResult();

			this.getEntityManager().getTransaction().commit();

			return municipio;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public void criaMunicipio() {

		Query query = this.getEntityManager().createNativeQuery("Select * from municipio");

		// SE TABELA MUNICIPIO VAZIA
		if (query.getResultList().isEmpty()) {

			// CARGA DO MUNICIPIO.SQL
			String sql = null;

			try {
				sql = IOUtils.toString(new FileInputStream("c:/Ita/util/municipio.sql"));
				// System.out.println(sql);

			} catch (FileNotFoundException e) {

				JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
				System.out.println(e.getMessage());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				JSFUtil.retornarMensagemErro(null, "Erro ao carregar MUNICIPIO, entre em contato com suporte.", null);
				System.out.println("Erro ao carregar MUNICIPIO, entre em contato com suporte.");

			}

			// SE CARREGROU SQL
			if (sql != null) {

				try {

					boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

					if (!transacaoAtiva)
						this.abrirTransacao();

					this.getEntityManager().createNativeQuery(sql).executeUpdate();

					query = this.getEntityManager().createNativeQuery("Select * from municipio");

					if (!query.getResultList().isEmpty()) {
						JSFUtil.retornarMensagemInfo(null, "Carregando MUNICIPIO! Aguarde...", null);
						System.out.println("Carregando MUNICIPIO! Aguarde...");
					}

					if (!transacaoAtiva)
						this.gravarTransacao();

				} catch (Exception e) {

					this.desfazerTransacao();

					JSFUtil.retornarMensagemErro(null, "Erro carregar MUNICIPIO, entre em contato com suporte.", null);
					System.out.println("Erro carregar MUNICIPIO, entre em contato com suporte.");

					e.printStackTrace();
				}
			}

		}

	}
}
