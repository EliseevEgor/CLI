package commandline.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/* Function wc
 * if it has arguments, then it counts the number of lines, words and bytes in files
 * else it counts the same in result of previous calculations
 */
public class Wc implements Command {
    private long totalByteCount = 0;
    private long totalLineCount = 0;
    private long totalWordCount = 0;
    private long wordCount = 0;
    private long lineCount = 0;
    private long byteCount = 0;

    // counts all in result of previous calculations
    private String countArgsEmpty(String before) {
        byte[] b = before.getBytes(StandardCharsets.UTF_8);
        byteCount = b.length;
        wordCount = before.replaceAll("\\s{2,}", " ").trim().split("\\s").length;
        lineCount = before.chars().filter(ch -> ch == '\n').count() + 1;
        return lineCount + "  " + wordCount + "  " + (byteCount + 1);
    }

    // counts all in files
    private String countArgsNoEmpty(List<String> args) {
        StringBuilder result = new StringBuilder();
        for (String s : args) {
            if (!s.equals(" ")) {
                wordCount = 0;
                lineCount = 0;
                File f = new File(s);
                byteCount = f.length();
                totalByteCount += byteCount;
                try (BufferedReader br = new BufferedReader(new FileReader(s))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.isEmpty()) {
                            wordCount += line.trim().split("\\s+").length;
                        }
                        lineCount += 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                totalLineCount += lineCount;
                totalWordCount += wordCount;
                result.append(lineCount).append("  ");
                result.append(wordCount).append("  ");
                result.append(byteCount).append(" ");
                if (args.size() > 1) {
                    result.append(s).append("\n");
                } else {
                    result.append(s);
                }
            }
        }
        if (args.size() == 1)
            return result.toString();
        result.append(totalLineCount).append("  ");
        result.append(totalWordCount).append("  ");
        result.append(totalByteCount).append(" total");
        return result.toString();
    }

    @Override
    // main function
    public String run(List<String> args, String before) {
        if (args.isEmpty()) {
            return countArgsEmpty(before);
        }
        return countArgsNoEmpty(args);
    }
}