import exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcSudokuBoardDaoTest {

    SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();

    @Test
    public void writeReadTest() throws Exception, DbLoadException, DbWriteException {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board1 = new SudokuBoard(solver);
        board1.solveGame();
        SudokuBoard board2;
        try (
                JdbcSudokuBoardDao dao = (JdbcSudokuBoardDao) factory.getDataBaseDao("Testowa");
        ) {
            dao.write(board1, "DodajSie");
            board2 = dao.read(1);
            assertNotSame(board1 , board2);
            assertEquals(board1,board2);
            dao.delete(1);
        }
    }

    @Test
    public void exceptionsTest() throws Exception {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board1 = new SudokuBoard(solver);
        board1.solveGame();
        SudokuBoard board2;
        try (
                JdbcSudokuBoardDao dao = factory.getDataBaseDao("NieWejdzie3");
        ) {
            dao.close();
            assertThrows(DbLoadException.class,
                    () -> {
                        dao.read(7);
                    });
            assertThrows(DbWriteException.class,
                    () -> {
                        dao.write(board1,"nieWCHODZI");
                    });
        }
    }
}


