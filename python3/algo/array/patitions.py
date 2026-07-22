def partitions(arr) :
    if not arr :
        yield []
        return 
    n= len(arr)
    for i in range(1,n+1) :
#        print('coming here')
        first = arr[:i]
        remaining = arr[i:]

        for p in partitions(remaining) :
#            print('coming here 2')
            yield [first] +p
        

l = list(partitions([1,2,3]))
for i in l :
    print(*i)