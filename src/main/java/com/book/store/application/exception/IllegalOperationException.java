package com.book.store.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IllegalOperationException extends RuntimeException {

	private String message;
}
