import nltk
text = "Hello world . Welcome to nltk . In this program we will count the number of sentences and words"
sentences = nltk.sent_tokenize(text)
print("number of sentenses")
print(len(sentences))
words = nltk.word_tokenize(text)
print("number of words")
print(len(words))

wordfreq = nltk.FreqDist(words)
wordfreq.most_common(1)
