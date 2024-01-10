package com.example.socialweb.errors;

import org.springframework.validation.FieldError;

public class AttributeError extends FieldError {
    public AttributeError(String objectName, String field, String defaultMessage) {
        super(objectName, field, defaultMessage);
    }
}
