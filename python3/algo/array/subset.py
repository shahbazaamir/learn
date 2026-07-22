
def powerset(arr) :
    res = [] 
    for i in range (0 , 1 << len(arr)) :
        subset = []
        for j in range(len(arr)) :
            if ( i & (1 <<  j)) > 0 :
                subset.append(  arr[j])
        res.append(subset)
    return res 
#arr=[1,2,3]
#print(powerset(arr))


def subset(arr):
    res = []
    n = len(arr)
    
    # 1 << n  means 2^n
    for i in range(1, 1 << n):        # start from 1 to exclude empty set
        subset = []
        for j in range(n):            # corrected: start from 0
            if (i & (1 << j)) > 0:    # if j-th bit is set
                subset.append(arr[j]) # corrected: take element, not index
        res.append(subset)
    
    return res

def powerset_backtrack(nums):
    result = []

    def backtrack(start, path):
        # Add current subset
        result.append(path[:])

        # Explore further elements
        for i in range(start, len(nums)):
            path.append(nums[i])          # choose
            backtrack(i + 1, path)        # explore
            path.pop()                    # un-choose (backtrack)

    backtrack(0, [])
    return result


def subset_backtrack(nums):
    result = []

    def backtrack(start, path):
        # Add current subset
        result.append(path[:])

        # Explore further elements
        for i in range(start, len(nums)):
            path.append(nums[i])          # choose
            backtrack(i + 2, path)        # explore
            path.pop()                    # un-choose (backtrack)

    backtrack(0, [])
    return result

# Example
print(subset_backtrack([1, 2, 3]))

#arr = [1, 2, 3]
#print(subset(arr))
