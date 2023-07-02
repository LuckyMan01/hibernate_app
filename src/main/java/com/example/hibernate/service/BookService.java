package com.example.hibernate.service;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import com.example.hibernate.repository.BooksRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BooksRepository booksRepository;
    private final PersonService personService;
    private final int MAX_DEY_RENT = 7;

    @Autowired
    public BookService(BooksRepository booksRepository, PersonService personService) {
        this.booksRepository = booksRepository;
        this.personService = personService;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(String sortByNameField) {
        return booksRepository.findAll(Sort.by(sortByNameField));
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
        book.setRentTime(limitMaxTimeForRent(MAX_DEY_RENT));
        book.setOwner(selectedPerson);
    }

    //        limit max time for rent
    private Date limitMaxTimeForRent(int day) {
        Date date = new Date();
        int setDate = date.getDate() + day;
        return new Date(date.getYear(), date.getMonth(), setDate, date.getHours(), date.getMinutes());
    }

    @Transactional
    public void release(int id) {
        Book book = findOne(id);
        Hibernate.initialize(book);
        book.setRentTime(null);
        book.setOwner(null);
    }

    public List<Book> pagination(int page, int itemsPerPage) {
        return booksRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public List<Book> pagination(int page, int itemsPerPage, String sort) {
        return booksRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by(sort))).getContent();
    }

// I wrote this code
    //    public List<Book> getBooksByPersonId(int id) {
//        Optional<Person> person = personService.fineOne(id);
//        if (person.isPresent()) {
//            List<Book> books = person.get().getBooks();
//            for (Book book : books) {
//                book.setTimeForRent(!new Date().after(book.getRendTime()));
//            }
//            return books;
//        } else {
//            return Collections.emptyList();
//        }
//    }

//    Vitalik made this code
    public List<Book> getBooksByPersonId(int id) {
        return personService.fineOne(id)
                .map(Person::getBooks)
                .orElse(Collections.emptyList())
                .stream()
                .peek(book -> book.setTimeForRent(!new Date().after(book.getRentTime())))
                .collect(Collectors.toList());
    }

    public List<Book> findByTitleStartWith(String title) {
        return booksRepository.findByTitleStartingWithIgnoreCase(title);
    }
}
