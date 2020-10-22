package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "item_venda")
public class ItemVenda implements BaseEntity, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_item_venda", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_item_venda", sequenceName = "codigo_item_venda", allocationSize = 1)
	private Long codigo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "venda_codigo")
	@NotNull
	private Venda venda;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "produto_codigo")
	@NotNull(message = "Selecione um produto.")
	private Produto produto;

	@Column(nullable = false)
	@NotNull
	private int quantidade;
	
	@Column(precision = 7, scale = 2, nullable = false)
	// @NotNull(message = "Preço de custo não definido.")
	private BigDecimal precoCusto;

	@Column(precision = 7, scale = 2, nullable = false)
	@NotNull
	@DecimalMin(value = "0.01", message = "The decimal value can not be less than 1.00 digit ")
	private BigDecimal precoVenda;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
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
		ItemVenda other = (ItemVenda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return produto.toString();
	}

}
