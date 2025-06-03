package command;

import model.*;

public final class MoveCommand implements Command {
    private final int steps;
    public MoveCommand(int steps) { this.steps = steps; }

    @Override public boolean apply(Context c) {
        boolean ok = c.knight().move(c.board(), steps);
        if (!ok) c.setStatus(Status.OUT_OF_THE_BOARD);
        return ok;
    }

    @Override public String toString() {
        return "MOVE " + steps;
    }
}

