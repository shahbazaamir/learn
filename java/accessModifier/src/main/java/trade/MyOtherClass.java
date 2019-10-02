package trade;

public class MyOtherClass {
	
	public void myMethod() {
		MyClass myClass=new MyClass();
		System.out.println( myClass.publicAttr);
		//System.out.println( myClass.privateAttr); error
		System.out.println( myClass.protectedAttr);
		System.out.println( myClass.defaultAttr);
	}
}
