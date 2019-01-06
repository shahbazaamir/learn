import nltk
text = "Welcome to another program using nltk . In this program we will find out the most common word in a the paragraph . We will use frequency distribution ."
words = nltk.word_tokenize(text)
wordfreq = nltk.FreqDist(words)
print(wordfreq.most_common(2))
