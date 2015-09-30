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
		testInvariant();
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
				testInvariant();
				return true;
			}
		}
		testInvariant();
		throw new RuntimeException("DEADCODER");//TODO: deadcode tbh WELL IT SHOULD BE. THIS EXCEPTION IS SANITY CHECK
		//return false;

	}

	public void setSelectedIndex(int select){
		this.selectedIndex = select;
	}

	public Carryable dropItem(){
		testInvariant();
		Carryable temp =  this.carriedItems.get(this.selectedIndex);
		this.carriedItems.set(this.selectedIndex, new NullEntity(CardinalDirection.NORTH));//remove item from our inventory because we are dropping it
		if(!(temp instanceof NullEntity)){
			this.carriedCount --;//decrement our count of items because we are losing an item
		}
		testInvariant();
		System.out.println("so just picked up: " + temp + "and now my inventory is... \n");//TODO: debug shititt
		for(Carryable eachOne: this.carriedItems){
			System.out.println(eachOne + "\n");
		}
		return temp; //because it needs to be placed back ono the board


	}


	//USED TO TEST INVENTORY INVARIANTS
	private void testInvariant(){
		assert(this.carriedItems.size() <= 5):"INVENTORY STATE INVARIANT VIOLATED";
		assert(this.carriedItems.size() >= 0):"INVENTORY STATE INVARIANT VIOLATED";
		assert(this.carriedCount <= 5):"INVENTORY STATE INVARIANT VIOLATED";
		assert(this.carriedCount >= 0):"INVENTORY STATE INVARIANT VIOLATED";
		int i = 0;
		for(Carryable eachEnt: this.carriedItems){
			assert(eachEnt != null):"INVENTORY STATE INVARIANT VIOLATED";
			i++;
		}
		assert(i == 5):"INVENTORY STATE INVARIANT VIOLATED";

	}


}
