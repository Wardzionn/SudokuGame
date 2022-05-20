public enum Difficulty {

    EASY(45),

    MEDIUM(55),

    HARD(65);
    private int value;

    Difficulty(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
