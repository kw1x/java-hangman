package academy.hangman;

import academy.hangman.game.HangmanGame;
import academy.hangman.model.Category;
import academy.hangman.model.Difficulty;
import academy.hangman.model.GuessResult;
import academy.hangman.model.Word;

public class TestMode {

    public static String runTest(String targetWord, String guessedWord) {
        // Валидация входных данных
        if (targetWord == null || targetWord.isBlank()) {
            return "Unknown word";
        }

        if (guessedWord == null || guessedWord.isBlank()) {
            return "Unknown word";
        }

        targetWord = targetWord.toLowerCase().trim();
        guessedWord = guessedWord.toLowerCase().trim();

        // Проверка корректности длины слова
        if (targetWord.length() < 2) {
            return "Unknown word";
        }

        // Создаем слово с дефолтными параметрами
        Word word;
        try {
            word = new Word(targetWord, Category.ANIMALS, Difficulty.MEDIUM);
        } catch (IllegalArgumentException e) {
            return "Unknown word";
        }

        // Создаем игровую сессию
        HangmanGame game = new HangmanGame(word, Difficulty.MEDIUM.getMaxAttempts());

        // Эмулируем процесс угадывания
        for (char c : guessedWord.toCharArray()) {
            if (!game.getSession().isGameOver()) {
                game.guess(c);
            }
        }

        // Получаем результат
        GuessResult result = game.getCurrentResult();

        // Форматируем вывод согласно требованиям
        return result.formatForTest();
    }
}
