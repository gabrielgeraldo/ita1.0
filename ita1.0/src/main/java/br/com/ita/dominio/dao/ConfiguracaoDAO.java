package br.com.ita.dominio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import br.com.ita.controle.util.JpaDAO;
import br.com.ita.dominio.config.Configuracao;

public class ConfiguracaoDAO extends JpaDAO<Configuracao> implements Serializable {

	private static final long serialVersionUID = 1L;

	public ConfiguracaoDAO() {
		super();
	}

	public ConfiguracaoDAO(EntityManager manager) {
		super(manager);
	}

}
