from nltk.book import *
len(set(text1))
#19317
lc_words = [ word.lower() for word in text1] 
len(set(lc_words))

from nltk import PorterStemmer
porter = PorterStemmer()
p_stem_words = [porter.stem(word) for word in set(lc_words) ]
len(set(p_stem_words))

from nltk import LancasterStemmer
lancaster = LancasterStemmer()
l_stem_words = [lancaster.stem(word) for word in set(lc_words) ]
len(set(l_stem_words))

wnl = nltk.WordNetLemmatizer()
wnl_stem_words = [wnl.lemmatize(word) for word in set(lc_words) ]
len(set(wnl_stem_words))
#15168 
