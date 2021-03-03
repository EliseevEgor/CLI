package commandline.commands;

import java.util.List;

//returns user.dir
public class Pwd implements Command {
    @Override
    public String run(List<String> args, String before) {
        return System.getProperty("user.dir");
    }
}
