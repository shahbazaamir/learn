from algo.palindrome import ispalin


class Solution(object):
    def longestPalindrome(self, s):
        """
        :type s: str
        :rtype: str
        """
        def ispalin(w) :
            j = 0
            l = len(w)
            while j < l//2 :
                if not w[j] == w[(l-1) - j ] :
                    return False
                j +=1
            return True
        n =len(s)
        p1 = 0
        p2 = n -1 
        while n> p1 > -1 : 
            while n > p2 > p1 :
                if s[p1] == s[p2] :
                    o = s[p1: (p2+1) ]
                    if ispalin (o) :
                        return o
                p2 -=1
            p1 +=1
        return ""

            

w = "abc"
print(ispalin(w))

w = "abcba"
print(ispalin(w))