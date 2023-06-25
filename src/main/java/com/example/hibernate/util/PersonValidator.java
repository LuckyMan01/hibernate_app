package com.example.hibernate.util;


import com.example.hibernate.model.Person;
import com.example.hibernate.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        if (errors.hasErrors()) {
            errors.rejectValue("dateOfBirth", "", "This dateOFBirth it is wrong! Correct enter 00/00/0000");
        }
        if (personService.findByEmail(person.getEmail()).size() != 0 ){
            errors.rejectValue("email", "", "This email has gat other people! enter another email");
        }
    }
}
