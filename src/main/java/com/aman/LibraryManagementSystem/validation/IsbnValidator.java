package com.aman.LibraryManagementSystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidIsbn,String> {

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context){
        if(isbn == null || isbn.isBlank()){
            return true;
        }
        String cleaned = isbn.replace("-","")
                .replace(" ","");

        return cleaned.matches("\\d{10}|\\d{13}");
    }
}
