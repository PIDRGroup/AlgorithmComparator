package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class SMAStar extends Algorithm{

	private Environment world;
	private Place destination;	
	private int max_depth = 10;
	private int memory_size = 15;
	
	public SMAStar() {
		path = new ArrayList<Place>();
		eval = new Evaluation(); 
	}
	
	@Override
	public void grow(Environment world, Place src, Place dest) throws UnknownPlaceException {

		this.world = world;
		this.destination = dest;
		
		ArrayList<NodeSMAstar> memory = new ArrayList<NodeSMAstar>();
		ArrayList<NodeSMAstar> queue = new ArrayList<NodeSMAstar>();
		
		NodeSMAstar buffer = new NodeSMAstar(src, h(src), 0.0, 0, null);
		for (int i = 0; i < this.world.size(); i++){
			if(world.get(src.getIndex(), this.world.getByIndex(i).getIndex()) < Double.MAX_VALUE){
				buffer.addSuccessor(i);
			}
		}
		
		memory.add(buffer);
		queue.add(buffer);
		
		NodeSMAstar current = null;
		NodeSMAstar toDelete = null;
		NodeSMAstar parent = null;
		
		int indice = 0;
		
		this.eval.start();
		
		while(true){
			
			System.out.println("Je suis bloqué la");
			
			if (queue.isEmpty()){
				System.out.println("Chemin non trouvé! La queue est vide");
				break;
			}
			
			queue = this.sortQueue(queue);
			
			current = queue.get(0);
			
			if (current.getPlace().equals(dest)){
				this.eval.gotASolution(0.0, 0);
				break;
			}
			
			if((Integer)(indice = current.nextSuccessor()) == -1){
				queue.remove(current);
				System.out.println("Par la");
			}else{
				
				Place successor = world.getByIndex(indice);
				NodeSMAstar chargesuccessor = this.loadNodeSMAstar(successor, current.getDepth()+1, current);
				chargesuccessor.setG(current.getG()+world.get(current.getPlace().getIndex(), successor.getIndex()));
				memory.add(chargesuccessor);
				current.addSuccessorMemory(chargesuccessor);
				
				if(!successor.equals(dest) && chargesuccessor.getF() >= this.max_depth){
					chargesuccessor.setF(Double.MAX_VALUE);
				}else{
					chargesuccessor.setF(Math.max(current.getF(), chargesuccessor.getG()+h(successor)));
				}
				
				if(current.isAllSuccessorGenerated()){
					this.updateNode(current);
				}
				
				if(current.isAllSuccessorInMemory()){
					queue.remove(current);
				}
				
				if(memory.size() == memory_size){
					queue = this.sortQueue(queue);
					toDelete = queue.get(queue.size()-1);
					queue.remove(toDelete);
					
					if((parent = toDelete.getParent()) != null){
						parent.rmSuccessorMemory(toDelete);
					}
					
					memory.remove(toDelete);
					if(!queue.contains(parent)){
						queue.add(parent);
					}
				}
				
				queue.add(chargesuccessor);
			}
			
			
			
		}
		
	}
	
	private NodeSMAstar loadNodeSMAstar(Place place, int depth, NodeSMAstar parent) throws UnknownPlaceException{
		NodeSMAstar buffer = new NodeSMAstar(place, Double.MAX_VALUE, Double.MAX_VALUE, depth, parent);
		
		for (int i = 0; i < this.world.size(); i++){
			System.out.println("la");
			if(world.get(place.getIndex(), i) < Double.MAX_VALUE){
				buffer.addSuccessor(i);
			}
		}
		
		
		return buffer;
	}
	
	private ArrayList<NodeSMAstar> sortQueue(ArrayList<NodeSMAstar> queue){
		ArrayList<NodeSMAstar> sortedqueue = new ArrayList<NodeSMAstar>();
		
		int size = queue.size();
		
		for (int i = 0; i < size; i++){
			
			System.out.println("la2");
			
			double min = Double.MAX_VALUE;
			NodeSMAstar first = null;
			
			for (int j = 0; j < queue.size(); j++){
				if (queue.get(j).getF() < min){
					min = queue.get(j).getF();
					first = queue.get(j);
				}
				
				if (first == null) first = queue.get(0); 
				
				sortedqueue.add(first);
				queue.remove(first);
			}
		}
		
		return sortedqueue;
	}
	
	private void updateNode(NodeSMAstar current){
		double min = Double.MAX_VALUE; 
		
		for (NodeSMAstar node : current.getSuccessors()){
			
			System.out.println("la3");
			
			if (node.getF() < min){
				min = node.getF();
			}
		}
		
		if (current.getSecondBest() < min){
			min = current.getSecondBest();
		}
		
		current.setF(min);
		
		NodeSMAstar buffer = current;
		NodeSMAstar bufferparent = current.getParent();
		
		while(bufferparent != null){
			
			System.out.println("la4");
			
			if (buffer.getF() < bufferparent.getSecondBest()){
				bufferparent.setF(buffer.getF());
			}else{
				bufferparent.setF(bufferparent.getSecondBest());
			}
			
			buffer = bufferparent;
			bufferparent = bufferparent.getParent();
		}
		
	}
	
	public double h(Place current){
		return current.distanceEuclidienne(this.destination);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SMA*";
	}

}
