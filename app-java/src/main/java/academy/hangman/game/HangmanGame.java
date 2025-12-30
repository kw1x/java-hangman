package academy.hangman.game;

import academy.hangman.model.GuessResult;
import academy.hangman.model.Word;

public class HangmanGame {
    private final GameSession session;

    public HangmanGame(Word word, int maxAttempts) {
        this.session = new GameSession(word, maxAttempts);
    }

    public GuessResult guess(char letter) {
        if (session.isGameOver()) {
            return createResult("Игра уже завершена");
        }

        boolean correct = session.makeGuess(letter);
        String message = buildMessage(letter, correct);

        return createResult(message);
    }

    public GuessResult guessWord(String word) {
        if (session.isGameOver()) {
            return createResult("Игра уже завершена");
        }

        String normalizedWord = word.toLowerCase();
        String targetWord = session.getWord().value();

        if (normalizedWord.equals(targetWord)) {
            // Отгадываем все буквы сразу
            for (char c : targetWord.toCharArray()) {
                if (!session.isLetterGuessed(c)) {
                    session.makeGuess(c);
                }
            }
            return createResult("Поздравляем! Вы угадали слово!");
        } else {
            // Неверная попытка угадать слово целиком считается ошибкой
            for (int i = 0; i < session.getMaxAttempts() - session.getAttemptsMade(); i++) {
                session.makeGuess('_'); // Используем невалидный символ для увеличения счетчика ошибок
            }
            return createResult("Неверно! Вы проиграли.");
        }
    }

    private String buildMessage(char letter, boolean correct) {
        if (correct) {
            if (session.getGameResult() == academy.hangman.model.GameResult.WON) {
                return "Поздравляем! Вы угадали слово!";
            }
            return "Верно! Буква '" + letter + "' есть в слове.";
        } else {
            if (session.getGameResult() == academy.hangman.model.GameResult.LOST) {
                return "Вы проиграли! Загаданное слово: " + session.getWord().value();
            }
            return "Неверно! Буквы '" + letter + "' нет в слове. Осталось попыток: " + session.getRemainingAttempts();
        }
    }

    private GuessResult createResult(String message) {
        return new GuessResult(
            session.getCurrentState(),
            session.getAttemptsMade(),
            session.getMaxAttempts(),
            session.getGameResult(),
            message
        );
    }

    public GuessResult getCurrentResult() {
        return createResult("Текущее состояние игры");
    }

    public GameSession getSession() {
        return session;
    }
}
