# maps-current-Location-Demo-app
this application takes the current location of the device from google maps using api and its key and displays it onto the mapActivity this also demonstrate the sending of data between activities in an app in this case the current coordinates of the device are sent from main activity to map activity
Get string between parentheses

\\( – opening parenthesis
\\) – closing parenthesis
(...) – start and end of the match group
[^)]* – match any character except “)” zero or more times (use “+” instead of “*” if you want to match one or more times)
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Regex
{
    public static void main(String[] args)
    {
        String str1 = "I'm the (First string) to be found. ()?";
        String str2 = "I'm the (SECOND string) to be found. Right? (haha?._,?)";
 
        Pattern pattern = Pattern.compile(" (([^)]*)\\)");
 
        matchPattern(str1, pattern);
        matchPattern(str2, pattern);
    }
 
    private static void matchPattern(String str1, Pattern pattern) {
        Matcher m = pattern.matcher(str1);
        while(m.find()) {
            System.out.println(m.group(1));
        }
    }
}
This version of the program will match empty parentheses as well.

Output:

First string

SECOND string
haha?._,?
Get string between square brackets

Pattern for brackets is much like the pattern between parentheses. We will have a opening and a closing bracket and a one or more matches of any character except the closing bracket:

"\\[([^]]+)\\]"
Get string between curly braces

"\\{([^}]+)\\}"
