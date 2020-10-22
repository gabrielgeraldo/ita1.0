package br.com.ita.dominio.filtros;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.ita.dominio.Categoria;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.dao.CategoriaDAO;
import br.com.ita.dominio.dao.FornecedorDAO;

@ManagedBean(name = "dtFilterViewProdutos")
@ViewScoped
public class FilterViewProdutos implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Categoria> categorias = null;

	private CategoriaDAO categoriaDao = new CategoriaDAO();

	private List<Fornecedor> fornecedores = null;

	private FornecedorDAO fornecedorDao = new FornecedorDAO();

	public CategoriaDAO getCategoriaDao() {
		return categoriaDao;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public void setCategoriaDao(CategoriaDAO categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

	public List<Categoria> getCategorias() {
		if (this.categorias == null)
			this.categorias = this.categoriaDao.lerTodos();

		return this.categorias;
	}

	public List<Fornecedor> getFornecedores() {

		if (this.fornecedores == null)
			this.fornecedores = this.fornecedorDao.lerTodos();

		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public FornecedorDAO getFornecedorDao() {
		return fornecedorDao;
	}

	public void setFornecedorDao(FornecedorDAO fornecedorDao) {
		this.fornecedorDao = fornecedorDao;
	}

}