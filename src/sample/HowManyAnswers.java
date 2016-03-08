package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.Optional;

/**
 * Created by popov on 22.02.2016.
 */

public class HowManyAnswers {
    static String answerType;
    private static Integer nAn;

    public static Question newWindow(Integer nQu, Boolean debug, Main main, Window primaryStage, Integer howManyQuestions) {
        String htmlCode;


        boolean[] isSence = new boolean[2];
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        WebView webEditor = new WebView();
        webEditor.setMaxHeight(430);
        WebEngine webEditorEngine = webEditor.getEngine();
        webEditorEngine.setJavaScriptEnabled(true);
        webEditorEngine.load(main.getClass().getResource("ckeditor/index.htm").toString());
        grid.add(webEditor, 0, 0);


        Label isRegistrSense = new Label("Чувствительность к регистру (при впечатывании)");
        isRegistrSense.setTooltip(new Tooltip("В разработке"));
        grid.add(isRegistrSense, 0, 1);
        CheckBox isRegistrSenseCheckBox = new CheckBox();
        isRegistrSenseCheckBox.setTooltip(new Tooltip("В разработке"));
        grid.add(isRegistrSenseCheckBox, 1, 1);

        isRegistrSenseCheckBox.setOnAction(event1 -> {
            if (isRegistrSenseCheckBox.isSelected() == true) {
                if (debug) System.out.println("Ответы не чусвтсвительны к регистру");
                isSence[0] = true;
            } else isSence[0] = false;
        });

        Label isSpaceSense = new Label("Чувствительность к пробелам (при впечатывании)");
        isSpaceSense.setTooltip(new Tooltip("В разработке"));
        grid.add(isSpaceSense, 0, 2);
        CheckBox isSpaceSenseCheckBox = new CheckBox();
        isSpaceSenseCheckBox.setTooltip(new Tooltip("В разработке"));
        grid.add(isSpaceSenseCheckBox, 1, 2);

        isSpaceSenseCheckBox.setOnAction(event1 -> {
            if (isSpaceSenseCheckBox.isSelected() == true) {
                if (debug) System.out.println("Ответы не чусвтсвительны к пробелам");
                isSence[1] = true;
            } else isSence[1] = false;
        });

        //Сетка для типа ответов
        GridPane gridForAnswerType = new GridPane();
        gridForAnswerType.setHgap(10);
        gridForAnswerType.setVgap(10);
        gridForAnswerType.setGridLinesVisible(debug);
        grid.add(gridForAnswerType, 0, 3);
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton answerTypeUsual = new RadioButton("Один или несколько вариантов ответа");
        RadioButton answerTypeDirectInput = new RadioButton("Прямой ввод");
        RadioButton answerTypeComplies = new RadioButton("Соответствие");
        RadioButton answerTypeSort = new RadioButton("Сортировка");
        gridForAnswerType.add(answerTypeUsual, 0, 0);
        gridForAnswerType.add(answerTypeDirectInput, 1, 0);
        gridForAnswerType.add(answerTypeComplies, 2, 0);
        gridForAnswerType.add(answerTypeSort, 3, 0);

        answerTypeUsual.setToggleGroup(toggleGroup);
        answerTypeDirectInput.setToggleGroup(toggleGroup);
        answerTypeComplies.setToggleGroup(toggleGroup);
        answerTypeSort.setToggleGroup(toggleGroup);

        //Сетка для количества ответов
        Label HowManyAnswers = new Label("Количество ответов: ");
        GridPane gridForRadioButtonAnswers = new GridPane();
        gridForRadioButtonAnswers.setGridLinesVisible(debug);

        RadioButton HowManyAnswersRadioButton2 = new RadioButton();
        HowManyAnswersRadioButton2.setText("2 ");

        HowManyAnswersRadioButton2.setSelected(true);
        RadioButton HowManyAnswersRadioButton3 = new RadioButton();
        HowManyAnswersRadioButton3.setText("3 ");

        RadioButton HowManyAnswersRadioButton4 = new RadioButton();
        HowManyAnswersRadioButton4.setText("4 ");

        RadioButton HowManyAnswersRadioButton5 = new RadioButton();
        HowManyAnswersRadioButton5.setText("5 ");

        RadioButton HowManyAnswersRadioButton6 = new RadioButton();
        HowManyAnswersRadioButton6.setText("6 ");
        //Добавляем RadioButton для количество ответов в сетку
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton2, 0, 0);
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton3, 1, 0);
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton4, 2, 0);
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton5, 3, 0);
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton6, 4, 0);


        //Сетка для текстовых полей с ответами
        GridPane gridForTextingAnswers = new GridPane();
        gridForTextingAnswers.setGridLinesVisible(debug);
        ScrollPane scrollPane = new ScrollPane(gridForTextingAnswers);

        TextField directInputTextField = new TextField();
        directInputTextField.setPromptText("Правильный ответ");
        directInputTextField.setTooltip(new Tooltip("Ответ, вписываемый учеником"));
        answerTypeDirectInput.setOnAction(event -> {
            grid.getChildren().remove(gridForRadioButtonAnswers);
            grid.getChildren().remove(HowManyAnswers);
            //Убираем поля с ответами
            grid.getChildren().remove(scrollPane);
            grid.add(directInputTextField, 0, 4);
            nAn = 1;
            answerType = "DirectInput";
        });


        //ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
        answerTypeUsual.setOnAction(event -> {
            grid.getChildren().remove(directInputTextField);
            grid.add(HowManyAnswers, 0, 4);
            grid.add(gridForRadioButtonAnswers, 0, 5);
            grid.add(scrollPane, 0, 6);
            answerType = "One";
        });


        gridForTextingAnswers.setMinHeight(100);
        gridForTextingAnswers.setGridLinesVisible(debug);
        gridForTextingAnswers.setVgap(11);
        gridForTextingAnswers.setVgap(7);


        Label answer1 = new Label("Ответ 1");
        Label answer2 = new Label("Ответ 2");
        Label answer3 = new Label("Ответ 3");
        Label answer4 = new Label("Ответ 4");
        Label answer5 = new Label("Ответ 5");
        Label answer6 = new Label("Ответ 6");

        TextField answerField1 = new TextField();
        TextField answerField2 = new TextField();
        TextField answerField3 = new TextField();
        TextField answerField4 = new TextField();
        TextField answerField5 = new TextField();
        TextField answerField6 = new TextField();

        RadioButton answerChoose1 = new RadioButton();
        RadioButton answerChoose2 = new RadioButton();
        RadioButton answerChoose3 = new RadioButton();
        RadioButton answerChoose4 = new RadioButton();
        RadioButton answerChoose5 = new RadioButton();
        RadioButton answerChoose6 = new RadioButton();

        answerChoose1.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose2.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose3.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose4.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose5.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose6.setTooltip(new Tooltip("Выберите правильный ответ"));

        answerChoose1.setFocusTraversable(false);
        answerChoose2.setFocusTraversable(false);
        answerChoose3.setFocusTraversable(false);
        answerChoose4.setFocusTraversable(false);
        answerChoose5.setFocusTraversable(false);
        answerChoose6.setFocusTraversable(false);

        ToggleGroup groupHowManyAnswersRadioButton = new ToggleGroup();
        HowManyAnswersRadioButton2.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton3.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton4.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton5.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton6.setToggleGroup(groupHowManyAnswersRadioButton);


        answerChoose1.setUserData("1");
        answerChoose2.setUserData("2");
        answerChoose3.setUserData("3");
        answerChoose4.setUserData("4");
        answerChoose5.setUserData("5");
        answerChoose6.setUserData("6");

        //ДОБАВЛЯЕМ КНОПКИ ОТКРЫТИЯ РЕДАКТОРА ОТВЕТОВ
        Tooltip tooltip = new Tooltip("Вы можете написать ответы на вопрос с помощью редактора");
        Button btn1 = new Button("Открыть редактор");
        btn1.setTooltip(tooltip);
        btn1.setFocusTraversable(false);
        btn1.setOnAction(event1 -> {
            answerField1.setText(openEditor(" ответа", 1, main, debug));
        });
        Button btn2 = new Button("Открыть редактор");
        btn2.setTooltip(tooltip);
        btn2.setFocusTraversable(false);
        btn2.setOnAction(event1 -> {
            answerField2.setText(openEditor(" ответа", 2, main, debug));
        });
        Button btn3 = new Button("Открыть редактор");
        btn3.setTooltip(tooltip);
        btn3.setFocusTraversable(false);
        btn3.setOnAction(event1 -> {
            answerField3.setText(openEditor(" ответа", 3, main, debug));
        });
        Button btn4 = new Button("Открыть редактор");
        btn4.setTooltip(tooltip);
        btn4.setFocusTraversable(false);
        btn4.setOnAction(event1 -> {
            answerField4.setText(openEditor(" ответа", 4, main, debug));
        });
        Button btn5 = new Button("Открыть редактор");
        btn5.setTooltip(tooltip);
        btn5.setFocusTraversable(false);
        btn5.setOnAction(event1 -> {
            answerField5.setText(openEditor(" ответа", 5, main, debug));
        });
        Button btn6 = new Button("Открыть редактор");
        btn6.setTooltip(tooltip);
        btn6.setFocusTraversable(false);
        btn6.setOnAction(event1 -> {
            answerField6.setText(openEditor(" ответа", 6, main, debug));
        });


        answerField1.setPromptText("Текст 1 ответа");
        answerField2.setPromptText("Текст 2 ответа");
        answerField3.setPromptText("Текст 3 ответа");
        answerField4.setPromptText("Текст 4 ответа");
        answerField5.setPromptText("Текст 5 ответа");
        answerField6.setPromptText("Текст 6 ответа");

        //ПРИ НАЖАТИИ НА "2 ОТВЕТА"
        HowManyAnswersRadioButton2.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();
            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);


            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            nAn = 2;
        });
        HowManyAnswersRadioButton3.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            nAn = 3;
        });

        HowManyAnswersRadioButton4.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            gridForTextingAnswers.add(answerChoose4, 2, 3);
            gridForTextingAnswers.add(btn4, 3, 3);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            nAn = 4;
        });

        HowManyAnswersRadioButton5.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            String savedText5 = answerField5.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            gridForTextingAnswers.add(answerChoose4, 2, 3);
            gridForTextingAnswers.add(btn4, 3, 3);

            gridForTextingAnswers.add(answer5, 0, 4);
            gridForTextingAnswers.add(answerField5, 1, 4);
            gridForTextingAnswers.add(answerChoose5, 2, 4);
            gridForTextingAnswers.add(btn5, 3, 4);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField5.setText(savedText5);
            nAn = 5;
        });

        HowManyAnswersRadioButton6.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            String savedText5 = answerField5.getText();
            String savedText6 = answerField6.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            gridForTextingAnswers.add(answerChoose4, 2, 3);
            gridForTextingAnswers.add(btn4, 3, 3);

            gridForTextingAnswers.add(answer5, 0, 4);
            gridForTextingAnswers.add(answerField5, 1, 4);
            gridForTextingAnswers.add(answerChoose5, 2, 4);
            gridForTextingAnswers.add(btn5, 3, 4);

            gridForTextingAnswers.add(answer6, 0, 5);
            gridForTextingAnswers.add(answerField6, 1, 5);
            gridForTextingAnswers.add(answerChoose6, 2, 5);
            gridForTextingAnswers.add(btn6, 3, 5);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField5.setText(savedText5);
            answerField6.setText(savedText6);
            nAn = 6;
        });


        Question question = new Question();

        Button btn = new Button("Далее");
        grid.add(btn, 1, 9);
        btn.setOnAction(event -> {
            String[] answers = new String[6];
            boolean[] checkBoxes = new boolean[6];
            boolean isError = false;
            //ЕСЛИ ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
            if (answerType == "One") {
                try {
                    answers[0] = answerField1.getText();
                    answers[1] = answerField2.getText();
                    if (nAn >= 3) answers[2] = answerField3.getText();
                    if (nAn >= 4) answers[3] = answerField4.getText();
                    if (nAn >= 5) answers[4] = answerField5.getText();
                    if (nAn == 6) answers[5] = answerField6.getText();
                } catch (Exception e) {
                    isError = true;
                }

                try {
                    if (answerChoose1.isSelected()) checkBoxes[0] = true;
                    if (answerChoose2.isSelected()) checkBoxes[1] = true;
                    if (answerChoose3.isSelected()) checkBoxes[2] = true;
                    if (answerChoose4.isSelected()) checkBoxes[3] = true;
                    if (answerChoose5.isSelected()) checkBoxes[4] = true;
                    if (answerChoose6.isSelected()) checkBoxes[5] = true;

                } catch (Exception e) {
                    isError = true;
                }
                if (isError)
                    main.showAlert(Alert.AlertType.ERROR, "Ошибка", "Вы не выбрали правильный вариант ответа !", false, new GridPane());
                //ЕСЛИ ПРЯМОЙ ВВОД
            } else if (answerType == "DirectInput") {
                answers[0] = directInputTextField.getText();
                checkBoxes[0] = true;
            } else if (answerType == "Compiles") {
                //// TODO: 05.03.2016 Сделать соответствие
            } else if (answerType == "Sort") {
                //// TODO: 05.03.2016 Сделать сортировку
            }
            question.qText = webEditorEngine.executeScript("CKEDITOR.instances['editor1'].getData()").toString().replace("\n", "");
            question.isRegistrSense = isSence[0];
            question.isSpaceSense = isSence[1];
            question.nAn = nAn;
            question.answers = answers;
            question.checkBoxes = checkBoxes;
            question.i = nQu;
            question.answerType = answerType;


            if (nAn != null && !isError) window.close();
        });


        Scene scene = new Scene(grid, 920, 840);
        window.setScene(scene);
        window.setTitle("Вопрос " + nQu + "(" + howManyQuestions + ")");
        if (debug == true) grid.setGridLinesVisible(true);
        window.showAndWait();

        return question;
    }

    public static String openEditor(String QuOrAn, Integer n, Main main, Boolean debug) {
        String htmlCode;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Редактор текста для " + n + QuOrAn);
        alert.setResizable(true);
        WebView webEditor = new WebView();
        WebEngine webEditorEngine = webEditor.getEngine();
        webEditorEngine.setJavaScriptEnabled(true);
        webEditorEngine.load(main.getClass().getResource("ckeditor/index.htm").toString());
        alert.getDialogPane().setContent(webEditor);
        /*HTMLEditor editor = new HTMLEditor();

        FileChooser fileChooser = new FileChooser();
        File choosenImage = fileChooser.showOpenDialog(primaryStage);
        String base64 = ImageUtils.encodeToString(choosenImage, "jpeg");*/

        //Кнопка БЕЗ РЕДАКТОРА
        ButtonType withoutEditor = new ButtonType("Без редактора");
        alert.getButtonTypes().add(withoutEditor);

        //ПОКАЗЫВАЕМ РЕДАКТОР
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            htmlCode = webEditorEngine.executeScript("CKEDITOR.instances['editor1'].getData()").toString();
            if (debug) System.out.println("Введённый HTML код на " + n + " вопрос: " + htmlCode);
        } else {
            htmlCode = "";
            if (debug) System.out.println("Введённый HTML код на " + n + " вопрос: пуст(" + htmlCode + ")");
        }
        return htmlCode;
    }

    public static Question newEditorOfQu(String qText,
                                         boolean isRegistrSense1,
                                         boolean isSpaceSense1,
                                         int nAn1,
                                         String answers1[],
                                         boolean[] checkBoxes1,
                                         int nQu, boolean debug, Main main, Window primaryStage,
                                         final String[] answerType) {

        boolean[] isSence = new boolean[2];
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        // Общая сетка для всех элементов
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Сохраняем текст вопроса в файл
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(System.getProperty("user.dir") + "\\sample\\ckeditor\\editorForEditingQu.htm")));
            if (debug) System.out.println(System.getProperty("user.dir") + "\\sample\\ckeditor\\editorForEditingQu.htm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            bw.write("<html>" +
                    "    <head>" +
                    "        <meta charset=\"windows-1251\">" +
                    "        <title>CKEditor</title>" +
                    "        <script src=\"ckeditor.js\"></script>" +
                    "    </head>" +
                    "    <body>" +
                    "        <textarea id=\"editor1\" name=\"editor1\" placeholder=\"Впишите сюда текст вопроса/ответа. Для вставки текста используйте соченание клавиш Ctrl + V\">" + qText + "</textarea>" +
                    "        <script>" +
                    "            CKEDITOR.replace( 'editor1' );" +
                    "        </script>" +
                    "    </body>" +
                    "</html>");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Подгружаем сохранённый вопрос
        WebView webEditor = new WebView();
        webEditor.setMaxHeight(430);
        WebEngine webEditorEngine = webEditor.getEngine();
        webEditorEngine.setJavaScriptEnabled(true);
        webEditorEngine.load(main.getClass().getResource("ckeditor/editorForEditingQu.htm").toString());
        grid.add(webEditor, 0, 0);

        Label isRegistrSense = new Label("Чувствительность к регистру (при впечатывании)");
        isRegistrSense.setTooltip(new Tooltip("В разработке"));
        grid.add(isRegistrSense, 0, 1);
        CheckBox isRegistrSenseCheckBox = new CheckBox();
        isRegistrSenseCheckBox.setSelected(isRegistrSense1);
        isRegistrSenseCheckBox.setTooltip(new Tooltip("В разработке"));
        grid.add(isRegistrSenseCheckBox, 1, 1);

        isRegistrSenseCheckBox.setOnAction(event1 -> {
            if (isRegistrSenseCheckBox.isSelected() == true) {
                if (debug) System.out.println("Ответы не чусвтсвительны к регистру");
                isSence[0] = true;
            } else isSence[0] = false;
        });

        Label isSpaceSense = new Label("Чувствительность к пробелам (при впечатывании)");
        isSpaceSense.setTooltip(new Tooltip("В разработке"));
        grid.add(isSpaceSense, 0, 2);
        CheckBox isSpaceSenseCheckBox = new CheckBox();
        isSpaceSenseCheckBox.setSelected(isSpaceSense1);
        isSpaceSenseCheckBox.setTooltip(new Tooltip("В разработке"));
        grid.add(isSpaceSenseCheckBox, 1, 2);

        isSpaceSenseCheckBox.setOnAction(event1 -> {
            if (isSpaceSenseCheckBox.isSelected() == true) {
                if (debug) System.out.println("Ответы не чусвтсвительны к пробелам");
                isSence[1] = true;
            } else isSence[1] = false;
        });

        //// TODO: 05.03.2016 ПОДДЕРЖКА РЕДАКТИРОВАНИЯ ВОПРОСОВ В РЕДАКТОРЕ (ПРИ ПРЯМОМ ВВОДЕ)
        Label howManyAnswersLabel = new Label("Количество ответов: ");
        GridPane gridForRadioButtonAnswers = new GridPane();
        gridForRadioButtonAnswers.setGridLinesVisible(debug);/*
        grid.add(gridForRadioButtonAnswers, 0, 4);*/

        // ТИП ОТВЕТОВ
        GridPane gridForAnswerType = new GridPane();
        gridForAnswerType.setHgap(10);
        gridForAnswerType.setVgap(10);
        gridForAnswerType.setGridLinesVisible(debug);
        grid.add(gridForAnswerType, 0, 3);
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton answerTypeUsual = new RadioButton("Один или несколько вариантов ответа");
        RadioButton answerTypeDirectInput = new RadioButton("Прямой ввод");
        RadioButton answerTypeComplies = new RadioButton("Соответствие");
        RadioButton answerTypeSort = new RadioButton("Сортировка");
        gridForAnswerType.add(answerTypeUsual, 0, 0);
        gridForAnswerType.add(answerTypeDirectInput, 1, 0);
        gridForAnswerType.add(answerTypeComplies, 2, 0);
        gridForAnswerType.add(answerTypeSort, 3, 0);

        answerTypeUsual.setToggleGroup(toggleGroup);
        answerTypeDirectInput.setToggleGroup(toggleGroup);
        answerTypeComplies.setToggleGroup(toggleGroup);
        answerTypeSort.setToggleGroup(toggleGroup);

        RadioButton HowManyAnswersRadioButton2 = new RadioButton();
        HowManyAnswersRadioButton2.setText("2 ");
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton2, 0, 0);
        HowManyAnswersRadioButton2.setSelected(true);
        if (nAn == 2) HowManyAnswersRadioButton2.setSelected(true);

        RadioButton HowManyAnswersRadioButton3 = new RadioButton();
        HowManyAnswersRadioButton3.setText("3 ");
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton3, 1, 0);
        if (nAn == 3) HowManyAnswersRadioButton3.setSelected(true);

        RadioButton HowManyAnswersRadioButton4 = new RadioButton();
        HowManyAnswersRadioButton4.setText("4 ");
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton4, 2, 0);
        if (nAn == 4) HowManyAnswersRadioButton4.setSelected(true);

        RadioButton HowManyAnswersRadioButton5 = new RadioButton();
        HowManyAnswersRadioButton5.setText("5 ");
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton5, 3, 0);
        if (nAn == 5) HowManyAnswersRadioButton5.setSelected(true);

        RadioButton HowManyAnswersRadioButton6 = new RadioButton();
        HowManyAnswersRadioButton6.setText("6 ");
        gridForRadioButtonAnswers.add(HowManyAnswersRadioButton6, 4, 0);
        if (nAn == 6) HowManyAnswersRadioButton6.setSelected(true);


        GridPane gridForTextingAnswers = new GridPane();
        gridForTextingAnswers.setGridLinesVisible(debug);
        ScrollPane scrollPaneForTextingAnswers = new ScrollPane(gridForTextingAnswers);

        TextField directInputTextField = new TextField();
        directInputTextField.setPromptText("Правильный ответ");
        directInputTextField.setTooltip(new Tooltip("Ответ, вписываемый учеником"));
        if (answerType[0] == "DirectInput"){
            grid.getChildren().remove(gridForRadioButtonAnswers);
            grid.getChildren().remove(howManyAnswersLabel);
            grid.getChildren().remove(scrollPaneForTextingAnswers);
            grid.add(directInputTextField, 0, 4);
            answerTypeDirectInput.setSelected(true);
            directInputTextField.setText(answers1[0]);
            nAn = 1;
        }
        answerTypeDirectInput.setOnAction(event -> {
            grid.getChildren().remove(gridForRadioButtonAnswers);
            grid.getChildren().remove(howManyAnswersLabel);
            grid.getChildren().remove(scrollPaneForTextingAnswers);
            grid.add(directInputTextField, 0, 4);
            answerTypeDirectInput.setSelected(true);
            directInputTextField.setText(answers1[0]);
            nAn = 1;
            answerType[0] = "DirectInput";
        });

//ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
        if (answerType[0] == "One"){
            grid.getChildren().remove(directInputTextField);
            grid.add(howManyAnswersLabel, 0, 4);
            grid.add(gridForRadioButtonAnswers, 0, 5);
            grid.add(scrollPaneForTextingAnswers, 0, 6);
            answerTypeUsual.setSelected(true);
        }
        answerTypeUsual.setOnAction(event -> {
            grid.getChildren().remove(directInputTextField);
            grid.add(howManyAnswersLabel, 0, 4);
            grid.add(gridForRadioButtonAnswers, 0, 5);
            grid.add(scrollPaneForTextingAnswers, 0, 6);
            answerType[0] = "One";
        });

        gridForTextingAnswers.setMinHeight(100);
        gridForTextingAnswers.setGridLinesVisible(debug);
        gridForTextingAnswers.setVgap(11);
        gridForTextingAnswers.setVgap(7);/*
        grid.add(scrollPane, 0, 5);*/
        Label answer1 = new Label("Ответ 1");
        Label answer2 = new Label("Ответ 2");
        Label answer3 = new Label("Ответ 3");
        Label answer4 = new Label("Ответ 4");
        Label answer5 = new Label("Ответ 5");
        Label answer6 = new Label("Ответ 6");

        TextField answerField1 = new TextField();
        TextField answerField2 = new TextField();
        TextField answerField3 = new TextField();
        TextField answerField4 = new TextField();
        TextField answerField5 = new TextField();
        TextField answerField6 = new TextField();

        RadioButton answerChoose1 = new RadioButton();
        RadioButton answerChoose2 = new RadioButton();
        RadioButton answerChoose3 = new RadioButton();
        RadioButton answerChoose4 = new RadioButton();
        RadioButton answerChoose5 = new RadioButton();
        RadioButton answerChoose6 = new RadioButton();

        answerChoose1.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose2.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose3.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose4.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose5.setTooltip(new Tooltip("Выберите правильный ответ"));
        answerChoose6.setTooltip(new Tooltip("Выберите правильный ответ"));

        answerChoose1.setFocusTraversable(false);
        answerChoose2.setFocusTraversable(false);
        answerChoose3.setFocusTraversable(false);
        answerChoose4.setFocusTraversable(false);
        answerChoose5.setFocusTraversable(false);
        answerChoose6.setFocusTraversable(false);

        ToggleGroup groupHowManyAnswersRadioButton = new ToggleGroup();
        HowManyAnswersRadioButton2.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton3.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton4.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton5.setToggleGroup(groupHowManyAnswersRadioButton);
        HowManyAnswersRadioButton6.setToggleGroup(groupHowManyAnswersRadioButton);


        answerChoose1.setUserData("1");
        answerChoose2.setUserData("2");
        answerChoose3.setUserData("3");
        answerChoose4.setUserData("4");
        answerChoose5.setUserData("5");
        answerChoose6.setUserData("6");

        //ДОБАВЛЯЕМ КНОПКИ ОТКРЫТИЯ РЕДАКТОРА ОТВЕТОВ
        Tooltip tooltip = new Tooltip("Вы можете написать ответы на вопрос с помощью редактора");
        Button btn1 = new Button("Открыть редактор");
        btn1.setTooltip(tooltip);
        btn1.setFocusTraversable(false);
        btn1.setOnAction(event1 -> {
            answerField1.setText(openEditor(" ответа", 1, main, debug));
        });
        Button btn2 = new Button("Открыть редактор");
        btn2.setTooltip(tooltip);
        btn2.setFocusTraversable(false);
        btn2.setOnAction(event1 -> {
            answerField2.setText(openEditor(" ответа", 2, main, debug));
        });
        Button btn3 = new Button("Открыть редактор");
        btn3.setTooltip(tooltip);
        btn3.setFocusTraversable(false);
        btn3.setOnAction(event1 -> {
            answerField3.setText(openEditor(" ответа", 3, main, debug));
        });
        Button btn4 = new Button("Открыть редактор");
        btn4.setTooltip(tooltip);
        btn4.setFocusTraversable(false);
        btn4.setOnAction(event1 -> {
            answerField4.setText(openEditor(" ответа", 4, main, debug));
        });
        Button btn5 = new Button("Открыть редактор");
        btn5.setTooltip(tooltip);
        btn5.setFocusTraversable(false);
        btn5.setOnAction(event1 -> {
            answerField5.setText(openEditor(" ответа", 5, main, debug));
        });
        Button btn6 = new Button("Открыть редактор");
        btn6.setTooltip(tooltip);
        btn6.setFocusTraversable(false);
        btn6.setOnAction(event1 -> {
            answerField6.setText(openEditor(" ответа", 6, main, debug));
        });


        answerField1.setPromptText("Текст 1 ответа");
        answerField2.setPromptText("Текст 2 ответа");
        answerField3.setPromptText("Текст 3 ответа");
        answerField4.setPromptText("Текст 4 ответа");
        answerField5.setPromptText("Текст 5 ответа");
        answerField6.setPromptText("Текст 6 ответа");

        if (answerType[0] == "One") {
                gridForTextingAnswers.add(answer1, 0, 0);
                gridForTextingAnswers.add(answerField1, 1, 0);
                gridForTextingAnswers.add(answerChoose1, 2, 0);
                gridForTextingAnswers.add(btn1, 3, 0);


            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            if (nAn1 >= 3) {
                gridForTextingAnswers.add(answer3, 0, 2);
                gridForTextingAnswers.add(answerField3, 1, 2);
                gridForTextingAnswers.add(answerChoose3, 2, 2);
                gridForTextingAnswers.add(btn3, 3, 2);
            }
            if (nAn1 >= 4) {
                gridForTextingAnswers.add(answer4, 0, 3);
                gridForTextingAnswers.add(answerField4, 1, 3);
                gridForTextingAnswers.add(answerChoose4, 2, 3);
                gridForTextingAnswers.add(btn4, 3, 3);
            }
            if (nAn1 >= 5) {
                gridForTextingAnswers.add(answer5, 0, 4);
                gridForTextingAnswers.add(answerField5, 1, 4);
                gridForTextingAnswers.add(answerChoose5, 2, 4);
                gridForTextingAnswers.add(btn5, 3, 4);
            }
            if (nAn1 >= 6) {
                gridForTextingAnswers.add(answer6, 0, 5);
                gridForTextingAnswers.add(answerField6, 1, 5);
                gridForTextingAnswers.add(answerChoose6, 2, 5);
                gridForTextingAnswers.add(btn6, 3, 5);
            }
        }
        //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
        answerField1.setText(answers1[0]);
        answerField2.setText(answers1[1]);
        answerField3.setText(answers1[2]);
        answerField4.setText(answers1[3]);
        answerField5.setText(answers1[4]);
        answerField6.setText(answers1[5]);

        answerChoose1.setSelected(checkBoxes1[0]);
        answerChoose2.setSelected(checkBoxes1[1]);
        answerChoose3.setSelected(checkBoxes1[2]);
        answerChoose4.setSelected(checkBoxes1[3]);
        answerChoose5.setSelected(checkBoxes1[4]);
        answerChoose6.setSelected(checkBoxes1[5]);


        //ПРИ НАЖАТИИ НА "2 ОТВЕТА"
        HowManyAnswersRadioButton2.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();
            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);


            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);
            //ПОДГРУЖАЕМ ТЕКСТ С РЕДАКТОРА

            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            nAn = 2;
        });
        HowManyAnswersRadioButton3.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            nAn = 3;
        });

        HowManyAnswersRadioButton4.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            gridForTextingAnswers.add(answerChoose4, 2, 3);
            gridForTextingAnswers.add(btn4, 3, 3);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ

            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);

            nAn = 4;
        });

        HowManyAnswersRadioButton5.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            String savedText5 = answerField5.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            gridForTextingAnswers.add(answerChoose4, 2, 3);
            gridForTextingAnswers.add(btn4, 3, 3);

            gridForTextingAnswers.add(answer5, 0, 4);
            gridForTextingAnswers.add(answerField5, 1, 4);
            gridForTextingAnswers.add(answerChoose5, 2, 4);
            gridForTextingAnswers.add(btn5, 3, 4);
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField5.setText(savedText5);
            nAn = 5;
        });

        HowManyAnswersRadioButton6.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            String savedText5 = answerField5.getText();
            String savedText6 = answerField6.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            gridForTextingAnswers.add(answerChoose1, 2, 0);
            gridForTextingAnswers.add(btn1, 3, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            gridForTextingAnswers.add(answerChoose3, 2, 2);
            gridForTextingAnswers.add(btn3, 3, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            gridForTextingAnswers.add(answerChoose4, 2, 3);
            gridForTextingAnswers.add(btn4, 3, 3);

            gridForTextingAnswers.add(answer5, 0, 4);
            gridForTextingAnswers.add(answerField5, 1, 4);
            gridForTextingAnswers.add(answerChoose5, 2, 4);
            gridForTextingAnswers.add(btn5, 3, 4);

            gridForTextingAnswers.add(answer6, 0, 5);
            gridForTextingAnswers.add(answerField6, 1, 5);
            gridForTextingAnswers.add(answerChoose6, 2, 5);
            gridForTextingAnswers.add(btn6, 3, 5);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField5.setText(savedText5);
            answerField6.setText(savedText6);
            nAn = 6;
        });


        Question question = new Question();
        String[] answers = new String[6];
        boolean[] checkBoxes = new boolean[6];
        final boolean[] isError = {false};

        Button btn = new Button("Далее");
        grid.add(btn, 1, 9);
        btn.setOnAction(event -> {
            if (answerType[0] == "One" || answerType[0] == "Many") {

                answers[0] = answerField1.getText();
                answers[1] = answerField2.getText();
                if (nAn >= 3) answers[2] = answerField3.getText();
                if (nAn >= 4) answers[3] = answerField4.getText();
                if (nAn >= 5) answers[4] = answerField5.getText();
                if (nAn == 6) answers[5] = answerField6.getText();

                try {
                    if (answerChoose1.isSelected()) checkBoxes[0] = true;
                    if (answerChoose2.isSelected()) checkBoxes[1] = true;
                    if (answerChoose3.isSelected()) checkBoxes[2] = true;
                    if (answerChoose4.isSelected()) checkBoxes[3] = true;
                    if (answerChoose5.isSelected()) checkBoxes[4] = true;
                    if (answerChoose6.isSelected()) checkBoxes[5] = true;
                } catch (Exception e) {
                    isError[0] = true;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Вы не выбрали правильный вариант ответа !");
                    alert.showAndWait();
                }
            } else if (answerType[0] == "DirectInput") {
                answers[0] = directInputTextField.getText();
                checkBoxes[0] = true;
                // // TODO: 06.03.2016 DirectInput РЕДАКТОР
            } else if (answerType[0] == "Complites") {
                // // TODO: Compiles РЕДАКТОР
            } else if (answerType[0] == "Sort") {
                // // TODO: 06.03.2016 Sort РЕДАКТОР
            }

            question.qText = webEditorEngine.executeScript("CKEDITOR.instances['editor1'].getData()").toString().replace("\n", "");
            question.isRegistrSense = isSence[0];
            question.isSpaceSense = isSence[1];
            question.nAn = nAn;
            question.answers = answers;
            question.checkBoxes = checkBoxes;
            question.i = nQu;
            question.answerType = answerType[0];


            if (nAn != null && !isError[0]) window.close();
        });


        Scene scene = new Scene(grid, 920, 840);
        window.setScene(scene);
        window.setTitle("Вопрос " + nQu);
        if (debug == true) grid.setGridLinesVisible(true);
        window.showAndWait();

        return question;
    }
}
