


  
@FunctionalInterface 
interface Polygon
{ 
    int perimeter(int x,int y,int w,int z); 
} 
  /* Testing the functional interface */
class First
{ 
    public static void main(String args[]) 
    { 
        int a = 5; 
  int b = 5; 
      int c = 5; 
      int d = 5; 
        // lambda expression to define the perimeter method
        Polygon p=(int x,int y,int w,int z)->w+ x+y+z;
  
        // parameter passed and return type must be 
        // same as defined in the prototype 
        int ans = p.perimeter(a,b,c,d); 
        System.out.println(ans); 
    } 
}
