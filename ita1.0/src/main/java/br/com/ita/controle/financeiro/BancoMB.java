package br.com.ita.controle.financeiro;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.financeiro.Banco;
import br.com.ita.dominio.dao.financeiro.BancoDAO;

@Named("bancoMB")
@RequestScoped
public class BancoMB {

	@Inject
	private Banco banco;

	@Inject
	private BancoDAO bancoDao;

	private List<Banco> bancos = null;
	private List<Banco> bancosFiltrados = null;

	public String listar() {

		return "/Banco/bancoListar?faces-redirect=true";

	}

	public String novo() {

		// limpar o objeto da página
		this.setBanco(new Banco());

		return "/Banco/bancoEditar";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Banco objetoDoBanco = this.bancoDao.lerPorId(codigo);
		this.setBanco(objetoDoBanco);

		return "/Banco/bancoEditar";

	}

	public String salvar() {

		if (this.getBanco().getCodigo() == null) {
			Banco objetoDoBanco = this.bancoDao.verificaSeBancoExiste(this.getBanco().getNumeroAgencia(),
					this.getBanco().getNumeroConta(), this.getBanco().getNomeAgencia());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null, "Outra banco com o mesmo Nome/Ag/Ct já existe no sistema.", null);
				return null;
			}
		}

		this.bancoDao.merge(this.getBanco());
		// limpa a lista
		this.bancos = null;

		// limpar o objeto da página
		this.setBanco(new Banco());

		JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.", null);

		return "/Banco/bancoListar";
	}

	public String cancelar() {

		// limpar o objeto da página
		this.setBanco(new Banco());

		return "/Banco/bancoListar";
	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Banco objetoDoBanco = this.bancoDao.lerPorId(codigo);
		this.bancoDao.remove(objetoDoBanco);

		if (this.bancoDao.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluído com sucesso.", null);
		}

		// limpar o objeto da página
		this.setBanco(new Banco());
		// limpa a lista
		this.bancos = null;

		return "/Banco/bancoListar";

	}

	public Banco getBanco() {
		return banco;
	}

	public BancoDAO getBancoDao() {
		return bancoDao;
	}

	public List<Banco> getBancos() {

		if (this.bancos == null)
			this.bancos = this.bancoDao.lerTodos();

		return bancos;
	}

	public List<Banco> getBancosFiltrados() {
		return bancosFiltrados;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void setBancoDao(BancoDAO bancoDao) {
		this.bancoDao = bancoDao;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public void setBancosFiltrados(List<Banco> bancosFiltrados) {
		this.bancosFiltrados = bancosFiltrados;
	}

}
