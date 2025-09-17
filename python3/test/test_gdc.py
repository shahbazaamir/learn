from collections import Counter
import  math  
def hasGroupsSizeX(  deck):
        """
        :type deck: List[int]
        :rtype: bool
        """
        n=len(deck)
        if n ==1 :
            return False
        cntr = Counter(deck)
        x =[]
        for e in cntr : 
            x.append(cntr[e])
        small = min(x)
        divi = math.gcd(*x)
        print (small,divi)
        y= small % divi
        if y == 0 : 
            return True
        else :
            return False

print(hasGroupsSizeX([1,2,3,4]))