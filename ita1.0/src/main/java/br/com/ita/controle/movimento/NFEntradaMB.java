package br.com.ita.controle.movimento;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorIEDestinatario;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaProcessada;
import com.fincatto.documentofiscal.utils.DFPersister;

import br.com.ita.controle.util.JSFUtil;
import br.com.ita.dominio.NFEntrada;
import br.com.ita.dominio.Categoria;
import br.com.ita.dominio.Fornecedor;
import br.com.ita.dominio.ItemNFEntrada;
import br.com.ita.dominio.Produto;
import br.com.ita.dominio.TipoPesquisaProduto;
import br.com.ita.dominio.dao.CategoriaDAO;
import br.com.ita.dominio.dao.EstadoDAO;
import br.com.ita.dominio.dao.FornecedorDAO;
import br.com.ita.dominio.dao.ItemNFEntradaDAO;
import br.com.ita.dominio.dao.MunicipioDAO;
import br.com.ita.dominio.dao.NFEntradaDAO;
import br.com.ita.dominio.dao.ProdutoDAO;
import br.com.ita.dominio.dao.filtros.FiltroNFEntrada;
import br.com.ita.dominio.financeiro.ContasPagar;
import br.com.ita.dominio.financeiro.ContasPagarPK;

@Named("nfEntradaMB")
@ConversationScoped
public class NFEntradaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversacao;

	@Inject
	private NFEntrada nfEntrada;

	@Inject
	private Produto produto;

	@Inject
	private ProdutoDAO daoProduto;

	@Inject
	private FornecedorDAO daoFornecedor;

	@Inject
	private ItemNFEntrada itemNFEntrada;

	@Inject
	private NFEntradaDAO daoNFEntrada;

	private List<ItemNFEntrada> itensNFEntrada = null;

	@Inject
	private FiltroNFEntrada filtro;

	private List<NFEntrada> notasNFEntrada = null;

	private List<NFEntrada> notasNFEntradaFiltrados = null;

	@Inject
	private ItemNFEntradaDAO daoItemNFEntrada;

	@NotNull(message = "O novo preço de custo é de preenchimento obrigatário.")
	private BigDecimal novoPrecoCusto;

	@Inject
	private CategoriaDAO daoCategoria;

	private boolean bloqueaCampos;

	@Inject
	private EstadoDAO daoEstado;

	@Inject
	private MunicipioDAO daoMunicipio;

	private List<TipoPesquisaProduto> tiposPesquisaProduto = null;

	private TipoPesquisaProduto tipoPesquisaProduto;

	public List<Fornecedor> completeFornecedor(String fornecedor) {
		return this.daoFornecedor.autoCompleteFornecedor(fornecedor);
	}

	public List<Produto> completeProduto(String produto) {

		if (tipoPesquisaProduto == TipoPesquisaProduto.CODIGO) {
			return this.daoProduto.autoCompleteProdutoPorCodigo(produto);
		}
		
		if (tipoPesquisaProduto == TipoPesquisaProduto.CODIGOBARRAS) {
			return this.daoProduto.autoCompleteProdutoPorCodigoDeBarras(produto);
		}

		if (tipoPesquisaProduto == TipoPesquisaProduto.DESCRICAO) {
			return this.daoProduto.autoCompleteProdutoPorDescricao(produto);
		}

		return null;
	}

	public String listar() {

		return "/NFEntrada/nfEntradaListar?faces-redirect=true";

	}

	public void consultar() {

		this.notasNFEntrada = daoNFEntrada.consultar(filtro);

		this.setFiltro(new FiltroNFEntrada());

	}

	public String vizualizar() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NFEntrada objetoDoBanco = this.daoNFEntrada.lerPorId(codigo);
		this.setNfEntrada(new NFEntrada());
		this.setNfEntrada(objetoDoBanco);

		this.itensNFEntrada = this.daoItemNFEntrada.buscaItens(this.nfEntrada);

		return "/NFEntrada/nfEntradaVizualizar";

	}

	public String novo() {

		if (conversacao.isTransient()) {
			conversacao.setTimeout(900000);
			conversacao.begin();
		}

		this.setNfEntrada(new NFEntrada());
		this.setItensNFEntrada(new ArrayList<ItemNFEntrada>());

		nfEntrada.setTotal(new BigDecimal("0.00"));

		bloqueaCampos = false;

		itemNFEntrada.setQuantidade(1);

		this.setTipoPesquisaProduto(TipoPesquisaProduto.DESCRICAO);

		return "/NFEntrada/nfEntradaEditar";

	}

	public void removerProduto(ItemNFEntrada itemNFEntrada) {

		// -------------------- Método remover.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNFEntrada.size() && posicaoEncntrada < 0; i++) {
			ItemNFEntrada itemTemp = itensNFEntrada.get(i);

			if (itemTemp.getProduto().equals(itemNFEntrada.getProduto())) {
				posicaoEncntrada = i;
			}
		}

		if (posicaoEncntrada > -1) {
			itensNFEntrada.remove(posicaoEncntrada);
		}

		nfEntrada.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensNFEntrada.size(); j++) {
			nfEntrada.setTotal(nfEntrada.getTotal().add(itensNFEntrada.get(j).getPrecoVenda()
					.multiply(new BigDecimal(itensNFEntrada.get(j).getQuantidade()))));

		}
		// -------------------- Método remover.
	}

	public String finalizar() {

		if (this.getNfEntrada().getCodigo() == null) {

			NFEntrada objetoDoBanco = this.daoNFEntrada.lerPorChave(this.getNfEntrada().getChave());
			if (objetoDoBanco != null) {
				JSFUtil.retornarMensagemAviso(null, "Outra NF com a mesma chave já existe no sistema.", null);
				return null;
			}

			NFEntrada objetoDoBanco2 = this.daoNFEntrada.lerPorNumeroSerieFornecedor(this.getNfEntrada().getNumero(),
					this.getNfEntrada().getSerie(), this.getNfEntrada().getFornecedor());
			if (objetoDoBanco2 != null) {
				JSFUtil.retornarMensagemAviso(null,
						"Outra NF com a mesmo número/série/fornecedor já existe no sistema.", null);
				return null;
			}

		}

		try {

			daoNFEntrada.salvarNFEntrada(nfEntrada, itensNFEntrada);

			nfEntrada = new NFEntrada();
			nfEntrada.setTotal(new BigDecimal("0.00"));

			itensNFEntrada = new ArrayList<ItemNFEntrada>();

			// JSFUtil.retornarMensagemInfo(null, "Salvo/Alterado com sucesso.",
			// null);

			if (!conversacao.isTransient()) {
				conversacao.end();
			}

			return "/NFEntrada/nfEntradaListar";

		} catch (Exception e) {

			JSFUtil.retornarMensagemFatal(null, "Falha ao salvar.", null);

			return null;

		}

	}

	public String cancelar() {

		// limpar o objeto da página
		this.setNfEntrada(new NFEntrada());

		return "/NFEntrada/nfEntradaListar";
	}

	public void adicionar() {

		Produto produto = itemNFEntrada.getProduto();

		// -------------------- Método adicionar.
		int posicaoEncntrada = -1;

		for (int i = 0; i < itensNFEntrada.size() && posicaoEncntrada < 0; i++) {
			ItemNFEntrada itemTemp = itensNFEntrada.get(i);

			if (itemTemp.getProduto().equals(produto)) {
				posicaoEncntrada = i;
			}
		}

		itemNFEntrada.setProduto(produto);

		if (posicaoEncntrada < 0) {
			itemNFEntrada.setPrecoCusto(novoPrecoCusto);
			itemNFEntrada.setPrecoVenda(novoPrecoCusto);
			itensNFEntrada.add(itemNFEntrada);
		} else {
			ItemNFEntrada itemTemp = itensNFEntrada.get(posicaoEncntrada);
			itemNFEntrada.setQuantidade(itemTemp.getQuantidade() + itemNFEntrada.getQuantidade());
			itemNFEntrada.setPrecoCusto(novoPrecoCusto);
			itemNFEntrada.setPrecoVenda(novoPrecoCusto);
			itensNFEntrada.set(posicaoEncntrada, itemNFEntrada);
		}

		nfEntrada.setTotal(new BigDecimal("0.00"));
		for (int j = 0; j < itensNFEntrada.size(); j++) {
			nfEntrada.setTotal(nfEntrada.getTotal().add(itensNFEntrada.get(j).getPrecoVenda()
					.multiply(new BigDecimal(itensNFEntrada.get(j).getQuantidade()))));

		}
		// -------------------- Método adicionar.

		this.novoPrecoCusto = null;
		this.setItemNFEntrada(new ItemNFEntrada());
		itemNFEntrada.setQuantidade(1);

		boolean fecharDialog = true;
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.addCallbackParam("fecharDialog", fecharDialog);
		PrimeFaces.current().ajax().addCallbackParam("fecharDialog", fecharDialog);

		JSFUtil.retornarMensagemInfo(null, "Adicionado com sucesso.", null);

	}

	public void adcionarProdutoEntradaXML(Produto produto) {

		itemNFEntrada = new ItemNFEntrada();
		itemNFEntrada.setProduto(produto);

		itemNFEntrada.setQuantidade(produto.getQtdEstq());
		itemNFEntrada.setPrecoCusto(produto.getPrecoCusto());

		itemNFEntrada.setPrecoVenda(daoProduto.lerPorId(produto.getCodigo()).getPrecoUnitario());

		itensNFEntrada.add(itemNFEntrada);

		/*
		 * int posicaoEncntrada = -1;
		 * 
		 * for (int i = 0; i < itensNFEntrada.size() && posicaoEncntrada < 0;
		 * i++) { ItemNFEntrada itemTemp = itensNFEntrada.get(i);
		 * 
		 * if (itemTemp.getProduto().equals(produto)) { posicaoEncntrada = i; }
		 * }
		 * 
		 * itemNFEntrada = new ItemNFEntrada();
		 * itemNFEntrada.setProduto(produto);
		 * 
		 * if (posicaoEncntrada < 0) { itemNFEntrada.setQuantidade(1);
		 * itemNFEntrada.setPrecoCusto(produto.getPrecoCusto()); //
		 * itemNFEntrada.setPrecoVenda(produto.getPrecoUnitario());
		 * itensNFEntrada.add(itemNFEntrada); } else { ItemNFEntrada itemTemp =
		 * itensNFEntrada.get(posicaoEncntrada);
		 * itemNFEntrada.setQuantidade(itemTemp.getQuantidade() + 1);
		 * itemNFEntrada.setPrecoCusto(produto.getPrecoCusto()); //
		 * itemNFEntrada.setPrecoVenda(produto.getPrecoUnitario().multiply(new
		 * // BigDecimal(itemNFEntrada.getQuantidade())));
		 * itensNFEntrada.set(posicaoEncntrada, itemNFEntrada); }
		 */
		itemNFEntrada = new ItemNFEntrada();

	}

	public String excluir() {

		Long codigo = JSFUtil.getParametroLong("itemcodigo");

		NFEntrada objetoDoBanco = this.daoNFEntrada.lerPorId(codigo);
		this.setNfEntrada(new NFEntrada());
		this.setNfEntrada(objetoDoBanco);

		this.itensNFEntrada = this.daoItemNFEntrada.buscaItens(this.nfEntrada);

		this.daoNFEntrada.excluirNFEntrada(objetoDoBanco, this.itensNFEntrada);

		if (this.daoNFEntrada.lerPorId(objetoDoBanco.getCodigo()) == null) {
			JSFUtil.retornarMensagemInfo(null, "Excluído com sucesso.", null);
		}

		// limpar o objeto da página
		this.setNfEntrada(new NFEntrada());
		// limpa a lista
		this.notasNFEntrada = null;

		return "/NFEntrada/nfEntradaListar";

	}

	public void upload(FileUploadEvent event) {
		
		this.setNfEntrada(new NFEntrada());
		this.setItensNFEntrada(new ArrayList<ItemNFEntrada>());

		nfEntrada.setTotal(new BigDecimal("0.00"));

		UploadedFile file = event.getFile();
		InputStream input = null;
		try {
			input = file.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
			e.printStackTrace();
		}
		String xml = null;
		try {
			xml = IOUtils.toString(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
			e.printStackTrace();
		}

		NFNotaProcessada nota = null;
		try {
			nota = new DFPersister().read(NFNotaProcessada.class, xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
			e.printStackTrace();
		}

		if (nota != null) {

			Fornecedor fornecedor = daoFornecedor.verificaSeFornecedorExiste(
					nota.getNota().getInfo().getEmitente().getCnpj(),
					nota.getNota().getInfo().getEmitente().getInscricaoEstadual(),
					nota.getNota().getInfo().getEmitente().getRazaoSocial());

			if (fornecedor == null) {
				System.out.println("Fornecedor não cadastrado!");
				try {
					System.out.println("Cadastrando fornecedor...");
					fornecedor = new Fornecedor();
					fornecedor
							.setBairro(nota.getNota().getInfo().getEmitente().getEndereco().getBairro().toUpperCase());
					fornecedor.setCep(nota.getNota().getInfo().getEmitente().getEndereco().getCep());
					fornecedor.setCgc(nota.getNota().getInfo().getEmitente().getCnpj());

					fornecedor.setComplemento(
							nota.getNota().getInfo().getEmitente().getEndereco().getComplemento() != null ? nota
									.getNota().getInfo().getEmitente().getEndereco().getComplemento().toUpperCase()
									: "SEM COMPLEMENTO");

					fornecedor.setEndereco(
							nota.getNota().getInfo().getEmitente().getEndereco().getLogradouro().toUpperCase());
					fornecedor.setInscricaoEstadual(nota.getNota().getInfo().getEmitente().getInscricaoEstadual());

					fornecedor.setMunicipio(daoMunicipio.lerPorId(
							Long.parseLong(nota.getNota().getInfo().getEmitente().getEndereco().getCodigoMunicipio())));

					fornecedor.setNomeFantasia(nota.getNota().getInfo().getEmitente().getNomeFantasia() != null
							? nota.getNota().getInfo().getEmitente().getNomeFantasia().toUpperCase()
							: nota.getNota().getInfo().getEmitente().getRazaoSocial().toUpperCase());
					fornecedor.setNumero(nota.getNota().getInfo().getEmitente().getEndereco().getNumero());
					fornecedor.setRazaoSocial(nota.getNota().getInfo().getEmitente().getRazaoSocial().toUpperCase());

					fornecedor.setTelefone(nota.getNota().getInfo().getEmitente().getEndereco().getTelefone() != null
							? nota.getNota().getInfo().getEmitente().getEndereco().getTelefone() : "0000000000");

					fornecedor.setTipo("J");

					fornecedor.setEstado(
							daoEstado.lerEstadoPorUF(nota.getNota().getInfo().getEmitente().getEndereco().getUf()));

					fornecedor.setNfIndicadorIEDestinatario(NFIndicadorIEDestinatario.NAO_CONTRIBUINTE);

					fornecedor.setEmail("sememail@a.com");

					daoFornecedor.merge(fornecedor);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Erro ao cadastrar fornecedor!");
					JSFUtil.retornarMensagemErro(null, "Erro ao cadastrar fornecedor!", null);
					JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
					e.printStackTrace();
				}

			}

			// Verifica novamente se o fornecedor existe.
			// Se houver erro no merge, a veriavel fica null.
			fornecedor = daoFornecedor.verificaSeFornecedorExiste(nota.getNota().getInfo().getEmitente().getCnpj(),
					nota.getNota().getInfo().getEmitente().getInscricaoEstadual(),
					nota.getNota().getInfo().getEmitente().getRazaoSocial());

			Categoria categoria = daoCategoria.lerPorDescricao("CATEGORIA PADRAO");
			if (categoria == null) {
				System.out.println("Categoria não cadastrada!");
				try {
					System.out.println("Cadastrando categoria...");
					categoria = new Categoria();
					categoria.setDescricao("CATEGORIA PADRAO");
					daoCategoria.merge(categoria);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Erro ao cadastrar categoria!");
					JSFUtil.retornarMensagemErro(null, "Erro ao cadastrar categoria!", null);
					JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
					e.printStackTrace();
				}
			}

			if (fornecedor != null && categoria != null) {

				for (int i = 0; i < nota.getNota().getInfo().getItens().size(); i++) {
					Produto produto = daoProduto
							.lerPorId(nota.getNota().getInfo().getItens().get(i).getProduto().getCodigo());
					if (produto == null) {
						System.out.println("Produto (código): "
								+ nota.getNota().getInfo().getItens().get(i).getProduto().getCodigo()
								+ " não cadastrador");

						try {
							System.out.println("Cadastrando produto (código): "
									+ nota.getNota().getInfo().getItens().get(i).getProduto().getCodigo() + "...");
							produto = new Produto();
							produto.setCategoria(daoCategoria.lerPorDescricao("CATEGORIA PADRAO"));
							produto.setCest(nota.getNota().getInfo().getItens().get(i).getProduto()
									.getCodigoEspecificadorSituacaoTributaria());
							produto.setCodigo(nota.getNota().getInfo().getItens().get(i).getProduto().getCodigo());
							produto.setCodigoBarras(
									nota.getNota().getInfo().getItens().get(i).getProduto().getCodigoDeBarras());
							produto.setDescricao(nota.getNota().getInfo().getItens().get(i).getProduto().getDescricao()
									.toUpperCase());
							produto.setFornecedor(daoFornecedor.verificaSeFornecedorExiste(
									nota.getNota().getInfo().getEmitente().getCnpj(),
									nota.getNota().getInfo().getEmitente().getInscricaoEstadual(),
									nota.getNota().getInfo().getEmitente().getRazaoSocial()));
							produto.setNcm(nota.getNota().getInfo().getItens().get(i).getProduto().getNcm());

							/*
							 * produto.setPrecoCusto(new
							 * java.math.BigDecimal("0.00"));
							 * produto.setPrecoUnitario(new
							 * java.math.BigDecimal("0.00"));
							 */

							produto.setPrecoCusto(new BigDecimal(
									nota.getNota().getInfo().getItens().get(i).getProduto().getValorUnitario()));
							produto.setPrecoUnitario(new BigDecimal(
									nota.getNota().getInfo().getItens().get(i).getProduto().getValorUnitario()));

							produto.setQtdEstq(0);
							
							produto.setUnidadeComercial(nota.getNota().getInfo().getItens().get(i).getProduto().getUnidadeTributavel());
							
							daoProduto.merge(produto);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							System.out.println("Erro ao cadastrar produto!");
							JSFUtil.retornarMensagemErro(null, "Erro ao cadastrar produto!", null);
							JSFUtil.retornarMensagemErro(null, e.getMessage(), null);
							e.printStackTrace();
						}

					}
				}

				// VERIFICA SE TODOS OS PRODUTOS REALMENTE ESTÃO
				// CADASTRADOS.
				// NESSE PONTO O FORNECEDOR E A CATEGORIA JA ESTÃO
				// CADASTRADOS
				boolean todosOsProdutosCadastrados = true;
				for (int x = 0; x < nota.getNota().getInfo().getItens().size(); x++) {
					Produto produtoCADASTRADO = daoProduto
							.lerPorId(nota.getNota().getInfo().getItens().get(x).getProduto().getCodigo());
					if (produtoCADASTRADO == null) {
						todosOsProdutosCadastrados = false;
						System.out.println("Houve erro no cadastro do produto: "
								+ nota.getNota().getInfo().getItens().get(x).getProduto().getCodigo());
					}
				}

				if (todosOsProdutosCadastrados) {
					// System.out.println("Todos os dados necessários foram
					// cadastrador!");

					this.setNfEntrada(new NFEntrada());

					this.nfEntrada.setChave(nota.getNota().getInfo().getChaveAcesso());
					this.nfEntrada.setEmissao(
							Date.from(nota.getNota().getInfo().getIdentificacao().getDataHoraEmissao().toInstant()));
					this.nfEntrada.setFornecedor(
							daoFornecedor.verificaSeFornecedorExiste(nota.getNota().getInfo().getEmitente().getCnpj(),
									nota.getNota().getInfo().getEmitente().getInscricaoEstadual(),
									nota.getNota().getInfo().getEmitente().getRazaoSocial()));
					this.nfEntrada
							.setNumero(Integer.parseInt(nota.getNota().getInfo().getIdentificacao().getNumeroNota()));
					this.nfEntrada.setSerie(Integer.parseInt(nota.getNota().getInfo().getIdentificacao().getSerie()));
					this.nfEntrada.setTotal(new BigDecimal(
							nota.getNota().getInfo().getTotal().getIcmsTotal().getValorTotalDosProdutosServicos()));

					for (int x = 0; x < nota.getNota().getInfo().getItens().size(); x++) {
						Produto produtoQueVaiSerAdicionadoNaNFEntrada = daoProduto
								.lerPorId(nota.getNota().getInfo().getItens().get(x).getProduto().getCodigo());

						BigDecimal qtd = new BigDecimal(
								nota.getNota().getInfo().getItens().get(x).getProduto().getQuantidadeComercial());
						produtoQueVaiSerAdicionadoNaNFEntrada.setQtdEstq(Integer.valueOf(qtd.intValue()));

						produtoQueVaiSerAdicionadoNaNFEntrada.setPrecoCusto(new BigDecimal(
								nota.getNota().getInfo().getItens().get(x).getProduto().getValorUnitario()));

						try {
							this.adcionarProdutoEntradaXML(produtoQueVaiSerAdicionadoNaNFEntrada);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					if (nota.getNota().getInfo().getCobranca() != null) {

						ContasPagar contaPagar = new ContasPagar();
						ContasPagarPK contaPagarPK = new ContasPagarPK();
						List<ContasPagar> contasPagar = new ArrayList<ContasPagar>();

						if (nota.getNota().getInfo().getCobranca().getParcelas() != null) {

							for (int x = 0; x < nota.getNota().getInfo().getCobranca().getParcelas().size(); x++) {

								contaPagar.setEmissao(Date.from(
										nota.getNota().getInfo().getIdentificacao().getDataHoraEmissao().toInstant()));

								contaPagarPK.setFornecedor(fornecedor);
								contaPagarPK.setNumeroCP(
										Integer.parseInt(nota.getNota().getInfo().getIdentificacao().getSerie()
												+ nota.getNota().getInfo().getIdentificacao().getNumeroNota()));
								contaPagarPK.setParcelaCP(Integer.parseInt(nota.getNota().getInfo().getCobranca()
										.getParcelas().get(x).getNumeroParcela()));

								contaPagar.setId(contaPagarPK);

								// contaPagar.setFornecedor(fornecedor);
								contaPagar.setNfEntrada(nfEntrada);

								// contaPagar.setNumero(Integer.parseInt(nota.getNota().getInfo().getIdentificacao().getSerie()
								// +
								// nota.getNota().getInfo().getIdentificacao().getNumeroNota()));

								// contaPagar.setParcela(Integer.parseInt(
								// nota.getNota().getInfo().getCobranca().getParcelas().get(x).getNumeroParcela()));

								contaPagar.setValor(new BigDecimal(
										nota.getNota().getInfo().getCobranca().getParcelas().get(x).getValorParcela()));
								contaPagar.setSaldo(new BigDecimal(
										nota.getNota().getInfo().getCobranca().getParcelas().get(x).getValorParcela()));
								contaPagar.setVencimento(Date.from(nota.getNota().getInfo().getCobranca().getParcelas()
										.get(x).getDataVencimento().atStartOfDay(ZoneId.systemDefault()).toInstant()));
								contasPagar.add(contaPagar);
								this.nfEntrada.setContasPagar(contasPagar);
								contaPagar = new ContasPagar();
								contaPagarPK = new ContasPagarPK();

							}

						}

					}

					bloqueaCampos = true;

				}

			}

		}

	}

	public Conversation getConversacao() {
		return conversacao;
	}

	public NFEntrada getNfEntrada() {
		return nfEntrada;
	}

	public Produto getProduto() {
		return produto;
	}

	public ProdutoDAO getDaoProduto() {
		return daoProduto;
	}

	public FornecedorDAO getDaoFornecedor() {
		return daoFornecedor;
	}

	public ItemNFEntrada getItemNFEntrada() {
		return itemNFEntrada;
	}

	public NFEntradaDAO getDaoNFEntrada() {
		return daoNFEntrada;
	}

	public List<ItemNFEntrada> getItensNFEntrada() {
		return itensNFEntrada;
	}

	public FiltroNFEntrada getFiltro() {
		return filtro;
	}

	public List<NFEntrada> getNotasNFEntrada() {
		return notasNFEntrada;
	}

	public List<NFEntrada> getNotasNFEntradaFiltrados() {
		return notasNFEntradaFiltrados;
	}

	public ItemNFEntradaDAO getDaoItemNFEntrada() {
		return daoItemNFEntrada;
	}

	public void setConversacao(Conversation conversacao) {
		this.conversacao = conversacao;
	}

	public void setNfEntrada(NFEntrada nfEntrada) {
		this.nfEntrada = nfEntrada;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setDaoProduto(ProdutoDAO daoProduto) {
		this.daoProduto = daoProduto;
	}

	public void setDaoFornecedor(FornecedorDAO daoFornecedor) {
		this.daoFornecedor = daoFornecedor;
	}

	public CategoriaDAO getDaoCategoria() {
		return daoCategoria;
	}

	public void setDaoCategoria(CategoriaDAO daoCategoria) {
		this.daoCategoria = daoCategoria;
	}

	public void setItemNFEntrada(ItemNFEntrada itemNFEntrada) {
		this.itemNFEntrada = itemNFEntrada;
	}

	public void setDaoNFEntrada(NFEntradaDAO daoNFEntrada) {
		this.daoNFEntrada = daoNFEntrada;
	}

	public void setItensNFEntrada(List<ItemNFEntrada> itensNFEntrada) {
		this.itensNFEntrada = itensNFEntrada;
	}

	public void setFiltro(FiltroNFEntrada filtro) {
		this.filtro = filtro;
	}

	public void setNotasNFEntrada(List<NFEntrada> notasNFEntrada) {
		this.notasNFEntrada = notasNFEntrada;
	}

	public void setNotasNFEntradaFiltrados(List<NFEntrada> notasNFEntradaFiltrados) {
		this.notasNFEntradaFiltrados = notasNFEntradaFiltrados;
	}

	public void setDaoItemNFEntrada(ItemNFEntradaDAO daoItemNFEntrada) {
		this.daoItemNFEntrada = daoItemNFEntrada;
	}

	public BigDecimal getNovoPrecoCusto() {
		return novoPrecoCusto;
	}

	public void setNovoPrecoCusto(BigDecimal novoPrecoCusto) {
		this.novoPrecoCusto = novoPrecoCusto;
	}

	public boolean isBloqueaCampos() {
		return bloqueaCampos;
	}

	public void setBloqueaCampos(boolean bloqueaCampos) {
		this.bloqueaCampos = bloqueaCampos;
	}

	public EstadoDAO getDaoEstado() {
		return daoEstado;
	}

	public void setDaoEstado(EstadoDAO daoEstado) {
		this.daoEstado = daoEstado;
	}

	public MunicipioDAO getDaoMunicipio() {
		return daoMunicipio;
	}

	public void setDaoMunicipio(MunicipioDAO daoMunicipio) {
		this.daoMunicipio = daoMunicipio;
	}

	public List<TipoPesquisaProduto> getTiposPesquisaProduto() {

		if (this.tiposPesquisaProduto == null)
			this.tiposPesquisaProduto = Arrays.asList(TipoPesquisaProduto.values());

		return tiposPesquisaProduto;
	}

	public void setTiposPesquisaProduto(List<TipoPesquisaProduto> tiposPesquisaProduto) {
		this.tiposPesquisaProduto = tiposPesquisaProduto;
	}

	public TipoPesquisaProduto getTipoPesquisaProduto() {
		return tipoPesquisaProduto;
	}

	public void setTipoPesquisaProduto(TipoPesquisaProduto tipoPesquisaProduto) {
		this.tipoPesquisaProduto = tipoPesquisaProduto;
	}

}
