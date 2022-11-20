package br.com.ita.controle;

import java.nio.file.Files;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
		
		/*
		Reader reader = null;
		try {
			reader = Files.newBufferedReader(Paths.get("C:\\temp\\produtos.csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

		List<String[]> pessoas = null;
		try {
			pessoas = csvReader.readAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		USE [SICNET]
				GO

				-- codigo,produto,fabricante,lkfornec,precocusto,customedio,precovenda,quantidade,unidade,cst,pesobruto,pesoliq,cean,ceantrib,cest
				
				SELECT [codigo]
				      ,[produto]
				      ,[fabricante]
				      ,[lkfornec]
				      ,[precocusto]
				      ,[customedio]
				      ,[precovenda]
				      ,[quantidade]
				      ,[unidade]
				      ,[cst]
				      ,[pesobruto]
				      ,[pesoliq]
				      ,[cean]
				      ,[ceantrib]
				      ,[cest]
				  FROM [dbo].[TABEST1]

				GO
		
		
		
		
		for (String[] pessoa : pessoas) {
			// System.out.println(pessoa[0] + pessoa[1] + pessoa[3] + pessoa[4] + pessoa[5] + pessoa[6] + pessoa[7]
					// + pessoa[8] + pessoa[9] + pessoa[10] + pessoa[11] + pessoa[12] + pessoa[13] + pessoa[14]);
			
			System.out.println(pessoa[1] + " - " + pessoa[2]);
			
			try {
				categoria = new Categoria();
				categoria.setDescricao(pessoa[2]);
				categoriaDao.merge(categoria);
			} catch (Exception e) {
				System.out.println("Erro ao cadastrar categoria:"+pessoa[2]);
				e.printStackTrace();
			}
			
		}
		*/
		
		return "/Categoria/categoriaListar?faces-redirect=true";

	}

	public String novo() {

		// limpar o objeto da p�gina
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
				JSFUtil.retornarMensagemAviso(null, "Outra categoria de produto com o mesmo nome j� existe no sistema.",
						null);
				return null;
			}
		}

		this.categoriaDao.merge(this.getCategoria());
		// limpa a lista
		this.categorias = null;

		// limpar o objeto da p�gina
		this.setCategoria(new Categoria());

		JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

		return "/Categoria/categoriaListar";
	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setCategoria(new Categoria());

		return "/Categoria/categoriaListar";
	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Categoria objetoDoBanco = this.categoriaDao.lerPorId(codigo);
		this.categoriaDao.remove(objetoDoBanco);

		if (this.categoriaDao.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Exclu�do com sucesso.", null);
		}

		// limpar o objeto da p�gina
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
