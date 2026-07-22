
## Input s ="abc"
## "abc" , "acb" , "bac" , "bca" , "cab" , "cba" 
## 1 2 6 24 
import itertools

def letters(s) :
    l = len(s)
    res=[] 
    c=0 
    while c<l :
        print(s[c])
        c+=1
    return res

def combi(str1) :
    l = len(str1)
    res=[]
    res.append(str1 )
    print(res)
    s=list(str1)
    c=0 
    while c<(l-1) :
        s[c] , s[c+1] = s[c+1] , s[c]
        res .append( ''.join(s) )
        c+=1
    return res

def anagrams(s) : 
    res = []
    for p in itertools.permutations(s) :
        res.append(''.join(p))
    return res

def powerset(s) :
    res = []
    for i in range(1<<len(s)) :
        subset = ''
        for j in range(len(s)) :
            if (i & (1 << j)) > 0 :
                subset += s[j]
        res.append(subset)
    return res

def powerPermutation(s) :
    res = []
    for i in range(1<<len(s)) :
        subset = ''
        for j in range(len(s)) :
            if (i & (1 << j)) > 0 :
                subset += s[j]
        res.append(subset)
    new_res = []
    for subset in res :
        if len(subset) > 1 :
            new_res+= anagrams(subset)
        else :
            new_res.append(subset)
    return new_res

#print(combi("abc"))
#anagrams("abc")
#print(powerset("abc"))
print(powerPermutation("abc"))