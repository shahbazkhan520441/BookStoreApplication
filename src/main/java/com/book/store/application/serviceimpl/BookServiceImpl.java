package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Cart;
import com.book.store.application.entity.Discount;
import com.book.store.application.entity.Image;
import com.book.store.application.enums.AvailabilityStatus;
import com.book.store.application.enums.DiscountType;
import com.book.store.application.enums.ImageType;
import com.book.store.application.exception.FileSizeExceededException;
import com.book.store.application.exception.InvalidFileFormatException;
import com.book.store.application.mapper.BookMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.CartRepository;
import com.book.store.application.repository.DiscountRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final DiscountRepository discountRepository;
    private final CartRepository cartRepository;



    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, ImageService imageService, ImageRepository imageRepository, DiscountRepository discountRepository, CartRepository cartRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.discountRepository = discountRepository;
        this.cartRepository = cartRepository;
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

        //  save Discount object
        saveDiscount(bookRequest, book);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<BookResponse>()
                .setStatus(HttpStatus.CREATED.value())
                .setMessage(" book added to database sucessfully")
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

    //---------------------------------------------------------------------------------------------------
//    save thee Discount object
    private void saveDiscount(BookRequest bookRequest, Book book) {
        Discount discount = new Discount();
        discount.setBook(book);

        if (bookRequest.getDiscountType() != null) {
            discount.setDiscountType(bookRequest.getDiscountType());
        } else {
            discount.setDiscountType(DiscountType.FLAT);
        }
        if (bookRequest.getDiscount() != 0.0) {
            discount.setDiscountValue(bookRequest.getDiscount());
        } else {
            discount.setDiscountValue(0.0);
        }
        discount.setActive(true);
        discountRepository.save(discount);
    }

    //    Update discount
    private void updateDiscount(BookRequest bookRequest, Book book) {
        List<Discount> discounts = discountRepository.findByBookAndIsActiveTrue(book);
        if (!discounts.isEmpty()) {
            Discount discount = discounts.getFirst();
            discount.setDiscountValue(bookRequest.getDiscount());
            discount.setDiscountType(bookRequest.getDiscountType());
            discountRepository.save(discount);
        }
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public ResponseEntity<ResponseStructure<BookResponse>> updateBook(Long bookId, int quantity, MultipartFile bookImage, BookRequest bookRequest) throws IOException {

        // Fetch the existing book from the repository
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book Id : " + bookId + ", does not exist"));

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

            // Validate file size (max 2MB)
            long maxSizeInBytes = 2 * 1024 * 1024; // 2 MB
            if (bookImage.getSize() > maxSizeInBytes) {
                throw new FileSizeExceededException("File size must be less than 2 MB.");
            }

            // Upload new image
            String imagePath = imageService.uploadImage(bookImage);
            ImageType imageType = determineImageType(contentType);

            // Get the list of existing images associated with the book
            List<Image> existingImages = imageRepository.findByBook(book);

            if (existingImages != null && !existingImages.isEmpty()) {
                // Update the first image in the list
                Image existingImage = existingImages.get(0);
                existingImage.setImage(imagePath);
                existingImage.setImageType(imageType);
                imageRepository.save(existingImage);
            } else {
                // Create a new image entry if no images exist
                Image newImage = Image.builder()
                        .image(imagePath)
                        .imageType(imageType)
                        .book(book)
                        .build();
                imageRepository.save(newImage);
            }

        } else {

            List<Image> images = imageRepository.findByBook(book);
            if (!images.isEmpty()) {
                // If there are existing images, set them to the book
                book.setImages(images);
            }


        }

        // Update book entity with new values
        book = bookMapper.mapBookRequestToBook(bookRequest, book);
        book.setBookQuantity(quantity);
        book.setAvailabilityStatus(quantity > 0 ? AvailabilityStatus.YES : AvailabilityStatus.NO);

        // Save the updated book
        book = bookRepository.save(book);

        // Update discounts if applicable
        updateDiscount(bookRequest, book);

        // Update all carts that contain this book
        updateCart(book);

        // Return the updated book in the response
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<BookResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Book updated successfully.")
                .setData(bookMapper.mapBookToBookResponse(book)));
    }

    // Method to update carts that contain the updated book
    private void updateCart(Book book) {
        // Fetch all carts that contain the book
        List<Cart> carts = cartRepository.findByBook(book);

        // Update each cart with the updated book
        carts.forEach(cart -> {
            cart.setBook(book); // Update the book reference in the cart
            cartRepository.save(cart); // Save the updated cart entry
        });
    }


//    ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<BookResponse>> findBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<BookResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Book fetched successfully.")
                .setData(bookMapper.mapBookToBookResponse(book))
        );
    }

    //    -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<List<BookResponse>>> findBooks() {
        // Fetch all books from the repository
        List<Book> books = bookRepository.findAll();

        // Map the list of Book entities to a list of BookResponse DTOs
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::mapBookToBookResponse)
                .toList(); // Use `collect(Collectors.toList())` for Java 8

        // Prepare the response using your preferred way
        ResponseStructure<List<BookResponse>> responseStructure = new ResponseStructure<>();
        responseStructure
                .setStatus(HttpStatus.OK.value())
                .setMessage("Books fetched successfully.")
                .setData(bookResponses);

        // Return the response entity with the response structure and HTTP status
        return ResponseEntity.status(HttpStatus.OK).body(responseStructure);
    }


}
