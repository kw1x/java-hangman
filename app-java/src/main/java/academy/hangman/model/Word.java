package academy.hangman.model;

public record Word(String value, Category category, Difficulty difficulty, String hint) {
    public Word {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        if (value.length() < 2) {
            throw new IllegalArgumentException("Word must have at least 2 characters");
        }
    }

    public Word(String value, Category category, Difficulty difficulty) {
        this(value.toLowerCase(), category, difficulty, null);
    }

    public int length() {
        return value.length();
    }

    public char charAt(int index) {
        return value.charAt(index);
    }

    public boolean contains(char letter) {
        return value.indexOf(Character.toLowerCase(letter)) >= 0;
    }
}
