import re

s = 'Python is cool!!!'
print(re.findall(r'\s\w+\b', s))