package nonogram.gui;

import nonogram.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * display the hints of rows and columns
 *
 * @author Niteesh
 * @version March 2023
 */
public class HintNonPanel extends BaseNonPanel implements Observer {
    /**
     * The constant NORMAL_COLOR.
     */
    public static Color NORMAL_COLOR = Color.DARK_GRAY;
    /**
     * The constant SOLVED_COLOR.
     */
    public static Color SOLVED_COLOR = Color.GREEN;
    /**
     * The constant INVALID_COLOR.
     */
    public static Color INVALID_COLOR = Color.RED;
    /**
     * The Is row hint.
     */
    private boolean isRowHint;

    /**
     * The Packs.
     */
    private int[][] packs;

    /**
     * Default constructor
     */
    public HintNonPanel(boolean isRowHint, NonogramGUI driver) {
        super(driver);
        this.isRowHint = isRowHint;
    }

    /**
     * Prepare board.
     */
    public void prepareBoard() {

        removeAll();

        if (isRowHint) {
            int numRows = getPuzzle().getNumRows();
            packs = new int[numRows][];
            setLayout(new GridLayout(0, 2, -1, -1));

        } else {
            int numCols = getPuzzle().getNumCols();
            packs = new int[numCols][];
            setLayout(new GridLayout(0, numCols, -1, -1));
        }

        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        for (int i = 0; i < packs.length; i++) {

            JPanel packPanel = new JPanel();

            if (isRowHint) {
                packs[i] = getPuzzle().getRowNums(i);
                packPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            } else {
                packs[i] = getPuzzle().getColNums(i);
                packPanel.setLayout(new GridLayout(0, 1));
            }

            for (int j = 0; j < packs[i].length; j++) {
                packPanel.add(new JLabel(packs[i][j] + ""));
            }
            packPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(packPanel);

            if (isRowHint) {
                JLabel label = new JLabel((i % 10) + "");
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(label);
            }
        }

        if (!isRowHint) {
            for (int j = 0; j < packs.length; j++) {
                JLabel label = new JLabel((j % 10) + "");
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(label);
            }
        }
    }

    /**
     * Update.
     *
     * @param obj the obj
     * @param arg the arg
     */
    public void update(Observable obj, Object arg) {
        if (!(arg instanceof Cell)) {
            return;
        }

        Cell cell = (Cell) arg;
        // System.out.println("Updating cell(" + cell.getRow() + ", " + cell.getCol() + ")");

        int index = 0;
        Color textColor = NORMAL_COLOR;

        if (isRowHint) {
            index = cell.getRow() * 2;  // multiplied by 2 to skip gutter
            textColor = getPuzzle().isRowValid(cell.getRow()) ?
                    getPuzzle().isRowSolved(cell.getRow()) ?
                            SOLVED_COLOR : NORMAL_COLOR : INVALID_COLOR;
        } else {
            index = cell.getCol();
            textColor = getPuzzle().isColValid(cell.getCol()) ?
                    getPuzzle().isColSolved(cell.getCol()) ?
                            SOLVED_COLOR : NORMAL_COLOR : INVALID_COLOR;
        }

        Component c = getComponent(index);
        if (!(c instanceof JPanel)) {
            error("Unable to find the hint panel");
            return;
        }

        JPanel packPanel = (JPanel) c;
        int totalCorrectLabels = 0;
        for (Component label : packPanel.getComponents()) {
            label.setForeground(textColor);
            if (textColor.equals(SOLVED_COLOR) || textColor.equals(INVALID_COLOR)){
                totalCorrectLabels += 1;
            }
        }
        if (totalCorrectLabels == packPanel.getComponentCount()){
            if(textColor.equals(SOLVED_COLOR)) {
                packPanel.setBackground(SOLVED_COLOR);
                for (Component label : packPanel.getComponents()) {
                    label.setForeground(NORMAL_COLOR);
                }
            } else if (textColor.equals(INVALID_COLOR)) {
                packPanel.setBackground(INVALID_COLOR);
                for (Component label : packPanel.getComponents()) {
                    label.setForeground(Color.WHITE);
                }
            }

        } else {
            packPanel.setBackground(Color.WHITE);
            for (Component label : packPanel.getComponents()) {
                label.setForeground(NORMAL_COLOR);
            }
        }

    }
}
