import exceptions.DaoException;
import exceptions.DbConnectException;
import exceptions.DbCreateTablesException;
import exceptions.DbInsertBoardException;
import exceptions.DbInsertFieldException;
import exceptions.DbLoadException;
import exceptions.DbWriteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    public static String url = "";
    private Connection conn;
    private Statement stat;

    public JdbcSudokuBoardDao(String filename) throws SQLException {
        url = "jdbc:sqlite:" + filename;
        try {
            connect();
        } catch (DbConnectException e) {
            e.printStackTrace();
        }
        try {
            createTables();
        } catch (DbCreateTablesException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws DbConnectException {
        try {
            this.conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        } catch (SQLException e) {
            throw new DbConnectException(e.getLocalizedMessage(), e);
        }
    }

    public List<String> getBoardNames() throws SQLException {
        List<String> names = new ArrayList<String>();
        String query = "SELECT " + "boardName FROM Boards";
        ResultSet readNames = stat.executeQuery(query);
        while (readNames.next()) {
            names.add(readNames.getString(1));
        }
        readNames.close();
        return names;
    }

    public List<Integer> getBoardIndexes() throws SQLException {
        List<Integer> indexes = new ArrayList<Integer>();
        String query = "SELECT " + "board_id FROM Boards";
        ResultSet readIndexes = stat.executeQuery(query);
        while (readIndexes.next()) {
            indexes.add(readIndexes.getInt(1));
        }
        readIndexes.close();
        return indexes;
    }

    @Override
    public SudokuBoard read() throws DaoException {
        return null;
    }

    public SudokuBoard read(int index) throws DbLoadException {
        SudokuBoard newBoard = new SudokuBoard(new BacktrackingSudokuSolver());
        try {
            String query = "SELECT " + "pos_x, pos_y, field_value, isEditable"
                    + " FROM fields WHERE board_id= " + index;
            ResultSet readBoard = stat.executeQuery(query);
            int x;
            int y;
            while (readBoard.next()) {
                x = readBoard.getInt(1);
                y = readBoard.getInt(2);
                newBoard.set(x, y, readBoard.getInt(3));
                if (readBoard.getBoolean(4)) {
                    newBoard.getField(x, y).setEditable(true);
                }
            }
            readBoard.close();
        } catch (SQLException e) {
            throw new DbLoadException(e.getLocalizedMessage(), e);
        }
        return newBoard;
    }

    @Override
    public void write(SudokuBoard obj) throws DaoException, SQLException {

    }

    public void write(SudokuBoard obj, String nameSudoku) throws DbWriteException {
        try {
            insertSudokuBoard(obj, nameSudoku);
        } catch (SQLException e) {
            throw new DbWriteException(e.getLocalizedMessage(), e);
        }
    }


    public void createTables() throws DbCreateTablesException {

        String createTable1 = "CREATE TABLE IF NOT EXISTS "
                + "Boards(board_id INTEGER NOT NULL PRIMARY KEY , boardName varchar(255) NOT NULL)";

        String createTable2 = "CREATE TABLE IF NOT EXISTS fields "
                + "(pos_X integer, "
                + "pos_Y INTEGER, "
                + "field_Value INTEGER , "
                + "isEditable BOOLEAN, "
                + "board_id INTEGER , "
                + "CONSTRAINT fk_boardID FOREIGN KEY (board_id) REFERENCES Boards(sudokuBoardId))";

        try (Statement statement1 = conn.createStatement()) {
            statement1.execute(createTable1);
        } catch (SQLException e) {
            throw new DbCreateTablesException(e.getLocalizedMessage(), e);
        }
        try (Statement statement2 = conn.createStatement()) {
            statement2.execute(createTable2);
        } catch (SQLException e) {
            throw new DbCreateTablesException(e.getLocalizedMessage(), e);
        }

    }

    public boolean insertSudokuField(SudokuField field, int x, int y, int boardId)
            throws DbInsertFieldException {
        try (PreparedStatement prepStmt = conn.prepareStatement(
                "insert into fields values (?, ?, ?, ?,?);")
        ) {
            prepStmt.setInt(1, x);
            prepStmt.setInt(2, y);
            prepStmt.setInt(3, field.getFieldValue());
            prepStmt.setBoolean(4, field.isEditable());
            prepStmt.setInt(5, boardId);
            prepStmt.execute();
        } catch (SQLException e) {
            throw new DbInsertFieldException(e.getLocalizedMessage(), e);
        }
        return true;
    }

    public boolean insertSudokuBoard(SudokuBoard board, String name) throws SQLException {

        try (PreparedStatement prepStmt = conn.prepareStatement(
                "insert into Boards values (?, ?);")
        ) {
            prepStmt.setString(2, name);
            prepStmt.execute();

        }
        try (ResultSet result = stat.executeQuery("SELECT MAX(rowid) FROM Boards")
        ) {


            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    insertSudokuField(board.getField(i, j), i, j, result.getInt(1));

                }
            }
            result.close();
        } catch (SQLException e) {
            throw new DbInsertBoardException(e.getLocalizedMessage(), e);
        }

        return true;
    }


    public void delete(int index) {

        try (
                PreparedStatement st = conn
                        .prepareStatement("DELETE FROM fields WHERE board_id='" + index
                        + "' ;" + " DELETE FROM Boards WHERE board_id= '" + index + "'")
        ) {
            st.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try (
                PreparedStatement st = conn
                        .prepareStatement("DELETE FROM Boards WHERE board_id= '" + index + "'");
        ) {
            st.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        conn.close();
        stat.close();
    }
}