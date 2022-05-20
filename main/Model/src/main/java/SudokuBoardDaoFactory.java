import java.sql.SQLException;

public class SudokuBoardDaoFactory {

    public Dao<SudokuBoard> getFileDao(String fileName) {
        return new FileSudokuBoardDao(fileName);
    }

    public JdbcSudokuBoardDao getDataBaseDao(String fileName) throws SQLException {
        return new JdbcSudokuBoardDao(fileName);
    }
}
