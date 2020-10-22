package br.com.ita.dominio.dao.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.util.Cfop;

public class CfopDAO extends JpaDAO<Cfop> implements Serializable {

	private static final long serialVersionUID = 1L;

	public CfopDAO() {
		super();
	}

	public CfopDAO(EntityManager manager) {
		super(manager);
	}

	public void criaCfop() {

		Query query = this.getEntityManager().createNativeQuery("Select * from cfop");

		// SE TABELA CFOP VAZIA
		if (query.getResultList().isEmpty()) {

			// CARGA DO CFOP.SQL
			String sql = null;

			try {
				sql = IOUtils.toString(new FileInputStream("c:/Ita/util/cfop.sql"));
				// System.out.println(sql);

			} catch (FileNotFoundException e) {

				JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
				System.out.println(e.getMessage());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				JSFUtil.retornarMensagemErro(null, "Erro ao carregar CFOP, entre em contato com suporte.", null);
				System.out.println("Erro ao carregar CFOP, entre em contato com suporte.");

			}

			// SE CARREGROU SQL
			if (sql != null) {

				try {

					boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

					if (!transacaoAtiva)
						this.abrirTransacao();

					this.getEntityManager().createNativeQuery(sql).executeUpdate();

					query = this.getEntityManager().createNativeQuery("Select * from cfop");

					if (!query.getResultList().isEmpty()) {
						JSFUtil.retornarMensagemInfo(null, "Carregando CFOP! Aguarde...", null);
						System.out.println("Carregando CFOP! Aguarde...");
					}

					if (!transacaoAtiva)
						this.gravarTransacao();

				} catch (Exception e) {

					this.desfazerTransacao();

					JSFUtil.retornarMensagemErro(null, "Erro carregar CFOP, entre em contato com suporte.", null);
					System.out.println("Erro carregar CFOP, entre em contato com suporte.");

					e.printStackTrace();
				}
			}

		}

	}
}
