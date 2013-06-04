package us.havanki.tamal.entity;

/**
 * The direction an entity is facing.
 */
public enum Direction {
    DOWN(0), UP(1), LEFT(2), RIGHT(3);

    private final int n;
    private Direction (int n) { this.n = n; }
}