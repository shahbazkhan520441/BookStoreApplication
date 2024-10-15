package com.book.store.application.serviceimpl;

import com.book.store.application.entity.Book;
import com.book.store.application.entity.Discount;
import com.book.store.application.exception.BookNotExistException;
import com.book.store.application.exception.DiscountNotExistException;
import com.book.store.application.mapper.DiscountMapper;
import com.book.store.application.repository.BookRepository;
import com.book.store.application.repository.DiscountRepository;
import com.book.store.application.requestdto.DiscountRequest;
import com.book.store.application.responsedto.DiscountResponse;
import com.book.store.application.service.DiscountService;
import com.book.store.application.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final BookRepository bookRepository;
    private final DiscountMapper discountMapper;

    @Override
    public ResponseEntity<ResponseStructure<List<DiscountResponse>>> getDiscounts(Long bookId) {
        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new BookNotExistException("book Id : " + bookId + ", is not exist"));

        List<Discount> discounts = discountRepository.findByBookAndIsActiveTrue(book);
        List<DiscountResponse> discountResponses = discounts
                .stream()
                .map(discountMapper::mapDiscountToDiscountResponse)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<List<DiscountResponse>>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Discounts are founded")
                .setData(discountResponses));
    }
    //---------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<DiscountResponse>> updateDiscount(
            Long sellerId,
            Long discountId,
            DiscountRequest discountRequest) {

        Discount discount = discountRepository
                .findById(discountId)
                .orElseThrow(() -> new DiscountNotExistException("Discount Id : " + discountId + ", is not exist"));

        discount = discountMapper.mapDiscountRequestToDiscount(discountRequest, discount);
        discount = discountRepository.save(discount);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<DiscountResponse>()
                .setStatus(HttpStatus.OK.value())
                .setMessage("Discounts is Updated")
                .setData(discountMapper.mapDiscountToDiscountResponse(discount)));
    }
}