import networkx as nx
import matplotlib.pyplot as plt

# 1. Create a directed graph object
G = nx.DiGraph()

# 2. Add nodes (optional, adding edges will automatically add nodes)
G.add_nodes_from(["A", "B", "C", "D"])

# 3. Add directed edges
G.add_edges_from([("A", "B"), ("B", "C"), ("C", "A"), ("A", "D")])

# 4. Define the layout for the nodes
# This helps in positioning the nodes for better visualization.
# Different layouts exist, e.g., spring_layout, circular_layout, shell_layout.
pos = nx.spring_layout(G) 

# 5. Draw the graph
# arrows=True is crucial for directed graphs to show the direction of edges.
nx.draw_networkx(G, pos, with_labels=True, arrows=True, node_color='skyblue', node_size=1000, font_size=10)

# 6. Display the plot
plt.title("Simple Directed Graph")
plt.axis('off') # Turn off the axis
plt.show()