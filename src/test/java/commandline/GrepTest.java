package commandline;

import commandline.utils.ShellCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GrepTest {

    private String out(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return CommandLine.forTest(in);
    }

    @Test
    void testGrepWithBind() {
        String input = "echo abc 1234 abcd | grep 'abc' \n exit";
        Assertions.assertEquals("abc 1234 abcd\n", out(input));

        input = "echo abc 1234 abcd | grep -i 'ABC' \n exit";
        Assertions.assertEquals("abc 1234 abcd\n", out(input));

        input = "cat testFiles/1.txt | grep '1' \n exit";
        Assertions.assertEquals("1\n", out(input));

        input = "cat testFiles/testGrep.txt | grep -w 'hello' \n exit";
        Assertions.assertEquals("hello World\n", out(input));

        input = "cat testFiles/testGrep.txt | grep 'hello' \n exit";
        Assertions.assertEquals("hello World\nhelloWorld\n", out(input));

        input = "cat testFiles/testGrep.txt | grep -A 3 'hello' \n exit";
        Assertions.assertEquals("hello World\n" +
                "helloWorld\n" +
                "abcd\n" +
                "bcde\n", out(input));

        System.setIn(System.in);
    }

    @Test
    void testGrepWithOneFile() {
        String input = "grep 'abc' testFiles/testGrep.txt \n exit";
        Assertions.assertEquals("abcd\n" + "abc\n", out(input));

        input = "grep -i 'ABC' testFiles/testGrep.txt \n exit";
        Assertions.assertEquals("abcd\n" + "abc\n", out(input));

        input = "grep -A 2 'abc' testFiles/testGrep.txt \n exit";
        Assertions.assertEquals("abcd\nbcde\n1\nabc\n", out(input));

        System.setIn(System.in);
    }

    @Test
    void testGrepWithTwoFiles() {
        String input = "grep 'hello' testFiles/testGrep.txt testFiles/123.txt \n exit";
        Assertions.assertEquals("testFiles/testGrep.txt: hello World\n" +
                "testFiles/testGrep.txt: helloWorld\n" +
                "testFiles/123.txt: hello\n", out(input));

        input = "grep -i 'HELLO' testFiles/testGrep.txt testFiles/123.txt \n exit";
        Assertions.assertEquals("testFiles/testGrep.txt: hello World\n" +
                "testFiles/testGrep.txt: helloWorld\n" +
                "testFiles/123.txt: hello\n", out(input));


        input = "grep -A 2 'hello' testFiles/testGrep.txt testFiles/123.txt \n exit";
        Assertions.assertEquals("testFiles/testGrep.txt: hello World\n" +
                "testFiles/testGrep.txt: helloWorld\n" +
                "testFiles/testGrep.txt: abcd\n" +
                "testFiles/123.txt: hello\n", out(input));

        System.setIn(System.in);
    }

}
