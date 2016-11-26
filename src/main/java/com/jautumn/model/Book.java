package com.jautumn.model;

import java.util.Date;

import lombok.Data;
import com.jautumn.model.enums.Language;

@Data
public class Book extends BaseEntity{
    private Long id;
    private String isbn;
    private String name;
    private Author author;
    private Publisher publisher;

    private Integer pages;
    private Date pubDate;
    private String description;

    private String pageLink;
    private String downloadLink;
    private String amazonLink;

    private Language language;
}
