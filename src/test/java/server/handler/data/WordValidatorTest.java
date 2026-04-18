package server.handler.data;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordValidatorTest {

    @Test
    void canBeFormedRespectsLetterFrequency() {
        List<String> letters = Arrays.asList("A", "B", "B", "C", "D");

        assertTrue(WordValidator.canBeFormed("abb", letters));
        assertFalse(WordValidator.canBeFormed("abbb", letters));
    }

    @Test
    void dictionaryLookupIsCaseInsensitive() {
        String sampleWord = firstDictionaryWord();
        assertTrue(WordValidator.isInDictionary(sampleWord.toLowerCase()));
        assertTrue(WordValidator.isInDictionary(sampleWord.toUpperCase()));
    }

    private String firstDictionaryWord() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim();
                if (!word.isEmpty()) {
                    return word;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to load words.txt for test", e);
        }
        throw new IllegalStateException("words.txt did not contain any words");
    }
}


