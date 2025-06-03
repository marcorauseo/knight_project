package model;



public final class Knight {
    private Position position;
    private Direction direction;

    public Knight(Position start, Direction dir) {
        this.position = start;
        this.direction = dir;
    }

    public Knight() {

    }

    public Position getPosition()  { return position; }
    public Direction getDirection() { return direction; }

    public void rotate(Direction newDir) { this.direction = newDir; }

    /**  movimento passo-dopo-passo
     * Aggiorna la posizione (position = next) solo se
     – la nuova cella è dentro la board e
     – non c’è un ostacolo.
     - Ritorna false se esce dal bordo; ritorna true se si ferma su un ostacolo o percorre tutti i passi. */
    public boolean move(Board board, int steps) {
        for (int i = 0; i < steps; i++) {
            Position next = position.translate(direction, 1);
            if (!board.inside(next)) return false;          // OUT_OF_THE_BOARD
            if (board.obstacle(next)) break;                // si ferma davanti ostacolo
            position = next;
        }
        return true;
    }

    public void place(Position p) {
        this.position = p;
    }


}
