import nltk
text = 'Python is awesome.'
words = nltk.word_tokenize(text)
default_tagger = nltk.DefaultTagger('NN')
default_tagger.tag(words)