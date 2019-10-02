package diffpack;

import trade.MyClass;

public class MyClassDiffPack {
	
	public void myMethod() {
		MyClass myClass=new MyClass();
		System.out.println( myClass.publicAttr);
		//System.out.println( myClass.privateAttr); error
		//System.out.println( myClass.protectedAttr); error
		//System.out.println( myClass.defaultAttr); error
	}

}
