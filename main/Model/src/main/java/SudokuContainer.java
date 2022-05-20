import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class SudokuContainer implements Serializable, Cloneable {

    private List<SudokuField> container;

    public SudokuContainer(SudokuField[] fields) {
        container = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < fields.length; i++) {
            container.set(i, fields[i]);
        }
    }

    public int getFieldValue(int i) {
        return container.get(i).getFieldValue();
    }

    public List<SudokuField> getContainer() {
        return container;
    }

    public boolean verify() {
        Set<SudokuField> set = new HashSet<>();
        for (int i = 0; i < container.size(); i++) {
            if (this.container.get(i).getFieldValue() != 0 && !set.add(container.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuContainer that = (SudokuContainer) o;

        return new EqualsBuilder().append(container, that.container).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(container).toHashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("container", container)
                .toString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuContainer clone = (SudokuContainer) super.clone();
        List<SudokuField> containerClone = new ArrayList<>();
        Iterator<SudokuField> iterator = container.iterator();
        while (iterator.hasNext()) {
            containerClone.add((SudokuField) iterator.next().clone());
        }
        clone.container = Collections.unmodifiableList(containerClone);
        return clone;
    }
}
