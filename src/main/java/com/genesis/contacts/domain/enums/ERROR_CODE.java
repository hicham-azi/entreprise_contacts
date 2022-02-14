package com.genesis.contacts.domain.enums;

public enum ERROR_CODE {
    DUPLICATE_VALUE("Existe déja"),
    CONTACT_USED("Le contact lié a une entreprise"),
    INEXISTANTE_ENTREPRISE("Entreprise inexistante"),
    TVA_CONTACT_MONDATORY("Tva doit être non null"),BACKEND_TECHNICAL_EXCEPTION("Une erreur technique est survenue"),
    INEXISTANT_CONTACT("Contact inexistant") ,TECHNICAL_EXCEPTION("Une erreur technique est survenue"),
    BAD_REQUEST("BAD_REQUEST"), INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"), OBJ_NOT_FOUND("OBJ_NOT_FOUND");

    private String value;

    ERROR_CODE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
