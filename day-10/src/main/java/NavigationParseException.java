public class NavigationParseException extends Exception{
    private final char character;

    public NavigationParseException(char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }
}
