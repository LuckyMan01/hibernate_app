package com.example.hibernate.repository;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    List<Person> findByOwner(Person person);
}
