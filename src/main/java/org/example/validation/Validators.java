package org.example.validation;

import org.example.exception.ValidationException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
            // Log or handle (e.g., empty list if file missing)
            System.err.println("Failed to load badwords.txt: " + e.getMessage());  // Or use logger
        }
        return words;
    }

    public static void validateNoProfanity(String value, String fieldName) {
        if (value == null) return;

        String lowerValue = value.toLowerCase();
        for (String bad : BAD_WORDS) {
            Pattern pattern = Pattern.compile("\\b" + Pattern.quote(bad) + "\\b");
            if (pattern.matcher(lowerValue).find()) {
                throw new ValidationException("Invalid content in");
                ex.addError(fieldName, "contains prohibited word");
                throw ex;
            }
        }
    }
}