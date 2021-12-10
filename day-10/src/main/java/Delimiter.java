enum Delimiter {
    PARENTHESES(')', 3, 1),
    BRACKETS(']', 57, 2),
    CURLY_BRACKETS('}', 1197, 3),
    ANGLE_BRACKETS('>', 25137, 4);

    public static Delimiter fromChar(char c) {
        return switch (c) {
            case '(', ')' -> Delimiter.PARENTHESES;
            case '[', ']' -> Delimiter.BRACKETS;
            case '{', '}' -> Delimiter.CURLY_BRACKETS;
            case '<', '>' -> Delimiter.ANGLE_BRACKETS;
            default -> throw new IllegalArgumentException("Unsupported delimiter " + c);
        };
    }

    private final char close;
    private final int errorPoints;
    private final int completionPoints;

    Delimiter(char close, int illegalPoints, int completionPoints) {
        this.close = close;
        this.errorPoints = illegalPoints;
        this.completionPoints = completionPoints;
    }

    public char getClose() {
        return close;
    }

    public int getErrorPoints() {
        return errorPoints;
    }

    public int getCompletionPoints() {
        return completionPoints;
    }
}
