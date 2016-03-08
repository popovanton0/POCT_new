package sample;

/**
 * Created by popov on 22.02.2016.
 */
public class Question {

    String qText;
    boolean isRegistrSense;
    boolean isSpaceSense;
    Integer nAn;
    String[] answers;
    boolean[] checkBoxes;
    Integer i;
    String answerType;

    public String getFormatedResult() {
        String isRegistrSenseStr;
        String isSpaceSenseStr;
        String ansewrsFmt = "";

        if (isRegistrSense) isRegistrSenseStr = "true";
        else isRegistrSenseStr = "false";
        if (isSpaceSense) isSpaceSenseStr = "true";
        else isSpaceSenseStr = "false";

        for (int j = 0; j < checkBoxes.length; j++) {
            if (checkBoxes[j] == true) {
                for (int k = 0; k < checkBoxes.length; k++) {
                    if (k != j) {
                        if (checkBoxes[k]) answerType = "Many";
                    }
                }
            }
        }

        // ЕСЛИ ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
        if (answerType == "One" || answerType == "Many") {
            for (int i = 0; i < nAn; i++) {
                int n = i + 1;
                String ToF = "False";
                if (checkBoxes[i]) ToF = "True";
                String answer = "{" +
                        "\"NUMBER\":" + n + "," +
                        "\"TEXT\":\"<p>" + answers[i] + "</p>\\n\"," +
                        "\"VALUE\":\"" + ToF + "\"" +
                        "}";

                if (i == (nAn - 1)) ansewrsFmt = ansewrsFmt + answer;
                else ansewrsFmt = ansewrsFmt + answer + ",";
            }
            //ЕСЛИ ПРЯМОЙ ВВОД
        } else if (answerType == "DirectInput") {
            String answer = "{" +
                    "\"NUMBER\":" + 1 + "," +
                    "\"TEXT\":null," +
                    "\"VALUE\":\"<p>" + answers[0] + "</p>\\n\"" +
                    "}";

            ansewrsFmt = ansewrsFmt + answer;

        }else  if (answerType == "Sort"){
            for (int i = 0; i < nAn; i++) {
                int n = i + 1;
                String answer = "{" +
                        "\"NUMBER\":" + n + "," +
                        "\"TEXT\":null," +
                        "\"VALUE\":\"<p>" + answers[i] + "</p>\\n\"" +
                        "}";

                if (i == (nAn - 1)) ansewrsFmt = ansewrsFmt + answer;
                else ansewrsFmt = ansewrsFmt + answer + ",";
            }
        }else if (answerType == "Compiles"){
            for (int j = 0; j < nAn; j++) {
                int n = i + 1;
                String answer = "{" +
                        "\"NUMBER\":" + n + "," +
                        "\"TEXT\":\"" + answers[i] + "\"," +
                        "\"VALUE\":\"<p>" + answers[i + 5] + "</p>\\n\"" +
                        "}";

                if (i == (nAn - 1)) ansewrsFmt = ansewrsFmt + answer;
                else ansewrsFmt = ansewrsFmt + answer + ",";
            }
        }

        String Result = "{\"DELETEDATE\":null," +
                "\"NUMBER\":" + i + "," +
                "\"COMMENT\":\"\"," +
                "\"ANSWERTYPE\":\"" + answerType + "\"," +
                "\"TEXT\":\"<p>" + qText + "</p>\\n\"," +
                "\"NAME\":\"Вопрос " + i + "\"," +
                "\"DIFFICULTID\":6," +
                "\"ISREGISTRSENSITIVENESS\":" + isRegistrSenseStr + "," +
                "\"ISSPACESENSITIVENESS\":" + isSpaceSenseStr + "," +
                "\"ISDELETED\":false," +
                "\"ISREGEXPR\":false," +
                "\"ANSWERS\":[" +
                ansewrsFmt +
                "]}";


        return Result;
    }
    /*public JSONObject getQuestionJSON(){

    }*/
}
/*
{
        "DELETEDATE": null,
        "NUMBER": 1,
        "COMMENT": "",
        "ANSWERTYPE": "One",
        "TEXT": "<p>Назовите элементы с постоянной валентонстью 1</p>\n",
        "NAME": "Назовите элементы с постоянной валентонстью 1",
        "DIFFICULTID": 6,
        "ISREGISTRSENSITIVENESS": false,
        "ISSPACESENSITIVENESS": false,
        "ISDELETED": false,
        "ISREGEXPR": false,
        "ANSWERS": [
        {
        "NUMBER": 1,
        "TEXT": "<p>S</p>\n",
        "VALUE": "False"
        },
        {
        "NUMBER": 2,
        "TEXT": "<p>Na</p>\n",
        "VALUE": "True"
        },
        {
        "NUMBER": 3,
        "TEXT": null,
        "VALUE": "False"
        },
        {
        "NUMBER": 4,
        "TEXT": null,
        "VALUE": "False"
        }
        ]
        }*/
