import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class Tests {

    private Markdown2HTML markdown2HTML;
    String path = "C:/Educate/java-advanced-exam-2021/tickets/ticket-26/src/";

    private void prepare(String testFile) throws IOException {
        markdown2HTML = new Markdown2HTML();
        markdown2HTML.logger = new StringBuilder();
        markdown2HTML.main(get_input(path + testFile));
    }

    private String[] get_input(String testOutFile) throws IOException {
        String ans[];
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testOutFile)));

        String line = reader.readLine();
        ans = line.split(" ");
        return ans;
    }

    private String get_ans(String testOutFile) throws IOException {
        StringBuilder tmp = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testOutFile)));

        String line = null;

        while((line = reader.readLine()) != null)
            tmp.append(line);

        StringBuilder ans = new StringBuilder();
        for(int i = 0; i < tmp.length(); i++) {
            if(tmp.charAt(i) != '\t' && tmp.charAt(i) != '\n') {
                ans.append(tmp.charAt(i));
            }
        }

        return ans.toString();
    }

    private String get(StringBuilder cur) {
        StringBuilder ans = new StringBuilder();
        for(int i = 0; i < cur.length(); i++) {
            if(cur.charAt(i) != '\t' && cur.charAt(i) != '\n') {
                ans.append(cur.charAt(i));
            }
        }

        return ans.toString();
    }

    @Test
    public void test1() throws IOException {
        prepare("testFiles/test01/input");
        Assert.assertEquals(
                get_ans(path + "testFiles/test01/output"),
                get(markdown2HTML.logger)
        );
    }

    @Test
    public void test2() throws IOException {
        prepare("testFiles/test02/input");
        Assert.assertEquals(
                get_ans(path + "testFiles/test02/output"),
                get(markdown2HTML.logger)
        );
    }

    @Test
    public void test3_blockquote() throws IOException {
        prepare("testFiles/test03/input");
        Assert.assertEquals(
                get_ans(path + "testFiles/test03/output"),
                get(markdown2HTML.logger)
        );
    }

    @Test
    public void test4_blockquote2() throws IOException {
        prepare("testFiles/test04/input");
        Assert.assertEquals(
                get_ans(path + "testFiles/test04/output"),
                get(markdown2HTML.logger)
        );
    }

    @Test
    public void test5_lists2() throws IOException {
        prepare("testFiles/test05/input");
        Assert.assertEquals(
                get_ans(path + "testFiles/test05/output"),
                get(markdown2HTML.logger)
        );
    }
}
