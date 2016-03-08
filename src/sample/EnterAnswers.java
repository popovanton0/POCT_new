package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by popov on 22.02.2016.
 */

public class EnterAnswers {
    public static AnswerObject newWindow(Integer nAn, Boolean debug, Main main) {
        String[] answers = new String[nAn];
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ScrollPane pane = new ScrollPane(grid);
        Label answer1 = new Label();
        Label answer2 = new Label();
        Label answer3 = new Label();
        Label answer4 = new Label();
        Label answer5 = new Label();
        Label answer6 = new Label();

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

        Tooltip tooltip = new Tooltip("Вы можете написать ответы на вопрос с помощью редактора");
        Button btn1 = new Button("Открыть редактор");
        btn1.setTooltip(tooltip);
        btn1.setOnAction(event1 -> {
            answerField1.setText(HowManyAnswers.openEditor(" ответа", 1, main, debug));
        });
        Button btn2 = new Button("Открыть редактор");
        btn2.setTooltip(tooltip);
        btn2.setOnAction(event1 -> {
            answerField2.setText(HowManyAnswers.openEditor(" ответа", 2, main, debug));
        });
        Button btn3 = new Button("Открыть редактор");
        btn3.setTooltip(tooltip);
        btn3.setOnAction(event1 -> {
            answerField3.setText(HowManyAnswers.openEditor(" ответа", 3, main, debug));
        });
        Button btn4 = new Button("Открыть редактор");
        btn4.setTooltip(tooltip);
        btn4.setOnAction(event1 -> {
            answerField5.setText(HowManyAnswers.openEditor(" ответа", 4, main, debug));
        });
        Button btn5 = new Button("Открыть редактор");
        btn5.setTooltip(tooltip);
        btn5.setOnAction(event1 -> {
            answerField5.setText(HowManyAnswers.openEditor(" ответа", 5, main, debug));
        });
        Button btn6 = new Button("Открыть редактор");
        btn6.setTooltip(tooltip);
        btn6.setOnAction(event1 -> {
            answerField6.setText(HowManyAnswers.openEditor(" ответа", 6, main, debug));
        });


        answer1.setText("Ответ 1" );
        answer2.setText("Ответ 2" );
        answer3.setText("Ответ 3" );
        answer4.setText("Ответ 4" );
        answer5.setText("Ответ 5" );
        answer6.setText("Ответ 6" );

        answerField1.setPromptText("Текст 1 ответа");
        answerField2.setPromptText("Текст 2 ответа");
        answerField3.setPromptText("Текст 3 ответа");
        answerField4.setPromptText("Текст 4 ответа");
        answerField5.setPromptText("Текст 5 ответа");
        answerField6.setPromptText("Текст 6 ответа");

        grid.add(answer1, 0, 0);
        grid.add(answerField1, 1, 0);
        grid.add(answerChoose1, 2, 0);
            grid.add(btn1, 3, 0);

        answerChoose1.setSelected(true);

        grid.add(answer2, 0, 1);
        grid.add(answerField2, 1, 1);
        grid.add(answerChoose2, 2, 1);
            grid.add(btn2, 3, 1);

        if(nAn >= 3) {
        grid.add(answer3, 0, 2);
        grid.add(answerField3, 1, 2);
        grid.add(answerChoose3, 2, 2);
            grid.add(btn3, 3, 2);
            }

        if(nAn >= 4) {
        grid.add(answer4, 0, 3);
        grid.add(answerField4, 1, 3);
        grid.add(answerChoose4, 2, 3);
            grid.add(btn4, 3, 3);
            }

        if(nAn >= 5){
        grid.add(answer5, 0, 4);
        grid.add(answerField5, 1, 4);
        grid.add(answerChoose5, 2, 4);
            grid.add(btn5, 3, 4);
            }

        if(nAn == 6){
        grid.add(answer6, 0, 5);
        grid.add(answerField6, 1, 5);
        grid.add(answerChoose6, 2, 5);
            grid.add(btn6, 3, 5);
            }

        ToggleGroup group = new ToggleGroup();
        answerChoose1.setToggleGroup(group);
        answerChoose2.setToggleGroup(group);
        answerChoose3.setToggleGroup(group);
        answerChoose4.setToggleGroup(group);
        answerChoose5.setToggleGroup(group);
        answerChoose6.setToggleGroup(group);

        answerChoose1.setUserData("1");
        answerChoose2.setUserData("2");
        answerChoose3.setUserData("3");
        answerChoose4.setUserData("4");
        answerChoose5.setUserData("5");
        answerChoose6.setUserData("6");

        boolean [] checkBoxes = new boolean[nAn];

        AnswerObject answerReturn = new AnswerObject();

        int btnPlace = 0;
        if (nAn == 2) btnPlace = 2;
        if (nAn == 3) btnPlace = 3;
        if (nAn == 4) btnPlace = 4;
        if (nAn == 5) btnPlace = 5;
        if (nAn == 6) btnPlace = 6;

        Button btn = new Button("Далее");
        grid.add(btn, 2, btnPlace);

        btn.setOnAction(event -> {

                answers[0] = answerField1.getText();
                answers[1] = answerField2.getText();
                if(nAn >= 3) answers[2] = answerField3.getText();
                if(nAn >= 4) answers[3] = answerField4.getText();
                if(nAn >= 5) answers[4] = answerField5.getText();
                if(nAn == 6) answers[5] = answerField6.getText();

            if(group.getSelectedToggle().getUserData() == "1") checkBoxes[0] = true;
            if(group.getSelectedToggle().getUserData() == "2") checkBoxes[1] = true;
            if(group.getSelectedToggle().getUserData() == "3") checkBoxes[2] = true;
            if(group.getSelectedToggle().getUserData() == "4") checkBoxes[3] = true;
            if(group.getSelectedToggle().getUserData() == "5") checkBoxes[4] = true;
            if(group.getSelectedToggle().getUserData() == "6") checkBoxes[5] = true;

            answerReturn.answers = answers;
            answerReturn.checkBoxes = checkBoxes;

            window.close();
        });


        Scene scene = new Scene(pane, 450, 300);
        window.setScene(scene);
        window.setTitle("Введите ответы на вопрос(ы) ");
        if(debug == true) grid.setGridLinesVisible(true);
        window.showAndWait();

        return answerReturn;
    }
    public void add(int i){
        Label answer = new Label();
        TextField answerField = new TextField();
        GridPane grid = new GridPane();
        answer.setText("Ответ " + i);
        grid.add(answer, 0, i);

        answerField.setText("Текст " + i + " ответа");
        grid.add(answerField, 1, i);
    }

}
