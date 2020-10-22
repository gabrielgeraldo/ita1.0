package br.com.ita.controle.conversores;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;
import javax.management.relation.Role;

@FacesConverter("roleConverter")
public class RoleConverter extends EnumConverter {
    public RoleConverter() {
        super(Role.class);
    }
}