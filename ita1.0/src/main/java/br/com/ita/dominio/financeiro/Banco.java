package br.com.ita.dominio.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ita.dominio.BaseEntity;

@Entity
@Table(name = "banco")
public class Banco implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_banco", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_banco", sequenceName = "codigo_banco", allocationSize = 1)
	private Long codigo;

	@Column(length = 70, nullable = false)
	@NotNull(message = "O nome da agencia é de preenchimento obrigatório.")
	@Size(min = 4, max = 70, message = "O nome da agencia deve ter entre 4 e 70 caracteres.")
	private String nomeAgencia;

	@Column(length = 5, nullable = false)
	private int numeroAgencia;

	@Column(length = 10, nullable = false)
	private int numeroConta;

	@NotNull(message = "A saldo atual é de preenchimento obrigatório.")
	@DecimalMin(value = "0.01", message = "O saldo atual deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal saldoAtual;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNomeAgencia() {
		return nomeAgencia;
	}

	public int getNumeroAgencia() {
		return numeroAgencia;
	}

	public int getNumeroConta() {
		return numeroConta;
	}

	public void setNomeAgencia(String nomeAgencia) {
		this.nomeAgencia = nomeAgencia;
	}

	public void setNumeroAgencia(int numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public void setNumeroConta(int numeroConta) {
		this.numeroConta = numeroConta;
	}

	public BigDecimal getSaldoAtual() {
		return saldoAtual;
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
		Banco other = (Banco) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nomeAgencia + " - " + numeroAgencia + " - " + numeroConta;
	}

}
