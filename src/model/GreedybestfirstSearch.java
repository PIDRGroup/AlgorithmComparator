package model;

import java.util.ArrayList;

public class GreedybestfirstSearch<E extends Number> extends Algorithm<E> {

	public GreedybestfirstSearch(Environment<E> env){
		this.world = env;
		this.path = new ArrayList<Integer>();
	}
	
	@Override
	public void grow(int source, int destination) throws UnknownPlaceException {
		// TODO Auto-generated method stub

		ArrayList<Node> frontiere = new ArrayList<Node>();
		ArrayList<Integer> exploration = new ArrayList<Integer>();
		
		frontiere.add(new Node(source, 0,null));
		
		while(true){
			if (frontiere.isEmpty()){
				System.out.println("Frontière vide: Aucune solution n'a été trouvée");
				break;
			}
			
			int dist;
			int size = frontiere.size();
			ArrayList<Node> buffer = new ArrayList<Node>();
			
			for (int i = 0; i < size; i++){
				
				Node node = new Node(-1,0,null);
				int min = Integer.MAX_VALUE;
				
				for (int j = 0; j < frontiere.size();j++ ){
					if((dist = (Integer) frontiere.get(j).getpathcost()) < min){
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
			
			exploration.add(currentnode.getstat());
			int currentpathcost = (Integer) currentnode.getpathcost();
			ArrayList<Node> currentsolvation = currentnode.getsolvation();
			currentsolvation.add(currentnode);
			
			for (int i = 0; i < world.size(); i++){
				if((dist = world.get(currentnode.getstat(), i).intValue()) < Integer.MAX_VALUE){
					int newpathcost = currentpathcost+dist;
					
					if (!(exploration.contains(i)||frontiere.contains(i))){
						frontiere.add(new Node(i, newpathcost,currentsolvation));
					}else{
						for (int j = 0; j < frontiere.size(); j++){
							if (frontiere.get(j).isSuperior(new Node(i, newpathcost, null))){
								frontiere.set(j,new Node(i,newpathcost,currentsolvation));
							}
						}
					}
				}	
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
