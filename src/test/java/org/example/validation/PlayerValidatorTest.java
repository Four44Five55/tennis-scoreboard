package org.example.validation;

import org.example.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("PlayerValidator: проверка имени игрока")
class PlayerValidatorTest {


    @Test
    @DisplayName("Обычное имя возвращается без изменений")
    void validName_Success() throws ValidationException {
        assertEquals("John", PlayerValidator.validateName("John"));
    }

    @Test
    void nullName_ThrowsException() {
        assertThrows(ValidationException.class,
                () -> PlayerValidator.validateName(null));
    }

    @Test
    void emptyName_ThrowsException() {
        assertThrows(ValidationException.class,
                () -> PlayerValidator.validateName(""));
    }

    @Test
    void tooLongName_ThrowsException() {
        assertThrows(ValidationException.class,
                () -> PlayerValidator.validateName("A".repeat(21)));
    }

    @Test
    void startsWithNumber_ThrowsException() {
        assertThrows(ValidationException.class,
                () -> PlayerValidator.validateName("1John"));
    }

    @Test
    void onlyNumbers_ThrowsException() {
        assertThrows(ValidationException.class,
                () -> PlayerValidator.validateName("12345"));
    }
}