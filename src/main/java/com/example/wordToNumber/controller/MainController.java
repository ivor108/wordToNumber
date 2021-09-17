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
    private NumberTranslator numberTranslator;
    private WordTranslator wordTranslator;

    @Autowired
    private TranslationRepo translationRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) throws IOException {
        Iterable<Translation> translations = translationRepo.findAll();
        model.addAttribute("translations", translations);

        numberTranslator = new NumberTranslator(Languages.RU, uploadPath);
        wordTranslator = new WordTranslator(Languages.RU, uploadPath);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String textFrom,
            @ModelAttribute("modeChoice") String modeChoice,
            Model model
    ) {
        String textTo;

        if(modeChoice.equals("1")){
            textTo = numberTranslator.translate(textFrom);
        }
        else {
            textTo = wordTranslator.translate(textFrom);
        }

        model.addAttribute("textFrom", textFrom);
        model.addAttribute("textTo", textTo);

        if(!textTo.isEmpty()){
            Translation translation = new Translation(textFrom, textTo, user);
            translationRepo.save(translation);
        }

        Iterable<Translation> translations = translationRepo.findAll();

        model.addAttribute("translations", translations);

        return "main";
    }
}
