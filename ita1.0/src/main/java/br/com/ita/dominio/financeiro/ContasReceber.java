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

import br.com.ita.dominio.NTFe;
import br.com.ita.dominio.Venda;

@Entity
@Table(name = "contas_receber")
public class ContasReceber implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ContasReceberPK id;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A emiss�o � de preenchimento obrigat�rio.")
	private Date emissao;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A vencimento � de preenchimento obrigat�rio.")
	private Date vencimento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date baixa;

	@NotNull(message = "A valor � de preenchimento obrigat�rio.")
	@DecimalMin(value = "0.01", message = "O valor dever� ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal valor;

	@NotNull(message = "O saldo � de preenchimento obrigat�rio.")
	@DecimalMin(value = "0.00", message = "O saldo dever� ser um valor positivo")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal saldo;

	@Version
	private Integer versao;

	@ManyToOne
	private NTFe nfe;

	@ManyToOne
	private Venda venda;

	public ContasReceberPK getId() {
		return id;
	}

	public void setId(ContasReceberPK id) {
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

	public NTFe getNfe() {
		return nfe;
	}

	public void setNfe(NTFe nfe) {
		this.nfe = nfe;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
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
		ContasReceber other = (ContasReceber) obj;
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
