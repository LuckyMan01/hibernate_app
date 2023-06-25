package com.example.hibernate.service;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import com.example.hibernate.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BooksRepository booksRepository;
    private final PersonService personService;

    @Autowired
    public BookService(BooksRepository booksRepository, PersonService personService) {
        this.booksRepository = booksRepository;
        this.personService = personService;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Person findOwner(int id) {
        return personService.fineOne(id);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        booksRepository.findById(id).get().setOwner(selectedPerson);
    }

    @Transactional
    public void release(int id) {
        booksRepository.findById(id).get().setOwner(null);
    }
}
