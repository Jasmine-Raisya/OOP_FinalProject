//This is the first foundation of the "Theme Feature" -- personalised for each user

import java.awt.Color; //Encapsulates RGB
import java.awt.Font; //Encapsulates glyphs collection

public class Theme { //First Declaration of the class: Theme

    //Initialise
    private Color backgroundColor;
    private Color textColor;
    private Font font;

    public Theme(Color backgroundColor, Color textColor, Font font) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.font = font;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public Font getFont() {
        return font;
    }
}
