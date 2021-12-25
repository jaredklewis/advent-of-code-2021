import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCuboid {

    @Test
    public void testIntersects() {
        Cuboid a = new Cuboid(
            new Range(1, 1),
            new Range(1, 1),
            new Range(1, 1)
        );

        Cuboid b = new Cuboid(
            new Range(10, 10),
            new Range(10, 10),
            new Range(10, 10)
        );

        assertFalse(a.intersects(b));
    }

    @Test
    public void testRemove() {
        Cuboid parent = new Cuboid(
            new Range(1, 10),
            new Range(1, 10),
            new Range(1, 10)
        );

        Cuboid child = new Cuboid(
            new Range(4, 6),
            new Range(4, 6),
            new Range(5, 7)
        );

        Set<Cuboid> parts = parent.remove(child);

        assertEquals(6, parts.size());

        // Slabs on the horizontal side
        assertTrue(parts.contains(
            new Cuboid(
                new Range(1, 3),
                new Range(1, 10),
                new Range(1, 10)
            )
        ));
        assertTrue(parts.contains(
            new Cuboid(
                new Range(7, 10),
                new Range(1, 10),
                new Range(1, 10)
            )
        ));

        // Slabs vertically above and below
        assertTrue(parts.contains(
            new Cuboid(
                new Range(4, 6),
                new Range(1, 3),
                new Range(1, 10)
            )
        ));
        assertTrue(parts.contains(
            new Cuboid(
                new Range(4, 6),
                new Range(7, 10),
                new Range(1, 10)
            )
        ));

        // Slabs above and below in terms of z
        assertTrue(parts.contains(
            new Cuboid(
                new Range(4, 6),
                new Range(4, 6),
                new Range(1, 4)
            )
        ));
        assertTrue(parts.contains(
            new Cuboid(
                new Range(4, 6),
                new Range(4, 6),
                new Range(8, 10)
            )
        ));

    }
}
