package commandline.commands;

import java.io.File;
import java.util.List;
import java.util.Objects;


public class Ls implements Command {
    @Override
    public String run(List<String> args, String before) {
        StringBuilder resString = new StringBuilder();
        if (args.isEmpty()) {
            File curDir = new File(".");
            for (File file : Objects.requireNonNull(curDir.listFiles())) {
                String name = file.getName();
                if (!name.startsWith(".")) {
                    resString.append(name).append("  ");
                }
            }
        } else {
            File curDir = new File(args.get(0));
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
