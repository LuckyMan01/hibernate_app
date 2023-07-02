package com.example.hibernate.controler;

import com.example.hibernate.model.Person;
import com.example.hibernate.service.BookService;
import com.example.hibernate.service.PersonService;
import com.example.hibernate.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;
    private final PersonValidator personValidator;
    private final BookService bookService;

    @Autowired
    public PeopleController(PersonService personService, PersonValidator personValidator, BookService bookService) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.bookService = bookService;
    }

    @GetMapping()
    public String index(Model model) {

        model.addAttribute("people", personService.fineAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Optional<Person> personOpt = personService.fineOne(id);
        if (personOpt.isPresent()) {
            model.addAttribute("person", personOpt.get());
            model.addAttribute("books", bookService.getBooksByPersonId(id));
            return "people/show";
        }
        model.addAttribute("origin", "people");
        return "errors/404";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";

        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.fineOne(id).get());
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";

        personService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/people";
    }
}