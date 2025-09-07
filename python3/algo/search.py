
def binary1( arr , target ) :
	n = len(arr )
	n = n//2
	while n > 0 and n < len(arr):
		print( ' current pointer : {} , current element : {} '.format(  n , arr[n])  ) 
		if arr[n] == target : 
			return n
		if arr[n] > target :
			n = n//2
		if arr[n] < target :
			n+= (n//2)
	return None	

def binary( arr , target ) :
	n = len(arr )
	start =0
	end=n-1
	while start >= 0  and end < n  and start <= end:
		mid = (start + end) //2 
		print( ' current pointer : {} , current element : {} , start :{} , end : {} '.format( mid  , arr[mid] , start , end )  ) 
		if arr[mid] == target : 
			return mid
		if arr[mid] > target :
			end = mid -1
		if arr[mid] < target :
			start = mid+1
	return None	

def log( **args ) :
	print( args )
