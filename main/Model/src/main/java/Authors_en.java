import java.util.ListResourceBundle;

public class Authors_en extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            {"Title", "Authors"},
            {"author1", "Jakub Wardyn"},
            {"author2", "Szymon Ziemecki"}

    };
}
