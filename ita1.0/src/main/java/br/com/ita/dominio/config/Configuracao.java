package br.com.ita.dominio.config;

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
@Table(name = "configuracao")
public class Configuracao implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "codigo_configuracao")
	@SequenceGenerator(sequenceName = "codigo_configuracao", name = "codigo_configuracao", allocationSize = 1)
	private Long codigo;

	@Column()
	private String senhaCer;

	@Column()
	private String idCodigoSegurancaContribuinte;

	@Column()
	private String codigoSegurancaContribuinte;

	@Column(length = 1, nullable = false)
	@NotNull(message = "O ambiente é de preenchimento obrigatório.")
	@Size(min = 1, max = 1, message = "O ambiente deve somente 1 caractere.")
	private String ambiente;

	@Column(nullable = false)
	@NotNull(message = "A senha do mês é de preenchimento obrigatório.")
	private String senhaMes;

	@Column(length = 1, nullable = false, columnDefinition = "VARCHAR(1) default '1'")
	@NotNull(message = "A série é de preenchimento obrigatório.")
	@Size(min = 1, max = 1, message = "A série deve somente 1 caractere.")
	private String serieNfce;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSenhaCer() {
		return senhaCer;
	}

	public void setSenhaCer(String senhaCer) {
		this.senhaCer = senhaCer;
	}

	public String getIdCodigoSegurancaContribuinte() {
		return idCodigoSegurancaContribuinte;
	}

	public void setIdCodigoSegurancaContribuinte(String idCodigoSegurancaContribuinte) {
		this.idCodigoSegurancaContribuinte = idCodigoSegurancaContribuinte;
	}

	public String getCodigoSegurancaContribuinte() {
		return codigoSegurancaContribuinte;
	}

	public void setCodigoSegurancaContribuinte(String codigoSegurancaContribuinte) {
		this.codigoSegurancaContribuinte = codigoSegurancaContribuinte;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	public String getSenhaMes() {
		return senhaMes;
	}

	public void setSenhaMes(String senhaMes) {
		this.senhaMes = senhaMes;
	}

	public String getSerieNfce() {
		return serieNfce;
	}

	public void setSerieNfce(String serieNfce) {
		this.serieNfce = serieNfce;
	}

}
