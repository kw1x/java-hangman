package academy.hangman;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestModeTest {

    @Test
    void shouldReturnPositiveResultForCorrectGuess() {
        String result = TestMode.runTest("кот", "кот");
        assertThat(result).isEqualTo("кот;POS");
    }

    @Test
    void shouldReturnNegativeResultForIncorrectGuess() {
        String result = TestMode.runTest("кот", "абв");
        assertThat(result).isEqualTo("***;NEG");
    }

    @Test
    void shouldReturnPartiallyGuessedWord() {
        String result = TestMode.runTest("слон", "со");
        assertThat(result).isEqualTo("с*о*;NEG");
    }

    @Test
    void shouldHandleCaseInsensitiveInput() {
        String result1 = TestMode.runTest("КОТ", "кот");
        String result2 = TestMode.runTest("кот", "КОТ");
        assertThat(result1).isEqualTo("кот;POS");
        assertThat(result2).isEqualTo("кот;POS");
    }

    @Test
    void shouldReturnUnknownWordForNullTarget() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest(null, "кот"));
    }

    @Test
    void shouldReturnUnknownWordForEmptyTarget() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest("", "кот"));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest("   ", "кот"));
    }

    @Test
    void shouldReturnUnknownWordForNullGuess() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest("кот", null));
    }

    @Test
    void shouldReturnUnknownWordForEmptyGuess() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest("кот", ""));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest("кот", "   "));
    }

    @Test
    void shouldReturnUnknownWordForTooShortTarget() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> TestMode.runTest("а", "а"));
    }

    @Test
    void shouldHandleRepeatedLetters() {
        // Act
        String result = TestMode.runTest("мама", "ам");

        // Assert
        assertThat(result).isEqualTo("мама;POS");
    }

    @Test
    void shouldHandlePartialGuessWithRepeatedLetters() {
        // Act
        String result = TestMode.runTest("мама", "м");

        // Assert
        assertThat(result).isEqualTo("м*м*;NEG");
    }

    @Test
    void shouldLoseAfterTooManyIncorrectGuesses() {
        // Act - гадаем много неправильных букв
        String result = TestMode.runTest("кот", "абвгдежзийклмнпрс");

        // Assert
        assertThat(result).contains("NEG");
    }

    @Test
    void shouldHandleMixedCorrectAndIncorrectGuesses() {
        // Act
        String result = TestMode.runTest("дом", "дхом");

        // Assert
        assertThat(result).isEqualTo("дом;POS");
    }

    @Test
    void shouldTrimInputSpaces() {
        // Act
        String result = TestMode.runTest("  кот  ", "  кот  ");

        // Assert
        assertThat(result).isEqualTo("кот;POS");
    }
}
