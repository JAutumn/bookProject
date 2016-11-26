package com.jautumn.model;

import java.util.List;

import lombok.Data;

@Data
public class Author extends BaseEntity{
    private Long id;
    private String name;
    private List<Book> books;
}
