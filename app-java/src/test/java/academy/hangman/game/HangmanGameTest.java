package academy.hangman.game;

import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.GameResult;
import academy.hangman.model.GuessResult;
import academy.hangman.model.Word;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HangmanGameTest {
    
    private HangmanGame game;
    private Word testWord;
    
    @BeforeEach
    void setUp() {
        testWord = new Word("тест", Category.ANIMALS, Difficulty.EASY);
        game = new HangmanGame(testWord, 6);
    }
    
    @Test
    void shouldCorrectlyProcessGuessedLetter() {
        GuessResult result = game.guess('т');
        assertThat(result.currentState()).contains("т");
        assertThat(result.attemptsMade()).isEqualTo(0);
        assertThat(result.gameResult()).isEqualTo(GameResult.IN_PROGRESS);
        assertThat(result.message()).contains("Верно");
    }
    
    @Test
    void shouldCorrectlyProcessWrongLetter() {
        GuessResult result = game.guess('х');
        assertThat(result.currentState()).isEqualTo("****");
        assertThat(result.attemptsMade()).isEqualTo(1);
        assertThat(result.remainingAttempts()).isEqualTo(5);
        assertThat(result.gameResult()).isEqualTo(GameResult.IN_PROGRESS);
        assertThat(result.message()).contains("Неверно");
    }
    
    @Test
    void shouldHandleLettersRegardlessOfCase() {
        GuessResult upperResult = game.guess('Т');
        GuessResult lowerResult = game.guess('е');
        assertThat(upperResult.currentState()).contains("т");
        assertThat(lowerResult.currentState()).contains("е");
        assertThat(upperResult.attemptsMade()).isEqualTo(0);
        assertThat(lowerResult.attemptsMade()).isEqualTo(0);
    }
    
    @Test
    void shouldWinGameWhenAllLettersGuessed() {
        game.guess('т');
        game.guess('е');
        GuessResult finalResult = game.guess('с');
        assertThat(finalResult.currentState()).isEqualTo("тест");
        assertThat(finalResult.gameResult()).isEqualTo(GameResult.WON);
        assertThat(finalResult.message()).contains("Поздравляем");
    }
    
    @Test
    void shouldLoseGameAfterMaxAttempts() {
        game.guess('х');
        game.guess('ю');
        game.guess('щ');
        game.guess('ц');
        game.guess('й');
        GuessResult finalResult = game.guess('ъ');
        assertThat(finalResult.attemptsMade()).isEqualTo(6);
        assertThat(finalResult.gameResult()).isEqualTo(GameResult.LOST);
        assertThat(finalResult.message()).contains("проиграли");
    }
    
    @Test
    void shouldNotAllowActionsAfterGameOver() {
        game.guess('х');
        game.guess('ю');
        game.guess('щ');
        game.guess('ц');
        game.guess('й');
        game.guess('ъ');
        GuessResult result = game.guess('т');
        assertThat(result.message()).contains("уже завершена");
        assertThat(result.gameResult()).isEqualTo(GameResult.LOST);
    }
    
    @Test
    void shouldCorrectlyGuessWholeWord() {
        GuessResult result = game.guessWord("тест");
        assertThat(result.currentState()).isEqualTo("тест");
        assertThat(result.gameResult()).isEqualTo(GameResult.WON);
        assertThat(result.message()).contains("угадали слово");
    }
    
    @Test
    void shouldLoseWhenGuessingWrongWord() {
        GuessResult result = game.guessWord("неверно");
        assertThat(result.message()).contains("Неверно");
    }
    
    @Test
    void shouldHandleWordGuessRegardlessOfCase() {
        GuessResult result = game.guessWord("ТЕСТ");
        assertThat(result.gameResult()).isEqualTo(GameResult.WON);
        assertThat(result.message()).contains("угадали слово");
    }
}