package br.com.ita.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "condicaopagamento")
public class CondicaoPagamento implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_condicao", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_condicao", sequenceName = "codigo_condicao", allocationSize = 1)
	private Long codigo;

	@Column(length = 30, nullable = false)
	@NotNull(message = "A descrição é de preenchimento obrigatório.")
	@Size(min = 2, max = 30, message = "A descrição deve ter entre 2 e 30 caracteres.")
	private String descricao;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "A forma de pagamento é de preenchimento obrigatório.")
	private FormaPagamento formaPagamento;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "O meio de pagamento é de preenchimento obrigatório.")
	private MeioPagamento meioPagamento;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public MeioPagamento getMeioPagamento() {
		return meioPagamento;
	}

	public void setMeioPagamento(MeioPagamento meioPagamento) {
		this.meioPagamento = meioPagamento;
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
		CondicaoPagamento other = (CondicaoPagamento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.descricao + " - " + formaPagamento + " - " + meioPagamento.getDescricao().toUpperCase();
	}

}
