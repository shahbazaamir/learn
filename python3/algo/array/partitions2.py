def partitions(arr) :
    print(arr)
    n = len(arr)
    if not arr :
        yield []
        return 

    for i in range(1,n+1) :
        first = arr[:i]
        remaining = arr[i:]

        for p in partitions(remaining) :
            yield [first] + p 
    
l = partitions( [3,5,7]) 
print(*l)