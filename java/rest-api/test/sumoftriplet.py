class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        res =  []
        nums.sort()
        print('input',nums)
        n=len(nums)
        for i in range(0,n-1) :
            num = nums[i]
            si=0
            ei=n-1
            while si < n and ei > -1 :
                if i == si or si ==ei:
                    si+=1
                    continue
                if i== ei :
                    ei -=1
                    continue
                
                start = nums[si]
                end = nums[ei]
                sum = start + end + num
                print(start ,num, end  )
                if sum == 0 :
                    r =[start,end,num]
                    r.sort()
                    #print('triplet ',r);
                    found = False
                    
                    for item in res :
                        if item[0] == r[0] and item[1] == r[1]  and item[2] == r[2] :
                            #print('duplicate triplets', r)
                            found =True
                            break
                    
                    if not found :
                        #print('new triplet', r)
                        res.append( r)
                        #print('list triplets', res)
                    break
                    
                elif sum < 0 :
                    si +=1
                elif sum > 0 :
                    ei -=1
        
        return res
    
