public record RebootStep(
    boolean on,
    Range xRange,
    Range yRange,
    Range zRange) {

    @Override
    public String toString() {
        return "RebootStep{" +
            "on=" + on +
            ", xRange=" + xRange +
            ", yRange=" + yRange +
            ", zRange=" + zRange +
            '}';
    }
}

