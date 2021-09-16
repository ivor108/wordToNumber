package com.example.wordToNumber.repos;

import com.example.wordToNumber.domain.Translation;
import org.springframework.data.repository.CrudRepository;

public interface TranslationRepo extends CrudRepository<Translation, Integer> {
}
