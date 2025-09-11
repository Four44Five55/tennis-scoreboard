package org.example.validation;

import org.example.model.Player;

import static org.example.validation.Validators.*;

public final class PlayerValidator {
    public static final int NAME_MAX = 20;

    private PlayerValidator() {
    }

    public static void validate(Player p) {
        Errors e = new Errors();
        String name = trim(p.getName());

        requireNotBlank(e, "name", name, "Имя не может быть пустым");
        maxLen(e, "name", name, NAME_MAX, "Максимум " + NAME_MAX + " символов");

        p.setName(name);

        e.throwIfAny();
    }
}