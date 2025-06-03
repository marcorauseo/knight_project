package model;


import java.util.concurrent.atomic.AtomicReference;

public final class Context {
    private final Board board;
    private final Knight knight;
    private final AtomicReference<Status> status = new AtomicReference<>(Status.SUCCESS);

    public Context(Board board, Knight knight) {
        this.board  = board;
        this.knight = knight;
    }
    public Board   board()  { return board; }
    public Knight  knight() { return knight; }
    public Status  status() { return status.get(); }
    public void    setStatus(Status s) { status.set(s); }
}

