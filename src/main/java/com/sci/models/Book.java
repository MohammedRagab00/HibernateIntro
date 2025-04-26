package com.sci.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "authors")
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Book {

    @Id
    private Integer id;
    private String name;
    private Date published_at;

    @ManyToMany
    @JoinTable(
            name = "author_book_table",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    /*
     * Using a Set in Hibernate many-to-many relationships avoid duplicates,
     * simplify mapping by not storing list indices,
     * and ensure more efficient operations compared to a List
     */
    private Set<Author> authors = new HashSet<>();
    //* initializing is generally a good practice to avoid null pointer exception


    public void addAuthor(Author author) {
        getAuthors().add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        getAuthors().remove(author);
        author.getBooks().remove(this);
    }
}
