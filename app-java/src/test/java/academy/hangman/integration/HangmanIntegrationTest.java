package academy.hangman.integration;

import static org.assertj.core.api.Assertions.assertThat;

import academy.hangman.game.HangmanGame;
import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.GameResult;
import academy.hangman.model.GuessResult;
import academy.hangman.model.Word;
import academy.hangman.repository.InMemoryWordRepository;
import academy.hangman.repository.WordRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HangmanIntegrationTest {

    private WordRepository wordRepository;

    @BeforeEach
    void setUp() {
        wordRepository = new InMemoryWordRepository();
    }

    @Test
    void shouldPlayCompleteGameSuccessfully() {
        Optional<Word> wordOpt = wordRepository.getRandomWord(Category.ANIMALS, Difficulty.EASY);
        assertThat(wordOpt).isPresent();
        Word word = wordOpt.get();
        HangmanGame game = new HangmanGame(word, 6);
        GuessResult result = null;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (result == null || !result.isGameOver()) {
                result = game.guess(letter);
            }
        }
        assertThat(result).isNotNull();
        assertThat(result.gameResult()).isEqualTo(GameResult.WON);
        assertThat(result.currentState()).isEqualTo(word.value());
    }

    @Test
    void shouldLoseGameWithIncorrectGuesses() {
        // Arrange
        Optional<Word> wordOpt = wordRepository.getRandomWord();
        assertThat(wordOpt).isPresent();

        Word word = wordOpt.get();
        HangmanGame game = new HangmanGame(word, 3); // Малое количество попыток

        // Act - делаем только неправильные попытки
        GuessResult result = null;
        String wrongLetters = "ъыьэюящ"; // Буквы, которых вряд ли нет в словах

        for (int i = 0; i < wrongLetters.length() && (result == null || !result.isGameOver()); i++) {
            result = game.guess(wrongLetters.charAt(i));
        }

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.gameResult()).isEqualTo(GameResult.LOST);
        assertThat(result.attemptsMade()).isEqualTo(3);
    }

    @Test
    void shouldHandleMixedCorrectAndIncorrectGuesses() {
        // Arrange
        Word testWord = new Word("кот", Category.ANIMALS, Difficulty.EASY);
        HangmanGame game = new HangmanGame(testWord, 5);

        // Act - смешанные правильные и неправильные попытки
        GuessResult result1 = game.guess('к'); // правильно
        GuessResult result2 = game.guess('х'); // неправильно
        GuessResult result3 = game.guess('о'); // правильно
        GuessResult result4 = game.guess('ы'); // неправильно
        GuessResult result5 = game.guess('т'); // правильно - победа

        // Assert
        assertThat(result1.currentState()).isEqualTo("к**");
        assertThat(result1.attemptsMade()).isEqualTo(0);

        assertThat(result2.currentState()).isEqualTo("к**");
        assertThat(result2.attemptsMade()).isEqualTo(1);

        assertThat(result3.currentState()).isEqualTo("ко*");
        assertThat(result3.attemptsMade()).isEqualTo(1);

        assertThat(result4.currentState()).isEqualTo("ко*");
        assertThat(result4.attemptsMade()).isEqualTo(2);

        assertThat(result5.currentState()).isEqualTo("кот");
        assertThat(result5.gameResult()).isEqualTo(GameResult.WON);
    }

    @Test
    void shouldNotAllowActionsAfterGameEnd() {
        // Arrange
        Word testWord = new Word("кот", Category.ANIMALS, Difficulty.EASY);
        HangmanGame game = new HangmanGame(testWord, 1); // Только одна попытка

        // Act - проигрываем игру
        game.guess('х'); // Неправильная буква - игра окончена
        GuessResult afterGameResult = game.guess('к'); // Пытаемся продолжить

        // Assert
        assertThat(afterGameResult.message()).contains("уже завершена");
    }

    @Test
    void shouldValidateWordConstraints() {
        // Arrange & Act & Assert - слова из репозитория должны соответствовать ограничениям
        wordRepository.getAllWords().forEach(word -> {
            assertThat(word.length()).isGreaterThanOrEqualTo(2);
            assertThat(word.value()).isNotBlank();
            assertThat(word.value()).isEqualTo(word.value().toLowerCase());
            assertThat(word.category()).isNotNull();
            assertThat(word.difficulty()).isNotNull();
        });
    }

    @Test
    void shouldSupportDifferentDifficultyLevels() {
        // Act & Assert - проверяем что можем получить слова разной сложности
        for (Difficulty difficulty : Difficulty.values()) {
            for (Category category : Category.values()) {
                Optional<Word> wordOpt = wordRepository.getRandomWord(category, difficulty);
                // Если слово найдено, проверяем соответствие параметрам
                wordOpt.ifPresent(word -> {
                    assertThat(word.difficulty()).isEqualTo(difficulty);
                    assertThat(word.category()).isEqualTo(category);

                    // Создаем игру и проверяем что она работает
                    HangmanGame game = new HangmanGame(word, 5);
                    GuessResult result = game.guess('а'); // Пробуем любую букву
                    assertThat(result).isNotNull();
                    assertThat(result.maxAttempts()).isEqualTo(5);
                });
            }
        }
    }

    @Test
    void shouldMaintainGameStateConsistency() {
        // Arrange
        Word testWord = new Word("слово", Category.ANIMALS, Difficulty.MEDIUM);
        HangmanGame game = new HangmanGame(testWord, 7);

        // Act & Assert - проверяем состояние на каждом шаге
        GuessResult result = game.guess('с');
        assertThat(result.currentState()).isEqualTo("с****");
        assertThat(result.gameResult()).isEqualTo(GameResult.IN_PROGRESS);

        result = game.guess('л');
        assertThat(result.currentState()).isEqualTo("сл***");
        assertThat(result.gameResult()).isEqualTo(GameResult.IN_PROGRESS);

        result = game.guess('о');
        assertThat(result.currentState()).isEqualTo("сло*о");
        assertThat(result.gameResult()).isEqualTo(GameResult.IN_PROGRESS);

        result = game.guess('в');
        assertThat(result.currentState()).isEqualTo("слово");
        assertThat(result.gameResult()).isEqualTo(GameResult.WON);
        assertThat(result.isGameOver()).isTrue();
    }
}
