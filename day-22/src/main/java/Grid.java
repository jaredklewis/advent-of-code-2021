import java.util.List;

public class Grid {
    private final boolean[][][] cubes;
    private final int max;
    private final int size;

    public Grid(int max) {
        this.max = max;
        this.size = max * 2 + 1;
        this.cubes = new boolean[size][size][size];
    }

    public void runSteps(List<RebootStep> rebootSteps) {
        for (RebootStep step : rebootSteps) {
            int[] xIndices = buildIndexArray(step.xRange());
            int[] yIndices = buildIndexArray(step.yRange());
            int[] zIndices = buildIndexArray(step.zRange());

            for (int x : xIndices) {
                for (int y : yIndices) {
                    for (int z : zIndices) {
                        cubes[x][y][z] = step.on();
                    }
                }
            }
        }
    }

    public long countOn() {
        long count = 0L;
        for (boolean[][] layer : cubes) {
            for (boolean[] row : layer) {
                for (boolean col : row) {
                    if (col) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int coordToIndex(int coordinate) {
        return coordinate + this.max;
    }


    private int[] buildIndexArray(Range range) {
        return range.toIntStream()
            .map(this::coordToIndex)
            .filter((index) -> index >= 0 && index < this.size)
            .toArray();
    }
}
