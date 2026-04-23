package server.handler.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WordValidator {

    private static Set<String> DICTIONARY = new HashSet<>();

    static {
        loadWords();
    }

    private static void loadWords() {
        try (InputStream in = WordValidator.class.getClassLoader().getResourceAsStream("words.txt");
             BufferedReader reader = in == null ? null : new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            if (reader == null) {
                System.err.println("words.txt not found in classpath; dictionary is empty.");
                return;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toUpperCase();
                if (!line.isEmpty()) {
                    DICTIONARY.add(line);
                }
            }
            System.out.println("Loaded " + DICTIONARY.size() + " words into dictionary.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load words.txt configuration.");
        }
    }

    public static boolean isInDictionary(String word) {
        return DICTIONARY.contains(word.trim().toUpperCase());
    }

    public static boolean canBeFormed(String word, List<String> availableLetters) {
        word = word.trim().toUpperCase();

        Map<Character, Long> availableCounts = availableLetters.stream()
                .map(s -> s.charAt(0))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        for (char c : word.toCharArray()) {
            Long count = availableCounts.get(c);
            if (count == null || count == 0) {
                return false;
            }
            availableCounts.put(c, count - 1);
        }
        return true;
    }

    public static List<String> findSubmittableWords(List<String> availableLetters, int minLength) {
        if (availableLetters == null || availableLetters.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> matches = new ArrayList<>();
        for (String candidate : DICTIONARY) {
            if (candidate.length() >= minLength && canBeFormed(candidate, availableLetters)) {
                matches.add(candidate);
            }
        }
        Collections.sort(matches);
        return matches;
    }

    public static List<String> getDictionaryWords() {
        List<String> words = new ArrayList<>(DICTIONARY);
        Collections.sort(words);
        return Collections.unmodifiableList(words);
    }
}



