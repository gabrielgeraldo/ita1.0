package br.com.ita.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "usuario")
public class Usuario implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_usuario", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_usuario", sequenceName = "codigo_usuario", allocationSize = 1)
	private Long codigo;

	@Column(length = 40, nullable = false)
	@NotNull(message = "O usuário é de preenchimento obrigatório.")
	@Size(min = 4, max = 40, message = "O usuário deve ter entre 4 e 40 caracteres.")
	private String usuario;

	@Column(length = 20, nullable = false)
	@NotNull(message = "A senha é de preenchimento obrigatório.")
	@Size(min = 4, max = 20, message = "A senha deve ter entre 4 e 20 caracteres.")
	private String senha;

	@ManyToMany
	@NotEmpty(message = "Papel não definido.")
	@JoinTable(name = "papel_usuario")
	private List<Papel> papeis = new ArrayList<>();

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Papel> getPapeis() {
		return papeis;
	}

	public void setPapeis(List<Papel> papeis) {
		this.papeis = papeis;
	}

	@Override
	public int hashCode() {
		if (this.codigo == null)
			return 0;

		return this.codigo.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.usuario;
	}

}
