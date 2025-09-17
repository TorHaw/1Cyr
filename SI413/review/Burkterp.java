import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Burkterp {

    private static final String HEADER = "HEAR_YE(";
    private static final String UNIFY_TOKEN = "~ UNIFY ~";

    static String giveMe(String inside) throws IOException {
        if (inside.contains("GIVE_ME")) {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String userInput = console.readLine();
            inside = inside.replace("GIVE_ME", userInput);
        }
        return inside;
    }

    static String reverse(String inside) {
        Pattern pattern = Pattern.compile("REVERTERE\\(([^)]*)\\)");
        Matcher matcher = pattern.matcher(inside);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String content = matcher.group(1);
            String reversed = new StringBuilder(content).reverse().toString();
            matcher.appendReplacement(sb, Matcher.quoteReplacement(reversed));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    static void processHearYe(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;
        int depth = 1;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    break;
                }
            }
            sb.append(c);
        }

        String inside = sb.toString().trim();
        inside = giveMe(inside);
        inside = reverse(inside);

        inside = inside.replaceAll("~\\s*UNIFY\\s*~", " ");
        inside = inside.replaceFirst("^~[A-Z]+\\b\\s*", "");
        inside = inside.replaceFirst("\\s*~[A-Z]+\\b\\s*$", "");
        inside = inside.replaceFirst("^~\\s*", "");
        inside = inside.replaceFirst("\\s*~$", "");
        inside = inside.replaceAll("\\s+", " ").trim();

        if (!inside.isEmpty()) {
            System.out.println(inside);
        }
    }

    static boolean readHeaderAfterH(BufferedReader reader) throws IOException {
        char[] need = HEADER.substring(1).toCharArray();
        reader.mark(need.length + 1);
        for (char expect : need) {
            int ch = reader.read();
            if (ch == -1 || (char) ch != expect) {
                reader.reset();
                return false;
            }
        }
        return true;
    }

    static void hearYe(BufferedReader reader, char firstChar) throws IOException {
        if (firstChar != 'H') return;
        if (!readHeaderAfterH(reader)) return;
        processHearYe(reader);
    }

    public static void main(String[] args) {
        String fname = "test.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                if ((char) ch == 'H') {
                    hearYe(reader, (char) ch);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
