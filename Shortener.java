/**
 * Utility class for converting short URLs to an integer, and vice-versa.
 * @author Emily Canto
 */
public class Shortener {
    private static final char[] charMap = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int base = charMap.length; // Default 62

    /**
     * Convert database integer key to shortened URL
     * @param number Database primary key for full URL
     * @return Shortened URL
     */
    public static String intToShortURL(int number) {
        StringBuilder shortURL = new StringBuilder();
        while (number > 0) {
            shortURL.append(charMap[number % base]);
            number = number / base;
        }
        return shortURL.reverse().toString();
    }

    /**
     * Convert shortened URL to database integer key
     * @param shortURL Shortened URL to convert
     * @return Database primary key for full URL
     */
    public static int shortURLToInt(String shortURL) {
        int number = 0;
        for (int i = 0; i < shortURL.length(); i++)
        {
            if ('a' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'z')
                number = number * base + shortURL.charAt(i) - 'a';
            if ('A' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'Z')
                number = number * base + shortURL.charAt(i) - 'A' + 26;
            if ('0' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= '9')
                number = number * base + shortURL.charAt(i) - '0' + 52;
        }
        return number;
    }
}
