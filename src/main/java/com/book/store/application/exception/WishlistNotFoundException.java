package com.book.store.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WishlistNotFoundException extends RuntimeException {
  private String message;
}
