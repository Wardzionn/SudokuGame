import exceptions.LoadFromFileException;
import exceptions.WriteToFileException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FIleSudokuBoardDaoTest {

    SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();

    @Test
    public void writeReadTest() throws Exception {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board1 = new SudokuBoard(solver);
        try (
            FileSudokuBoardDao dao = (FileSudokuBoardDao) factory.getFileDao("test");
            FileSudokuBoardDao dao2 = (FileSudokuBoardDao) factory.getFileDao("test2")
        ) {
            board1.set(3, 3, 3);
            dao.write(board1);
            SudokuBoard board2 = dao.read();
            assertEquals(board2.get(3, 3), board1.get(3,3));
            assertEquals(board2.get(1, 1), board1.get(1,1));
            assertThrows(LoadFromFileException.class,
                    () -> {
                        dao2.read();
                    });

        } catch (WriteToFileException e) {
            e.printStackTrace();

        } catch (LoadFromFileException e) {
            e.printStackTrace();

        }
    }





}

