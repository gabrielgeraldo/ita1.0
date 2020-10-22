package br.com.ita.controle;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.CondicaoPagamento;
import br.com.ita.dominio.FormaPagamento;
import br.com.ita.dominio.MeioPagamento;
import br.com.ita.dominio.dao.CondicaoPagamentoDAO;

@Named("condicaoPagamentoMB")
@RequestScoped
public class CondicaoPagamentoMB {

	@Inject
	private CondicaoPagamento condicaoPagamento;

	@Inject
	private CondicaoPagamentoDAO condicaoPagamentoDao;

	private List<CondicaoPagamento> condicoesPagamentos = null;
	private List<CondicaoPagamento> condicoesPagamentosFiltrados = null;

	private List<FormaPagamento> formaPagamento = null;

	private List<MeioPagamento> meioPagamento = null;

	public String listar() {

		return "/CondicaoPagamento/condicaoPagamentoListar?faces-redirect=true";

	}

	public String novo() {

		// limpar o objeto da página
		this.setCondicaoPagamento(new CondicaoPagamento());

		return "/CondicaoPagamento/condicaoPagamentoEditar";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		CondicaoPagamento objetoDoBanco = this.condicaoPagamentoDao.lerPorId(codigo);
		this.setCondicaoPagamento(objetoDoBanco);

		return "/CondicaoPagamento/condicaoPagamentoEditar";

	}

	public String salvar() {

		if (this.getCondicaoPagamento().getCodigo() == null) {
			CondicaoPagamento objetoDoBanco = this.condicaoPagamentoDao
					.lerPorDescricao(this.getCondicaoPagamento().getDescricao());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null,
						"Outra condição de pagamento com o mesmo nome já existe no sistema.", null);
				return null;
			}
		}

		this.condicaoPagamentoDao.merge(this.getCondicaoPagamento());
		// limpa a lista
		this.condicoesPagamentos = null;

		// limpar o objeto da página
		this.setCondicaoPagamento(new CondicaoPagamento());

		JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

		return "/CondicaoPagamento/condicaoPagamentoListar";

	}

	public String cancelar() {

		// limpar o objeto da página
		this.setCondicaoPagamento(new CondicaoPagamento());

		return "/CondicaoPagamento/condicaoPagamentoListar";

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		CondicaoPagamento objetoDoBanco = this.condicaoPagamentoDao.lerPorId(codigo);
		this.condicaoPagamentoDao.remove(objetoDoBanco);

		if (this.condicaoPagamentoDao.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluído com sucesso.", null);
		}

		// limpar o objeto da página
		this.setCondicaoPagamento(new CondicaoPagamento());
		// limpa a lista
		this.condicoesPagamentos = null;

		return "/CondicaoPagamento/condicaoPagamentoListar";

	}

	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public CondicaoPagamentoDAO getCondicaoPagamentoDao() {
		return condicaoPagamentoDao;
	}

	public void setCondicaoPagamentoDao(CondicaoPagamentoDAO condicaoPagamentoDao) {
		this.condicaoPagamentoDao = condicaoPagamentoDao;
	}

	public List<CondicaoPagamento> getCondicoesPagamentos() {
		if (this.condicoesPagamentos == null)
			this.condicoesPagamentos = this.condicaoPagamentoDao.lerTodos();
		return condicoesPagamentos;
	}

	public void setCondicoesPagamentos(List<CondicaoPagamento> condicoesPagamentos) {
		this.condicoesPagamentos = condicoesPagamentos;
	}

	public List<CondicaoPagamento> getCondicoesPagamentosFiltrados() {
		return condicoesPagamentosFiltrados;
	}

	public void setCondicoesPagamentosFiltrados(List<CondicaoPagamento> condicoesPagamentosFiltrados) {
		this.condicoesPagamentosFiltrados = condicoesPagamentosFiltrados;
	}

	public List<FormaPagamento> getFormaPagamento() {
		if (this.formaPagamento == null)
			this.formaPagamento = Arrays.asList(FormaPagamento.values());
		return formaPagamento;
	}

	public void setFormaPagamento(List<FormaPagamento> formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public List<MeioPagamento> getMeioPagamento() {
		if (this.meioPagamento == null)
			this.meioPagamento = Arrays.asList(MeioPagamento.values());
		return meioPagamento;
	}

	public void setMeioPagamento(List<MeioPagamento> meioPagamento) {
		this.meioPagamento = meioPagamento;
	}

}
