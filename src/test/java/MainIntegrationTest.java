import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Direction;
import org.junit.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class MainIntegrationTest {

    private File boardFile;
    private File commandsFile;


    private static class ExecResult {
        final int exitCode;
        final String json;          // può essere null se Main crasha
        ExecResult(int exitCode, String json) {
            this.exitCode = exitCode;
            this.json = json;
        }
    }

    private ExecResult runMain(String boardJson, String commandsJson) throws Exception {
        boardFile    = Files.createTempFile("board",".json").toFile();
        commandsFile = Files.createTempFile("cmd",".json").toFile();
        Files.write(boardFile.toPath(),    boardJson.getBytes(StandardCharsets.UTF_8));
        Files.write(commandsFile.toPath(), commandsJson.getBytes(StandardCharsets.UTF_8));

        String javaExe   = System.getProperty("java.home") + "/bin/java";
        String classPath = System.getProperty("java.class.path");

        ProcessBuilder pb = new ProcessBuilder(javaExe, "-cp", classPath, "app.Main");
        pb.environment().put("BOARD_API",    boardFile.toURI().toString());
        pb.environment().put("COMMANDS_API", commandsFile.toURI().toString());
        pb.redirectErrorStream(true);

        Process p = pb.start();

        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) out.append(line).append('\n');
        }
        int code = p.waitFor();


        String json = null;
        for (String l : out.toString().split("\n")) if (l.startsWith("{")) json = l;
        return new ExecResult(code, json);
    }

    @After
    public void cleanup() {
        if (boardFile != null)    boardFile.delete();
        if (commandsFile != null) commandsFile.delete();
    }


    @Test
    public void testSuccess() throws Exception {
        ExecResult r = runMain(BOARD_JSON_SUCC, CMD_JSON_SUCC);
        assertEquals(0, r.exitCode);

        JsonNode root = new ObjectMapper().readTree(r.json);
        assertEquals("SUCCESS", root.get("status").asText());
        JsonNode pos = root.get("position");
        assertEquals(7, pos.get("x").asInt());
        assertEquals(3, pos.get("y").asInt());
        assertEquals(Direction.NORTH.name(), pos.get("direction").asText());
    }


    @Test
    public void testOutOfBoard() throws Exception {
        ExecResult r = runMain(BOARD_JSON_SMALL, CMD_JSON_OOB);
        assertEquals(0, r.exitCode);

        JsonNode root = new ObjectMapper().readTree(r.json);
        assertEquals("OUT_OF_THE_BOARD", root.get("status").asText());
    }


    @Test
    public void testGenericError() throws Exception {
        ExecResult r = runMain(BOARD_JSON_SMALL, CMD_JSON_GENERIC_ERR);


        if (r.json != null) {
            JsonNode root = new ObjectMapper().readTree(r.json);
            assertEquals("GENERIC_ERROR", root.get("status").asText());
        } else {

            assertNotEquals("Process should fail", 0, r.exitCode);
        }
    }




    private static final String BOARD_JSON_SUCC =
            "{ \"width\": 10, \"height\": 10, \"obstacles\": [" +
                    "{\"x\":0,\"y\":8},{\"x\":1,\"y\":8},{\"x\":1,\"y\":7},{\"x\":1,\"y\":6},{\"x\":1,\"y\":5}," +
                    "{\"x\":1,\"y\":4},{\"x\":2,\"y\":2},{\"x\":3,\"y\":2},{\"x\":4,\"y\":2},{\"x\":5,\"y\":4}," +
                    "{\"x\":5,\"y\":3},{\"x\":5,\"y\":2},{\"x\":5,\"y\":1},{\"x\":6,\"y\":4},{\"x\":6,\"y\":1}," +
                    "{\"x\":7,\"y\":4},{\"x\":8,\"y\":4},{\"x\":8,\"y\":1},{\"x\":9,\"y\":4},{\"x\":9,\"y\":1}] }";

    private static final String CMD_JSON_SUCC   = "{ \"commands\": [ \"START 3,7,SOUTH\", \"MOVE 4\", \"ROTATE WEST\", \"MOVE 2\", \"ROTATE SOUTH\", \"MOVE 2\", \"ROTATE EAST\", \"MOVE 10\", \"ROTATE SOUTH\", \"MOVE 1\", \"ROTATE EAST\", \"MOVE 3\", \"ROTATE NORTH\", \"MOVE 8\" ] }";


    private static final String BOARD_JSON_SMALL = "{ \"width\":3, \"height\":3, \"obstacles\": [] }";


    private static final String CMD_JSON_OOB =
            "{ \"commands\": [\"START 0,0,SOUTH\", \"MOVE 2\"] }";


    private static final String CMD_JSON_GENERIC_ERR =
            "{ \"commands\": [\"START 0,0,NORTH\", \"JUMP 1\"] }";
}
