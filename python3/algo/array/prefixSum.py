def prefixSum(arr):
    n = len(arr)
    r = [0] * n 
    r[0] = arr[0]
    for i in range(1,n) :
        r[i] = r[i-1] +arr[i]
    return r

print(prefixSum([1,2,3,4]))
