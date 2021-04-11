package commandline.commands;

import java.awt.geom.Path2D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            var path = Paths.get(System.getProperty("user.dir"), args.get(1));
            if (!Files.isDirectory(path)) {
                return "no such directory";
            }
            System.setProperty("user.dir", path.toAbsolutePath().toString());
        }
        return "";
    }

}
