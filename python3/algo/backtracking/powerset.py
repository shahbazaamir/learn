def init_state(input_data):
    

def solve(input_data):
    result = []

    def backtrack(state, choices):
        # ✅ Base case: solution found
        if is_solution(state):
            result.append(build_result(state))
            return
        
        # 🔁 Try all possible choices
        for choice in get_choices(state, choices):
            
            # ❌ Skip invalid choices (pruning)
            if not is_valid(state, choice):
                continue
            
            # ✅ Choose
            apply_choice(state, choice)
            
            # 🔽 Recurse
            backtrack(state, update_choices(choices, choice))
            
            # 🔙 Undo (backtrack)
            undo_choice(state, choice)

    # Initial call
    initial_state = init_state(input_data)
    initial_choices = init_choices(input_data)
    
    backtrack(initial_state, initial_choices)
    return result