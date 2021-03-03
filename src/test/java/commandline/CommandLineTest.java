package commandline;

import commandline.utils.ShellCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

// Testing
class CommandLineTest {
    // helper
    private String out(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return CommandLine.forTest(in);
    }

    /* Test echo
     * echo 12345
     * echo '123 45'
     * a=10; echo $a
     * a=10; echo "a = $a"
     * a=10; b=100; echo  echo "a = $a and b = $b and c ==  end"
     * echo
     * echo 10 | echo 20
     * echo 10 | wc
     * echo 10 | pwd
     * echo 10 | cat
     * */
    @Test
    void testEcho() throws IOException, InterruptedException {
        String input = "echo 12345 \n exit";
        Assertions.assertEquals("12345\n", out(input));
        input = "echo '123 45' \n exit";
        Assertions.assertEquals("123 45\n", out(input));
        input = "a=10 \n echo $a \n exit";
        Assertions.assertEquals("10\n", out(input));
        input = "a=10 \n echo \"a = $a\" \n exit";
        Assertions.assertEquals("a = 10\n", out(input));
        input = "a=10 \n b=100 \n echo \"a = $a and b = $b and c ==  end\" \n exit";
        Assertions.assertEquals("a = 10 and b = 100 and c ==  end\n", out(input));
        input = "echo \n exit";
        Assertions.assertEquals("\n", out(input));
        input = "echo 10 | echo 20 \n exit";
        Assertions.assertEquals("20\n", out(input));
        input = "echo 10 | wc \n exit";
        Assertions.assertEquals("1  1  3\n", out(input));
        input = "echo 10 | pwd \n exit";
        Assertions.assertEquals(ShellCommand.executeCommand("pwd"), out(input));
        input = "echo 10 | cat \n exit";
        Assertions.assertEquals("10\n", out(input));
        System.setIn(System.in);
    }

    /* Test pwd */
    @Test
    void testPwd() throws IOException, InterruptedException {
        String input = "pwd \n exit";
        Assertions.assertEquals(ShellCommand.executeCommand("pwd"), out(input));
        System.setIn(System.in);
    }

    /* Test cat
     * cat gradlew.bat
     * cat 1.txt 123.txt
     * cat go wrong
     * */
    @Test
    void testCat() throws IOException, InterruptedException {
        String input = "cat gradlew.bat \n exit";
        Assertions.assertEquals(ShellCommand.executeCommand(input), out(input));
        input = "cat testFiles/1.txt testFiles/123.txt \n exit";
        Assertions.assertEquals(ShellCommand.executeCommand(input), out(input));
        input = "cat nofile.nofile \n exit";
        Assertions.assertEquals("File not found\n", out(input));
        System.setIn(System.in);
    }

    /*
     * wc gradlew.bat
     * wc 1.txt 123.txt
     * cat 1.txt | wc
     * */
    @Test
    void testWc() {
        String input = "wc gradlew.bat \n exit";
        Assertions.assertEquals("89  357  2763 gradlew.bat\n", out(input));
        input = "wc testFiles/1.txt testFiles/123.txt \n exit";
        Assertions.assertEquals("3  3  6 testFiles/1.txt\n" + "3  3  25 testFiles/123.txt\n" +
                "6  6  31 total\n", out(input));
        input = "cat testFiles/1.txt | wc \n exit";
        Assertions.assertEquals("3  3  6\n", out(input));
        System.setIn(System.in);
    }

    // Test command which is not in the project
    @Test
    void testOtherCommands() {
        String input = "python \n exit";
        Assertions.assertEquals("Wrong command: python\n", out(input));
        System.setIn(System.in);
    }
}
