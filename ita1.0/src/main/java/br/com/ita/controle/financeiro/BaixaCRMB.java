package br.com.ita.controle.financeiro;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.dao.financeiro.BancoDAO;
import br.com.ita.dominio.dao.financeiro.ContasReceberDAO;
import br.com.ita.dominio.dao.financeiro.MovimentacaoBancariaDAO;
import br.com.ita.dominio.financeiro.Banco;
import br.com.ita.dominio.financeiro.ContasReceber;
import br.com.ita.dominio.financeiro.MovimentacaoBancaria;

@Named("baixaCRMB")
@ViewScoped
public class BaixaCRMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BancoDAO bancoDAO;

	@Inject
	private ContasReceberDAO contasReceberDAO;

	private List<Banco> bancos = null;

	private List<ContasReceber> contasReceberLista = null;

	@Inject
	private MovimentacaoBancaria movimentacaoBancaria;

	@Inject
	private MovimentacaoBancariaDAO movimentacaoBancariaDAO;

	public String listar() {

		return "/Financeiro/_financeiroBaixarCR?faces-redirect=true";

	}
	
	public String baixar() {

		if (this.movimentacaoBancaria.getContasReceber() == null) {
			JSFUtil.retornarMensagemErro(null, "O CR é de preenchimento obrigatório.", null);
			return null;
		}

		if (this.movimentacaoBancaria.getValorEntrada() == null) {
			JSFUtil.retornarMensagemErro(null, "Valor a receber é de preenchimento obrigatório.", null);
			return null;
		}

		if (this.movimentacaoBancaria.getValorEntrada()
				.compareTo(this.movimentacaoBancaria.getContasReceber().getSaldo()) > 0) {
			JSFUtil.retornarMensagemAviso(null, "Valor a receber maior que o saldo.", null);
			return null;
		}

		if (this.movimentacaoBancaria.getDataMovimentacao()
				.compareTo(this.movimentacaoBancaria.getContasReceber().getEmissao()) < 0) {
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

			ContasReceber cp = this.contasPagarDAO.lerPorId(movimentacaoBancaria.getContasReceber().getCodigo());
			BigDecimal saldoCP = cp.getSaldo().subtract(this.movimentacaoBancaria.getValorSaida());

			cp.setSaldo(saldoCP);
			cp.setBaixa(this.movimentacaoBancaria.getDataMovimentacao());
			this.contasPagarDAO.merge(cp);

			JSFUtil.retornarMensagemInfo(null, "Baixa realizada com sucesso.", null);

		} catch (Exception e) {
			JSFUtil.retornarMensagemFatal(null, "Erro ao baixar título", null);
		}
		*/
		
		this.contasReceberDAO.baixar(movimentacaoBancaria);
		
		return "/Financeiro/_financeiroBaixarCR";

	}


	public BancoDAO getBancoDAO() {
		return bancoDAO;
	}

	public ContasReceberDAO getContasReceberDAO() {
		return contasReceberDAO;
	}

	public List<Banco> getBancos() {

		if (this.bancos == null)
			this.bancos = this.bancoDAO.lerTodos();

		return bancos;
	}

	public List<ContasReceber> getContasReceberLista() {
		if (this.contasReceberLista == null)
			this.contasReceberLista = this.contasReceberDAO.buscaContasReceberNaoBaixadas();
		return contasReceberLista;
	}

	public MovimentacaoBancaria getMovimentacaoBancaria() {
		return movimentacaoBancaria;
	}

	public MovimentacaoBancariaDAO getMovimentacaoBancariaDAO() {
		return movimentacaoBancariaDAO;
	}

	public void setBancoDAO(BancoDAO bancoDAO) {
		this.bancoDAO = bancoDAO;
	}

	public void setContasReceberDAO(ContasReceberDAO contasReceberDAO) {
		this.contasReceberDAO = contasReceberDAO;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public void setContasReceberLista(List<ContasReceber> contasReceberLista) {
		this.contasReceberLista = contasReceberLista;
	}

	public void setMovimentacaoBancaria(MovimentacaoBancaria movimentacaoBancaria) {
		this.movimentacaoBancaria = movimentacaoBancaria;
	}

	public void setMovimentacaoBancariaDAO(MovimentacaoBancariaDAO movimentacaoBancariaDAO) {
		this.movimentacaoBancariaDAO = movimentacaoBancariaDAO;
	}
}
