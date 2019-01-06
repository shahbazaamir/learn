import nltk
from nltk.book import *
fdist1 = FreqDist(text1)
print(fdist1)
count=fdist1['test']
print("count of test")
print(count)