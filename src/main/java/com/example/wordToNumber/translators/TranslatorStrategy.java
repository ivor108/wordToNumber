package com.example.wordToNumber.translators;

import java.io.IOException;


public enum TranslatorStrategy {
    RU {
        @Override
        public Translator getTranslator(String path) throws IOException {
            return new RuTranslator(path);
        }
    },
    EN {
        @Override
        public Translator getTranslator(String path) throws IOException {
            return new EnTranslator(path);
        }
    };
    public abstract Translator getTranslator(String path) throws IOException;
}
