package com.book.store.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class AlreadyAddressExistException extends RuntimeException {
 private String message;
}
