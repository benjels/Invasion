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




/**
 * a carrier that a player can use to store Carryables in.
 * @author brownmax1
 *
 */

public abstract class Carrier extends Carryable{
	private final ArrayList<Carryable> carriedItems;
	private int selectedIndex = 0; //by default the selected index is 0 (the left most one)
	private final int inventoryCapacity; //the amount of slots
	private final int slotSize; //the size that each slot can accomodate. things we are putting in the bag must be smaller than or equal to this
	private int carriedCount; //amount of actual items held
	private Carrier upOneLevel = this; //the carrier that contains this one. NOTE: all initially set to point at self, but when it is picked up, it gets set to whatever carrier it is placed in

	public Carrier(CardinalDirection directionFaced, int inventoryCapacity, int slotSize, int sizeRequired){
		super(directionFaced, sizeRequired);
		this.carriedItems = new ArrayList<Carryable>(0);
		this.inventoryCapacity = inventoryCapacity;
		this.slotSize = slotSize;
		for(int i = 0; i < this.inventoryCapacity; i ++){//fill up the inventory with null entities so that when we drop something something into a room we are never dropping a true null
			this.carriedItems.add(new NullEntity(CardinalDirection.NORTH));
		}
	}

	/**
	 * attempts to place an item that the character wanted to pick up into their inventory. First trys to place it in the
	 * currently selected slot. If that is full, trys to place it in the first available slot. If there are no slots available in this inventory, does nothing
	 * @param pickUp the item that we are trying to place in the inventory
	 * @return bool true if the item is placed in the inventory, else false
	 */
	public boolean pickUpItem(Carryable pickUp){


		//cannot carry more than capacity AND cannot put things in this inventory that are larger than the slot size
		if((this.carriedCount == this.inventoryCapacity && !(pickUp instanceof NullEntity)) || pickUp.getSize() > this.slotSize){

			return false;
		}
		//if the selected slot is free, put item there
		if(this.carriedItems.get(this.getSelectedIndex()) instanceof NullEntity ){
			this.carriedItems.set(this.getSelectedIndex(), pickUp);
			//we picked something up so increase our count of carried items
			if(!(pickUp instanceof NullEntity)){
				this.carriedCount ++;
				//if we picked up another carrier, set its upOneLevel to this
				if(pickUp instanceof Carrier){
					((Carrier) pickUp).setUpOneLevel(this);
				}
			}


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
					pickUp.checkIfPickingUpThisItemChangesPlayerState(this.getCurrentHolder());//recursively descends the "tree" of Carryables starting from this one checking if one of the Carryables taht we are picking up will change the player's status in some way s
					return true;
				}
			}
		}

		//in case that there are no free slots in the inventory
		return false;

	}



	/**
	 * attempts to drop the item in the currently selected slot back into the cached entities array where the player is currently standing in a room. If the selected slot is empty, the NullEntity
	 * in that slot is returned back to the cached entities and effectively nothing happens
	 * @return Carryable the item that is in the selected slot in the inventory that we are trying to "drop"
	 */
	public Carryable dropItem(){
		assert(this.getCurrentHolder() != null):"i need to be attached to someone!";
		Carryable drop =  this.carriedItems.get(this.getSelectedIndex());

		//check if dropping the item changed the player's status
		drop.checkIfDroppingThisItemChangesPlayerState(this.getCurrentHolder());//recursively descends the "tree" of Carryables starting from this one checking if dropping one of those carryables changes the player's status

		//dropped the item, so it no longer belongs to this player
		drop.setCurrentHolder(null);

		//"remove" item from inventory by replacing its slot with null entity
		this.carriedItems.set(this.getSelectedIndex(), new NullEntity(CardinalDirection.NORTH));
		//decrement our count of items because if dropped one
		if(!(drop instanceof NullEntity)){
			this.carriedCount --;
			//if we dropped a carrier, its up one level is no longer this carrier
			if(drop instanceof Carrier){
				((Carrier) drop).setUpOneLevel((Carrier) drop); //peep the double cast
			}
		}


		//return the "dropped" items to be placed back into the entities cache
		return drop;
	}







	/**
	 * creates a drawable version of this inventory to be sent to the game ui
	 * @return
	 */
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




	/**
	 * attempts to select an inventory slot
	 * @param eventWeNeedToUpdateStateWith the selection event
	 * @return true if the slot was selected
	 */
	public boolean attemptInventorySelectionEventByPlayer(InventorySelectionEvent eventWeNeedToUpdateStateWith) {
		assert(eventWeNeedToUpdateStateWith.getSlot() <= this.inventoryCapacity):"YOU TRIED TO ACCESS AN INVENTORY SLOT WHICH THIS CARRIER IS TOO SMALL FOR";
		//set the player's attempted selection
		this.selectedIndex = eventWeNeedToUpdateStateWith.getSlot() - 1;
		//we set it so return true
		return true;
	}

	/**
	 * used to go up one level or down one level in the carrier tree
	 * @param eventWeNeedToUpdateStateWith the open or close carrier event
	 * @return true if we the event applied successfully
	 */
	public boolean attemptSwitchCurrentInventoryEventByPlayer(CarrierOpenCloseEvent eventWeNeedToUpdateStateWith) {

		if(eventWeNeedToUpdateStateWith instanceof CarrierOpenEvent){
			//check that selected index is a carrier
			if(this.carriedItems.get(this.getSelectedIndex()) instanceof Carrier){
				this.getCurrentHolder().setCurrentInventory((Carrier) this.carriedItems.get(this.getSelectedIndex()));
				return true;
			}else{
				return false;
			}
		}else if(eventWeNeedToUpdateStateWith instanceof CarrierCloseEvent){
			this.getCurrentHolder().setCurrentInventory(this.upOneLevel);

			return true;
			}
		else{

			return false;
		}
	}



	/**
	 * if we piced up a carrier, we need to recursively check all of the items that it contains to see whether they change the player state
	 */
	protected void checkIfPickingUpThisItemChangesPlayerState(Player pickUpPlayer){
		//change this carrier's belong to field
		this.setCurrentHolder(pickUpPlayer);

		//recursively check contents
		for(Carryable eachInside: this.carriedItems){
			eachInside.checkIfPickingUpThisItemChangesPlayerState(pickUpPlayer);
		}
	}

	/**
	 * if we dropped a carrier, we need to recursively check all of the items to see whether they changed the player state
	 */
	protected void checkIfDroppingThisItemChangesPlayerState(Player droppingPlayer){

		 //change this carrier's belong to field
		 this.setCurrentHolder(null);

		 //recursively check contents
		 for(Carryable eachInside: this.carriedItems){
				eachInside.checkIfDroppingThisItemChangesPlayerState(droppingPlayer);
			}
	 }

	/**
	 * used to get rid of a health kit when the player uses one
	 * @return
	 */
	protected boolean attemptExpendHealthKit() {
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


	@Override
	abstract public RenderEntity generateDrawableCopy();



	//UTILITY///
	//USED BY THE PICK UP/DROP RECURSIVE METHODS IN CARRIER CLASSES SO THAT THEY CAN RECURSOVELY MAKE A CALL TO ALL OF THEIR COMPONENTS
	protected ArrayList<Carryable> getCarriedItems(){
		return this.carriedItems;
	}




/*	//USED TO TEST INVENTORY INVARIANTS
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
*/
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



	public int getSelectedIndex() {
		return selectedIndex;
	}




}
