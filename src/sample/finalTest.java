package sample;

/**
 * Created by popov on 23.02.2016.
 */
public class finalTest {
    String questionsFmt;
    String testName;
    Integer subjectId;

    public String getFormatedTest() {
        String result = "{" +
                "\"INFO\":\"\"," +
                "\"NAME\":\"" + testName + "\"," +
                "\"ISDELETED\":false," +
                "\"DELETEDATE\":null," +
                "\"TESTGROUPID\":null," +
                "\"SUBJECTID\":" + subjectId + "," +
                "\"THEMES\":[{\"NUMBER\":1,\"NAME\":\"" + testName + "\",\"INFO\":\"\",\"ISDELETED\":false," +
                "\"DELETEDATE\":null,\"QUESTIONS\":[" + questionsFmt + "]}]}";

        return result;
    }

}

