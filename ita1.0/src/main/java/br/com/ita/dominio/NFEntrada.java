package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ita.dominio.financeiro.ContasPagar;

@Entity
@Table(name = "nfentrada")
public class NFEntrada implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_nfentrada", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_nfentrada", sequenceName = "codigo_nfentrada", allocationSize = 1)
	private Long codigo;

	@Column(length = 9, nullable = false)
	@NotNull(message = "O número é de preenchimento obrigatório.")
	private Integer numero;

	@Column(length = 3, nullable = false)
	@NotNull(message = "A série é de preenchimento obrigatório.")
	private Integer serie;

	@ManyToOne
	@NotNull(message = "O fornecedor é de preenchimento obrigatório.")
	private Fornecedor fornecedor;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A emissão é de preenchimento obrigatório.")
	private Date emissao;

	@NotNull(message = "O total é de preenchimento obrigatório.")
	@DecimalMin(value = "0.01", message = "O total deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal total;

	@Column(length = 44)
	@NotNull(message = "A chave é de preenchimento obrigatório.")
	@Size(min = 44, max = 44, message = "A chave deve ter 44 dígitos.")
	private String chave;

	@OneToMany(mappedBy = "nfEntrada", cascade = CascadeType.ALL)
	private List<ContasPagar> contasPagar;

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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public List<ContasPagar> getContasPagar() {
		return contasPagar;
	}

	public void setContasPagar(List<ContasPagar> contasPagar) {
		this.contasPagar = contasPagar;
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
		NFEntrada other = (NFEntrada) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NFEntrada [numero=" + numero + "]";
	}
}
