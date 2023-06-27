package com.example.hibernate.repository;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Book,Integer> {
    Page<Book> findAll(Pageable page);
    List<Book> findAll(Sort sortUseValue);
}
