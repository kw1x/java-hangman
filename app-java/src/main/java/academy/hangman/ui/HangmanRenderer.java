package academy.hangman.ui;

import academy.hangman.model.GuessResult;

public class HangmanRenderer {
    private static final int SEPARATOR_LENGTH = 50;
    private static final String[] HANGMAN_STAGES = {
        """
         +---+
         |   |
             |
             |
             |
             |
        =========
        """,
        """
         +---+
         |   |
         O   |
             |
             |
             |
        =========
        """,
        """
         +---+
         |   |
         O   |
         |   |
             |
             |
        =========
        """,
        """
         +---+
         |   |
         O   |
        /|   |
             |
             |
        =========
        """,
        """
         +---+
         |   |
         O   |
        /|\\  |
             |
             |
        =========
        """,
        """
         +---+
         |   |
         O   |
        /|\\  |
        /    |
             |
        =========
        """,
        """
         +---+
         |   |
         O   |
        /|\\  |
        / \\  |
             |
        =========
        """
    };

    public String renderHangman(int attempts, int maxAttempts) {
        if (attempts >= maxAttempts) {
            return HANGMAN_STAGES[HANGMAN_STAGES.length - 1];
        }

        // Вычисляем индекс стадии пропорционально количеству ошибок
        int stageIndex = (int) ((double) attempts / maxAttempts * (HANGMAN_STAGES.length - 1));
        return HANGMAN_STAGES[Math.min(stageIndex, HANGMAN_STAGES.length - 1)];
    }

    public void displayGameState(GuessResult result) {
    
        System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
        System.out.println(renderHangman(result.attemptsMade(), result.maxAttempts()));
        System.out.println("Слово: " + formatWord(result.currentState()));
        System.out.println("Попыток осталось: " + result.remainingAttempts() + " из " + result.maxAttempts());
        System.out.println("=".repeat(SEPARATOR_LENGTH));
    }

    public void displayMessage(String message) {
        System.out.println("\n>>> " + message);
    }

    public void displayWelcome() {
        System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
        System.out.println("         ДОБРО ПОЖАЛОВАТЬ В ИГРУ 'ВИСЕЛИЦА'");
        System.out.println("=".repeat(SEPARATOR_LENGTH));
    }

    public void displayGameOver(GuessResult result, String word) {
        System.out.println("\n" + "=".repeat(SEPARATOR_LENGTH));
        System.out.println(renderHangman(result.attemptsMade(), result.maxAttempts()));

        switch (result.gameResult()) {
            case WON -> {
                System.out.println("🎉 ПОЗДРАВЛЯЕМ! ВЫ ВЫИГРАЛИ! 🎉");
                System.out.println("Загаданное слово: " + word.toUpperCase());
            }
            case LOST -> {
                System.out.println("💀 ВЫ ПРОИГРАЛИ! 💀");
                System.out.println("Загаданное слово было: " + word.toUpperCase());
            }
            default -> {}
        }

        System.out.println("=".repeat(SEPARATOR_LENGTH) + "\n");
    }

    private String formatWord(String word) {
        return word.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .reduce((a, b) -> a + " " + b)
                .orElse("")
                .toUpperCase();
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
