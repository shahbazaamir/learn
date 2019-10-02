package diffpack;

import trade.MyClass;

public class MySubClassDiffPack extends MyClass{
	public void myMethod() {

		System.out.println(publicAttr);
		//		System.out.println(privateAttr); error
		System.out.println(protectedAttr);
		//System.out.println(defaultAttr); error
	}
}
