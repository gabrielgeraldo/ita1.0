package br.com.ita.dominio.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ita.dominio.BaseEntity;

@Entity
@Table(name = "movimentacao_bancaria")
public class MovimentacaoBancaria implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_movimentacao_bancaria", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_movimentacao_bancaria", sequenceName = "codigo_movimentacao_bancaria", allocationSize = 1)
	private Long codigo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumns({ @JoinColumn(name = "numeroCP", referencedColumnName = "numeroCP"),
			@JoinColumn(name = "parcelaCP", referencedColumnName = "parcelaCP"),
			@JoinColumn(name = "fornecedor", referencedColumnName = "fornecedor") })
	private ContasPagar contasPagar;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumns({ @JoinColumn(name = "numeroCR", referencedColumnName = "numeroCR"),
			@JoinColumn(name = "parcelaCR", referencedColumnName = "parcelaCR"),
			@JoinColumn(name = "cliente", referencedColumnName = "cliente") })
	private ContasReceber contasReceber;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "banco_codigo")
	@NotNull(message = "O banco é de preenchimento obrigatório.")
	private Banco banco;

	@Column(length = 65, nullable = false)
	@NotNull(message = "O histórico é de preenchimento obrigatório.")
	@Size(min = 4, max = 65, message = "O histórico deve ter entre 4 e 65 caracteres.")
	private String historico;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A data da movimentação é de preenchimento obrigatório.")
	private Date dataMovimentacao = new Date();

	@DecimalMin(value = "0.01", message = "O valor saída deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2)
	private BigDecimal valorSaida;

	@DecimalMin(value = "0.01", message = "O valor entrada deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2)
	private BigDecimal valorEntrada;

	@NotNull(message = "O saldo atual é de preenchimento obrigatório.")
	@DecimalMin(value = "0.01", message = "O saldo atual deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal saldoAtual;

	public Long getCodigo() {
		return codigo;
	}

	public ContasPagar getContasPagar() {
		return contasPagar;
	}

	public ContasReceber getContasReceber() {
		return contasReceber;
	}

	public Banco getBanco() {
		return banco;
	}

	public String getHistorico() {
		return historico;
	}

	public Date getDataMovimentacao() {
		return dataMovimentacao;
	}

	public BigDecimal getValorSaida() {
		return valorSaida;
	}

	public BigDecimal getValorEntrada() {
		return valorEntrada;
	}

	public BigDecimal getSaldoAtual() {
		return saldoAtual;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setContasPagar(ContasPagar contasPagar) {
		this.contasPagar = contasPagar;
	}

	public void setContasReceber(ContasReceber contasReceber) {
		this.contasReceber = contasReceber;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public void setDataMovimentacao(Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}

	public void setValorSaida(BigDecimal valorSaida) {
		this.valorSaida = valorSaida;
	}

	public void setValorEntrada(BigDecimal valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public void setSaldoAtual(BigDecimal saldoAtual) {
		this.saldoAtual = saldoAtual;
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
		MovimentacaoBancaria other = (MovimentacaoBancaria) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MovimentacaoBancaria [codigo=" + codigo + "]";
	}

}
