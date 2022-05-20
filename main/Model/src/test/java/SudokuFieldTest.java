import static org.junit.jupiter.api.Assertions.*;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;


class SudokuFieldTest {

    @Test
    public void setFieldValueTest() {
        SudokuField field = new SudokuField();
        assertEquals(field.getFieldValue(),0);
        field.setFieldValue(11);
        assertEquals(field.getFieldValue(),11);
        field.setFieldValue(9);
        assertEquals(field.getFieldValue(),9);
        field.setFieldValue(5);
        assertEquals(field.getFieldValue(),5);
        field.setFieldValue(12);
        assertEquals(field.getFieldValue(),12);
    }

    @Test
    public void toStringTest() {
        ToStringVerifier.forClass(SudokuField.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .withIgnoredFields("valueProperty")
                .verify();
    }

    @Test
    public void simpleEqualsContract() {
        EqualsVerifier.simple().forClass(SudokuField.class).verify();

    }

    @Test
    public void equalsHashcodeCohesionTest(){
        SudokuField field1 = new SudokuField();
        SudokuField field2 = new SudokuField();
        field2.setFieldValue(4);
        SudokuField field3 = field1;

        assertEquals(field1.equals(field3),true);
        assertEquals(field1.hashCode(),field3.hashCode());

    }

    @Test
    public void ShallowCopyTest() throws CloneNotSupportedException {
        SudokuField field = new SudokuField();
        SudokuField copy = (SudokuField) field.clone();

        assertTrue(field.equals(copy));
        assertTrue(field.getFieldValue() == copy.getFieldValue());

    }

    @Test
    public void CompareToTest(){
        SudokuField field1 = new SudokuField();
        field1.setFieldValue(4);
        SudokuField fieldGreater = new SudokuField();
        fieldGreater.setFieldValue(5);
        SudokuField fieldEqual = new SudokuField();
        fieldEqual.setFieldValue(4);
        SudokuField fieldSmaller = new SudokuField();
        fieldSmaller.setFieldValue(1);

        assertThrows(NullPointerException.class, () -> {
           field1.compareTo(null);
        });

        assertEquals(field1.compareTo(fieldEqual), 0);
        assertEquals(field1.compareTo(fieldGreater), -1);
        assertEquals(field1.compareTo(fieldSmaller), 1);
    }


}
