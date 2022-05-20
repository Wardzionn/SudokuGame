import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuController implements Initializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Stage stage;
    public String acutalLanguage = "en";
    Locale locale = new Locale(acutalLanguage);
    public ResourceBundle bundle = ResourceBundle.getBundle("bundles.text", locale);
    public ResourceBundle bundle2 = ResourceBundle.getBundle("Authors", locale);
    SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
    JdbcSudokuBoardDao base;
    int chosenIndex;

    public MenuController() throws SQLException {
        stage = new Stage();
        base = factory.getDataBaseDao("DataBase");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Menu.fxml"));
            loader.setController(this);
            loader.setResources(bundle);
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(bundle.getString("TITLE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().add(bundle.getString("EASY"));
        choiceBox.getItems().add(bundle.getString("MEDIUM"));
        choiceBox.getItems().add(bundle.getString("HARD"));
        diffText.setText(bundle.getString("CH_DIFF"));
        titleText.setText(bundle.getString("TITLE"));
        authorsLabel.setText(bundle2.getString("Title"));
        author1Label.setText(bundle2.getString("author1"));
        author2Label.setText(bundle2.getString("author2"));
        langEN.setOnAction(actionEvent -> {
            try {
                acutalLanguage = "en";
                changeLanguage(acutalLanguage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        langPL.setOnAction(actionEvent -> {
            try {
                acutalLanguage = "pl";
                changeLanguage(acutalLanguage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        noDiffAlert.setText("");
    }

    @FXML
    Text titleText;
    @FXML
    Text diffText;
    @FXML
    Text noDiffAlert;
    @FXML
    Button closeButton;
    @FXML
    Button loadGameButton;
    @FXML
    ChoiceBox choiceBox;
    @FXML
    Button langEN;
    @FXML
    Button langPL;
    @FXML
    Button delButton;
    @FXML
    Label authorsLabel;
    @FXML
    Label author1Label;
    @FXML
    Label author2Label;


    @FXML
    public void startButtonPress(ActionEvent event) throws Exception {
        if (choiceBox.getSelectionModel().isEmpty()) {
            noDiffAlert.setText(bundle.getString("NO_DIFF_CH"));
            logger.info(bundle.getString("NO_DIFF_CH"));
        } else {
            GameScreenController sudokuController = new GameScreenController(this);
            logger.info(bundle.getString("DIFF_CH_MESS"), choiceBox.getValue());
            stage.close();
            sudokuController.showStage();
        }
    }

    @FXML
    public void loadGameButtonPress() throws Exception {
        List<String> names = base.getBoardNames();
        ChoiceDialog<String> chd = new ChoiceDialog<String>(names.get(0), names);
        chd.setHeaderText("Select a load file:");
        Optional<String> result = chd.showAndWait();
        List<Integer> indexes = base.getBoardIndexes();
        chosenIndex = indexes.get(names.indexOf(result.get()));
        GameScreenController controller = new GameScreenController(this);
        stage.close();
        controller.showStage();
        logger.info(bundle.getString("LOAD_GAME"));
    }

    public void delButtonPress() throws SQLException {
        List<Integer> indexes = base.getBoardIndexes();
        List<String> names = base.getBoardNames();
        ChoiceDialog<String> chd = new ChoiceDialog<String>(names.get(0), names);
        chd.setHeaderText("Select a save file to delete:");
        Optional<String> result = chd.showAndWait();
        base.delete(indexes.get(names.indexOf(result.get())));
    }

    @FXML
    public void closeButtonPress(ActionEvent event) {
        stage.close();
    }


    public void changeLanguage(String language) throws IOException {
        this.locale = new Locale(language);
        bundle2 = ResourceBundle.getBundle("Authors", locale);
        bundle = ResourceBundle.getBundle("bundles.text", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"), bundle);
        loader.setResources(bundle);
        loader.setController(this);
        stage.setScene(new Scene(loader.load()));
        logger.info(bundle.getString("LANG_CH"));
    }

    public void showStage() {
        stage.show();
    }


}
