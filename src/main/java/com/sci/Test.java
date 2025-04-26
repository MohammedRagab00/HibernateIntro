package com.sci;

import com.sci.dao.DBAuthor;
import com.sci.dao.DBBook;
import com.sci.models.Author;
import com.sci.models.Book;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        DBAuthor dbAuthor = new DBAuthor();
        DBBook dbBook = new DBBook();

        Author author = new Author();
        author.setId(1);
        author.setName("Hafez");

        Book book = new Book();
        book.setId(1);
        book.setPublished_at(new Date());
        book.setName("The Art of Computer Programming");


        //* we insert some records
//        dbAuthor.insert(author);
//        dbBook.insert(book);

        //* added an author for some book
//        book.addAuthor(author);
//        dbBook.update(book);

        //* retrieved author
//        System.out.println(dbAuthor.getById(1));
    }
}
