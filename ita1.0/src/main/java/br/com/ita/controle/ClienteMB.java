package br.com.ita.controle;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastro;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastroDados;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastroEndereco;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastroSituacaoCadastral;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorIEDestinatario;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

//import com.fincatto.nfe310.classes.nota.NFIndicadorIEDestinatario;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.ValidaCNPJ;
import br.com.ita.controle.util.ValidaCPF;
import br.com.ita.dominio.Cliente;
import br.com.ita.dominio.Estado;
import br.com.ita.dominio.Municipio;
import br.com.ita.dominio.dao.ClienteDAO;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

@Path("/cliente")
@Named("clienteMB")
@RequestScoped
public class ClienteMB {

	@Inject
	private Cliente cliente;

	@Inject
	private ClienteDAO daoCliente;

	private List<Cliente> clientes = null;
	private List<Cliente> clientesFiltrados = null;
	private List<NFIndicadorIEDestinatario> nfIndicadoresIEDestinatarios = null;

	@Inject
	private NFeConfigIta config;

	@Inject
	private EstadoDAO daoEstado;

	private List<Estado> estados = null;

	private List<Municipio> municipios = null;

	@Inject
	private MunicipioDAO daoMunicipio;

	@Inject
	private Municipio municipio;

	public String listar() {

		return "/Cliente/clienteListar?faces-redirect=true";

	}

	public void selecionaMunicipio() {
		municipios = daoMunicipio.lerMunicipiosPorEstado(cliente.getEstado().getUf());
	}

	public void consultar() {

		try {
			NFRetornoConsultaCadastro retorno = new WSFacade(config).consultaCadastro(this.cliente.getCgc(),
					DFUnidadeFederativa.valueOf(this.cliente.getEstado().getUf()));

			NFRetornoConsultaCadastroDados dados = retorno.getDados();

			List<NFRetornoConsultaCadastroSituacaoCadastral> situacaoCadastral = dados.getSituacaoCadastral();
			for (NFRetornoConsultaCadastroSituacaoCadastral x : situacaoCadastral) {

				NFRetornoConsultaCadastroEndereco endereco = x.getEndereco();

				this.cliente.setCgc(x.getCnpj());
				this.cliente.setInscEst(x.getInscricaoEstadual());
				this.cliente.setNome(x.getRazaoSocial());
				this.cliente.setNomeFantasia(x.getNomeFantasia());

				this.cliente.setMunicipio(daoMunicipio.lerMunicipiosPorCodigo(endereco.getCodigoMunicipio()));
				this.selecionaMunicipio();

				this.cliente.setCep(endereco.getCep());
				this.cliente.setBairro(endereco.getBairro());
				this.cliente.setNumero(endereco.getNumero());
				this.cliente.setEndereco(endereco.getLogradouro());
				this.cliente.setComplemento(endereco.getComplemento());

				JSFUtil.retornarMensagemInfo(null, "Consulta realizada com sucesso.", null);

			}

		} catch (Exception e) {
			JSFUtil.retornarMensagemAviso(null, e.getMessage(), null);
			e.printStackTrace();
		}

	}

	public String novo() {

		// limpar o objeto da p�gina
		this.setCliente(new Cliente());

		this.cliente.setTipo("F");

		cliente.setNfIndicadorIEDestinatario(NFIndicadorIEDestinatario.NAO_CONTRIBUINTE);

		return "/Cliente/clienteEditar";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Cliente objetoDoBanco = this.daoCliente.lerPorId(codigo);
		this.setCliente(objetoDoBanco);

		selecionaMunicipio();

		return "/Cliente/clienteAlterar";

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Cliente objetoDoBanco = this.daoCliente.lerPorId(codigo);
		this.daoCliente.remove(objetoDoBanco);

		if (this.daoCliente.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Exclu�do com sucesso.", null);
		}

		// limpar o objeto da p�gina
		this.setCliente(new Cliente());
		// limpa a lista
		this.clientes = null;

		return "/Cliente/clienteListar";

	}

	public String salvar() {

		if (this.getCliente() != null) {
			Cliente objetoDoBanco = this.daoCliente.verificaSeClienteExiste(this.getCliente().getCgc(),
					this.getCliente().getInscEst(), this.getCliente().getNome());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null, "Outro cliente com o mesmo CPF/CNPJ ou nome j� existe no sistema.",
						null);
				return null;
			}
		}

		if (this.getCliente().getTipo().equals("J")) {

			if (!ValidaCNPJ.isCNPJ(this.getCliente().getCgc())) {
				JSFUtil.retornarMensagemErro(null, "CNPJ inv�lido.", null);
				return null;
			}

		} else {

			if (!ValidaCPF.isCPF(this.getCliente().getCgc())) {
				JSFUtil.retornarMensagemErro(null, "CPF inv�lido.", null);
				return null;
			}

		}

		this.daoCliente.persist(this.getCliente());
		// limpa a lista
		this.cliente = null;

		// limpar o objeto da p�gina
		this.setCliente(new Cliente());

		JSFUtil.retornarMensagemInfo(null, "Salvo com sucesso.", null);

		// executa a a��o listar e retorna a sua p�gina
		return "/Cliente/clienteListar";

	}

	public String salvarAlteracao() {

		this.daoCliente.merge(this.getCliente());

		// limpa a lista
		this.clientes = null;

		// limpar o objeto da p�gina
		this.setCliente(new Cliente());

		JSFUtil.retornarMensagemInfo(null, "Alterado com sucesso.", null);

		return "/Cliente/clienteListar";

	}

	public String cancelar() {

		// limpar o objeto da p�gina
		this.setCliente(new Cliente());

		return "/Cliente/clienteListar";

	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ClienteDAO getDaoCliente() {
		return daoCliente;
	}

	public void setDaoCliente(ClienteDAO daoCliente) {
		this.daoCliente = daoCliente;
	}

	@GET
	@Produces("application/json; charset=UTF-8")
	@Path("/getClientes")
	public List<Cliente> getClientes() {
		if (this.clientes == null)
			this.clientes = this.daoCliente.lerTodos();

		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public void setClientesFiltrados(List<Cliente> clientesFiltrados) {
		this.clientesFiltrados = clientesFiltrados;
	}

	public List<NFIndicadorIEDestinatario> getNfIndicadoresIEDestinatarios() {
		if (this.nfIndicadoresIEDestinatarios == null)
			this.nfIndicadoresIEDestinatarios = Arrays.asList(NFIndicadorIEDestinatario.values());

		return nfIndicadoresIEDestinatarios;
	}

	public void setNfIndicadoresIEDestinatarios(List<NFIndicadorIEDestinatario> nfIndicadoresIEDestinatarios) {
		this.nfIndicadoresIEDestinatarios = nfIndicadoresIEDestinatarios;
	}

	public NFeConfigIta getConfig() {
		return config;
	}

	public void setConfig(NFeConfigIta config) {
		this.config = config;
	}

	public EstadoDAO getDaoEstado() {
		return daoEstado;
	}

	public void setDaoEstado(EstadoDAO daoEstado) {
		this.daoEstado = daoEstado;
	}

	public List<Estado> getEstados() {

		if (this.estados == null)
			this.estados = this.daoEstado.lerTodos();

		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	public MunicipioDAO getDaoMunicipio() {
		return daoMunicipio;
	}

	public void setDaoMunicipio(MunicipioDAO daoMunicipio) {
		this.daoMunicipio = daoMunicipio;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

}
