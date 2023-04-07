package kz.web.library.repositories;

import kz.web.library.models.Book;
import kz.web.library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByOwner(Person owner);
    Optional<Book> findFirstByOwner(Person owner);
    @Modifying
    @Query("update Book b SET b.owner = null WHERE b.owner.id = :id")
    void updateBookSetOwnerNull(@Param("id") int id);

    List<Book> findAllByTitleStartingWith(String title);
    List<Book> findAllByOrderByYear();
}
