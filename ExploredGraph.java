import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Queue;
import java.util.function.Function;

/**
 * @author Alex Chiem
 * This class demonstrates the use of a problem-space graph to solve a popular puzzle
 * Towers of Hanoi. For more information on this puzzle, please visit https://en.wikipedia.org/wiki/Tower_of_Hanoi.
 * It uses Breadth First Search (BFS) and Depth First Search (DFS) to find a path from a starting vertex 
 * to the goal vertex. BFS will return shortest path and DFS will return any path it finds.
 * Each vertex represents a state of the problem and each edge represents the transition 
 * from one state to another.
 *
 *
 * The code I wrote are methods with "// TODO: I implemented this" before the method signature.
 * Extra Credit Options Implemented, if any:  (mention them here.)
 * 
 * Solution to Assignment 5 in CSE 373, Autumn 2014
 * University of Washington.
 * This assignment requires Java 8 JDK
 * 
 * 
 * Starter code provided by Steve Tanimoto and Si J. Liu, Nov. 21, 2014.
 * Starter code also named the class ExploredGraph
 */

public class ExploredGraph {
	public final int NUMBER_OF_PEGS = 3; // number of pegs in this game
	private Set<Vertex> Ve; // collection of explored vertices
	private Set<Edge> Ee; // collection of explored edges
	private int VeSize; // size of collection of explored vertices
	private int EeSize; // size of collection of explored edges
	private List<Operator> operators; // collection of operators (6 in this game)
	private HashMap<Vertex, LinkedList<Edge>> map; // map of successor vertex with its edges

	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		
		// Test the vertex constructor:
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		Vertex v1 = eg.new Vertex("[[],[],[4,3,2,1]]");


		//Test and Debugging code
	
		ArrayList<Vertex> path = eg.shortestPath(v0, v1);
		//eg.dfs(v0, v1);
		//ArrayList<Vertex> path = eg.retrievePath(v1);
		
		System.out.println(path);
		System.out.println(path.size());
		

	}

	public ExploredGraph() {
		initialize();
	}

	// TODO: I implemented this
	public void initialize() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		map = new HashMap<Vertex, LinkedList<Edge>>();
		VeSize = 0;
		EeSize = 0;
		setOperators();
	}

	//Initialize the field operators with 6 Operators:
	private void setOperators() {		
		// (i, j) = {(0, 1), (0, 2), (1, 0), (1, 2), (2, 0), (2, 1)}		
		operators = new ArrayList<Operator>();
		Operator op;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (i != j) {
					op = new Operator(i, j);							//Make a new operator
					operators.add(op);									//Add new operator into the list					
				}	
			}
		}		
	}

	// TODO: I implemented this
	//Return size of explored vertices set
	public int nvertices() {
		return VeSize;
	}

	// TODO: I implemented this
	//Return size of explored edges set
	public int nedges() { 
		return EeSize;
	}

	// TODO: I implemented this
	//Implement depth first search algorithm
	//Input: Takes the starting and goal vertices
	//Output: Creates a graph between starting and goal vertices
	public void dfs(Vertex vi, Vertex vj) {
		initialize();													//Reset all private fields
		Stack<Vertex> visitedStack = new Stack<Vertex>();				//Instantiate stack for visited Vertexes
		visitedStack.push(vi);											//Add starting vertex into visited stack
		Ve.add(vi);														//Add starting vertex into vertex explored set
		Vertex curVertex, temp, next;
		Edge curEdge;
		LinkedList<Edge> edgeList;
		while(!visitedStack.isEmpty()) {								//Check if visited stack is empty
			curVertex = visitedStack.pop();								//Remove top vertex in the stack to process					
			if (curVertex.equals(vj)) {									//Check if current vertex is equal to goal vertex
				Ve.add(vj);												//Add goal vertex to vertex explored set
				VeSize++;												//Increment explored vertex set size
				//System.out.println("done " + vString);
				break;
			} else {
				for (int i = 0; i < operators.size(); i++) {			//Iterate through all the operators					
					temp = new Vertex(curVertex.toString());			//Duplicate curVertex and set into temp
					Operator op = operators.get(i);						//Grab the ith operator
					if((Boolean) op.getPrecondition().apply(temp)) {	//Check if the operation can be performed on the successor vertex						
						next = (Vertex) op.getTransition().apply(temp); //Create the successor vertex						
						curEdge = new Edge(curVertex, next);			//Create an edge between the current vertex to the successor vertex
						Ee.add(curEdge);								//Add the new edge to the edge explored set						
						if (!Ve.contains(next)) {						//Check if the successor vertex is in the vertex explored set
							visitedStack.push(next);					//Push the successor vertex into the visited stack														
							Ve.add(next);								//Add the successor vertex into the vertex explored set
							VeSize++;									//Increment explored vertex set size
							edgeList = new LinkedList<Edge>(); 			//Instantiate a new linked list for the edges
							edgeList.add(curEdge);						//Add the edge into the linked list
							if (!map.containsValue(edgeList)) {			//Check if the hash map contains the linked list of edges
								map.put(next, edgeList);				//Put the linked list into the map
							}
						}
					}
				}
			}
		}
	}

	// TODO: I implemented this
	//Implement breadth first search algorithm
	//Input: Takes the starting and goal vertices
	//Output: Creates a graph between starting and goal vertices
	public void bfs(Vertex vi, Vertex vj) {
		initialize();													//Reset all private fields
		Queue<Vertex> visitedQueue = new LinkedList<Vertex>();			//Instantiate queue for visited Vertexes
		visitedQueue.add(vi);											//Add starting vertex into visited queue
		Ve.add(vi);														//Add starting vertex into vertex explored set
		Vertex curVertex, temp, next;
		Edge curEdge;
		LinkedList<Edge> edgeList;
		while (!visitedQueue.isEmpty()) {								//Check if visited queue is empty		
			curVertex = visitedQueue.poll();							//Remove front vertex in the queue to process			
			if (curVertex.equals(vj)) {									//Check if current vertex is equal to goal vertex
				Ve.add(vj);												//Add goal vertex to vertex explored set
				VeSize++;												//Increment explored vertex set size
				//Optimization opportunity
				break;
			} else {	
				for (int i = 0; i < operators.size(); i++) {			//Iterate through all the operators
					temp = new Vertex(curVertex.toString());			//Duplicate curVertex and set into temp		
					Operator op = operators.get(i);						//Grab the ith operator
					if((Boolean) op.getPrecondition().apply(temp)) {	//Check if the operation can be performed on the successor vertex
						next = (Vertex) op.getTransition().apply(temp); //Create the successor vertex
						curEdge = new Edge(curVertex, next);			//Create an edge between the current vertex to the successor vertex
						Ee.add(curEdge);								//Add the new edge to the edge explored set
						EeSize++;										//Increment explored edge set size
						if (!Ve.contains(next)) {						//Check if the successor vertex is in the vertex explored set
							visitedQueue.add(next);						//Add the successor vertex into the visited queue
							Ve.add(next);								//Add the successor vertex into the vertex explored set
							VeSize++;									//Increment explored vertex set size
							edgeList = new LinkedList<Edge>(); 			//Instantiate a new linked list for the edges
							edgeList.add(curEdge);						//Add the edge into the linked list
							if (!map.containsValue(edgeList)) {			//Check if the hash map contains the linked list of edges
								map.put(next, edgeList);				//Put the linked list into the map
							}
						}
					}
				}
			}
		}		
    }
				
	// TODO: I implemented this				
	//Retrieve the path to the vj(Goal Vertex)
	//Input: Takes a Vertex and iterate backwards to starting vertex to find the path
	public ArrayList<Vertex> retrievePath(Vertex vj) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();				//Instantiate array to store path to goal vertex
		Vertex curVertex = new Vertex(vj.toString());					//Instantiate the current vertex being looked at
		path.add(0, curVertex);											//Add current vertex into the list
		LinkedList<Edge> edgeList;
		Edge curEdge;
		while(map.get(curVertex) != null) {								//Check if current vertex has reached the starting vertex				
			edgeList = (map.get(curVertex)); 							//Grab the edge list at the current vertex 
			curEdge = edgeList.poll();									//Grab the first edge in the edge list
			curVertex = new Vertex(curEdge.vi.toString());				//Create new vertex for the previous vertex pointing at the current vertex
			path.add(0, curVertex);										//Add the new vertex into the list
		}
		return path;
	}

	// TODO: I implemented this
	//Return a shortest path as an array list
	//Input: Takes starting and goal vertex and find the shortest path between them
	//Output: Returns an array list with the vertices between starting to goal vertex
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {		
		bfs(vi, vj);
		ArrayList<Vertex> path = retrievePath(vj);
		return path;
	}

	//Output: Returns the set of explored vertices
	public Set<Vertex> getVertices() {
		return Ve;														
	}	
	
	//Output: Returns the set of explored Edges
	public Set<Edge> getEdges() {
		return Ee;				
	}

	//Starter Code
	class Vertex {
		Stack<Integer>[] pegs; // Each vertex will hold a Towers-of-Hanoi state.

		// There will be 3 pegs in the standard version, but more if you do
		// extra credit option A5E1.
		
		// Constructor that takes a string such as "[[4,3,2,1],[],[]]":
		@SuppressWarnings("unchecked")
		public Vertex(String vString) {
			String[] parts = vString.split("\\],\\[");
			pegs = new Stack[NUMBER_OF_PEGS];
			for (int i = 0; i < NUMBER_OF_PEGS; i++) {
				pegs[i] = new Stack<Integer>();
				try {
					parts[i] = parts[i].replaceAll("\\[", "");
					parts[i] = parts[i].replaceAll("\\]", "");
					ArrayList<String> al = new ArrayList<String>(
							Arrays.asList(parts[i].split(",")));
					// System.out.println("ArrayList al is: " + al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						Object item = it.next();
						// System.out.println("item is: " + item);
						Integer diskInteger = new Integer((String) item);
						pegs[i].push(diskInteger);
					}
				} catch (Exception e) {
				}
			}
		}

		public String toString() {
			String ans = "[";
			for (int i = 0; i < NUMBER_OF_PEGS; i++) {
				ans += pegs[i].toString().replace(" ", "");
				if (i < NUMBER_OF_PEGS - 1) {
					ans += ",";
				}
			}
			ans += "]";
			return ans;
		}

		@Override
		public boolean equals(Object v) {
			if (!(v instanceof Vertex)) {								//Checks if v is an Vertex object
				return false;											//Returns false if v is not an Vertex object
			} else {			
				Vertex o = (Vertex) v;									//Cast object v to an Vertex object
				return this.hashCode() == o.hashCode();					//Return true if both hash codes are the same, false otherwise
			}
		}

		@Override
		public int hashCode() {
			return this.toString().hashCode();							//Converts Vertex into a string and obtain the hash code from it
		}

	}

	// TODO: I implemented this
	class Edge {
		public Vertex vi;
		public Vertex vj;

		public Edge(Vertex vi, Vertex vj) {
			this.vi = vi;
			this.vj = vj;
		}

		public String toString() {
			return vi.toString() + " -> " + vj.toString();			
		}

		@Override
		public boolean equals(Object e) {
			if (!(e instanceof Edge)) {									//Checks if e is an edge object
				return false;											//Returns false if e is not an edge object
			} else {			
				Edge o = (Edge) e;										//Cast object e to an edge object
				return this.hashCode() == o.hashCode();					//Return true if both hash codes are the same, false otherwise
			}
		}

		@Override
		public int hashCode() {
			return this.toString().hashCode();							//Converts Edge into a string and obtain the hash code from it
		}
	}

	// TODO: I implemented this class
	class Operator {
		private int i, j;

		public Operator(int i, int j) {
			this.i = i;
			this.j = j;
		}

		// TODO: I implemented this
		// Additional explanation of what to do here will be given in GoPost or
		// as extra text in the spec.
		@SuppressWarnings("rawtypes")
		//Return a function that can be applied to a vertex to get a "successor" vertex, Checks if the move is possible
		Function getPrecondition() {
			return new Function() {
				@Override
				public Object apply(Object vertex) {
					if (vertex instanceof Vertex) {						//Check if vertex is an Vertex object
						Vertex o = (Vertex) vertex;						//Cast vertex to a Vertex object						
						if (o.pegs[i].isEmpty()) {						//Check if peg i is empty
							return false;								
						//Check if peg j is empty or if disk on peg i is smaller than disk on peg j
						} else if (o.pegs[j].isEmpty() || o.pegs[i].peek() < o.pegs[j].peek()) { 							
							 return true;
						}						
					}
					return false;
				}
			};
		}

		// TODO: I implemented this
		@SuppressWarnings("rawtypes")
		//Return a function that can be applied to vertex to get a "successor" vertex
		Function getTransition() {
			return new Function() {
				@Override
				public Object apply(Object vertex) {
					if (vertex instanceof Vertex) {						//Check if vertex is an Vertex object
						Vertex o = (Vertex) vertex;						//Cast vertex to a Vertex object
						o.pegs[j].push(o.pegs[i].pop());				//Move the disk from peg i to j
						return o;										//Return the modified vertex
					}
					return null;										//Return null if vertex is not a Vertex object
				}
			};
		}

		// TODO: I implemented this
		public String toString() {
			return "(" + i + ", " + j + ")";							//Returns a string as "(i, j)"
		}
	}

}
