package commandline.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* Class for running shell commands */
public class ShellCommand {
    private ShellCommand() {
    }

    public static String executeCommand(String command) throws IOException, InterruptedException {
        StringBuilder output = new StringBuilder();
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        try (
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }
}
