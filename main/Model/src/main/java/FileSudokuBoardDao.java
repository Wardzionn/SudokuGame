import exceptions.LoadFromFileException;
import exceptions.WriteToFileException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {
    String fileName;

    FileSudokuBoardDao(String fileName) {
        this.fileName = fileName;
    }

    private final Logger logger = LoggerFactory.getLogger(SudokuBoard.class);
    private final ResourceBundle bundle = ResourceBundle.getBundle("bundles.exceptions");

    @Override
    public SudokuBoard read() throws LoadFromFileException {
        try (FileInputStream fin = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fin)) {
            SudokuBoard board = (SudokuBoard) ois.readObject();
            return board;
        }  catch (IOException | ClassNotFoundException e) {
            logger.warn(e.getMessage());
            throw new LoadFromFileException(bundle.getString("LOAD_FROM_FILE"), e);
        }
    }

    @Override
    public void write(SudokuBoard board) throws WriteToFileException {
        try (FileOutputStream fout = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(board);
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new WriteToFileException(bundle.getString("WRITE_TO_FILE"), e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
