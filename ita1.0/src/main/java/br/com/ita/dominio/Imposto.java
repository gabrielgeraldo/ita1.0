package br.com.ita.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fincatto.documentofiscal.nfe400.classes.NFRegimeTributario;

@Entity
@Table(name = "imposto")
public class Imposto implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_imposto", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_imposto", sequenceName = "codigo_imposto", allocationSize = 1)
	private Long codigo;

	@Column(length = 1, nullable = false)
	@NotNull(message = "O regimeTributario é de preenchimento obrigatório.")
	private NFRegimeTributario regimeTributario;

	@Column(length = 3, nullable = false)
	@NotNull(message = "O ICMS é de preenchimento obrigatório.")
	private String icms;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public NFRegimeTributario getRegimeTributario() {
		return regimeTributario;
	}

	public void setRegimeTributario(NFRegimeTributario regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public String getIcms() {
		return icms;
	}

	public void setIcms(String icms) {
		this.icms = icms;
	}

}
