class Solution(object):
    def intToRoman1(self, num):
        """
        :type num: int
        :rtype: str
        """
        d = {1: 'I', 5: 'V', 10: 'X', 50: 'L', 100: 'C', 500: 'D', 1000: 'M'}
        counter = num
        roman =''
        keys =list( d.keys() )
        keys.reverse()
        prev = ''
        while counter != 0 :
            for k in keys :
                mul  = counter //  k
                if mul == 4 :
                    roman= roman + d[k] + prev
                else :
                    conv = d[k] * mul
                    roman = roman + conv
                if counter == 0 :
                    break
                if mul == 0 :
                    continue
                counter = counter % ( mul * k)
                prev = d[k]
        return roman
        
        
    def intToRoman(self, num):
        """
        :type num: int
        :rtype: str
        """
        d = {1: 'I', 5: 'V', 10: 'X', 50: 'L', 100: 'C', 500: 'D', 1000: 'M'}
        
        muls = [1 , 5 , 10 , 50 , 100 , 500 , 1000 ]
        #d={1:'I' , 5:'V' , 10:'X'}
        mynum = num
        x=6
        roman = ''
        divisor = 1000
        while divisor != 0 :
            mynum = num //divisor
            mynum = mynum * divisor
            while x >= -1 and mynum != 0:
                mul = mynum //muls [x]
                roman += d[muls [x]] * mul
                mynum = mynum % muls [x]
                if mynum == 9  :
                    roman += (d[muls[x-2]] + d[muls[x]])
                    x=x-2
                    break
                elif mynum == 4 :
                    roman += (d[muls[x-2]] + d[muls[x-1]])
                    x=x-2
                    break
                x=x-1
            divisor = divisor //10
        return roman
        
        
            
