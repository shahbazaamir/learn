def max_sum_subarray(arr, k):
    n = len(arr)
    window_sum = sum(arr[:k])
    max_sum = window_sum

    for i in range(k, n):
        window_sum += arr[i] - arr[i - k]
        max_sum = max(max_sum, window_sum)

    return max_sum

# Example
# arr = [2, 1, 5, 1, 3, 2]
# k = 3
# print(max_sum_subarray(arr, k))  
# Output: 9 (5+1+3)


def longest_unique_substring(s):
    char_set = set()
    left = 0
    max_len = 0

    for right in range(len(s)):
        while s[right] in char_set:
            char_set.remove(s[left])
            left += 1
        char_set.add(s[right])
        max_len = max(max_len, right - left + 1)

    return max_len

# Example
# s = "abcabcbb"
# print(longest_unique_substring(s))  
# Output: 3 ("abc")


def count_subarrays_with_sum_lte_k(arr, k):
    left = 0
    total = 0
    count = 0

    for right in range(len(arr)):
        total += arr[right]
        while total > k:
            total -= arr[left]
            left += 1
        count += right - left + 1

    return count

# Example
# arr = [1, 2, 3]
# k = 4
# print(count_subarrays_with_sum_lte_k(arr, k)) 
# Output: 4


