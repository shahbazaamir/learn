package string;

import java.awt.*;
import java.awt.event.KeyEvent;


public class TypeCommand {

    public void type( ) {

        try{
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_H);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
