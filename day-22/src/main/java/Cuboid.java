import java.util.HashSet;
import java.util.Set;

public record Cuboid(Range xRange, Range yRange, Range zRange) {
    public long getVolume() {
        return xRange.length() * yRange.length() * zRange.length();
    }

    public boolean contains(Cuboid cuboid) {
        return xRange.contains(cuboid.xRange) &&
            yRange.contains(cuboid.yRange) &&
            zRange.contains(cuboid.zRange);
    }

    public boolean intersects(Cuboid cuboid) {
        return xRange.intersects(cuboid.xRange) &&
            yRange.intersects(cuboid.yRange) &&
            zRange.intersects(cuboid.zRange);
    }

    public Cuboid intersection(Cuboid cuboid) {
        return new Cuboid(
            xRange.intersection(cuboid.xRange),
            yRange.intersection(cuboid.yRange),
            zRange.intersection(cuboid.zRange)
        );
    }

    public Set<Cuboid> remove(Cuboid remove) {
        Set<Cuboid> parts = new HashSet<>();

        if (!this.contains(remove)) {
            throw new UnsupportedOperationException("Cannot remove cuboid from a cuboid that does not contain it.");
        }

        Cuboid intersection = this.intersection(remove);

        this.xRange.remove(intersection.xRange).forEach((x) -> {
            parts.add(new Cuboid(x, yRange, zRange));
        });

        this.yRange.remove(intersection.yRange).forEach((y) -> {
            parts.add(new Cuboid(intersection.xRange, y, zRange));
        });

        this.zRange.remove(intersection.zRange).forEach((z) -> {
            parts.add(new Cuboid(intersection.xRange, intersection.yRange, z));
        });

        return parts;
    }
}
