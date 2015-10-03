package storage;

import gamelogic.CardinalDirection;
import gamelogic.RoomState;
import gamelogic.entities.GameEntity;
import gamelogic.entities.NullEntity;
import gamelogic.entities.OuterWall;
import gamelogic.tiles.GameRoomTile;
import gamelogic.tiles.SpaceShipInteriorStandardTile;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * i guess this will be used for saving or s.t
 *
 */
public class GameStateWriter {

	public void marshall(){
		try {

			RoomState state = createRoom();

			JAXBContext jc = JAXBContext.newInstance(RoomState.class);
			Marshaller ms = jc.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  true);

			//ms.marshal(state, System.out);
			ms.marshal(state, new File("demo.xml"));


		} catch (Exception e){
			e.printStackTrace(System.out);;
		}
	}


	 public void unmarshall(){
		try{
			JAXBContext jc = JAXBContext.newInstance(RoomState.class);
			Unmarshaller ums = jc.createUnmarshaller();
			RoomState p = (RoomState)ums.unmarshal(new File("demo.xml"));
			for (int i = 0; i < p.getEntities().length; i++){
				for (int j = 0; j < p.getEntities()[i].length; j++){
					System.out.println(p.getEntities()[i][j].toString());
				}
			}
			for (int i = 0; i <p.getTiles().length; i++){
				for (int j = 0; j < p.getTiles()[i].length; j++){
					System.out.println(p.getTiles()[i][j].toString());
				}
			}

		}catch (JAXBException e){
			e.printStackTrace();
		}
	}


	public RoomState createRoom(){

		int width = 20;
		int height = 20;


		GameRoomTile[][] dummyTiles = new GameRoomTile[width][height];
		GameEntity[][] dummyEntities = new GameEntity[width][height];

		//fill up tha tiles
		for(int i = 0; i < width ; i++){
			for(int j = 0; j < height ; j++){
				dummyTiles[i][j] = new SpaceShipInteriorStandardTile();
			}
		}
		//fill up the entities with null
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height ; j++){


				dummyEntities[i][j] = new NullEntity(CardinalDirection.NORTH);//default for null entities is north

			}
		}

		//add the walls to edge locations
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				//insert walls at the top and bottom
				if( i == 0 || i == width - 1 || j == 0 || j == height - 1){
					//System.out.println("inserting a wall at: " + i + " " + j);
					dummyEntities[i][j] = new OuterWall(CardinalDirection.NORTH); //TODO: this should probably be set to something sensible. e.g. if directionFaced is SOUTH, then that wall looks like a "top of the map wall" from NORTH is up viewing perspective. if directionFaced is NORTH, then that wall looks like a bottom of the map wall from a NORTH is up viewing perspective.
				}
			}
		}


		RoomState DummyRoom1 = new RoomState(dummyTiles, dummyEntities, width, height, 3220);

		return DummyRoom1;
	}
}