from algo.array.rotate import rotate_right,rotate_left , reverse ,sort_with_rotate_reverse

arr= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
arr2= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
arr3= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
arr4= [1,  3, 5,  7, 8, 9, 10,2, 4,6,]  
k =3 
reverse(arr,0,9)

reverse(arr,0,5)
reverse(arr,6,9)
print(arr)
'''
print('Array before', arr2)
rotate_right(arr2,4)
print('Array after', arr2)

print('Array before', arr3)
rotate_left(arr3,4)
print('Array after', arr3)

print('Array before', arr3)
rotate_left(arr3,4)
print('Array after', arr3)
'''
print('Array before', arr4)
arr5 = sort_with_rotate_reverse(arr4)
print('Array after', arr5)