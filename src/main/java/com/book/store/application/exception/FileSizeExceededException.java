package com.book.store.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileSizeExceededException extends RuntimeException {

    private String message;

}
