package nonogram.gui;

import nonogram.Assign;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Stack;

/**
 * display buttons and perform actions
 *
 * @author Niteesh
 * @version March 2023
 */
public class ControlNonPanel extends BaseNonPanel {
    /**
     * The constant BTN_NORMAL_COLOR.
     */
    public static Color BTN_NORMAL_COLOR = Color.decode("#4801a2");
    /**
     * The constant BTN_HOVER_COLOR.
     */
    public static Color BTN_HOVER_COLOR = Color.decode("#8041d5");
    /**
     * The Help btn.
     */
    private JButton helpBtn;
    /**
     * The Clear btn.
     */
    private JButton clearBtn;
    /**
     * The Undo btn.
     */
    private JButton undoBtn;
    /**
     * The Load non btn.
     */
    private JButton loadNonBtn;
    /**
     * The Save moves btn.
     */
    private JButton saveMovesBtn;
    /**
     * The Load moves btn.
     */
    private JButton loadMovesBtn;
    /**
     * The Assign stack.
     */
    private Stack<Assign> assignStack = new Stack<>();

    /**
     * Default constructor
     */
    public ControlNonPanel(NonogramGUI driver) {
        super(driver);

        setLayout(new GridLayout(3, 2));

        helpBtn = new JButton("Help");
        helpBtn.addActionListener(helpHandler());
        add(helpBtn);
        setButtonColor(helpBtn);

        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(clearHandler());
        add(clearBtn);
        setButtonColor(clearBtn);

        undoBtn = new JButton("Undo");
        undoBtn.addActionListener(undoHandler());
        add(undoBtn);
        setButtonColor(undoBtn);

        loadNonBtn = new JButton("Load non game");
        loadNonBtn.addActionListener(loadNonHandler());
        add(loadNonBtn);
        setButtonColor(loadNonBtn);

        saveMovesBtn = new JButton("Save moves");
        saveMovesBtn.addActionListener(saveMovesHandler());
        add(saveMovesBtn);
        setButtonColor(saveMovesBtn);

        loadMovesBtn = new JButton("Load moves");
        loadMovesBtn.addActionListener(loadMovesHandler());
        add(loadMovesBtn);
        setButtonColor(loadMovesBtn);

    }

    /**
     * Set button color.
     *
     * @param btn the btn
     */
    private static void setButtonColor(JButton btn) {
        btn.setBackground(BTN_NORMAL_COLOR);
        btn.setForeground(Color.WHITE);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BTN_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BTN_NORMAL_COLOR);
            }
        });
    }

    /**
     * Accept file name file.
     *
     * @param title   the title
     * @param message the message
     * @param ext     the ext
     * @return the file
     */
    public static File acceptFileName(String title, String message, String ext) {
        JFileChooser fch = new JFileChooser(".");
        fch.setDialogTitle(title);
        javax.swing.filechooser.FileFilter fileFilter = new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String filePath = f.getAbsolutePath();
                return (f.isFile() && filePath.endsWith(ext));
            }

            @Override
            public String getDescription() {
                return ext;
            }
        };
        fch.setFileFilter(fileFilter);
        if (title.startsWith("Save")) {
            if (fch.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                if (!fch.getSelectedFile().getName().endsWith(".mov")) {
                    fch.setSelectedFile(new File(fch.getSelectedFile().getParentFile(), fch.getSelectedFile().getName() + ".mov"));
                }
                return fch.getSelectedFile();
            }
        } else if (title.startsWith("Load")) {
            if (fch.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                return fch.getSelectedFile();
            }
        }
        return null;
    }

    /**
     * Help handler action listener.
     *
     * @return the action listener
     */
    public ActionListener helpHandler() {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String help = "<html>Objective: Fill the colors as per the given pattern<br><br>";
                help += "Left click on unknown cell turns into purple color<br>";
                help += "Right click on unknown cell makes it empty by changing to pink color<br>";
                help += "left or right click on filled or empty cell turns into unknown cell<br><br>";
                help += "Load non file - load a new game<br>";
                help += "Save mov file - saves moves made into a .mov file<br>";
                help += "Load mov file - loads .mov file";
                pop("Help", help);

            }
        };
    }

    /**
     * Clear handler action listener.
     *
     * @return the action listener
     */
    public ActionListener clearHandler() {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearGame();

            }
        };
    }

    /**
     * Undo handler action listener.
     *
     * @return the action listener
     */
    public ActionListener undoHandler() {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Assign assignFromStack = popFromStack();

                if (assignFromStack == null) {
                    error("No more moves to undo");
                    return;
                }

                Assign reverseAssign = assignFromStack.reverse();
                getPuzzle().setState(reverseAssign);
                driver.displayCell(reverseAssign);
                driver.revalidateBoard();

            }
        };
    }

    /**
     * Load non handler action listener.
     *
     * @return the action listener
     */
    public ActionListener loadNonHandler() {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                loadNonGame();
            }
        };
    }

    /**
     * Save moves handler action listener.
     *
     * @return the action listener
     */
    public ActionListener saveMovesHandler() {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                saveMoves();
            }
        };
    }

    /**
     * Load moves handler action listener.
     *
     * @return the action listener
     */
    public ActionListener loadMovesHandler() {
        return new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                loadMoves();
            }
        };
    }

    /**
     * Clear game.
     */
    public void clearGame() {
        clearStack();
        getPuzzle().clear();
        driver.refreshBoard();
    }

    /**
     * load the non file
     */
    private void loadNonGame() {

        try {
            File myFile = acceptFileName("Load .non file", "Mention a .non file to load", ".non");

            if (myFile == null)
                return;

            driver.loadNon(myFile.getAbsolutePath());

            log("Successfully loaded the game from file.");
        } catch (Exception ex) {
            error("Error while loading non. " + ex.getMessage());
        }
    }

    /**
     * load the moves from the user provided file
     */
    private void loadMoves() {
        try {
            File myFile = acceptFileName("Load .mov file", "Enter the name of the .mov file to load", ".mov");

            if (myFile == null) {
                return;
            }

            List<String> lines = Files.readAllLines(myFile.toPath());
            String states = String.join("", lines);

            clearGame();
            getPuzzle().setStatesByString(states);
            driver.refreshBoard();

            log("Successfully loaded moves from file.");
        } catch (IOException e) {
            error("Error while loading moves: " + e.getMessage());
        }
    }

    /**
     * save state of all cells to text file
     * user is prompted to provide a file name (optionally with path)
     */
    private void saveMoves() {
        String states = getStatesAsString();

        try {
            File myFile = acceptFileName("Save .mov file", "Enter a name for the .mov file to save", ".mov");

            if (myFile == null) {
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(myFile))) {
                writer.write(states);
            }

            log("Moves successfully saved to file.");
        } catch (IOException e) {
            error("An error occurred while saving moves: " + e.getMessage());
        }
    }

    /**
     * Gets states as string.
     *
     * @return the states as string
     */
    private String getStatesAsString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getPuzzle().getNumRows(); i++) {
            for (int j = 0; j < getPuzzle().getNumCols(); j++) {
                sb.append(getPuzzle().getState(i, j));
            }
        }

        return sb.toString();
    }

    /**
     * place the assign into stack
     */
    public void pushToStack(Assign assign) {

        Assign copy = assign.copy();
        assignStack.push(copy);
        undoBtn.setEnabled(true);
    }

    /**
     * return the top most assign
     * returns null if stack is empty
     *
     * @return assign
     */
    public Assign popFromStack() {
        Assign assign = assignStack.isEmpty() ? null : assignStack.pop();

        if (assignStack.isEmpty()) {
            undoBtn.setEnabled(false);
        }

        return assign;
    }

    /**
     * Clear stack.
     */
    public void clearStack() {
        assignStack.clear();
        undoBtn.setEnabled(false);
    }

}
