import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Paper {
    private final List<FoldInstruction> foldInstructions;
    private List<List<Cell>> grid;

    public static Paper load(String resource) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(resource);
        String text = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        String[] parts = text.split("\n\n");

        // Fold Instructions
        Pattern foldPattern = Pattern.compile("^fold along ([xy])=([0-9]+)$");
        List<FoldInstruction> foldInstructions = parts[1].lines().map((line) -> {
            Matcher matcher = foldPattern.matcher(line);
            if (!matcher.find()) {
                throw new UnsupportedOperationException("Unable to parse fold instructions " + line);
            }

            return new FoldInstruction(
                matcher.group(1).equals("x") ? FoldInstruction.Type.VerticalLine : FoldInstruction.Type.HorizontalLine,
                Integer.parseInt(matcher.group(2))
            );
        }).collect(Collectors.toList());

        // Dots
        List<Dot> dots = parts[0].lines().map((line) -> {
            String[] coordinates = line.split(",");
            return new Dot(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        }).toList();

        // Calculate the width and height of the grid
        // For whatever reason, the actual size of the transparent paper
        // cannot be ascertained solely from the dot coordinates, and
        // it seems my paper was a few rows longer than the bottom-most coordinate.
        // So we can get the paper size from the folds, which always seem to be
        // in the middle.
        FoldInstruction firstVerticalLineFold = foldInstructions
            .stream()
            .filter((instruction) -> instruction.type() == FoldInstruction.Type.VerticalLine)
            .findFirst()
            .orElseThrow();

        FoldInstruction firstHorizontalLineFold = foldInstructions
            .stream()
            .filter((instruction) -> instruction.type() == FoldInstruction.Type.HorizontalLine)
            .findFirst()
            .orElseThrow();

        int width = (firstVerticalLineFold.value() * 2) + 1;

        int height = (firstHorizontalLineFold.value() * 2) + 1;

        // Build the grid
        Dot[][] dotGrid = new Dot[height][width];
        for (Dot dot : dots) {
            dotGrid[dot.y][dot.x] = dot;
        }

        List<List<Cell>> grid = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            List<Cell> row = new ArrayList<>(width);
            for (int j = 0; j < width; j++) {
                if (dotGrid[i][j] != null) {
                    row.add(new Cell(true));
                } else {
                    row.add(new Cell(false));
                }
            }
            grid.add(row);
        }

        return new Paper(foldInstructions, grid);
    }

    public Paper(List<FoldInstruction> foldInstructions, List<List<Cell>> grid) {
        this.foldInstructions = foldInstructions;
        this.grid = grid;
    }

    @Override
    public String toString() {
        return "Paper{" +
            "foldInstructions=" + foldInstructions +
            '}';
    }

    public String visualize() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        for (List<Cell> row : grid) {
            for (Cell cell : row) {
                sb.append(cell.hasDot ? "░" : " ");
            }
            sb.append("\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    public void doFirstFold() {
        FoldInstruction instruction = foldInstructions.get(0);
        performFold(instruction);
    }

    public void performAllFolds() {
        for (FoldInstruction foldInstruction : foldInstructions) {
            performFold(foldInstruction);
        }
    }

    private void performFold(FoldInstruction instruction) {
        if (instruction.type() == FoldInstruction.Type.VerticalLine) {
            List<List<Cell>> left = new ArrayList<>(grid.size());

            for (List<Cell> row : grid) {
                List<Cell> reversedRightRow = row.subList(instruction.value() + 1, row.size());
                Collections.reverse(reversedRightRow);
                List<Cell> leftRow = row.subList(0, instruction.value());

                for (int j = 0; j < leftRow.size(); j++) {
                    Cell cell = leftRow.get(j);
                    if (!cell.hasDot && reversedRightRow.get(j).hasDot) {
                        leftRow.set(j, new Cell(true));
                    }
                }

                left.add(leftRow);
            }
            grid = left;

        } else {
            List<List<Cell>> top = grid.subList(0, instruction.value());
            List<List<Cell>> reversedBottom = grid.subList(instruction.value() + 1, grid.size());
            Collections.reverse(reversedBottom);

            for (int i = 0; i < top.size(); i++) {
                List<Cell> topRow = top.get(i);

                List<Cell> bottomRow = reversedBottom.get(i);

                for (int j = 0; j < topRow.size(); j++) {
                    Cell cell = topRow.get(j);
                    if (!cell.hasDot && bottomRow.get(j).hasDot) {
                        topRow.set(j, new Cell(true));
                    }
                }
            }

            grid = top;
        }
    }

    public int countDots() {
        return grid.stream().mapToInt((row) -> row.stream().mapToInt((cell) -> cell.hasDot ? 1 : 0).sum()).sum();
    }

    private record Dot(int x, int y) {}

    private record Cell(boolean hasDot) {}

    private record FoldInstruction(Type type, int value) {
        enum Type {
            VerticalLine, HorizontalLine
        }
    }
}
