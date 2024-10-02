package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Image;
import com.book.store.application.enums.AvailabilityStatus;
import com.book.store.application.enums.ImageType;
import com.book.store.application.exception.FileSizeExceededException;
import com.book.store.application.exception.InvalidFileFormatException;
import com.book.store.application.mapper.BookMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.ImageRepository;
import com.book.store.application.requestdto.BookRequest;
import com.book.store.application.responsedto.BookResponse;
import com.book.store.application.service.BookService;
import com.book.store.application.service.ImageService;
import com.book.store.application.util.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, ImageService imageService, ImageRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @Override
    public ResponseEntity<ResponseStructure<BookResponse>> addBook(int quantity, MultipartFile bookImage, BookRequest bookRequest) throws IOException {
        Book book = bookMapper.mapBookRequestToBook(bookRequest, new Book());
        book.setBookQuantity(quantity);
        book.setAvailabilityStatus(AvailabilityStatus.YES);
        book = bookRepository.save(book);

        Image image = null;


        if (bookImage != null && !bookImage.isEmpty()) {
            // Validate content type
            String contentType = bookImage.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/jpeg") ||
                            contentType.equals("image/png") ||
                            contentType.equals("image/jpg"))) {
                throw new InvalidFileFormatException("Only JPEG, PNG, and JPG formats are supported.");
            }

            // Validate file size
            long maxSizeInBytes = 2 * 1024 * 1024; // 2 MB
            if (bookImage.getSize() > maxSizeInBytes) {
                throw new FileSizeExceededException("File size must be less than 2 MB.");
            }
            String imagePath = imageService.uploadImage(bookImage);
            // Determine the image type dynamically
            ImageType imageType = determineImageType(contentType);
            image = Image.builder()
                    .image(imagePath)
                    .imageType(imageType)
                    .book(book)
                    .build();
            imageRepository.save(image);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<BookResponse>()
                .setStatus(HttpStatus.CREATED.value())
                .setMessage( " book added to database sucessfully")
                .setData(bookMapper.mapBookToBookResponse(book)));



    }

    // Helper method to determine ImageType based on content type
    private ImageType determineImageType(String contentType) {
        return switch (contentType.toLowerCase()) {
            case "image/jpeg" -> ImageType.JPEG;
            case "image/png" -> ImageType.PNG;
            case "image/jpg" -> ImageType.JPG;
            default -> throw new InvalidFileFormatException("Unsupported image format.");
        };
    }
//---------------------------------------------------------------------------------------------------------------------------------
    @Override
    public ResponseEntity<ResponseStructure<BookResponse>> updateBook(Long bookId, int quantity, MultipartFile bookImage, BookRequest bookRequest) throws IOException {
        // Fetch the book from the repository
        Book existingBook = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        // Map updated fields from BookRequest to the existing book
        existingBook = bookMapper.mapBookRequestToBook(bookRequest, existingBook);
        existingBook.setBookQuantity(quantity);
        existingBook.setAvailabilityStatus(quantity > 0 ? AvailabilityStatus.YES : AvailabilityStatus.NO);

        // Handle image update if provided
        if (bookImage != null && !bookImage.isEmpty()) {
            // Validate content type
            String contentType = bookImage.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/jpeg") ||
                            contentType.equals("image/png") ||
                            contentType.equals("image/jpg"))) {
                throw new InvalidFileFormatException("Only JPEG, PNG, and JPG formats are supported.");
            }

            // Validate file size
            long maxSizeInBytes = 2 * 1024 * 1024; // 2 MB
            if (bookImage.getSize() > maxSizeInBytes) {
                throw new FileSizeExceededException("File size must be less than 2 MB.");
            }

            // Upload new image and update the book's image
            String imagePath = imageService.uploadImage(bookImage);
            ImageType imageType = determineImageType(contentType);

            // Get the list of images associated with the book
            List<Image> existingImages = imageRepository.findByBook(existingBook);

            if (existingImages != null && !existingImages.isEmpty()) {
                // Assuming you want to update the first image in the list (or loop through and update all)
                Image existingImage = existingImages.get(0); // Example: updating the first image
                existingImage.setImage(imagePath);
                existingImage.setImageType(imageType);
                imageRepository.save(existingImage);
            } else {
                // Create a new image entry if no images exist
                Image newImage = Image.builder()
                        .image(imagePath)
                        .imageType(imageType)
                        .book(existingBook)
                        .build();
                imageRepository.save(newImage);
            }
        }

        // Save updated book entity
        bookRepository.save(existingBook);

        // Prepare the response
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<BookResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Book updated successfully.")
                .setData(bookMapper.mapBookToBookResponse(existingBook)));
    }


}
