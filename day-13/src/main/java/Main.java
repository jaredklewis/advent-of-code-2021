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
}
