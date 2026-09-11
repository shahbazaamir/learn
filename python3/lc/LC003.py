""" 
Question : Find longest substring without repeating characters
Input : abcabcbb
Output : 3 
Strategy :
ht = { letter : index }
if letter in ht :
    ht[letter] = curr_index
    starting_index = prev_index +1 
    max_substring = max( max_substring , (prev_index - curr_index  ) )


"""

class Solution:
    
    def __init__(self):
        self.log = True

    def lengthOfLongestSubstring(self, s: str) -> int:
        ht = {}
        curr_index = 0
        starting_index = 0
        max_substring =0
        for letter in s :
            if letter in ht :
                prev_index = ht[letter]
                starting_index = max(starting_index , prev_index +1 )
            ht[letter] = curr_index
            max_substring = max( max_substring , (curr_index - starting_index  ) +1 )
            if self.log : print(f'starting index : {starting_index} \nending index :{curr_index} \nlength of subs :{max_substring} ')
            curr_index += 1
        return max_substring