package model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;


public final class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) { this.x = x; this.y = y; }

    public int getX() { return x; }
    public int getY() { return y; }

    /** abbiamo supposto un sistema ad assi x orizzontale e y verticale con x che aumenta con direzione EST e y che aumenta con diretione NORD */
    public Position translate(Direction dir, int steps) {
        switch (dir) {
            case NORTH: return new Position(x, y + steps);
            case SOUTH: return new Position(x, y - steps);
            case EAST:  return new Position(x + steps, y);
            case WEST:  return new Position(x - steps, y);
            default:    throw new AssertionError(dir);
        }
    }

    /* ---------- qui il punto chiave ---------- */
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return x == p.x && y == p.y;
    }
    @Override public int hashCode() { return Objects.hash(x, y); }
    /* ---------------------------------------- */

    @Override public String toString() { return "(" + x + "," + y + ")"; }
}


