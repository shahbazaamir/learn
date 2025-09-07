#
d={"I": 1 ,
"V":             5,
"X" :            10,
"L" :            50,
"C" :            100,
"D" :            500,
"M" :            1000
}
reversed_dict = {v: k for k, v in d.items()}
k1 = d.keys()
        keys = k1.reverse()
        counter = num
        mul =-1
        roman =''
        while counter != 0 :
            for num in keys :
                mul = counter //  num
                counter = counter % num
                if mul == 4 :
                    
                else :
                    roman = roman + (mul * d[num])
