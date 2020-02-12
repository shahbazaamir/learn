package setup;

import move.mouse.MoveMouse;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import programs.Notepad;

public class TestNotepad {

    @Test
    public void moveMouse() {

        try{

            Notepad mm=new Notepad();
            mm.open();
          //  Thread.sleep(10*1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
