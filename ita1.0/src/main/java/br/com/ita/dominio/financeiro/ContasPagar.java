package br.com.ita.dominio.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import br.com.ita.dominio.NFEntrada;

@Entity
@Table(name = "contas_pagar")
public class ContasPagar implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ContasPagarPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A emissão é de preenchimento obrigatório.")
	private Date emissao;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A vencimento é de preenchimento obrigatório.")
	private Date vencimento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date baixa;

	@NotNull(message = "A valor é de preenchimento obrigatório.")
	@DecimalMin(value = "0.01", message = "O valor deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal valor;

	@NotNull(message = "O saldo é de preenchimento obrigatório.")
	@DecimalMin(value = "0.00", message = "O saldo deverá ser um valor positivo")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal saldo;

	@Version
	private Integer versao;

	@ManyToOne
	private NFEntrada nfEntrada;

	public ContasPagarPK getId() {
		return id;
	}

	public void setId(ContasPagarPK id) {
		this.id = id;
	}

	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public Date getBaixa() {
		return baixa;
	}

	public void setBaixa(Date baixa) {
		this.baixa = baixa;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public NFEntrada getNfEntrada() {
		return nfEntrada;
	}

	public void setNfEntrada(NFEntrada nfEntrada) {
		this.nfEntrada = nfEntrada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ContasPagar other = (ContasPagar) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id.toString();
	}

}
