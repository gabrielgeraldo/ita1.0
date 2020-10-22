package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fincatto.documentofiscal.nfe400.classes.NFFinalidade;
import com.fincatto.documentofiscal.nfe400.classes.NFModalidadeFrete;
import com.fincatto.documentofiscal.nfe400.classes.NFTipo;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIdentificadorLocalDestinoOperacao;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorPresencaComprador;
import com.fincatto.documentofiscal.nfe400.classes.nota.NFOperacaoConsumidorFinal;

import br.com.ita.dominio.financeiro.ContasReceber;
import br.com.ita.dominio.financeiro.ContasReceberPK;

@Entity
@Table(name = "nfe")
public class NTFe implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_nfe", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_nfe", sequenceName = "codigo_nfe", allocationSize = 1)
	private Long codigo;

	@Column(length = 9, nullable = false)
	@NotNull(message = "O número é de preenchimento obrigatório.")
	private Integer numero;

	@Column(length = 3, nullable = false)
	@NotNull(message = "A série é de preenchimento obrigatório.")
	private Integer serie;

	@ManyToOne
	// @NotNull(message = "O cliente é de preenchimento obrigatório.")
	private Cliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A data é de preenchimento obrigatório.")
	private Date data = new Date();

	@NotNull(message = "O total é de preenchimento obrigatório.")
	@DecimalMin(value = "0.01", message = "O total deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal total;

	@Column(length = 15)
	@NotNull(message = "A natureza da operação é de preenchimento obrigatório.")
	private String naturezaOperacao;

	@Column(length = 2)
	@NotNull(message = "A finalidade é de preenchimento obrigatório.")
	private NFFinalidade nfFfinalidade;

	@Column(length = 2)
	@NotNull(message = "O tipo é de preenchimento obrigatório.")
	private NFTipo tipo;

	@Column(length = 2)
	@NotNull(message = "O Identificador local destino operação é de preenchimento obrigatório.")
	private NFIdentificadorLocalDestinoOperacao nfIdentificadorLocalDestinoOperacao;

	@Column(length = 2)
	@NotNull(message = "A operação consumidor final é de preenchimento obrigatório.")
	private NFOperacaoConsumidorFinal nfOperacaoConsumidorFinal;

	@Column(length = 2)
	@NotNull(message = "O Indicador presença comprador é de preenchimento obrigatório.")
	private NFIndicadorPresencaComprador nfIndicadorPresencaComprador;

	@Column(length = 2)
	@NotNull(message = "A modalidade de frete é de preenchimento obrigatório.")
	private NFModalidadeFrete nfModalidadeFrete;

	@Column(length = 44)
	private String chave;

	@Column(length = 15)
	private String numProtocolo;

	@Column(length = 3)
	private String status;

	@Column(length = 145)
	private String rejeicaoMotivo;

	@Column(length = 1)
	private String ambienteRecebimento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecebimento;

	@ManyToOne
	private NFEntrada nfEntrada;

	@ManyToOne
	private Orcamento orcamento;

	@ManyToOne
	@NotNull(message = "A condição de pagamento é de preenchimento obrigatório.")
	private CondicaoPagamento condicaoPagamento;

	@OneToMany(mappedBy = "nfe", cascade = CascadeType.ALL)
	private List<ContasReceber> contasReceber;

	@Transient
	private int numeroDeParcelas = 1;

	@Column(length = 145)
	@Size(min = 0, max = 145, message = "O campo informações complementares deve ter no máximo 145 caracteres.")
	private String informacoesComplementares;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getSerie() {
		return serie;
	}

	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(String naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public NFFinalidade getNfFfinalidade() {
		return nfFfinalidade;
	}

	public void setNfFfinalidade(NFFinalidade nfFfinalidade) {
		this.nfFfinalidade = nfFfinalidade;
	}

	public NFTipo getTipo() {
		return tipo;
	}

	public void setTipo(NFTipo tipo) {
		this.tipo = tipo;
	}

	public NFIdentificadorLocalDestinoOperacao getNfIdentificadorLocalDestinoOperacao() {
		return nfIdentificadorLocalDestinoOperacao;
	}

	public void setNfIdentificadorLocalDestinoOperacao(
			NFIdentificadorLocalDestinoOperacao nfIdentificadorLocalDestinoOperacao) {
		this.nfIdentificadorLocalDestinoOperacao = nfIdentificadorLocalDestinoOperacao;
	}

	public NFOperacaoConsumidorFinal getNfOperacaoConsumidorFinal() {
		return nfOperacaoConsumidorFinal;
	}

	public void setNfOperacaoConsumidorFinal(NFOperacaoConsumidorFinal nfOperacaoConsumidorFinal) {
		this.nfOperacaoConsumidorFinal = nfOperacaoConsumidorFinal;
	}

	public NFIndicadorPresencaComprador getNfIndicadorPresencaComprador() {
		return nfIndicadorPresencaComprador;
	}

	public void setNfIndicadorPresencaComprador(NFIndicadorPresencaComprador nfIndicadorPresencaComprador) {
		this.nfIndicadorPresencaComprador = nfIndicadorPresencaComprador;
	}

	public NFModalidadeFrete getNfModalidadeFrete() {
		return nfModalidadeFrete;
	}

	public void setNfModalidadeFrete(NFModalidadeFrete nfModalidadeFrete) {
		this.nfModalidadeFrete = nfModalidadeFrete;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getNumProtocolo() {
		return numProtocolo;
	}

	public void setNumProtocolo(String numProtocolo) {
		this.numProtocolo = numProtocolo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejeicaoMotivo() {
		return rejeicaoMotivo;
	}

	public void setRejeicaoMotivo(String rejeicaoMotivo) {
		this.rejeicaoMotivo = rejeicaoMotivo;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getAmbienteRecebimento() {
		return ambienteRecebimento;
	}

	public void setAmbienteRecebimento(String ambienteRecebimento) {
		this.ambienteRecebimento = ambienteRecebimento;
	}

	public NFEntrada getNfEntrada() {
		return nfEntrada;
	}

	public void setNfEntrada(NFEntrada nfEntrada) {
		this.nfEntrada = nfEntrada;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public List<ContasReceber> getContasReceber() {
		return contasReceber;
	}

	public void setContasReceber(List<ContasReceber> contasReceber) {
		this.contasReceber = contasReceber;
	}

	public int getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(int numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

	public void calculaParcelas() {

		ContasReceberPK crPK = new ContasReceberPK();
		ContasReceber cr = new ContasReceber();
		List<ContasReceber> contasReceber = new ArrayList<ContasReceber>();

		BigDecimal valorParcela = this.getTotal().divide(new BigDecimal(numeroDeParcelas), BigDecimal.ROUND_FLOOR);

		for (int i = 1; i <= numeroDeParcelas; i++) {

			crPK = new ContasReceberPK();
			cr = new ContasReceber();
			crPK.setCliente(this.getCliente());
			crPK.setNumeroCR(this.getNumero());
			crPK.setParcelaCR(i);

			cr.setEmissao(this.getData());
			cr.setId(crPK);
			cr.setNfe(this);
			cr.setSaldo(valorParcela);
			cr.setValor(valorParcela);

			Calendar vencimento = Calendar.getInstance();
			vencimento.setTime(this.getData());
			vencimento.set(Calendar.MONTH, vencimento.get(Calendar.MONTH) + i);

			cr.setVencimento(vencimento.getTime());

			contasReceber.add(cr);
			this.setContasReceber(contasReceber);
		}

	}

	public void excluirParcelas() {
		this.numeroDeParcelas = 1;
		this.contasReceber = null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NTFe other = (NTFe) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NTFe [numero=" + numero + "]";
	}

}
