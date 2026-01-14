package academy.hangman.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.GameResult;
import academy.hangman.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameSessionTest {

    private GameSession session;
    private Word testWord;

    @BeforeEach
    void setUp() {
        testWord = new Word("слон", Category.ANIMALS, Difficulty.EASY);
        session = new GameSession(testWord, 5);
    }

    @Test
    void shouldInitializeCorrectly() {
        assertThat(session.getWord()).isEqualTo(testWord);
        assertThat(session.getMaxAttempts()).isEqualTo(5);
        assertThat(session.getAttemptsMade()).isEqualTo(0);
        assertThat(session.getRemainingAttempts()).isEqualTo(5);
        assertThat(session.getCurrentState()).isEqualTo("****");
        assertThat(session.getGameResult()).isEqualTo(GameResult.IN_PROGRESS);
        assertThat(session.isGameOver()).isFalse();
    }

    @Test
    void shouldThrowExceptionForInvalidWord() {
        assertThatThrownBy(() -> new GameSession(null, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Word cannot be null");
    }

    @Test
    void shouldThrowExceptionForInvalidMaxAttempts() {
        assertThatThrownBy(() -> new GameSession(testWord, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Max attempts must be positive");
        assertThatThrownBy(() -> new GameSession(testWord, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Max attempts must be positive");
    }

    @Test
    void shouldCorrectlyHandleCorrectGuess() {
        boolean result = session.makeGuess('с');
        assertThat(result).isTrue();
        assertThat(session.getAttemptsMade()).isEqualTo(0);
        assertThat(session.getCurrentState()).isEqualTo("с***");
        assertThat(session.getGuessedLetters()).contains('с');
    }

    @Test
    void shouldCorrectlyHandleIncorrectGuess() {
        boolean result = session.makeGuess('х');
        assertThat(result).isFalse();
        assertThat(session.getAttemptsMade()).isEqualTo(1);
        assertThat(session.getCurrentState()).isEqualTo("****");
        assertThat(session.getRemainingAttempts()).isEqualTo(4);
    }

    @Test
    void shouldHandleLettersRegardlessOfCase() {
        boolean upperResult = session.makeGuess('С');
        boolean lowerResult = session.makeGuess('л');
        assertThat(upperResult).isTrue();
        assertThat(lowerResult).isTrue();
        assertThat(session.getCurrentState()).isEqualTo("сл**");
        assertThat(session.getGuessedLetters()).contains('с', 'л');
    }

    @Test
    void shouldNotCountRepeatedGuesses() {
        session.makeGuess('с');
        boolean secondGuess = session.makeGuess('с');
        assertThat(secondGuess).isFalse();
        assertThat(session.getAttemptsMade()).isEqualTo(0);
    }

    @Test
    void shouldWinWhenAllLettersGuessed() {
        session.makeGuess('с');
        session.makeGuess('л');
        session.makeGuess('о');
        session.makeGuess('н');
        assertThat(session.getCurrentState()).isEqualTo("слон");
        assertThat(session.getGameResult()).isEqualTo(GameResult.WON);
        assertThat(session.isGameOver()).isTrue();
    }

    @Test
    void shouldLoseAfterMaxAttempts() {
        session.makeGuess('х');
        session.makeGuess('ы');
        session.makeGuess('щ');
        session.makeGuess('ъ');
        session.makeGuess('ю');
        assertThat(session.getAttemptsMade()).isEqualTo(5);
        assertThat(session.getGameResult()).isEqualTo(GameResult.LOST);
        assertThat(session.isGameOver()).isTrue();
        assertThat(session.getRemainingAttempts()).isEqualTo(0);
    }

    @Test
    void shouldTrackGuessedLettersCorrectly() {
        // Act
        session.makeGuess('с');
        session.makeGuess('х'); // incorrect
        session.makeGuess('л');

        // Assert
        assertThat(session.getGuessedLetters()).containsExactlyInAnyOrder('с', 'х', 'л');
    }
}
