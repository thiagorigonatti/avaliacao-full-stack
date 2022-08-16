package me.thiagorigonatti.avaliacaofullstack.exception;

public class ValidationExceptionDetails extends ExceptionDetails{

    private final String fields;
    private final String fieldsMessage;

    public ValidationExceptionDetails(String fields, String fieldsMessage) {
        this.fields = fields;
        this.fieldsMessage = fieldsMessage;
    }

    public String getFields() {
        return fields;
    }

    public String getFieldsMessage() {
        return fieldsMessage;
    }
}
