package br.com.ita.controle.validadores;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="email-validator")
public class EmailValidator implements Validator {

	@Override
	public void validate(FacesContext contexto, UIComponent campo, Object valor)
			throws ValidatorException {
		
		String valorS = (String) valor;
		if (valorS.indexOf("@") <= 0)
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "O e-mail é inválido", null));
		

	}

}
