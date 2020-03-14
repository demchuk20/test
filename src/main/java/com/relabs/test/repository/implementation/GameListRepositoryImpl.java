package com.relabs.test.repository.implementation;

import com.relabs.test.entity.Game;
import com.relabs.test.repository.GameListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameListRepositoryImpl implements GameListRepository {
    private HashOperations hashOperations;

    @Autowired
    public GameListRepositoryImpl(RedisTemplate<String, List<Game>> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public List<Game> save(List<Game> games, String name) {
        hashOperations.put("GAMES", name, games);
        return (List<Game>) hashOperations.get("GAMES", name);
    }


    @Override
    public List<List<Game>> findAll() {
        return hashOperations.values("GAMES");
    }

    @Override
    public List<Game> findByName(String name) {
        return (List<Game>) hashOperations.get("GAMES", name);
    }

    @Override
    public List<Game> update(List<Game> games, String name) {
        return save(games, name);
    }

    @Override
    public void delete(String name) {
        hashOperations.delete("GAMES", name);
    }
}
