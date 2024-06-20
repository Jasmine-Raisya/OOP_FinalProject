import java.util.prefs.Preferences;

public class UserPreferences {
    private Preferences prefs;
    private static final String THEME_KEY = "theme";

    public UserPreferences() {
        prefs = Preferences.userNodeForPackage(UserPreferences.class);
    }

    public void saveTheme(String themeName) {
        prefs.put(THEME_KEY, themeName);
    }

    public String loadTheme() {
        return prefs.get(THEME_KEY, "light"); // Default to light theme
    }
}
