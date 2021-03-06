package sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.jsoup.Jsoup;
import sample.Crypters.TestCrypter;
import sample.popov.PopovUtilites.PopovUtilites;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sample.Crypters.TestCrypter.*;


public class Main extends Application {


    private double version = 2.3;
    private boolean debug = false;
    private boolean isCrypt = true;
    public static final String URL_UPLOAD_WINDOW = "http://net.citycheb.ru/upload";
    private static final String URL_TO_VIDEO_GUIDE = "http://www.youtube.com/embed/Qhowc1PSO4E?autoplay=1";
    String updateUrl = "https://drive.google.com/folderview?id=0B9Ne8mwSPZxYRlJxamZEeVcxUzQ&usp=sharing#list";
    private static final Level LOG_LEVEL = Level.CONFIG;
    private GridPane grid = new GridPane();
    private Button btn = new Button();
    private String nQuStr;
    private Integer nQu;
    private String appName = "POCT";
    private Integer subjectId = 0000;
    private int nCreatedQu;
    private final Question[][] questions = new Question[1][1];
    private Button saveTestToFile = new Button("Сохранить тест в файл");
    private String endEndResult;
    private String loadedTestName = "";
    private String debugText = "";
    private String logsLimit = "50";
    private Text scenetitle = new Text("Создание тестов для РОСТ \nВерсия " + Double.toString(version));
    private static Logger log = Logger.getLogger(Main.class.getName());
    private static Sender tlsSender = new Sender("help_review@mail.ru", getVar("ctg 45"));
    private boolean isLoaded = false;
    private Text stringUpdate;
    private int count = 0;
    private BorderPane root = new BorderPane();

    public static void main(String[] args) {launch(args);}
    Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            if (count == 0) {
                try {
                    List<String> arg = getParameters().getUnnamed();
                    if (arg.size() != 0) {
                        debugText = arg.get(0);
                        //logsLimit = arg.get(1);
                        //log.info("limit of logs from console: " + logsLimit);
                        log.info("debugText is: " + debugText);
                    }
                } catch (Exception e) {
                    log.warning(getStackTrace(e));
                }
                count++;
            }

            if (debugText.equals("debug")) {
                debug = true;
                log.config("Debug mode: " + PopovUtilites.booleanToString(debug));
            }
            System.setProperty("isDebugModeEnabled", PopovUtilites.booleanToString(debug));
            //Настраиваем лог
            settingUpLogging(/*Integer.parseInt(logsLimit*/2/*)*/);
            log.info("Starting app...");

            // Выводим в лог список файлов в папке с тестами
            log.info("List files in \"ROST_tests\" folder: ");
            File[] listFiles = new File("C:\\ROST_Tests\\").listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                log.info((i + 1) + ". " + listFiles[i].getName());
            }

            // Проверяем обновления
            checkUpdate(false);
            mainStage = primaryStage;

            createMainView(mainStage);
        } catch (Exception e) {
            log.warning(getStackTrace(e));
        }
    }

    private void createMainView(Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane(grid);
        root.getChildren().clear();
        grid.getChildren().clear();
        grid.setMinHeight(400);
        grid.setMinWidth(500);
        root.setCenter(scrollPane);

        addMenuBar(root, primaryStage, stringUpdate);

        TreeItem<File> treeFileView = createNode(new File("C:/ROST_tests"));
        treeFileView.expandedProperty().setValue(true);

        TreeView treeView = new TreeView<File>(treeFileView);

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            isLoaded = true;
            Question[] question = loadQuestions(primaryStage, loadFromFile(handleMouseClicked(event, treeView)));
            questions[0] = openQuestionsEditor(question.length, question, primaryStage, grid);
        };
        grid.setGridLinesVisible(debug);

        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

        root.setLeft(treeView);

        primaryStage.setTitle(appName);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        if (debug){
            grid.add(new Label("ВНИМАНИЕ !!! Вы находитесь в режиме \nразработчика. В этом режиме\nпрограмма работает \nНЕ стабильно. \nЕсли вы не знаете что это, \nперезапустите програму"), 0, 5);
            Label look = new Label(" <<== ВНИМАНИЕ !!!");
            grid.add(look, 1, 5);
        }

        //Заголовок
        grid.add(scenetitle, 0, 0, 2, 1);

        Button feedback = new Button("Отзыв");
        feedback.setOnAction(event3 -> sendFeedback());
        grid.add(feedback, 1, 0);

        //Название теста
        Label testName = new Label("Название теста:");
        grid.add(testName, 0, 1);
        TextField testNameTextField = new TextField();
        testNameTextField.setPromptText("Озёра. Реки. 8 класс.");
        Tooltip testNameToolTip = new Tooltip("Впишите сюда название теста.");
        testNameTextField.setTooltip(testNameToolTip);
        grid.add(testNameTextField, 1, 1);

        Label nQuLabel = new Label("Количество вопросов:      ");
        grid.add(nQuLabel, 0, 2);
        TextField nQuTextField = new TextField();
        nQuTextField.setPromptText("7");
        Tooltip nQuToolTip = new Tooltip("Впишите сюда количество вопросов.");
        nQuTextField.setTooltip(nQuToolTip);
        grid.add(nQuTextField, 1, 2);

        Label subjectIdLabel = new Label("Выберите предмет");
        grid.add(subjectIdLabel, 0, 3);
        ChoiceBox subjectIdChoiceBox = new ChoiceBox();
        Tooltip subjectIdChoiceBoxTooltip = new Tooltip("Предмет, которому принадлежит тест");
        subjectIdChoiceBox.setTooltip(subjectIdChoiceBoxTooltip);
        grid.add(subjectIdChoiceBox, 1, 3);
        subjectIdChoiceBox.setItems(FXCollections.observableArrayList(
            /*0*/"Предмета нет в списке",
            /*1*/"Химия",
            /*2*/"Природоведение",
            /*3*/"Обучение здоровью",
            /*4*/"Биология",
            /*5*/"Этика и психология семейной жизни",
            /*6*/"География",
            /*7*/"Экономика"
        ));
        subjectIdChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    if (new_val.intValue() == 0) subjectId = 0000;
                    if (new_val.intValue() == 1) subjectId = 2523;
                    if (new_val.intValue() == 2) subjectId = 2521;
                    if (new_val.intValue() == 3) subjectId = 66855;
                    if (new_val.intValue() == 4) subjectId = 2518;
                    if (new_val.intValue() == 5) subjectId = 64251;
                    if (new_val.intValue() == 6) subjectId = 2519;
                    if (new_val.intValue() == 7) subjectId = 2953;
                    if (new_val.intValue() == 8) subjectId = 0000;
                    if (new_val.intValue() == 9) subjectId = 0000;
                    if (new_val.intValue() == 10) subjectId = 0000;
                    if (new_val.intValue() == 11) subjectId = 0000;
                    if (new_val.intValue() == 12) subjectId = 0000;
                    if (new_val.intValue() == 13) subjectId = 0000;

                }
        );

        //КНОПКА СОЗДАНИЯ ВОПРОСА
        btn.setText("Далее");
        grid.add(btn, 1, 4);

        // Вводим пароль и сохраняем в массив ps[]
        if (isCrypt) TestCrypter.inputPasswordDialog();
        // if (isCrypt)decryptTests(new File(System.getProperty("user.dir") + "\\sample\\logs\\"));


        //КНОПКА СОЗДАНИЯ ВОПРОСА
        btn.setOnAction(event -> {
            log.info("Test name: " + testNameTextField.getText());
            log.info("Typed questions number: " + nQuTextField.getText());
            nQuStr = nQuTextField.getText();
            boolean fail = false;
            try {
                nQu = new Integer(nQuStr);
            } catch (NumberFormatException e) {
                fail = true;
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверное значение строки \"Количество вопросов\"", false, new GridPane());

                log.info("Неверное значение строки \"Количество вопросов\"");
                log.warning(getStackTrace(e));
            }
            questions[0] = new Question[nQu];
            if (testNameTextField.getText() != null && !testNameTextField.getText().trim().isEmpty() && nQuTextField.getText() != null && !nQuTextField.getText().trim().isEmpty() && !fail) {
                try {
                    //ЗАПУСКАЕМ ДИАЛОГИ
                    for (int i = 0; i < nQu; i++) {
                        questions[0][i] = startMakeMagic(i, primaryStage);
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", e.getMessage(), false, new GridPane());
                    log.warning(getStackTrace(e));
                }

                grid.getChildren().clear();
                GridPane pane = new GridPane();
                pane.setGridLinesVisible(debug);
                pane.setAlignment(Pos.CENTER);
                pane.setVgap(10);
                pane.setHgap(10);
                pane.setPadding(new Insets(15, 15, 15, 15));



                //СОЗДАНИЕ РЕДАКТОРА
                openQuestionsEditor(nQu, questions[0], primaryStage, pane);



                //pane.add(saveTestToFile, 2, nQu);
                primaryStage.setScene(new Scene(new ScrollPane(pane)));
                primaryStage.setMinHeight(120);
                primaryStage.setMinWidth(410);
                primaryStage.show();
            } else {
                boolean[] both = {false, false};
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (testNameTextField.getText() == null || testNameTextField.getText().trim().isEmpty()) {
                    log.info("Введён НЕправильный текст названия теста");
                    alert.setHeaderText("Введён НЕправильный текст названия теста");
                    both[0] = true;
                }
                if (nQuTextField.getText() == null || nQuTextField.getText().trim().isEmpty()) {
                    log.info("Введён НЕправильный текст количества вопросов");
                    alert.setHeaderText("Введён НЕправильный текст количества вопросов");
                    both[1] = true;
                }

                if (both[0] && both[1]) {
                    log.info("Введён НЕправильный текст названия теста и количества вопросов");
                    alert.setHeaderText("Введён НЕправильный текст названия теста и количества вопросов");
                }

                alert.showAndWait();

            }

        });


//СОЗДАНИЕ ФИНАЛЬНОГО STRING`А ИЗ МАССИВА СОХРАНЕНИЕ В ФАЙЛ
        saveTestToFile.setOnAction(event1 -> {

            String questionsFmt = "";
            log.info(questions.toString());
            for (int i = 0; i < questions[0].length; i++) {
                if (i == (nQu - 1)) {
                    questionsFmt = questionsFmt + questions[0][i].getFormatedResult(log);
                }
                else {
                    questionsFmt = questionsFmt + questions[0][i].getFormatedResult(log) + ",";
                }

            }


            finalTest test = new finalTest();

            test.testName = testNameTextField.getText();
            log.info("isLoaded on save: " + isLoaded + "\n loadedTestName: " + loadedTestName);
            if (isLoaded) test.testName = loadedTestName;
            test.questionsFmt = questionsFmt;
            test.subjectId = subjectId;
            endEndResult = test.getFormatedTest();

            log.info("На saveTestToFile" + endEndResult);
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("C:\\ROST_tests\\" + test.testName + ".rost"), "UTF-8"));
                primaryStage.setTitle(appName);
            } catch (UnsupportedEncodingException e) {
                primaryStage.setTitle(appName + " Ошибка: неподдерживаемая кодировка");
                log.warning(getStackTrace(e));
            } catch (FileNotFoundException e) {
                primaryStage.setTitle(appName + " Ошибка: файл не найден");
                log.warning(getStackTrace(e));
            }
            try {
                bw.write(endEndResult);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех !");
                alert.setHeaderText("Тест создан !");
                alert.setContentText("Тест \"" + test.testName + "\" успешно создан и сохранён в C:\\ROST_tests\\"
                        + test.testName + ".rost");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("icons/successIcon.png").toString()));
                alert.setGraphic(new ImageView(new Image(this.getClass().getResource("icons/successIcon.png").toString())));

                Label label = new Label("Информация для разработчиков");
                TextArea textArea = new TextArea(endEndResult);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);


                alert.getDialogPane().setExpandableContent(expContent);

                Optional<ButtonType> result = alert.showAndWait();
                try {
                    if (result.get() == ButtonType.OK) {
                        File file = new File("C:\\ROST_tests"); //для ОС Windows
                        Desktop desktop = null;

                        if (Desktop.isDesktopSupported()) {
                            desktop = Desktop.getDesktop();
                        }
                        try {
                            desktop.open(file);
                        } catch (IOException e) {
                            log.warning(getStackTrace(e));
                        }
                    } else primaryStage.close();
                } catch (Exception e) {
                    log.warning(getStackTrace(e));
                }


            } catch (IOException e) {
                primaryStage.setTitle(appName + " Ошибка: неизвестная ошибка");
                log.warning(getStackTrace(e));
            }
            try {
                bw.close();
            } catch (IOException e) {
                primaryStage.setTitle(appName + " Ошибка: неизвестная ошибка");
                log.warning(getStackTrace(e));
            }
            if (isCrypt) encryptTests(new File("C:\\ROST_Tests\\"), "test");
            // TODO: 04.04.2016 стоит ли шифровать логи ? если появится ошибка шифрования, то я даже не узнаю, в чём проблема.
            //if (isCrypt)encryptTests(new File(System.getProperty("user.dir") + "\\sample\\logs\\"), "log");
            primaryStage.close();
        });

        if (isCrypt)primaryStage.setOnCloseRequest(event -> {
            encryptTests(new File("C:\\ROST_Tests\\"), "test");
            // TODO: 04.04.2016 стоит ли шифровать логи ? если появится ошибка шифрования, то я даже не узнаю, в чём проблема.
            //if (isCrypt)encryptTests(new File(System.getProperty("user.dir") + "\\sample\\logs\\"), "log");
        });


        //ДЕЛАЕМ СЦЕНУ
        Scene scene = new Scene(root, 520, 375);

        primaryStage.setMinWidth(790);
        primaryStage.setMinHeight(470);

        primaryStage.setScene(scene);
        grid.setGridLinesVisible(debug);
        primaryStage.show();

        log.setLevel(Level.ALL);
        log.info("Started");
    }



    public void sendFeedback() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Отправка отзыва");
        alert.setHeaderText("Отзыв о программе");
        GridPane emailPane = new GridPane();
        emailPane.setGridLinesVisible(debug);
        Label textFeedback = new Label("Текст отзыва:");
        TextArea textField = new TextArea();
        Label name = new Label("Ваше имя: ");
        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Иванов Иван Иванович");
        Label emailOfUserLabel = new Label("Ваша почта для обратной связи: ");
        TextField emailOfUser = new TextField();
        emailOfUser.setPromptText("ваша_почта@mail.ru");
        emailPane.add(textFeedback, 0, 0);
        emailPane.add(textField, 1, 0);
        emailPane.add(name, 0, 1);
        emailPane.add(nameTextField, 1, 1);
        emailPane.add(emailOfUserLabel, 0, 2);
        emailPane.add(emailOfUser, 1, 2);
        emailPane.setVgap(12);
        alert.getDialogPane().setContent(emailPane);
        //Date currentDate = new Date();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText("Отправка ...");
            alert1.setContentText("Идёт отправка отзыва. Ваш отзыв скопирован в буфер обмена.");
            alert1.show();
            log.info("Show \"Sending ...\" dialog");
            String msgToCopy = textField.getText();
            //Копируем в буфер обмена
            if (copyToClipBoard(msgToCopy)) return;



            //Делаем красивый лист
            String systemProperties = "";
            Properties p = null;
            try {
                p = System.getProperties();
            } catch (Exception e) {
                log.warning(getStackTrace(e));
            }
            Enumeration keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                String value = (String)p.get(key);
                systemProperties = systemProperties + "\n" + key + ": " + value;
            }


            //Отправляем письмо
            tlsSender.send("[POCT] Отзыв",
                    "Текст отзыва:\n" + textField.getText() + "\n---------------------------------------------\n" +
                            "ФИО user`а:" + nameTextField.getText() + "\n" +
                            "Почта для связи с user`ом: " + emailOfUser.getText() + "\n" +
                            "Версия программы: " + Double.toString(version) + "\n" +
                            "Дата отправки: " + new Date() + "\n СИСТЕМНАЯ ИНФОРМАЦИЯ:\n-----------(http://developer-remarks.blogspot.ru/2012/12/system-get-properties.html )------------------------\n" + systemProperties +
                            "\n ДР. СИСТЕМНАЯ ИНФОРМАЦИЯ:\n-----------------------------------\n" + System.getenv(),
                    "popovanton0@gmail.com", "help_review@mail.ru", log);
            alert1.close();
            Alert alert11 = new Alert(Alert.AlertType.INFORMATION);
            alert11.setHeaderText("Отзыв успешно отправлен !");
            log.info("Feedback sended successfully");
            alert11.showAndWait();
        }
    }

    public static String loadFromFile(File file) {
        String loadedQu = "";
        if (file != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    loadedQu = loadedQu + sCurrentLine;
                }
            } catch (Exception e) {
                log.warning(getStackTrace(e));
            }
        }
        return loadedQu;
    }

    public void addMenuBar(BorderPane root, Stage primaryStage, Text stringUpdate) {
        //Меню бар
        MenuBar menuBar = new MenuBar();

        //Меню
        Menu menu = new Menu("Файл");
        MenuItem newFile = new MenuItem("Новый тест");

        newFile.setOnAction(event -> {
            //Код лля перезапуска приложения
            log.info("Choosed \"New Test\" menu");

            createMainView(mainStage);
        });

        MenuItem open = new MenuItem("Открыть...");
        open.setOnAction(event -> {
            log.info("Choosed \"Open...\" menu");
            isLoaded = true;
            log.info("Is loaded Questions: " + isLoaded);

            String loadedQu = null;

            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter fileExtension =
                    new FileChooser.ExtensionFilter(
                            "Тесты РОСТ", "*.rost");
            fc.getExtensionFilters().add(fileExtension);

            File file = fc.showOpenDialog(primaryStage);

            loadedQu = loadFromFile(file);

            Question[] loadedQuestionsFromFile = loadQuestions(primaryStage, loadedQu);
            questions[0] = openQuestionsEditor(loadedQuestionsFromFile.length, loadedQuestionsFromFile, primaryStage, grid);
        });

        MenuItem exit = new MenuItem("Выход");
        exit.setOnAction(event -> {
            log.warning("Exiting via file menu...");
            if (isCrypt) encryptTests(new File("C:\\ROST_Tests\\"), "test");
            // TODO: 04.04.2016 стоит ли шифровать логи ? если появится ошибка шифрования, то я даже не узнаю, в чём проблема.
            //if (isCrypt)encryptTests(new File(System.getProperty("user.dir") + "\\sample\\logs\\"), "log");
            System.exit(0);
        });

        // Управление тестами
        Menu testsControl = new Menu("Управление тестами");

        MenuItem uploadTest = new MenuItem("Загрузить тест в РОСТ");
        uploadTest.setOnAction(event -> {
            getHostServices().showDocument(URL_UPLOAD_WINDOW);
        });

        MenuItem changePass = new MenuItem("Сменить пароль");
        changePass.setOnAction(event -> {
            changePassword();
        });

        testsControl.getItems().addAll(uploadTest, changePass);

        /*// Настройки
        Menu settings = new Menu("Настройки");
        settings.setOnAction(event -> {
            Alert settingsAlert = new Alert(Alert.AlertType.NONE);
            settingsAlert.setTitle("Настройки");
            GridPane pane = new GridPane();
            CheckBox cryptoCheckBox = new CheckBox("Шифрование включено");
            cryptoCheckBox.setSelected(true);
            cryptoCheckBox.setOnAction(event1 -> {
                if (cryptoCheckBox.isSelected()){
                    isCrypt = true;
                }
            });
        });*/

        // О прграмме
        Menu aboutProgramm = new Menu("О прграмме");
        MenuItem update = new MenuItem("Проверить обновления");
        update.setOnAction(event -> {
            checkUpdate(true);
        });

        MenuItem site = new MenuItem("Сайт программы");
        site.setOnAction(event -> getHostServices().showDocument("http://popovanton0.github.io/POCT_new/"));

        MenuItem feedback = new MenuItem("Отправить отзыв");
        feedback.setOnAction(event -> sendFeedback());

        MenuItem help = new MenuItem("Помощь");
        help.setOnAction(event -> {
            WebView webview = new WebView();
            webview.getEngine().load(URL_TO_VIDEO_GUIDE);
            webview.setPrefSize(640, 390);

            GridPane pane = new GridPane();
            pane.add(webview, 0, 0);
            showAlert(Alert.AlertType.INFORMATION, "Видеоурок", "", true, pane);
        });

        MenuItem about = new MenuItem("О программе");
        about.setOnAction(event -> easterEgg());
        aboutProgramm.getItems().addAll(update, site, feedback, help, about);
        //                              временно
        menu.getItems().addAll(newFile, open, new SeparatorMenuItem(), exit);
        menuBar.getMenus().addAll(menu, testsControl, aboutProgramm);
        root.setTop(menuBar);
    }


    private Question[] loadQuestions(Stage primaryStage, String loadedQu) {
        // = "{\"INFO\":\"\",\"NAME\":\"Десятичные дроби\",\"ISDELETED\":false,\"DELETEDATE\":null,\"TESTGROUPID\":null,\"SUBJECTID\":2530,\"THEMES\":[{\"NUMBER\":1,\"NAME\":\"Сложение и вычитание\",\"INFO\":\"\",\"ISDELETED\":false,\"DELETEDATE\":null,\"QUESTIONS\":[{\"DELETEDATE\":null,\"NUMBER\":1,\"COMMENT\":\"\",\"ANSWERTYPE\":\"One\",\"TEXT\":\"<p>Выберите из списка десятичную дробь</p>\\n\",\"NAME\":\"Выберите из списка десятичную дробь\",\"DIFFICULTID\":9,\"ISREGISTRSENSITIVENESS\":false,\"ISSPACESENSITIVENESS\":false,\"ISDELETED\":false,\"ISREGEXPR\":false,\"ANSWERS\":[{\"NUMBER\":1,\"TEXT\":\"<p>12</p>\\n\",\"VALUE\":\"False\"},{\"NUMBER\":2,\"TEXT\":\"<p>12,5</p>\\n\",\"VALUE\":\"True\"}]}]}]}";

        log.info("loadedQu string = " + loadedQu);
        JsonReader reader = new JsonReader(new StringReader(loadedQu));
        reader.setLenient(true);
        JsonElement start = new Gson().fromJson(reader, JsonElement.class);
        JsonArray themesArray;
        JsonArray questionsArray = null;
        loadedTestName = start.getAsJsonObject().getAsJsonPrimitive("NAME").getAsString();
        log.info(loadedTestName);
        try {
            themesArray = start.getAsJsonObject().getAsJsonArray("THEMES");
            questionsArray = themesArray.get(0).getAsJsonObject().getAsJsonArray("QUESTIONS");;
        } catch (Exception e) {
            log.warning(getStackTrace(e));
            showAlert(Alert.AlertType.ERROR, "Ошибка", " 1 - Ошибка: не удалость преобразовать в JSON массив [\n" + getStackTrace(e) + "\n]", false, new GridPane());
            primaryStage.close();
        }

        //МАССИВ ВОРОСОВ
        Question[] questions;
        questions = new Question[questionsArray.size()];
        nQu = questions.length;
        if (questions.length == 0) showAlert(Alert.AlertType.ERROR, "Ошибка", " 1 - Ошибка: массив questions = 0, Main.java:196", false, new GridPane());
        //ПОЛУЧАЕМ МАССИВ ВОПРОСОВ ИЗ JSON

        try {
            for (int j = 0; j < questionsArray.size(); j++) {
                questions[j] = new Question();
                JsonObject object = (JsonObject) questionsArray.get(j);



                questions[j].qText = object.getAsJsonObject().getAsJsonPrimitive("TEXT").toString().replace("\"", "").replace("\\n", "");
                questions[j].answerType = object.getAsJsonObject().getAsJsonPrimitive("ANSWERTYPE").getAsString();
                questions[j].isRegistrSense = false;
                questions[j].isSpaceSense = false;
                questions[j].i = j + 1;

                if (object.getAsJsonObject().getAsJsonPrimitive("ISREGISTRSENSITIVENESS").getAsString().equals("true"))
                    questions[j].isRegistrSense = true;

                if (object.getAsJsonObject().getAsJsonPrimitive("ISSPACESENSITIVENESS").getAsString().equals("true"))
                    questions[j].isSpaceSense = true;

                //ПОЛУЧАЕМ ОТВЕТЫ ИЗ JSON
                JsonArray jsonAnswers = object.getAsJsonArray("ANSWERS");
                questions[j].nAn = jsonAnswers.size();
                log.info("jsonAnswers size = " + jsonAnswers.size());
                String[] answers = new String[12];
                String[] checkBoxes = new String[6];
                for (int k = 0; k < jsonAnswers.size(); k++) {
                    // Защита от null (как, например, в прямом вводе)
                    String text = "";
                    try {

                        text = jsonAnswers.get(k).getAsJsonObject().getAsJsonPrimitive("TEXT").toString().replace("\"", "");

                    } catch (Exception e) {
                        log.warning("Catched exception, because text are null");
                        text = jsonAnswers.get(k).getAsJsonObject().getAsJsonPrimitive("VALUE").toString().replace("\"", "");
                    }

                    answers[k] = text;
                    checkBoxes[k] = jsonAnswers.get(k).getAsJsonObject().getAsJsonPrimitive("VALUE").toString();

                    log.info("Loaded answerText:" + answers[k]);
                    log.info("Loaded answerValue:" + checkBoxes[k]);
                }
                questions[j].answers = answers;
                questions[j].checkBoxes = checkBoxes;

            }
        } catch (Exception e) {
            log.warning(getStackTrace(e));
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка: не удалость преобразовать в JSON массив [\n" + getStackTrace(e) + "\n]", false, new GridPane());
            primaryStage.close();
        }


        grid.getChildren().clear();
        GridPane pane = new GridPane();
        pane.setGridLinesVisible(debug);
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setPadding(new Insets(15, 15, 15, 15));


        try {
            pane.add(saveTestToFile, 2, questionsArray.size());
            grid.add(pane, 0, 0);
        } catch (Exception e) {log.warning(getStackTrace(e));}
        return questions;
    }

    public void settingUpLogging(int logsLimit) {
        try {
            File myPath = new File(System.getProperty("user.dir") + "\\sample\\logs\\" + new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss").format(new Date()) + ".ghk");
            File folderPath = new File(System.getProperty("user.dir") + "\\sample\\logs\\");
            folderPath.mkdirs();
            //Отчищаем от лишних логов
            while (folderPath.listFiles().length > logsLimit - 1) {
                if (folderPath.listFiles().length > logsLimit - 1) {
                    File[] lastFile = folderPath.listFiles();
                    lastFile[lastFile.length - 1].delete();
                }
            }
            FileHandler fh;


            try {

                fh = new FileHandler(myPath.toString(), true);
                CustomLogFormatter formatter = new CustomLogFormatter();
                fh.setFormatter(formatter);
                ConsoleHandler handler = new ConsoleHandler();
                handler.setLevel(LOG_LEVEL);
                handler.setFormatter(formatter);
                if (debug) log.addHandler(handler);
                log.setLevel(LOG_LEVEL);
                log.addHandler(fh);
                log.setUseParentHandlers(false);

                log.log(Level.WARNING,"Log setting up completed");

            } catch (SecurityException e) {
                log.warning(getStackTrace(e));
            } catch (IOException e) {
                log.warning(getStackTrace(e));
            }
        } catch (Exception e) {
            log.warning(getStackTrace(e));
        }
    }

    public void checkUpdate(Boolean isMenu) {
        try {
            Button installUpdate;
            JsonObject json = null;
            try {
                json = readJsonFromUrl("https://raw.githubusercontent.com/popovanton0/POCT_new/master/updateCheck.json");
            } catch (Exception e) {
                log.warning(getStackTrace(e));
            }

            String urlWithOutQuotes = json.get("url").toString().replace("\"", "");
            log.info("urlToUpdate: " + urlWithOutQuotes);

            if (json != null) {
                if (json.get("version").getAsDouble() > version) {
                    log.info("Доступная версия " + json.get("version").getAsDouble() + " > чем " + version);
                    String updateMsgWithOutQuotes = json.get("msgToUpdate").toString().replace("\"", "");
                    log.info("msgToUpdate: " + updateMsgWithOutQuotes);

                    GridPane updatePane = new GridPane();

                    installUpdate = new Button("Установить обновление");
                    updatePane.add(installUpdate, 2, 0);
                    installUpdate.setOnAction(event -> getHostServices().showDocument(urlWithOutQuotes));
                    // Показ уведомления об обновлении при запуске
                    showAlert(Alert.AlertType.INFORMATION, "Доступно обновление программы !", updateMsgWithOutQuotes, true, updatePane);
                } else {
                    String withOutQuotes = json.get("msgNoUpdate").toString().replace("\"", "");
                    log.info("No updates found (" + json.get("version").getAsDouble() + " <= " + version + "), msgNoUpdate: " + withOutQuotes);
                    if (isMenu)
                        showAlert(Alert.AlertType.INFORMATION, "Обновлений нет", withOutQuotes, false, new GridPane());
                }
            }
        } catch (Exception e) {
            if (isMenu)
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Нет подключения к интеренету, загрузка обновлений невозможна", false, new GridPane());
            log.info("No internet connection detected");
        }
    }

    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    private File handleMouseClicked(MouseEvent event, TreeView treeView) {
        File name = null;
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            name = (File) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
            log.info("Node click: " + name.getPath());
        }
        return name;
    }

    public boolean copyToClipBoard(String msgToCopy) {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(msgToCopy);
            try{
                clipboard.setContents(stringSelection, (clipboard1, contents) -> {});
            }catch (Exception e) {
                log.warning(getStackTrace(e));
                return true;
            }
        } catch (HeadlessException e) {
            log.warning(getStackTrace(e));
        }
        return false;
    }
    public static JsonObject readJsonFromUrl(String url)  {
        InputStream is = null;
        try {
            is = new URL(url).openStream();
        } catch (IOException e) {
            log.warning(getStackTrace(e));
            return null;
        }
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JsonObject json = new Gson().fromJson(jsonText, JsonObject.class).getAsJsonObject();
            return json;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.warning(getStackTrace(e));
            }
        }
    }
    private static String readAll(Reader rd){
        StringBuilder sb = new StringBuilder();
        int cp;
        try {
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
        } catch (IOException e) {
            log.warning(getStackTrace(e));
        }
        return sb.toString();
    }

    public Question[] openQuestionsEditor(int nQu, Question[] questions, Stage primaryStage,  GridPane pane) {
        pane.getChildren().clear();
        primaryStage.setMinWidth(970);
        saveTestToFile.setText("Сохранить изменённый тест");
        pane.add(saveTestToFile, 2, nQu);
        for (int i = 0; i < nQu; i++) {
            final int[] n = {i + 1};
            Label questionLabel = new Label("Вопрос № " + n[0]);
            Button questionButton = new Button("Изменить вопрос", new ImageView(new Image(getClass().getResourceAsStream("icons/text-edit.png"))));
            questionButton.setTooltip(new Tooltip("Редактировать " + n[0] + " вопрос"));
            questionLabel.setTooltip(new Tooltip("Редактировать " + n[0] + " вопрос"));
            pane.add(questionLabel, 0, i);
            pane.add(questionButton, 2, i);
            pane.setGridLinesVisible(debug);
            questionButton.setId(Integer.toString(i));


            Question editingQu = questions[Integer.parseInt(questionButton.getId())];
            Text questionText = null;
            try {
                questionText = new Text(Jsoup.parse(editingQu.qText.replace("amp;", "").replace("\\n", "")).text());
            } catch (Exception e) {
                log.info("NPE in openQuestionsEditor, because text of question is null");
                questionText = new Text("");
            }
            questionText.setWrappingWidth(400);
            pane.add(questionText, 1, i);

            String qText = editingQu.qText;
            boolean isRegistrSense = editingQu.isRegistrSense;
            boolean isSpaceSense = editingQu.isSpaceSense;
            int nAn = editingQu.nAn;

            String answers[] = editingQu.answers;

            String[] checkBoxes = editingQu.checkBoxes;
            n[0] = editingQu.i;
            String[] answerType = new String[1];
            answerType[0] = editingQu.answerType;

            log.info("[ПРИ РЕДАКТИРОВАНИИ " + editingQu.i + " ВОПРОСА]\nТекст " + editingQu.i + " вопроса: " + editingQu.qText);
            for (int h = 0; h < answers.length; h++) {
                int j = h + 1;
                log.info("    Текст " + j + " ответа: " + answers[h]);
            }
            for (int h = 0; h < checkBoxes.length; h++) {
                int j = h + 1;
                log.info("    Правильность " + j + " ответа: " + checkBoxes[h]);
            }
            log.info("    Код " + n[0] + " вопроса: " + editingQu.getFormatedResult(log));

            questionButton.setOnAction(event1 -> {

                questions[Integer.parseInt(questionButton.getId())] = HowManyAnswers.newWindow(
                        n[0],
                        debug,
                        this,
                        primaryStage,
                        1,
                        true,
                        qText,
                        isRegistrSense,
                        isSpaceSense,
                        nAn,
                        answers,
                        checkBoxes,
                        answerType,
                        log);
            });
        }
        log.info("Number of elements in \"editing questions\" pane: " + String.valueOf(pane.getChildren().size()));
        return questions;
    }
    public static String getf(){
        return "jxyl";
    }

    public Question startMakeMagic(int i, Window primaryStage) {
        Question question = null;
        try {
            int n = i + 1;

            log.info("number Of Question in startMakeMagic = " + n);
            question = HowManyAnswers.newWindow(n, debug, this, primaryStage, nQu, false, "1", false, false, 1, new String[1], new String[1], new String[1], log);
            log.info("Вариантов ответа на " + n + " вопрос: " + question.nAn);


            {
                String[] answersMass = question.answers;
                String[] checkBoxesMass = question.checkBoxes;
                log.info("Текст " + n + " вопроса: " + question.qText);
                for (int h = 0; h < answersMass.length; h++) {
                    int j = h + 1;
                    log.info("    Текст " + j + " ответа: " + answersMass[h]);
                }
                for (int h = 0; h < checkBoxesMass.length; h++) {
                    int j = h + 1;
                    log.info("    Правильность " + j + " ответа: " + checkBoxesMass[h]);
                }
                log.info("    Код " + n + " вопроса: " + question.getFormatedResult(log) +
                        "\n------------------------------------------------------------------");
            }
        } catch (Exception e) {
            log.warning(getStackTrace(e));
        }
        return question;
    }

    // рудимент
    public void crt(Integer nQu) {
        for (int i = 0; i < nQu; i++) {
            int n = i + 1;
            Label Qu = new Label("Вопрос " + n + ":");
            grid.add(Qu, 0, i + 4);
            TextField QuTextField = new TextField();
            grid.add(QuTextField, 1, i + 4);
            nCreatedQu = nQu;
            btn.setText("Готово !");
        }

    }
    private static String getVar(String var) {

        try {
            if (var.equals("ctg 45")){
                for (int i = 0; i < 5; i++) {
                    if (i == 0)var = "w";
                    if (i == 1)var = var + "k";
                    if (i == 2)var = var + "f";
                    if (i == 3)var = var + "i";
                }
            }else {}
        } catch (Exception e) {
            log.warning(getStackTrace(e));
        }

        return var + "watzqcij" + getf();
    }
    private TreeItem<File> createNode(final File f) {
        return new TreeItem<File>(f) {
            private boolean isLeaf;
            private boolean isFirstTimeChildren = true;
            private boolean isFirstTimeLeaf = true;

            @Override
            public ObservableList<TreeItem<File>> getChildren() {
                if (isFirstTimeChildren) {
                    isFirstTimeChildren = false;
                    super.getChildren().setAll(buildChildren(this));
                }
                return super.getChildren();
            }



            @Override
            public boolean isLeaf() {
                if (isFirstTimeLeaf) {
                    isFirstTimeLeaf = false;
                    File f = (File) getValue();
                    isLeaf = f.isFile();
                }
                return isLeaf;
            }

            private ObservableList<TreeItem<File>> buildChildren(
                    TreeItem<File> TreeItem) {
                File f = TreeItem.getValue();
                if (f == null) {
                    return FXCollections.emptyObservableList();
                }
                if (f.isFile()) {
                    return FXCollections.emptyObservableList();
                }
                File[] files = f.listFiles();
                if (files != null) {
                    ObservableList<TreeItem<File>> children = FXCollections
                            .observableArrayList();
                    for (File childFile : files) {
                        children.add(createNode(childFile));
                    }
                    return children;
                }
                return FXCollections.emptyObservableList();
            }
        };
    }

    public void easterEgg() {
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("О программе");
            alert.setHeaderText("Создатель этой программы - Попов Антон Палович\nУченик МБОУ \"СОШ №62\", 2016.");
            GridPane gr = new GridPane();
            Image img = null;
            try {
                img = new Image(getClass().getResourceAsStream("qu.class"));
            } catch (Exception e) {
                log.warning(getStackTrace(e));
            }
            ImageView imageView = new ImageView(img);

            Label label = new Label("Особая благодарность выражается \nСоколовой Ольге Борисовне, \nпринимавшей активное участие в \nтестировании ранних версий программы.");
            label.setOnScroll(event4 -> {
                label.setOnContextMenuRequested(event5 -> {
                    Alert anotherAlert = new Alert(Alert.AlertType.INFORMATION);
                    anotherAlert.setTitle("Another one Easter egg");
                    anotherAlert.setHeaderText("Ещё одна пасхалка !");
                    anotherAlert.showAndWait();
                });
            });
            label.setPadding(new Insets(10, 10, 10, 10));
            gr.add(imageView, 0, 0);
            gr.add(label, 1, 0);
            gr.setGridLinesVisible(debug);
            alert.getDialogPane().setContent(gr);
            log.warning("EASTER EGG Activated");
            alert.showAndWait();

        } catch (Exception e) {
            log.warning(getStackTrace(e));
        }
    }

    public void showAlert(Alert.AlertType alertType, String title, String headerText, boolean isPane, GridPane pane) {
        try {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            if (isPane) alert.getDialogPane().setContent(pane);
            alert.showAndWait();
        } catch (Exception e) {
            log.warning(getStackTrace(e));
        }
    }

}
