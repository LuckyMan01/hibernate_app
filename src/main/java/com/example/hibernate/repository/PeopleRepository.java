package com.example.hibernate.repository;

import com.example.hibernate.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

    List<Person> findByFullName(String name);

    //    sorted age for person
    List<Person> findByFullNameOrderByAge(String name);

    List<Person> findByEmail(String email);

    List<Person> findByAge(int age);

    List<Person> findByFullNameStartingWith(String nameStartWith);

    Optional<Person> findByDateOfBirth(Date dateOfBirth);
}
