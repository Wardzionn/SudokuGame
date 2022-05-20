import java.util.ListResourceBundle;

public class Authors_pl extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
            {"Title", "Autorzy"},
            {"author1", "Jakub Wardyn"},
            {"author2", "Szymon Ziemecki"}

    };
}
