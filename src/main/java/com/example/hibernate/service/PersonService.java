package com.example.hibernate.service;

import com.example.hibernate.model.Person;
import com.example.hibernate.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PeopleRepository peopleRepositories;

    @Autowired
    public PersonService(PeopleRepository peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    public List<Person> fineAll() {
        return peopleRepositories.findAll();
    }

    public Optional<Person> fineOne(int id) {
        return peopleRepositories.findById(id);
    }

    @Transactional
    public void save(Person person) {
        person.setCreatedAt(new Date());
        peopleRepositories.save(person);
    }

    @Transactional
    public void update(int id, Person updatePerson) {
        updatePerson.setId(id);
        peopleRepositories.save(updatePerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepositories.deleteById(id);
    }

    public Optional<Person> findByDateOfBirth(Date date) {
        return peopleRepositories.findByDateOfBirth(date);
    }

    public List<Person> findByEmail(String email) {
        return peopleRepositories.findByEmail(email);
    }

}