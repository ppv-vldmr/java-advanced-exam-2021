import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Markdown2HTML {
    static final String OPEN_PICTURE = "<img alt='";
    static final String PICTURE_SRC = "' src='";
    static final String OPEN_LINK = "<a href='";
    static final String CLOSE_LINK = "</a>";
    static final String CLOSE_URL = "'>";
    static final String TAB = "\t";
    static BufferedWriter out;
    static String inputEncode = "UTF8";
    static String outputEncode = "UTF8";
    private static String line = "";
    private static String lastHeadline = "";
    private static StringBuilder paragraph = new StringBuilder();
    static int index;
    static int tabCount = 2;
    static int sizeQuote = 0;
    static int newQuote = 0;
    static boolean swapQuote = false;
    static int newTypeList = 0; //0 - don't list, 1 - unordered list, 2 - ordered list
    static int currentTypeList = 0;
    static int levelList = -1;
    static int newLevelList = 0;
    static boolean swapType = false;

    static StringBuilder logger = new StringBuilder();

    static final private Map<String, String> closeSequences = new HashMap<>(Map.of(
            "**", "</strong>",
            "__", "</strong>",
            "*", "</em>",
            "_", "</em>",
            "--", "</s>",
            "++", "</u>",
            "'", "</code>",
            "~", "</mark>",
            ")", "",
            "]", ""
    ));
    static final private Map<Character, String> specialSymbols = new HashMap<>(Map.of(
            '>', "&gt;",
            '<', "&lt;",
            '&', "&amp;"
    ));
    static final private Map<String, String> openClose = new HashMap<>(Map.of(
            "</strong>", "<strong>",
            "</em>", "<em>",
            "</code>", "<code>",
            "</s>", "<s>",
            "</u>", "<u>",
            "</mark>", "<mark>"
    ));
    static final private String[] markupSequences = {"**", "*", "__", "_", "--", "`", "++", "~"};
    static final private String[] separation = {"***", "---"};

    static void print(String str) throws IOException {
        if (out != null) {
            out.write(str + "\n");
        } else {
            System.out.println(str);
        }
    }

    public static void run(BufferedReader in) throws IOException {
        StringBuilder parsedMarkdownText = new StringBuilder();
        line = "";

        parsedMarkdownText.append("<!DOCTYPE HTML>").append('\n').append("<html>").append('\n').append(TAB).append("<head>")
                .append('\n').append(TAB + TAB).append("<meta charset=\"").append(outputEncode).append("\">").append('\n')
                .append(TAB).append("</head>").append("\n").append(TAB).append("<body>").append("\n");
        while(!nextParagraph(in).isEmpty()){
            parsedMarkdownText.append(parseParagraph()).append('\n');
        }
        parsedMarkdownText.append(TAB).append("</body>").append('\n').append("</html>");

        logger = parsedMarkdownText;

        print(parsedMarkdownText.toString());
    }

    public static String parseParagraph() {
        StringBuilder parsedParagraph = new StringBuilder();
        index = 0;
        int countLevelHeadline = parseHeadline();

        parsedParagraph.append(generateTab());
        if(countLevelHeadline == 0) {
            parsedParagraph.append("<p>\n");
            parsedParagraph.append(paragraph, 0, index);
            tabCount++;
            parsedParagraph.append(generateTab());
            parsedParagraph.append(parseText(null, false));
            swapType = false;
            tabCount--;
            parsedParagraph.append("\n").append(generateTab());
            parsedParagraph.append("</p>");
        } else {
            parsedParagraph.append("<h").append(countLevelHeadline).append(">\n");
            tabCount++;
            parsedParagraph.append(generateTab());
            parsedParagraph.append(parseText(null, false));
            tabCount--;
            parsedParagraph.append("\n").append(generateTab());
            parsedParagraph.append("</h").append(countLevelHeadline).append(">");
        }

        return parsedParagraph.toString();
    }

    public static String parseText(String stopSequence, boolean isPicture) {
        StringBuilder parsedText = new StringBuilder();

        while(index < paragraph.length()) {
            if (index > 0 && paragraph.charAt(index - 1) == '\\') {
                parsedText.append(convertSpecialSymbols());
            } else if (checkStopSequence(stopSequence)) {
                parsedText.append(convertStopSequence(stopSequence));
                index += stopSequence.length();
                return parsedText.toString();
            } else if (checkStartPicture()) {
                parsedText.append(convertPicture());
            } else if (paragraph.charAt(index) == '[' && !isPicture) {
                parsedText.append(convertLink());
            } else if (checkSeparation()) {
                parsedText.append(convertSeparation());
                index += 2;
            } else if (checkList()) {
                parsedText.append(convertList());
            } else if (getMarkupSequence() != null && !isPicture) {
                parsedText.append(convertMarkup());
            } else if (checkQuote()) {
                parsedText.append(convertQuote());
                if(newQuote < sizeQuote) {
                    index--;
                    swapQuote = true;
                    break;
                }
            }else if (paragraph.charAt(index) == '\\') {
                if (index + 1 >= paragraph.length()) {
                    parsedText.append('\\');
                }
            } else if (checkSeparation()) {
                parsedText.append(convertSeparation());
            } else {
                parsedText.append(convertSpecialHtmlSymbols(paragraph.charAt(index)));
            }
            if(index < paragraph.length() && paragraph.charAt(index) == '\n') {
                parsedText.append(generateTab());
            }
            index++;
        }
        return parsedText.toString();
    }

    private static String convertList() {
        int lastLevel = levelList;
        int lastTypeList = currentTypeList;
        StringBuilder convertedList = new StringBuilder();

        if(levelList < newLevelList) {
            levelList = newLevelList;
            swapType = true;
            if(currentTypeList != newTypeList) {
                swapType = true;
            }
            currentTypeList = newTypeList;
            tabCount++;
            if(currentTypeList == 1) {
                convertedList.append("<ul>").append('\n');
                convertedList.append(generateTab()).append("<li>");
                convertedList.append(parseText(null, false));
                convertedList.append("</li>").append('\n');
                tabCount--;
                convertedList.append(generateTab()).append("</ul>").append('\n').append(generateTab());
            } else {
                convertedList.append("<ol>").append('\n');
                convertedList.append(generateTab()).append("<li>");
                convertedList.append(parseText(null, false));
                convertedList.append("</li>").append('\n');
                tabCount--;
                convertedList.append(generateTab()).append("</ol>").append('\n').append(generateTab());
            }
            currentTypeList = lastTypeList;
            levelList = lastLevel;
        } else {
            if(currentTypeList != newTypeList) {
                currentTypeList = newTypeList;
                if(lastTypeList != 0) {
                    swapType = true;
                }
                if(currentTypeList == 1) {
                    if(swapType) {
                        tabCount--;
                        convertedList.append("</li>").append('\n').append(generateTab()).append("</ol>");
                        convertedList.append('\n').append(generateTab());
                    }
                    convertedList.append("<ul>").append('\n');
                    tabCount++;
                    convertedList.append(generateTab()).append("<li>");
                    convertedList.append(parseText(null, false));
                    if(swapType) {
                        convertedList.append("</li>").append('\n');
                        swapType = false;
                        tabCount--;
                        convertedList.append(generateTab()).append("</ul>");
                    }
                } else {
                    if(swapType) {
                        tabCount--;
                        convertedList.append("</li>").append('\n').append(generateTab()).append("</ul>");
                        convertedList.append('\n').append(generateTab());
                    }
                    convertedList.append("<ol>").append('\n');
                    tabCount++;
                    convertedList.append(generateTab()).append("<li>");
                    convertedList.append(parseText(null, false));
                    if(swapType) {
                        convertedList.append("</li>").append('\n');
                        swapType = false;
                        tabCount--;
                        convertedList.append(generateTab()).append("</ol>");
                    }
                }
                currentTypeList = lastTypeList;
            } else {
                convertedList.append("</li>").append('\n').append(generateTab());
                convertedList.append("<li>");
                convertedList.append(parseText(null, false));
            }
        }

        return convertedList.toString();
    }

    private static boolean isNumber(char ch) {
        if(ch >= '0' && ch <= '9') {
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkList() {
        if((index > 0 && paragraph.charAt(index - 1) == '\n') || index == 0) {
            int temp_index = index;
            int currentTab = 0;
            while(temp_index + 3 < paragraph.length() &&
                    paragraph.substring(temp_index, temp_index + 4).equals("    ")) {
                temp_index += 4;
                currentTab++;
            }

            index = temp_index;

            if(paragraph.charAt(temp_index) == '+' || paragraph.charAt(temp_index) == '-' || paragraph.charAt(temp_index) == '*')
            {
                if(temp_index + 1 < paragraph.length() && paragraph.charAt(temp_index + 1) == ' ') {
                    index = temp_index + 2;
                    newTypeList = 1;
                    newLevelList =currentTab;
                    return true;
                } else {
                    return false;
                }
            } else if(isNumber(paragraph.charAt(temp_index))) {
                while(temp_index < paragraph.length() && isNumber(paragraph.charAt(temp_index))) {
                    temp_index++;
                }
                if(temp_index < paragraph.length()) {
                    if (paragraph.charAt(temp_index) == '.' &&
                            (temp_index + 1 < paragraph.length() && paragraph.charAt(temp_index + 1) == ' ')) {
                        index = temp_index + 2;
                        newTypeList = 2;
                        newLevelList = currentTab;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static String convertQuote() {
        StringBuilder convertedQuote = new StringBuilder();

        if(paragraph.charAt(index) == '\n' && index + 1 < paragraph.length() && paragraph.charAt(index + 1) == '>')
            index++;

        newQuote = 0;
        while(paragraph.charAt(index) == '>') {
            newQuote++;
            index++;
        }

        if (Character.isWhitespace(paragraph.charAt(index))) {
            index++;
        }

        if(newQuote > sizeQuote) {
            swapQuote = false;
            int last = sizeQuote;
            convertedQuote.append("<blockquote>\n");
            tabCount++;
            sizeQuote = newQuote;
            convertedQuote.append(generateTab());
            convertedQuote.append(parseText(null, false));
            tabCount--;
            convertedQuote.append('\n').append(generateTab());
            convertedQuote.append("</blockquote>");
            sizeQuote = last;
        } else {
            index--;
        }

        return convertedQuote.toString();
    }

    private static boolean checkQuote() {
        if(index == 0 && paragraph.charAt(index) == '>'){
            return true;
        } else if(index > 0 && paragraph.charAt(index) == '>' && paragraph.charAt(index - 1) == '\n') {
            return true;
        } else {
            return false;
        }
    }

    public static String generateTab() {
        StringBuilder tab = new StringBuilder();

        for(int i = 0; i < tabCount; i++) {
            tab.append(TAB);
        }

        return tab.toString();
    }

    public static boolean checkSeparation() {
        for (String s : separation) {
            if(paragraph.substring(0, paragraph.length()).equals(s)) {
                return true;
            }

            if (index + s.length() + 1 <= paragraph.length()){
                if(paragraph.substring(index, index + s.length() + 1).equals("\n" + s)) {
                    return true;
                } else if(paragraph.substring(index, index + s.length() + 1).equals(s + "\n")) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String convertSeparation() {
        return "<hr>";
    }

    public static String convertSpecialSymbols() {
        StringBuilder string = new StringBuilder();

        char current = paragraph.charAt(index);
        if(current == '\\') {
            string.append(current);
        } else if (current == '`' || current == '_' || current == '*') {
            string.append(current);
        } else {
            string.append('\n');
            string.append(current);
        }

        return string.toString();
    }

    public static boolean checkStartPicture() {
        return paragraph.charAt(index) == '!' && index + 1 < paragraph.length() && paragraph.charAt(index + 1) == '[';
    }

    public static String convertPicture() {
        StringBuilder convertedPicture = new StringBuilder();

        index += 2;
        String text = parseText("]", true);
        index++;
        String url = parseText(")", true);
        index--;

        convertedPicture.append(OPEN_PICTURE).append(text).append(PICTURE_SRC).append(url).append(CLOSE_URL);

        return convertedPicture.toString();
    }

    public static String convertLink() {
        StringBuilder convertedLink = new StringBuilder();

        index++;
        String link = parseText("]", false);
        index++;
        String url = parseText(")", true);
        index--;

        convertedLink.append(OPEN_LINK).append(url).append(CLOSE_URL).append(link).append(CLOSE_LINK);

        return convertedLink.toString();
    }

    public static boolean checkStopSequence(String stopSequence) {
        if(stopSequence == null || index + stopSequence.length() > paragraph.length()) {
            return false;
        }

        return paragraph.substring(index, index + stopSequence.length()).equals(stopSequence);
    }

    public static String convertStopSequence(String stopSequence) {
        return closeSequences.get(stopSequence);
    }

    public static String convertSpecialHtmlSymbols(char symbol) {
        if(specialSymbols.containsKey(symbol)) {
            return specialSymbols.get(symbol);
        } else {
            return String.valueOf(symbol);
        }
    }

    public static String getMarkupSequence() {
        for (String sequence : markupSequences) {
            if (index + sequence.length() <= paragraph.length() &&
                    paragraph.substring(index, index + sequence.length()).equals(sequence)) {
                return sequence;
            }
        }

        return null;
    }

    public static String convertMarkup() {
        StringBuilder convertedMarkup = new StringBuilder();

        String markupSequence = getMarkupSequence();
        index += markupSequence.length();

        int returnIndex = index;
        boolean needToReturn = false;

        String parsedText = parseText(markupSequence, false);
        int closeSequencePos = parsedText.length() - convertStopSequence(markupSequence).length();
        if (closeSequencePos > 0) {
            String closeSequence = parsedText.substring(parsedText.length() - convertStopSequence(markupSequence).length());
            convertedMarkup.append(openClose.getOrDefault(closeSequence, markupSequence));
            index--;
        } else {
            convertedMarkup.append(markupSequence);
            needToReturn = true;
        }

        if (needToReturn) {
            index = returnIndex - 1;
        } else
            convertedMarkup.append(parsedText);

        return convertedMarkup.toString();
    }

    public static int parseHeadline() {
        while (index < paragraph.length() && paragraph.charAt(index) == '#') {
            index++;
        }

        if (Character.isWhitespace(paragraph.charAt(index))) {
            return index++;
        }


        return 0;
    }

    public static String nextLine(BufferedReader in) throws IOException {
        return line = in.readLine();
    }

    public static boolean isHeadline() {
        int i = 0;
        while(line.charAt(i) == '#') {
            i++;
        }

        if(i > 0 && line.charAt(i) == ' ')
            return true;

        return false;
    }

    public static int isHeadlineFirstOrSecond() {
        int i = 0;
        boolean haveOnlyFirst = true;
        boolean haveOnlySecond = true;

        while(i < line.length()) {
            if(line.charAt(i) != '='){
                haveOnlyFirst = false;
            }
            if(line.charAt(i) != '-'){
                haveOnlySecond = false;
            }
            i++;
        }

        if(haveOnlyFirst){
            return 1;
        } else if(haveOnlySecond) {
            return 2;
        }
        return 0;
    }

    public static String nextParagraph(BufferedReader in) throws IOException {
        if(line == null) {
            return "";
        }

        while(line != null && line.isEmpty()){
            line = nextLine(in);
        }

        paragraph = new StringBuilder();
        if(lastHeadline != "") {
            paragraph.append(lastHeadline);
            lastHeadline = "";
            return paragraph.toString();
        }
        paragraph.append(line);

        if(isHeadline()) {
            nextLine(in);
            return paragraph.toString();
        }

        while(nextLine(in) != null && !line.isEmpty()) {
            int level = isHeadlineFirstOrSecond();
            if(level > 0) {
                StringBuilder tmp = new StringBuilder();
                tmp.append("#".repeat(level));
                tmp.append(" ").append(paragraph);
                paragraph = tmp;
                nextLine(in);
                return paragraph.toString();
            }
            if(isHeadline()) {
                lastHeadline = line;
                nextLine(in);
                return paragraph.toString();
            }
            paragraph.append("\n");
            paragraph.append(line);
        }

        while(line != null && line.isEmpty()){
            nextLine(in);
        }

        return paragraph.toString();
    }

    public static void main(String[] args) throws IOException {
        if(Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("Expected all not null arguments");
        }

        if(args[0].equals("Standard")){
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                run(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(args[0].equals("File")) {
            if(args.length != 3 && args.length != 5) {
                logger.append("Expected 3 or 5 arguments");
                System.err.println("Expected 3 or 5 arguments");
            }

            String curPath = new File("").getAbsolutePath() + "\\src\\testFiles\\";
            if(args.length == 5) {
                inputEncode = args[3];
                outputEncode = args[4];
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(curPath + args[1]), inputEncode))
            ) {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(curPath + args[2]), outputEncode))
                )  {
                    out = writer;
                    run(reader);
                } catch (IOException e) {
                    System.err.println("Problems with output file : ");
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.err.println("Problems with input file : ");
                e.printStackTrace();
            }
        } else {
            logger.append("Unknown input type");
            System.err.println("Unknown input type");
        }
    }
}