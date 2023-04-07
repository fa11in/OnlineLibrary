package kz.web.library.controllers;

import jakarta.validation.Valid;
import kz.web.library.models.Book;
import kz.web.library.models.Person;
import kz.web.library.services.BookService;
import kz.web.library.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private PeopleService peopleService;
    private BookService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    // get all
    @GetMapping()
    public String index(Model model, @RequestParam(required = false, name = "page") Integer page,
                        @RequestParam(required = false, name = "books_per_page") Integer booksPerPage,
                        @RequestParam(required = false, name = "sort_by_year") Boolean sortByYear) {

        List<Book> books = bookService.getAll(page, booksPerPage, sortByYear);

        model.addAttribute("books", books);
        return "books/index";
    }

    // get one
    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model) {
        Optional<Book> bookOptional = bookService.get(id);
        if (bookOptional.isEmpty()) {
            return "redirect:/404";
        }
        Book book = bookOptional.get();
        model.addAttribute("book", book);
        if (book.getOwner() != null) {
            model.addAttribute("person", book.getOwner());
        } else {
            model.addAttribute("people", peopleService.getAll());
            model.addAttribute("person", new Person());
        }
        model.addAttribute("isOwned", book.getOwner() != null);
        return "books/show";
    }

    // add
    @GetMapping("/new")
    public String addNew(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String postNew(@ModelAttribute("book") @Valid Book book, BindingResult result) {
        if (result.hasErrors()) return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    // edit
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        Optional<Book> bookOptional = bookService.get(id);
        if (bookOptional.isEmpty()) {
            return "redirect:/404";
        }
        model.addAttribute("book", bookOptional.get());
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult result) {
        if (result.hasErrors()) return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person owner) {
        bookService.assign(id, owner);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/return")
    public String returnBook(@PathVariable("id") int id) {
        bookService.returnBook(id);
        return "redirect:/books";
    }

    // delete
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    // search
    @GetMapping("/search")
    public String search(Model model, @RequestParam(required = false) String title) {
        if (title == null || title.isBlank()) {
            model.addAttribute("isFound", false);
        } else {
            List<Book> foundBooks = bookService.findByTitle(title);
            model.addAttribute("isFound", !foundBooks.isEmpty());
            model.addAttribute("books", foundBooks);
        }
        return "books/search";
    }

}
