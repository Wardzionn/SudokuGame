import com.google.common.base.MoreObjects;
import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuBoard implements Serializable, Cloneable {

    private final int boardSize = 9;

    public final int numberOfFields = 81;

    private SudokuField[][] board = new SudokuField[boardSize][boardSize];

    private final SudokuSolver solver;

    public SudokuBoard(SudokuSolver solver) {
        this.solver = solver;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = new SudokuField();
            }
        }
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public int get(int x, int y) {
        return board[x][y].getFieldValue();
    }

    public SudokuField getField(int x, int y) {
        return board[x][y];
    }

    public SudokuField[][] getBoard() {
        return board;
    }

    public void set(int x, int y, int value) {
        board[x][y].setFieldValue(value);
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void solveGame() {
        solver.solve(this);
    }

    public IntegerProperty getFieldProperty(int i, int j) {
        return board[i][j].getProperty();
    }

    public int getDifficulty(int diff) {
        int level = 0;
        if (diff == 0) {
            level = Difficulty.EASY.getValue();
        } else if (diff == 1) {
            level = Difficulty.MEDIUM.getValue();
        } else if (diff == 2) {
            level = Difficulty.HARD.getValue();
        }
        return level;
    }

    public int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public void prepareGame(int diff) {
        int rx;
        int ry;
        this.solveGame();
        for (int i = 0; i < getDifficulty(diff); i++) {
            rx = getRandomNumber(0, 9);
            ry = getRandomNumber(0, 9);
            while (this.get(rx, ry) == 0) {
                rx = getRandomNumber(0, 9);
                ry = getRandomNumber(0, 9);
            }
            this.set(rx, ry, 0);
            this.getField(rx,ry).setEditable(true);
        }
    }

    public void printBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(board[i][j].getFieldValue() + " ");
            }
            System.out.println();
        }
    }

    private boolean checkBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!getRow(i).verify()
                        || !getCol(j).verify()
                        || !getBox(i, j).verify()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean publicCheckBoard() {
        return this.checkBoard();
    }

    public SudokuRow getRow(int y) {
        SudokuField[] fields = new SudokuField[9];
        SudokuField[][] newBoard = board.clone();
        for (int i = 0; i < boardSize; i++) {
            fields[i] = newBoard[y][i];
        }
        return new SudokuRow(fields);
    }

    public SudokuColumn getCol(int x) {
        SudokuField[] fields = new SudokuField[9];
        SudokuField[][] newBoard = board.clone();
        for (int i = 0; i < boardSize; i++) {
            fields[i] = newBoard[i][x];
        }
        return new SudokuColumn(fields);

    }

    public SudokuBox getBox(int x, int y) {
        SudokuField[] fields = new SudokuField[9];
        SudokuField[][] newBoard = board.clone();
        int index = 0;
        int startBoxRow = x - x % 3;
        int startBoxCol = y - y % 3;
        for (int i = startBoxRow; i < startBoxRow + 3; i++) {
            for (int j = startBoxCol; j < startBoxCol + 3; j++) {
                fields[index++] = newBoard[i][j];
            }
        }
        return new SudokuBox(fields);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuBoard)) {
            return false;
        }

        SudokuBoard that = (SudokuBoard) o;

        return new EqualsBuilder().append(board, that.board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(board).toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("board", board)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuBoard clone = (SudokuBoard) super.clone();
        SudokuField[][] boardClone = new SudokuField[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardClone[i][j] = (SudokuField) this.getField(i, j).clone();
            }
        }
        clone.board = boardClone;
        return clone;
    }
}

