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

package com.karankumar.bookproject.backend.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a shelf (or a list) of books (e.g. books in a 'to read' shelf)
 */

@Entity
public class Shelf extends BaseEntity {
    @NotNull
    @Enumerated(EnumType.STRING)
    private ShelfName name;

//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<Book> books;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelf")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelf")
    private List<Book> books;

    public enum ShelfName {
        TO_READ("To read"),
        READING("Reading"),
        READ("Read");

        private String shelfName;

        ShelfName (String shelfName) {
            this.shelfName = shelfName;
        }

        @Override
        public String toString() {
            return shelfName;
        }
    }

    public Shelf() {
    }

    public Shelf(ShelfName name) {
        this.name = name;
    }

    public ShelfName getName()  {
        return name;
    }

    public void setName(ShelfName name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        if (books.isEmpty()) {
            System.out.println("Shelf: books are empty");
        } else {
            System.out.println("Books are not empty");
        }
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
