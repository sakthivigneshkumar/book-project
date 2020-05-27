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
package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.backend.model.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
//    @Query("select s from Shelf s where lower(s.name) like lower(concat('%', :shelfName, '%'))")
//    List<Book> getBooksInShelf(@Param("shelfName") String shelfName);

//    @Query("select s from Shelf s where s.name = :shelfName")
//    List<Shelf> getBooksInShelf(@Param("shelfName") String shelfName);

    @Query("SELECT s FROM Shelf s WHERE s.name = :shelfName")
    List<Shelf> getBooksInShelf(@Param("shelfName") Shelf.ShelfName shelfName);
}
