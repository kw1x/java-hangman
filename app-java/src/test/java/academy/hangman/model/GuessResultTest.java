package academy.hangman.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GuessResultTest {

    @ParameterizedTest
    @CsvSource({
    "к**,2,6,IN_PROGRESS,Попробуйте еще",
    "кот,3,6,WON,Победа!",
    "***,5,5,LOST,Поражение"
})
    void shouldCreateGuessResultCorrectly(String state, int made, int max, String result, String message) {
        GuessResult gr = new GuessResult(state, made, max, GameResult.valueOf(result), message);
        assertThat(gr.currentState()).isEqualTo(state);
        assertThat(gr.attemptsMade()).isEqualTo(made);
        assertThat(gr.maxAttempts()).isEqualTo(max);
        assertThat(gr.gameResult()).isEqualTo(GameResult.valueOf(result));
        assertThat(gr.message()).isEqualTo(message);
    }

    @ParameterizedTest
    @CsvSource({
        "**,3,7,IN_PROGRESS,Продолжайте,4",
        "***,0,8,IN_PROGRESS,Начинаем,8",
        "***,5,5,LOST,Поражение,0"
    })
    void shouldCalculateRemainingAttemptsCorrectly(String state, int made, int max, String result, String message, int expected) {
        GuessResult gr = new GuessResult(state, made, max, GameResult.valueOf(result), message);
        assertThat(gr.remainingAttempts()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "слово,2,5,WON,Победа!,true",
        "***,5,5,LOST,Поражение,true",
        "к*т,2,5,IN_PROGRESS,Продолжайте,false"
    })
    void shouldDetectGameOverCorrectly(String state, int made, int max, String result, String message, boolean expected) {
        GuessResult gr = new GuessResult(state, made, max, GameResult.valueOf(result), message);
        assertThat(gr.isGameOver()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "кот,3,6,WON,Победа!,кот;POS",
        "к**,6,6,LOST,Поражение,к**;NEG",
        "к*т,2,6,IN_PROGRESS,Продолжайте,к*т;NEG"
    })
    void shouldFormatForTestCorrectly(String state, int made, int max, String result, String message, String expected) {
        GuessResult gr = new GuessResult(state, made, max, GameResult.valueOf(result), message);
        assertThat(gr.formatForTest()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "***,5,5,LOST,Поражение,0,true",
        "***,0,8,IN_PROGRESS,Начинаем,8,false"
    })
    void shouldHandleAttemptStates(String state, int made, int max, String result, String message, int expectedRemaining, boolean expectedGameOver) {
        GuessResult gr = new GuessResult(state, made, max, GameResult.valueOf(result), message);
        assertThat(gr.remainingAttempts()).isEqualTo(expectedRemaining);
        assertThat(gr.isGameOver()).isEqualTo(expectedGameOver);
    }
}
