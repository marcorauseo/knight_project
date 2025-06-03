package command;


import model.Context;

/** Ritorna true se la pipeline può continuare. */
public interface Command {
    boolean apply(Context ctx);
}

