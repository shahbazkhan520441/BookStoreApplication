package com.book.store.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenExpiredException extends RuntimeException {
   private  String message;
}
