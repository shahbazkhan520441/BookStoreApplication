package com.book.store.application.controller;

import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.service.BookService;
import com.book.store.application.util.ErrorStructure;
import com.book.store.application.util.ResponseStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "Books Endpoints", description = "Contains all the endpoints related to book management in the bookstore application")
//@CrossOrigin(origins = "*", allowCredentials = "true")
public class BookController {

    private final BookService bookService;

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds a new book along with its image to the database.
     *
     * @param quantity    the quantity of the book to be added.
     * @param bookImage   the image file of the book.
     * @param bookRequest the details of the book to be added, wrapped in a BookRequest object.
     * @return ResponseEntity containing a ResponseStructure with the added BookResponse object.
     * @throws IOException if there is an error processing the image file.
     */
    @PostMapping("/book")
    @Operation(description = "The endpoint is used to add a new book along with its image to the database.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book successfully added to the database"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<BookResponse>> addBook(
            @RequestParam("quantity") int quantity,
            @RequestPart("bookImage") MultipartFile bookImage,
            @RequestPart("bookrequest") @Valid BookRequest bookRequest) throws IOException { // @Valid added here

        return bookService.addBook(quantity, bookImage, bookRequest);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Updates an existing book in the database, including its image.
     *
     * @param bookId      the ID of the book to be updated.
     * @param quantity    the updated quantity of the book.
     * @param bookImage   the new image file of the book.
     * @param bookRequest the updated details of the book, wrapped in a BookRequest object.
     * @return ResponseEntity containing a ResponseStructure with the updated BookResponse object.
     * @throws IOException if there is an error processing the new image file.
     */
    @PutMapping("/books/{bookId}")
    @Operation(description = "The endpoint is used to update an existing book in the database, including its image.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<BookResponse>> updateBook(
            @PathVariable Long bookId,
            @RequestParam int quantity,
            @RequestPart("bookImage") MultipartFile bookImage,
            @RequestPart("bookrequest") @Valid BookRequest bookRequest) throws IOException { // @Valid added here
        return bookService.updateBook(bookId, quantity, bookImage, bookRequest);
    }

//    --------------------------------------------------------------------------------------------------------------------------------------------------------

    @PatchMapping("/books/{bookId}/quantity")
    @Operation(description = "The endpoint is used to update the quantity of an existing book in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book quantity successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<BookResponse>> updateBookQuantity(
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        return bookService.updateBookQuantity(bookId, quantity);
    }


    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves the details of a specific book by its ID.
     *
     * @param bookId the ID of the book to retrieve.
     * @return ResponseEntity containing a ResponseStructure with the BookResponse object.
     * @throws IllegalArgumentException if the bookId is null or invalid.
     */
    @GetMapping("/books/{bookId}")
    @Operation(description = "The endpoint is used to retrieve the details of a specific book by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Book not found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<BookResponse>> findBook(@PathVariable Long bookId) {
        return bookService.findBook(bookId);
    }

    //------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves a list of all books in the database.
     *
     * @return ResponseEntity containing a ResponseStructure with a list of BookResponse objects.
     * @throws NoSuchElementException if no books are found in the database.
     */
    @GetMapping("/books")
    @Operation(description = "The endpoint is used to retrieve a list of all books in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Books successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "No books found", content = {
                            @Content(schema = @Schema(oneOf = ErrorStructure.class))
                    })
            })
    public ResponseEntity<ResponseStructure<List<BookResponse>>> findBooks() {
        return bookService.findBooks();
    }
}
