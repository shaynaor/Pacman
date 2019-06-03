package Gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Algo.AutoPlay;
import Coords.Convert_pixel_gps;
import GIS.Box;
import GIS.Corner;
import GIS.Fruit;
import GIS.Game;
import GIS.Ghost;
import GIS.Pacman;
import GIS.Player;
import Geom.Pixel;
import Geom.Point3D;
import Robot.Play;
import mySQL.EndGameStatistics;

/**
 * This class is the main class of the program. it will create the gui with all
 * the option written in the slider menu on the top left. depending on what the
 * user chooses in the gui the class will start a function .
 * 
 * @author Alex vaisman, Shay naor
 *
 */
public class MyFrame extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private BufferedImage myImage, ghostImage, fruitImage, playerImage, pacmanImage;
	private Game game;
	private boolean isGameLoaded;
	private boolean isPlayer;
	private Play play1;

	public MyFrame() {

		/* INIT myImge filed */
		try {
			this.myImage = ImageIO.read(new File("images\\Ariel1.png"));
		} catch (IOException e) {
			System.err.println("ERROR: incorrect path for picture!");
			e.printStackTrace();
		}
		this.setTitle("Pacman");
		isGameLoaded = false;

		this.addMouseListener(this);

		initGUI();
	}

	/**
	 * This function creates all the menu bars and options in the gui. It also has
	 * in it functions which listen to which option was selected. If and option was
	 * selected the corresponding function will start.
	 */
	private void initGUI() {
		/* Create the menu bar. */
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		MenuItem loadFile = new MenuItem("Load");
		MenuItem clearFile = new MenuItem("Clear");
		MenuItem exitFile = new MenuItem("Exit");

		fileMenu.add(loadFile);
		fileMenu.add(clearFile);
		fileMenu.add(exitFile);

		menuBar.add(fileMenu);

		Menu inputMenu = new Menu("Input");
		MenuItem playerInput = new MenuItem("Player");

		inputMenu.add(playerInput);

		menuBar.add(inputMenu);

		Menu playMenu = new Menu("Play");
		MenuItem startPlay = new MenuItem("Start");
		MenuItem startSim = new MenuItem("Start Auto");

		playMenu.add(startPlay);
		playMenu.add(startSim);

		menuBar.add(playMenu);

		setMenuBar(menuBar);

		/* End to create the menu bar. */

		/*
		 * Add action to load File button
		 * https://stackoverflow.com/questions/15703214/save-file-open-file-dialog-box-
		 * using-swing-netbeans-gui-editor
		 */
		loadFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				ChooseButtonLoadFile();
			}
		});

		/* Add action to clear File button */
		clearFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isGameLoaded) {
					game.clearGame();
					repaint();
					isGameLoaded = false;

				}
			}
		});

		/* Add action to exit File button */
		exitFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		/* Add action to player input button */
		playerInput.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isGameLoaded) {
					isPlayer = true;
				}
			}
		});

		/* Add action to start play button */
		startPlay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isGameLoaded) {
					ChooseButtonStartPlay();
				}
			}
		});

		/* Add action to startSim play auto button */
		startSim.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isGameLoaded) {
					ChooseButtonStartSim();
				}
			}
		});

	}

	/**
	 * This function starts a simulated game.
	 * It will create an AutoPlay object which will calculate the next step of the pacman.
	 * it will start a thread which will call AutoPlay it will then update the player
	 * orientation then it will update the game and call repaint.
	 * and the thread will sleep for 100ms.
	 * if the game ends it will call EndGameStatistics class which will print end game Statistics.
	 */
	private void ChooseButtonStartSim() {
		AutoPlay start = new AutoPlay(this.game, this.play1);
		play1.start();
		repaint();
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				int mapId = 0;
				int counter = 0;
				while (play1.isRuning() && isGameLoaded) {
					start.UpdateAlgo(game, play1);
					if (counter == 0) {
						mapId = play1.getHash1();
						counter++;
					}
					play1.rotate(game.getPlayers().get(0).getOrientation());
					ArrayList<String> board_data = play1.getBoard();
					game.updateTheGame(board_data);

					repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!play1.isRuning()) {
					EndGameStatistics test = new EndGameStatistics(play1.getStatistics(), mapId);
					printEndGameStatistics(test);
				}
			}

		});
		t2.start();

	}

	
	/**
	 * This function prints end game statistics.
	 * @param test , the object that hold the relevant statistics.
	 */
	private void printEndGameStatistics(EndGameStatistics test) {

		System.out.println("\n\n\n\n\n");

		System.out.println(
				"******************************************Game-Over******************************************");
		System.out.println();
		System.out.println("\t\t\t\tYour score is: " + test.getMyScore());
		System.out.println("\t\t\t\tYour position is: " + test.getMyPosition());
		System.out.println("\t\t\t\tThe best score on this map is: " + test.getFirstPlace());
		System.out.println("\t\t\t\tThe second best score is: " + test.getSecondPlace());
		System.out.println("\t\t\t\tThe third best score is: " + test.getThirdPlace());

		System.out.println("\n");

		System.out.println("\t\t\t\tYour map id: " + test.getMapId());

	}

	/**
	 * This function loads a csv file and creates a new game.
	 */
	private void ChooseButtonLoadFile() {
		/* Open load file chooser */
		JFileChooser openFile = new JFileChooser();
		int returnValue = openFile.showOpenDialog(null);

		/* If the file selected */
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = openFile.getSelectedFile();// the file that selected.
			String filePath = selectedFile.getPath();// the path to the file.

			/* If the file that selected is csv file */
			if (filePath.contains(".csv")) {

				/* Load the new game to the game board */
				play1 = new Play(filePath);
				play1.setIDs(206125767, 317524445);

				String map_data = play1.getBoundingBox();
				ArrayList<String> board_data = play1.getBoard();
				this.game = new Game(board_data, map_data);

				/* Ghost GUI image */
				if (this.game.getGhosts().size() > 0) {
					ghostImage = this.game.getGhosts().get(0).getMyImage();
				}

				/* Fruit GUI image */
				if (this.game.getFruits().size() > 0) {
					fruitImage = this.game.getFruits().get(0).getMyImage();
				}

				/* Player GUI image */
				if (this.game.getPlayers().size() > 0) {
					playerImage = this.game.getPlayers().get(0).getMyImage();
				}

				/* Pacman GUI image */
				if (this.game.getPacmans().size() > 0) {
					pacmanImage = this.game.getPacmans().get(0).getMyImage();
				}

				isGameLoaded = true;
				repaint();
			}
		}

	}

	/**
	 * This function starts a manual game.
	 * it will start a thread which will update the game every 100ms.
	 * and it will update the player orientation according to what is saved in the myFrame.
	 * then it will call repaint.
	 * and the thread will sleep for 100ms.
	 * if the game ends it will call EndGameStatistics class which will print end game Statistics.
	 */
	private void ChooseButtonStartPlay() {

		play1.start();
		repaint();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				int mapId = 0;
				int counter = 0;
				while (play1.isRuning() && isGameLoaded) {
					if (counter == 0) {
						mapId = play1.getHash1();
						counter++;
					}
					play1.rotate(game.getPlayers().get(0).getOrientation());
					ArrayList<String> board_data = play1.getBoard();
					game.updateTheGame(board_data);

					repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!play1.isRuning()) {
					EndGameStatistics test = new EndGameStatistics(play1.getStatistics(), mapId);
					printEndGameStatistics(test);
				}
			}
		});
		t1.start();
	}

	/**
	 * This function will the objects on the map.
	 * it will paint the object only if the gameIsLoaded == true.
	 */
	public void paint(Graphics g) {

		Image image = createImage(5000, 5000);
		Graphics g1 = image.getGraphics();

		g1.drawImage(this.myImage, 0, 50, this.getWidth() - 7, this.getHeight() - 58, this);

		if (isGameLoaded) {
			this.game.getMap().setHeight(this.getHeight());
			this.game.getMap().setWidth(this.getWidth());

			/* Draw pacmans */
			Iterator<Pacman> pacIt = this.game.getPacmans().iterator();
			Convert_pixel_gps convert = new Convert_pixel_gps(this.game.getMap());

			while (pacIt.hasNext()) {
				Pacman pac = pacIt.next();
				Pixel pixel = convert.convertGPStoPixel(pac.getGps());
				g1.drawImage(this.pacmanImage, pixel.getX(), pixel.getY(), null);
			}

			/* Draw fruits */
			Iterator<Fruit> fruitIt = this.game.getFruits().iterator();
			synchronized (getFrames()) {
				while (fruitIt.hasNext()) {
					synchronized (getFrames()) {
						Fruit fruit = fruitIt.next();
						Pixel pixel = convert.convertGPStoPixel(fruit.getGps());
						g1.drawImage(this.fruitImage, pixel.getX(), pixel.getY() + 10, null);
					}
				}
			}

			/* Draw ghosts */
			Iterator<Ghost> ghostIt = this.game.getGhosts().iterator();
			while (ghostIt.hasNext()) {
				Ghost ghost = ghostIt.next();
				Pixel pixel = convert.convertGPStoPixel(ghost.getGps());
				g1.drawImage(this.ghostImage, pixel.getX(), pixel.getY(), null);
			}

			/* Draw boxes */
			Iterator<Box> boxIt = this.game.getBoxes().iterator();

			while (boxIt.hasNext()) {
				Box box = boxIt.next();
				Pixel pixelBotLeft = convert.convertGPStoPixel(box.getBotLeft());
				Pixel pixelTopRight = convert.convertGPStoPixel(box.getTopRight());

				int width = pixelTopRight.getX() - pixelBotLeft.getX();
				int height = pixelBotLeft.getY() - pixelTopRight.getY();

				Pixel pixelTopLeft = convert.convertGPStoPixel(box.getTopLeft());

				g1.setColor(Color.BLACK);
				g1.fillRect(pixelTopLeft.getX(), pixelTopLeft.getY() + 35, width, height);

			}

			/* Draw player */
			Iterator<Player> playerIt = this.game.getPlayers().iterator();

			while (playerIt.hasNext()) {
				Player player = playerIt.next();
				if (convert.isIn(player.getGps())) {
					Pixel pixel = convert.convertGPStoPixel(player.getGps());
					g1.drawImage(this.playerImage, pixel.getX() - 20, pixel.getY() - 15, null);
				}
			}
			System.out.println(play1.getStatistics());
		}
		g.drawImage(image, 0, 0, this);

	}

	
	
	public void mouseClicked(MouseEvent e) {

		if (isPlayer && isGameLoaded) {
			int x = e.getX() - 15;
			int y = e.getY() - 30;
			Pixel pixel = new Pixel(x, y);
			Convert_pixel_gps convert = new Convert_pixel_gps(this.game.getMap());
			Point3D gps = new Point3D(convert.convertPixeltoGPS(pixel));
			this.play1.setInitLocation(gps.x(), gps.y());
			ArrayList<String> board_data = play1.getBoard();
			game.updateTheGame(board_data);
			repaint();
			isPlayer = false;

		}

		if (isGameLoaded && !game.getPlayers().isEmpty()) {
			int x = e.getX();
			int y = e.getY();
			Pixel pixDirection = new Pixel(x, y);
			Convert_pixel_gps convert = new Convert_pixel_gps(this.game.getMap());
			Point3D gpsDirection = new Point3D(convert.convertPixeltoGPS(pixDirection));
			game.getPlayers().get(0).findOrientation(gpsDirection);
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public BufferedImage getMyImage() {
		return myImage;
	}

	public Game getGame() {
		return this.game;
	}

}
