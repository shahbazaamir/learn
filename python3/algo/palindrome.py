def ispalin(w) :
    j = 0
    l = len(w)
    while j < l//2 :
        if not w[j] == w[(l-1) - j ] :
            return False
        j+=1
    return True