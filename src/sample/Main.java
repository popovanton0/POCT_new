package sample;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public class Main extends Application {

    public static final String isUpdateAvailable = "https://app.simplenote.com/publish/L787jN";
    //public static final String isUpdateAvailable = "https://raw.githubusercontent.com/popovanton0/POCT_new/gh-pages/isUpdateAvailaible.txt";
    private static Sender tlsSender = new Sender("popovanton0@gmail.com", "ilmtcbelwvtnlugv");
    double version = 2.0;
    boolean debug = true;
    String updateUrl = "https://drive.google.com/folderview?id=0B9Ne8mwSPZxYRlJxamZEeVcxUzQ&usp=sharing#list";
    GridPane grid = new GridPane();
    Button btn = new Button();
    String nQuStr;
    Integer nQu;
    String appName = "POCT";
    Integer subjectId = 0000;
    int nCreatedQu;
    finalTest test = new finalTest();
    Button saveTestToFile = new Button("Сохранить тест в файл");
    String endEndResult;
    String debugText = "";
    Text scenetitle = new Text("Создание тестов для РОСТ \nВерсия " + Double.toString(version));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List<String> arg = getParameters().getUnnamed();
        if (arg.size() != 0) {
            debugText = arg.get(0);
        }
        if (debugText.equals("debug")) {
            System.out.println("Debug mode turned on");
            debug = true;

        }
        primaryStage.setTitle(appName);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        ScrollPane root = new ScrollPane(grid);


        //Заголовок

        easterEgg();
        grid.add(scenetitle, 0, 0, 2, 1);

        Button feedback = new Button("Отзыв");
        feedback.setOnAction(event3 -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            GridPane emailPane = new GridPane();
            Label textFeedback = new Label("Текст отзыва:");
            TextField textField = new TextField();
            Label emailOfUserLabel = new Label("Ваша почта для обратной связи: ");
            TextField emailOfUser = new TextField();
            emailOfUser.setPromptText("ваша_почта@mail.ru");
            emailPane.add(textFeedback, 0, 0);
            emailPane.add(textField, 1, 0);
            emailPane.add(emailOfUserLabel, 0, 1);
            emailPane.add(emailOfUser, 1, 1);
            emailPane.setVgap(12);
            alert.getDialogPane().setContent(emailPane);

            //Date currentDate = new Date();
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                tlsSender.send("[POCT] Отзыв",
                        textField.getText() + "\n" +
                                "Почта для связи с user`ом: " + emailOfUser.getText() + "\n" +
                                "Версия программы: " + Double.toString(version) + "\n" +
                                "Дата отправки: " + new Date(), "popovanton0@gmail.com", "help_review@mail.ru");
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Отзыв успешно отправлен !");
                alert1.showAndWait();
            }
        });
        grid.add(feedback, 1, 0);

        //Название теста
        Label testName = new Label("Название теста:");
        grid.add(testName, 0, 1);
        TextField testNameTextField = new TextField();
        testNameTextField.setPromptText("Озёра. Реки. 8 класс.");
        Tooltip testNameToolTip = new Tooltip("Впишите сюда название теста.");
        testNameTextField.setTooltip(testNameToolTip);
        grid.add(testNameTextField, 1, 1);

        Label nQuLabel = new Label("Количество вопросов:");
        grid.add(nQuLabel, 0, 2);
        TextField nQuTextField = new TextField();
        nQuTextField.setPromptText("7");
        Tooltip nQuToolTip = new Tooltip("Впишите сюда количество вопросов.");
        nQuTextField.setTooltip(nQuToolTip);
        grid.add(nQuTextField, 1, 2);

        Label subjectIdLabel = getLabel("Выберите предмет");
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

        //ЗАГРУЗКА ТЕСТОВ
        Button loadTestFromFileBtn = new Button("Загрузить тест из файла");
        loadTestFromFileBtn.setTooltip(new Tooltip("Тестовая фича, находится в разработке"));
        if (debug) grid.add(loadTestFromFileBtn, 1, 5);

        loadTestFromFileBtn.setOnAction(event2 -> {
            String loadedQu = "{\"INFO\":\"\",\"NAME\":\"Десятичные дроби\",\"ISDELETED\":false,\"DELETEDATE\":null,\"TESTGROUPID\":null,\"SUBJECTID\":2530,\"THEMES\":[{\"NUMBER\":1,\"NAME\":\"Сложение и вычитание\",\"INFO\":\"\",\"ISDELETED\":false,\"DELETEDATE\":null,\"QUESTIONS\":[{\"DELETEDATE\":null,\"NUMBER\":1,\"COMMENT\":\"\",\"ANSWERTYPE\":\"One\",\"TEXT\":\"<p>Выберите из списка десятичную дробь</p>\\n\",\"NAME\":\"Выберите из списка десятичную дробь\",\"DIFFICULTID\":9,\"ISREGISTRSENSITIVENESS\":false,\"ISSPACESENSITIVENESS\":false,\"ISDELETED\":false,\"ISREGEXPR\":false,\"ANSWERS\":[{\"NUMBER\":1,\"TEXT\":\"<p>12</p>\\n\",\"VALUE\":\"False\"},{\"NUMBER\":2,\"TEXT\":\"<p>12,5</p>\\n\",\"VALUE\":\"True\"}]}]}]}";
            JsonElement start = new Gson().fromJson(loadedQu, JsonElement.class);
            JsonArray themesArray = null;
            JsonArray questionsArray = null;
            try {
                themesArray = start.getAsJsonObject().getAsJsonArray("THEMES");
                questionsArray = (JsonArray) themesArray.get(0).getAsJsonObject().getAsJsonArray("QUESTIONS");;
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Ошибка", " 1 - Ошибка: не удалость преобразовать в JSON массив [\n" + e.toString() + "\n]", false, new GridPane());
                primaryStage.close();
            }

            //МАССИВ ВОРОСОВ
            Question[] questions;
            questions = new Question[questionsArray.size()];
            if (questions.length == 0) showAlert(Alert.AlertType.ERROR, "Ошибка", " 1 - Ошибка: массив questions = 0, Main.java:196", false, new GridPane());
            //ПОЛУЧАЕМ МАССИВ ВОПРОСОВ ИЗ JSON

            try {
                for (int j = 0; j < questionsArray.size(); j++) {
                    questions[j] = new Question();
                    JsonObject object = (JsonObject) questionsArray.get(j);


                    questions[j].qText = object.getAsJsonObject().getAsJsonPrimitive("TEXT").toString();
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
                    String[] answers = new String[jsonAnswers.size()];
                    boolean[] checkBoxes = new boolean[jsonAnswers.size()];
                    for (int k = 0; k < answers.length; k++) {
                        answers[k] = jsonAnswers.get(k).getAsJsonObject().getAsJsonPrimitive("TEXT").toString();
                        if (jsonAnswers.get(k).getAsJsonObject().getAsJsonPrimitive("VALUE").toString() == "True")
                            checkBoxes[k] = true;
                        else checkBoxes[k] = false;
                    }
                    questions[j].answers = answers;
                    questions[j].checkBoxes = checkBoxes;

                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка: не удалость преобразовать в JSON массив [\n" + e.toString() + "\n]", false, new GridPane());
                primaryStage.close();
            }


            grid.getChildren().clear();
            GridPane pane = new GridPane();
            pane.setGridLinesVisible(debug);
            pane.setAlignment(Pos.CENTER);
            pane.setVgap(10);
            pane.setHgap(10);
            pane.setPadding(new Insets(15, 15, 15, 15));



            //СОЗДАНИЕ РЕДАКТОРА
            openQuestionsEditor(questions.length, questions, primaryStage, pane);


            try {
                pane.add(saveTestToFile, 2, questionsArray.size());
                grid.add(pane, 0, 0);
            } catch (Exception e) {}
        });

        //КНОПКА СОЗДАНИЯ ВОПРОСА
        btn.setText("Далее");
        grid.add(btn, 1, 4);


        final Question[][] questions = new Question[1][1];


        btn.setOnAction(event -> {
            nQuStr = nQuTextField.getText();
            boolean fail = false;
            try {
                nQu = new Integer(nQuStr);
            } catch (NumberFormatException e) {
                fail = true;
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверное значение строки \"Количество вопросов\"", false, new GridPane());
                if (debug) System.out.println("Неверное значение строки \"Количество вопросов\"");
                e.printStackTrace();
            }
            questions[0] = new Question[nQu];
            if (testNameTextField.getText() != null && !testNameTextField.getText().trim().isEmpty() && nQuTextField.getText() != null && !nQuTextField.getText().trim().isEmpty() && !fail) {
                try {
                    //ЗАПУСКАЕМ ДИАЛОГИ
                    for (int i = 0; i < nQu; i++) {
                        questions[0][i] = startMakeMagic(i, primaryStage);
                    }
                } catch (NumberFormatException | IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", e.getMessage(), false, new GridPane());
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



                pane.add(saveTestToFile, 2, nQu);
                primaryStage.setScene(new Scene(new ScrollPane(pane)));
                primaryStage.setMinHeight(120);
                primaryStage.setMinWidth(410);
                primaryStage.show();
            } else {
                boolean[] both = {false, false};
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (testNameTextField.getText() == null || testNameTextField.getText().trim().isEmpty()) {
                    if (debug) System.out.println("Введён НЕправильный текст названия теста");
                    alert.setHeaderText("Введён НЕправильный текст названия теста");
                    both[0] = true;
                }
                if (nQuTextField.getText() == null || nQuTextField.getText().trim().isEmpty()) {
                    if (debug) System.out.println("Введён НЕправильный текст количества вопросов");
                    alert.setHeaderText("Введён НЕправильный текст количества вопросов");
                    both[1] = true;
                }

                if (both[0] && both[1]) {
                    if (debug) System.out.println("Введён НЕправильный текст названия теста и количества вопросов");
                    alert.setHeaderText("Введён НЕправильный текст названия теста и количества вопросов");
                }

                alert.showAndWait();

            }

        });


//СОЗДАНИЕ ФИНАЛЬНОГО STRING`А ИЗ МАССИВА СОХРАНЕНИЕ В ФАЙЛ
        saveTestToFile.setOnAction(event1 -> {
            String questionsFmt = "";
            for (int i = 0; i < questions[0].length; i++) {
                if (i == (nQu - 1))
                    questionsFmt = questionsFmt + questions[0][i].getFormatedResult();

                else questionsFmt = questionsFmt + questions[0][i].getFormatedResult() + ",";

            }

            finalTest test = new finalTest();
            test.testName = testNameTextField.getText();
            test.questionsFmt = questionsFmt;
            test.subjectId = subjectId;
            endEndResult = test.getFormatedTest();
            if (debug) System.out.println("На saveTestToFile" + endEndResult);
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("C:\\ROST_tests\\" + testNameTextField.getText() + ".rost"), "UTF-8"));
                primaryStage.setTitle(appName);
            } catch (UnsupportedEncodingException e) {
                primaryStage.setTitle(appName + " Ошибка: неподдерживаемая кодировка");
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                primaryStage.setTitle(appName + " Ошибка: файл не найден");
                e.printStackTrace();
            }
            try {
                bw.write(endEndResult);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех !");
                alert.setHeaderText("Тест создан !");
                alert.setContentText("Тест \"" + testNameTextField.getText() + "\" успешно создан и сохранён в C:\\ROST_tests\\"
                        + testNameTextField.getText() + ".rost");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("successIcon.png").toString()));


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
                            System.out.println(e);
                        }
                    } else primaryStage.close();
                } catch (Exception e) {
                }


            } catch (IOException e) {
                primaryStage.setTitle(appName + " Ошибка: неизвестная ошибка");
                e.printStackTrace();
            }
            try {
                bw.close();
            } catch (IOException e) {
                primaryStage.setTitle(appName + " Ошибка: неизвестная ошибка");
                e.printStackTrace();
            }
            primaryStage.close();
        });

        //ПОДГРУЖАЕМ ОБНОВЛЕНИЯ С ИНЕТА
        WebView isUpdate = new WebView();
        WebEngine isUpdateEngine = isUpdate.getEngine();
        isUpdateEngine.load(isUpdateAvailable);
        isUpdate.setMaxSize(220, 80);
        isUpdate.setOnMouseClicked(event -> {
            getHostServices().showDocument(updateUrl);
        });
        grid.add(isUpdate, 0, 4);
        //УБИРАЕМ SCROLLBAR
        isUpdate.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> change) {
                Set<Node> deadSeaScrolls = isUpdate.lookupAll(".scroll-bar");
                for (Node scroll : deadSeaScrolls) {
                    scroll.setVisible(false);
                }
            }
        });
        //ДЕЛАЕМ СЦЕНУ
        Scene scene = new Scene(root, 520, 375);
        primaryStage.setScene(scene);
        if (debug == true) grid.setGridLinesVisible(true);
        primaryStage.show();

    }

    public void openQuestionsEditor(int nQu, Question[] question, Stage primaryStage,  GridPane pane) {
        for (int i = 0; i < nQu; i++) {
            final int[] n = {i + 1};
            Label questionLabel = new Label("Вопрос № " + n[0]);
            Button questionButton = new Button("Изменить вопрос", new ImageView(new Image(getClass().getResourceAsStream("text-edit.png"))));
            questionButton.setTooltip(new Tooltip("Редактировать " + n[0] + " вопрос"));
            questionLabel.setTooltip(new Tooltip("Редактировать " + n[0] + " вопрос"));
            pane.add(questionLabel, 0, i);
            pane.add(questionButton, 1, i);
            questionButton.setId(Integer.toString(i));

            questionButton.setOnAction(event1 -> {


                Question editingQu = question[Integer.parseInt(questionButton.getId())];

                String qText = editingQu.qText;
                boolean isRegistrSense = editingQu.isRegistrSense;
                boolean isSpaceSense = editingQu.isSpaceSense;
                int nAn = editingQu.nAn;
                String answers[] = editingQu.answers;
                boolean[] checkBoxes = editingQu.checkBoxes;
                n[0] = editingQu.i;
                String[] answerType = new String[1];
                answerType[0] = editingQu.answerType;
                if (debug) {
                    System.out.println("[ПРИ РЕДАКТИРОВАНИИ " + editingQu.i + " ВОПРОСА]\nТекст " + editingQu.i + " вопроса: " + editingQu.qText);
                    for (int h = 0; h < answers.length; h++) {
                        int j = h + 1;
                        System.out.println("    Текст " + j + " ответа: " + answers[h]);
                    }
                    for (int h = 0; h < checkBoxes.length; h++) {
                        int j = h + 1;
                        System.out.println("    Правильность " + j + " ответа: " + checkBoxes[h]);
                    }
                    System.out.println("    Код " + n[0] + " вопроса: " + editingQu.getFormatedResult());
                }
                question[Integer.parseInt(questionButton.getId())] = HowManyAnswers.newWindow(
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
                        answerType);
            });
        }
    }

    private Label getLabel(String text) {
        return new Label(text);
    }

    public Question startMakeMagic(int i, Window primaryStage) throws IOException {
        int n = i + 1;
        Question question;
        if (debug) System.out.println("startMakeMagic = " + n);
        question = HowManyAnswers.newWindow(n, debug, this, primaryStage, nQu, false, "1", false, false, 1, new String[1], new boolean[1], new String[1]);
        if (debug) System.out.println("Вариантов ответа на " + n + " вопрос: " + question.nAn);


        if (debug) {
            String[] answersMass = question.answers;
            boolean[] checkBoxesMass = question.checkBoxes;
            System.out.println("Текст " + n + " вопроса: " + question.qText);
            for (int h = 0; h < answersMass.length; h++) {
                int j = h + 1;
                System.out.println("    Текст " + j + " ответа: " + answersMass[h]);
            }
            for (int h = 0; h < checkBoxesMass.length; h++) {
                int j = h + 1;
                System.out.println("    Правильность " + j + " ответа: " + checkBoxesMass[h]);
            }
            System.out.println("    Код " + n + " вопроса: " + question.getFormatedResult() +
                    "\n------------------------------------------------------------------");
        }
        return question;
    }

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

    public void easterEgg() {
        scenetitle.setOnContextMenuRequested(event2 -> {
            scenetitle.setOnScroll(event1 -> {
                scenetitle.setOnMouseClicked(event -> {
                    scenetitle.setOnScroll(event3 -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Easter egg");
                        alert.setHeaderText("Создатель этой великолепной программы - Попов Антон Палович\nУченик МБОУ \"СОШ №62\", 2016.");
                        GridPane gr = new GridPane();
                        Image img = new Image(getClass().getResourceAsStream("qu.class"));
                        ImageView imageView = new ImageView(img);

                        Label label = new Label("«Пасхальное яйцо» (англ. Easter Egg) — \nразновидность секрета, оставляемого в игре, \nфильме или программном обеспечении создателями. \n" +
                                "Пасхальные яйца играют роль своеобразных \nшуток для внимательных игроков или зрителей. \n" +
                                "Чаще всего для «получения» пасхального яйца следует \nпроизвести сложную и/или нестандартную совокупность\n действий, " +
                                "что делает маловероятным либо \nпрактически исключает случайное обнаружение.");
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
                        alert.showAndWait();
                    });
                });
            });
        });
    }

    public void showAlert(Alert.AlertType alertType, String title, String headerText, boolean isPane, GridPane pane) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        if (isPane) alert.getDialogPane().setContent(pane);
        alert.showAndWait();
    }

}
