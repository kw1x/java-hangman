package academy.hangman;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        String result = TestMode.runTest(null, "кот");
        assertThat(result).isEqualTo("Unknown word");
    }
    
    @Test
    void shouldReturnUnknownWordForEmptyTarget() {
        // Act
        String result1 = TestMode.runTest("", "кот");
        String result2 = TestMode.runTest("   ", "кот");
        
        // Assert
        assertThat(result1).isEqualTo("Unknown word");
        assertThat(result2).isEqualTo("Unknown word");
    }
    
    @Test
    void shouldReturnUnknownWordForNullGuess() {
        // Act
        String result = TestMode.runTest("кот", null);
        
        // Assert
        assertThat(result).isEqualTo("Unknown word");
    }
    
    @Test
    void shouldReturnUnknownWordForEmptyGuess() {
        // Act
        String result1 = TestMode.runTest("кот", "");
        String result2 = TestMode.runTest("кот", "   ");
        
        // Assert
        assertThat(result1).isEqualTo("Unknown word");
        assertThat(result2).isEqualTo("Unknown word");
    }
    
    @Test
    void shouldReturnUnknownWordForTooShortTarget() {
        // Act
        String result = TestMode.runTest("а", "а");
        
        // Assert
        assertThat(result).isEqualTo("Unknown word");
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