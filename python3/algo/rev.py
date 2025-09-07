#
//  rev.py
//  spring-test
//
//  Created by Zainab Firdaus on 21/06/25.
//

class ListNode(object):
     def __init__(self, val=0, next=None):
         self.val = val
         self.next = next
def rev(arr,k):
    if k==0 or arr is None or len(arr) == 0 :
        return arr
    res = [ None] * len(arr)
    k = k % len(arr)
    for i in range(len(arr) ) :
        if i+k >= len(arr) :
            res[(i+k) - len(arr) ] = arr[i]
        else :
            res[i+k] = arr[i]
    return res


def arrtolinkedlist(arr  ) :
    if arr is None or len(arr) == 0 :
        return ListNode()
    res = ListNode(arr[0])
    resiter = res
    for e in arr[1:] :
        resiter.next =ListNode(e)
        resiter = resiter.next
    return res
    
def linkedlistToArr(ll : ListNode) :
