package com.example.wordToNumber.repos;

import com.example.wordToNumber.domain.Translation;
import com.example.wordToNumber.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TranslationRepo extends CrudRepository<Translation, Integer> {
    List<Translation> findByAuthor(User author);
}
