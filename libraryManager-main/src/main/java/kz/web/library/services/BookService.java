package kz.web.library.services;

import kz.web.library.models.Book;
import kz.web.library.models.Person;
import kz.web.library.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public List<Book> getAll(Integer page, Integer booksPerPage, Boolean sortByYear) {
        List<Book> books = getAll();
        if (page != null && booksPerPage != null) {
            books = books.subList((page - 1) * booksPerPage, (page - 1) * booksPerPage + booksPerPage);
        }
        if (sortByYear != null && sortByYear) {
            //sortByYear(books);
            books = bookRepository.findAllByOrderByYear();
        }
        return books;
    }

    private void sortByYear(List<Book> books) {
        books.sort(Comparator.comparingInt(Book::getYear));
    }

    public Optional<Book> get(int id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public boolean hasBooks(Person owner) {
        return bookRepository.findFirstByOwner(owner).isPresent();
    }

    public List<Book> getPersonBooks(Person owner) {
        return bookRepository.findAllByOwner(owner);
    }

    @Transactional
    public boolean assign(int id, Person newOwner) {
        Optional<Book> bookOptional = get(id);
        if (bookOptional.isEmpty()) return false;
        Book book = bookOptional.get();
        book.setTakenDate(new Date());
        book.setOwner(newOwner);
        bookRepository.save(book);
        return true;
    }

    @Transactional
    public boolean returnBook(int id) {
        Optional<Book> bookOptional = get(id);
        if (bookOptional.isEmpty()) return false;
        Book book = bookOptional.get();
        book.setOwner(null);
        bookRepository.save(book);
        return true;
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findAllByTitleStartingWith(title);
    }


}
