def permute(s, answer=""):
    if len(s) == 0:
        return [answer] 
    result=[]
    for i in range(len(s)) :
        ch = s[i]
        rest = s[:i] + s[i+1:]
        result.extend(permute(rest,answer+ch))
    return result

# Example
s = "ABC"
result = permute(s)
print(result)   # ['ABC', 'ACB', 'BAC', 'BCA', 'CAB', 'CBA']
