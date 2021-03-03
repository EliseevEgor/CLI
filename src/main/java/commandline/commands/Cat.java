package commandline.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/** Function Cat
   if the command hasn't received any args, then it returns previous
   calculation, else returns the contents of args
 */
public class Cat implements Command {
    @Override
    public String run(List<String> args, String before) {
        if (args.isEmpty()) {
            return before;
        }
        StringBuilder output = new StringBuilder();
        for (String str : args) {
            if (!str.equals(" ")) {
                try (BufferedReader br = new BufferedReader(new FileReader(str))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                        output.append("\n");
                    }
                } catch (IOException e) {
                    return "File not found";
                }
            }
        }
        return output.deleteCharAt(output.length() - 1).toString();
    }
}
