import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://adventofcode.com/2021/day/6
 */
public class LaternfishSimulator {
    private final List<Laternfish> school = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // Data
            int[] exampleAges = parse("3,4,3,1,2");
            InputStream inputStream = LaternfishSimulator.class.getClassLoader().getResourceAsStream("input.txt");
            String input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            int[] ages = parse(input.trim());

            // Part one Example
            LaternfishSimulator example = new LaternfishSimulator(exampleAges);
            long exampleResult = example.simulate(80);
            assertExample("Part one example", 5934, exampleResult);

            // Part one
            LaternfishSimulator simulator = new LaternfishSimulator(ages);
            long result = simulator.simulate(80);
            System.out.println("Part one - total laternfish after 80 days: " + result);

            // Part two example
            long longExampleResult = example.simulate(256 - 80);
            assertExample("Part two example", 26_984_457_539L, longExampleResult);

            // Part two
            long longResult = simulator.simulate(256 - 80);
            System.out.println("Part two - total laternfish after 256 days: " + longResult);
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

    private static int[] parse(String text) {
        return Arrays.stream(text.split(",")).mapToInt(Integer::valueOf).toArray();
    }

    public LaternfishSimulator(int[] ages) {
        Map<Integer, Laternfish> fishByAges = new HashMap<>();
        for (int age : ages) {
            fishByAges.compute(age, (key, val) -> {
                if (val == null) {
                    return new Laternfish(age, 1);
                }
                val.incrementTotal();
                return val;
            });
        }
        school.addAll(fishByAges.values());
    }

    public long simulate(int days) {
        for (int i = 0; i < days; i++) {
            List<Laternfish> newFish = new ArrayList<>();
            for (Laternfish fish : school) {
                Laternfish result = fish.cycle();
                if (result != null) {
                    newFish.add(result);
                }
            }
            school.addAll(newFish);
            dedupSchool();
        }

        long total = 0L;
        for (Laternfish fish : school) {
            total += fish.getTotal();
        }
        return total;
    }

    private void dedupSchool() {
        Map<Integer, Laternfish> fishByAges = new HashMap<>();
        for (Laternfish fish : school) {
            fishByAges.compute(fish.getTimer(), (key, val) -> {
                if (val == null) {
                    return fish;
                }
                val.addToTotal(fish.getTotal());
                return val;
            });
        }

        school.clear();
        school.addAll(fishByAges.values());
    }
}
