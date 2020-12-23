package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orcamento")
public class Orcamento implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_orcamento", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_orcamento", sequenceName = "codigo_orcamento", allocationSize = 1)
	private Long codigo;

	@ManyToOne
	private Cliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull(message = "A data é de preenchimento obrigatório.")
	private Date data = new Date();

	@NotNull(message = "O total é de preenchimento obrigatério.")
	@DecimalMin(value = "0.01", message = "O total deverá ser superior a 0.00")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal total;

	@ManyToOne
	@NotNull(message = "O usuário é de preenchimento obrigatório.")
	private Usuario usuario;

	@Transient
	private List<ItemOrcamento> itens = new ArrayList<>();

	public Long getCodigo() {
		return codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Date getData() {
		return data;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<ItemOrcamento> getItens() {
		return itens;
	}

	public void setItens(List<ItemOrcamento> itens) {
		this.itens = itens;
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
		Orcamento other = (Orcamento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {

		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formatador.format(data);

		final String cli;

		if (cliente != null)
			cli = cliente.toString();
		else
			cli = "Cliente não identificado";

		return codigo + " - " + cli + " - " + dataFormatada + " - " + total;

	}

}
