package br.com.ita.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fincatto.documentofiscal.nfe400.classes.nota.NFIndicadorIEDestinatario;

//import com.fincatto.nfe310.classes.nota.NFIndicadorIEDestinatario;

@Entity
@Table(name = "cliente")
public class Cliente implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codigo_cliente")
	@SequenceGenerator(sequenceName = "codigo_cliente", name = "codigo_cliente", allocationSize = 1)
	private Long codigo;

	@Column(length = 1, nullable = false, columnDefinition = "VARCHAR(1) default 'F'")
	@NotNull(message = "O tipo � de preenchimento obrigat�rio.")
	private String tipo;

	@Column(length = 18, nullable = false)
	@NotNull(message = "O CNPJ/CPF � de preenchimento obrigat�rio.")
	@Size(min = 4, max = 18, message = "O CNPJ/CPF deve ter entre 4 e 18 caracteres.")
	private String cgc;

	@Column(length = 14)
	@Size(min = 0, max = 14, message = "A IE deve ter entre 0 e 14 caracteres.")
	private String inscEst;

	@Column(length = 70, nullable = false)
	@NotNull(message = "A raz�o social � de preenchimento obrigat�rio.")
	@Size(min = 4, max = 70, message = "A raz�o social deve ter entre 4 e 70 caracteres.")
	private String nome;

	@Column(length = 70, nullable = false)
	@NotNull(message = "O nome fantasia � de preenchimento obrigat�rio.")
	@Size(min = 4, max = 70, message = "O nome fantasia deve ter entre 4 e 70 caracteres.")
	private String nomeFantasia;

	@Column(length = 15, nullable = false)
	@NotNull(message = "O telefone � de preenchimento obrigat�rio.")
	@Size(min = 4, max = 15, message = "O telefone deve ter entre 4 e 15 digitos.")
	private String telefone;

	@Column(length = 15)
	private String celular;

	@Column(length = 40, nullable = false)
	@NotNull(message = "O e-mail � de preenchimento obrigat�rio.")
	@Size(min = 0, max = 40, message = "O e-mail deve ter no m�ximo 40 caracteres.")
	@Email(message = "Digite um e-mail v�lido.")
	private String email;

	@Column(length = 40)
	@Size(min = 0, max = 40, message = "O compo obs. deve ter no m�ximo 40 caracteres.")
	private String obs;

	@Column(length = 35, nullable = false)
	@NotNull(message = "O endere�o � de preenchimento obrigat�rio.")
	@Size(min = 4, max = 35, message = "O endere�o deve ter entre 4 e 35 caracteres.")
	private String endereco;

	@Column(length = 15, nullable = false)
	@NotNull(message = "O n�mero � de preenchimento obrigat�rio.")
	@Size(min = 1, max = 15, message = "O n�mero deve ter entre 1 e 15 digitos.")
	private String numero;

	@ManyToOne
	@NotNull(message = "O estado � de preenchimento obrigat�rio.")
	private Estado estado;

	@ManyToOne
	@NotNull(message = "O munic�pio � de preenchimento obrigat�rio.")
	private Municipio municipio;

	@Column(length = 35, nullable = false)
	@NotNull(message = "O bairro � de preenchimento obrigat�rio.")
	@Size(min = 4, max = 35, message = "O bairro deve ter entre 4 e 35 caracteres.")
	private String bairro;

	@Column(length = 25, nullable = false)
	@NotNull(message = "O CEP � de preenchimento obrigat�rio.")
	private String cep;

	@Column(length = 65, nullable = false)
	@NotNull(message = "O complemento � de preenchimento obrigat�rio.")
	@Size(min = 0, max = 65, message = "O campo complemento deve ter no m�ximo 65 caracteres.")
	private String complemento;

	@Column(length = 1, nullable = false)
	@NotNull(message = "O indicador IE destinatario � de preenchimento obrigat�rio.")
	private NFIndicadorIEDestinatario nfIndicadorIEDestinatario;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCgc() {
		return cgc;
	}

	public void setCgc(String cgc) {
		this.cgc = cgc.replaceAll("\\.", "").replaceAll("\\/", "").replace("-", "").replace("(", "").replace(")", "");
	}

	public String getInscEst() {
		return inscEst;
	}

	public void setInscEst(String inscEst) {
		this.inscEst = inscEst;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone.replaceAll("\\.", "").replaceAll("\\/", "").replace("-", "").replace("(", "")
				.replace(")", "");
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular.replaceAll("\\.", "").replaceAll("\\/", "").replace("-", "").replace("(", "")
				.replace(")", "");
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep.replaceAll("\\.", "").replaceAll("\\/", "").replace("-", "").replace("(", "").replace(")", "");
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public NFIndicadorIEDestinatario getNfIndicadorIEDestinatario() {
		return nfIndicadorIEDestinatario;
	}

	public void setNfIndicadorIEDestinatario(NFIndicadorIEDestinatario nfIndicadorIEDestinatario) {
		this.nfIndicadorIEDestinatario = nfIndicadorIEDestinatario;
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
		Cliente other = (Cliente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

}
