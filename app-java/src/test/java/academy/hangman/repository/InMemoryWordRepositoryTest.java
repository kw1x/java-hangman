package academy.hangman.repository;

import static org.assertj.core.api.Assertions.assertThat;

import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.Word;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryWordRepositoryTest {

    private InMemoryWordRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWordRepository();
    }

    @Test
    void shouldReturnWordForSpecificCategoryAndDifficulty() {
        // Act
        Optional<Word> word = repository.getRandomWord(Category.ANIMALS, Difficulty.EASY);

        // Assert
        assertThat(word).isPresent();
        assertThat(word.get().category()).isEqualTo(Category.ANIMALS);
        assertThat(word.get().difficulty()).isEqualTo(Difficulty.EASY);
    }

    @Test
    void shouldReturnRandomWord() {
        // Act
        Optional<Word> word = repository.getRandomWord();

        // Assert
        assertThat(word).isPresent();
        assertThat(word.get().value()).isNotBlank();
    }

    @Test
    void shouldReturnDifferentWordsOnMultipleCalls() {
        Set<String> wordValues = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            Optional<Word> word = repository.getRandomWord();
            if (word.isPresent()) {
                wordValues.add(word.get().value());
            }
        }
        assertThat(wordValues.size()).isGreaterThan(1);
    }

    @Test
    void shouldReturnEmptyForNonExistentCategoryDifficultyCombo() {
        List<Word> allWords = repository.getAllWords();
        boolean foundMissing = false;
        for (Category category : Category.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                boolean exists =
                        allWords.stream().anyMatch(w -> w.category() == category && w.difficulty() == difficulty);
                if (!exists) {
                    Optional<Word> result = repository.getRandomWord(category, difficulty);
                    assertThat(result).isEmpty();
                    foundMissing = true;
                    break;
                }
            }
            if (foundMissing) break;
        }
    }

    @Test
    void shouldReturnAllWords() {
        // Act
        List<Word> allWords = repository.getAllWords();

        // Assert
        assertThat(allWords).isNotEmpty();
        assertThat(allWords)
                .allMatch(word -> word.value() != null && !word.value().isBlank());
        assertThat(allWords).allMatch(word -> word.category() != null);
        assertThat(allWords).allMatch(word -> word.difficulty() != null);
    }

    @Test
    void shouldHaveWordsForAllCategories() {
        List<Word> allWords = repository.getAllWords();
        Set<Category> categoriesInRepo = new HashSet<>();
        allWords.forEach(word -> categoriesInRepo.add(word.category()));
        assertThat(categoriesInRepo).containsAll(Set.of(Category.values()));
    }

    @Test
    void shouldHaveWordsForAllDifficulties() {
        List<Word> allWords = repository.getAllWords();
        Set<Difficulty> difficultiesInRepo = new HashSet<>();
        allWords.forEach(word -> difficultiesInRepo.add(word.difficulty()));
        assertThat(difficultiesInRepo).containsAll(Set.of(Difficulty.values()));
    }

    @Test
    void shouldHaveValidWordLengths() {
        List<Word> allWords = repository.getAllWords();
        assertThat(allWords).allMatch(word -> word.length() >= 2);
    }

    @Test
    void shouldReturnWordsThatMatchRequestedParameters() {
        // Arrange
        Category targetCategory = Category.FRUITS;
        Difficulty targetDifficulty = Difficulty.EASY;

        // Act - проверяем несколько раз чтобы убедиться в стабильности
        for (int i = 0; i < 10; i++) {
            Optional<Word> word = repository.getRandomWord(targetCategory, targetDifficulty);
            if (word.isPresent()) {
                // Assert
                assertThat(word.get().category()).isEqualTo(targetCategory);
                assertThat(word.get().difficulty()).isEqualTo(targetDifficulty);
            }
        }
    }
}
