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
		
		eval.start();
		
		ArrayList<Node> frontiere = new ArrayList<Node>();
		ArrayList<Integer> exploration = new ArrayList<Integer>();
		
		frontiere.add(new Node(source, 0,null));
		
		while(true){
			
			eval.newWhile();
			
			if (frontiere.isEmpty()){
				System.out.println("Frontière vide: Aucune solution n'a été trouvée");
				break;
			}
			
			double dist;
			int size = frontiere.size();
			ArrayList<Node> buffer = new ArrayList<Node>();
			
			for (int i = 0; i < size; i++){
				
				Node node = new Node(null,0,null);
				double min = Integer.MAX_VALUE;
				
				for (int j = 0; j < frontiere.size();j++ ){
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
			
			if (currentnode.getstat() == destination){
				
				path.add(destination);
				eval.gotASolution(currentnode.getpathcost());
				
				ArrayList<Node> solution = currentnode.getsolvation();
				for (Node n: solution){
					path.add(n.getstat());
				}
				
				break;
			}
			
			exploration.add(world.indexOf(currentnode.getstat()));
			double currentpathcost = currentnode.getpathcost();
			ArrayList<Node> currentsolvation = currentnode.getsolvation();
			currentsolvation.add(currentnode);
			
			for (int i = 0; i < world.size(); i++){
				if((dist = world.get(currentnode.getstat(), world.getByIndex(i))) < Integer.MAX_VALUE){
					double newpathcost = currentpathcost+dist;
					
					if (!(exploration.contains(i)||frontiere.contains(i))){
						frontiere.add(new Node(world.getByIndex(i), newpathcost,currentsolvation));
					}else{
						for (int j = 0; j < frontiere.size(); j++){
							if (frontiere.get(j).isSuperior(new Node(world.getByIndex(i), newpathcost, null))){
								frontiere.set(j,new Node(world.getByIndex(i),newpathcost,currentsolvation));
							}
						}
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
