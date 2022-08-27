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

import br.com.ita.dominio.financeiro.ContasReceber;
import br.com.ita.dominio.financeiro.ContasReceberPK;

@Entity
@Table(name = "venda")
public class Venda implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(generator = "codigo_venda", strategy = GenerationType.SEQUENCE)
	// @SequenceGenerator(name = "codigo_venda", sequenceName = "codigo_venda", allocationSize = 1)
	private Long codigo;

	@ManyToOne
	@NotNull(message = "A condição de pagamento é de preenchimento obrigatório.")
	private CondicaoPagamento condicaoPagamento;

	@ManyToOne
	private Cliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date data = new Date();

	@NotNull(message = "O total é de preenchimento obrigatório.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal total;

	@Column(length = 15)
	@NotNull(message = "Situação não definida.")
	private String situacao;

	@ManyToOne
	private Orcamento orcamento;

	@NotNull(message = "O valor pagamento é de preenchimento obrigatório.")
	@DecimalMin(value = "0.01", message = "O valor pagamento deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false, columnDefinition = "decimal(7,2) default 0")
	private BigDecimal valorPagamento;

	@NotNull(message = "O valor troco é de preenchimento obrigatório.")
	@DecimalMin(value = "0.00", message = "O valor pagamento deverá ser superior ou igual a 0.00")
	@Column(precision = 7, scale = 2, nullable = false, columnDefinition = "decimal(7,2) default 0")
	private BigDecimal valorTroco;

	@Transient
	private int numeroDeParcelas = 1;

	@OneToMany(mappedBy = "nfe", cascade = CascadeType.ALL)
	private List<ContasReceber> contasReceber;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
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

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public BigDecimal getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(BigDecimal valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public BigDecimal getValorTroco() {
		return valorTroco;
	}

	public void setValorTroco(BigDecimal valorTroco) {
		this.valorTroco = valorTroco;
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
			crPK.setNumeroCR(codigo.intValue());
			crPK.setParcelaCR(i);

			cr.setEmissao(this.getData());
			cr.setId(crPK);
			cr.setVenda(this);
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

	public int getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(int numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public List<ContasReceber> getContasReceber() {
		return contasReceber;
	}

	public void setContasReceber(List<ContasReceber> contasReceber) {
		this.contasReceber = contasReceber;
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
		Venda other = (Venda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Venda [codigo=" + codigo + "]";
	}

}
