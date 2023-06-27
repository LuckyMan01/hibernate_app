package com.example.hibernate.service;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import com.example.hibernate.repository.BooksRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book book = findOne(id);
        Hibernate.initialize(book);
        if (book.getOwner() != null) {
            throw new RuntimeException(String.format("Book %s belong to other user %s",
                    id, book.getOwner().getId()));
        }
        book.setOwner(selectedPerson);
    }

    @Transactional
    public void release(int id) {
        Book book = findOne(id);
        Hibernate.initialize(book);
        book.setOwner(null);
    }

    public List<Book> pagination(int page, int itemsPerPage) {
        return booksRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = personService.fineOne(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
