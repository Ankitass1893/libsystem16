package com.lib.controller;

import com.lib.model.Book;
import com.lib.model.Borrower;
import com.lib.repository.BookRepository;
import com.lib.repository.BorrowerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public LibraryController(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    // Register new borrower
    @PostMapping("/borrowers")
    public Borrower registerBorrower(@RequestBody Borrower borrower) {
        return borrowerRepository.save(borrower);
    }

    // Register new book
    @PostMapping("/books")
    public Book registerBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Get all books
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Borrow a book
    @PostMapping("/books/{bookId}/borrow/{borrowerId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (book.isBorrowed()) {
            return ResponseEntity.badRequest().body("Book already borrowed!");
        }
        Borrower borrower = borrowerRepository.findById(borrowerId).orElseThrow();
        book.setBorrowed(true);
        book.setBorrower(borrower);
        bookRepository.save(book);
        return ResponseEntity.ok("Book borrowed successfully");
    }

    // Return a book
    @PostMapping("/books/{bookId}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        if (!book.isBorrowed()) {
            return ResponseEntity.badRequest().body("Book was not borrowed!");
        }
        book.setBorrowed(false);
        book.setBorrower(null);
        bookRepository.save(book);
        return ResponseEntity.ok("Book returned successfully");
    }
}
