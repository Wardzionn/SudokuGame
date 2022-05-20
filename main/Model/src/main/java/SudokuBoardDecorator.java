public class SudokuBoardDecorator extends SudokuBoard {
    private SudokuBoard baseBoard;
    private SudokuBoard endBoard;

    public SudokuBoardDecorator(SudokuSolver solver) {
        super(solver);
        this.baseBoard = new SudokuBoard(solver);
        this.endBoard = new SudokuBoard(solver);
    }

    public void setBaseBoard(SudokuBoard baseBoard) {
        this.baseBoard = baseBoard;
    }

    public void setEndBoard(SudokuBoard board) {
        this.endBoard = board;
    }

    public void clearEndBoard() {
        for (int i = 0; i < baseBoard.getBoardSize(); i++) {
            for (int j = 0; j < baseBoard.getBoardSize(); j++) {
                endBoard.set(i, j, 0);
            }
        }
    }

    public SudokuBoard getBaseBoard() {
        return baseBoard;
    }

    public SudokuBoard getEndBoard() {
        return endBoard;
    }

}
