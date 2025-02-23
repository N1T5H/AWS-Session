package nonogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * A text-based user interface to a Nonogram puzzle.
 *
 * @author Dr Mark C. Sinclair
 * @version September 2022
 */

public class NonogramUI {
    public static final char EMPTY_CHAR = 'X';
    public static final char FULL_CHAR = '@'; // or use '\u2588'
    public static final char UNKNOWN_CHAR = '.';
    public static final char INVALID_CHAR = '?';
    public static final char SOLVED_CHAR = '*';
    private static final String NGFILE = "nons/tiny.non";
    private Scanner scnr = null;
    private Nonogram puzzle = null;
    private Stack<List<Assign>> assignStack = new Stack();

    /**
     * Default constructor
     */
    public NonogramUI() {
        scnr = new Scanner(System.in);
        Scanner fs = null;
        try {
            fs = new Scanner(new File(NGFILE));
        } catch (FileNotFoundException e) {
            System.out.println(NGFILE + " not found");
        }
        puzzle = new Nonogram(fs);
    }

    /**
     * Get a number from a character representation (0-9A-Za-z)
     *
     * @param c the character representation
     * @return the integer representation (or -1 on error)
     */
    public static int numFromChar(char c) {
        final String regex = "[0-9A-Za-z]";
        final Pattern pat = Pattern.compile(regex);
        if (!pat.matcher("" + c).matches())
            throw new IllegalArgumentException("c must be " + regex);
        if ((c >= '0') && (c <= '9'))
            return (int) (c - '0');
        else if ((c >= 'A') && (c <= 'Z'))
            return (int) (c - 'A' + 10);
        else if ((c >= 'a') && (c <= 'z'))
            return (int) (c - 'a' + 36);
        else
            return -1; // should never happen
    }

    /**
     * Get a character representation (0-9A-Za-z) of a number
     *
     * @param i the integer (must be positive)
     * @return the character representation (or '?' on error)
     */
    public static char numAsChar(int i) {
        if (i < 0)
            throw new IllegalArgumentException("i must be >= 0 (" + i + ")");
        if ((i >= 0) && (i < 10))
            return (char) ('0' + i);
        else if ((i >= 10) && (i < 36))
            return (char) ('A' + i - 10);
        else if ((i >= 36) && (i < 62))
            return (char) ('a' + i - 36);
        else
            return '?';
    }

    /**
     * Check if a character represents a valid cell state
     *
     * @param c the character
     * @return true if this character represents a vaild cell state, otherwise false
     */
    public static boolean isValidStateChar(char c) {
        if ((c == FULL_CHAR) ||
                (c == EMPTY_CHAR) ||
                (c == UNKNOWN_CHAR))
            return true;
        else
            return false;
    }

    /**
     * Convert a cell state sequence string into a string representation using the
     * display characters. If showFullOnly is set, the unknown cells are shown as empty.
     *
     * @param seq          the cell state sequence
     * @param showFullOnly show all non full cells as empty
     * @return the sequence ready for display
     */
    public static String seqAsChar(String seq, boolean showFullOnly) {
        if (seq == null)
            throw new IllegalArgumentException("seq cannot be null");
        if (seq.length() < Nonogram.MIN_SIZE)
            throw new IllegalArgumentException("seq cannot be shorter than " + Nonogram.MIN_SIZE);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < seq.length(); i++) {
            int state = Nonogram.UNKNOWN;
            try {
                state = Integer.parseInt(seq.substring(i, i + 1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("seq contains non number (" + seq.charAt(i) + ") in seq[" + i + "]");
            }
            if (!Cell.isValidState(state))
                throw new IllegalArgumentException("invalid state (" + state + ") in seq[" + i + "]");
            if (!showFullOnly)
                sb.append(stateAsChar(state));
            else
                sb.append(state == Nonogram.FULL ? NonogramUI.FULL_CHAR : " ");
        }
        return sb.toString();
    }

    /**
     * Convert a cell state sequence string into a string representation using the
     * display characters.
     *
     * @param seq the cell state sequence
     * @return the sequence ready for display
     */
    public static String seqAsChar(String seq) {
        return seqAsChar(seq, false);
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
     * The entry point of non-gui application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        NonogramUI ui = new NonogramUI();
        ui.menu();
    }

    /**
     * A string representation of a Nonogram puzzle suitable for console display
     * If showFullOnly is set, the unknown cells are shown as empty.
     *
     * @param showFullOnly show all non full cells as empty
     * @return a string representation of the puzzle for display
     */
    public String display(boolean showFullOnly) {
        // collect the nums for the rows and columns
        int numRows = puzzle.getNumRows();
        int numCols = puzzle.getNumCols();
        int[][] rowNums = new int[numRows][];
        int[][] colNums = new int[numCols][];
        int maxRowNumsLen = 0;
        int maxColNumsLen = 0;
        for (int row = 0; row < numRows; row++) {
            rowNums[row] = puzzle.getRowNums(row);
            if (rowNums[row].length > maxRowNumsLen)
                maxRowNumsLen = rowNums[row].length;
        }
        for (int col = 0; col < numRows; col++) {
            colNums[col] = puzzle.getColNums(col);
            if (colNums[col].length > maxColNumsLen)
                maxColNumsLen = colNums[col].length;
        }

        // nums for columns
        StringBuffer sb = new StringBuffer();
        sb.append(" ".repeat(2 * maxRowNumsLen + 4));
        sb.append("-".repeat(numCols));
        sb.append("\n");
        for (int i = 0; i < maxColNumsLen; i++) {
            sb.append(" ".repeat(2 * maxRowNumsLen + 4));
            for (int col = 0; col < numCols; col++)
                if (i < colNums[col].length)
                    sb.append(numAsChar(colNums[col][i]));
                else
                    sb.append(" ");
            sb.append("\n");
        }
        sb.append(" ".repeat(2 * maxRowNumsLen + 4));
        sb.append("-".repeat(numCols));
        sb.append("\n");
        sb.append(" ".repeat(2 * maxRowNumsLen + 4));
        for (int col = 0; col < numCols; col++)
            sb.append(alertChar(false, col));
        sb.append("\n");
        sb.append(" ".repeat(2 * maxRowNumsLen + 4));
        for (int col = 0; col < numCols; col++)
            sb.append(numAsChar(col));
        sb.append("\n\n");

        // nums for row and the grid
        for (int row = 0; row < numRows; row++) {
            sb.append("[");
            for (int i = 0; i < rowNums[row].length; i++) {
                sb.append(numAsChar(rowNums[row][i]));
                if (i < rowNums[row].length - 1)
                    sb.append(" ");
            }
            sb.append("]");
            sb.append(" ".repeat(2 * (maxRowNumsLen - rowNums[row].length)));
            sb.append(alertChar(true, row));
            sb.append(numAsChar(row) + " ");
            sb.append(seqAsChar(puzzle.getRowSequence(row), showFullOnly) + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * A string representation of a Nonogram puzzle suitable for console display
     *
     * @return a string representation of the puzzle for display
     */
    public String display() {
        return display(false);
    }

    /**
     * Provides the character to annotate each row to indicate if it is valid or solved
     *
     * @param isRow a switch to indicate this is a row (true) or column (false)
     * @param idx   the row or column number
     * @return the character to display
     */
    private char alertChar(boolean isRow, int idx) {
        if (isRow && (idx < 0 || idx >= puzzle.getNumRows()))
            throw new IllegalArgumentException("invalid idx for row (" + idx + ")");
        else if (!isRow && (idx < 0 || idx >= puzzle.getNumCols()))
            throw new IllegalArgumentException("invalid idx for col (" + idx + ")");
        if (isRow) {
            if (puzzle.isRowSolved(idx))
                return SOLVED_CHAR;
            else if (!puzzle.isRowValid(idx))
                return INVALID_CHAR;
            else
                return ' ';
        } else { // is col
            if (puzzle.isColSolved(idx))
                return SOLVED_CHAR;
            else if (!puzzle.isColValid(idx))
                return INVALID_CHAR;
            else
                return ' ';
        }
    }

    /**
     * Main control loop.  This displays the puzzle, then enters a loop displaying a menu,
     * getting the user command, executing the command, displaying the puzzle and checking
     * if further moves are possible
     */
    public void menu() {
        String command = "";
        System.out.println(display(puzzle.isSolved()));
        while (!command.equalsIgnoreCase("Quit") && !puzzle.isSolved()) {
            displayMenu();
            command = getCommand();
            execute(command);
            if (command.equalsIgnoreCase("Quit"))
                break;
            System.out.println(display(puzzle.isSolved()));
            if (puzzle.isSolved())
                System.out.println("puzzle is solved");
        }
    }

    /**
     * Display the user menu
     */
    private void displayMenu() {
        System.out.println("Commands are:");
        System.out.println("   Help               [H]");
        System.out.println("   Move               [M]");
        System.out.println("   Row multi move     [R]");
        System.out.println("   Col multi move     [C]");
        System.out.println("   Undo assignment    [U]");
        System.out.println("   Restart puzzle [Clear]");
        System.out.println("   Save to file    [Save]");
        System.out.println("   Load from file  [Load]");
        System.out.println("   To end program  [Quit]");
    }

    /**
     * Get the user command
     *
     * @return the user command string
     */
    private String getCommand() {
        System.out.print("Enter command: ");
        return scnr.nextLine();
    }

    /**
     * Execute the user command string
     *
     * @param command the user command string
     */
    private void execute(String command) {
        if (command.equalsIgnoreCase("Quit")) {
            System.out.println("Program closing down");
            System.exit(0);
        } else if (command.equalsIgnoreCase("H")) {
            help();
        } else if (command.equalsIgnoreCase("M")) {
            move();
        } else if (command.equalsIgnoreCase("R")) {
            rowMultiMove();
        } else if (command.equalsIgnoreCase("C")) {
            colMultiMove();
        } else if (command.equalsIgnoreCase("U")) {
            undo();
        } else if (command.equalsIgnoreCase("Clear")) {
            clearGame();
        } else if (command.equalsIgnoreCase("Save")) {
            saveMoves();
        } else if (command.equalsIgnoreCase("Load")) {
            loadMoves();
        } else {
            System.out.println("Unknown command (" + command + ")");
        }
    }

    /**
     * load moves from the user provided .non file
     */
    private void loadMoves() {
        String filename = getString("Enter filename to load moves: ");
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            // Clear the game before loading the moves
            clearGame();
            puzzle.setStatesByString(content);
            System.out.println("Successfully loaded the moves from file.");
        } catch (IOException ex) {
            System.out.println("Error while loading moves. " + ex.getMessage());
        }
    }

    /**
     * save state of all cells to text file (.non extension)
     * user is prompted to provide a file name (optionally with path)
     */
    private void saveMoves() {
        String filename = getString("Enter filename to store moves: ");
        String states = getStatesAsString();
        try {
            Files.write(Paths.get(filename), states.getBytes(StandardCharsets.UTF_8));
            System.out.println("Successfully wrote moves to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing moves. " + e.getMessage());
        }
    }

    /**
     * Gets states as string.
     *
     * @return the states as string
     */
    private String getStatesAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < puzzle.getNumRows(); i++) {
            for (int j = 0; j < puzzle.getNumCols(); j++) {
                sb.append(puzzle.getState(i, j));
            }
        }
        return sb.toString();
    }

    /**
     * undo the previously made move or multi-move
     */
    private void undo() {
        List<Assign> assignList = popFromStack();
        if (assignList == null) {
            System.out.println("No moves left to undo");
            return;
        }
        for (Assign assign : assignList) {
            puzzle.setState(assign.reverse());
        }
    }

    /**
     * clear the game
     */
    private void clearGame() {
        assignStack.clear();
        puzzle.clear();
    }

    /**
     * Display some use4ful help text on the puzzle
     */
    private void help() {
        System.out.println("Nonogram is a puzzle where you must colour in/fill in the grid according to the patterns");
        System.out.println("of contiguous full cells given in the rows and columns.  Full cells are shown as '" + FULL_CHAR + "',");
        System.out.println("unknown cells as '" + UNKNOWN_CHAR + "', and cells you are sure are empty as '" + EMPTY_CHAR + "'.");
        System.out.println("If a row or column is invalid (doesn't match the pattern) this will be marked with a '" + INVALID_CHAR + "'; ");
        System.out.println("a solved row or column is marked with a '" + SOLVED_CHAR + "', but it may still be wrong because of the other");
        System.out.println("columns or rows - keep trying!");
        System.out.println("");
    }

    /**
     * Make a move
     */
    private void move() {
        Assign userMove = getUserMove();
        if (userMove == null) {
            System.out.println("invalid user move");
            return;
        }
        pushToStack(userMove);
        puzzle.setState(userMove);
    }

    /**
     * Get the user's move
     *
     * @return the user move
     */
    private Assign getUserMove() {
        int row = getInt("Enter row (0 to " + numAsChar(puzzle.getNumRows() - 1) + "): ");
        if ((row < 0) || (row > (puzzle.getNumRows() - 1)))
            return null;
        int col = getInt("Enter col (0 to " + numAsChar(puzzle.getNumCols() - 1) + "): ");
        if ((col < 0) || (col > (puzzle.getNumCols() - 1)))
            return null;
        char c = getChar("Enter state ('" + EMPTY_CHAR + "','" + FULL_CHAR + "' or '" + UNKNOWN_CHAR + "'): ");
        if (!isValidStateChar(c))
            return null;
        int state = stateFromChar(c);
        return new Assign(row, col, state);
    }

    /**
     * Make a multi-column row move
     */
    private void rowMultiMove() {
        ArrayList<Assign> list = getRowMultiUserMove();
        if (list == null) {
            System.out.println("invalid user move list");
            return;
        }
        pushToStack(list);
        for (Assign a : list)
            puzzle.setState(a);
    }

    /**
     * Get the user's multi-column row move
     *
     * @return the move as list of moves (or null on error)
     */
    private ArrayList<Assign> getRowMultiUserMove() {
        int row = getInt("Enter row (0 to " + numAsChar(puzzle.getNumRows() - 1) + "): ");
        if ((row < 0) || (row > (puzzle.getNumRows() - 1)))
            return null;
        int first = getInt("Enter first col (0 to " + numAsChar(puzzle.getNumCols() - 1) + "): ");
        if ((first < 0) || (first > (puzzle.getNumCols() - 1)))
            return null;
        int last = getInt("Enter last col (0 to " + numAsChar(puzzle.getNumCols() - 1) + "): ");
        if ((last < 0) || (last > (puzzle.getNumCols() - 1)))
            return null;
        char c = getChar("Enter state ('" + EMPTY_CHAR + "','" + FULL_CHAR + "' or '" + UNKNOWN_CHAR + "'): ");
        if (!isValidStateChar(c))
            return null;
        int state = stateFromChar(c);
        int start = (first <= last) ? first : last;
        int end = (first <= last) ? last : first;
        ArrayList<Assign> list = new ArrayList<>();
        for (int col = start; col <= end; col++)
            list.add(new Assign(row, col, state));
        return list;
    }

    /**
     * Make a multi-row column move
     */
    private void colMultiMove() {
        ArrayList<Assign> list = getColMultiUserMove();
        if (list == null) {
            System.out.println("invalid user move list");
            return;
        }
        pushToStack(list);
        for (Assign a : list)
            puzzle.setState(a);
    }

    /**
     * Get the user's multi-row column move
     *
     * @return the move as an array-list of moves (or null on error)
     */
    private ArrayList<Assign> getColMultiUserMove() {
        int col = getInt("Enter col (0 to " + numAsChar(puzzle.getNumCols() - 1) + "): ");
        if ((col < 0) || (col > (puzzle.getNumCols() - 1)))
            return null;
        int first = getInt("Enter first row (0 to " + numAsChar(puzzle.getNumRows() - 1) + "): ");
        if ((first < 0) || (first > (puzzle.getNumRows() - 1)))
            return null;
        int last = getInt("Enter last row (0 to " + numAsChar(puzzle.getNumRows() - 1) + "): ");
        if ((last < 0) || (last > (puzzle.getNumRows() - 1)))
            return null;
        char c = getChar("Enter state ('" + EMPTY_CHAR + "','" + FULL_CHAR + "' or '" + UNKNOWN_CHAR + "'): ");
        if (!isValidStateChar(c))
            return null;
        int state = stateFromChar(c);
        int start = (first <= last) ? first : last;
        int end = (first <= last) ? last : first;
        ArrayList<Assign> list = new ArrayList<>();
        for (int row = start; row <= end; row++)
            list.add(new Assign(row, col, state));
        return list;
    }

    /**
     * Get an integer from the user
     *
     * @param prompt a string to prompt the user
     * @return the integer (or -1 on error)
     */
    private int getInt(String prompt) {
        if (prompt == null)
            throw new IllegalArgumentException("prompt cannot be null");
        System.out.print(prompt);
        if (!scnr.hasNext()) {
            scnr.nextLine(); // clear the line
            return -1;
        }
        char c = scnr.next().charAt(0);
        int num = numFromChar(c);
        scnr.nextLine(); // clear the line
        return num;
    }

    /**
     * Get a character from the user
     *
     * @param prompt a string to prompt the user
     * @return the character (or '?' on error)
     */
    private char getChar(String prompt) {
        if (prompt == null)
            throw new IllegalArgumentException("prompt cannot be null");
        System.out.print(prompt);
        if (!scnr.hasNext()) {
            scnr.nextLine(); // clear the line
            return '?'; // sentinel, not equal to one of the established chars
        }
        char c = scnr.next().charAt(0);
        scnr.nextLine(); // clear the line
        return c;
    }

    /**
     * Gets user input string(Helper String).
     *
     * @param prompt the prompt
     * @return the user input string
     */
    public String getString(String prompt) {
        if (prompt == null)
            throw new IllegalArgumentException("prompt cannot be null");
        System.out.print(prompt);
        if (!scnr.hasNext()) {
            scnr.nextLine(); // clear the line
            return "";
        }
        String s = scnr.next();
        scnr.nextLine(); // clear the line
        return s;
    }

    /**
     * place the assign into stack
     */
    public void pushToStack(Assign assign) {
        List<Assign> assignList = new ArrayList<>();
        assignList.add(assign);
        pushToStack(assignList);
    }

    /**
     * place items from assignList onto stack
     */
    public void pushToStack(List<Assign> assignList) {

        for (Assign assign : assignList) {
            int currState = puzzle.getState(assign.getRow(), assign.getCol());
            assign.setPreviousState(currState);
        }
        assignStack.push(assignList);
    }

    /**
     * return the top most assignList
     * returns null if stack is empty
     *
     * @return stack top element if not empty else null
     */
    public List<Assign> popFromStack() {
        return assignStack.isEmpty() ? null : assignStack.pop();
    }
}
