def subsets(nums):
    result = []

    def backtrack(start, path):
        result.append(path[:])
        for i in range(start, len(nums)):
            path.append(nums[i])
            backtrack(i + 1, path)
            path.pop()

    backtrack(0, [])
    return result

# print(subsets([1, 2, 3]))
# Output: All subsets of [1,2,3]


def solve_n_queens(n):
    results = []

    def is_valid(board, row, col):
        for r in range(row):
            if board[r] == col or \
               abs(board[r] - col) == abs(r - row):
                return False
        return True

    def backtrack(row, board):
        if row == n:
            results.append(board[:])
            return
        for col in range(n):
            if is_valid(board, row, col):
                board[row] = col
                backtrack(row + 1, board)
                board[row] = -1

    backtrack(0, [-1] * n)
    return results

# print(solve_n_queens(4))

def maze_solver(maze, x=0, y=0, path=[]):
    if x == len(maze) - 1 and y == len(maze[0]) - 1:
        return [path + [(x, y)]]

    if x >= len(maze) or y >= len(maze[0]) or maze[x][y] == 0:
        return []

    maze[x][y] = 0  # Mark as visited
    moves = [(1, 0), (0, 1)]  # Down, Right
    result = []
    for dx, dy in moves:
        result += maze_solver(maze, x + dx, y + dy, path + [(x, y)])
    maze[x][y] = 1  # Backtrack
    return result
"""
maze = [
    [1, 1, 1],
    [1, 0, 1],
    [1, 1, 1]
]
print(maze_solver(maze))
"""

