package com.book.store.application.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseStructure<T> {

	private int status;
	private String message;
	private T data;


	public ResponseStructure<T> setStatus(int status){
		this.status=status;
		return this;
	}


	public ResponseStructure<T> setMessage(String message){
		this.message=message;
		return this;
	}

	public ResponseStructure<T>  setData(T data){
		this.data=data;
		return this;
	}





}
