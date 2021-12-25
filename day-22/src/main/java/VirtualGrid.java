import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VirtualGrid {
    private Set<Cuboid> cuboids = new HashSet<>();

    public void runSteps(List<RebootStep> rebootSteps) {
        for (RebootStep step : rebootSteps) {
            AtomicBoolean add = new AtomicBoolean(true);
            if (step.on()) {
                Cuboid newCuboid = new Cuboid(step.xRange(), step.yRange(), step.zRange());
                cuboids = cuboids.stream().flatMap((cuboid) -> {
                    if (!cuboid.intersects(newCuboid)) {
                        add.set(!cuboid.contains(newCuboid));
                        return Stream.of(cuboid);
                    }

                    Set<Cuboid> next = cuboid.remove(cuboid.intersection(newCuboid));
                    return next.stream();
                }).collect(Collectors.toSet());
                if (add.get()) {
                    cuboids.add(newCuboid);
                }
            } else {
                Cuboid kill = new Cuboid(step.xRange(), step.yRange(), step.zRange());
                cuboids = cuboids.stream().flatMap((cuboid) -> {
                    if (!cuboid.intersects(kill)) {
                        return Stream.of(cuboid);
                    }

                   return cuboid.remove(cuboid.intersection(kill)).stream();
                }).collect(Collectors.toSet());
            }
        }
    }

    public long countOn() {
        long total = 0L;
        for (Cuboid cuboid : cuboids) {
            total += cuboid.getVolume();
        }

        return total;
    }
}
