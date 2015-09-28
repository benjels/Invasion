package imagehelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This class deals with images for the entire game. It sets/stores the images
 * path so then the other classes can make a new images and gets the required
 * image
 *
 * @author Ahmed-laptop
 *
 */
public class Imagehelper {
	// Characters cards
	private ImageIcon alex = loadImage("Alex",
			"resources/Cards/Characters/Alex.jpg");
	private ImageIcon henry = loadImage("Henry",
			"resources/Cards/Characters/Henry.jpg");
	private ImageIcon eileen = loadImage("Eileen",
			"resources/Cards/Characters/Eileen.jpg");
	private ImageIcon harry = loadImage("Harry",
			"resources/Cards/Characters/Harry.jpg");
	private ImageIcon cybil = loadImage("Cybil",
			"resources/Cards/Characters/Cybil.jpg");
	private ImageIcon heather = loadImage("Heather",
			"resources/Cards/Characters/Heather.jpg");
	private ImageIcon maria = loadImage("Maria",
			"resources/Cards/Characters/Maria.jpg");
	private ImageIcon james = loadImage("James",
			"resources/Cards/Characters/James.jpg");
	// Weapon cards
	private ImageIcon shotgun = loadImage("Shotgun",
			"resources/Cards/Weapons/Shotgun.jpg");
	private ImageIcon chainsaw = loadImage("Chainsaw",
			"resources/Cards/Weapons/Chainsaw.jpg");
	private ImageIcon beamSaber = loadImage("Beam Saber",
			"resources/Cards/Weapons/BeamSaber.jpg");
	private ImageIcon steelPipe = loadImage("Steel Pipe",
			"resources/Cards/Weapons/SteelPipe.jpg");
	private ImageIcon axe = loadImage("Axe", "resources/Cards/Weapons/Axe.jpg");
	private ImageIcon pistol = loadImage("Pistol",
			"resources/Cards/Weapons/Pistol.jpg");
	// Rooms cards
	private ImageIcon happyBurger = loadImage("Happy Burger",
			"resources/Cards/Rooms/HappyBurger.png");
	private ImageIcon lakeviewHotel = loadImage("Lakeview Hotel",
			"resources/Cards/Rooms/LakeviewHotel.jpg");
	private ImageIcon lakesideAmusementPark = loadImage(
			"Lakeside Amusement Park",
			"resources/Cards/Rooms/LakesideAmusementPark.jpg");
	private ImageIcon rosewaterPark = loadImage("RosewaterPark",
			"resources/Cards/Rooms/RosewaterPark.jpg");
	private ImageIcon brookHavenHospital = loadImage("BrookHaven Hospital",
			"resources/Cards/Rooms/BrookHavenHospital.png");
	private ImageIcon cafe5to2 = loadImage("Cafe 5to2",
			"resources/Cards/Rooms/Cafe5to2.jpg");
	private ImageIcon heavensNight = loadImage("Heavens Night",
			"resources/Cards/Rooms/HeavensNight.png");
	private ImageIcon room302 = loadImage("Room 302",
			"resources/Cards/Rooms/Room302.png");
	private ImageIcon tolucaPrison = loadImage("Toluca Prison",
			"resources/Cards/Rooms/TolucaPrison.jpg");
	// Player's token
	private ImageIcon player1 = loadImage("resources/Tokens/player1.jpg");
	private ImageIcon player2 = loadImage("resources/Tokens/player2.jpg");
	private ImageIcon player3 = loadImage("resources/Tokens/player3.jpg");
	private ImageIcon player4 = loadImage("resources/Tokens/player4.jpg");
	private ImageIcon player5 = loadImage("resources/Tokens/player5.jpg");
	private ImageIcon player6 = loadImage("resources/Tokens/player6.jpg");
	// Board
	private ImageIcon board = loadImage("resources/SilentHillBoard.png");
	// player's hand background
	private ImageIcon handBackground = loadImage("resources/Backgrounds/hand_background.jpg");

	// Dice
	private ImageIcon dice1 = loadImage("resources/Dices/1.png");
	private ImageIcon dice2 = loadImage("resources/Dices/2.png");
	private ImageIcon dice3 = loadImage("resources/Dices/3.png");
	private ImageIcon dice4 = loadImage("resources/Dices/4.png");
	private ImageIcon dice5 = loadImage("resources/Dices/5.png");
	private ImageIcon dice6 = loadImage("resources/Dices/6.png");

	private ImageIcon[] dices = new ImageIcon[] { dice1, dice2, dice3, dice4,
			dice5, dice6 };
	// Cluedo Gui Background
	private ImageIcon guiBackground = loadImage("resources/startingHand.jpg");

	public ImageIcon getGuiBackground2() {
		return guiBackground;
	}

	/**
	 * Loads the images with the specified directory and with the given type of
	 * the card This used the card type of images so than it can be retrieved
	 * and draw it on the canvas (for getting the right image)
	 *
	 * @param cardname
	 *            the card that is needed to store
	 * @param filename
	 *            the path to store that card
	 * @return image icon object
	 */
	private ImageIcon loadImage(String cardname, String filename) {
		ImageIcon image = new ImageIcon(filename);
		image.setDescription(cardname);
		return image;
	}

	/**
	 * Loads the images with the specified directory without specifying the type
	 * of the card This used for other images that are not cards
	 *
	 * @param filename
	 * @return
	 */
	private ImageIcon loadImage(String filename) {
		ImageIcon image = new ImageIcon(filename);
		return image;
	}

	public ImageIcon getAlex() {
		return alex;
	}

	public ImageIcon getHenry() {
		return henry;
	}

	public ImageIcon getEileen() {
		return eileen;
	}

	public ImageIcon getHarry() {
		return harry;
	}

	public ImageIcon getCybil() {
		return cybil;
	}

	public ImageIcon getHeather() {
		return heather;
	}

	public ImageIcon getMaria() {
		return maria;
	}

	public ImageIcon getJames() {
		return james;
	}

	public ImageIcon getShotgun() {
		return shotgun;
	}

	public ImageIcon getChainsaw() {
		return chainsaw;
	}

	public ImageIcon getBeamSaber() {
		return beamSaber;
	}

	public ImageIcon getSteelPipe() {
		return steelPipe;
	}

	public ImageIcon getAxe() {
		return axe;
	}

	public ImageIcon getPistol() {
		return pistol;
	}

	public ImageIcon getHappyBurger() {
		return happyBurger;
	}

	public ImageIcon getLakeviewHotel() {
		return lakeviewHotel;
	}

	public ImageIcon getLakesideAmusementPark() {
		return lakesideAmusementPark;
	}

	public ImageIcon getRosewaterPark() {
		return rosewaterPark;
	}

	public ImageIcon getBrookHavenHospital() {
		return brookHavenHospital;
	}

	public ImageIcon getCafe5to2() {
		return cafe5to2;
	}

	public ImageIcon getHeavensNight() {
		return heavensNight;
	}

	public ImageIcon getRoom302() {
		return room302;
	}

	public ImageIcon getTolucaPrison() {
		return tolucaPrison;
	}

	public ImageIcon getPlayer1() {
		return player1;
	}

	public ImageIcon getPlayer2() {
		return player2;
	}

	public ImageIcon getPlayer3() {
		return player3;
	}

	public ImageIcon getPlayer4() {
		return player4;
	}

	public ImageIcon getPlayer5() {
		return player5;
	}

	public ImageIcon getPlayer6() {
		return player6;
	}

	public ImageIcon getBoard() {
		return board;
	}

	public void setBoard(ImageIcon board) {
		this.board = board;
	}

	public ImageIcon getHandBackground() {
		return handBackground;
	}

	public ImageIcon[] getDices() {
		return dices;
	}

	public ImageIcon getPlayerToken(int number) {
		// System.out.println("inside getPlayerToken");
		// System.out.println("number = "+number);
		switch (number) {
		case 1:
			number = 1;
			return this.getPlayer1();
		case 2:
			number = 2;
			return this.getPlayer2();
		case 3:
			number = 3;
			return this.getPlayer3();
		case 4:
			number = 4;
			return this.getPlayer4();
		case 5:
			number = 5;
			return this.getPlayer5();
		case 6:
			number = 6;
			return this.getPlayer6();
		default:
			break;
		}
		return this.getPlayer6();
	}

	/**
	 * Get the images to draw from player's hand
	 *
	 * @param imagehelper
	 *            the image resources
	 * @param imageName
	 *            the image name to get back the correct image
	 * @return image to use for drawing
	 */
	public Image getImageCards(Imagehelper imagehelper, String imageName) {
		// // Characters
		if (imagehelper.getHenry().toString() == imageName) {
			return getHenry().getImage();
		} else if (imagehelper.getEileen().toString() == imageName) {
			return getEileen().getImage();
		} else if (imagehelper.getHarry().toString() == imageName) {
			return getHarry().getImage();
		} else if (imagehelper.getCybil().toString() == imageName) {
			return getCybil().getImage();
		} else if (imagehelper.getHeather().toString() == imageName) {
			return getHeather().getImage();
		} else if (imagehelper.getMaria().toString() == imageName) {
			return getMaria().getImage();
		}
		// // Weapons
		else if (imagehelper.getAxe().toString() == imageName) {
			return getAlex().getImage();
		} else if (imagehelper.getShotgun().toString() == imageName) {
			return getShotgun().getImage();
		} else if (imagehelper.getChainsaw().toString() == imageName) {
			return getChainsaw().getImage();
		} else if (imagehelper.getBeamSaber().toString() == imageName) {
			return getBeamSaber().getImage();
		} else if (imagehelper.getSteelPipe().toString() == imageName) {
			return getSteelPipe().getImage();
		} else if (imagehelper.getPistol().toString() == imageName) {
			return getPistol().getImage();
		}
		// Rooms
		else if (imagehelper.getCybil().toString() == imageName) {
			return getCybil().getImage();
		} else if (imagehelper.getHappyBurger().toString() == imageName) {
			return getHappyBurger().getImage();
		} else if (imagehelper.getLakeviewHotel().toString() == imageName) {
			return getLakeviewHotel().getImage();
		} else if (imagehelper.getLakesideAmusementPark().toString() == imageName) {
			return getLakesideAmusementPark().getImage();
		} else if (imagehelper.getRosewaterPark().toString() == imageName) {
			return getRosewaterPark().getImage();
		} else if (imagehelper.getBrookHavenHospital().toString() == imageName) {
			return getBrookHavenHospital().getImage();
		}

		else if (imagehelper.getCafe5to2().toString() == imageName) {
			return getCafe5to2().getImage();
		}

		else if (imagehelper.getHeavensNight().toString() == imageName) {
			return getHeavensNight().getImage();
		}

		else if (imagehelper.getRoom302().toString() == imageName) {
			return getRoom302().getImage();
		}

		else if (imagehelper.getTolucaPrison().toString() == imageName) {
			return getRoom302().getImage();
		}

		return null;
	}

	/**
	 * getting the image icon for the characters
	 *
	 * @param playerImage
	 *            the player name for the image
	 * @param CharName
	 *            comparing with the player name to get the correct image
	 * @return the image that is used to draw in the canvas
	 */
	public ImageIcon getPlayerImage(Imagehelper playerImage, String CharName) {

		if (playerImage.getHenry().toString() == CharName) {
			return getHenry();
		} else if (playerImage.getEileen().toString() == CharName) {
			return getEileen();
		} else if (playerImage.getHarry().toString() == CharName) {
			return getHarry();
		} else if (playerImage.getCybil().toString() == CharName) {
			return getCybil();
		} else if (playerImage.getHeather().toString() == CharName) {
			return getHeather();
		} else if (playerImage.getMaria().toString() == CharName) {
			return getMaria();
		}
		return null;
	}

	// how to load image
	public static Image loadImage2(String filename) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/" + filename));
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}
}