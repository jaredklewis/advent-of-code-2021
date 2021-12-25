import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRange {
    @Test
    public void testIntersects() {
        Range a = new Range(1, 10);
        Range b = new Range(5, 7);
        assertTrue(a.intersects(b));

        Range c = new Range(1, 5);
        assertTrue(a.intersects(c));

        Range d = new Range(8, 12);
        assertTrue(a.intersects(d));

        Range e = new Range(15, 20);
        assertFalse(a.intersects(e));

        Range f = new Range(-20, -10);
        assertFalse(a.intersects(f));
    }

    @Test
    public void testRemove() {
        Range parent = new Range(1, 10);

        Set<Range> parts = parent.remove(new Range(3, 6));
        assertEquals(2, parts.size());
        assertTrue(parts.contains(new Range(1, 2)));
        assertTrue(parts.contains(new Range(7, 10)));

        parts = parent.remove(new Range(1, 5));
        assertEquals(1, parts.size());
        assertTrue(parts.contains(new Range(6, 10)));

        parts = parent.remove(new Range(8, 10));
        assertEquals(1, parts.size());
        assertTrue(parts.contains(new Range(1, 7)));
    }
}
