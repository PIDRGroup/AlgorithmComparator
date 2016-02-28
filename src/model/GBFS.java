package model;

import java.util.ArrayList;

public class GBFS extends Algorithm{

	public GBFS(){
		this.path = new ArrayList<Place>();
	}
	
	@Override
	public void grow(Environment world, Place source, Place destination) throws UnknownPlaceException {
		// TODO Auto-generated method stub

		ArrayList<Node> frontiere = new ArrayList<Node>();
		ArrayList<Integer> exploration = new ArrayList<Integer>();
		
		frontiere.add(new Node(source, 0,null));
		
		while(true){
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
				break;
			}
			
			exploration.add(world.indexOf(currentnode.getstat()));
			double currentpathcost = currentnode.getpathcost();
			ArrayList<Node> currentsolvation = currentnode.getsolvation();
			currentsolvation.add(currentnode);
			
			for (int i = 0; i < world.size(); i++){
				if((dist = world.get(currentnode.getstat(), world.getPlace(i))) < Integer.MAX_VALUE){
					double newpathcost = currentpathcost+dist;
					
					if (!(exploration.contains(i)||frontiere.contains(i))){
						frontiere.add(new Node(world.getPlace(i), newpathcost,currentsolvation));
					}else{
						for (int j = 0; j < frontiere.size(); j++){
							if (frontiere.get(j).isSuperior(new Node(world.getPlace(i), newpathcost, null))){
								frontiere.set(j,new Node(world.getPlace(i),newpathcost,currentsolvation));
							}
						}
					}
				}	
			}
		}
	}

	@Override
	public String getName() {
		return "Greedy Best First Search";
	}

}
