package com.example.wordToNumber.controller;

import com.example.wordToNumber.domain.Translation;
import com.example.wordToNumber.domain.User;
import com.example.wordToNumber.repos.TranslationRepo;
import com.example.wordToNumber.translators.Translator;
import com.example.wordToNumber.translators.TranslatorStrategy;
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
    public String main(Model model) throws IOException {
        Iterable<Translation> translations = translationRepo.findAll();
        model.addAttribute("translations", translations);


        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String textFrom,
            @ModelAttribute("modeChoice") String modeChoice,
            @ModelAttribute("languageChoice") String languageChoice,
            Model model
    ) throws IOException {
        String textTo;

        Translator translator =  TranslatorStrategy.valueOf(languageChoice).getTranslator(uploadPath);

        if(modeChoice.equals("1")){
            textTo = translator.translateNumber(textFrom);
        }
        else {
            textTo = translator.translateWord(textFrom);
        }


        model.addAttribute("textFrom", textFrom);
        model.addAttribute("textTo", textTo);
        model.addAttribute("modeChoice", modeChoice);
        model.addAttribute("languageChoice", languageChoice);

        if(!textTo.isEmpty()){
            Translation translation = new Translation(textFrom, textTo, user);
            translationRepo.save(translation);
        }
        return "main";
    }
}
