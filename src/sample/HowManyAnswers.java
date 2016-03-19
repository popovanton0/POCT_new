package sample;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by popov on 22.02.2016.
 */

public class HowManyAnswers {
    private static Integer nAn;

    public static Question newWindow(Integer nQu, boolean debug, Main main, Window primaryStage, Integer howManyQuestions,
                                     boolean isEditor,
                                     String qText,
                                     boolean isRegistrSense1,
                                     boolean isSpaceSense1,
                                     int nAnLoaded,
                                     String[] answersLoaded,
                                     boolean[] checkBoxesLoaded,
                                     final String[] answerType, Logger log) {


        Question question = null;
        try {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);

            //ОБЪЯВЛЯЕМ ЭЛЕМЕНТЫ ИНТЕРФЕЙСА (МНОГО !!!!)
            RadioButton HowManyAnswersRadioButton2 = new RadioButton();
            RadioButton HowManyAnswersRadioButton3 = new RadioButton();
            RadioButton HowManyAnswersRadioButton4 = new RadioButton();
            RadioButton HowManyAnswersRadioButton5 = new RadioButton();
            RadioButton HowManyAnswersRadioButton6 = new RadioButton();

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

            TextField answerField1Compilles = new TextField();
            TextField answerField2Compilles = new TextField();
            TextField answerField3Compilles = new TextField();
            TextField answerField4Compilles = new TextField();
            TextField answerField5Compilles = new TextField();
            TextField answerField6Compilles = new TextField();

            RadioButton answerChoose1 = new RadioButton();
            RadioButton answerChoose2 = new RadioButton();
            RadioButton answerChoose3 = new RadioButton();
            RadioButton answerChoose4 = new RadioButton();
            RadioButton answerChoose5 = new RadioButton();
            RadioButton answerChoose6 = new RadioButton();

            Label isRegistrSense = new Label("Чувствительность к регистру (при впечатывании)");
            Label isSpaceSense = new Label("Чувствительность к пробелам (при впечатывании)");
            CheckBox isRegistrSenseCheckBox = new CheckBox();
            CheckBox isSpaceSenseCheckBox = new CheckBox();

            //ДОБАВЛЯЕМ КНОПКИ ОТКРЫТИЯ РЕДАКТОРА ОТВЕТОВ
            Button btn1 = new Button("Открыть редактор");
            Button btn2 = new Button("Открыть редактор");
            Button btn3 = new Button("Открыть редактор");
            Button btn4 = new Button("Открыть редактор");
            Button btn5 = new Button("Открыть редактор");
            Button btn6 = new Button("Открыть редактор");

            //ГЛАВНАЯ СЕТКА
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));


            if (isEditor) saveTextOuToFile(qText, debug, log);

            //CKEditor
            WebView webEditor = new WebView();
            webEditor.setMaxHeight(430);
            WebEngine webEditorEngine = webEditor.getEngine();
            webEditorEngine.setJavaScriptEnabled(true);
            if (isEditor) webEditorEngine.load(main.getClass().getResource("ckeditor/editorForEditingQu.htm").toString());
           else webEditorEngine.load(main.getClass().getResource("ckeditor/index.htm").toString());
            grid.add(webEditor, 0, 0);

            boolean[] isSence = new boolean[2];
            //УСТАНАВЛИВАЕМ TOOLPIPE`Ы, СТАВИМ LISTENER`Ы И ПОДГРУЖАЕМ ЕСЛИ ОТКРЫВАЕМ РЕДАКТОР ВОПРОСА
            settingUpRegistrAndSpaceSense(log, debug, isEditor, isRegistrSense1, isSpaceSense1, isRegistrSense, isSpaceSense, isRegistrSenseCheckBox, isSpaceSenseCheckBox, grid, isSence);

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


            settingUpNumderOfAnswersRadioBtns(isEditor, nAnLoaded, gridForRadioButtonAnswers, HowManyAnswersRadioButton2, HowManyAnswersRadioButton3, HowManyAnswersRadioButton4, HowManyAnswersRadioButton5, HowManyAnswersRadioButton6, log);


            //Сетка для текстовых полей с ответами
            GridPane gridForTextingAnswers = new GridPane();
            gridForTextingAnswers.setGridLinesVisible(debug);
            gridForTextingAnswers.setMinHeight(100);
            gridForTextingAnswers.setGridLinesVisible(debug);
            gridForTextingAnswers.setVgap(11);
            gridForTextingAnswers.setVgap(7);

            ScrollPane scrollPane = new ScrollPane(gridForTextingAnswers);

            //Прямой ввод
            TextField directInputTextField = new TextField();
            directInputTextField.setPromptText("Правильный ответ");
            directInputTextField.setTooltip(new Tooltip("Ответ, вписываемый учеником"));

            //СТАВИМ LISTENER`ОВ НА ВЫБОР ТИПА ОТВЕТА, ПОДГРУЖАЕМ ТИП ОТВЕТА ЕСЛИ ОТКРЫВАЕМ РЕДАКТОР ВОПРОСА
            setAnswerTypeChoosers(log, isEditor, answerType, grid, answerTypeUsual, answerTypeDirectInput, answerTypeComplies, answerTypeSort, HowManyAnswers, gridForRadioButtonAnswers, scrollPane, directInputTextField);
            //СТАВИМ PROMPT TEXT, ВОССТАНАВЛИВАЕМ
            setAnswerTextFields(isEditor, directInputTextField, answersLoaded, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, log);

            //УСТАНАВЛИВАЕМ TOOLPIPE`Ы, ЗАПРЕЩАЕМ НАВОДИТЬ ФОКУС
            setUpRightAnswerChooses(isEditor, checkBoxesLoaded, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, log);

            //УСТАНАВЛИВАЕМ TOOLPIPE`Ы, ЗАПРЕЩАЕМ НАВОДИТЬ ФОКУС, УСТАНАВЛИВАЕМ LISTENER`ОВ НА ОТКРЫТИЕ РЕДАКТОРА
            setUpOpenEditorBtns(log, debug, main, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, btn1, btn2, btn3, btn4, btn5, btn6);


            int isSpaseForCompilesTextField = 2;
            if (answerType[0] == "Compiles") isSpaseForCompilesTextField = 3;
            final int finalIsSpaseForCompilesTextField = isSpaseForCompilesTextField;

            //УСТАНАВЛИВАЕМ LISTENER`Ы ДЛЯ
            setListenersToAddTextFields(log, nAnLoaded, isEditor, answerType, HowManyAnswersRadioButton2, HowManyAnswersRadioButton3, HowManyAnswersRadioButton4, HowManyAnswersRadioButton5, HowManyAnswersRadioButton6, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField);


            question = new Question();

            Button btn = new Button("Далее");
            grid.add(btn, 1, 9);
            final Question finalQuestion = question;
            btn.setOnAction(event -> {
                String[] answers = new String[12];
                boolean[] checkBoxes = new boolean[6];
                boolean isError = false;
                //ЕСЛИ ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
                if (answerType[0].equals("One") || answerType[0].equals("Sort") || answerType[0].equals("Compiles")) {
                    try {
                        answers[0] = answerField1.getText();
                        answers[1] = answerField2.getText();
                        if (nAn >= 3) answers[2] = answerField3.getText();
                        if (nAn >= 4) answers[3] = answerField4.getText();
                        if (nAn >= 5) answers[4] = answerField5.getText();
                        if (nAn == 6) answers[5] = answerField6.getText();
                    } catch (Exception e) {
                        isError = true;
                        log.warning(Main.getStackTrace(e));
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
                        log.warning(Main.getStackTrace(e));
                    }

                    if (answerType[0].equals("Compiles")) {
                        answers[6] = answerField1Compilles.getText();
                        answers[7] = answerField2Compilles.getText();
                        if (nAn >= 3) answers[8] = answerField3Compilles.getText();
                        if (nAn >= 4) answers[9] = answerField4Compilles.getText();
                        if (nAn >= 5) answers[10] = answerField5Compilles.getText();
                        if (nAn == 6) answers[11] = answerField6Compilles.getText();
                    }

                    if (isError)
                        main.showAlert(Alert.AlertType.ERROR, "Ошибка", "Вы не выбрали правильный вариант ответа !", false, new GridPane());
                    //ЕСЛИ ПРЯМОЙ ВВОД
                } else if (answerType[0] == "DirectInput") {
                    answers[0] = directInputTextField.getText();
                    checkBoxes[0] = true;
                }
               finalQuestion.qText = webEditorEngine.executeScript("CKEDITOR.instances['editor1'].getData()").toString().replace("\n", "");
               finalQuestion.isRegistrSense = isSence[0];
               finalQuestion.isSpaceSense = isSence[1];
               finalQuestion.nAn = nAn;
               finalQuestion.answers = answers;
               finalQuestion.checkBoxes = checkBoxes;
               finalQuestion.i = nQu;
               finalQuestion.answerType = answerType[0];

                log.info("Text of " + nQu + "question" + finalQuestion.qText);
                if (answerType.equals("Compiles") || answerTypeComplies.isSelected())log.info("For Compiles: " +
                       finalQuestion.answers[0] + " : " + finalQuestion.answers[6] + "\n" +
                       finalQuestion.answers[1] + " : " + finalQuestion.answers[7] +"\n" +
                       finalQuestion.answers[2] + " : " + finalQuestion.answers[8] +"\n" +
                       finalQuestion.answers[3] + " : " + finalQuestion.answers[9] +"\n" +
                       finalQuestion.answers[4] + " : " + finalQuestion.answers[10] +"\n" +
                       finalQuestion.answers[5] + " : " + finalQuestion.answers[11]);


                if (nAn != null && !isError) window.close();
            });


            Scene scene = new Scene(grid, 920, 840);
            window.setScene(scene);
            window.setTitle("Вопрос " + nQu + "(" + howManyQuestions + ")");
            if (debug == true) grid.setGridLinesVisible(true);
            window.showAndWait();
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }

        return question;
    }

    public static void setListenersToAddTextFields(Logger log, int nAnLoaded, boolean isEditor, String[] answerType, RadioButton howManyAnswersRadioButton2, RadioButton howManyAnswersRadioButton3, RadioButton howManyAnswersRadioButton4, RadioButton howManyAnswersRadioButton5, RadioButton howManyAnswersRadioButton6, Label answer1, Label answer2, Label answer3, Label answer4, Label answer5, Label answer6, TextField answerField1, TextField answerField2, TextField answerField3, TextField answerField4, TextField answerField5, TextField answerField6, TextField answerField1Compilles, TextField answerField2Compilles, TextField answerField3Compilles, TextField answerField4Compilles, TextField answerField5Compilles, TextField answerField6Compilles, RadioButton answerChoose1, RadioButton answerChoose2, RadioButton answerChoose3, RadioButton answerChoose4, RadioButton answerChoose5, RadioButton answerChoose6, Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, GridPane gridForTextingAnswers, int finalIsSpaseForCompilesTextField) {
        try {
            if (isEditor) addToTextingAnswers(nAnLoaded, answerType, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField, log);

            howManyAnswersRadioButton2.setOnMouseClicked(event1 -> {
                addToTextingAnswers(2, answerType, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField, log);
            });
            howManyAnswersRadioButton3.setOnMouseClicked(event1 -> {
                addToTextingAnswers(3, answerType, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField, log);
            });

            howManyAnswersRadioButton4.setOnMouseClicked(event1 -> {
                addToTextingAnswers(4, answerType, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField, log);
            });

            howManyAnswersRadioButton5.setOnMouseClicked(event1 -> {
                addToTextingAnswers(5, answerType, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField, log);
            });

            howManyAnswersRadioButton6.setOnMouseClicked(event1 -> {
                addToTextingAnswers(6, answerType, answer1, answer2, answer3, answer4, answer5, answer6, answerField1, answerField2, answerField3, answerField4, answerField5, answerField6, answerField1Compilles, answerField2Compilles, answerField3Compilles, answerField4Compilles, answerField5Compilles, answerField6Compilles, answerChoose1, answerChoose2, answerChoose3, answerChoose4, answerChoose5, answerChoose6, btn1, btn2, btn3, btn4, btn5, btn6, gridForTextingAnswers, finalIsSpaseForCompilesTextField, log);
            });
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static void addToTextingAnswers(int timesToAdd, String[] answerType, Label answer1, Label answer2, Label answer3, Label answer4, Label answer5, Label answer6, TextField answerField1, TextField answerField2, TextField answerField3, TextField answerField4, TextField answerField5, TextField answerField6, TextField answerField1Compilles, TextField answerField2Compilles, TextField answerField3Compilles, TextField answerField4Compilles, TextField answerField5Compilles, TextField answerField6Compilles, RadioButton answerChoose1, RadioButton answerChoose2, RadioButton answerChoose3, RadioButton answerChoose4, RadioButton answerChoose5, RadioButton answerChoose6, Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6, GridPane gridForTextingAnswers, int finalIsSpaseForCompilesTextField, Logger log) {
        //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
        try {
            String savedText1 = null;
            String savedText2 = null;
            String savedText3 = null;
            String savedText4 = null;
            String savedText5 = null;
            String savedText6 = null;
            String savedText1Compiles = null;
            String savedText2Compiles = null;
            String savedText3Compiles = null;
            String savedText4Compiles = null;
            String savedText5Compiles = null;
            String savedText6Compiles = null;
            if (timesToAdd >= 2) {
                savedText1 = answerField1.getText();
                savedText1Compiles = answerField1Compilles.getText();
                savedText2 = answerField2.getText();
                savedText2Compiles = answerField2Compilles.getText();
            }
            if (timesToAdd >= 3) {
                savedText3 = answerField3.getText();
                savedText3Compiles = answerField3Compilles.getText();
            }
            if (timesToAdd >= 4) {
                savedText4 = answerField4.getText();
                savedText4Compiles = answerField4Compilles.getText();
            }
            if (timesToAdd >= 5) {
                savedText5 = answerField5.getText();
                savedText5Compiles = answerField5Compilles.getText();
            }
            if (timesToAdd == 6) {
                savedText6 = answerField6.getText();
                savedText6Compiles = answerField6Compilles.getText();
            }

            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            if (timesToAdd >= 2) {
                gridForTextingAnswers.add(answer1, 0, 0);
                gridForTextingAnswers.add(answerField1, 1, 0);
                if (!Objects.equals(answerType[0], "Compiles"))
                    gridForTextingAnswers.add(answerChoose1, finalIsSpaseForCompilesTextField, 0);
                gridForTextingAnswers.add(btn1, finalIsSpaseForCompilesTextField + 1, 0);

                gridForTextingAnswers.add(answer2, 0, 1);
                gridForTextingAnswers.add(answerField2, 1, 1);
                if (!Objects.equals(answerType[0], "Compiles"))
                    gridForTextingAnswers.add(answerChoose2, finalIsSpaseForCompilesTextField, 1);
                gridForTextingAnswers.add(btn2, finalIsSpaseForCompilesTextField + 1, 1);
            }
            if (timesToAdd >= 3) {
                gridForTextingAnswers.add(answer3, 0, 2);
                gridForTextingAnswers.add(answerField3, 1, 2);
                if (!Objects.equals(answerType[0], "Compiles"))
                    gridForTextingAnswers.add(answerChoose3, finalIsSpaseForCompilesTextField, 2);
                gridForTextingAnswers.add(btn3, finalIsSpaseForCompilesTextField + 1, 2);
            }
            if (timesToAdd >= 4) {
                gridForTextingAnswers.add(answer4, 0, 3);
                gridForTextingAnswers.add(answerField4, 1, 3);
                if (!Objects.equals(answerType[0], "Compiles"))
                    gridForTextingAnswers.add(answerChoose4, finalIsSpaseForCompilesTextField, 3);
                gridForTextingAnswers.add(btn4, finalIsSpaseForCompilesTextField + 1, 3);
            }
            if (timesToAdd >= 5) {
                gridForTextingAnswers.add(answer5, 0, 4);
                gridForTextingAnswers.add(answerField5, 1, 4);
                if (!Objects.equals(answerType[0], "Compiles"))
                    gridForTextingAnswers.add(answerChoose5, finalIsSpaseForCompilesTextField, 4);
                gridForTextingAnswers.add(btn5, finalIsSpaseForCompilesTextField + 1, 4);
            }
            if (timesToAdd == 6) {
                gridForTextingAnswers.add(answer6, 0, 5);
                gridForTextingAnswers.add(answerField6, 1, 5);
                if (!Objects.equals(answerType[0], "Compiles"))
                    gridForTextingAnswers.add(answerChoose6, finalIsSpaseForCompilesTextField, 5);
                gridForTextingAnswers.add(btn6, finalIsSpaseForCompilesTextField + 1, 5);
            }
            //ДОБВАЛЯЕМ ПОЛЯ ДЛЯ COMPILES
            if (answerType[0].equals("Compiles")){
                if (timesToAdd >= 2) {
                    gridForTextingAnswers.add(answerField1Compilles, 2, 0);
                    gridForTextingAnswers.add(answerField2Compilles, 2, 1);
                }
                if (timesToAdd >= 3) gridForTextingAnswers.add(answerField3Compilles, 2, 2);
                if (timesToAdd >= 4) gridForTextingAnswers.add(answerField4Compilles, 2, 3);
                if (timesToAdd >= 5) gridForTextingAnswers.add(answerField5Compilles, 2, 4);
                if (timesToAdd == 6) gridForTextingAnswers.add(answerField6Compilles, 2, 5);
            }else {
                gridForTextingAnswers.getChildren().remove(answerField1Compilles);
                gridForTextingAnswers.getChildren().remove(answerField2Compilles);
                gridForTextingAnswers.getChildren().remove(answerField3Compilles);
                gridForTextingAnswers.getChildren().remove(answerField4Compilles);
                gridForTextingAnswers.getChildren().remove(answerField5Compilles);
                gridForTextingAnswers.getChildren().remove(answerField6Compilles);
            }
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            if (timesToAdd >= 2) {
                answerField1.setText(savedText1);
                answerField2.setText(savedText2);
            }
            if (timesToAdd >= 3) answerField3.setText(savedText3);
            if (timesToAdd >= 4) answerField4.setText(savedText4);
            if (timesToAdd >= 5) answerField5.setText(savedText5);
            if (timesToAdd == 6) answerField6.setText(savedText6);
            if (timesToAdd >= 2) {
                answerField1Compilles.setText(savedText1Compiles);
                answerField2Compilles.setText(savedText2Compiles);
            }
            if (timesToAdd >= 3)answerField3Compilles.setText(savedText3Compiles);
            if (timesToAdd >= 4)answerField4Compilles.setText(savedText4Compiles);
            if (timesToAdd >= 5)answerField5Compilles.setText(savedText5Compiles);
            if (timesToAdd == 6)answerField6Compilles.setText(savedText6Compiles);
            nAn = timesToAdd;
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static void settingUpRegistrAndSpaceSense(Logger log, boolean debug, boolean isEditor, boolean isRegistrSense1, boolean isSpaceSense1, Label isRegistrSense, Label isSpaceSense, CheckBox isRegistrSenseCheckBox, CheckBox isSpaceSenseCheckBox, GridPane grid, boolean[] isSence) {
        //Чувствительность к регистру и пробелам
        try {
            grid.add(isRegistrSense, 0, 1);
            grid.add(isRegistrSenseCheckBox, 1, 1);

            grid.add(isSpaceSense, 0, 2);
            grid.add(isSpaceSenseCheckBox, 1, 2);

            isRegistrSenseCheckBox.setOnAction(event1 -> {
                if (isRegistrSenseCheckBox.isSelected() == true) {
                    log.info("Ответы не чусвтсвительны к регистру");
                    isSence[0] = true;
                } else isSence[0] = false;
            });
            isSpaceSenseCheckBox.setOnAction(event1 -> {
                if (isSpaceSenseCheckBox.isSelected() == true) {
                    log.info("Ответы не чусвтсвительны к пробелам");
                    isSence[1] = true;
                } else isSence[1] = false;
            });

            if (isEditor) {
                isRegistrSenseCheckBox.setSelected(isRegistrSense1);
                isSence[0] = isRegistrSense1;
            }
            if (isEditor) {
                isSpaceSenseCheckBox.setSelected(isSpaceSense1);
                isSence[1] = isSpaceSense1;
            }
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static void settingUpNumderOfAnswersRadioBtns(boolean isEditor, int nAnLoaded, GridPane gridForRadioButtonAnswers, RadioButton howManyAnswersRadioButton2, RadioButton howManyAnswersRadioButton3, RadioButton howManyAnswersRadioButton4, RadioButton howManyAnswersRadioButton5, RadioButton howManyAnswersRadioButton6, Logger log) {
        try {
            howManyAnswersRadioButton2.setSelected(true);

            howManyAnswersRadioButton2.setText("2 ");
            if (nAnLoaded == 2 && isEditor) howManyAnswersRadioButton2.setSelected(true);

            howManyAnswersRadioButton3.setText("3 ");
            if (nAnLoaded == 3 && isEditor) howManyAnswersRadioButton2.setSelected(true);

            howManyAnswersRadioButton4.setText("4 ");
            if (nAnLoaded == 4 && isEditor) howManyAnswersRadioButton2.setSelected(true);

            howManyAnswersRadioButton5.setText("5 ");
            if (nAnLoaded == 5 && isEditor) howManyAnswersRadioButton2.setSelected(true);

            howManyAnswersRadioButton6.setText("6 ");
            if (nAnLoaded == 6 && isEditor) howManyAnswersRadioButton2.setSelected(true);

            //Добавляем RadioButton для количество ответов в сетку
            gridForRadioButtonAnswers.add(howManyAnswersRadioButton2, 0, 0);
            gridForRadioButtonAnswers.add(howManyAnswersRadioButton3, 1, 0);
            gridForRadioButtonAnswers.add(howManyAnswersRadioButton4, 2, 0);
            gridForRadioButtonAnswers.add(howManyAnswersRadioButton5, 3, 0);
            gridForRadioButtonAnswers.add(howManyAnswersRadioButton6, 4, 0);

            ToggleGroup groupHowManyAnswersRadioButton = new ToggleGroup();
            howManyAnswersRadioButton2.setToggleGroup(groupHowManyAnswersRadioButton);
            howManyAnswersRadioButton3.setToggleGroup(groupHowManyAnswersRadioButton);
            howManyAnswersRadioButton4.setToggleGroup(groupHowManyAnswersRadioButton);
            howManyAnswersRadioButton5.setToggleGroup(groupHowManyAnswersRadioButton);
            howManyAnswersRadioButton6.setToggleGroup(groupHowManyAnswersRadioButton);
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static void setAnswerTextFields(boolean isEditor, TextField directInputTextField, String[] answersLoaded, TextField answerField1, TextField answerField2, TextField answerField3, TextField answerField4, TextField answerField5, TextField answerField6, TextField answerField1Compilles, TextField answerField2Compilles, TextField answerField3Compilles, TextField answerField4Compilles, TextField answerField5Compilles, TextField answerField6Compilles, Logger log) {

        if (isEditor) {
            for (int i = 0; i < answersLoaded.length; i++) {
                try {
                    answersLoaded[i] = HTMLEntities.unhtmlentities(answersLoaded[i]);
                    log.info("Formatted Answers: " + answersLoaded[i]);
                } catch (Exception e) {
                    log.warning(Main.getStackTrace(e));
                }
            }
        }


        try {
            answerField1.setPromptText("Текст 1 ответа");
            answerField2.setPromptText("Текст 2 ответа");
            answerField3.setPromptText("Текст 3 ответа");
            answerField4.setPromptText("Текст 4 ответа");
            answerField5.setPromptText("Текст 5 ответа");
            answerField6.setPromptText("Текст 6 ответа");
            if (isEditor) {
                directInputTextField.setText(answersLoaded[0]);
                answerField1.setText(answersLoaded[0]);
                answerField2.setText(answersLoaded[1]);
                answerField3.setText(answersLoaded[2]);
                answerField4.setText(answersLoaded[3]);
                answerField5.setText(answersLoaded[4]);
                answerField6.setText(answersLoaded[5]);

                answerField1Compilles.setText(answersLoaded[6]);
                answerField2Compilles.setText(answersLoaded[7]);
                answerField3Compilles.setText(answersLoaded[8]);
                answerField4Compilles.setText(answersLoaded[9]);
                answerField5Compilles.setText(answersLoaded[10]);
                answerField6Compilles.setText(answersLoaded[11]);
            }
            //setPromtText для полей ввода у соответствия
            answerField1Compilles.setPromptText("Соответствие для 1 ответа");
            answerField2Compilles.setPromptText("Соответствие для 2 ответа");
            answerField3Compilles.setPromptText("Соответствие для 3 ответа");
            answerField4Compilles.setPromptText("Соответствие для 4 ответа");
            answerField5Compilles.setPromptText("Соответствие для 5 ответа");
            answerField6Compilles.setPromptText("Соответствие для 6 ответа");
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static void setUpRightAnswerChooses(boolean isEditor, boolean[] checkBoxes1, RadioButton answerChoose1, RadioButton answerChoose2, RadioButton answerChoose3, RadioButton answerChoose4, RadioButton answerChoose5, RadioButton answerChoose6, Logger log) {
        try {
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
            if (isEditor) {
                answerChoose1.setSelected(checkBoxes1[0]);
                answerChoose2.setSelected(checkBoxes1[1]);
                answerChoose3.setSelected(checkBoxes1[2]);
                answerChoose4.setSelected(checkBoxes1[3]);
                answerChoose5.setSelected(checkBoxes1[4]);
                answerChoose6.setSelected(checkBoxes1[5]);
            }
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }

    }

    public static void setUpOpenEditorBtns(Logger log, boolean debug, Main main, TextField answerField1, TextField answerField2, TextField answerField3, TextField answerField4, TextField answerField5, TextField answerField6, Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6) {
        try {
            Tooltip tooltip = new Tooltip("Вы можете написать ответы на вопрос с помощью редактора");

            btn1.setTooltip(tooltip);
            btn1.setFocusTraversable(false);
            btn1.setOnAction(event1 -> {
                answerField1.setText(openEditor(" ответа", 1, main, debug, log));
            });

            btn2.setTooltip(tooltip);
            btn2.setFocusTraversable(false);
            btn2.setOnAction(event1 -> {
                answerField2.setText(openEditor(" ответа", 2, main, debug, log));
            });

            btn3.setTooltip(tooltip);
            btn3.setFocusTraversable(false);
            btn3.setOnAction(event1 -> {
                answerField3.setText(openEditor(" ответа", 3, main, debug, log));
            });

            btn4.setTooltip(tooltip);
            btn4.setFocusTraversable(false);
            btn4.setOnAction(event1 -> {
                answerField4.setText(openEditor(" ответа", 4, main, debug, log));
            });

            btn5.setTooltip(tooltip);
            btn5.setFocusTraversable(false);
            btn5.setOnAction(event1 -> {
                answerField5.setText(openEditor(" ответа", 5, main, debug, log));
            });

            btn6.setTooltip(tooltip);
            btn6.setFocusTraversable(false);
            btn6.setOnAction(event1 -> {
                answerField6.setText(openEditor(" ответа", 6, main, debug, log));
            });
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static void setAnswerTypeChoosers(Logger log, boolean isEditor, String[] answerType, GridPane grid, RadioButton answerTypeUsual, RadioButton answerTypeDirectInput, RadioButton answerTypeComplies, RadioButton answerTypeSort, Label howManyAnswers, GridPane gridForRadioButtonAnswers, ScrollPane scrollPane, TextField directInputTextField) {
        try {
            String isEd = "false";
            if (isEditor) isEd = "true";
            log.info("is Editor Mode: " + isEd);

            if (isEditor) {
                if (answerType[0].equals("DirectInput")) {
                    log.info("DirectInput - creating answerTextField");
                    grid.getChildren().remove(directInputTextField);
                    grid.getChildren().remove(gridForRadioButtonAnswers);
                    grid.getChildren().remove(howManyAnswers);
                    //Убираем поля с ответами
                    grid.getChildren().remove(scrollPane);
                    grid.add(directInputTextField, 0, 4);
                    answerTypeDirectInput.setSelected(true);
                    nAn = 1;
                }
            }
            answerTypeDirectInput.setOnAction(event -> {
                grid.getChildren().remove(directInputTextField);
                grid.getChildren().remove(gridForRadioButtonAnswers);
                grid.getChildren().remove(howManyAnswers);
                //Убираем поля с ответами
                grid.getChildren().remove(scrollPane);
                grid.add(directInputTextField, 0, 4);
                nAn = 1;
                answerType[0] = "DirectInput";
                log.info("Choosed answerType: " + answerType[0]);
            });

            //Сортировка
            if (isEditor) {
                if (answerType[0].equals("Sort")) {
                    grid.getChildren().remove(directInputTextField);
                    grid.getChildren().remove(gridForRadioButtonAnswers);
                    grid.getChildren().remove(howManyAnswers);
                    grid.getChildren().remove(scrollPane);
                    grid.add(howManyAnswers, 0, 4);
                    grid.add(gridForRadioButtonAnswers, 0, 5);
                    grid.add(scrollPane, 0, 6);
                    answerTypeSort.setSelected(true);
                }
            }
            answerTypeSort.setOnAction(event2 -> {
                grid.getChildren().remove(directInputTextField);
                grid.getChildren().remove(gridForRadioButtonAnswers);
                grid.getChildren().remove(howManyAnswers);
                grid.getChildren().remove(scrollPane);
                grid.add(howManyAnswers, 0, 4);
                grid.add(gridForRadioButtonAnswers, 0, 5);
                grid.add(scrollPane, 0, 6);
                answerType[0] = "Sort";
                log.info("Choosed answerType: " + answerType[0]);
            });
            //ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
            if (isEditor){
                if (answerType[0].equals("One") || answerType[0].equals("Many")) {
                    grid.getChildren().remove(directInputTextField);
                    grid.getChildren().remove(gridForRadioButtonAnswers);
                    grid.getChildren().remove(howManyAnswers);
                    grid.getChildren().remove(scrollPane);
                    grid.add(howManyAnswers, 0, 4);
                    grid.add(gridForRadioButtonAnswers, 0, 5);
                    grid.add(scrollPane, 0, 6);
                    answerTypeUsual.setSelected(true);
                }
            }

            answerTypeUsual.setOnAction(event -> {
                grid.getChildren().remove(directInputTextField);
                grid.getChildren().remove(gridForRadioButtonAnswers);
                grid.getChildren().remove(howManyAnswers);
                grid.getChildren().remove(scrollPane);
                grid.add(howManyAnswers, 0, 4);
                grid.add(gridForRadioButtonAnswers, 0, 5);
                grid.add(scrollPane, 0, 6);
                answerType[0] = "One";
                log.info("Choosed answerType: " + answerType[0]);
            });
            if (isEditor && answerType[0].equals("Compiles")){
                grid.getChildren().remove(directInputTextField);
                grid.getChildren().remove(gridForRadioButtonAnswers);
                grid.getChildren().remove(howManyAnswers);
                grid.getChildren().remove(scrollPane);
                grid.add(howManyAnswers, 0, 4);
                grid.add(gridForRadioButtonAnswers, 0, 5);
                grid.add(scrollPane, 0, 6);
                answerTypeComplies.setSelected(true);
            }
            answerTypeComplies.setOnAction(event2 -> {
                grid.getChildren().remove(directInputTextField);
                grid.getChildren().remove(gridForRadioButtonAnswers);
                grid.getChildren().remove(howManyAnswers);
                grid.getChildren().remove(scrollPane);
                grid.add(howManyAnswers, 0, 4);
                grid.add(gridForRadioButtonAnswers, 0, 5);
                grid.add(scrollPane, 0, 6);
                answerType[0] = "Compiles";
                log.info("Choosed answerType: " + answerType[0]);
            });
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
    }

    public static String openEditor(String QuOrAn, Integer n, Main main, Boolean debug, Logger log) {
        String htmlCode = null;
        try {
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
                log.info("Введённый HTML код на " + n + " вопрос: " + htmlCode);
            } else {
                htmlCode = "";
                log.info("Введённый HTML код на " + n + " вопрос: пуст(" + htmlCode + ")");
            }
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }
        return htmlCode;
    }

    /*public static Question newEditorOfQu(String qText,
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
        saveTextOuToFile(qText, debug);


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

        Label howManyAnswersLabel = new Label("Количество ответов: ");
        GridPane gridForRadioButtonAnswers = new GridPane();
        gridForRadioButtonAnswers.setGridLinesVisible(debug);*//*
        grid.add(gridForRadioButtonAnswers, 0, 4);*//*

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
            grid.getChildren().remove(directInputTextField);
            grid.getChildren().remove(gridForRadioButtonAnswers);
            grid.getChildren().remove(howManyAnswersLabel);
            //Убираем поля с ответами
            grid.getChildren().remove(scrollPaneForTextingAnswers);
            grid.getChildren().remove(gridForRadioButtonAnswers);
            grid.add(directInputTextField, 0, 4);
            answerTypeDirectInput.setSelected(true);
            directInputTextField.setText(answers1[0]);
            nAn = 1;
        }
        answerTypeDirectInput.setOnAction(event -> {
            grid.getChildren().remove(directInputTextField);
            grid.getChildren().remove(gridForRadioButtonAnswers);
            grid.getChildren().remove(howManyAnswersLabel);
            //Убираем поля с ответами
            grid.getChildren().remove(scrollPaneForTextingAnswers);
            grid.getChildren().remove(gridForRadioButtonAnswers);
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
        if (answerType[0] == "Compiles"){
            grid.getChildren().remove(directInputTextField);
            grid.add(howManyAnswersLabel, 0, 4);
            grid.add(gridForRadioButtonAnswers, 0, 5);
            grid.add(scrollPaneForTextingAnswers, 0, 6);
            answerTypeComplies.setSelected(true);
        }
        answerTypeComplies.setOnAction(event -> {
            grid.getChildren().remove(directInputTextField);
            grid.add(howManyAnswersLabel, 0, 4);
            grid.add(gridForRadioButtonAnswers, 0, 5);
            grid.add(scrollPaneForTextingAnswers, 0, 6);
            answerType[0] = "Compiles";
        });

        gridForTextingAnswers.setMinHeight(100);
        gridForTextingAnswers.setGridLinesVisible(debug);
        gridForTextingAnswers.setVgap(11);
        gridForTextingAnswers.setVgap(7);*//*
        grid.add(scrollPane, 0, 5);*//*
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

        TextField answerField1Compilles = new TextField();
        TextField answerField2Compilles = new TextField();
        TextField answerField3Compilles = new TextField();
        TextField answerField4Compilles = new TextField();
        TextField answerField5Compilles = new TextField();
        TextField answerField6Compilles = new TextField();

        //setPromtText для полей ввода
        answerField1.setPromptText("Текст 1 ответа");
        answerField2.setPromptText("Текст 2 ответа");
        answerField3.setPromptText("Текст 3 ответа");
        answerField4.setPromptText("Текст 4 ответа");
        answerField5.setPromptText("Текст 5 ответа");
        answerField6.setPromptText("Текст 6 ответа");

        //setPromtText для полей ввода у соответствия
        answerField1Compilles.setPromptText("Соответствие для 1 ответа");
        answerField2Compilles.setPromptText("Соответствие для 2 ответа");
        answerField3Compilles.setPromptText("Соответствие для 3 ответа");
        answerField4Compilles.setPromptText("Соответствие для 4 ответа");
        answerField5Compilles.setPromptText("Соответствие для 5 ответа");
        answerField6Compilles.setPromptText("Соответствие для 6 ответа");

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

                gridForTextingAnswers.add(answer1, 0, 0);
                gridForTextingAnswers.add(answerField1, 1, 0);
                if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose1, 2, 0);
                gridForTextingAnswers.add(btn1, 3, 0);


            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose2, 2, 1);
            gridForTextingAnswers.add(btn2, 3, 1);

            if (nAn1 >= 3) {
                gridForTextingAnswers.add(answer3, 0, 2);
                gridForTextingAnswers.add(answerField3, 1, 2);
                if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose3, 2, 2);
                gridForTextingAnswers.add(btn3, 3, 2);
            }
            if (nAn1 >= 4) {
                gridForTextingAnswers.add(answer4, 0, 3);
                gridForTextingAnswers.add(answerField4, 1, 3);
                if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose4, 2, 3);
                gridForTextingAnswers.add(btn4, 3, 3);
            }
            if (nAn1 >= 5) {
                gridForTextingAnswers.add(answer5, 0, 4);
                gridForTextingAnswers.add(answerField5, 1, 4);
                if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose5, 2, 4);
                gridForTextingAnswers.add(btn5, 3, 4);
            }
            if (nAn1 >= 6) {
                gridForTextingAnswers.add(answer6, 0, 5);
                gridForTextingAnswers.add(answerField6, 1, 5);
                if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose6, 2, 5);
                gridForTextingAnswers.add(btn6, 3, 5);
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


        int isSpaseForCompilesTextField = 1;
        if (answerType[0] == "Compiles") isSpaseForCompilesTextField = 3;
        final int finalIsSpaseForCompilesTextField = isSpaseForCompilesTextField;

        //ПРИ НАЖАТИИ НА "2 ОТВЕТА"
        HowManyAnswersRadioButton2.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText1Compiles = answerField1Compilles.getText();
            String savedText2Compiles = answerField2Compilles.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();
            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose1, finalIsSpaseForCompilesTextField, 0);
            gridForTextingAnswers.add(btn1, finalIsSpaseForCompilesTextField + 1, 0);
            if (answerType[0] == "Compiles"){
                gridForTextingAnswers.add(answerField1Compilles, 2, 0);
                gridForTextingAnswers.add(answerField2Compilles, 2, 1);
            }


            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose2, finalIsSpaseForCompilesTextField, 1);
            gridForTextingAnswers.add(btn2, finalIsSpaseForCompilesTextField + 1, 1);
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField1Compilles.setText(savedText1Compiles);
            answerField2Compilles.setText(savedText2Compiles);
            nAn = 2;
        });
        HowManyAnswersRadioButton3.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText1Compiles = answerField1Compilles.getText();
            String savedText2Compiles = answerField2Compilles.getText();
            String savedText3Compiles = answerField3Compilles.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose1, finalIsSpaseForCompilesTextField, 0);
            gridForTextingAnswers.add(btn1, finalIsSpaseForCompilesTextField + 1, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose2, finalIsSpaseForCompilesTextField, 1);
            gridForTextingAnswers.add(btn2, finalIsSpaseForCompilesTextField + 1, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose3, finalIsSpaseForCompilesTextField + 1, 2);
            gridForTextingAnswers.add(btn3, finalIsSpaseForCompilesTextField + 1, 2);
            if (answerType[0] == "Compiles"){
                gridForTextingAnswers.add(answerField1Compilles, 2, 0);
                gridForTextingAnswers.add(answerField2Compilles, 2, 1);
                gridForTextingAnswers.add(answerField3Compilles, 2, 2);
            }
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField1Compilles.setText(savedText1Compiles);
            answerField2Compilles.setText(savedText2Compiles);
            answerField3Compilles.setText(savedText3Compiles);
            nAn = 3;
        });

        HowManyAnswersRadioButton4.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            String savedText1Compiles = answerField1Compilles.getText();
            String savedText2Compiles = answerField2Compilles.getText();
            String savedText3Compiles = answerField3Compilles.getText();
            String savedText4Compiles = answerField4Compilles.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose1, finalIsSpaseForCompilesTextField, 0);
            gridForTextingAnswers.add(btn1, finalIsSpaseForCompilesTextField + 1, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose2, finalIsSpaseForCompilesTextField, 1);
            gridForTextingAnswers.add(btn2, finalIsSpaseForCompilesTextField + 1, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose3, finalIsSpaseForCompilesTextField + 1, 2);
            gridForTextingAnswers.add(btn3, finalIsSpaseForCompilesTextField + 1, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose4, finalIsSpaseForCompilesTextField + 1, 3);
            gridForTextingAnswers.add(btn4, finalIsSpaseForCompilesTextField + 1, 3);
            if (answerType[0] == "Compiles"){
                gridForTextingAnswers.add(answerField1Compilles, 2, 0);
                gridForTextingAnswers.add(answerField2Compilles, 2, 1);
                gridForTextingAnswers.add(answerField3Compilles, 2, 2);
                gridForTextingAnswers.add(answerField4Compilles, 2, 3);
            }
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField1Compilles.setText(savedText1Compiles);
            answerField2Compilles.setText(savedText2Compiles);
            answerField3Compilles.setText(savedText3Compiles);
            answerField4Compilles.setText(savedText4Compiles);
            nAn = 4;
        });

        HowManyAnswersRadioButton5.setOnMouseClicked(event1 -> {
            //ПОЛУЧАЕМ ВВЕДЁННЫЙ ТЕКСТ С ПОЛЕЙ
            String savedText1 = answerField1.getText();
            String savedText2 = answerField2.getText();
            String savedText3 = answerField3.getText();
            String savedText4 = answerField4.getText();
            String savedText5 = answerField5.getText();
            String savedText1Compiles = answerField1Compilles.getText();
            String savedText2Compiles = answerField2Compilles.getText();
            String savedText3Compiles = answerField3Compilles.getText();
            String savedText4Compiles = answerField4Compilles.getText();
            String savedText5Compiles = answerField5Compilles.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose1, finalIsSpaseForCompilesTextField, 0);
            gridForTextingAnswers.add(btn1, finalIsSpaseForCompilesTextField + 1, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose2, finalIsSpaseForCompilesTextField, 1);
            gridForTextingAnswers.add(btn2, finalIsSpaseForCompilesTextField + 1, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose3, finalIsSpaseForCompilesTextField, 2);
            gridForTextingAnswers.add(btn3, finalIsSpaseForCompilesTextField + 1, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose4, finalIsSpaseForCompilesTextField, 3);
            gridForTextingAnswers.add(btn4, finalIsSpaseForCompilesTextField + 1, 3);

            gridForTextingAnswers.add(answer5, 0, 4);
            gridForTextingAnswers.add(answerField5, 1, 4);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose5, finalIsSpaseForCompilesTextField, 4);
            gridForTextingAnswers.add(btn5, finalIsSpaseForCompilesTextField + 1, 4);
            if (answerType[0] == "Compiles"){
                gridForTextingAnswers.add(answerField1Compilles, 2, 0);
                gridForTextingAnswers.add(answerField2Compilles, 2, 1);
                gridForTextingAnswers.add(answerField3Compilles, 2, 2);
                gridForTextingAnswers.add(answerField4Compilles, 2, 3);
                gridForTextingAnswers.add(answerField5Compilles, 2, 4);
            }
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField5.setText(savedText5);
            answerField1Compilles.setText(savedText1Compiles);
            answerField2Compilles.setText(savedText2Compiles);
            answerField3Compilles.setText(savedText3Compiles);
            answerField4Compilles.setText(savedText4Compiles);
            answerField5Compilles.setText(savedText5Compiles);
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
            String savedText1Compiles = answerField1Compilles.getText();
            String savedText2Compiles = answerField2Compilles.getText();
            String savedText3Compiles = answerField3Compilles.getText();
            String savedText4Compiles = answerField4Compilles.getText();
            String savedText5Compiles = answerField5Compilles.getText();
            String savedText6Compiles = answerField6Compilles.getText();
            //ОЧИЩАЕМ СЕТКУ ОТ ЭЛЕМЕНТОВ
            gridForTextingAnswers.getChildren().clear();

            //ДОБАВЛЯЕМ ЭЛЕМЕНТЫ(LABEL, ПОЛЕ, RADIO BUTTON, ОТКРЫТИЕ РЕДАКТОРА)
            gridForTextingAnswers.add(answer1, 0, 0);
            gridForTextingAnswers.add(answerField1, 1, 0);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose1, finalIsSpaseForCompilesTextField, 0);
            gridForTextingAnswers.add(btn1, finalIsSpaseForCompilesTextField + 1, 0);

            gridForTextingAnswers.add(answer2, 0, 1);
            gridForTextingAnswers.add(answerField2, 1, 1);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose2, finalIsSpaseForCompilesTextField, 1);
            gridForTextingAnswers.add(btn2, finalIsSpaseForCompilesTextField + 1, 1);

            gridForTextingAnswers.add(answer3, 0, 2);
            gridForTextingAnswers.add(answerField3, 1, 2);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose3, finalIsSpaseForCompilesTextField, 2);
            gridForTextingAnswers.add(btn3, finalIsSpaseForCompilesTextField + 1, 2);

            gridForTextingAnswers.add(answer4, 0, 3);
            gridForTextingAnswers.add(answerField4, 1, 3);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose4, finalIsSpaseForCompilesTextField, 3);
            gridForTextingAnswers.add(btn4, finalIsSpaseForCompilesTextField + 1, 3);

            gridForTextingAnswers.add(answer5, 0, 4);
            gridForTextingAnswers.add(answerField5, 1, 4);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose5, finalIsSpaseForCompilesTextField, 4);
            gridForTextingAnswers.add(btn5, finalIsSpaseForCompilesTextField + 1, 4);

            gridForTextingAnswers.add(answer6, 0, 5);
            gridForTextingAnswers.add(answerField6, 1, 5);
            if (answerType[0] != "Compiles")gridForTextingAnswers.add(answerChoose6, finalIsSpaseForCompilesTextField, 5);
            gridForTextingAnswers.add(btn6, finalIsSpaseForCompilesTextField + 1, 5);
            if (answerType[0] == "Compiles"){
                gridForTextingAnswers.add(answerField1Compilles, 2, 0);
                gridForTextingAnswers.add(answerField2Compilles, 2, 1);
                gridForTextingAnswers.add(answerField3Compilles, 2, 2);
                gridForTextingAnswers.add(answerField4Compilles, 2, 3);
                gridForTextingAnswers.add(answerField5Compilles, 2, 4);
                gridForTextingAnswers.add(answerField5Compilles, 2, 5);
            }
            //ВОССТАНАВЛИВАЕМ СОХРАНЁННЫЙ ТЕКСТ
            answerField1.setText(savedText1);
            answerField2.setText(savedText2);
            answerField3.setText(savedText3);
            answerField4.setText(savedText4);
            answerField5.setText(savedText5);
            answerField6.setText(savedText6);
            answerField1Compilles.setText(savedText1Compiles);
            answerField1Compilles.setText(savedText2Compiles);
            answerField1Compilles.setText(savedText3Compiles);
            answerField1Compilles.setText(savedText4Compiles);
            answerField1Compilles.setText(savedText5Compiles);
            answerField1Compilles.setText(savedText6Compiles);
            nAn = 6;
        });


        Question question = new Question();
        String[] answers = new String[6];
        boolean[] checkBoxes = new boolean[6];
        final boolean[] isError = {false};

        Button btn = new Button("Далее");
        grid.add(btn, 1, 9);
        btn.setOnAction(event -> {
            if (answerType[0] == "One" || answerType[0] == "Sort") {

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
                if (answerType[0] == "Compiles") {
                    answers[6] = answerField1Compilles.getText();
                    answers[7] = answerField2Compilles.getText();
                    if (nAn >= 3) answers[8] = answerField3Compilles.getText();
                    if (nAn >= 4) answers[9] = answerField4Compilles.getText();
                    if (nAn >= 5) answers[10] = answerField5Compilles.getText();
                    if (nAn == 6) answers[11] = answerField6Compilles.getText();
                }
            } else if (answerType[0] == "DirectInput") {
                answers[0] = directInputTextField.getText();
                checkBoxes[0] = true;
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
    }*/

    public static void saveTextOuToFile(String qText, boolean debug, Logger log) {
        try {
        //Сохраняем текст вопроса в файл
        BufferedWriter bw = null;
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(System.getProperty("user.dir") + "\\sample\\ckeditor\\editorForEditingQu.htm")));
            /*OUT*/ System.out.println(System.getProperty("user.dir") + "\\sample\\ckeditor\\editorForEditingQu.htm");


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
            log.warning(Main.getStackTrace(e));
        }
    }
}
