package commandline.utils;

import commandline.commands.*;

/* Takes command's name
 * if command is found then returns the instance of the command class
 * else returns null
 */
public class CommandBuilder {
    private CommandBuilder() {
    }

    public static Command getCommand(String commandName) {
        if (commandName.equals("pwd")) {
            return new Pwd();
        } else if (commandName.equalsIgnoreCase("cat")) {
            return new Cat();
        } else if (commandName.equalsIgnoreCase("echo")) {
            return new Echo();
        } else if (commandName.equalsIgnoreCase("wc")) {
            return new Wc();
        } else {
            return null;
        }
    }
}
