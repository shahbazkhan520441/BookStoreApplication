package com.book.store.application.email;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageData {
    private String to;
    private String subject;
    private String text;
    private Date sendDate;
}