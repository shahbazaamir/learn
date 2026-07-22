def coin_change(amt) :
    denominations = [1,2,5]
    dp = [float('inf')] * (amt+1)
    dp[0] = 0
    for i in range(1,amt+1) :
        for d in denominations :
            if i - d >= 0 :
                dp[i] = min( dp[i] , (1 + dp[i-d] ) )
    print('DP array :')
    print(dp)
    if dp[amt] != float('inf') :
        return dp[amt]
    return -1

print(coin_change(6))