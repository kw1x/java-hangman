package academy.hangman.model;

public enum Difficulty {
    EASY(7),
    MEDIUM(5),
    HARD(3);

    private final int maxAttempts;

    Difficulty(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}
