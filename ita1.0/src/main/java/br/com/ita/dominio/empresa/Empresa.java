package br.com.ita.dominio.empresa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.ita.dominio.BaseEntity;

@Entity
@Table(name = "empresa")
public class Empresa implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codigo_empresa")
	@SequenceGenerator(sequenceName = "codigo_empresa", name = "codigo_empresa", allocationSize = 1)
	private Long codigo;

	@Column(length = 18, nullable = false)
	@NotNull(message = "O CNPJ/CPF é de preenchimento obrigatório.")
	private String cnpj;

	@Column(length = 14)
	@Size(min = 0, max = 14, message = "A IE deve ter entre 0 e 14 caracteres.")
	private String inscricaoEstadual;

	@Column(length = 70, nullable = false)
	@NotNull(message = "A razão social é de preenchimento obrigatório.")
	@Size(min = 4, max = 70, message = "A razão social deve ter entre 4 e 70 caracteres.")
	private String razaoSocial;

	@Column(length = 70, nullable = false)
	@NotNull(message = "O nome fantasia é de preenchimento obrigatório.")
	@Size(min = 4, max = 70, message = "O nome fantasia deve ter entre 4 e 70 caracteres.")
	private String nomeFantasia;

	@Column(length = 15, nullable = false)
	@NotNull(message = "O código do cliente é de preenchimento obrigatório.")
	private String codClient;

	@Column(length = 50, nullable = false)
	@NotNull(message = "O endereço é de preenchimento obrigatório.")
	@Size(min = 4, max = 50, message = "O endereço deve ter entre 4 e 50 caracteres.")
	private String endereco;

	@Column(length = 15, nullable = false)
	@NotNull(message = "O número é de preenchimento obrigatório.")
	@Size(min = 1, max = 15, message = "O número deve ter entre 1 e 15 digitos.")
	private String numero;

	@Column(length = 65, nullable = false)
	@NotNull(message = "O complemento é de preenchimento obrigatório.")
	@Size(min = 0, max = 65, message = "O campo complemento deve ter no máximo 65 caracteres.")
	private String complemento;

	@Column(length = 35, nullable = false)
	@NotNull(message = "O bairro é de preenchimento obrigatório.")
	@Size(min = 4, max = 35, message = "O bairro deve ter entre 4 e 35 caracteres.")
	private String bairro;

	@Column(length = 7, nullable = false)
	@NotNull(message = "O código do município é de preenchimento obrigatório.")
	private String codigoMunicipio;

	@Column(length = 80, nullable = false)
	@NotNull(message = "O descrição município é de preenchimento obrigatório.")
	private String descricaoMunicipio;

	@Column(length = 2, nullable = false)
	@NotNull(message = "A UF é de preenchimento obrigatório.")
	private String uf;

	@Column(length = 25, nullable = false)
	@NotNull(message = "O CEP é de preenchimento obrigatório.")
	private String cep;

	@Column(length = 7, nullable = false)
	@NotNull(message = "O código do pais é de preenchimento obrigatório.")
	private String codigoPais;

	@Column(length = 80, nullable = false)
	@NotNull(message = "O município é de preenchimento obrigatório.")
	private String descricaoPais;

	@Column(length = 15, nullable = false)
	@NotNull(message = "O telefone é de preenchimento obrigatório.")
	@Size(min = 4, max = 15, message = "O telefone deve ter entre 4 e 15 digitos.")
	private String telefone;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
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

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public String getDescricaoMunicipio() {
		return descricaoMunicipio;
	}

	public void setDescricaoMunicipio(String descricaoMunicipio) {
		this.descricaoMunicipio = descricaoMunicipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getDescricaoPais() {
		return descricaoPais;
	}

	public void setDescricaoPais(String descricaoPais) {
		this.descricaoPais = descricaoPais;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
