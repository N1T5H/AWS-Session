package nonogram.gui;

import nonogram.Assign;
import nonogram.Nonogram;

import javax.swing.*;
import java.awt.*;

/**
 * main board content
 *
 * @author Niteesh
 * @version March 2023
 */
public class BaseNonPanel extends JPanel {

    /**
     * The Driver.
     */
    protected NonogramGUI driver;
    /**
     * The Log panel.
     */
    private LogNonPanel logPanel;
    /**
     * The Control panel.
     */
    private ControlNonPanel controlPanel;
    /**
     * The Hint col panel.
     */
    private HintNonPanel hintColPanel;
    /**
     * The Hint row panel.
     */
    private HintNonPanel hintRowPanel;
    /**
     * The Game board.
     */
    private GameBoardNon gameBoard;

    /**
     * Instantiates a new Base panel.
     *
     * @param driver the driver
     */
    public BaseNonPanel(NonogramGUI driver) {
        this.driver = driver;
    }

    /**
     * compose components in the main board
     */
    public BaseNonPanel setPanel() {

        logPanel = new LogNonPanel(driver);
        controlPanel = new ControlNonPanel(driver);
        hintColPanel = new HintNonPanel(false, driver);
        hintRowPanel = new HintNonPanel(true, driver);
        gameBoard = new GameBoardNon(driver);

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        this.add(hintColPanel, gbc);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(hintRowPanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        this.add(gameBoard, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        add(controlPanel, gbc);
        gbc.gridy = 3;
        add(logPanel, gbc);

        return this;
    }

    /**
     * call this method when game is loaded
     */
    public void prepareBoard() {
        hintColPanel.prepareBoard();
        hintRowPanel.prepareBoard();
        gameBoard.prepareBoard();

        getPuzzle().addObserver(hintColPanel);
        getPuzzle().addObserver(hintRowPanel);
    }

    /**
     * Push to stack.
     *
     * @param assign the assign
     */
    public void pushToStack(Assign assign) {
        controlPanel.pushToStack(assign);
    }

    /**
     * Clear stack.
     */
    public void clearStack() {
        if (controlPanel != null)
            controlPanel.clearStack();
    }

    /**
     * Clear.
     */
    public void clear() {
        clearStack();
        removeAll();
        revalidate();
    }

    /**
     * Revalidate board.
     */
    public void revalidateBoard() {
        gameBoard.revalidateBoard();
    }

    /**
     * Refresh board.
     */
    public void refreshBoard() {
        gameBoard.refreshBoard();
    }

    /**
     * Display cell.
     *
     * @param assign the assign
     */
    public void displayCell(Assign assign) {
        gameBoard.displayCell(assign);
    }

    /**
     * Log.
     *
     * @param str the str
     */
    public void log(String str) {
        if (this.getClass().equals(BaseNonPanel.class))
            logPanel.log(str);
        else
            driver.log(str);
    }

    /**
     * Pop.
     *
     * @param title the title
     * @param str   the str
     */
    public void pop(String title, String str) {
        if (this.getClass().equals(BaseNonPanel.class))
            logPanel.pop(title, str);
        else
            driver.pop(title, str);
    }

    /**
     * Error.
     *
     * @param str the str
     */
    public void error(String str) {
        if (this.getClass().equals(BaseNonPanel.class))
            logPanel.error(str);
        else
            driver.error(str);
    }

    /**
     * Gets puzzle.
     *
     * @return the puzzle
     */
    public Nonogram getPuzzle() {
        return driver.getPuzzle();
    }
}
