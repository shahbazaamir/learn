import nltk
from nltk.corpus import genesis

eng_tokens = genesis.words('english-kjv.txt')
eng_bigrams = nltk.bigrams(eng_tokens)
filtered_bigrams = [ (w1, w2) for w1, w2 in eng_bigrams if len(w1) >=8 and len(w2) >= 8 ]
print(filtered_bigrams)