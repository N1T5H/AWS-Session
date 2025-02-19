# ✨ Nonogram Puzzle Game

Welcome to the **Nonogram Puzzle Game**, a logic-based puzzle game built in **Java Swing**! Solve challenging grid-based puzzles by filling in the correct cells based on numeric clues. 🧩

---

## 🚀 Features
- **Classic Nonogram Gameplay** - Solve puzzles using logical deduction.
- **Graphical User Interface (GUI)** - Built using **Java Swing** for a smooth experience.
- **Custom Puzzle Support** - Load different Nonogram puzzles from files.
- **Interactive Controls** - Click to fill or mark cells with different states.
- **Hint System** - Displays row/column hints dynamically.
- **Error Logging** - Provides feedback on incorrect moves.

---

## 📷 Screenshots
*(Include screenshots of the game in action here!)*

---

## 🛠️ Installation & Running the Game
### Prerequisites
Ensure you have **Java 11+** installed on your system.

### Running the Game
You can run the game using either the **command line** (recommended for developers) or **BlueJ** (easier for beginners).

#### **Option 1: Command-Line (Recommended)**
1. **Clone this repository:**
   ```bash
   git clone https://github.com/yourusername/nonogram-game.git
   cd nonogram-game
   ```
2. **Compile all Java files:**
   ```bash
   javac -d bin -sourcepath src $(find src -name "*.java")
   ```
3. **Run the game:**
   ```bash
   java -cp bin nonogram.gui.NonogramGUI
   ```

#### **Option 2: Using BlueJ**
1. Open **BlueJ**.
2. Open this project folder (`nonogram-game/`).
3. Right-click `NonogramGUI` and select **Run Main Method**.

---

## 🎮 How to Play
1. Open the game window.
2. Select a puzzle to play.
3. Use the numeric hints to determine which cells should be filled.
4. Click on a cell to mark it as filled (`@`) or empty (`X`).
5. Solve the grid by filling all required cells correctly!

---

## 📂 Project Structure
```
nonogram-game/
├── src/
│   ├── nonogram/
│   │   ├── Nonogram.java        # Core game logic
│   │   ├── NonogramGUI.java     # Main GUI interface
│   │   ├── GameBoardNon.java    # The puzzle board
│   │   ├── Constraint.java      # Logic for constraints
│   │   ├── NGPattern.java       # Manages row/column patterns
│   │   ├── LogNonPanel.java     # Logging panel
│   │   ├── HintNonPanel.java    # Hint display panel
│   │   └── ...
│
├── nons/                       # Sample Nonogram puzzles
│   ├── tiny.non
│   ├── 6.non
│   ├── test15.non
│
├── bin/                        # Compiled Java classes
├── README.md                   # This file
└── LICENSE                     # License file
```

---

## 🏗️ Contributing
Want to improve the game? Feel free to contribute!

1. Fork the repo & create a new branch.
2. Make your changes and test thoroughly.
3. Submit a pull request with detailed explanations.

---

## 📝 License
This project is licensed under the **MIT License**. See `LICENSE` for more details.

---

## ⭐ Acknowledgments
Special thanks to **Dr. Mark C. Sinclair** for the original concept and inspiration.

🚀 Happy puzzling! 🧩

