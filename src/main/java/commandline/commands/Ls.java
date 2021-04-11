package commandline.commands;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/* Function ls
 * if it has 0 arguments, then it print all filenames and fir name
 * in current directory, ignoring starting with ./ or ../
 * if it has one argument, if it file, its just print filename
 * if it directory, just print content from this directory
 */
public class Ls implements Command {
    @Override
    public String run(List<String> args, String before) {
        StringBuilder resString = new StringBuilder();
        if (args.isEmpty()) {
            File curDir = Paths.get(System.getProperty("user.dir")).toFile();
            for (File file : Objects.requireNonNull(curDir.listFiles())) {
                String name = file.getName();
                resString.append(name).append("  ");
            }
        } else {
            File curDir = Paths.get(System.getProperty("user.dir"), args.get(1)).toFile();
            if (curDir.isDirectory()) {
                for (File file : Objects.requireNonNull(curDir.listFiles())) {
                    resString.append(file.getName()).append("  ");
                }
            }
            if (curDir.isFile()) {
                resString.append(curDir.getName()).append("  ");
            }
        }
        return resString.toString().trim();
    }

}
