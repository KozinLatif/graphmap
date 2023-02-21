# Simple weighted undirected graph to represent a network

The program allows the user to open a map (a background image), define locations on the map with mouse clicks, create connections between these locations, and then find a path between two user-selected locations. 

Includes classes ListGraph and Edge for the representation of simple weighted undirected graphs implemented by adjacency lists. Written generically so that they can be used in other applications as well.

<img width="1137" alt="Screenshot 2023-02-21 at 20 03 21" src="https://user-images.githubusercontent.com/29358769/220435806-d74a922e-dd1d-4877-95f0-54c507664020.png">

# Implementations

• <b>Add</b>: receives a node and adds it to the graph. If the node already exists in the graph, there will be no change.

• <b>Remove</b>: receives a node and removes it from the graph. If the node is not in the graph, throws NoSuchElementExceptio. When a node is removed, its edges should also be removed, and since the graph is undirected, the edges should also be removed from the other nodes.

• <b>Connect</b>: Takes two nodes, a string (the name of the connection), and an integer (the weight of the connection), and connects these nodes with edges with this name and weight. If either node is missing from the graph, throws NoSuchElementException. If the weight is negative, throws IllegalArgumentException. If an edge already exists between these two nodes, throws IllegalStateException (there should be at most one connection between two nodes).

• <b>Disconnect</b>: Takes two nodes and removes the edge that connects these nodes. If either node is missing in the graph, throws NoSuchElementException. If the edge does not exist between these two nodes, throws IllegalStateException.

• <b>setConnectionWeight</b>: Takes two nodes and an integer (the new weight of the connection) and sets this weight as the new weight of the connection between these two nodes. If either of the nodes is missing in the graph or no edge exists between these two nodes, throws NoSuchElementException. If the weight is negative, throws IllegalArgumentException.

• <b>getNodes</b>: Returns a copy of the set of all nodes.

• <b>getEdgesFrom</b>: Takes a node and returns a copy of the collection of all edges leading from this node. If the node is missing in the graph, throws NoSuchElementException.

• <b>getEdgeBetween</b>: Takes two nodes and returns the edge between these nodes. If either of the nodes is missing in the graph, throws NoSuchElementException. If there is no edge between the nodes, null is returned.

• <b>pathExists</b>: Takes two nodes and returns true if there is a path through the graph from one node to the other (possibly over many other nodes), otherwise false is returned. If either of the nodes is not in the graph, false is also returned. Uses a helper method for depth-first search through a graph.

• <b>getPath</b>: Takes two nodes and returns a list of edges that represent a path between these nodes through the graph, or null if there is no path between these two nodes.
