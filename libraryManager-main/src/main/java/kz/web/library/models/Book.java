package kz.web.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty")
    private String title;

    @Column(name = "author")
    @NotEmpty(message = "Author should not be empty")
    private String author;

    @Column(name = "year")
    @Min(value = 0, message = "Invalid year input")
    private int year;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "taken_date")
    @Temporal(TemporalType.DATE)
    private Date takenDate;

    public Book(String title, String author, int year, Date takenDate) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.takenDate = takenDate;
    }

    public Book(String title, String author, int year, Date takenDate, Person owner) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.owner = owner;
        this.takenDate = takenDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(owner, book.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, year, owner);
    }

    public String getDescription() {
        return title + ", " + author + ", " + year;
    }

    public boolean isOwned() {
        return owner != null;
    }

    public boolean isOverdue() {
        return new Date().getTime() - takenDate.getTime() > 10 * 24 * 60 * 60 * 1000;
    }
}
