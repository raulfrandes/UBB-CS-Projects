package com.example.lab_8.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
