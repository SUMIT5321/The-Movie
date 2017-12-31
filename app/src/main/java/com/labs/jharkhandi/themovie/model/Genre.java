package com.labs.jharkhandi.themovie.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sumit on 30/12/17.
 */

public class Genre {
    private int id;
    private String name;
    public static List<Genre> GenreList = Arrays.asList(
            new Genre(28,"Action"),
            new Genre(27,"Horror"),
            new Genre(16,"Animation"),
            new Genre(10749,"Romance"),
            new Genre(35,"Comedy"),
            new Genre(80,"Crime")
    );

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
