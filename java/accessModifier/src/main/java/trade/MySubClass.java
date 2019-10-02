package trade;

public class MySubClass extends MyClass {
	
	public void myMethod() {
		
		System.out.println(publicAttr);
//		System.out.println(privateAttr); error
		System.out.println(protectedAttr);
		System.out.println(defaultAttr);
	}
}
