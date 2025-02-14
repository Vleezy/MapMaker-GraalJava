package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;

import gfx.Assets;
import gfx.Sound;
import input.Selector;
import main.Handler;
import worlds.World;

import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;

public class MenuState extends State
{
	private Selector selector;
	private World world;
	private boolean inMainMenu;
	private boolean inControlsMenu;
	private boolean inOptionsMenu;
	private static Font headerFont = new Font("Arial", Font.BOLD, 30);
	private static Font menuFont = new Font("Arial",Font.PLAIN, 20);
	private static Color DARKRED = new Color(204,0,0);
	private static Color DARKGREEN = new Color(0,204,0);

	//scrolling song title
	private int currentSongX;
	private int counter;
	
	public MenuState(Handler handler)
	{
		super(handler);
		//getting the original world
		this.world = handler.getWorld();
		this.selector = new Selector(handler, world.getSpawnX(), world.getSpawnY());
		this.inMainMenu = true;
		this.currentSongX = 0;
		this.counter = 0;
	}

	@Override
	public void tick() throws IOException
	{
		selector.tick();
		checkClickedButtons();
	}

	@Override
	public void render(Graphics g)
	{
		if(inMainMenu)
			mainMenu(g);
	    
	    if(inControlsMenu)
	    	controlsMenu(g);
	    if(inOptionsMenu)
	    	optionsMenu(g);
	}
	
	//checks if the buttons are clicked on
	public void checkClickedButtons() throws IOException
	{
		//CHECKING MOUSE CLICK AREAS
		//if its not in controls or options menu.
		if(inMainMenu)
		{
			//CONTROLS BUTTON
			if((handler.getKeyManager().cX >= 10 && handler.getKeyManager().cX <= 210) && (handler.getKeyManager().cY >= 295 && handler.getKeyManager().cY <= 345))
			{
				System.out.println("Controls button clicked, going into controls menu");
				inMainMenu = false;
				inControlsMenu = true;
			}
			//OPTIONS BUTTON
			if((handler.getKeyManager().cX >= 430 && handler.getKeyManager().cX <= 630) && (handler.getKeyManager().cY >= 295 && handler.getKeyManager().cY <= 345))
			{
				System.out.println("Options button clicked, going into options menu");
				inMainMenu = false;
				inOptionsMenu = true;
			}
			//NEW WORLD BUTTON
			if((handler.getKeyManager().cX >= 220 && handler.getKeyManager().cX <= 420) && (handler.getKeyManager().cY >= 70 && handler.getKeyManager().cY <= 120))
			{
				System.out.println("New World button clicked, creating a new world.");
				handler.getWorld().newWorld();
			}
			//LOAD WORLD BUTTON
			if((handler.getKeyManager().cX >= 220 && handler.getKeyManager().cX <= 420) && (handler.getKeyManager().cY >= 145 && handler.getKeyManager().cY <= 195))
			{
				System.out.println("Load World clicked, loading a world.");
				handler.getWorld().loadNewWorld();
			}
			//SAVE WORLD BUTTON
			if((handler.getKeyManager().cX >= 220 && handler.getKeyManager().cX <= 420) && (handler.getKeyManager().cY >= 220 && handler.getKeyManager().cY <= 270))
			{
				System.out.println("Save World clicked, saving the world.");
				handler.getWorld().saveWorld();
			}
			//IMPORT TILE BUTTON
			if((handler.getKeyManager().cX >= 10 && handler.getKeyManager().cX <= 210) && (handler.getKeyManager().cY >= 220 && handler.getKeyManager().cY <= 270))
			{
				System.out.println("Import Tiles clicked, importing tiles.");
				handler.getMapMakerCamera().setZoomLevel(3);
				Assets.importTiles();
				State.setState(handler.getMapMaker().mapMakerState);
			}
			//EXIT BUTTON
			if((handler.getKeyManager().cX >= 220 && handler.getKeyManager().cX <= 420) && (handler.getKeyManager().cY >= 295 && handler.getKeyManager().cY <= 345))
			{	
				System.out.println("EXITING.");
				System.exit(1);
			}
			//BACK BUTTON
			if((handler.getKeyManager().cX >= 475 && handler.getKeyManager().cX <= 625) && (handler.getKeyManager().cY >= 20 && handler.getKeyManager().cY <= 70))
			{	
				System.out.println("Going back to Map Maker");
				State.setState(handler.getMapMaker().mapMakerState);
			}
		}
		//if its in a different menu area
		else
		{
			if(inControlsMenu)
			{
				//BACK BUTTON
				if(State.getState() == handler.getMapMaker().menuState)
				{
					//220,70,400,50
					if((handler.getKeyManager().cX >= 475 && handler.getKeyManager().cX <= 625) && (handler.getKeyManager().cY >= 20 && handler.getKeyManager().cY <= 70))
					{	
						System.out.println("Going back to the Main Menu");
						this.inControlsMenu = false;
						this.inMainMenu = true;
					}
				}
			}
			if(inOptionsMenu)
			{
				//BACK BUTTON
				if(State.getState() == handler.getMapMaker().menuState)
				{
					//220,70,400,50
					if((handler.getKeyManager().cX >= 475 && handler.getKeyManager().cX <= 625) && (handler.getKeyManager().cY >= 20 && handler.getKeyManager().cY <= 70))
					{	
						System.out.println("Going back to the Main Menu");
						this.inOptionsMenu = false;
						this.inMainMenu = true;
					}
				}
				//SLIDERS
				//MUSIC
				//MUTE
				if((handler.getKeyManager().cX >=50 && handler.getKeyManager().cX <= 100) && (handler.getKeyManager().cY >= 100 && handler.getKeyManager().cY <= 150))
				{	
					System.out.println("Muting Music.");
					Sound.muteMusic();
				}
				//LOWER
				if((handler.getKeyManager().cX >= 110 && handler.getKeyManager().cX <= 160) && (handler.getKeyManager().cY >= 100 && handler.getKeyManager().cY <= 150))
				{	
					System.out.println("Lowering Music Volume.");
					Sound.setMusicVolume(Sound.musicVolumeChange-1.0f);
				}
				//RAISE
				if((handler.getKeyManager().cX >= 380 && handler.getKeyManager().cX <= 430) && (handler.getKeyManager().cY >= 100 && handler.getKeyManager().cY <= 150))
				{	
					System.out.println("Raising Music Volume.");
					Sound.setMusicVolume(Sound.musicVolumeChange+1.0f);
				}
				//NEXT SONG
				if((handler.getKeyManager().cX >= 440 && handler.getKeyManager().cX <= 490) && (handler.getKeyManager().cY >= 100 && handler.getKeyManager().cY <= 150))
				{
					System.out.println("Changing song to next song.");
					Sound.playNextSong();
				}
				//SFX
				//MUTE
				if((handler.getKeyManager().cX >=50 && handler.getKeyManager().cX <= 100) && (handler.getKeyManager().cY >= 200 && handler.getKeyManager().cY <= 250))
				{	
					System.out.println("Muting SFX.");
					Sound.muteSfx();
				}
				//LOWER
				if((handler.getKeyManager().cX >= 110 && handler.getKeyManager().cX <= 160) && (handler.getKeyManager().cY >= 200 && handler.getKeyManager().cY <= 250))
				{	
					System.out.println("Lowering SFX Volume.");
					Sound.setSfxVolume(Sound.sfxVolumeChange-1.0f);
				}
				//RAISE
				if((handler.getKeyManager().cX >= 380 && handler.getKeyManager().cX <= 430) && (handler.getKeyManager().cY >= 200 && handler.getKeyManager().cY <= 250))
				{	
					System.out.println("Raising SFX Volume.");
					Sound.setSfxVolume(Sound.sfxVolumeChange+1.0f);
				}
				
			}
		}
		handler.getKeyManager().cX = -1;
		handler.getKeyManager().cY = -1;
	}
	
	public void mainMenu(Graphics g)
	{
		//Drawing background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 1080);
		//MENU text
		g.setColor(Color.RED);
	    g.setFont(headerFont);
	    g.drawString("MENU", 280, 40);
	    
	    g.setFont(menuFont);
	    
		//CONTROLS button
	    if((handler.getKeyManager().mX >= 10 && handler.getKeyManager().mX <= 210) && (handler.getKeyManager().mY >= 295 && handler.getKeyManager().mY <= 345))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(10,295,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("Controls", 75, 325);
	    
		//OPTIONS button
	    if((handler.getKeyManager().mX >= 430 && handler.getKeyManager().mX <= 630) && (handler.getKeyManager().mY >= 295 && handler.getKeyManager().mY <= 345))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(430,295,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("Options", 495, 325);
	    
		//NEW WORLD button
	    if((handler.getKeyManager().mX >= 220 && handler.getKeyManager().mX <= 420) && (handler.getKeyManager().mY >= 70 && handler.getKeyManager().mY <= 120))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(220,70,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("New World", 275, 100);
	    
	    //LOAD WORLD button
	    if((handler.getKeyManager().mX >= 220 && handler.getKeyManager().mX <= 420) && (handler.getKeyManager().mY >= 145 && handler.getKeyManager().mY <= 195))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(220,145,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("Load World", 270, 175);
	    
	    //SAVE WORLD button
	    if((handler.getKeyManager().mX >= 220 && handler.getKeyManager().mX <= 420) && (handler.getKeyManager().mY >= 220 && handler.getKeyManager().mY <= 270))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(220,220,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("Save World", 270, 250);

	    //IMPORT TILES button
	    if((handler.getKeyManager().mX >= 10 && handler.getKeyManager().mX <= 210) && (handler.getKeyManager().mY >= 220 && handler.getKeyManager().mY <= 270))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(10,220,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("Import", 75, 240);
	    g.drawString("Tiles", 80, 262);
	   
	    //EXIT button
	    if((handler.getKeyManager().mX >= 220 && handler.getKeyManager().mX <= 420) && (handler.getKeyManager().mY >= 295 && handler.getKeyManager().mY <= 345))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(220,295,200,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("EXIT MAP MAKER", 235, 325);
	    
	    //BACK button
	    if((handler.getKeyManager().mX >= 475 && handler.getKeyManager().mX <= 625) && (handler.getKeyManager().mY >= 20 && handler.getKeyManager().mY <= 70))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(475,20,150,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("<-Back", 495, 50);
	}
	
	public void controlsMenu(Graphics g)
	{
		//Drawing background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 1080);
		
		//CONTROLS text
		g.setColor(Color.RED);
	    g.setFont(headerFont);
	    g.drawString("CONTROLS", 235, 40);
	    
	    //Showing the controls
	    g.setFont(menuFont);
		g.setColor(Color.BLUE);
	    g.drawString("Right Click Drag OR", 20, 35);
	    g.drawString("WASD/Arrow Keys", 25, 60);
	    g.drawString("Escape", 125, 85);
	    g.drawString("E", 180, 110);
	    g.drawString("Scroll Down", 87, 135);
	    g.drawString("Scroll Up", 113, 160);
	    g.drawString("Left Click", 110, 185);
	    g.drawString("Middle Click", 85, 210);
	    g.drawString("0", 180, 235);
	    g.drawString("= OR +", 125, 260);
	    g.drawString("- OR _", 125, 285);
	    g.drawString("H", 180, 310);
	    g.drawString("G", 180, 335);


		g.setColor(Color.YELLOW);
	    g.drawString("| Move the camera", 200, 60);
	    g.drawString("| Open / Close Menu", 200, 85);
	    g.drawString("| Open / Close Tile Selection", 200, 110);
	    g.drawString("| Zoom Out", 200, 135);
	    g.drawString("| Zoom In", 200, 160);
	    g.drawString("| Place a tile", 200, 185);
	    g.drawString("| Copy current selected tile", 200, 210);
	    g.drawString("| Change to base layer", 200, 235);
	    g.drawString("| Move up a layer", 200, 260);
	    g.drawString("| Move down a layer", 200, 285);
	    g.drawString("| Highlight all tiles on the layer (can cause lag)", 200, 310);
	    g.drawString("| Toggles grid view (displays a grid for tiles)", 200, 335);


	    //BACK button
	    if((handler.getKeyManager().mX >= 475 && handler.getKeyManager().mX <= 625) && (handler.getKeyManager().mY >= 20 && handler.getKeyManager().mY <= 70))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(475,20,150,50);
	    g.setColor(Color.BLACK);  
	    g.drawString("<-Back", 495, 50);
	}
	
	public void optionsMenu(Graphics g)
	{
		//Drawing background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1920, 1080);
	    
	    //Showing the options
	    
	    //SLIDERS
	    //MUSIC Sounds
	    g.setFont(menuFont);
	    g.setColor(Color.WHITE);
	    g.drawString("Music Volume",210, 85);
	    
	    if(!Sound.musicMuted)
	    {
	    	if((handler.getKeyManager().mX >= 50 && handler.getKeyManager().mX <= 100) && (handler.getKeyManager().mY >= 100 && handler.getKeyManager().mY <= 150))
	    	{
				g.setColor(Color.YELLOW);
				g.drawString("Mute Music", 50, 90);
	    		g.setColor(DARKRED);
	    	}
	    	else
	    		g.setColor(Color.RED);
	    }
	    else
	    {
	    	if((handler.getKeyManager().mX >= 50 && handler.getKeyManager().mX <= 100) && (handler.getKeyManager().mY >= 100 && handler.getKeyManager().mY <= 150))
	    	{
				g.setColor(Color.YELLOW);
				g.drawString("Unmute Music", 50, 90);
	    		g.setColor(DARKGREEN);
	    	}	    	
	    	else
	    		g.setColor(Color.GREEN);
	    }
	    g.fillRect(50, 100, 50, 50);
	    g.setColor(Color.BLACK);
	    g.drawImage(Assets.muteSound, 50, 100, 50, 50, null);
	    
		if((handler.getKeyManager().mX >= 110 && handler.getKeyManager().mX <= 160) && (handler.getKeyManager().mY >= 100 && handler.getKeyManager().mY <= 150))
			g.setColor(DARKRED);
		else
			g.setColor(Color.RED);
	    g.fillRect(110, 100, 50, 50);
	    g.setColor(Color.WHITE);
	    g.fillRect(170, 100, (int)Sound.musicVolume*4, 50);
	    
	    //scrolling song name
	    g.drawString("Current song playing:", 0, 20);
	    g.drawString(Sound.currentSongName+"", currentSongX, 42);
	    int disappearX = -(Sound.currentSongName.length()*10);
	    int reappearX = 250 ;
	    counter++;
	    if(counter%50 == 0)
	    {
	    	currentSongX--;
	    }
	    if(currentSongX < disappearX)
	    	currentSongX = reappearX;
	    g.setColor(Color.BLACK);
	    g.fillRect(250,0,400,60);
	    /////////////////////////
	    
		if((handler.getKeyManager().mX >= 380 && handler.getKeyManager().mX <= 430) && (handler.getKeyManager().mY >= 100 && handler.getKeyManager().mY <= 150))
			g.setColor(DARKGREEN);
		else
			g.setColor(Color.GREEN);
	    g.fillRect(380, 100, 50, 50);
		if((handler.getKeyManager().mX >= 440 && handler.getKeyManager().mX <= 490) && (handler.getKeyManager().mY >= 100 && handler.getKeyManager().mY <= 150))
		{
			g.setColor(Color.YELLOW);
			g.drawString("Next Song", 440, 90);
			g.setColor(DARKGREEN);
		}
		else
			g.setColor(Color.GREEN);
	    g.fillRect(440, 100, 50, 50);
	    g.setColor(Color.BLACK);
	    g.setFont(headerFont);
	    g.drawImage(Assets.nextSong, 442, 102, 46, 46, null);
	    g.drawString("-", 130, 135);
	    g.drawString("+", 395, 135);

	    //SFX Sounds
	    g.setFont(menuFont);
	    g.setColor(Color.WHITE);
	    g.drawString("SFX Volume",215, 185);
	    
	    if(!Sound.sfxMuted)
	    {
			if((handler.getKeyManager().mX >= 50 && handler.getKeyManager().mX <= 100) && (handler.getKeyManager().mY >= 200 && handler.getKeyManager().mY <= 250))
			{	
				g.setColor(Color.YELLOW);
				g.drawString("Mute SFX", 50, 190);
				g.setColor(DARKRED);
			}
			else
				g.setColor(Color.RED);
	    }
	    else
	    {
	    	if((handler.getKeyManager().mX >= 50 && handler.getKeyManager().mX <= 100) && (handler.getKeyManager().mY >= 200 && handler.getKeyManager().mY <= 250))
	    	{
				g.setColor(Color.YELLOW);
				g.drawString("Unmute SFX", 50, 190);
	    		g.setColor(DARKGREEN);
	    	}
	    	else
	    		g.setColor(Color.GREEN);
	    }
	    g.fillRect(50, 200, 50, 50);
	    g.setColor(Color.BLACK);
	    g.drawImage(Assets.muteSound, 50, 200, 50, 50, null);
	    
		if((handler.getKeyManager().mX >= 110 && handler.getKeyManager().mX <= 160) && (handler.getKeyManager().mY >= 200 && handler.getKeyManager().mY <= 250))
			g.setColor(DARKRED);
		else
			g.setColor(Color.RED);
		g.fillRect(110, 200, 50, 50);
	    g.setColor(Color.WHITE);
	    g.fillRect(170, 200,  (int)Sound.sfxVolume*4, 50);
		if((handler.getKeyManager().mX >= 380 && handler.getKeyManager().mX <= 430) && (handler.getKeyManager().mY >= 200 && handler.getKeyManager().mY <= 250))
			g.setColor(DARKGREEN);
		else
			g.setColor(Color.GREEN);
		g.fillRect(380, 200, 50, 50);
	    g.setColor(Color.BLACK);
	    g.setFont(headerFont);
	    g.drawString("-", 130, 235);
	    g.drawString("+", 395, 235);


	    //BACK button
	    if((handler.getKeyManager().mX >= 475 && handler.getKeyManager().mX <= 625) && (handler.getKeyManager().mY >= 20 && handler.getKeyManager().mY <= 70))
	    	g.setColor(Color.GRAY);
	    else
	    	g.setColor(Color.WHITE);
	    g.fillRect(475,20,150,50);
	    g.setFont(menuFont);
	    g.setColor(Color.BLACK);  
	    g.drawString("<-Back", 495, 50);
	    
		g.setColor(Color.RED);
	    g.setFont(headerFont);
	    g.drawString("OPTIONS", 255, 40);
	}
	
}