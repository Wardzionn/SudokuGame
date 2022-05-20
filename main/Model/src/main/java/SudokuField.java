import com.google.common.base.MoreObjects;
import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {

    private int value = 0;

    private boolean isEditable = false;

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    private transient IntegerProperty valueProperty = new SimpleIntegerProperty(value);

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        this.valueProperty.setValue(value);
        this.value = value;
    }

    public IntegerProperty getProperty() {
        return valueProperty;
    }

    public final void setPropertyValue(int value) {
        this.valueProperty = new SimpleIntegerProperty(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SudokuField)) {
            return false;
        }

        SudokuField that = (SudokuField) o;

        return new EqualsBuilder().append(value, that.value)
                .append(isEditable, that.isEditable).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).append(isEditable).toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("isEditable", isEditable)
                .toString();
    }

    @Override
    public int compareTo(SudokuField o) {
        if (o == null) {
            throw new NullPointerException("Passed object value is null");
        } else if (this.getFieldValue() == o.getFieldValue()) {
            return 0;
        } else if (this.getFieldValue() > o.getFieldValue()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
