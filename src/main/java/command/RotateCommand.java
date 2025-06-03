package command;

import model.*;

public final class RotateCommand implements Command {
    private final Direction dir;
    public RotateCommand(Direction dir) { this.dir = dir; }
    @Override public boolean apply(Context c) {
        c.knight().rotate(dir);
        return true;
    }

    @Override public String toString() {
        return "ROTATE " + dir;
    }
}
