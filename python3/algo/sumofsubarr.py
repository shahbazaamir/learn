def subarraySum(nums, k):
    prefix_sum = 0
    count = 0
    seen = {0: 1}  # prefix sum count hash table
    for num in nums: 
        prefix_sum += num 
        count += seen.get(prefix_sum - k, 0) 
        seen[prefix_sum] = seen.get(prefix_sum, 0) + 1 # add or update the count 
    print(seen)
    return count

# array  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
#