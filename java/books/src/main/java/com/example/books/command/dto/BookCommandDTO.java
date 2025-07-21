package com.example.books.command.dto;

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
public class BookCommandDTO {
    private String title;
    private String author;
    private LocalDate publishedDate;
}
