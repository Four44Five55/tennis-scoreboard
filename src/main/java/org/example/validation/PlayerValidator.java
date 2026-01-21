package org.example.validation;

import org.example.entity.Player;

public final class PlayerValidator {
    public static final int NAME_MAX = 20;

    private PlayerValidator() {
    }

    public static void validateCreate(Player p) {
        Errors e = new Errors();

        String name = Validators.trim(p.getName());
        Validators.requireNotBlank(e, "name", name, "Имя не может быть пустым");
        Validators.maxLen(e, "name", name, NAME_MAX, "Максимум " + NAME_MAX + " символов");
        Validators.validateNoProfanity(e, "name", name);

        p.setName(name);

        e.throwIfAny();
    }
    public static String validateNameQuery(String name) {
        Errors e = new Errors();

        String n = Validators.trim(name);
        Validators.requireNotBlank(e, "name", n, "Имя не может быть пустым");
        Validators.maxLen(e, "name", n, NAME_MAX, "Максимум " + NAME_MAX + " символов");
        Validators.validateNoProfanity(e, "name", n);
        e.throwIfAny();
        return n;
    }
}