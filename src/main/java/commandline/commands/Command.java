package commandline.commands;

import java.util.List;

/**Interface for all commands with a single method,
   that executes the command
 */
public interface Command {
    // takes args and result of previous calculation
    String run(List<String> args, String before);
}