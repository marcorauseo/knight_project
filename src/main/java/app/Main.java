package app;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import command.*;
import model.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());


    private static class CommandsDto {
        public List<String> commands;
    }

    public static void main(String[] args) throws Exception {


        /** Definisco gli url (in locale li prende dalla run config, poi li mettero nel docker file*/
        String boardUrl    = System.getenv("BOARD_API");
        String commandsUrl = System.getenv("COMMANDS_API");



        if (boardUrl == null || commandsUrl == null) {
            System.err.println("BOARD_API o COMMANDS_API non definite");
            System.exit(1);
        }


        /** Deserializziamo il json di risposta della api BOARD e assegniamo i valori alla board */
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(readUrl(boardUrl));

        int width  = root.get("width").asInt();
        int height = root.get("height").asInt();

        Set<Position> obstacles = new HashSet<>();
        for (JsonNode n : root.withArray("obstacles")) {
            int x = n.get("x").asInt();
            int y = n.get("y").asInt();
            obstacles.add(new Position(x, y));
        }

        Board board = new Board(width, height, obstacles);


        /** Facciamo la stessa cosa per i comandi con la COMMANDS_API */
        CommandsDto cmdDto = mapper.readValue(readUrl(commandsUrl), CommandsDto.class);
        //Il commandparser mi serve per separare il tipo di comando (start,move o rotate) e le coordinate
        List<Command> commands = CommandParser.parse(cmdDto.commands);


        Knight knight = new Knight();

        /** l'oggetto context mi serve per conservare lo stato della board e del cavaliere e eventuali errori durante i movimenti **/
        Context ctx   = new Context(board, knight);


        /** cicliamo sui movimenti (commands) del cavaliere e logghiamo le posizioni dopo ogni step **/
        int index = 1;
        for (Command c : commands) {
            LOG.log(Level.INFO, ">> [{0}] Eseguo: {1}", new Object[]{index, c});

            boolean continuePipeline = c.apply(ctx) && ctx.status() == Status.SUCCESS;

            // log stato dopo il comando
            Position p = knight.getPosition();
            Direction d = knight.getDirection();
            LOG.log(Level.INFO, "   Posizione dopo [{0}]: ({1},{2}) facing {3}",
                    new Object[]{index, p.getX(), p.getY(), d});

            if (!continuePipeline) {
                LOG.log(Level.INFO, "   Pipeline interrotta: status = {0}", ctx.status());
                break;
            }
            index++;
        }

        Map<String, Object> out = new LinkedHashMap<>();
        if (ctx.status() == Status.SUCCESS) {
            Map<String, Object> pos = new LinkedHashMap<>();
            pos.put("x", knight.getPosition().getX());
            pos.put("y", knight.getPosition().getY());
            pos.put("direction", knight.getDirection().name());
            out.put("position", pos);
        }
        out.put("status", ctx.status().name());
        mapper.writeValue(System.out, out);
    }



    private static String readUrl(String url) throws IOException {
        try (InputStream in = new URL(url).openStream();
             Reader r = new InputStreamReader(in, StandardCharsets.UTF_8);
             StringWriter w = new StringWriter()) {
            char[] buf = new char[4096];
            int n;
            while ((n = r.read(buf)) != -1) w.write(buf, 0, n);
            return w.toString();
        }
    }
}

