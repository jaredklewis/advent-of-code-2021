import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            // Part one example
            Paper partOneExample = Paper.load("example.txt");
            partOneExample.doFirstFold();
            assertExample("Part one example", 17, partOneExample.countDots());

            // Part one
            Paper partOnePaper = Paper.load("input.txt");
            partOnePaper.doFirstFold();
            System.out.println("Part one - dots after first fold: " + partOnePaper.countDots());

            // Part two example
            Paper example = Paper.load("example.txt");
            example.performAllFolds();
            System.out.println(example.visualize());

            // Part two
            Paper paper = Paper.load("input.txt");
            paper.performAllFolds();
            System.out.println(paper.visualize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void assertExample(String name, long expected, long actual) {
        if (actual != expected) {
            throw new IllegalStateException(
                String.format("%s incorrect. Expected: %d - Actual: %d", name, expected, actual)
            );
        }
    }

    public static int doSomething() {
        return 0;
    }

    private static List<String> loadInput(String name) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(name);
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return text.lines().map(String::strip).collect(Collectors.toList());
    }
}
