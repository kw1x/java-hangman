package academy.hangman.model;

public record GuessResult(
        String currentState, int attemptsMade, int maxAttempts, GameResult gameResult, String message) {
    public int remainingAttempts() {
        return maxAttempts - attemptsMade;
    }

    public boolean isGameOver() {
        return gameResult != GameResult.IN_PROGRESS;
    }

    public String formatForTest() {
        String result = gameResult == GameResult.WON ? "POS" : "NEG";
        return currentState + ";" + result;
    }
}
