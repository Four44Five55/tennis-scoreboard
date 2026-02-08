package org.example.validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public static void notStartNumber(Errors e, String field, String value, String msg) {
        if (value != null && !value.isEmpty() && Character.isDigit(value.charAt(0))) e.add(field, msg);
    }
    public static void isOnlyNumber(Errors e, String field, String value, String msg) {
        if (value != null && value.matches("\\d+")) e.add(field, msg);
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
                Objects.requireNonNull(Validators.class.getClassLoader().getResourceAsStream("badwords.txt")),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim().toLowerCase();
                if (!trimmed.isEmpty() && !trimmed.startsWith("\\")) {
                    words.add(trimmed);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки файла с нецензурной лексикой badwords.txt: " + e.getMessage());
        }
        return words;
    }

    public static void validateNoProfanity(Errors e, String field, String value) {
        if (value == null || value.isBlank() || BAD_WORDS.isEmpty()) return;

        String normalized = normalize(value);
        List<String> tokensList = tokens(normalized);

        for (String token : tokensList) {
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
        String x = s.toLowerCase().replace('ё', 'е');
        x = Normalizer.normalize(x, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        x = x.replace('0', 'o').replace('@', 'a').replace('$', 's')
                .replace('1', 'i').replace('3', 'e').replace('7', 't');
        return x.replaceAll("(\\p{L})\\1{2,}", "$1$1").trim();
    }

    private static List<String> tokens(String normalized) {
        if (normalized.isBlank()) return List.of();
        return Arrays.stream(normalized.split("[^\\p{L}]+"))
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());
    }
}