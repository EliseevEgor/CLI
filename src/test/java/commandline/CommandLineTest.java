package commandline;

import commandline.commands.Cd;
import commandline.commands.Command;
import commandline.commands.Ls;
import commandline.utils.ShellCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
     * x=10  echo "aasd'$x'asd" == "aasd'10'asd"
     * echo "aasd'$c'asd" == "aasd''asd"
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
        input = "x=10 \n echo \"aasd'$x'asd\"\n exit";
        Assertions.assertEquals("aasd'10'asd\n", out(input));
        input = "echo \"aasd'$c'asd\"\n exit";
        Assertions.assertEquals("aasd''asd\n", out(input));
        System.setIn(System.in);
    }

    /*Test variables
     * a=p b=wd $a$b == command pwd
     * a=ec b=ho $a$b 5 == echo 5 == 5
     * a=ec b=ho echo $a$b == echo echo == echo
     * a=ec b=ho echo $a $b == echo ec ho == ec ho
     * */
    @Test
    void testVars() throws IOException, InterruptedException {
        String input = "a=p \n b=wd \n $a$b \n exit";
        Assertions.assertEquals(ShellCommand.executeCommand("pwd"), out(input));
        input = "a=ec \n b=ho \n $a$b 5 \n exit";
        Assertions.assertEquals("5\n", out(input));
        input = "a=ec \n b=ho \n echo $a$b \n exit";
        Assertions.assertEquals("echo\n", out(input));
        input = "a=ec \n b=ho \n echo $a $b \n exit";
        Assertions.assertEquals("ec ho\n", out(input));
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
     * wc ololo  ==  wc: ololo: No such file or directory
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
        input = "wc ololo \n exit";
        Assertions.assertEquals("wc: ololo: No such file or directory\n"
                , out(input));
    }

    // Test commands which is not in the project
    @Test
    void testOtherCommands() {
        String input = "python \n exit";
        Assertions.assertEquals("Wrong command: python\n", out(input));
        System.setIn(System.in);
        input = "git lolol \n exit";
        Assertions.assertEquals("Wrong command: git lolol\n", out(input));
    }

    @Test
    void testLsCommands() {
        Command ls = new Ls();
        ArrayList<String> lst = new ArrayList<>();
        String expected = "build  build.gradle.kts  gradle  gradlew  gradlew.bat  README.md  settings.gradle  src  testFiles";
        Assertions.assertEquals(expected, ls.run(lst, ""));
        lst.add("src");
        expected = "main  test";
        Assertions.assertEquals(expected, ls.run(lst, ""));
        lst.clear();
        expected = "README.MD";
        lst.add(expected);
        Assertions.assertEquals(expected, ls.run(lst, ""));
    }

    @Test
    void testCdCommands() {
        Command cd = new Cd();
        ArrayList<String> lst = new ArrayList<>();
        String res = System.getProperty("user.dir");
        cd.run(lst, "");
        Assertions.assertEquals(System.getProperty("user.home"), System.getProperty("user.dir"));
        lst.add("main");
        cd.run(lst, "");
        Assertions.assertEquals(System.getProperty("user.dir"), res + "\\" + "main");
    }

}
