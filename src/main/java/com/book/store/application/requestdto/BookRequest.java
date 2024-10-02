package com.book.store.application.requestdto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@Builder
public class BookRequest {

    private String bookName;
    private String bookAuthor;
    private String bookDescription;
    private Double bookPrice;

}
