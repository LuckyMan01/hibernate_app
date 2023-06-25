package com.example.hibernate.controler;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import com.example.hibernate.service.BookService;
import com.example.hibernate.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final PersonService personService;
    private final BookService bookService;

    public BooksController(PersonService personService, BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        Book bookOpt = bookService.findOne(id);
        Person bookOwner = bookService.findOwner(id);

        if (bookOpt != null) {
            model.addAttribute("book", bookOpt);
            if (bookOwner != null) {
                model.addAttribute("owner", bookOwner);
            } else {
                model.addAttribute("people", personService.fineAll());
            }
            return "books/show";
        }


        model.addAttribute("origin", "books");
        return "errors/404";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasGlobalErrors()) {
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

}
