/* MWST.java
   CSC 226 - Winter 2017
   Assignment 2 - Template for a Minimum Weight Spanning Tree algorithm

   Original:
   CSC 225 - Spring 2012
   Assignment 5 - Template for a Minimum Weight Spanning Tree algorithm

   The assignment is to implement the mwst() method below, using any of the algorithms
   studied in the course (Kruskal, Prim-Jarnik or Baruvka). The mwst() method computes
   a minimum weight spanning tree of the provided graph and returns the total weight
   of the tree. To receive full marks, the implementation must run in O(mlog(n)) time
   on a graph with n vertices and m edges.

   This template includes some testing code to help verify the implementation.
   Input graphs can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
	java MWST
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. graphs.txt), run the program with
    java MWST graphs.txt
	
   The input format for both methods is the same. Input consists
   of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   For example, a path on 3 vertices where one edge has weight 1 and the other
   edge has weight 2 would be represented by the following
   
    3
	0 1 0
	1 0 2
	0 2 0
	
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   B. Bird - 03/11/2012
*/

import java.util.Scanner;
import java.io.File;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;

public class MWST{

	/*
	* Edge class.
	* Used by the mwst method to keep track of edges between vertices.
	*/
	static class Edge implements Comparable<Edge>{
		
		public int to;
		public int from;
		public int weight;

		public Edge(int a, int b, int c){
			this.to = a;
			this.from = b;
			this.weight = c;
		}

		public int getWeight() {return weight;}
		public int getDest() {return to;}
		public int getPar() {return from;}

		public boolean equals(Edge e) {
			return this.getWeight() == e.getWeight();
		}

		public int compareTo(Edge e) {
			if(this.equals(e)) {
				return 0;
			} else if (getWeight() > e. getWeight()) {
				return 1;
			} else {
				return -1;
			}
		}
		
	}

	/* mwst(G)
		Given an adjacency matrix for graph G, return the total weight
		of all edges in a minimum weight spanning tree.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int mwst(int[][] G){

		int numVerts = G.length;

		boolean[] visited = new boolean[numVerts];			//Array for keeping track of visited vertices.
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>();		//PQ of edges.
		Queue<Edge> mst = new LinkedList<Edge>();			//MST to which edges will be added.

		//Start at vertex 0.
		visit(G, 0, visited, pq);

		//Continue checking vertices until the PQ is empty or the MST is full.
		while(!pq.isEmpty() && mst.size() < numVerts - 1) {
			Edge e = pq.remove();
			int v = e.from, w = e.to;
			if(visited[v] && visited[w]) continue;
			mst.add(e);
			if (!visited[v]) visit(G, v, visited, pq);
			if (!visited[w]) visit(G, w, visited, pq);
		}

		//Adding up the sum of all the edges in the MST.
		int totalWeight = 0;
		for(Edge e : mst) {
			totalWeight += e.getWeight();
		}

		return totalWeight;
	}

	/*
	* Visit method.
	* Used by the mwst method to visit vertices in the adjacency matrix.
	*/
	private static void visit(int[][] G, int v, boolean[] visited, PriorityQueue<Edge> pq) {
		visited[v] = true;
		for(int i = 0; i < G.length; i++) {
			if(!visited[i] && G[v][i] != 0) {
				pq.add(new Edge(i, v, G[v][i]));
			}
		}
	}

	public static void main(String[] args){
		/* Code to test your implementation */
		/* You may modify this, but nothing in this function will be marked */

		int graphNum = 0;
		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(!s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				G[i] = new int[n];
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			if (!isConnected(G)){
				System.out.printf("Graph %d is not connected (no spanning trees exist...)\n",graphNum);
				continue;
			}
			int totalWeight = mwst(G);
			System.out.printf("Graph %d: Total weight is %d\n",graphNum,totalWeight);
				
		}
	}

	/* isConnectedDFS(G, covered, v)
	   Used by the isConnected function below.
	   You may modify this, but nothing in this function will be marked.
	*/
	static void isConnectedDFS(int[][] G, boolean[] covered, int v){
		covered[v] = true;
		for (int i = 0; i < G.length; i++)
			if (G[v][i] > 0 && !covered[i])
				isConnectedDFS(G,covered,i);
	}
	   
	/* isConnected(G)
	   Test whether G is connected.
	   You may modify this, but nothing in this function will be marked.
	*/
	static boolean isConnected(int[][] G){
		boolean[] covered = new boolean[G.length];
		for (int i = 0; i < covered.length; i++)
			covered[i] = false;
		isConnectedDFS(G,covered,0);
		for (int i = 0; i < covered.length; i++)
			if (!covered[i])
				return false;
		return true;
	}

}