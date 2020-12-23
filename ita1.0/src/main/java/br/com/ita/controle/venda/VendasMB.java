package br.com.ita.controle.venda;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.ita.controle.util.DanfeUtil;
import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.venda.util.VendaService;
import br.com.ita.dominio.ItemVenda;
import br.com.ita.dominio.Venda;
import br.com.ita.dominio.dao.ItemVendaDAO;
import br.com.ita.dominio.dao.VendaDAO;
import br.com.ita.dominio.dao.filtros.FiltroVenda;

@Named("vendasMB")
@RequestScoped
public class VendasMB {

	@Inject
	private Venda venda;

	@Inject
	private VendaDAO daoVenda;

	@Inject
	private ItemVendaDAO daoItemVenda;

	private List<Venda> vendas = null;

	private List<Venda> vendasFiltrados = null;

	private List<ItemVenda> itensVendas = null;

	private VendaService vendaService = null;

	@Inject
	private FiltroVenda filtro;

	public void consultar() {

		this.vendas = daoVenda.consultar(filtro);

		this.setFiltro(new FiltroVenda());

	}

	public String acaoListar() {
		return "/Vendas/vendasListar?faces-redirect=true";
	}

	public String vizualizar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Venda objetoDoBanco = this.daoVenda.lerPorId(codigo);
		this.setVenda(new Venda());
		this.setVenda(objetoDoBanco);

		this.itensVendas = this.daoItemVenda.buscaItens(this.venda);

		return "/Vendas/vendaVizualizar";

	}

	public void imprimirComprovanteVenda() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		this.daoVenda = new VendaDAO();
		Venda objetoDoBanco = this.daoVenda.lerPorId(codigo);
		this.setVenda(objetoDoBanco);

		this.daoItemVenda = new ItemVendaDAO(); // verificar new

		if (this.venda != null) {

			this.itensVendas = this.daoItemVenda.buscaItens(venda);

		}

		String XMLVenda = null;

		vendaService = new VendaService(this.venda, this.itensVendas);

		try {

			XMLVenda = vendaService.gerarXMLVenda();

		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.retornarMensagemErro(null, "Erro ao gerar comprovante: " + e.getMessage(), null);
		}

		// IMPRIMI A VENDA.
		if (XMLVenda != null) {

			// System.out.println(XMLVenda);

			try {
				DanfeUtil.imprimirComprovanteVenda(XMLVenda);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				JSFUtil.retornarMensagemErro(null, "Erro ao gerar comprovante: " + e.getMessage(), null);
			}

		} else {
			JSFUtil.retornarMensagemErro(null, "Erro ao imprimir comprovante.", null);
		}

	}

	public String cancelarVenda() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		this.daoVenda = new VendaDAO();
		Venda objetoDoBanco = this.daoVenda.lerPorId(codigo);
		this.setVenda(objetoDoBanco);

		if (venda.getSituacao().equals("CANCELADA")) {

			JSFUtil.retornarMensagemAviso("Esta venda j� est� cancelada!", null, null);

			return "/Vendas/vendasListar";
		}

		venda.setSituacao("CANCELADA");

		this.daoVenda.cancelarVenda(venda);

		return "/Vendas/vendasListar";

	}

	public void vizualizarItens() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		this.daoVenda = new VendaDAO();
		Venda objetoDoBanco = this.daoVenda.lerPorId(codigo);
		this.setVenda(objetoDoBanco);

		this.daoItemVenda = new ItemVendaDAO(); // verificar new

		if (this.venda != null) {

			this.itensVendas = this.daoItemVenda.buscaItens(venda);

		}

	}

	public VendaDAO getDaoVenda() {
		return daoVenda;
	}

	public void setDaoVenda(VendaDAO daoVenda) {
		this.daoVenda = daoVenda;
	}

	public ItemVendaDAO getDaoItemVenda() {
		return daoItemVenda;
	}

	public void setDaoItemVenda(ItemVendaDAO daoItemVenda) {
		this.daoItemVenda = daoItemVenda;
	}

	public List<Venda> getVendas() {
		return this.vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public List<Venda> getVendasFiltrados() {
		return vendasFiltrados;
	}

	public void setVendasFiltrados(List<Venda> vendassFiltrados) {
		this.vendasFiltrados = vendassFiltrados;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<ItemVenda> getItensVendas() {
		return itensVendas;
	}

	public void setItensVendas(List<ItemVenda> itensVendas) {
		this.itensVendas = itensVendas;
	}

	public VendaService getVendaService() {
		return vendaService;
	}

	public void setVendaService(VendaService vendaService) {
		this.vendaService = vendaService;
	}

	public FiltroVenda getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroVenda filtro) {
		this.filtro = filtro;
	}

}
