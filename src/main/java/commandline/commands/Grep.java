package commandline.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Function Grep
 * we look at the key, depending on the key, we call the function with the flag of the given key.
 *  */
public class Grep implements Command {
    private String regexp;
    private Pattern anyCase;
    private static final String WRONG_COMMAND = "Wrong number of args: grep ";
    private static final String FILE_NOT_FOUND = ": No such file or directory";
    /**
     * takes arguments and a key flag
     * returns found lines or error message
     * */
    private String findLines(List<String> args, String before, String key) {
        if (args.isEmpty()) {
            switch (key){
                case("w"):
                    return WRONG_COMMAND + "-w";
                case("i"):
                    return WRONG_COMMAND + "-i";
                case("A"):
                    return WRONG_COMMAND + "-A";
                default:
                    return WRONG_COMMAND;
            }
        }
        StringBuilder result = new StringBuilder();
        regexp = args.remove(0);
        // for grep with key i
        if (key.equals("i")) {
            anyCase = Pattern.compile(".*" + regexp + ".*", Pattern.CASE_INSENSITIVE);
        }
        if (args.isEmpty()) {
            if (before.matches("\\s*")) {
                try  {
                    BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
                    String line;
                    while (!(line = systemIn.readLine()).isEmpty()) {
                        switch (key){
                            case("w"):
                                if (keyW(line)){
                                    result.append(line).append("\n");
                                }
                                break;
                            case("i"):
                                if (keyI(line)){
                                    result.append(line).append("\n");
                                }
                                break;
                            default:
                                if (without(line)){
                                    result.append(line).append("\n");
                                }
                        }
                    }
                    return result.toString();
                } catch (IOException e) {
                    return e.toString();
                }
            }
            String[] lines = before.split("\n");
            for (String line : lines) {
                switch (key){
                    case("w"):
                        if (keyW(line)){
                            result.append(line).append("\n");
                        }
                        break;
                    case("i"):
                        if (keyI(line)){
                            result.append(line).append("\n");
                        }
                        break;
                    default:
                        if (without(line)){
                            result.append(line).append("\n");
                        }
                        break;
                }

            }
            return result.deleteCharAt(result.length()-1).toString();
        }
        if (args.size() == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args.get(0)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    switch (key){
                        case("w"):
                            if (keyW(line)){
                                result.append(line).append("\n");
                            }
                            break;
                        case("i"):
                            if (keyI(line)){
                                result.append(line).append("\n");
                            }
                            break;
                        default:
                            if (without(line)){
                                result.append(line).append("\n");
                            }
                    }
                }
            } catch (IOException e) {
                return args.get(0) + FILE_NOT_FOUND;
            }
        } else {
            for (String file : args) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        switch (key){
                            case("w"):
                                if (keyW(line)){
                                    result.append(file).append(": ").append(line).append("\n");
                                }
                                break;
                            case("i"):
                                if (keyI(line)){
                                    result.append(file).append(": ").append(line).append("\n");
                                }
                                break;
                            default:
                                if (without(line)){
                                    result.append(file).append(": ").append(line).append("\n");
                                }
                        }
                    }
                } catch (IOException e) {
                    return args.get(0) + FILE_NOT_FOUND;
                }
            }
        }
        return result.deleteCharAt(result.length()-1).toString();
    }
    private boolean keyW(String line){
        String[] words = line.split("\\s+");
        for (String word : words) {
            if (word.matches(regexp)) {
                return true;
            }
        }
        return false;
    }

    private boolean keyI(String line){
        Matcher m = anyCase.matcher(line);
        return m.matches();
    }

    private boolean without(String line){
        return line.matches(".*" + regexp + ".*");
    }
    /**
     * Needs for key -A
     * */
    private String keyA(List<String> args, String before) {
        if (args.isEmpty()) {
            return WRONG_COMMAND + "-A";
        }
        String n = args.remove(0);
        try {
            Integer.parseInt(n);
        } catch (Exception e) {
            return WRONG_COMMAND + "-A" + n;
        }
        int num = Integer.parseInt(n);
        StringBuilder result = new StringBuilder();

        regexp = args.remove(0);
        if (args.isEmpty()) {
            if (before.matches("\\s*")) {
                try {
                    BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                    String line;
                    while (!(line = systemIn.readLine()).isEmpty()) {
                        if (line.matches(".*" + regexp + ".*")) {
                            result.append(line).append("\n");
                            int j = 0;
                            while ((line = systemIn.readLine()) != null && j < num) {
                                result.append(line).append("\n");
                                j++;
                            }
                        }
                    }
                    return result.deleteCharAt(result.length() -1).toString();
                } catch (IOException e) {
                    return e.toString();
                }
            }
            String[] lines = before.split("\n");
            int i = 0;
            while (i < lines.length) {
                int j;
                int k;
                if (lines[i].matches(".*" + regexp + ".*")) {
                    result.append(lines[i]).append("\n");
                    j = i + 1;
                    k = 0;
                    while (j < lines.length && k < num) {
                        result.append(lines[j]).append("\n");
                        j++;
                        k++;
                        i = j;
                    }
                }
                i++;
            }
            return result.deleteCharAt(result.length() -1).toString();
        }
        if (args.size() == 1) {
            try (BufferedReader br = new BufferedReader(new FileReader(args.get(0)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.matches(".*" + regexp + ".*")) {
                        result.append(line).append("\n");
                        int j = 0;
                        while ((line = br.readLine()) != null && j < num) {
                            result.append(line).append("\n");
                            j++;
                        }
                    }
                }
            } catch (IOException e) {
                return args.get(0) + FILE_NOT_FOUND;
            }
        } else {
            for (String file : args) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.matches(".*" + regexp + ".*")) {
                            result.append(file).append(": ").append(line).append("\n");
                            int j = 0;
                            while ((line = br.readLine()) != null && j < num) {
                                result.append(file).append(": ").append(line).append("\n");
                                j++;
                            }
                        }
                    }
                } catch (IOException e) {
                    return args.get(0) + FILE_NOT_FOUND;
                }
            }
        }
        return result.deleteCharAt(result.length() -1).toString();
    }

    // main function
    @Override
    public String run(List<String> args, String before) {
        if (args.isEmpty()) {
            return "Wrong command: grep";
        }
        String result;
        String maybeKey = args.get(0);
        switch (maybeKey) {
            case ("-w"):
                args.remove(0);
                result = findLines(args, before, "w");
                break;
            case ("-i"):
                args.remove(0);
                result = findLines(args, before, "i");
                break;
            case ("-A"):
                args.remove(0);
                result = keyA(args, before);
                break;
            default:
                result = findLines(args, before, "withoutKey");
        }
        return result;
    }
}