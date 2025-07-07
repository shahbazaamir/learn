#


class Solution(object):
    def maxArea(self, height):
        """
        :type height: List[int]
        :rtype: int
        """
        maxarea = 0
        n = len(height)
        areas =  [[None for _ in range(n)] for _ in range(n)]
        maxpossiblearea =[None for _ in range(n)]
        for i in range (n) :
            for j in range(i,n):
                if i==j :
                    continue
                area=min(height[i],height[j]) * ( abs(i - j ) )
                maxarea = max( maxarea , area ) #area(height[i],height[j] , dist(i,j)))
                areas [i][j] = area
                
        return maxarea ,areas

if __name__=="__main__" :
    arr = [1,2,3,4,5]
    s= Solution()
    maxarea ,areas  =s.maxArea(arr)
    print(maxarea)
    for area in areas :
        print(*area)
