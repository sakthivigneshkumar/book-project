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
package com.karankumar.bookproject.ui.shelf;

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.ShelfService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * Contains a {@code BookForm} and a Grid containing a list of books in a given {@code Shelf}
 */

@Route("")
@PageTitle("Home | Book Project")
public class MainView extends VerticalLayout {

    private BookForm bookForm;
    private BookService bookService;

    private ShelfService shelfService;

    private Grid<Book> bookGrid = new Grid<>(Book.class);
    private final TextField filterByTitle;
    private final ComboBox<Shelf.ShelfName> whichShelf;
    private final List<Shelf> shelves;
    private Shelf.ShelfName chosenShelf;
    private String bookTitle; // the book to filter by (if specified)

    public MainView(BookService bookService, ShelfService shelfService) {
        this.bookService = bookService;
        this.shelfService = shelfService;

        shelves = shelfService.findAll();

        whichShelf = new ComboBox<>();
        configureChosenShelf();

        filterByTitle = new TextField();
        configureFilter();

        HorizontalLayout horizontalLayout = new HorizontalLayout(whichShelf, filterByTitle);

        configureBookGrid();
        add(horizontalLayout, bookGrid);

        bookForm = new BookForm(shelfService);
        add(bookForm);

        bookForm.addListener(BookForm.SaveEvent.class, this::saveBook);
        bookForm.addListener(BookForm.DeleteEvent.class, this::deleteBook);

        bookGrid
                .asSingleSelect()
                .addValueChangeListener(
                        event -> {
                            if (event == null) {
                                System.out.println("event is null");
                            } else if (event.getValue() == null) {
                                System.out.println("event value is null");
                            } else {
                                editBook(event.getValue());
                            }
                        });
    }

    private void configureChosenShelf() {
        whichShelf.setPlaceholder("Select shelf");
        whichShelf.setItems(Shelf.ShelfName.values());
        whichShelf.setRequired(true);
        whichShelf.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                System.out.println("No choice selected");
            } else {
                chosenShelf = event.getValue();
                updateList();
            }
        });
    }

    private void configureBookGrid() {
        addClassName("book-grid");
        bookGrid.setColumns("title", "author", "genre", "dateStartedReading", "dateFinishedReading", "rating",
                "numberOfPages");
    }

    private void updateList() {
        if (chosenShelf == null) {
            return;
        }

        // Find the shelf that matches the chosen shelf's name
//        Shelf selectedShelf = null;
//        for (Shelf shelf : shelves) {
//            if (shelf.getName().equals(chosenShelf)) {
//                selectedShelf = shelf;
//                break;
//            }
//        }

        // this method call should return one shelf
        List<Shelf> shelves = shelfService.getBooksInShelf(chosenShelf);
        if (shelves.size() == 0) {
            System.out.println("MainView: shelf size = 0");
            return;
        }
        Shelf shelf = shelves.get(0);

        if (shelf != null) {
            System.out.println("\nMainView: shelf not null");
            bookGrid.setItems(shelf.getBooks());

            if (shelf.getBooks() == null) {
                System.out.println("MainView: null book");
            } else if (shelf.getBooks().isEmpty()) {
                System.out.println("MainView: empty books");
            }

        } else {
            System.out.println("MainView: shelf null");
        }

//        bookGrid.setItems(shelfService.getBooksInShelf(chosenShelf));

        // only set the grid if the book shelf name was matched
//        if (selectedShelf != null) {
//            if (bookTitle != null && !bookTitle.isEmpty()) {
//                bookGrid.setItems(bookService.findAll(bookTitle));
//            } else {
//                bookGrid.setItems(shelfService.getBooksInShelf(chosenShelf));

//                bookGrid.setItems(bookService.f);

//                bookGrid.setItems(selectedShelf.getBooks());

//                System.out.println("\nretrieving books");
//                List<Book> books = selectedShelf.getBooks();
//
//                if (books != null && !books.isEmpty()) {
//                    System.out.println("All good");
//                } else if (books == null) {
//                    System.out.println("null books");
//                } else if (books.isEmpty()) {
//                    System.out.println("empty books");
//                    System.out.println("Chosen shelf: " + chosenShelf);
//                    System.out.println("Selected shelf: " + selectedShelf.getName());
//                }
//            }
//        }
    }

    private void configureFilter() {
        filterByTitle.setPlaceholder("Filter by book title");
        filterByTitle.setClearButtonVisible(true);
        filterByTitle.setValueChangeMode(ValueChangeMode.LAZY);
        filterByTitle.addValueChangeListener(event -> {
            if (event.getValue() != null && !event.getValue().isEmpty()) {
                bookTitle = event.getValue();
            }
            updateList();
        });
    }

    private void editBook(Book book) {
        if (book != null && bookForm != null) {
            bookForm.setBook(book);
        }
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        bookService.delete(event.getBook());
        updateList();
    }

    private void saveBook(BookForm.SaveEvent event) {
        bookService.save(event.getBook());
        updateList();
    }

}
