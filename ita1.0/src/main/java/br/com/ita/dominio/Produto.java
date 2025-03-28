package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "produto")
public class Produto implements BaseEntityP, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotEmpty(message = "O código � de preenchimento obrigatório.")
	private String codigo;

	@Column(length = 120, nullable = false)
	@NotNull(message = "A descriçãoo é de preenchimento obrigatório.")
	@Size(min = 1, max = 120, message = "A descrição deve ter entre 1 e 120 caracteres.")
	private String descricao;

	@ManyToOne
	@NotNull(message = "O fornecedor é de preenchimento obrigatório.")
	private Fornecedor fornecedor;

	@ManyToOne
	@NotNull(message = "A categoria é de preenchimento obrigatório.")
	private Categoria categoria;

	@Column(precision = 7, scale = 2, nullable = false)
	@NotNull(message = "O preço de custo é de preenchimento obrigatório.")
	private BigDecimal precoCusto;

	@Column(precision = 7, scale = 2, nullable = false)
	@NotNull(message = "O preço de unitário é de preenchimento obrigatório.")
	private BigDecimal precoUnitario;

	@Column(nullable = false)
	@NotNull(message = "A quantidade em estoque é de preenchimento obrigatório.")
	private int qtdEstq;

	// @ManyToOne
	private String ncm;

	@Column(length = 6, nullable = false)
	@Size(min = 1, max = 6, message = "A unidade comercia deve ter entre 1 e 6 caracteres.")
	@NotNull(message = "A unidade comercia é de preenchimento obrigatório.")
	private String unidadeComercial;

	@Column(length = 7)
	@Size(min = 0, max = 7, message = "O código CEST deve ter 7 dígitos.")
	private String Cest;

	@Column(length = 15)
	@Size(min = 0, max = 15, message = "O código de barras deve ter entre 0 e 15 caracteres.")
	private String codigoBarras;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public int getQtdEstq() {
		return qtdEstq;
	}

	public void setQtdEstq(int qtdEstq) {
		this.qtdEstq = qtdEstq;
	}

	public String getNcm() {
		return ncm;
	}

	public void setNcm(String ncm) {
		this.ncm = ncm;
	}

	public String getUnidadeComercial() {
		return unidadeComercial;
	}

	public void setUnidadeComercial(String unidadeComercial) {
		this.unidadeComercial = unidadeComercial;
	}

	public String getCest() {
		return Cest;
	}

	public void setCest(String cest) {
		Cest = cest;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
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
		Produto other = (Produto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		// return descricao;
		return codigo + " - " + codigoBarras + " - " + descricao + " - " + precoUnitario;
	}

}
