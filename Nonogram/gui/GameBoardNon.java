package nonogram.gui;

import nonogram.Assign;
import nonogram.Nonogram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * code to draw the game board
 *
 * @author Niteesh
 * @version March 2023
 */
public class GameBoardNon extends BaseNonPanel {
    /**
     * The constant FULL_COLOR.
     */
    public static Color FULL_COLOR = Color.decode("#8041d5");
    /**
     * The constant EMPTY_COLOR.
     */
    public static Color EMPTY_COLOR = Color.PINK;
    /**
     * The constant UNKNOWN_COLOR.
     */
    public static Color UNKNOWN_COLOR = Color.WHITE;

    /**
     * Default constructor
     */
    public GameBoardNon(NonogramGUI driver) {
        super(driver);
        setListener();
    }

    /**
     * Sets listener.
     */
    public void setListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Component panel = getCompAt(GameBoardNon.this, e.getPoint());
                if (panel == null || panel == GameBoardNon.this) {
                    return;
                }

                if (!(panel instanceof JLabel))
                    return;

                JLabel label = (JLabel) panel;
                changeState(e.getButton() == MouseEvent.BUTTON1, label);

                Assign assign = (Assign) label.getClientProperty("assign");
                driver.pushToStack(assign);

                label.revalidate();
                label.repaint();

                if (getPuzzle().isSolved()) {
                    pop("Info", "Puzzle solved!");
                }
            }
        });
    }

    /**
     * Gets comp at.
     *
     * @param parent the parent
     * @param p      the p
     * @return the comp at
     */
    public Component getCompAt(Container parent, Point p) {
        for (Component child : parent.getComponents()) {
            if (child.getBounds().contains(p)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Change state.
     *
     * @param isLeftClick the is left click
     * @param label       the label
     */
    public void changeState(boolean isLeftClick, JLabel label) {

        Assign assign = (Assign) label.getClientProperty("assign");

        int currState = assign.getState();

        if (assign.getState() != Nonogram.UNKNOWN) {
            assign.setState(Nonogram.UNKNOWN);
        } else {
            assign.setState(isLeftClick ? Nonogram.FULL : Nonogram.EMPTY);
        }

        assign.setPreviousState(currState);

        displayCell(label);
    }

    /**
     * sync up board with the puzzle matrix
     */
    public void refreshBoard() {
        for (Component c : getComponents()) {
            if (!(c instanceof JLabel))
                continue;

            JLabel label = (JLabel) c;
            Assign assign = (Assign) label.getClientProperty("assign");

            int newState = getPuzzle().getState(assign.getRow(), assign.getCol());
            assign.setPreviousState(Nonogram.UNKNOWN);
            assign.setState(newState);

            displayCell(label);
        }
    }

    /**
     * draw the initial board filled with unknown char
     */
    public void prepareBoard() {
        removeAll();

        int numRows = getPuzzle().getNumRows();
        int numCols = getPuzzle().getNumCols();

        setLayout(new GridLayout(0, numCols, -1, -1));
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                JLabel label = new JLabel();
                label.setPreferredSize(new Dimension(NonogramGUI.CELL_SIZE, NonogramGUI.CELL_SIZE));
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setOpaque(true);
                Assign assign = new Assign(row, col, Nonogram.UNKNOWN);
                label.putClientProperty("assign", assign);

                add(label);
                displayCell(label);
            }
        }
    }

    /**
     * Display cell.
     *
     * @param label the label
     */
    public void displayCell(JLabel label) {
        Assign assign = (Assign) label.getClientProperty("assign");
        getPuzzle().setState(assign);

        char ch = NonogramGUI.stateAsChar(assign.getState());
        Color bgcolor = UNKNOWN_COLOR;

        switch (assign.getState()) {
            case Nonogram.FULL:
                bgcolor = FULL_COLOR;
                break;
            case Nonogram.EMPTY:
                bgcolor = EMPTY_COLOR;
                break;
        }
        label.setBackground(bgcolor);
        revalidate();
    }

    /**
     * Display cell.
     *
     * @param assign the assign
     */
    public void displayCell(Assign assign) {
        JLabel label = findLabel(assign);
        if (label == null) {
            error("Unable to find the cell");
            return;
        }

        label.putClientProperty("assign", assign);
        displayCell(label);
    }

    /**
     * Find label j label.
     *
     * @param assign the assign
     * @return the j label
     */
    public JLabel findLabel(Assign assign) {
        JLabel label = null;

        int index = assign.getRow() * getPuzzle().getNumCols()
                + assign.getCol();

        Component c = getComponent(index);

        if (c instanceof JLabel) {
            label = (JLabel) c;
        }

        return label;
    }

    /**
     * Revalidate board.
     */
    public void revalidateBoard() {
        revalidate();
    }
}
