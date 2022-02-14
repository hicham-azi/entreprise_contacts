package com.genesis.contacts.exceptions;


import com.genesis.contacts.domain.enums.ERROR_CODE;

public class ValidationException extends ContactsException {
    private static final long serialVersionUID = 1L;

    public ValidationException(String msg) {
        super(msg);
        this.errorCode = ERROR_CODE.BAD_REQUEST;
    }

    public ValidationException(ERROR_CODE errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ValidationException(ERROR_CODE errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public ValidationException(String message, Object... params) {
        super(ERROR_CODE.BAD_REQUEST, message, params);
    }

}
