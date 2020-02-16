package test;

import org.apache.maven.cli.MavenCli;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Test {

    public static void run( ) throws Exception {
        MavenCli cli = new MavenCli();
        String workingDir = "F:\\shahbaz\\study\\gitRepo\\new\\learn\\springboot\\database\\dbtest\\";
        String log = "F:\\shahbaz\\study\\gitRepo\\new\\learn\\springboot\\database\\dbtest\\output1.log";
        System.setProperty("maven.multiModuleProjectDirectory", workingDir);
        String [] clean = new String[]{ "clean", "install"};
        try(PrintStream fos=new PrintStream(log)) {
            cli.doMain(clean, workingDir, fos, fos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
