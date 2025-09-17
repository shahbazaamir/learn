# arr [1,2,3,4,5] k : 3
# 1.   reverse all         [5,4,3,2,1]
# 2.   reverse k           [3,4,5,2,1]
# 3.   reverse remaining   [3,4,5,1,2]
def rotate_right(arr,k) :
    n=len(arr)
    k=k % n
    reverse(arr,0, n-1)
    reverse(arr,0,k-1 )
    reverse(arr,k,n-1)

def rotate_left(arr,k) :
    n=len(arr)
    k=k%n 
    reverse(arr,0,k-1)
    reverse(arr,k,n-1)
    reverse(arr,0,n-1)

def reverse(arr,start,end):
    while start < end :
        arr[start] , arr[end] = arr[end] , arr[start]
        start +=1
        end -=1
###
# using rotate left
# and reverse 
def sort_n(arr) :
    n=len(arr)
    def min_idx(arr,i,n):
        for i in range(n) :
            print(i)
    #target = [i for in range(1,n)]
    for i in range(n) :
        print(i)

def reverse1(arr):
    """Reverse the entire array"""
    arr.reverse()

def left_rotate(arr, k):
    """Rotate array arr left by k using slicing"""
    n = len(arr)
    k %= n
    arr[:] = arr[k:] + arr[:k]

def sort_with_rotate_reverse(arr):
    target = sorted(arr)
    n = len(arr)
    
    # Safety limit: n*2 operations (not guaranteed optimal, but works)
    for _ in range(n * 2):
        if arr == target:
            return arr
        
        # If not sorted, check whether reversing helps
        if arr[0] > arr[-1]:
            reverse1(arr)
        else:
            left_rotate(arr, 1)  # rotate by 1 step
    
    return arr
 #+1,-2,3
 
         
