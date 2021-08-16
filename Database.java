import java.sql.*;

/**
 * Class for adding and fetching URLs from a database.
 * @author Emily Canto
 */
public class Database {
    private static final String DATABASE_FILE = "jdbc:sqlite:urls.db";
    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS URLS (URL  TEXT    NOT NULL    UNIQUE);";
    private static final String INSERT_TABLE_STATEMENT = "INSERT OR IGNORE INTO URLS (URL) VALUES ( ? );";
    private static final String SELECT_INT_STATEMENT = "SELECT ROWID FROM URLS WHERE URL = ?;";
    private static final String SELECT_URL_STATEMENT = "SELECT URL FROM URLS WHERE ROWID = ?;";


    public Database() {}
    /**
     * Create the database to store full URLs in if it has not been created already.
     */
    public void createDatabase() {
        try {
            DriverManager.getConnection(DATABASE_FILE); // create a new database if it does not exist already
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
    }

    /**
     * Setup the table inside the database.
     */
    public void setupDatabase() {
        try {
            Connection conn = DriverManager.getConnection(DATABASE_FILE);
            Statement statement = conn.createStatement();
            statement.executeUpdate(CREATE_TABLE_STATEMENT);
            statement.close();
            conn.close();
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
    }

    /**
     * Add a long URL to the database, if it does not exist.
     * If it already exists, it is ignored.
     * Then, the shortened URL code is returned.
     * @param url The long URL to add to the database.
     * @return The shortened URL code. Null if URL is not valid.
     */
    public String addURL(String url) {
        // Clean unnecessary parts from URL
        // Prevents duplicates of the same equivalent addresses.
        String cleanedURL = cleanURL(url);
        try {
            Connection conn = DriverManager.getConnection(DATABASE_FILE);
            PreparedStatement preparedStatement = conn.prepareStatement(INSERT_TABLE_STATEMENT);
            preparedStatement.setString(1, cleanedURL);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            conn.close();
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
        return Shortener.intToShortURL(getIDFromURL(cleanedURL));
    }

    /**
     * Get the ID of the full URL from the database.
     * @param url The full URL.
     * @return The ID of the full URL.
     */
    private int getIDFromURL(String url) {
        int row = 0;
        try {
            Connection conn = DriverManager.getConnection(DATABASE_FILE);
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_INT_STATEMENT);
            preparedStatement.setString(1, url);
            ResultSet results = preparedStatement.executeQuery();

            row = results.getInt("ROWID");

            preparedStatement.close();
            conn.close();
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
        return row;
    }

    /**
     * Get the full URL from the shortened URL code.
     * @param shortURL The shortened URL code to find.
     * @return The full URL, if it exists.
     */
    public String getFullURL(String shortURL) {
        String fullURL = "";
        shortURL = cleanURL(shortURL);
        try {
            Connection conn = DriverManager.getConnection(DATABASE_FILE);
            PreparedStatement preparedStatement = conn.prepareStatement(SELECT_URL_STATEMENT);
            preparedStatement.setString(1, Integer.toString(Shortener.shortURLToInt(shortURL)));
            ResultSet results = preparedStatement.executeQuery();
            fullURL = results.getString("URL");
            preparedStatement.close();
            conn.close();
        } catch (SQLException error) {
            System.err.println(error.getMessage());
        }
        return fullURL;
    }

    /**
     * Clean the URL of any unnecessary parts.
     * @param url The full URL to clean.
     * @return The cleaned full URL.
     */
    private static String cleanURL(String url) {
        if (url.startsWith("http://")) url = url.substring(7); // remove http://
        if (url.startsWith("https://")) url = url.substring(8); // remove https://
        if (url.startsWith("www.")) url = url.substring(4); // remove www.
        if (url.charAt(url.length() - 1) == '/') url = url.substring(0, url.length() - 1); //remove / at end
        return url;
    }
}
