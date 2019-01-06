import nltk
from nltk.corpus import reuters 


#gasg=reuters.words(genre='gas')
fdist1 = nltk.ConditionalFreqDist(
(genre, word)
for genre in reuters.categories()
for word in reuters.words(categories=genre)
)
print(fdist1)
genres=['gas','sugar']
modals=['gas','gasoline','barrels','tonnes','year'  ]
someobj=fdist1.tabulate(conditions=genres, samples=modals)


#count=fdist1['gasoline']
print("conditional freq dist of gasoline,barrels ")
print(someobj)