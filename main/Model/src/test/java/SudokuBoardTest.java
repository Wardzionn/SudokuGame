import static org.junit.jupiter.api.Assertions.*;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class SudokuBoardTest {
    public final int boardSize = 9;

    SudokuSolver solver = new BacktrackingSudokuSolver();

    @Test
    public void setTest() {
        SudokuBoard sudokuBoard = new SudokuBoard(solver);
        sudokuBoard.set(1, 1, 5);
        assertEquals(5, sudokuBoard.get(1, 1));
        assertEquals(0, sudokuBoard.get(1, 2));
    }

    @Test
    public void solveGameCorrectAssignmentTest() {


        SudokuBoard sudokuBoard = new SudokuBoard(solver);
        sudokuBoard.solveGame();

        //checking if rows were correctly assigned

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int k = j + 1; k < boardSize; k++) {
                    if (sudokuBoard.get(i, j) == sudokuBoard.get(i, k)) {
                        fail("Number " + sudokuBoard.get(i, j) + " is repeated in row " + i);
                    }
                }
            }
        }

        //checking if columns were correctly assigned

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                for (int k = j + 1; k < boardSize; k++) {
                    if (sudokuBoard.get(j, i) == sudokuBoard.get(k, i)) {
                        fail("Number " + sudokuBoard.get(i, j) + " is repeated in collumn " + i);
                    }
                }
            }
        }

        // checking for correct assignment in 3x3 boxes

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int startBoxRow = i - i % 3;
                int startBoxCol = j - j % 3;
                int collisions = 0;
                for (int k = startBoxRow; k < startBoxRow + 3; k++) {
                    for (int l = startBoxCol; l < startBoxCol + 3; l++) {
                        if (sudokuBoard.get(i, j) == sudokuBoard.get(k, l)) {
                            collisions++;
                        }
                    }
                    if (collisions > 1) {
                        fail("Numbers are repeating within boxes");
                    }
                }
            }
        }


    }

    @Test
    public void solveGameRandomGenerationTest() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(solver);
        SudokuBoard sudokuBoard2 = new SudokuBoard(solver);

        sudokuBoard1.solveGame();
        sudokuBoard2.solveGame();

        int repetitions = 0;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (sudokuBoard1.get(i, j) == sudokuBoard2.get(i, j)) {
                    repetitions++;
                }
            }
        }

        assertNotEquals(repetitions, 81);

    }

    @Test
    public void checkBoardTest() {
        SudokuBoard sudokuBoard1 = new SudokuBoard(solver);
        sudokuBoard1.solveGame();

        assertTrue(sudokuBoard1.publicCheckBoard());
        for (int i = 0; i < sudokuBoard1.getBoardSize(); i++) {
            sudokuBoard1.set(i, 8, 8);
        }
        assertFalse(sudokuBoard1.publicCheckBoard());

        SudokuBoard sudokuBoard2 = new SudokuBoard(solver);
        sudokuBoard2.solveGame();

        for (int i = 0; i < sudokuBoard2.getBoardSize(); i++) {
            sudokuBoard2.set(4, i, 8);
        }
        assertFalse(sudokuBoard2.publicCheckBoard());

        SudokuBoard sudokuBoard3 = new SudokuBoard(solver);
        sudokuBoard3.solveGame();

        for (int i = 0; i < sudokuBoard3.getBoardSize(); i++) {
            for (int j = 0; j < sudokuBoard3.getBoardSize(); j++) {
                sudokuBoard3.set(i, j, j);
            }
        }

        assertFalse(sudokuBoard3.publicCheckBoard());
    }

    @Test
    public void getRowTest() {
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();

        SudokuContainer row = board.getRow(1);

        boolean check = true;

        for (int i = 0; i < 9; i++) {
            if (board.get(1, i) != row.getFieldValue(i)) {
                check = false;
            }
        }

        assertTrue(check);
    }

    @Test
    public void getColTest() {
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();

        SudokuContainer col = board.getCol(1);

        boolean check = true;

        for (int i = 0; i < 9; i++) {
            if (board.get(i, 1) != col.getFieldValue(i)) {
                check = false;
            }
        }

        assertTrue(check);
    }

    @Test
    public void getBoxTest() {
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        int x = 1;
        int y = 1;
        SudokuContainer box = board.getBox(x, y);

        boolean check = true;
        int startBoxRow = x - x % 3;
        int startBoxCol = y - y % 3;
        for (int i = startBoxRow; i < startBoxRow + 3; i++) {
            for (int j = startBoxCol; j < startBoxCol + 3; j++) {
                if (board.get(i, j) != box.getFieldValue(i * 3 + j)) {
                    check = false;
                }
            }
        }

        assertTrue(check);
    }

    @Test
    public void testToString() {
        ToStringVerifier.forClass(SudokuBoard.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .withIgnoredFields("board", "solver", "boardSize", "numberOfFields")
                .withFailOnExcludedFields(true) // with this set true, if a developer accidently adds the password to the toString(), the unit test will fail
                .verify();
    }

    @Test
    public void equalsContract() {
        SudokuSolver solver1 = new BacktrackingSudokuSolver();
        SudokuSolver solver2 = new BacktrackingSudokuSolver();

        SudokuBoard board1 = new SudokuBoard(solver1);
        SudokuBoard board2 = new SudokuBoard(solver1);
        SudokuBoard board3 = board1;
        SudokuField field1 = new SudokuField();
        assertEquals(board1.hashCode(), board3.hashCode());
        assertEquals(board1.equals(field1), false);
        assertEquals(board1.equals(board3), true);
        assertEquals(board1.equals(board2), true);

    }

    @Test
    public void equalsHashcodeCohesionTest() {
        SudokuBoard board1 = new SudokuBoard(solver);
        SudokuBoard board2 = new SudokuBoard(solver);
        SudokuBoard board3 = board1;

        assertEquals(board1.equals(board3), true);
        assertEquals(board1.hashCode(), board3.hashCode());
    }


    @Test
    public void DeepCopyTest() throws CloneNotSupportedException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuBoard boardCopy = (SudokuBoard) board.clone();

        assertTrue(board.equals(boardCopy));
        assertFalse(board.getBoard() == boardCopy.getBoard());

    }
}

