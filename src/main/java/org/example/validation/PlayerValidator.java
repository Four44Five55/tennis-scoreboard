package org.example.validation;

import org.example.exception.ValidationException;

public final class PlayerValidator {
    public static final int NAME_MAX = 20;

    private PlayerValidator() {
    }

    public static String validateName(String name) throws ValidationException {
        Errors e = new Errors();
        Validators.requireNotBlank(e, "name", name, "Имя не может быть пустым");
        Validators.maxLen(e, "name", name, NAME_MAX, "Максимум " + NAME_MAX + " символов");
        Validators.validateNoProfanity(e, "name", name);
        Validators.notStartNumber(e,"name", name, "Имя не должно начинаться с числа");
        Validators.isOnlyNumber(e,"name", name, "Имя не должно состоять из чисел");
        e.throwIfAny();
        return name;
    }
}