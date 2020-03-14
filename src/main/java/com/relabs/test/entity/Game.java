package com.relabs.test.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Game implements Serializable {
    private String artistName;
    private Long id;
    private LocalDate releaseDate;
    private String name;
    private URL url;
    private URL artworkUrl100;
}
