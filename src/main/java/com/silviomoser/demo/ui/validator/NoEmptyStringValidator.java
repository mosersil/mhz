package com.silviomoser.demo.ui.validator;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;

public class NoEmptyStringValidator extends AbstractValidator<String> {

    /**
     * Constructs a validator with the given error message. The substring "{0}"
     * is replaced by the value that failed validation.
     *
     * @param errorMessage the message to be included in a failed result, not null
     */
    public NoEmptyStringValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        return toResult(value, isValid(value));
    }

    public boolean isValid(String value) {
        //we accept null as valid
        if (value==null) {
            return true;
        }
        //fail if it starts or ends with space
        if (value.startsWith(" ") || value.endsWith(" ")) {
            return false;
        }
        //otherwise its ok
        return true;
    }
}
