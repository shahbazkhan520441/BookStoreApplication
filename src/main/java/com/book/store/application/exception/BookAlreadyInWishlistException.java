package com.book.store.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BookAlreadyInWishlistException extends RuntimeException {
   private String message;
}