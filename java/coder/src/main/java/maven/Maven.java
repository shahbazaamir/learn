package maven;

import compile.Compile;
import org.apache.maven.cli.MavenCli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Maven {

    public static void run(){
        Logger logger = LoggerFactory.getLogger(Maven.class);
        logger.info("This is how you configure Java Logging with SLF4J");
        MavenCli cli = new MavenCli();
        System.setProperty("maven.multiModuleProjectDirectory", "F:\\shahbaz\\study\\gitRepo\\new\\learn\\springboot\\database\\");

        String [] proj=new String[]{"archetype:generate", "-DgroupId=db"
                , "-DartifactId=db"
                ,"-DarchetypeArtifactId=maven-archetype-quickstart"
                ,"-DinteractiveMode=false"
        };
        String [] clean = new String[]{"clean", "install"};
        cli.doMain(proj, "F:\\shahbaz\\study\\gitRepo\\new\\learn\\springboot\\database\\", System.out, System.out);

    }



    public static void  newProject(String grp,String arti){
        MavenCli cli = new MavenCli();
        System.setProperty("maven.multiModuleProjectDirectory",
                "F:\\shahbaz\\study\\gitRepo\\new\\learn\\springboot\\database\\");
        cli.doMain(new String[]{"archetype:generate", "-DgroupId="+grp
                , "-DartifactId="+arti
                ,"-DarchetypeArtifactId=maven-archetype-quickstart"
                        ,"-DinteractiveMode=false"
                },
                "F:\\shahbaz\\study\\gitRepo\\new\\learn\\springboot\\database\\", System.out, System.out);

        //mvn archetype:generate -DgroupId=YourProjectGroupId -DartifactId=YourProjectName -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false


    }

    public static void main (String [] ares )throws Exception {
        Maven Coder = new Maven();
       Coder.newProject("test4","test4");
        System.out.println( MavenCli.DEFAULT_USER_SETTINGS_FILE);
        System.out.println( MavenCli.userMavenConfigurationHome);
        run();
    }


    public static void mainx(String[] args)
    {
        try
        {
            // create a process and execute google-chrome
           // Process process = Runtime.getRuntime().exec("cmd \\c mvn clean install ");
           // System.out.println("Google Chrome successfully started");
            newProject("database","database");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
