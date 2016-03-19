package sample;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

//Не относится к проекту
public class rdg {

    public static void main(String[] args) {
        String progName = "notepad.exe";
        try {
            Robot robot = new Robot();


            System.out.println("Timer Started");
            for (int i = 1; i < 2; i++) {
                System.out.println(i);
                robot.delay(1000);
            }

            // Копирование в буффер
            toClipboard(progName);

            openExecuteWindow(robot);

            robot.delay(50);

            // Вставка из буффера
            fromClipboard(robot);

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(100);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }


    public static void toClipboard(String str) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(str);
        clipboard.setContents(stringSelection, new ClipboardOwner() {
            @Override
            public void lostOwnership(Clipboard clipboard, Transferable contents) {

            }
        });
    }

    public static void fromClipboard(Robot robot) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
    }
    public static void openExecuteWindow(Robot robot){
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_R);
    }

    public static String getClassLocation() {
        String classLocation = System.getProperty("user.home");
        return classLocation;
    }
}
