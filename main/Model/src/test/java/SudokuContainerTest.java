import static org.junit.jupiter.api.Assertions.*;
import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


public class SudokuContainerTest {

    SudokuSolver solver = new BacktrackingSudokuSolver();

    @Test
    public void verifyTest() {
        SudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();

        boolean check = true;

        for(int i = 0 ; i< 9 ; i++){
            for(int j = 0 ; j< 9 ; j ++){
                if(!board.getRow(i).verify()
                   || board.get(i,j) == 0
                   || !board.getCol(j).verify()
                   || !board.getBox(i,j).verify()){
                        check=false;
                }
            }
        }

        assertTrue(check);
    }

    @Test
    public void containerAddExceptionTest() {
        List<SudokuField> test_container=Arrays.asList(new SudokuField[9]);
        SudokuField test = new SudokuField();
        assertThrows(UnsupportedOperationException.class,
                () -> {
                    test_container.add(test);
                });

    }

    @Test
    public void toStringTest() {
        ToStringVerifier.forClass(SudokuRow.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();

        ToStringVerifier.forClass(SudokuColumn.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();

        ToStringVerifier.forClass(SudokuBox.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }

    @Test
    public void simpleEqualsContract() {
        EqualsVerifier.simple().forClass(SudokuRow.class).verify();
        EqualsVerifier.simple().forClass(SudokuColumn.class).verify();
        EqualsVerifier.simple().forClass(SudokuBox.class).verify();
    }

    @Test
    public void equalsHashcodeCohesionTest(){
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuContainer con1 = board.getCol(1);
        SudokuContainer con2 = board.getRow(1);
        SudokuContainer con3 = con1;

        assertEquals(con1.equals(con3),true);
        assertEquals(con1.hashCode(),con3.hashCode());

    }

    @Test
    public void DeepCopyTest() throws CloneNotSupportedException {
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        SudokuContainer Col = board.getCol(1);
        SudokuContainer Row = board.getRow(1);
        SudokuContainer Box = board.getBox(1,1);

        SudokuContainer cloneCol = (SudokuContainer) Col.clone();
        SudokuContainer cloneRow = (SudokuContainer) Row.clone();
        SudokuContainer cloneBox = (SudokuContainer) Box.clone();

        assertTrue(Col.equals(cloneCol));
        assertTrue(Row.equals(cloneRow));
        assertTrue(Box.equals(cloneBox));

        assertFalse( Col.getContainer() == cloneCol.getContainer());
        assertFalse( Row.getContainer() == cloneRow.getContainer());
        assertFalse( Box.getContainer() == cloneBox.getContainer());

    }


}
