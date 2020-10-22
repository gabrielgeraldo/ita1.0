package br.com.ita.controle.financeiro;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.dao.financeiro.BancoDAO;
import br.com.ita.dominio.dao.financeiro.ContasPagarDAO;
import br.com.ita.dominio.dao.financeiro.MovimentacaoBancariaDAO;
import br.com.ita.dominio.financeiro.Banco;
import br.com.ita.dominio.financeiro.ContasPagar;
import br.com.ita.dominio.financeiro.MovimentacaoBancaria;

@Named("baixaCPMB")
@ViewScoped
public class BaixaCPMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BancoDAO bancoDAO;

	@Inject
	private ContasPagarDAO contasPagarDAO;

	private List<Banco> bancos = null;

	private List<ContasPagar> contasPagarLista = null;

	@Inject
	private MovimentacaoBancaria movimentacaoBancaria;

	@Inject
	private MovimentacaoBancariaDAO movimentacaoBancariaDAO;

	public String listar() {

		return "/Financeiro/_financeiroBaixarCP?faces-redirect=true";

	}

	public String baixar() {

		if (this.movimentacaoBancaria.getContasPagar() == null) {
			JSFUtil.retornarMensagemErro(null, "O CP é de preenchimento obrigatório.", null);
			return null;
		}

		if (this.movimentacaoBancaria.getValorSaida() == null) {
			JSFUtil.retornarMensagemErro(null, "Valor a pagar é de preenchimento obrigatório.", null);
			return null;
		}

		if (this.movimentacaoBancaria.getValorSaida()
				.compareTo(this.movimentacaoBancaria.getContasPagar().getSaldo()) > 0) {
			JSFUtil.retornarMensagemAviso(null, "Valor a pagar maior que o saldo.", null);
			return null;
		}

		if (this.movimentacaoBancaria.getDataMovimentacao()
				.compareTo(this.movimentacaoBancaria.getContasPagar().getEmissao()) < 0) {
			JSFUtil.retornarMensagemAviso(null, "Data da movimentação menor que a data da emissão.", null);
			return null;
		}
		
		/*
		try {

			BigDecimal saldoAtual = this.movimentacaoBancaria.getBanco().getSaldoAtual()
					.subtract(this.movimentacaoBancaria.getValorSaida());

			this.movimentacaoBancaria.setSaldoAtual(saldoAtual);

			this.movimentacaoBancariaDAO.merge(this.movimentacaoBancaria);

			Banco banco = this.bancoDAO.lerPorId(this.movimentacaoBancaria.getBanco().getCodigo());
			banco.setSaldoAtual(saldoAtual);
			bancoDAO.merge(banco);

			ContasPagar cp = this.contasPagarDAO.lerPorId(movimentacaoBancaria.getContasPagar().getCodigo());
			BigDecimal saldoCP = cp.getSaldo().subtract(this.movimentacaoBancaria.getValorSaida());

			cp.setSaldo(saldoCP);
			cp.setBaixa(this.movimentacaoBancaria.getDataMovimentacao());
			this.contasPagarDAO.merge(cp);

			JSFUtil.retornarMensagemInfo(null, "Baixa realizada com sucesso.", null);

		} catch (Exception e) {
			JSFUtil.retornarMensagemFatal(null, "Erro ao baixar título", null);
		}
		*/
		
		this.contasPagarDAO.baixar(movimentacaoBancaria);
		
		return "/Financeiro/_financeiroBaixarCP";

	}

	public BancoDAO getBancoDAO() {
		return bancoDAO;
	}

	public ContasPagarDAO getContasPagarDAO() {
		return contasPagarDAO;
	}

	public void setBancoDAO(BancoDAO bancoDAO) {
		this.bancoDAO = bancoDAO;
	}

	public void setContasPagarDAO(ContasPagarDAO contasPagarDAO) {
		this.contasPagarDAO = contasPagarDAO;
	}

	public List<Banco> getBancos() {

		if (this.bancos == null)
			this.bancos = this.bancoDAO.lerTodos();

		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public MovimentacaoBancaria getMovimentacaoBancaria() {
		return movimentacaoBancaria;
	}

	public void setMovimentacaoBancaria(MovimentacaoBancaria movimentacaoBancaria) {
		this.movimentacaoBancaria = movimentacaoBancaria;
	}

	public List<ContasPagar> getContasPagarLista() {
		if (this.contasPagarLista == null)
			this.contasPagarLista = this.contasPagarDAO.buscaContasPagarNaoBaixadas();
		return contasPagarLista;
	}

	public void setContasPagarLista(List<ContasPagar> contasPagarLista) {
		this.contasPagarLista = contasPagarLista;
	}

	public MovimentacaoBancariaDAO getMovimentacaoBancariaDAO() {
		return movimentacaoBancariaDAO;
	}

	public void setMovimentacaoBancariaDAO(MovimentacaoBancariaDAO movimentacaoBancariaDAO) {
		this.movimentacaoBancariaDAO = movimentacaoBancariaDAO;
	}

}
