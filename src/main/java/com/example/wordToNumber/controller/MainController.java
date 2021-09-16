package com.example.wordToNumber.controller;

import com.example.wordToNumber.domain.Translation;
import com.example.wordToNumber.domain.User;
import com.example.wordToNumber.repos.TranslationRepo;
import com.example.wordToNumber.translators.Languages;
import com.example.wordToNumber.translators.NumberTranslator;
import com.example.wordToNumber.translators.WordTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


@Controller
public class MainController {
    @Autowired
    private TranslationRepo translationRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Translation> translations = translationRepo.findAll();

        model.addAttribute("translations", translations);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String textFrom,
            @ModelAttribute("modeChoice") String modeChoice,
            Model model
    ) throws IOException {
        String textTo;

        if(modeChoice.equals("1")){
            NumberTranslator numberTranslator = new NumberTranslator(Languages.RU, uploadPath);
            textTo = numberTranslator.translate(textFrom);
        }
        else {
            WordTranslator wordTranslator = new WordTranslator(Languages.RU, uploadPath);
            textTo = wordTranslator.translate(textFrom);
        }

        model.addAttribute("textTo", textTo);

        Translation translation = new Translation(textFrom, textTo, user);

        translationRepo.save(translation);

        Iterable<Translation> translations = translationRepo.findAll();

        model.addAttribute("translations", translations);

        return "main";
    }
}
