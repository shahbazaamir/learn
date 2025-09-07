cars = ["Maruti", "Tata", "Mahindra","Suzuki"]

print(cars)

#retrieve the second element
print(cars[1])

#modify the second element
cars[1]="Force"
print(cars[1])

#get number of elements
print("length :",len(cars))


#print each elements
for car in cars:
  print(car)

#  
print(cars[:3])
print(cars[1:3])
