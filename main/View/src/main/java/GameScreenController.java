import exceptions.DaoException;
import exceptions.DbLoadException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameScreenController implements Initializable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    SudokuSolver solver = new BacktrackingSudokuSolver();
    SudokuBoard board1 = new SudokuBoard(solver);
    Button clickedbutton;
    private final Stage stage;
    private final MenuController menuController;
    Locale locale = new Locale("en");
    public ResourceBundle bundle = ResourceBundle.getBundle("bundles.text", locale);
    SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();
    JdbcSudokuBoardDao base;

    public GameScreenController(MenuController controller) throws SQLException {
        this.menuController = controller;
        base = factory.getDataBaseDao("DataBase");
        stage = new Stage();
        try {
            changeLanguage(menuController.acutalLanguage);
            stage.setTitle(bundle.getString("TITLE"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button1.setOnAction(actionEvent -> setnumber("1"));
        button2.setOnAction(actionEvent -> setnumber("2"));
        button3.setOnAction(actionEvent -> setnumber("3"));
        button4.setOnAction(actionEvent -> setnumber("4"));
        button5.setOnAction(actionEvent -> setnumber("5"));
        button6.setOnAction(actionEvent -> setnumber("6"));
        button7.setOnAction(actionEvent -> setnumber("7"));
        button8.setOnAction(actionEvent -> setnumber("8"));
        button9.setOnAction(actionEvent -> setnumber("9"));
        checkBoardButton.setOnAction(actionEvent -> checkSolutionButtonPress());
        try {
            if (!menuController.choiceBox.getSelectionModel().isEmpty()) {
                this.setUpDifficulty((menuController.choiceBox.getSelectionModel()
                        .getSelectedIndex()));
            } else if (menuController.choiceBox.getSelectionModel().isEmpty()) {
                this.setUpDifficulty(4);
            }
        } catch (DaoException | CloneNotSupportedException | DbLoadException e) {
            e.printStackTrace();
        }
    }

    @FXML // The FXML loader is going to inject from the layout
    Button button1;
    @FXML
    Button button2;
    @FXML
    Button button3;
    @FXML
    Button button4;
    @FXML
    Button button5;
    @FXML
    Button button6;
    @FXML
    Button button7;
    @FXML
    Button button8;
    @FXML
    Button button9;
    @FXML
    Button returnButton;
    @FXML
    GridPane tableBoard;
    @FXML
    Button checkBoardButton;
    @FXML
    Button delButton;

    @FXML
    public void keyPress() {
        tableBoard.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                setnumber(event.getText());
            }
        });
    }

    @FXML
    private void setnumber(String input) {
        if (input.matches("[1-9]") || input.matches("")) {
            if (clickedbutton != null) {
                clickedbutton.setText(input);
                clickedbutton.setStyle("");
                clickedbutton.setFont(Font.getDefault());
                clickedbutton.setFont(Font.font(20));
                int c = Integer.parseInt(clickedbutton.getId()) / 9;
                int r = Integer.parseInt(clickedbutton.getId()) % 9;
                if (input.matches("")) {
                    board1.set(c, r, 0);
                    drawGrid();
                } else {
                    board1.set(c, r, Integer.parseInt(input));
                    drawGrid();
                }
            }
        }
        board1.printBoard();
    }

    @FXML
    public void deleteButtonPress() {
       setnumber("");
    }

    @FXML
    public void returnButtonPress() {
        this.stage.close();
        menuController.choiceBox.valueProperty().setValue(null);
        menuController.showStage();
    }

    @FXML
    public void resetBoardButtonPress() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board1.getField(i,j).isEditable()) {
                    board1.set(i, j, 0);
                }
            }
        }
        Node node = tableBoard.getChildren().get(0);
        tableBoard.getChildren().clear();
        tableBoard.getChildren().add(0, node);
        makeButtons();
    }

    @FXML
    public void saveButtonPress() throws CloneNotSupportedException, DaoException, SQLException {
        // create a text input dialog
        TextInputDialog td = new TextInputDialog("Nazwa");
        // setHeaderText
        td.setHeaderText("Wpisz nazwe dla planszy ktora chcesz zapisac");
        // show the text input dialog
        Optional<String> result = td.showAndWait();
        base.write(board1, result.get());
    }

    @FXML
    private void checkSolutionButtonPress() {
        if (board1.publicCheckBoard() == true) {
            checkBoardButton.setStyle("-fx-background-color: Green");
        } else {
            checkBoardButton.setStyle("-fx-background-color: Red");
        }
    }

    @FXML
    private void setUpDifficulty(int choice)
            throws DaoException, CloneNotSupportedException, DbLoadException {
        if (choice == 4) {
            board1 = this.loadBoard(menuController.chosenIndex);
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    board1.getField(i, j).setPropertyValue(board1.get(i, j));
                }
            }
        } else {
            board1.prepareGame(choice);
        }
        makeButtons();
    }

    private void makeButtons() {
        Button button;
        StringConverter<Number> converter = new NumberStringConverter();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                 if (!board1.getField(i,j).isEditable()) {
                     button = createButton(String.valueOf(board1.get(i, j)));
                     button.setFont(Font.font(20));
                     button.getStyleClass().add("boardButtonStart");
                     button.setDisable(true);
                     button.setId(String.valueOf(i * 9 + j));
                 } else {
                     button = createButton(String.valueOf(board1.get(i, j)));
                     button.getStyleClass().add("boardButtonPlayer");
                     button.setId(String.valueOf(i * 9 + j));
                     Button finalButton = button;
                     button.setOnAction(actionEvent -> actualbutton(finalButton));
                     if (button.getText().equals("0")) {
                         button.setFont(Font.font(0));
                     } else {
                         button.setFont(Font.font(20));
                     }
                 }
                tableBoard.add(button, j, i);
                Bindings.bindBidirectional(button.textProperty(),
                        board1.getFieldProperty(i, j), converter);
            }
        }
        drawGrid();
    }

    private SudokuBoard loadBoard(int name) throws DbLoadException {
        return base.read(name);
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return button;
    }

    private void drawGrid() {
        ObservableList<Node> childrens = tableBoard.getChildren();
        for (int i = 1; i < 82; i++) {
            if (Integer.parseInt(childrens.get(i).getId()) % 9 == 2
                    || Integer.parseInt(childrens.get(i).getId()) % 9 == 5) {
                childrens.get(i).setStyle("-fx-border-color:black;"
                        + "    -fx-border-width: 0 3 0 0;"
                        + " -fx-background-insets:  1;");
            } else if (Integer.parseInt(childrens.get(i).getId()) / 9 == 2
                    || Integer.parseInt(childrens.get(i).getId()) / 9 == 5) {
                childrens.get(i).setStyle("-fx-border-color:black;"
                        + "    -fx-border-width: 0 0 3 0;"
                        + " -fx-background-insets:  1;");
            }
            if (Integer.parseInt(childrens.get(i).getId()) == 23
                    || Integer.parseInt(childrens.get(i).getId()) == 20
                    || Integer.parseInt(childrens.get(i).getId()) == 47
                    || Integer.parseInt(childrens.get(i).getId()) == 50) {
                childrens.get(i).setStyle("-fx-border-color:black;"
                        + "    -fx-border-width: 0 3 3 0;"
                        + " -fx-background-insets:  1;");
            }
        }
    }

    private void actualbutton(Button button) {
        if (clickedbutton != null) {
            clickedbutton.setStyle("");
        }
        drawGrid();
        clickedbutton = button;
        clickedbutton.setStyle("-fx-border-color: Red;"
                + "-fx-border-radius: 3");
    }

    public void changeLanguage(String language) throws IOException {
        this.locale = new Locale(language);
        bundle = ResourceBundle.getBundle("bundles.text", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameScreen.fxml"), bundle);
        loader.setResources(bundle);
        loader.setController(this);
        stage.setScene(new Scene(loader.load()));
    }

    public void showStage() {
        stage.show();
    }


}
