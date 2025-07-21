package com.example.books.query.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookQueryDTO {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishedDate;
}
