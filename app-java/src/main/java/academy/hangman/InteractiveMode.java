package academy.hangman;

import academy.hangman.game.HangmanGame;
import academy.hangman.model.*;
import academy.hangman.repository.InMemoryWordRepository;
import academy.hangman.repository.WordRepository;
import academy.hangman.ui.ConsoleInput;
import academy.hangman.ui.HangmanRenderer;
import java.util.Random;

public class InteractiveMode {
    private final WordRepository wordRepository;
    private final ConsoleInput input;
    private final HangmanRenderer renderer;
    private final Random random;

    public InteractiveMode() {
        this.wordRepository = new InMemoryWordRepository();
        this.input = new ConsoleInput();
        this.renderer = new HangmanRenderer();
        this.random = new Random();
    }

    public void run() {
        renderer.displayWelcome();

        do {
            playGame();
        } while (input.readYesNo("\nХотите сыграть еще раз?"));

        System.out.println("\nСпасибо за игру! До свидания!");
        input.close();
    }

    private void playGame() {
        Category category = selectCategory();
        Difficulty difficulty = selectDifficulty();

        Word word = wordRepository.getRandomWord(category, difficulty).orElseGet(() -> wordRepository
                .getRandomWord()
                .orElseThrow(() -> new RuntimeException("Нет доступных слов")));

        System.out.println("\n🎮 Начинаем игру!");
        System.out.println("Категория: " + word.category().getDisplayName());
        System.out.println("Сложность: " + word.difficulty());
        System.out.println("Максимум попыток: " + word.difficulty().getMaxAttempts());

        if (word.hint() != null) {
            boolean wantHint = input.readYesNo("\nХотите получить подсказку?");
            if (wantHint) {
                System.out.println("💡 Подсказка: " + word.hint());
            }
        }

        HangmanGame game = new HangmanGame(word, word.difficulty().getMaxAttempts());

        while (!game.getSession().isGameOver()) {
            GuessResult result = game.getCurrentResult();
            renderer.displayGameState(result);

            char letter = input.readLetter();
            result = game.guess(letter);

            if (result.isGameOver()) {
                renderer.displayGameOver(result, word.value());
            } else {
                renderer.displayMessage(result.message());
            }
        }
    }

    private Category selectCategory() {
        System.out.println("\nВыберите категорию:");
        Category[] categories = Category.values();

        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getDisplayName());
        }
        System.out.println("0. Случайная категория");

        int choice = input.readInt("Ваш выбор: ", 0, categories.length);

        if (choice == 0) {
            return categories[random.nextInt(categories.length)];
        }

        return categories[choice - 1];
    }

    private Difficulty selectDifficulty() {
        System.out.println("\nВыберите уровень сложности:");
        Difficulty[] difficulties = Difficulty.values();

        for (int i = 0; i < difficulties.length; i++) {
            System.out.println(
                    (i + 1) + ". " + difficulties[i] + " (" + difficulties[i].getMaxAttempts() + " попыток)");
        }
        System.out.println("0. Случайная сложность");

        int choice = input.readInt("Ваш выбор: ", 0, difficulties.length);

        if (choice == 0) {
            return difficulties[random.nextInt(difficulties.length)];
        }

        return difficulties[choice - 1];
    }
}
