package com.relabs.test.repository;

import com.relabs.test.entity.Game;

import java.util.List;

public interface GameListRepository {
    List<Game> save(List<Game> games, String name);

    List<List<Game>> findAll();

    List<Game> findByName(String name);

    List<Game> update(List<Game> games, String name);

    void delete(String name);
}
