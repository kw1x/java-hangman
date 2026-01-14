package academy.hangman.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WordTest {
    
    @Test
    void shouldCreateValidWord() {
        Word word = new Word("кот", Category.ANIMALS, Difficulty.EASY);
        assertThat(word.value()).isEqualTo("кот");
        assertThat(word.category()).isEqualTo(Category.ANIMALS);
        assertThat(word.difficulty()).isEqualTo(Difficulty.EASY);
        assertThat(word.hint()).isNull();
        assertThat(word.length()).isEqualTo(3);
    }
    
    @Test
    void shouldCreateWordWithHint() {
        Word word = new Word("собака", Category.ANIMALS, Difficulty.EASY, "Лучший друг человека");
        assertThat(word.hint()).isEqualTo("Лучший друг человека");
    }
    
    @Test
    void shouldConvertToLowerCase() {
        Word word = new Word("СЛОН", Category.ANIMALS, Difficulty.EASY);
        assertThat(word.value()).isEqualTo("слон");
    }
    
    @Test
    void shouldThrowExceptionForNullValue() {
        assertThatThrownBy(() -> new Word(null, Category.ANIMALS, Difficulty.EASY))
            .isInstanceOf(NullPointerException.class);
    }
    
    @Test
    void shouldThrowExceptionForEmptyValue() {
        assertThatThrownBy(() -> new Word("", Category.ANIMALS, Difficulty.EASY))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Word cannot be null or empty");
        assertThatThrownBy(() -> new Word("   ", Category.ANIMALS, Difficulty.EASY))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Word cannot be null or empty");
    }
    
    @Test
    void shouldThrowExceptionForTooShortWord() {
        // Assert
        assertThatThrownBy(() -> new Word("а", Category.ANIMALS, Difficulty.EASY))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Word must have at least 2 characters");
    }
    
    @Test
    void shouldCorrectlyCheckContainsLetter() {
        // Arrange
        Word word = new Word("кот", Category.ANIMALS, Difficulty.EASY);
        
        // Assert
        assertThat(word.contains('к')).isTrue();
        assertThat(word.contains('о')).isTrue();
        assertThat(word.contains('т')).isTrue();
        assertThat(word.contains('К')).isTrue(); // Case insensitive
        assertThat(word.contains('х')).isFalse();
    }
    
    @Test
    void shouldCorrectlyGetCharAt() {
        // Arrange
        Word word = new Word("кот", Category.ANIMALS, Difficulty.EASY);
        
        // Assert
        assertThat(word.charAt(0)).isEqualTo('к');
        assertThat(word.charAt(1)).isEqualTo('о');
        assertThat(word.charAt(2)).isEqualTo('т');
    }
    
    @Test
    void shouldHandleSpecialCharacters() {
        // Act
        Word word = new Word("кот-собака", Category.ANIMALS, Difficulty.HARD);
        
        // Assert
        assertThat(word.value()).isEqualTo("кот-собака");
        assertThat(word.length()).isEqualTo(10);
        assertThat(word.contains('-')).isTrue();
    }
}