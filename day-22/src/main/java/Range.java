import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Range(int min, int max) {
    public IntStream toIntStream() {
        return IntStream.range(min, max + 1);
    }

    public long length() {
        return Math.abs(max - min) + 1;
    }

    public boolean contains(Range range) {
        return min <= range.min && max >= range.max();
    }

    public boolean intersects(Range range) {
        return max >= range.min() && min <= range.max();
    }

    public Range intersection(Range range) {
        return new Range(
            Math.max(min, range.min),
            Math.min(max, range.max)
        );
    }

    public Set<Range> remove(Range range) {
        Set<Range> parts = new HashSet<>();

        if (!this.contains(range)) {
            throw new UnsupportedOperationException("Cannot remove range from a range that does not contain it.");
        }

        if (min < range.min) {
            parts.add(new Range(min, range.min - 1));
        }

        if (max > range.max) {
            parts.add(new Range(range.max + 1, max));
        }

        return parts.stream()
            .filter((part) -> part.length() > 0)
            .collect(Collectors.toSet());
    }
}
