package com.example.hibernate.controler;

import com.example.hibernate.model.Book;
import com.example.hibernate.model.Person;
import com.example.hibernate.service.BookService;
import com.example.hibernate.service.PersonService;
import com.example.hibernate.util.Pagination;
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

    //            http request for pagination pages http://localhost:8080/books?page=0&size=1
//            if you want to use sort by "year" or other name field, you must make this request /n
//            http://localhost:8080/books?page=0&size=8&sort=year
    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "size", required = false) Integer size,
                        @RequestParam(value = "sort", required = false) String sort) {
        if (page == null) {
            model.addAttribute("books", sort == null ? bookService.findAll() : bookService.findAll(sort));
        } else {
            model.addAttribute("books_pagination",
                            sort == null ? bookService.pagination(page, size) : bookService.pagination(page, size, sort))
                    .addAttribute("pagination", new Pagination(0));
        }
        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {

        Book bookOpt = bookService.findOne(id);
        Person bookOwner = bookOpt.getOwner();

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

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "search", required = false) String search) {
        if (search != null) {
            model.addAttribute("search_books", bookService.findByTitleStartWith(search));
        }

        return "books/search";
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


    @PatchMapping("{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.assign(id, selectedPerson);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }


}
