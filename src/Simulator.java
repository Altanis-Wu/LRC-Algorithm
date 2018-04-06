import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

/*simulator.java
*Created on 2018年3月12日
*/

public class Simulator {
	private int numberOfVertex;
  	private Graph<Vertex, Edge> graph = new SimpleGraph<>(Edge.class);
  	private ArrayList<Vertex> vertexList=new ArrayList<>();
  	private int messageNumber=0;
  	private int rounds=0;
	public Simulator(int n){
		this.numberOfVertex=n;
	}
	
	public Output generateGraph(int num) throws InterruptedException{
		ArrayList<Integer> idList=new ArrayList<>();
		for(int i=0;i<num;i++){
			idList.add(uniqueId(num, idList));
		}
		for(int i=0;i<num;i++){
			Vertex vertex=new Vertex(idList.get(i), 
					idList.get(i), idList.get(i), "unknown");
	   		 vertexList.add(vertex);
	   		 graph.addVertex(vertex);
	   	 }
	   	 for(int i=0;i<num-1;i++){
	   		 Edge edge=new Edge(vertexList.get(i), vertexList.get(i+1));
	   		 graph.addEdge(vertexList.get(i), vertexList.get(i+1), edge);
	   	 }
	   	 Edge edge=new Edge(vertexList.get(num-1), vertexList.get(0));
	   	 graph.addEdge(vertexList.get(num-1), vertexList.get(0), edge);
	   	 System.out.println("Before:");
	   	 for(Vertex vertex:graph.vertexSet()){
	   		 System.out.println(vertex.toString());
	   	 }
	   	 while(!finish(vertexList)){
	   		rounds++;
	   		ExecutorService executor=Executors.newCachedThreadPool();
	   		for(int i=0;i<num;i++){
		   		executor.execute(new LCR_Algorithm(graph, vertexList.get(i)));
		   	}
	   		executor.shutdown();
	   	 }
	   	 Output output=new Output(messageNumber, rounds-1);
	   	 System.out.println("After:");
	   	 for(Vertex vertex:graph.vertexSet()){
	   		  System.out.println(vertex.toString());
	   	 }
	   	 return output;
   }
   
	protected int uniqueId(int num, ArrayList<Integer> idList){
		int randomId=(int) (Math.random()*3*num);
		if(idList.contains(randomId)||randomId==0){
			randomId=uniqueId(num, idList);
		}
		return randomId;
	}
	
	private boolean finish(ArrayList<Vertex> list){
		boolean result=true;
		for(int i=0;i<list.size();i++){
			if(list.get(0).getLeaderId()!=list.get(i).getLeaderId()){
				result=false;
			}
		}
		return result;
	}
	
	private class LCR_Algorithm implements Runnable{
		private Graph<Vertex, Edge> graph;
		private Vertex vertex;
		private Vertex neighbourRight, neighbourLeft;
		LCR_Algorithm(Graph<Vertex, Edge> g, Vertex v){
			this.graph=g;
			this.vertex=v;
		}
		public void run(){
			vertex.setSendId(vertex.getLeaderId());
			achieve();
			messageNumber++;
		}
		
		public void achieve(){
			Iterator<Edge> itr=graph.edgesOf(vertex).iterator();
			while(itr.hasNext()){
				Edge edge=itr.next();
				if(edge.getVertex2().getMyId()==vertex.getMyId()){
					if(edge.getVertex1().getSendId()>vertex.getLeaderId()){
						vertex.setLeaderId(edge.getVertex1().getSendId());
					}
				}
			}
			if(vertex.getLeaderId()==vertex.getMyId()){
				vertex.setStatus("leader");
			}else{
				vertex.setStatus("unknown");
			}
		}
	}
}
