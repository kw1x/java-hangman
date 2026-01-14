package academy.hangman.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GuessResultTest {

    @Test
    void shouldCreateGuessResultCorrectly() {
        GuessResult result = new GuessResult("к**", 2, 6, GameResult.IN_PROGRESS, "Попробуйте еще");
        assertThat(result.currentState()).isEqualTo("к**");
        assertThat(result.attemptsMade()).isEqualTo(2);
        assertThat(result.maxAttempts()).isEqualTo(6);
        assertThat(result.gameResult()).isEqualTo(GameResult.IN_PROGRESS);
        assertThat(result.message()).isEqualTo("Попробуйте еще");
    }

    @Test
    void shouldCalculateRemainingAttemptsCorrectly() {
        GuessResult result = new GuessResult("**", 3, 7, GameResult.IN_PROGRESS, "Продолжайте");
        assertThat(result.remainingAttempts()).isEqualTo(4);
    }

    @Test
    void shouldDetectGameOverForWonState() {
        GuessResult result = new GuessResult("слово", 2, 5, GameResult.WON, "Победа!");
        assertThat(result.isGameOver()).isTrue();
    }

    @Test
    void shouldDetectGameOverForLostState() {
        GuessResult result = new GuessResult("***", 5, 5, GameResult.LOST, "Поражение");
        assertThat(result.isGameOver()).isTrue();
    }

    @Test
    void shouldNotDetectGameOverForInProgressState() {
        GuessResult result = new GuessResult("к*т", 2, 5, GameResult.IN_PROGRESS, "Продолжайте");
        assertThat(result.isGameOver()).isFalse();
    }

    @Test
    void shouldFormatForTestCorrectlyForWin() {
        // Act
        GuessResult result = new GuessResult("кот", 3, 6, GameResult.WON, "Победа!");

        // Assert
        assertThat(result.formatForTest()).isEqualTo("кот;POS");
    }

    @Test
    void shouldFormatForTestCorrectlyForLoss() {
        // Act
        GuessResult result = new GuessResult("к**", 6, 6, GameResult.LOST, "Поражение");

        // Assert
        assertThat(result.formatForTest()).isEqualTo("к**;NEG");
    }

    @Test
    void shouldFormatForTestCorrectlyForInProgress() {
        // Act
        GuessResult result = new GuessResult("к*т", 2, 6, GameResult.IN_PROGRESS, "Продолжайте");

        // Assert
        assertThat(result.formatForTest()).isEqualTo("к*т;NEG");
    }

    @Test
    void shouldHandleZeroRemainingAttempts() {
        // Act
        GuessResult result = new GuessResult("***", 5, 5, GameResult.LOST, "Поражение");

        // Assert
        assertThat(result.remainingAttempts()).isEqualTo(0);
        assertThat(result.isGameOver()).isTrue();
    }

    @Test
    void shouldHandleFullAttemptsAvailable() {
        // Act
        GuessResult result = new GuessResult("***", 0, 8, GameResult.IN_PROGRESS, "Начинаем");

        // Assert
        assertThat(result.remainingAttempts()).isEqualTo(8);
        assertThat(result.isGameOver()).isFalse();
    }
}
