package com.example.wordToNumber.controller;

import com.example.wordToNumber.domain.Translation;
import com.example.wordToNumber.domain.User;
import com.example.wordToNumber.repos.TranslationRepo;
import com.example.wordToNumber.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private TranslationRepo translationRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping()
    public String showHistory(
            Model model,
            @RequestParam(required = false, defaultValue = "") String filter
    ) {
        Iterable<Translation> translations = translationRepo.findAll();

        if(filter != null && !filter.isEmpty()){
            User author = userRepo.findByUsername(filter);
            translations = translationRepo.findByAuthor(author);
        }else{
            translations = translationRepo.findAll();
        }

        model.addAttribute("translations", translations);
        model.addAttribute("filter", filter);
        return "history";
    }
}
