public class BacktrackingSudokuSolver implements SudokuSolver {

    @Override
    public void solve(SudokuBoard board) {
        int[] drawnNumbers = new int[board.getNumberOfFields()];
        for (int i = 0; i < board.getNumberOfFields(); i++) {
            int row = i / board.getBoardSize();
            int col = i % board.getBoardSize();

            boolean isFilled = false;
            if (drawnNumbers[i] == 0) {
                drawnNumbers[i] = (int) Math.floor(Math.random() * 9 + 1);
                board.set(row, col, drawnNumbers[i]);
                do {
                    if (board.getRow(row).verify()
                            && board.getCol(col).verify()
                            && board.getBox(row, col).verify()) {
                        isFilled = true;
                        break;
                    }
                    board.set(row, col, board.get(row, col) % 9 + 1);
                } while (board.get(row, col) != drawnNumbers[i]);
            } else {
                board.set(row, col, board.get(row, col) % 9 + 1);

                while (board.get(row, col) != drawnNumbers[i]) {
                    if (board.getRow(row).verify()
                            && board.getCol(col).verify()
                            && board.getBox(row, col).verify()) {
                        isFilled = true;
                        break;
                    }
                    board.set(row, col, board.get(row, col) % 9 + 1);
                }
            }
            if (!isFilled) {
                drawnNumbers[i] = 0;
                board.set(row, col, 0);
                i -= 2;

            }
        }
    }
}