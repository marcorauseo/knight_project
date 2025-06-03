package dto;


import model.Board;
import model.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public  class BoardDTO {
    public int width;
    public int height;
    public List<Coord> obstacles;

    public static class Coord {
        public int x;
        public int y;
    }

    Board toModel() {
        Set<Position> obs = new HashSet<>();
        if (obstacles != null) {
            for (Coord c : obstacles) {
                obs.add(new Position(c.x, c.y));
            }
        }
        return new Board(width, height, obs);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Coord> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Coord> obstacles) {
        this.obstacles = obstacles;
    }
}

