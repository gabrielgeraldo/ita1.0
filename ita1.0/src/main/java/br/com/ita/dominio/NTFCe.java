package br.com.ita.dominio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nfce")
public class NTFCe implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "codigo_nfce", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "codigo_nfce", sequenceName = "codigo_nfce", allocationSize = 1)
	private Long codigo;

	@Column(length = 9, nullable = false)
	@NotNull(message = "Número não definido.")
	private Integer numero;

	@Column(length = 3, nullable = false)
	@NotNull(message = "Série não definida.")
	private Integer serie;

	@ManyToOne
	private Cliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date data = new Date();

	@NotNull(message = "O total é de preenchimento obrigatório.")
	@Column(precision = 7, scale = 2, nullable = false)
	private BigDecimal total;

	@Column(length = 44)
	private String chave;

	@Column(length = 15)
	private String numProtocolo;

	@Transient
	private String status;

	@Transient
	private String rejeicaoMotivo;

	@Transient
	private String xml;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRecebimento;

	@Column(length = 1)
	private String ambienteRecebimento;

	@ManyToOne
	private Orcamento orcamento;

	@ManyToOne
	@NotNull(message = "A condição de pagamento é de preenchimento obrigatório.")
	private CondicaoPagamento condicaoPagamento;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getSerie() {
		return serie;
	}

	public void setSerie(Integer serie) {
		this.serie = serie;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getNumProtocolo() {
		return numProtocolo;
	}

	public void setNumProtocolo(String numProtocolo) {
		this.numProtocolo = numProtocolo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejeicaoMotivo() {
		return rejeicaoMotivo;
	}

	public void setRejeicaoMotivo(String rejeicaoMotivo) {
		this.rejeicaoMotivo = rejeicaoMotivo;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
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

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public CondicaoPagamento getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
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
		NTFCe other = (NTFCe) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NTFCe [numero=" + numero + "]";
	}

}
