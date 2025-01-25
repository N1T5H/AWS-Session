package nonogram.gui;

import javax.swing.*;
import java.io.Serial;

/**
 * The type Log panel for displaying logs
 *
 * @author Niteesh
 * @version March 2023
 */
public class LogNonPanel extends BaseNonPanel {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -2400685097805293914L;
    /**
     * The Log field.
     */
    private JTextField logField = new JTextField();

    /**
     * Instantiates a new Log panel.
     *
     * @param driver the driver
     */
    public LogNonPanel(NonogramGUI driver) {
        super(driver);

        logField.setEditable(false);

        add(logField);
    }

    /**
     * log method to log things in the panel
     *
     * @param str the str
     */
    public void log(String str) {
        logField.setText(str);
        System.out.println(str);
    }

    /**
     * Pop.
     *
     * @param title the title
     * @param str   the str
     */
    public void pop(String title, String str) {
        logField.setText(str);
        JOptionPane.showMessageDialog(null, str, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Error method to log error logs in panel.
     *
     * @param str the str
     */
    public void error(String str) {
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
        logField.setText(str);
    }
}
