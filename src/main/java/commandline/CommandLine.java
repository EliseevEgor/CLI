package commandline;

import commandline.commands.Command;
import commandline.utils.CommandBuilder;
import commandline.utils.Parser;
import commandline.utils.ShellCommand;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
  Main Class
    until get "exit":
     read the line, split it by |,
     send each part to the parser,
     get the command name and arguments,
     send the name to the commandBuilder,
     then execute the command by run method
*/
public class CommandLine {
    private static final List<String> results = new ArrayList<>();

    // main function for CommandLine
    private static String work(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        boolean cycle = true;
        StringBuilder output = new StringBuilder();
        while (cycle) {
            String userInput = scanner.nextLine();
            String[] commands = userInput.split("\\s*\\|\\s*");
            for (String str : commands) {
                List<String> list = Parser.parse(str);
                if (!list.isEmpty()) {
                    if (list.get(0).matches("exit")) {
                        cycle = false;
                        break;
                    }
                    Command command = CommandBuilder.getCommand(list.get(0));
                    if (command == null) {
                        try {
                            StringBuilder input = new StringBuilder();
                            for (String elem : list) {
                                input.append(elem);
                            }
                            String res = ShellCommand.executeCommand(input.toString());
                            results.add(res);
                            if (res.isEmpty()) {
                                output.append("Wrong command: ").append(input);
                                System.out.println("Wrong command: " + input);
                                break;
                            }
                        } catch (InterruptedException | IOException e) {
                            output.append("Wrong command: ").append(list.get(0)).append("\n");
                            System.out.println("Wrong command: " + list.get(0));
                            break;
                        }
                    } else {
                        List<String> s = list.subList(1, list.size());
                        if (!results.isEmpty()) {
                            String before = results.remove(results.size() - 1);
                            results.add(command.run(s, before));
                        } else {
                            results.add(command.run(s, ""));
                        }
                    }
                }
            }
            if (!results.isEmpty()) {
                String result = results.remove(0);
                output.append(result).append("\n");
                System.out.println(result);
            }
        }
        return output.toString();
    }

    public static void main(String[] args) {
        work(System.in);
    }

    // func only for test
    public static String forTest(InputStream input) {
        return work(input);
    }
}