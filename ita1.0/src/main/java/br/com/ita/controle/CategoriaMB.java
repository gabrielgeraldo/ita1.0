package br.com.ita.controle;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.Categoria;
import br.com.ita.dominio.dao.CategoriaDAO;

@Named("categoriaMB")
@RequestScoped
public class CategoriaMB {

	@Inject
	private Categoria categoria;

	@Inject
	private CategoriaDAO categoriaDao;

	private List<Categoria> categorias = null;
	private List<Categoria> categoriasFiltrados = null;

	public String listar() {

		return "/Categoria/categoriaListar?faces-redirect=true";

	}

	public String novo() {

		// limpar o objeto da página
		this.setCategoria(new Categoria());

		return "/Categoria/categoriaEditar";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Categoria objetoDoBanco = this.categoriaDao.lerPorId(codigo);
		this.setCategoria(objetoDoBanco);

		return "/Categoria/categoriaEditar";

	}

	public String salvar() {

		if (this.getCategoria().getCodigo() == null) {
			Categoria objetoDoBanco = this.categoriaDao.lerPorDescricao(this.getCategoria().getDescricao());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null, "Outra categoria de produto com o mesmo nome já existe no sistema.",
						null);
				return null;
			}
		}

		this.categoriaDao.merge(this.getCategoria());
		// limpa a lista
		this.categorias = null;

		// limpar o objeto da página
		this.setCategoria(new Categoria());

		JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

		return "/Categoria/categoriaListar";
	}

	public String cancelar() {

		// limpar o objeto da página
		this.setCategoria(new Categoria());

		return "/Categoria/categoriaListar";
	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Categoria objetoDoBanco = this.categoriaDao.lerPorId(codigo);
		this.categoriaDao.remove(objetoDoBanco);

		if (this.categoriaDao.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluído com sucesso.", null);
		}

		// limpar o objeto da página
		this.setCategoria(new Categoria());
		// limpa a lista
		this.categorias = null;

		return "/Categoria/categoriaListar";

	}

	public List<Categoria> getCategorias() {
		if (this.categorias == null)
			this.categorias = this.categoriaDao.lerTodos();

		return this.categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria Categoria) {
		this.categoria = Categoria;
	}

	public CategoriaDAO getCategoriaDao() {
		return categoriaDao;
	}

	public void setCategoriaDao(CategoriaDAO CategoriaDao) {
		this.categoriaDao = CategoriaDao;
	}

	public List<Categoria> getCategoriasFiltrados() {
		return categoriasFiltrados;
	}

	public void setCategoriasFiltrados(List<Categoria> categoriasFiltrados) {
		this.categoriasFiltrados = categoriasFiltrados;
	}

}
