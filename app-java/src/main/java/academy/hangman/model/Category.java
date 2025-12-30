package academy.hangman.model;

public enum Category {
    ANIMALS("Животные"),
    FRUITS("Фрукты"),
    COUNTRIES("Страны"),
    PROFESSIONS("Профессии");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
