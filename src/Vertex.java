/*Vertex.java
*Created on 2018年3月13日
*/

public class Vertex {
	private int sendId;
	private int myId;
	private int leaderId;
	private String status;
	public Vertex(int sId, int mId, int lId,String s){
		this.sendId=sId;
		this.myId=mId;
		this.leaderId=lId;
		this.status=s;
	}
	
	public int getSendId(){
		return sendId;
	}
	
	public void setSendId(int Id){
		this.sendId=Id;
	}
	
	public int getMyId(){
		return myId;
	}
	
	public int getLeaderId(){
		return leaderId;
	}
	
	public void setLeaderId(int Id){
		this.leaderId=Id;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String state){
		this.status=state;
	}
	
	public String toString(){
		return "Processor: "+ myId+" "+"Leader: "+leaderId+" "+status;
	}
}
