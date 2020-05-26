/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.RatingScale;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.ShelfRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Spring service that acts as the gateway to the {@code ShelfRepository} -- to use the {@code ShelfRepository},
 * a consumer should go via this {@code ShelfService}
 */
@Service
public class ShelfService extends BaseService<Shelf, Long> {
    private static final Logger LOGGER = Logger.getLogger(ShelfService.class.getSimpleName());

    private BookRepository bookRepository;
    private ShelfRepository shelfRepository;
    private AuthorRepository authorRepository;

    public ShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
                        ShelfRepository shelfRepository ) {
        this.bookRepository = bookRepository;
        this.shelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
    }

    @Override
    public Shelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    @Override
    public void save(Shelf shelf) {
        if (shelf != null) {
            shelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    public List<Book> getBooksInShelf(Shelf.ShelfName shelfName) {
        if (shelfName == null) {
            return new ArrayList<>();
        }
        return shelfRepository.getBooksInShelf(shelfName.toString());
    }

    @Override
    public void delete(Shelf shelf) {
        shelfRepository.delete(shelf);
    }

    @PostConstruct
    public void populateTestData() {
        if (authorRepository.count() == 0) {
          authorRepository.saveAll(
          Stream.of(
                  "J.K. Rowling",
                  "Neil Gaiman",
                  "J.R.R Tolkien",
                  "Roald Dahl",
                  "Robert Galbraith",
                  "Dan Brown")
              .map(
                  name -> {
                    String[] fullName = name.split(" ");


                    Author author = new Author();
                    author.setFirstName(fullName[0]);
                    author.setLastName(fullName[1]);
                    return author;
                  })
              .collect(Collectors.toList()));
        }

        System.out.println("Author count: " + authorRepository.count());

        if (bookRepository.count() == 0) {
            Random random = new Random(0);
            List<Author> authors = authorRepository.findAll();

            bookRepository.saveAll(
                Stream.of(
                        "Harry Potter and the Philosopher's stone",
                        "Harry Potter and the Chamber of Secrets",
                        "Harry Potter and the Prisoner of Azkaban",
                        "Harry Potter and the Goblet of Fire",
                        "Harry Potter and the Order of Phoenix",
                        "Harry Potter and the Half-Blood Prince",
                        "Harry Potter and the Deathly Hallows")
                    .map(title -> {
                        int min = 300;
                        int max = 1000;
                        int range = (max-min) + 1;
                        int pages = (int) (Math.random() * range);

                        Book book = new Book();
                        book.setTitle(title);

                        book.setAuthor(authors.get(0));

                        Genre genre = Genre.values()[random.nextInt(Genre.values().length)];

                        book.setGenre(genre);
                        book.setNumberOfPages(pages);

                        book.setDateStartedReading(LocalDate.now().minusDays(2));
                        book.setDateFinishedReading(LocalDate.now());

                        book.setRating(RatingScale.values()[random.nextInt(RatingScale.values().length)]);

                        return book;
                    }).collect(Collectors.toList()));
        }

        System.out.println("Book count: " + bookRepository.count());

        if (shelfRepository.count() == 0) {
            List<Book> books = bookRepository.findAll();
            shelfRepository.saveAll(
                    Stream.of(Shelf.ShelfName.values())
                        .map(name -> {
                            System.out.println("Shelf name in Shelf Service: " + name);
                            if (books.isEmpty()) {
                                System.out.println("empty books in ShelfService");
                            } else {
                                System.out.println("neither null nor empty in ShelfService");
                            }

                            Shelf shelf = new Shelf(name);
                            shelf.setBooks(books);
                            return shelf;
                    }).collect(Collectors.toList()));
        }

        System.out.println("Shelf count: " + bookRepository.count());
    }
}
