def longest_common_substring(str1, str2):
    m, n = len(str1), len(str2)
    # Create a 2D DP table
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    max_length = 0
    end_pos = 0  # To track the end position of LCS in str1

    # Fill the DP table
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if str1[i - 1] == str2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
                if dp[i][j] > max_length:
                    max_length = dp[i][j]
                    end_pos = i
            else:
                dp[i][j] = 0  # Reset on mismatch

    # Extract the substring
    longest_substr = str1[end_pos - max_length:end_pos]
    return longest_substr, max_length


# Example usage
#s1 = "abcdef"
#s2 = "zcdemf"
#substr, length = #longest_common_substring(s1, s2)
#print("Longest Common Substring:", substr)
#print("Length:", length)


def coin_change(coins, amount):
    # Initialize the DP table with amount+1 (impossible high value)
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0  # Base case: 0 coins to make amount 0

    for a in range(1, amount + 1):
        for coin in coins:
            if coin <= a:
                dp[a] = min(dp[a], dp[a - coin] + 1)

    return dp[amount] if dp[amount] != amount + 1 else -1

# Example usage
coins = [1, 2, 5]
amount = 11
result = coin_change(coins, amount)
print("Minimum coins required:", result)


def climb_stairs(n):
    if n <= 2:
        return n

    a, b = 1, 2  # base cases: 1 way to reach step 1, 2 ways to reach step 2
    for i in range(3, n + 1):
        a, b = b, a + b  # update the number of ways dynamically

    return b

# Example usage
n = 5
print(f"Number of distinct ways to climb {n} steps:", climb_stairs(n))


