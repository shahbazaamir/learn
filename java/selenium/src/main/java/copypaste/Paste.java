package copypaste;

public class Paste {
    public static void paste(String data)throws Exception{
        SystemClipboard.copy(data);
        SystemClipboard.paste();
    }
}
