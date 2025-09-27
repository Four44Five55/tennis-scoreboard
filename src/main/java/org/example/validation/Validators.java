package org.example.validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public final class Validators {
    private static final Set<String> BAD_WORDS = loadBadWords();

    private Validators() {
    }

    public static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public static void requireNotBlank(Errors e, String field, String value, String msg) {
        if (value == null || value.trim().isEmpty()) e.add(field, msg);
    }

    public static void maxLen(Errors e, String field, String value, int max, String msg) {
        if (value != null && value.length() > max) e.add(field, msg);
    }

    public static void minLen(Errors e, String field, String value, int min, String msg) {
        if (value != null && value.length() < min) e.add(field, msg);
    }

    public static void inSet(Errors e, String field, String value, Set<String> allowed, String msg) {
        if (value != null && !allowed.contains(value)) e.add(field, msg);
    }

    public static void requirePositiveInt(Errors e, String field, String value, String msg) {
        try {
            if (Integer.parseInt(value) <= 0) e.add(field, msg);
        } catch (NumberFormatException ex) {
            e.add(field, msg);
        }
    }

    private static Set<String> loadBadWords() {
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Validators.class.getClassLoader().getResourceAsStream("badwords.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());  // Add, normalize to lower
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки файла с нецензурной лексикой badwords.txt: " + e.getMessage());  // Or use logger
        }
        return words;
    }

    public static void validateNoProfanity(Errors e, String field, String value) {
        if (value == null || value.isBlank() || BAD_WORDS.isEmpty()) return;

        String normalized = normalize(value);

        for (String token : tokens(normalized)) {
            if (BAD_WORDS.contains(token)) {
                e.add(field, "Недопустимая лексика");
                return;
            }
        }

        String squeezed = normalized.replaceAll("[^\\p{L}]+", "");
        for (String w : BAD_WORDS) {
            if (squeezed.contains(w)) {
                e.add(field, "Недопустимая лексика");
                return;
            }
        }
    }

    private static String normalize(String s) {
        String x = s.toLowerCase(Locale.ROOT).replace('ё', 'е');
        x = Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        // упрощённая “leet”-нормализация
        x = x.replace('0', 'o').replace('@', 'a').replace('$', 's')
                .replace('1', 'i').replace('3', 'e').replace('7', 't');
        // схлопнуть слишком длинные повторы: “ооо” -> “оо”
        return x.replaceAll("(\\p{L})\\1{2,}", "$1$1").trim();
    }

    private static List<String> tokens(String normalized) {
        if (normalized.isBlank()) return List.of();
        return Arrays.stream(normalized.split("[^\\p{L}]+"))
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());
    }
}