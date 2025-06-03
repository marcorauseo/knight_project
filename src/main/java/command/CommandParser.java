package command;

import model.*;

import java.util.*;
import java.util.regex.*;

public final class CommandParser {
    private static final Pattern START  = Pattern.compile("^START\\s+(\\d+),(\\d+),([A-Z]+)$");
    private static final Pattern ROTATE = Pattern.compile("^ROTATE\\s+([A-Z]+)$");
    private static final Pattern MOVE   = Pattern.compile("^MOVE\\s+(\\d+)$");

    private CommandParser() {}

    public static List<Command> parse(List<String> raw) {
        List<Command> out = new ArrayList<>(raw.size());
        for (String line : raw) {
            line = line.trim().toUpperCase();
            Matcher m;
            if ((m = START.matcher(line)).matches()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                Direction dir = Direction.valueOf(m.group(3));
                out.add(new StartCommand(new Position(x, y), dir));
            } else if ((m = ROTATE.matcher(line)).matches()) {
                out.add(new RotateCommand(Direction.valueOf(m.group(1))));
            } else if ((m = MOVE.matcher(line)).matches()) {
                out.add(new MoveCommand(Integer.parseInt(m.group(1))));
            } else {
                throw new IllegalArgumentException("Comando non riconosciuto: " + line);
            }
        }
        return out;
    }
}

