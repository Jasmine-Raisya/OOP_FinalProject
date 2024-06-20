import java.awt.Color;
import java.awt.Font;

// This class is made to declare the specification and characteristics of the "themes" that will be chosen by the user

//class: Theme Manager
// calls the Theme Class and declares as a new Theme
public class ThemeManager {
    public static Theme lightTheme = new Theme(Color.WHITE, Color.BLACK, new Font("Arial", Font.PLAIN, 14));
    public static Theme darkTheme = new Theme(Color.DARK_GRAY, Color.WHITE, new Font("Arial", Font.PLAIN, 14));
    public static Theme blueTheme = new Theme(Color.BLUE, Color.WHITE, new Font("Arial", Font.PLAIN, 14));

}
