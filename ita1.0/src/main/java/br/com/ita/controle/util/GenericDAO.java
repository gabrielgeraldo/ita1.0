package br.com.ita.controle.util;

import java.util.List;

import javax.persistence.EntityManager;

public interface GenericDAO<T> {

	public EntityManager getEntityManager();

	public T lerPorId(Object id);

	public List<T> lerTodos();

	public void merge(T objeto);

	public void persist(T objeto);

	public void remove(T objeto);

	public void abrirTransacao();

	public void gravarTransacao();

	public void desfazerTransacao();

}