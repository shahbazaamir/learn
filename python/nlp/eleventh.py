import nltk
from nltk.book import text6


eng_bigrams = nltk.bigrams(text6)

filtered_bigrams = [ (w1, w2) for w1, w2 in eng_bigrams if w1 =="King" and w2=="Arthur" ]
print(len(filtered_bigrams))