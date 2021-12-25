import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern STEP_PATTERN = Pattern.compile(
        "^(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)"
    );

    public static void main(String[] args) {
        try {
            // Data
            List<RebootStep> smallExampleSteps = loadRebootSteps("small-example.txt");
            List<RebootStep> exampleSteps = loadRebootSteps("example.txt");
            List<RebootStep> p2ExampleSteps = loadRebootSteps("p2-example.txt");
            List<RebootStep> steps = loadRebootSteps("input.txt");

            // Part one small example
            Grid smallExample = new Grid(50);
            smallExample.runSteps(smallExampleSteps);
            assertExample("Part one small example", 39, smallExample.countOn());

            // Part one example
            Grid example = new Grid(50);
            example.runSteps(exampleSteps);
            assertExample("Part one example", 590784, example.countOn());

            // Part one
            Grid grid = new Grid(50);
            grid.runSteps(steps);
            System.out.println("Part one - total on cubes: " + grid.countOn());

            // Part two small example
            VirtualGrid smallExampleVirtualGrid = new VirtualGrid();
            smallExampleVirtualGrid.runSteps(smallExampleSteps);
            assertExample("Part two small example", 39, smallExampleVirtualGrid.countOn());

            // Part two example
            VirtualGrid exampleVirtualGrid = new VirtualGrid();
            exampleVirtualGrid.runSteps(p2ExampleSteps);
            assertExample("Part two example", 2758514936282235L, exampleVirtualGrid.countOn());

            // Part two
            VirtualGrid virtualGrid = new VirtualGrid();
            virtualGrid.runSteps(steps);
            System.out.println("Part two - total on cubes: " + virtualGrid.countOn());
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

    private static List<RebootStep> loadRebootSteps(String resourceFile) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(resourceFile);
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        return Arrays.stream(text.split("\n")).filter((line) -> !line.isEmpty()).map((line) -> {
            Matcher m = STEP_PATTERN.matcher(line);

            if (!m.find()) {
                throw new IllegalStateException("Unable to parse reboot step: " + line);
            }

            return new RebootStep(
                m.group(1).equals("on"),
                parseIntRange(m.group(2), m.group(3)),
                parseIntRange(m.group(4), m.group(5)),
                parseIntRange(m.group(6), m.group(7))
            );
        }).collect(Collectors.toList());
    }

    private static Range parseIntRange(String from, String to) {
        return new Range(Integer.parseInt(from), Integer.parseInt(to));
    }
}
