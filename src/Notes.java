import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class Notes extends JTextPane {

    public Notes() {
        this.setFont(new Font("Helvetica", Font.PLAIN, 16));
        this.setForeground(Color.BLACK);
        this.setPreferredSize(new Dimension(800,50));
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
        this.setCharacterAttributes(attributeSet,true);
        this.setEditable(false);
        clearNotes();
    }

    public void setNote(String note) {
        this.setText("\n    Comments:   "+note);
    }

    public void clearNotes() {
        this.setText("\n    Comments:");
    }
}
