package gamelogic.entities;

import gamelogic.CardinalDirection;
import gamelogic.events.CarrierCloseEvent;
import gamelogic.events.CarrierOpenCloseEvent;
import gamelogic.events.CarrierOpenEvent;
import gamelogic.events.InventorySelectionEvent;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * used by the players in the game to keep track of which items that they are carrying etc
 * @author Max Brown
 *
 */




/*goodshit	imo use composite pattern and the interface that Carrier and Carryable both implement has the abstract methods:
1)checkIfPickupChangedPlayerStatus                                                                                                                                                                                                                                                                   
2)checkIfDropChangedPlayerStatus
e.g. for pickup when we pick something up we call checkIfPickupChangedPlayerStatus() on it, if its a leaf, it will e.g. change NV goggles boolean field if it is NV goggles
if on the other hand it is a Carrier, it will call checkIfPickupChangedPlayerStatus() on all of the things it holds. so two diff implementations of this method.

!actually can  just make Inventory an abstract class with all the behaviour that it has but its individual implementations (e.g. pack, bag, wallet) have different sizes set in their 
constructors. Then just make Inventory implement the aforementioned alterred version of Carryable and it's all good.

one thing to think about:
	e.g. how does NV goggles update the "status" of the player? do we need to give everything Carryable the field of who currently holds it FOR WHEN IT IS DROPPED? would prefer a more dope asf solution but it's not that bad as required dropMethod can just unset that field when something dropped
		^^^ JUST SET PLAYER AS A FIELD IN CARRYABLE ELSE IT GETS DIFFICULT ASF. THAT FIELD IS UPDATED BY checkIfPickupChangedPlayerStatus() so maybe rename those two method ehh
		
		ez tbh
		
		ObjectiveWorldTimeCLockThread class tbh                            
*/

public abstract class Carrier extends Carryable{
	private final ArrayList<Carryable> carriedItems;
	private int selectedIndex = 0; //by default the selected index is 0 (the left most one)
	private final int inventoryCapacity; //the amount of slots
	private final int slotSize; //the size that each slot can accomodate. things we are putting in the bag must be smaller than or equal to this
	private int carriedCount; //amount of actual items held
	private boolean containsNightVisionGoggles = false; //true when night vision goggles in thsi inventory
	private Carrier upOneLevel = this; //the carrier that contains this one. NOTE: all initially set to point at self, but when it is picked up, it gets set to whatever carrier it is placed in
	
	public Carrier(CardinalDirection directionFaced, int inventoryCapacity, int slotSize, int sizeRequired){
		super(directionFaced, sizeRequired);
		this.carriedItems = new ArrayList<Carryable>(0);
		this.inventoryCapacity = inventoryCapacity;
		this.slotSize = slotSize;
		for(int i = 0; i < this.inventoryCapacity; i ++){//fill up the inventory with null entities so that when we drop something something into a room we are never dropping a true null
			this.carriedItems.add(new NullEntity(CardinalDirection.NORTH));
		}
		this.testInvariant();
	}

	/**
	 * attempts to place an item that the character wanted to pick up into their inventory. First trys to place it in the 
	 * currently selected slot. If that is full, trys to place it in the first available slot. If there are no slots available in this inventory, does nothing
	 * @param pickUp the item that we are trying to place in the inventory
	 * @return bool true if the item is placed in the inventory, else false
	 */
	public boolean pickUpItem(Carryable pickUp){
		System.out.println("im a " + this);
		//assert(this.playerIBelongTo != null):"i need to be attached to someone!";
		testInvariant();
		//cannot carry more than capacity AND cannot put things in this inventory that are larger than the slot size
		if((this.carriedCount == this.inventoryCapacity && !(pickUp instanceof NullEntity)) || pickUp.getSize() > this.slotSize){
			System.out.println(pickUp.getSize());
			System.out.println(this.slotSize);
			throw new RuntimeException("EITHER YOUR INVENTORY IS FULL OR YOU TRIED TO PICK UP SOMETHING THAT IS TOO BIG FOR SLOTS IN CURRENT INVENTORY");//TODO: obvs this should be handled better than it is atm
			//return false;
		}
		//if the selected slot is free, put item there
		if(this.carriedItems.get(this.selectedIndex) instanceof NullEntity ){
			this.carriedItems.set(this.selectedIndex, pickUp);
			//we picked something up so increase our count of carried items
			if(!(pickUp instanceof NullEntity)){
				this.carriedCount ++;
				//if we picked up another carrier, set its upOneLevel to this 
				if(pickUp instanceof Carrier){
					((Carrier) pickUp).setUpOneLevel(this);
				}
			}
			///DEBUG
			System.out.println("so just picked up: " + pickUp + "and now my inventory is... \n");//TODO: debug shititt
			for(Carryable eachOne: this.carriedItems){
				System.out.println(eachOne + "\n");
			}
			///////////
			testInvariant();
			pickUp.checkIfPickingUpThisItemChangesPlayerState(this.getCurrentHolder());//recursively descends the "tree" of Carryables starting from this one checking if one of the Carryables taht we are picking up will change the player's status in some way s
			return true;
			
			
		}else{ //if the selected slot not free, try to put picUp somewhere else in inventory
			for(int i = 0; i < this.inventoryCapacity; i ++){
				if(this.carriedItems.get(i) instanceof NullEntity){//if the space is blank, put the item there
					this.carriedItems.set(i, pickUp);
					//we picked something up so increase our count of carried items
					if(!(pickUp instanceof NullEntity)){
						this.carriedCount ++;
						//if we picked up another carrier, set its upOneLevel to this
						if(pickUp instanceof Carrier){
							((Carrier) pickUp).setUpOneLevel(this);
						}
					}
					//DEBUG
					System.out.println("so just picked up: " + pickUp + "and now my inventory is... \n");//TODO: debug shititt
					for(Carryable eachOne: this.carriedItems){
						System.out.println(eachOne + "\n");
					}
					////////
					testInvariant();
					pickUp.checkIfPickingUpThisItemChangesPlayerState(this.getCurrentHolder());//recursively descends the "tree" of Carryables starting from this one checking if one of the Carryables taht we are picking up will change the player's status in some way s
					return true;
				}
			}
		}
		
		//in case that there are no free slots in the inventory
		testInvariant();
		System.out.println("attempted to pick up but nothing happened. prob because your inventory full and you tried to pick up a null or something");
		return false; 

	}
	
	

	/**
	 * attempts to drop the item in the currently selected slot back into the cached entities array where the player is currently standing in a room. If the selected slot is empty, the NullEntity 
	 * in that slot is returned back to the cached entities and effectively nothing happens
	 * @return Carryable the item that is in the selected slot in the inventory that we are trying to "drop"
	 */
	public Carryable dropItem(){
		assert(this.getCurrentHolder() != null):"i need to be attached to someone!";
		testInvariant();
		Carryable drop =  this.carriedItems.get(this.selectedIndex);
		
		//check if dropping the item changed the player's status
		drop.checkIfDroppingThisItemChangesPlayerState(this.getCurrentHolder());//recursively descends the "tree" of Carryables starting from this one checking if dropping one of those carryables changes the player's status
		
		//dropped the item, so it no longer belongs to this player
		drop.setCurrentHolder(null);
		
		//"remove" item from inventory by replacing its slot with null entity
		this.carriedItems.set(this.selectedIndex, new NullEntity(CardinalDirection.NORTH));
		//decrement our count of items because if dropped one
		if(!(drop instanceof NullEntity)){
			this.carriedCount --;
			//if we dropped a carrier, its up one level is no longer this carrier
			if(drop instanceof Carrier){
				((Carrier) drop).setUpOneLevel((Carrier) drop); //peep the double cast
			}
		}
		//debug///
		testInvariant();
		System.out.println("so just dropped up: " + drop + "and now my inventory is... \n");//TODO: debug shititt
		for(Carryable eachOne: this.carriedItems){
			System.out.println(eachOne + "\n");
		}
		///////////
		
		
		
		//return the "dropped" items to be placed back into the entities cache
		return drop; 
	}

	
	
/*	*//**
	 * checks whether the player picked up something that changes their status. e.g. picking up night vision goggles will set nightVisionEquipped to true
	 *//*
	private void checkIfPickupChangedPlayerStatus(Carryable pickUp){
		if(pickUp instanceof NightVisionGoggles){
			this.playerIBelongTo.setNightVision(true);
		}

	}
	*//**
	 * checks whether the item that we are dropping changes the player's status. e.g. dropping the nightvision goggles results in a lack of nightvision.
	 * @param drop
	 *//*
	protected void checkIfDropChangedPlayerStatus(Carryable drop){

		if(drop instanceof NightVisionGoggles){
			this.playerIBelongTo.setNightVision(false);
		}
	
	}*/
	
	
	
	//CREATES A LIST OF THE CARRYABLES IN THIS CARRIER TO BE SENT TO THE GUI
	public ArrayList<RenderEntity> generateDrawableInventory() {
		//create the arraylist that will be returned as the inventroy
		ArrayList<RenderEntity> inventory = new ArrayList<>(0);
		//traverse our inventory and create the Render versions of all the entities and put them in list
		for(Carryable eachItem: this.carriedItems){
			inventory.add(  eachItem.generateDrawableCopy()); 
		}
		//we populated our inventory list so return it
		return inventory;
	}

	


	///INVENTORY SELECTION/opening/closing EVENTS///
	
	//USED TO CHANGE SELECTED INVENTORY SLOT
	public boolean attemptInventorySelectionEventByPlayer(InventorySelectionEvent eventWeNeedToUpdateStateWith) {
		this.testInvariant();
		assert(eventWeNeedToUpdateStateWith.getSlot() <= this.inventoryCapacity):"YOU TRIED TO ACCESS AN INVENTORY SLOT WHICH THIS CARRIER IS TOO SMALL FOR";
		//set the player's attempted selection
		this.selectedIndex = eventWeNeedToUpdateStateWith.getSlot() - 1;
		//we set it so return true
		this.testInvariant();
		System.out.println("just selected slot: " + (this.selectedIndex + 1));
		return true;
	}
	
	//USED TO OPEN AND CLOSE CARRIERS WITHIN CARRIERS
	public boolean attemptSwitchCurrentInventoryEventByPlayer(CarrierOpenCloseEvent eventWeNeedToUpdateStateWith) {
		
		if(eventWeNeedToUpdateStateWith instanceof CarrierOpenEvent){
			//check that selected index is a carrier
			if(this.carriedItems.get(this.selectedIndex) instanceof Carrier){
				this.getCurrentHolder().setCurrentInventory((Carrier) this.carriedItems.get(this.selectedIndex));
				System.out.println("just went down one level in the inv tree and now the current inv is: " + this.getCurrentHolder().getCurrentInventory());
				return true;
			}else{
				throw new RuntimeException("cannot open a non carrier item in inventor");//just delete this whole else clasue will return false anyway
			}
		}else if(eventWeNeedToUpdateStateWith instanceof CarrierCloseEvent){
			System.out.println("the up one level is: " + this.upOneLevel);
			System.out.println("the current holder is: " + this.getCurrentHolder());
			this.getCurrentHolder().setCurrentInventory(this.upOneLevel);
			System.out.println("just went up one level in the inventory tree and now the current inv is :" + this.getCurrentHolder().getCurrentInventory());
			return true;
			}
		else{
			throw new RuntimeException("there should beo nly two CarrierOpenCloseEvents");
			//return false; //e.g. in case that the selected index is not a carrier etc
		}
	}
	

	
	//IF WE PICKED UP ONE OF THESE CARRIER ITEMS THEN WE NEED TO CALL THIS METHOD RECURSIVELY ON ALL OF THE CARRIER'S CONTENTS because they might contain an item that changes
	//the player's status
	//THE OTHER SPECIAL THING TO MENTION ABOUT THESE TWO METHODSI S HOW THE RECURSIVE CHECKING THAT THEY DO IS NEVESSARY BECASUSE THE NV GOGGLES COULD BE LIKE 10 LAYERS DEEP IN THE BACKPACKS		
	void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer){
		//change this carrier's belong to field
		this.setCurrentHolder(pickUpPlayer);
		
		//recursively check contents
		for(Carryable eachInside: this.carriedItems){
			eachInside.checkIfPickingUpThisItemChangesPlayerState(pickUpPlayer);
		}
	}

	//if we dropped one of these carrier items, then we might have dropped e.g. NV goggles
	 void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer){
		
		 //change this carrier's belong to field
		 this.setCurrentHolder(null);
		 
		 //recursively check contents
		 for(Carryable eachInside: this.carriedItems){
				eachInside.checkIfDroppingThisItemChangesPlayerState(droppingPlayer);
			}
	 }
	
	//note that this is for creating the version of this carrier that will be drawn on the game board when it is dropped etc
	@Override
	abstract public RenderEntity generateDrawableCopy();
	
	//UTILITY///
	//USED BY THE PICK UP/DROP RECURSIVE METHODS IN CARRIER CLASSES SO THAT THEY CAN RECURSOVELY MAKE A CALL TO ALL OF THEIR COMPONENTS
	protected ArrayList<Carryable> getCarriedItems(){
		return this.carriedItems;
	}
	
	/////////////////DEBUG
	
	
	//USED TO TEST INVENTORY INVARIANTS
	private void testInvariant(){
		assert(this.carriedItems.size() <= this.inventoryCapacity):"INVENTORY STATE INVARIANT VIOLATED";
		assert(this.carriedItems.size() >= 0):"INVENTORY STATE INVARIANT VIOLATED";
		assert(this.carriedCount <= this.inventoryCapacity):"INVENTORY STATE INVARIANT VIOLATED";
		assert(this.carriedCount >= 0):"INVENTORY STATE INVARIANT VIOLATED";
		int i = 0;
		for(Carryable eachEnt: this.carriedItems){
			assert(eachEnt != null):"INVENTORY STATE INVARIANT VIOLATED";
			i++;
		}
		assert(i == this.inventoryCapacity):"INVENTORY STATE INVARIANT VIOLATED";
		
	}

	//utility////
	public void setUpOneLevel(Carrier parentCarrier){
		this.upOneLevel = parentCarrier;
	}
	
	public int getCapacity(){
		return inventoryCapacity;
	}
	
	public ArrayList<Carryable> getItems(){
		return carriedItems;
	}

	//used to get rid of a health kit in here when the player heals themselves
	public boolean attemptExpendHealthKit() {
		Iterator<Carryable> iter =  this.carriedItems.iterator();
		while(iter.hasNext()){
			if(iter.next() instanceof HealthKit){
				iter.remove();
				//we removed a health kit so we're done
				return true;
			}
		}
		//we couldnt find a health kit to use, so return false
		return false;
	}


}
