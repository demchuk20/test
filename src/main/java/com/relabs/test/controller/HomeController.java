package com.relabs.test.controller;

import com.relabs.test.entity.Game;
import com.relabs.test.repository.GameListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private GameListRepository repository;

    @Autowired
    public HomeController(GameListRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false, defaultValue = "100") String limit,
                       @RequestParam String country, @RequestParam String category, Model model) {
        List<Game> gameList = repository.findByName(category + country);
        model.addAttribute("gameList", gameList.subList(0, Integer.parseInt(limit)));
        return "list";
    }

}
