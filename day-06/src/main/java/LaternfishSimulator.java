import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        int[] exampleAges = parse("3,4,3,1,2");

        LaternfishSimulator example = new LaternfishSimulator(exampleAges);
        long exampleResult = example.simulate(80);

        if (exampleResult != 5934) {
            throw new AssertionError(String.format("Example incorrect: expected %d got %d", 5934, exampleResult));
        }

        InputStream inputStream = LaternfishSimulator.class.getClassLoader().getResourceAsStream("input.txt");
        String input = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        int[] ages = parse(input.trim());
        LaternfishSimulator simulator = new LaternfishSimulator(ages);
        long result = simulator.simulate(80);

        System.out.println("Part one - total laternfish after 80 days: " + result);

        long longExampleResult = example.simulate(256 - 80);
        if (longExampleResult != 26_984_457_539L) {
            throw new AssertionError(String.format("Example incorrect: expected %d got %d", 26_984_457_539L, longExampleResult));
        }

        long longResult = simulator.simulate(256 - 80);
        System.out.println("Part two - total laternfish after 256 days: " + longResult);
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
