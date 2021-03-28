package commandline.commands;

import java.io.File;
import java.util.List;

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
