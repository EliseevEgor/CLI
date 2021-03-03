package commandline.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Class for parsing commands using regular expressions
*/
public class Parser {
    private static final Variables variables = new Variables();
    private static final Set<String> commands = Set.of("echo", "wc", "pwd", "exit", "cat");

    private Parser() {
    }

    //parse string in " "
    private static String parseInQuotes(String input) {
        StringBuilder resultS = new StringBuilder(input.replaceAll("\"", ""));
        int i = 0;
        while (i < resultS.length()) {
            int d = resultS.indexOf("$");
            if (d != -1 && d != resultS.length() - 1) {
                for (i = d + 1; i < resultS.length(); i++) {
                    if (resultS.charAt(i) == ' ') {
                        break;
                    }
                }
                String var = resultS.substring(d + 1, i);
                resultS.replace(d, i, variables.getVar(var));
            } else {
                break;
            }
        }
        return resultS.toString();
    }

    // main function for parsing
    public static List<String> parse(String str) {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("(\\w+=\\w+)|('.+')|(\".+\")|(\\S+)|(\\$\\w+)").matcher(str);
        while (m.find()) {
            String currentS = m.group(0);
            if (currentS.charAt(0) == '\'' && currentS.charAt(m.group(0).length() - 1) == '\'') {
                list.add(currentS.substring(1, currentS.length() - 1));
            } else if (currentS.matches("\\w+=\\w+")) {
                String[] split = currentS.split("=");
                variables.putVar(split[0], split[1]);
            } else if (m.group(0).matches("\\$\\w+")) {
                list.add(variables.getVar(currentS.substring(1)));
            } else if (currentS.charAt(0) == '\"' && currentS.charAt(currentS.length() - 1) == '\"') {
                list.add(parseInQuotes(currentS));
            } else if (commands.contains(m.group(0))) {
                list.add(m.group(0));
            } else {
                list.add(m.group(0));
                list.add(" ");
            }
        }
        if (!list.isEmpty() && list.get(list.size() - 1).equals(" ")) {
            list.remove(list.size() - 1);
        }

        return list;
    }
}
