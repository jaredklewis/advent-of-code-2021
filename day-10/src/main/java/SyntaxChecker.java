import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class SyntaxChecker {
    private final List<String> lines;

    public static void main(String[] args) {
        try {
            // Data
            List<String> example = loadInput("example.txt");
            List<String> input = loadInput("input.txt");

            // Part one example
            SyntaxChecker exampleChecker = new SyntaxChecker(example);
            int exampleErrorScore = exampleChecker.scoreErrors();
            if (exampleErrorScore != 26397) {
                throw new AssertionError("Part one example incorrect, got score of " + exampleErrorScore);
            }

            // Part one
            SyntaxChecker checker = new SyntaxChecker(input);
            int errorScore = checker.scoreErrors();
            System.out.println("Part one - error score: " + errorScore);

            // Part two example
            long exampleCompletionScore = exampleChecker.scoreCompletion();
            if (exampleCompletionScore != 288957) {
                throw new AssertionError("Part one example incorrect, got score of " + exampleCompletionScore);
            }

            // Part two
            long completionScore = checker.scoreCompletion();
            System.out.println("Part two - completion score: " + completionScore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public SyntaxChecker(List<String> lines) {
        this.lines = lines;
    }

    public int scoreErrors() {
        int score = 0;
        for (String line : lines) {
            try {
                checkLine(line);
            } catch (NavigationParseException e) {
                Delimiter delimiter = Delimiter.fromChar(e.getCharacter());
                score += delimiter.getErrorPoints();
            }
        }
        return score;
    }

    private long scoreCompletion() {
        List<Long> scores = new ArrayList<>();
        for (String line : lines) {
            long score = 0;
            try {
                List<Delimiter> completion = checkLine(line);
                for (Delimiter delimiter : completion) {
                    score *= 5;
                    score += delimiter.getCompletionPoints();
                }
                scores.add(score);
            } catch (NavigationParseException ignored) {
            }
        }
        Collections.sort(scores);

        return scores.get(Math.floorDiv(scores.size(), 2));
    }

    public List<Delimiter> checkLine(String line) throws NavigationParseException {
        Stack<Delimiter> stack = new Stack<>();
        String open = "([{<";
        String close = ")]}>";
        char[] chars = line.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (open.indexOf(c) >= 0) {
                stack.push(Delimiter.fromChar(c));
            } else if (close.indexOf(c) >= 0) {
                Delimiter popped = stack.pop();
                if (popped.getClose() != c) {
                    throw new NavigationParseException(c);
                }
            } else {
                throw new UnsupportedOperationException("Unsupported character " + c);
            }
        }

        List<Delimiter> completion = new ArrayList<>(stack.size());
        while (!stack.isEmpty()) {
            completion.add(stack.pop());
        }

        return completion;
    }

    private static List<String> loadInput(String name) throws IOException {
        InputStream inputStream = SyntaxChecker.class.getClassLoader().getResourceAsStream(name);
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return text.lines().map(String::strip).collect(Collectors.toList());
    }
}
