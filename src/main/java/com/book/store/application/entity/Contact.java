package com.book.store.application.entity;

import com.book.store.application.enums.Priority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    private Long contactNumber;
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JsonIgnore
    private Address address;


}