import nltk
s= 'Python is an awesome language.'
tokens = nltk.word_tokenize(s)
someobj=list(nltk.bigrams(tokens))
print(someobj)