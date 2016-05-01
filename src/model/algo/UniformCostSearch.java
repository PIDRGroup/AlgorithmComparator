package model.algo;

import java.util.ArrayList;

import model.env.Environment;
import model.env.Place;
import model.env.UnknownPlaceException;

public class UniformCostSearch extends Algorithm{

	public UniformCostSearch(){
		this.path = new ArrayList<Place>();
		this.eval = new Evaluation();
	}
	
	@Override
	public void grow(Environment world, Place source, Place destination) throws UnknownPlaceException {
		
		ArrayList<Node> frontiere = new ArrayList<Node>();
		ArrayList<Integer> exploration = new ArrayList<Integer>();
		
		frontiere.add(new Node(source, 0, null));
		
		this.eval.start();
		
		double previouscost = Double.MAX_VALUE;
		
		while(true){
			
			if (frontiere.isEmpty()){
				System.out.println("Frontière vide: Aucune solution n'a été trouvée");
				break;
			}
			
			double dist;
			int size = frontiere.size();
			ArrayList<Node> buffer = new ArrayList<Node>();
			
			for (int i = 0; i < size; i++){
				
				Node node = null;
				double min = Double.MAX_VALUE;
				
				for (int j = 0; j < frontiere.size();j++ ){
					
					this.eval.newVisite(frontiere.get(j).getstat());
					
					if((dist = frontiere.get(j).getpathcost()) < min){
						min = dist;
						node = frontiere.get(j);
					}
				}
				
				frontiere.remove(node);
				buffer.add(node);				
								
			}
			
			frontiere=buffer;
			Node currentnode=frontiere.get(0);
			frontiere.remove(0);
			this.eval.newNoeudEnvisage();
			
			if (currentnode.getstat() == destination){
				
				path.add(destination);
				
				ArrayList<Node> solution = currentnode.getsolvation();
				for (Node n: solution){
					path.add(n.getstat());
				}
				
				eval.gotASolution(currentnode.getpathcost(), path.size());
				
				break;
			}
			
			if (world.get(currentnode.getstat(), destination) < Double.MAX_VALUE){
				
				double cost = 0.0;
				int count = currentnode.getsolvation().size()+1;
				cost += currentnode.getpathcost()+ world.get(currentnode.getstat(), destination);
				
				if(cost < previouscost){
					previouscost = cost;
					
					this.eval.gotASolution(cost , count+1);
				}
				
						
			}
			
			exploration.add(world.indexOf(currentnode.getstat()));
			double currentpathcost = currentnode.getpathcost();
			ArrayList<Node> currentsolvation = new ArrayList<Node>();
			
			for(Node n: currentnode.getsolvation()){
				currentsolvation.add(n);
			}
			
			currentsolvation.add(currentnode);
			
			for (int i = 0; i < world.size(); i++){
				if((dist = world.get(currentnode.getstat(), world.getByIndex(i))) < Double.MAX_VALUE){
					
					this.eval.newVisite(world.getByIndex(i));
					
					double newpathcost = currentpathcost+dist;
					boolean isInFrontiere = false;
					int indice = 0;
					
					for (int j = 0; j < frontiere.size(); j++){
						
						this.eval.newVisite(frontiere.get(j).getstat());
						
						if (frontiere.get(j).getstat().equals(world.getByIndex(i))){
							isInFrontiere = true;
							indice = j;
							break;
						}
					}
					
					if (!(exploration.contains(i)||isInFrontiere)){
						frontiere.add(new Node(world.getByIndex(i), newpathcost,currentsolvation));
					}else if(isInFrontiere && frontiere.get(indice).isSuperior(new Node(world.getByIndex(i), newpathcost, null))){
						frontiere.set(indice,new Node(world.getByIndex(i),newpathcost,currentsolvation));
					}
				}	
			}
		}
		
		
		
	}

	@Override
	public String getName() {
		return "Uniform Cost Search";
	}

}
