## This file contains different types of loops in shell scripting

### For Loop
for i in 1 2 3 4 5
do
  echo "Number: $i"
done

### While Loop
count=1
while [ $count -le 5 ]
do
  echo "Count: $count"
  count=$((count + 1))
-done

### Until Loop
count=10
until [ $count -le 0 ]
do
  echo "Count: $count"
  count=$((count - 1))
-done

### Loop with break
for i in {1..10}
do
  if [ $i -eq 5 ]; then
    break
  fi
  echo "Number: $i"
-done

### Loop with continue
for i in {1..10}
do
  if [ $i -eq 5 ]; then
    continue
  fi
  echo "Number: $i"
-done