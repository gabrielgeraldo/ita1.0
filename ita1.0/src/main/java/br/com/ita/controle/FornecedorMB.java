package br.com.ita.controle;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastro;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastroDados;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastroEndereco;
import com.fincatto.documentofiscal.nfe400.classes.cadastro.NFRetornoConsultaCadastroSituacaoCadastral;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorIEDestinatario;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.controle.util.ValidaCNPJ;
import br.com.ita.controle.util.ValidaCPF;
import br.com.ita.dominio.Estado;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.Municipio;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.FornecedorDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.notafiscal.NFeConfigIta;

@Named("fornecedorMB")
@RequestScoped
public class FornecedorMB {

	@Inject
	private Fornecedor fornecedor;

	@Inject
	private FornecedorDAO daoFornecedor;

	private List<Fornecedor> fornecedores = null;
	private List<Fornecedor> fornecedoresFiltrados = null;
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

		return "/Fornecedor/fornecedorListar?faces-redirect=true";

	}

	public void selecionaMunicipio() {
		municipios = daoMunicipio.lerMunicipiosPorEstado(fornecedor.getEstado().getUf());
	}

	public void consultar() {

		try {
			NFRetornoConsultaCadastro retorno = new WSFacade(config).consultaCadastro(this.fornecedor.getCgc(),
					DFUnidadeFederativa.valueOf(this.fornecedor.getEstado().getUf()));

			NFRetornoConsultaCadastroDados dados = retorno.getDados();

			List<NFRetornoConsultaCadastroSituacaoCadastral> situacaoCadastral = dados.getSituacaoCadastral();
			for (NFRetornoConsultaCadastroSituacaoCadastral x : situacaoCadastral) {

				NFRetornoConsultaCadastroEndereco endereco = x.getEndereco();

				this.fornecedor.setCgc(x.getCnpj());
				this.fornecedor.setInscricaoEstadual(x.getInscricaoEstadual());
				this.fornecedor.setRazaoSocial(x.getRazaoSocial());
				this.fornecedor.setNomeFantasia(x.getNomeFantasia());

				this.fornecedor.setMunicipio(daoMunicipio.lerMunicipiosPorCodigo(endereco.getCodigoMunicipio()));
				this.selecionaMunicipio();
				
				this.fornecedor.setCep(endereco.getCep());
				this.fornecedor.setBairro(endereco.getBairro());
				this.fornecedor.setNumero(endereco.getNumero());
				this.fornecedor.setEndereco(endereco.getLogradouro());
				this.fornecedor.setComplemento(endereco.getComplemento());

				JSFUtil.retornarMensagemInfo(null, "Consulta realizada com sucesso.", null);

			}

		} catch (Exception e) {
			JSFUtil.retornarMensagemAviso(null, e.getMessage(), null);
			e.printStackTrace();
		}

	}

	public String novo() {

		// limpar o objeto da página
		this.setFornecedor(new Fornecedor());

		this.fornecedor.setTipo("J");

		fornecedor.setNfIndicadorIEDestinatario(NFIndicadorIEDestinatario.NAO_CONTRIBUINTE);

		return "/Fornecedor/fornecedorEditar";

	}

	public String alterar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Fornecedor objetoDoBanco = this.daoFornecedor.lerPorId(codigo);
		this.setFornecedor(objetoDoBanco);

		selecionaMunicipio();

		return "/Fornecedor/fornecedorAlterar";

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		Fornecedor objetoDoBanco = this.daoFornecedor.lerPorId(codigo);
		this.daoFornecedor.remove(objetoDoBanco);

		if (this.daoFornecedor.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluído com sucesso.", null);
		}

		// limpar o objeto da página
		this.setFornecedor(new Fornecedor());
		// limpa a lista
		this.fornecedores = null;

		return "/Fornecedor/fornecedorListar";

	}

	public String salvar() {

		if (this.getFornecedor() != null) {
			Fornecedor objetoDoBanco = this.daoFornecedor.verificaSeFornecedorExiste(this.getFornecedor().getCgc(),
					this.getFornecedor().getInscricaoEstadual(), this.getFornecedor().getRazaoSocial());

			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null,
						"Outro fornecedor com o mesmo CPF/CNPJ ou razão social já existe no sistema.", null);
				return null;
			}
		}

		if (this.getFornecedor().getTipo().equals("J")) {

			if (!ValidaCNPJ.isCNPJ(this.getFornecedor().getCgc())) {
				JSFUtil.retornarMensagemErro(null, "CNPJ inválido.", null);
				return null;
			}

		} else {

			if (!ValidaCPF.isCPF(this.getFornecedor().getCgc())) {
				JSFUtil.retornarMensagemErro(null, "CPF inválido.", null);
				return null;
			}

		}

		this.daoFornecedor.persist(this.getFornecedor());
		// limpa a lista
		this.fornecedor = null;

		// limpar o objeto da página
		this.setFornecedor(new Fornecedor());

		JSFUtil.retornarMensagemInfo(null, "Salvo com sucesso.", null);

		// executa a ação listar e retorna a sua página
		return "/Fornecedor/fornecedorListar";

	}

	public String salvarAlteracao() {

		this.daoFornecedor.merge(this.getFornecedor());

		// limpa a lista
		this.fornecedores = null;

		// limpar o objeto da página
		this.setFornecedor(new Fornecedor());

		JSFUtil.retornarMensagemInfo(null, "Alterado com sucesso.", null);

		return "/Fornecedor/fornecedorListar";

	}

	public String cancelar() {

		// limpar o objeto da página
		this.setFornecedor(new Fornecedor());

		return "/Fornecedor/fornecedorListar";

	}

	public List<Fornecedor> getFornecedores() {
		if (this.fornecedores == null)
			this.fornecedores = this.daoFornecedor.lerTodos();

		return this.fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public FornecedorDAO getDaoFornecedor() {
		return daoFornecedor;
	}

	public void setDaoFornecedor(FornecedorDAO daoFornecedor) {
		this.daoFornecedor = daoFornecedor;
	}

	public List<Fornecedor> getFornecedoresFiltrados() {
		return fornecedoresFiltrados;
	}

	public void setFornecedoresFiltrados(List<Fornecedor> fornecedoresFiltrados) {
		this.fornecedoresFiltrados = fornecedoresFiltrados;
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
