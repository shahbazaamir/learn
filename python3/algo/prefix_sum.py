def prefix_sum(arr):
    prefix = [0] * len(arr)
    prefix[0] = arr[0]
    for i in range(1, len(arr)):
        prefix[i] = prefix[i - 1] + arr[i]
    return prefix

# Example
# arr = [2, 4, 6, 8]
# print(prefix_sum(arr)) 
# Output: [2, 6, 12, 20]
