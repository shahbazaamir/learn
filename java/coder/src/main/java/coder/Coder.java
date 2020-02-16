package coder;

import com.helger.jcodemodel.*;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.File;
import java.io.IOException;

public class Coder {

    public static JCodeModel cm = new JCodeModel();

    public void  createByteFile () throws IOException, CannotCompileException, NotFoundException {
         ClassPool pool = ClassPool.getDefault();
         CtClass cc = pool.makeClass("Point");

     }

     public static void main (String [] ares )throws Exception {
         Coder Coder = new Coder();
         Coder.createFile();
     }

    public void  createFile () throws IOException, JClassAlreadyExistsException {
        JCodeModel cm = new JCodeModel();
        JDefinedClass dc = cm._class("foo.Bar");
        JMethod m = dc.method(0, int.class, "foo");
        m.body()._return(JExpr.lit(5));

        File file = new File("./target/classes");
        file.mkdirs();
        cm.build(file);
    }


    public static JDefinedClass createClass() throws JClassAlreadyExistsException {
        return cm._class("foo.Bar");
    }
    public static void createMethod(JDefinedClass dc){
        JMethod m = dc.method(0, int.class, "foo");
        m.body()._return(JExpr.lit(5));
    }

}