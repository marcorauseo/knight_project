package model;


import java.util.Set;

public final class Board {
    private final int width;
    private final int height;
    private final Set<Position> obstacles;

    public Board(int width, int height, Set<Position> obstacles) {
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
    }
    public boolean inside(Position p) {
        return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
    }
    public boolean obstacle(Position p) { return obstacles.contains(p); }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<Position> getObstacles() {
        return obstacles;
    }
}

