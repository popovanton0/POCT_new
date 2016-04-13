package sample;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import sample.popov.PopovUtilites.PopovUtilites;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by popov on 22.02.2016.
 */
public class Question {

    String qText;
    boolean isRegistrSense;
    boolean isSpaceSense;
    Integer nAn;
    String[] answers;
    String[] checkBoxes;
    int i;
    String answerType;


    public String getFormatedResult(Logger log) {


        for (int j = 0; j < answers.length; j++) {
            try {
                answers[j] = HTMLEntities.htmlentities(answers[j]);
                answers[j] = HTMLEntities.htmlDoubleQuotes(answers[j]);
                answers[j] = HTMLEntities.htmlSingleQuotes(answers[j]);
                answers[j] = answers[j].replace("amp;", "").replace("\\n", "");
            } catch (Exception e) {
                log.warning(e.getLocalizedMessage());
            }
        }

        log.info("На getFormatedResult: " + answerType);
        String Result = null;
        try {
            String isRegistrSenseStr;
            String isSpaceSenseStr;
            String ansewrsFmt = "";
            log.info("AnswerType from creating string: " + answerType);

            if (isRegistrSense) isRegistrSenseStr = "true";
            else isRegistrSenseStr = "false";
            if (isSpaceSense) isSpaceSenseStr = "true";
            else isSpaceSenseStr = "false";

            if (answerType.equals("One") || answerType.equals("Many")) {
                // FIXME: 01.04.2016 NPE при answerType = Many, full_5
                int count = (int) Arrays.stream(checkBoxes)
                        .filter(s -> PopovUtilites.stringToBoolean(s))
                        .count();

                if (count > 1)answerType = "Many";
            }
            if (answerType.equals("One")) log.info("Must start \"ЕСЛИ ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА\"");
            // ЕСЛИ ОДИН ИЛИ НЕСКОЛЬКО ВАРИАНТОВ ОТВЕТА
            if (answerType.equals("One") || answerType.equals("Many")) {

                for (int i = 0; i < nAn; i++) {
                    log.info("starting create answer string, iteration " + i);
                    int n = i + 1;
                    String answer = "{" +
                            "\"NUMBER\":" + n + "," +
                            "\"TEXT\":\"<p>" + answers[i] + "</p>\\n\"," +
                            "\"VALUE\":\"" + checkBoxes[i] + "\"" +
                            "}";

                    if (i == (nAn - 1)) ansewrsFmt = ansewrsFmt + answer;
                    else ansewrsFmt = ansewrsFmt + answer + ",";
                }
                //ЕСЛИ ПРЯМОЙ ВВОД
            } else if (answerType.equals("DirectInput")) {
                log.info("starting create answer string, iteration " + i);
                String answer = "{" +
                        "\"NUMBER\":" + 1 + "," +
                        "\"TEXT\":null," +
                        "\"VALUE\":\"<p>" + answers[0] + "</p>\\n\"" +
                        "}";

                ansewrsFmt = ansewrsFmt + answer;

            }else  if (answerType.equals("Sort")){
                log.info("starting create answer string, iteration " + i);
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
            }else if (answerType.equals("Compiles")){
                log.info("starting create answer string, iteration " + i);
                for (int j = 0; j < nAn; j++) {
                    int n = j + 1;
                    String answer = "{" +
                            "\"NUMBER\":" + n + "," +
                            "\"TEXT\":\"" + answers[j] + "\"," +
                            "\"VALUE\":\"<p>" + checkBoxes[j] + "</p>\\n\"" +
                            "}";

                    if (j == (nAn - 1)) ansewrsFmt = ansewrsFmt + answer;
                    else ansewrsFmt = ansewrsFmt + answer + ",";
                }
            }
            Result = "{\"DELETEDATE\":null," +
                    "\"NUMBER\":" + i + "," +
                    "\"COMMENT\":\"\"," +
                    "\"ANSWERTYPE\":\"" + answerType + "\"," +
                    "\"TEXT\":\"<p>" + qText.replaceAll("\"","\\\\\"") + "</p>\\n\"," +
                    "\"NAME\":\"Вопрос " + i + "\"," +
                    "\"DIFFICULTID\":6," +
                    "\"ISREGISTRSENSITIVENESS\":" + isRegistrSenseStr + "," +
                    "\"ISSPACESENSITIVENESS\":" + isSpaceSenseStr + "," +
                    "\"ISDELETED\":false," +
                    "\"ISREGEXPR\":false," +
                    "\"ANSWERS\":[" +
                    ansewrsFmt +
                    "]}";
        } catch (Exception e) {
            log.warning(Main.getStackTrace(e));
        }


        return Result;
    }
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
