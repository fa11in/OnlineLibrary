package kz.web.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "people")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fullname")
    @NotEmpty(message = "Fullname should not be empty")
    private String fullname;

    @Column(name = "year")
    @Min(value = 1900, message = "Invalid year input")
    private int year;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(String fullname, int year) {
        this.fullname = fullname;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && year == person.year && Objects.equals(fullname, person.fullname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, year);
    }
}
