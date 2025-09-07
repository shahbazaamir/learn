# arr [1,2,3,4,5] k : 3
# 1.   reverse all         [5,4,3,2,1]
# 2.   reverse k           [3,4,5,2,1]
# 3.   reverse remaining   [3,4,5,1,2]
def rotatearray(arr,k) :
    
    n=len(arr)
    k=k % n
    reverse(arr,0, n-1)
    reverse(arr,0,k-1 )
    reverse(arr,k,n-1)

def reverse(arr,start,end):
    while start < end :
        arr[start] , arr[end] = arr[end] , arr[start]
        start +=1
        end -=1
