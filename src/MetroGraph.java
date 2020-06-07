// For cs310 pa4 Boston metro graph 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import edu.princeton.cs.algs4.*;

public class MetroGraph {

	class Node {
		String name;
		int nodeId;
		String trainLine;
		double lat, lon;
		int inNodeId, outNodeId; // not needed once have G
	}

	static final int MAXNODES = 200;
	Node[] nodes = new Node[MAXNODES];
	Graph G;

	public MetroGraph(String filePath) throws FileNotFoundException {
		Scanner in = null;
		in = new Scanner(new File(filePath));
		// placeholder Node for spot 0
		Node n0 = new Node();
		n0.name = "fake node";
		n0.trainLine = " ";
		nodes[0] = n0;
		int nodeId = 1;
		while (in.hasNextLine()) {
			String line1 = in.nextLine();
			String[] tokens = line1.split(",");
			int thisStation = Integer.parseInt(tokens[0]);
			int thisNodeId = nodeId++;
			// System.out.println("doing station " + thisStation + " node " + thisNodeId);
			assert thisNodeId == thisStation : "bad original nodeId";
			Node node = new Node();
			nodes[thisNodeId] = node;
			node.nodeId = thisNodeId;
			node.name = tokens[1];
			node.trainLine = tokens[2];
			node.inNodeId = Integer.parseInt(tokens[3]);
			node.outNodeId = Integer.parseInt(tokens[4]);
			if (tokens.length > 5 && !tokens[5].isEmpty())
				node.lat = Double.parseDouble(tokens[5]);
			if (tokens.length > 6 && !tokens[6].isEmpty())
				node.lon = Double.parseDouble(tokens[6]);
		}
		in.close();
		int nV = nodeId; // including fake node 0
		G = new Graph(nV);
		// Connect nodes together by inNodeIds and outNodeIds
		for (int i = 1; i < nV; i++) {
			Node n = nodes[i];
			if (n.outNodeId > 0) // 0 at end-of-trainLine
				G.addEdge(n.nodeId, n.outNodeId);
			if (n.inNodeId > 0)
				G.addEdge(n.nodeId, n.inNodeId);
		}
		// connect the nodes for one station all together
		// with edges for transfers
		Set<String> stations = new HashSet<String>();
		for (int i = 1; i < nV; i++)
			stations.add(nodes[i].name);
		for (String s : stations) {
			// Find all nodeIds for station
			Set<Integer> nodeIds = new HashSet<Integer>();
			for (int i = 1; i < nV; i++) {
				if (nodes[i].name.equals(s))
					nodeIds.add(i);
			}
			if (nodeIds.size() > 1) {
				// case of multiple nodes for station: link together
				for (int i : nodeIds)
					for (int j : nodeIds)
						if (i < j)
							G.addEdge(i, j);
			}
		}
		//System.out.println("orig graph #edges =" + G.E());
		deDup();
		//System.out.println("after dedup, graph #edges =" + G.E());
	}

	Graph getGraph() {
		return G;
	}
	
	// check that an edge between nodes x and y would be reasonable.
	// Edges should be between nodes on the same trainLine or
	// nodes at the same station, so check if neither is true
	// but allow a RedA station to connect directly to a Red station
	// because that's just following a split, no rider transfer needed.
	private void ckEdge(int x, int y) {
		// check edge: OK to match RedA with Red and GreenC with Green
		// since these connections allow trains to go through, no transfer
		if (!nodes[x].name.equals(nodes[y].name)
				&& !nodes[x].trainLine.substring(0, 3).equals(nodes[y].trainLine.substring(0, 3))) {
			System.out.println("link connects different stations on different lines: " + nodes[x].name + " on "
					+ nodes[x].trainLine + " and " + nodes[y].name + " on " + nodes[y].trainLine);
		}
	}

	// named SimpleEdge to be different from Edge of book
	private class SimpleEdge {
		private int x, y;

		SimpleEdge(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object other) {
			// code on pg. 103, adapted
			if (this == other)
				return true;
			if (other == null)
				return false;
			if (this.getClass() != other.getClass())
				return false;
			SimpleEdge o = (SimpleEdge) other;
			return x == o.x && y == o.y || x == o.y && y == o.x;
		}

		@Override
		public int hashCode() {
			return Integer.valueOf(x).hashCode() & Integer.valueOf(y).hashCode();
		}
	}

	private void deDup() {
		Set<SimpleEdge> edges = new HashSet<SimpleEdge>();
		for (int i = 1; i < G.V(); i++) {
			for (int j : G.adj(i)) {
				if (i < j) {
					edges.add(new SimpleEdge(i, j));
				} else if (i > j) {
					edges.add(new SimpleEdge(j, i)); // smaller first
				} else
					System.out.println("Didn't expect i->i edge, i = " + i);
			}
		}
		G = new Graph(G.V());  // recreate it
		for (SimpleEdge e : edges) {
			ckEdge(e.x, e.y);
			G.addEdge(e.x, e.y);
		}
	}

	public Map<Integer, Platform> getPlatformData() {
		Map<Integer, Platform> platforms = new HashMap<Integer, Platform>();
		// System.out.println("G.V =" + G.V());
		for (int i = 1; i < G.V(); i++) {
			Platform p = new Platform(nodes[i].name, nodes[i].trainLine, nodes[i].lat, nodes[i].lon, nodes[i].nodeId);
			// System.out.println("getPlatformData: " + p.getNodeId());
			platforms.put(p.getPlatformId(), p);
		}
		return platforms;
	}
	public Set<String> findAllLines(){
		Map<Integer, Platform> platMap = getPlatformData();
		Set<String> findAllLines = new TreeSet<String>();
		Platform p = null;
		for (int i = 1; i < G.V(); i++) {
			p = platMap.get(i);
			findAllLines.add(p.getTrainLine());
		}
		findAllLines.remove("RedA"); // I'm getting a NullPointerException error with
									 // "RedA" so i'm removing it from the Set.
		return findAllLines;
	}
	public Platform endStation(String line){
	
		Platform p = null;
		double bigLat = 0;
		Platform endStation = null;
		Map<Integer, Platform> platMap = getPlatformData();
		for (int i = 1; i < G.V(); i++) {
			p = platMap.get(i);					 // get platform data the ith vertex
			if (p.getTrainLine().equals(line)) { // check if the platform is the same train line
				if(G.degree(i) == 1) {			 // check if the platform is a end station 
					if(p.getLatitude() > bigLat) {
						bigLat = p.getLatitude();
						endStation = p;
					}
				}
			}
		}
		 return endStation;
		//System.out.println(endStation);
	}	

	
	public void printLine(String line) {
		Set<String> visitedStations = new HashSet<String>();
		Platform endStation = endStation(line);
		Iterable<Integer> adj = G.adj(endStation.getPlatformId());
		Map<Integer, Platform> platMap = getPlatformData();
		Platform p = null;
		Platform p1 = null;
		visitedStations.add(endStation.getStationName());
		System.out.println();
		System.out.print(line + ": " + endStation.getStationName());
		for (int i = 1; i < G.V(); i++){
			p = platMap.get(i);
			if (p.getTrainLine().equals(line)) {
				for (int j : adj) {
					p1 = platMap.get(j);
					if (!visitedStations.contains(p1.getStationName())) {
						System.out.print( ", " + p1.getStationName());
						visitedStations.add(p1.getStationName());
						adj = G.adj(j);
					}
				}
			}
		}
	}
}