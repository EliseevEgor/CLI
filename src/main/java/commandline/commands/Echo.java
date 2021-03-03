package commandline.commands;

import java.util.List;

/** Function echo
   if it has args then returns them
   else returns empty string
 */
public class Echo implements Command {
    @Override
    public String run(List<String> args, String before) {
        if (args.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String s : args) {
            result.append(s);
        }
        return result.toString();
    }
}
