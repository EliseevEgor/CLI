package commandline.commands;

import java.io.File;
import java.util.List;

/* Function cd
 * if it has 0 arguments, then it change current directory to
 * home directory
 * if it has one argument, then it change current directory to new one from arg
 */
public class Cd implements Command {
    @Override
    public String run(List<String> args, String before) {
        if (args.isEmpty()) {
            System.setProperty("user.dir", System.getProperty("user.home"));
        } else {
            File file = new File(args.get(0));
            System.setProperty("user.dir", file.getAbsolutePath());
        }
        return null;
    }

}
