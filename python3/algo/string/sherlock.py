import itertools

def anag(s) :
    res = [] 
    for p in itertools.permutations(s) :
        res.append("".join(p))
    return res

def pows(s):
    res = []
    for i in range(1, 1<< len(s) ) : 
        ps = ""
        for j in range(len(s)) :
            if (i & (1 << j)) > 0 :
                ps += s[j]
        res.append(ps)
    return res

#print(anag("abc"))
print(pows("mom"))
pss ={}
pss.