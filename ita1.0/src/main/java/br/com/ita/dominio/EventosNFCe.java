package br.com.ita.dominio;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "eventosnfce")
public class EventosNFCe implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_eventosnfce", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_eventosnfce", sequenceName = "codigo_eventosnfce", allocationSize = 1)
	private Long codigo;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "nfce_codigo")
	@NotNull(message = "NFC-e não definida.")
	private NTFCe nfce;

	@Column(length = 15)
	private String numProtocolo;

	@Column(length = 15)
	private String tipoEvento;

	@Column(length = 15)
	private String numeroSequencialEvento;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecebimento;

	@Column(length = 1)
	private String ambienteRecebimento;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public NTFCe getNfce() {
		return nfce;
	}

	public void setNfce(NTFCe nfce) {
		this.nfce = nfce;
	}

	public String getNumProtocolo() {
		return numProtocolo;
	}

	public void setNumProtocolo(String numProtocolo) {
		this.numProtocolo = numProtocolo;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getNumeroSequencialEvento() {
		return numeroSequencialEvento;
	}

	public void setNumeroSequencialEvento(String numeroSequencialEvento) {
		this.numeroSequencialEvento = numeroSequencialEvento;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getAmbienteRecebimento() {
		return ambienteRecebimento;
	}

	public void setAmbienteRecebimento(String ambienteRecebimento) {
		this.ambienteRecebimento = ambienteRecebimento;
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
		EventosNFCe other = (EventosNFCe) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EventosNFCe [codigo=" + codigo + "]";
	}

}
