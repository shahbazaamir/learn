package setup;

import copypaste.Paste;
import org.junit.Test;
import programs.Notepad;

public class NotepadPasteText {
    @Test
    public void moveMouse() {

        try{

            Notepad mm=new Notepad();
            mm.open();
            Paste.paste("Hello");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
