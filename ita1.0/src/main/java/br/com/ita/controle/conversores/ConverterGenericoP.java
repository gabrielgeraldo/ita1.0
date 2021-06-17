package br.com.ita.controle.conversores;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.ita.dominio.BaseEntityP;

/**
 * Converter simples para ser usado nos selectOneMenu do jsf
 * 
 * Fonte: http://www.rponte.com.br/2008/07/26/entity-converters-pra-da-e-vender/
 * 
 * @author Levy Moreira
 *
 */
@FacesConverter(value = "converter-genericop")
public class ConverterGenericoP implements Converter {

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		//System.out.println("getAsObject = " + value);
		if (value != null) {
			return this.getAttributesFrom(component).get(value);
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		// System.out.println("getAsString = " + value);
		if (value != null && !"".equals(value)) {
			// System.out.println("entrou");

			BaseEntityP entity = (BaseEntityP) value;  

			// adiciona item como atributo do componente
			this.addAttribute(component, entity);

			String codigo = entity.getCodigo();
			if (codigo != null) {
				return String.valueOf(codigo);
			}
		}

		return (String) value;
	}

	protected void addAttribute(UIComponent component, BaseEntityP o) {
		//System.out.println("addAttribute = " + o.getCodigo());
		String key = o.getCodigo().toString(); // codigo como chave neste caso
		this.getAttributesFrom(component).put(key, o);
	}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

}