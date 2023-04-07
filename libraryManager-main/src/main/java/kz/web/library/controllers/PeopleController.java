package kz.web.library.controllers;

import jakarta.validation.Valid;
import kz.web.library.models.Person;
import kz.web.library.services.BookService;
import kz.web.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private PeopleService peopleService;
    private BookService bookService;

    @Autowired
    public PeopleController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    // get all
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.getAll());
        return "people/index";
    }

    // get one
    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        Optional<Person> personOptional = peopleService.get(id);
        if (personOptional.isEmpty()) {
            return "redirect:/404";
        }
        Person person = personOptional.get();
        model.addAttribute("person", person);
        model.addAttribute("hasBooks", bookService.hasBooks(person));
        model.addAttribute("books", bookService.getPersonBooks(person));
        return "people/show";
    }

    // add new
    @GetMapping("/new")
    public String addNew(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String postNew(@ModelAttribute("person") @Valid Person person, BindingResult result) {
        if (result.hasErrors()) return "people/new";
        peopleService.save(person);
        return "redirect:/people";
    }

    // edit
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Person> personOptional = peopleService.get(id);
        if (personOptional.isEmpty()) {
            return "redirect:/404";
        }
        Person person = personOptional.get();
        model.addAttribute("person", person);
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("person") @Valid Person person,
                         BindingResult result) {
        if (result.hasErrors()) return "people/edit";
        peopleService.update(id, person);
        return "redirect:/people";
    }

    // delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/people";
    }
}
