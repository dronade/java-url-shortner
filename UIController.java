import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Class for binding the UI to methods.
 * @author Emily Canto
 */
public class UIController {
    private static final String DOMAIN = "shortlink.co";
    private static final String DOMAIN_SLASH = DOMAIN + "/";
    private static final String DOMAIN_HTTP = "http://"+DOMAIN_SLASH;
    private static final String DOMAIN_HTTPS = "https://"+DOMAIN_SLASH;

    @FXML
    private TextField textFieldCreate;
    @FXML
    private TextField textFieldGet;
    private final Database database = new Database();

    /**
     * Setup the database before displaying UI.
     */
    @FXML
    private void initialize() {
        database.createDatabase();
        database.setupDatabase();
    }

    /**
     * Create the short URL from the URL in the text box
     */
    @FXML
    public void generateShortURL() {
        if (textFieldCreate.getText() == null || textFieldCreate.getText().replaceAll("\\s","").isEmpty())
            return;

        textFieldCreate.setText("https://"+DOMAIN+"/" + database.addURL(textFieldCreate.getText()));
    }

    /**
     * Fetch the long URL from the short URL in the text box
     */
    @FXML
    public void generateLongURL() {
        String shortURL = textFieldGet.getText();
        if (shortURL == null || shortURL.replaceAll("\\s","").isEmpty()) return;

        if (shortURL.startsWith(DOMAIN_SLASH)) {
            textFieldGet.setText(database.getFullURL(shortURL.substring(DOMAIN_SLASH.length())));
        } else if (shortURL.startsWith(DOMAIN_HTTP)) {
            textFieldGet.setText(database.getFullURL(shortURL.substring(DOMAIN_HTTP.length())));
        } else if (shortURL.startsWith(DOMAIN_HTTPS)) {
            textFieldGet.setText(database.getFullURL(shortURL.substring(DOMAIN_HTTPS.length())));
        }

    }
}
