package br.com.ita.controle.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class JpaDAO<T> implements GenericDAO<T>, Serializable {

	private static final long serialVersionUID = 1L;

	protected Class<T> persistentClass;

	private EntityManager manager;

	/**
	 * Método construtor que
	 */
	@SuppressWarnings("unchecked")
	public JpaDAO() {
		super();

		// pegar a classe persistente por reflexão
		Type tipo = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.persistentClass = (Class<T>) tipo;

		// pega o EntityManager padrão da ThreadAtual
		this.manager = JPAUtil.getEntityManager();
	}

	public JpaDAO(EntityManager manager) {
		this();
		this.manager = manager;
	}

	public EntityManager getEntityManager() {
		// caso o EntityManager tenha sido fechado, deve criar um novo
		if ((this.manager != null) && (!this.manager.isOpen()))
			this.manager = JPAUtil.getEntityManager();

		return this.manager;
	}

	@Override
	public T lerPorId(Object id) {
		return (T) this.getEntityManager().find(this.persistentClass, id);
	}

	@Override
	public List<T> lerTodos() {

		CriteriaBuilder cb = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(this.persistentClass);
		c.select(c.from(this.persistentClass));

		List<T> resultado = this.getEntityManager().createQuery(c).getResultList();

		return resultado;

	}

	public List<T> executeSQL(String sql) {
		javax.persistence.Query query = this.getEntityManager().createQuery(sql);
		@SuppressWarnings("unchecked") // Verificar notação.
		List<T> resultado = query.getResultList();
		return resultado;
	}

	@Override
	public void merge(T objeto) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			this.getEntityManager().merge(objeto);

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemFatal("Erro Merge", e.getMessage(), null);
			this.desfazerTransacao();

		}

	}

	@Override
	public void persist(T objeto) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			this.getEntityManager().persist(objeto);

			if (!transacaoAtiva)
				this.gravarTransacao();

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemFatal("Erro Persist", e.getMessage(), null);
			this.desfazerTransacao();

		}
	}

	@Override
	public void remove(T objeto) {

		try {

			boolean transacaoAtiva = this.getEntityManager().getTransaction().isActive();

			if (!transacaoAtiva)
				this.abrirTransacao();

			this.getEntityManager().remove(objeto);

			if (!transacaoAtiva)
				this.gravarTransacao();
		} catch (PersistenceException e) {

			JSFUtil.retornarMensagemAviso(null, "Não é possível excluir, este registro pode estar associado a outro.",
					null);

			this.desfazerTransacao();

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemFatal("Erro Remove", e.getMessage(), null);
			this.desfazerTransacao();

		}

	}

	@Override
	public void abrirTransacao() {
		this.getEntityManager().getTransaction().begin();
	}

	@Override
	public void gravarTransacao() {
		this.getEntityManager().flush();
		this.getEntityManager().getTransaction().commit();
	}

	@Override
	public void desfazerTransacao() {
		this.getEntityManager().getTransaction().rollback();
	}

}