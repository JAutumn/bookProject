package com.jautumn.model;

import java.util.List;

import lombok.Data;

@Data
public class Publisher extends BaseEntity {
    private Long id;
    private String name;
    private List<Book> books;
}
