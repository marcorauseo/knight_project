package command;


import model.*;

public final class StartCommand implements Command {
    private final Position start;
    private final Direction dir;

    public StartCommand(Position start, Direction dir) {
        this.start = start;
        this.dir = dir;
    }

    public boolean apply(Context c) {
        if (!c.board().inside(start) || c.board().obstacle(start)) {
            c.setStatus(Status.INVALID_START_POSITION);
            return false;
        }
        c.knight().place(start);   // <-- sposta davvero il cavaliere
        c.knight().rotate(dir);
        return true;

    }

    @Override public String toString() {
        return "START " + start.getX() + "," + start.getY() + "," + dir;
    }

}
