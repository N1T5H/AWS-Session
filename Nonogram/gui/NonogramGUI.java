package nonogram.gui;

import nonogram.Assign;
import nonogram.Cell;
import nonogram.Nonogram;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serial;
import java.util.Scanner;

/**
 * A GUI-based user interface to a Nonogram puzzle.
 * <p>
 * NonogramGUI acts as driver to other gui modules
 *
 * @author Dr Mark C. Sinclair
 * @version September 2022
 */

public class NonogramGUI extends JFrame {
    public static final char EMPTY_CHAR = 'X';
    public static final char FULL_CHAR = '@'; // or use '\u2588'
    public static final char UNKNOWN_CHAR = '.';
    public static final char INVALID_CHAR = '?';
    public static final char SOLVED_CHAR = '*';
    public static final int CELL_SIZE = 30;
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -4089527572300712394L;
    private static final String NGFILE = "nons/tiny.non";
    private Nonogram puzzle = null;
    private BaseNonPanel basePanel;

    /**
     * Default constructor
     */
    public NonogramGUI() {

        basePanel = new BaseNonPanel(this);
        basePanel.setPanel();
//        add(basePanel);

        setTitle("Nonogram By Niteesh 😊");

        JScrollPane png = new JScrollPane(basePanel);
        getContentPane().add(png, BorderLayout.CENTER);

        setSize(500, 400);
        setPreferredSize(getSize());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pack();

        loadNon(NGFILE);
    }

    /**
     * Convert an individual cell state into a display character
     *
     * @param state the cell state
     * @return the character ready for display
     */
    public static char stateAsChar(int state) {
        if (!Cell.isValidState(state))
            throw new IllegalArgumentException("invalid state (" + state + ")");
        if (state == Nonogram.FULL)
            return FULL_CHAR;
        else if (state == Nonogram.EMPTY)
            return EMPTY_CHAR;
        else
            return UNKNOWN_CHAR;
    }

    /**
     * Convert a display character into a Nonogram cell state
     *
     * @param c the display character
     * @return the cell state
     */
    public static int stateFromChar(char c) {
        if (!isValidStateChar(c))
            throw new IllegalArgumentException("invalid state char (" + c + ")");
        if (c == FULL_CHAR)
            return Nonogram.FULL;
        else if (c == EMPTY_CHAR)
            return Nonogram.EMPTY;
        else
            return Nonogram.UNKNOWN;
    }

    /**
     * Check if a character represents a valid cell state
     *
     * @param c the character
     * @return true if this character represents a valid cell state, otherwise false
     */
    public static boolean isValidStateChar(char c) {
        if ((c == FULL_CHAR) ||
                (c == EMPTY_CHAR) ||
                (c == UNKNOWN_CHAR))
            return true;
        return false;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        new NonogramGUI();
    }

    /**
     * load non file and refresh the board
     *
     * @param filename .non file name
     */
    public void loadNon(String filename) {
        clear();
        Scanner fs = null;
        try {
            fs = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            error(filename + " not found");
        }
        setPuzzle(new Nonogram(fs));
        basePanel.setPanel();
        prepareBoard();

        log("game loaded from " + filename);
    }

    /**
     * Prepare board.
     */
    public void prepareBoard() {
        basePanel.prepareBoard();

        revalidate();
        pack();
    }

    /**
     * Clear Board.
     */
    public void clear() {
        if (puzzle != null)
            puzzle.clear();
        basePanel.clear();
    }

    /**
     * Revalidate board.
     */
    public void revalidateBoard() {
        basePanel.revalidateBoard();
    }

    /**
     * Refresh board.
     */
    public void refreshBoard() {
        basePanel.refreshBoard();
    }

    /**
     * Display cell.
     *
     * @param assign the assign
     */
    public void displayCell(Assign assign) {
        basePanel.displayCell(assign);
    }

    /**
     * Push assign to stack.
     *
     * @param assign the assign
     */
    public void pushToStack(Assign assign) {
        basePanel.pushToStack(assign);
    }

    /**
     * Log.
     *
     * @param str the str
     */
    public void log(String str) {
        basePanel.log(str);
    }

    /**
     * Error.
     *
     * @param str the str
     */
    public void error(String str) {
        basePanel.error(str);
    }

    /**
     * Pop.
     *
     * @param title the title
     * @param str   the str
     */
    public void pop(String title, String str) {
        basePanel.pop(title, str);
    }

    /**
     * Gets Nonogram puzzle.
     *
     * @return the puzzle
     */
    public Nonogram getPuzzle() {
        return puzzle;
    }

    /**
     * Sets Nonogram puzzle.
     *
     * @param puzzle the puzzle
     */
    public void setPuzzle(Nonogram puzzle) {
        this.puzzle = puzzle;
    }
}
