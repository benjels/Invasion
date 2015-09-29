package gamelogic;

import gamelogic.entities.Carryable;
import gamelogic.entities.NullEntity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * used by the players in the game to keep track of which items that they are carrying etc
 * @author Max Brown
 *
 */
public class Inventory {
	private final ArrayList<Carryable> carriedItems = new ArrayList<>(); 
	private int selectedIndex = 0;
	private final int INVENTORY_SIZE = 5;
	private int carriedCount; //amount of actual items held
	
	public Inventory(){
		for(int i = 0; i < this.INVENTORY_SIZE; i ++){//fill up the inventory with null entities so that when we drop something something into a room we are never dropping a true null
			this.carriedItems.add(new NullEntity(CardinalDirection.NORTH));
		}
	}
	
	public boolean pickUpItem(Carryable pickUp){
	//cannot carry more than capacity
		if(this.carriedCount == this.INVENTORY_SIZE){
			throw new RuntimeException("no you cannot carry anything morea sdfasdf");//TODO: obvs this should be handled better than it is atm
			//return false;
		}
		//traverse for the first free slot
		for(int i = 0; i < this.INVENTORY_SIZE; i ++){
			if(this.carriedItems.get(i) instanceof NullEntity){//if the space is blank, put the item there
				this.carriedItems.set(i, pickUp);
				//we picked something up so increase our count of carried items
				if(!(pickUp instanceof NullEntity)){
					this.carriedCount ++;
				}
				System.out.println("so just picked up: " + pickUp + "and now my inventory is... \n");//TODO: debug shititt
				for(Carryable eachOne: this.carriedItems){
					System.out.println(eachOne + "\n");
				}
				return true;
			}
		}
		throw new RuntimeException("DEADCODER");//TODO: deadcode tbh WELL IT SHOULD BE. THIS EXCEPTION IS SANITY CHECK
		//return false;
		
	}
	
	public void setSelectedIndex(int select){
		this.selectedIndex = select;
	}
	
	public Carryable dropItem(){
		Carryable temp =  this.carriedItems.get(this.selectedIndex);
		this.carriedItems.remove(this.selectedIndex);//remove item from our inventory because we are dropping it
		return temp; //because it needs to be placed back ono the board

	}
	
	
	
}
