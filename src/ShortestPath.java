import java.io.FileNotFoundException;
import java.util.Map;

import edu.princeton.cs.algs4.*;

public class ShortestPath {
	private MetroGraph mG;
	
	public ShortestPath(MetroGraph m) {
		mG = m;
	}
	private EdgeWeightedDigraph digraph() {
		EdgeWeightedDigraph di = new EdgeWeightedDigraph(mG.G.V());
		Graph G = mG.getGraph();
		Map<Integer, Platform> platMap = mG.getPlatformData();
		Platform p = null;
		Platform p1 = null;
		DirectedEdge e = null;
		
		for(int i = 1; i < G.V(); i++) {
			p = platMap.get(i);
			for (int j : G.adj(i)) {
				p1 = platMap.get(j);
				if ((p.getStationName().equals(p1.getStationName()))) {
					double weight = 6.0;
					e = new DirectedEdge(i,j,weight);
					di.addEdge(e);
				}
				else {
					double weight = 1.0;
					e = new DirectedEdge(i,j,weight);
					di.addEdge(e);
				}
			}
		}
		return di;
	}
	
	private int getStationID(String station) {
		Map<Integer, Platform> platMap = mG.getPlatformData();
		Platform p = null;
		int stationID = 0;
		
		for (int i = 1; i < mG.G.V(); i++) {
			p = platMap.get(i);
			if (p.getStationName().equals(station)) {
				stationID = p.getPlatformId();
			}
		}
		
		return stationID;
	}
	
	public void printRoute(Iterable<DirectedEdge> itr) {
		Map<Integer, Platform> platMap = mG.getPlatformData();
		
		for (DirectedEdge e : itr){
			Platform from = platMap.get(e.from());
			Platform to = platMap.get(e.to());
			
			if (from.getStationName().equals(to.getStationName())) {
				System.out.println("Switching from " + from.getTrainLine() + " line to " 
						+ to.getTrainLine() + " line at " + from.getStationName());
			}
			else {
				System.out.println(from.getStationName() + " --> " + to.getStationName());
			}
		}
	}
	
	public int travelTime(Iterable<DirectedEdge> itr){
		int time = 0;
		
		for (DirectedEdge e : itr) {
			time += e.weight();
		}
		return time;
	}
	
//	public int waitTime() {
//		
//	}
	
	public static void main(String[] args) throws FileNotFoundException {
		MetroGraph mG = new MetroGraph(args[0]);
		String fromStation = args[1];
		String toStation = args[2];
		
		ShortestPath sP = new ShortestPath(mG);

		int fromStationID = sP.getStationID(fromStation);
		int toStationID = sP.getStationID(toStation);
		
		EdgeWeightedDigraph di = sP.digraph();
		DijkstraSP shortest = new DijkstraSP(di, fromStationID);
		Iterable<DirectedEdge> stations = shortest.pathTo(toStationID);
		
		if (shortest.hasPathTo(toStationID)) {
			System.out.println("Printing route from " + fromStation + " to " + toStation);
			System.out.println();
			sP.printRoute(stations);
			System.out.println();
		}
		else {
			System.out.println("There is no path from " + fromStation + " to " + toStation);
		}
		
		int travelTime  = sP.travelTime(stations);
		System.out.println("Estimated time from " + fromStation + " to " + toStation 
				+ " is " + travelTime + " minutes.");
	}
}