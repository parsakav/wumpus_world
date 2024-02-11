package com.pgu.wumpusworld;

import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class JFXArena extends Pane implements Runnable
{

    private final int NUMBER_OF_PIT;
    private final int NUMBER_OF_GOLD;
    private static final String WUMPUS_IMAGE_FILE = "images.jpg";
    private static final String IMAGE_FILE = "1554047213.png";
    private static final String WALL_IMAGE_FILE = "181478.png";
    private static final String GOLD_IMAGE_FILE = "gold.jpg";
    private Image robot1;
    private Image wall;
    private Image gold;
    private Image wumpus;
private int speed=3000;

    private Random random = new Random();
    private ExecutorService moverThread= Executors.newSingleThreadExecutor();

    private  volatile boolean finished=false;
    

    private int gridWidth = 4;
    private int gridHeight = 4;
   private final char [][] grid;
    private int robotX = 0;
    private int robotY = 0;

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.

    private List<ArenaListener> listeners = null;



        /**
         * Creates a new arena object, loading the robot image and initialising a drawing surface.
         */
    public JFXArena(int number_of_pit, int number_of_gold, int speed, int n)
    {
        NUMBER_OF_PIT = number_of_pit;
        NUMBER_OF_GOLD = number_of_gold;

        this.speed=speed;
   this.gridWidth=n;
   this.gridHeight=n;
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(GOLD_IMAGE_FILE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + GOLD_IMAGE_FILE);
            }
            gold = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + GOLD_IMAGE_FILE, e);
        }
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(WUMPUS_IMAGE_FILE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + WUMPUS_IMAGE_FILE);
            }
            wumpus = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + IMAGE_FILE, e);
        }
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(IMAGE_FILE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + IMAGE_FILE);
            }
            robot1 = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + IMAGE_FILE, e);
        } try(InputStream is = getClass().getClassLoader().getResourceAsStream(WALL_IMAGE_FILE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + WALL_IMAGE_FILE);
            }
            wall = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + WALL_IMAGE_FILE, e);
        }
        
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
        grid= new char[gridWidth][gridHeight];
        for(int i=0;i<grid.length;i++){
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j]= WumpusWorld.EMPTY;
            }
        }
        grid[0][0]='b';
        while(true) {
            int wpus_x = random.nextInt(grid.length);
            int wpus_y = random.nextInt(grid.length);
            if(wpus_x==0 && wpus_y==0){
                continue;
            }
            grid[wpus_x][wpus_y] = WumpusWorld.WUMPUS;
            System.out.println(wpus_x);
            System.out.println(wpus_y);
            break;
        }
for(int i=0;i<NUMBER_OF_GOLD;i++) {
    while (true) {

        int gold_x = random.nextInt(grid.length);
        int gold_y = random.nextInt(grid.length);
        if (grid[gold_x][gold_y] != WumpusWorld.EMPTY) {
            continue;
        }

        grid[gold_x][gold_y] = WumpusWorld.GOLD;
        break;


    }
}
for(int i=0;i<NUMBER_OF_PIT;i++) {
    while (true) {
        int pit_x = random.nextInt(grid.length);
        int pit_y = random.nextInt(grid.length);
        if (grid[pit_x][pit_y] != WumpusWorld.EMPTY) {
            continue;
        }

        grid[pit_x][pit_y] = WumpusWorld.PIT;
        break;
    }
}
        moverThread.submit(this
   );
    }

    
    
    /**
     * Moves a robot image to a new grid position. This is highly rudimentary, as you will need
     * many different robots in practice. This method currently just serves as a demonstration.
     */
    public void setRobotPosition(int x, int y)
    {
        robotX = x;
        robotY = y;
        requestLayout();
    }
    
    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback 
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the 
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);
                
                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : listeners)
                    {   
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        listeners.add(newListener);
    }


    public void changeBotPosition() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        changePosition();
    }

    /**
     * Moves a robot image to a new grid position. This is highly rudimentary, as you will need
     * many different robots in practice. This method currently just serves as a demonstration.
     */
    public void changePosition() {

        requestLayout();

    }
        
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren()
    {
        super.layoutChildren(); 
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);
            
        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Invoke helper methods to draw things at the current location.
        // ** You will need to adapt this to the requirements of your application. **
       int a[]= findAgentPosition();
        robotX=a[0];
        robotY=a[1];
                    drawImage(gfx, robot1, robotX, robotY);
                    drawLabel(gfx, "Agent", robotX, robotY);


for (int i=0;i<grid.length;i++){
    for(int j=0;j<grid.length;j++){
        Image img=getPositionImage(i,j);
        if((img)!=null){
            drawImage(gfx, img, i, j);

        }
    }
}




    }
    private String[] getSensorsPrecept(){
        String[] precept = new String[4];
        int x=findAgentPosition()[0];
        int y=findAgentPosition()[1];

          String p1 = sensor(x,y+1);
          String p2 = sensor(x+1,y);
          String p3 = sensor(x-1,y);
          String p4 = sensor(x,y-1);

          precept[0]=p1;
          precept[1]=p2;
          precept[2]=p3;
          precept[3]=p4;
          return precept;

    }
    private Image getPositionImage(int x,int y){
        if(grid[x][y]=='p'){
            return wall;
        }   if(grid[x][y]=='g'){
            return gold;
        }if(grid[x][y]=='w'){
            return wumpus;
        } return null;
    }

    private String sensor(int x,int y){
        if(x>=gridWidth || x<0 ||y<0|| y>=gridHeight){
            return "None";
        }
        if(grid[x][y]=='p'){
            return "Breeze";
        }   if(grid[x][y]=='g'){
            return "Gold";
        } if(grid[x][y]=='g'){
            return "Gold";
        }if(grid[x][y]=='w'){
            return "Stench";
        } return "None";
    }
    private int[] findAgentPosition() {

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                // b means bot
                if (grid[i][j] == 'b') {
int g[] = {i,j};
return g;
                }
            }
        }
    throw new Error("should not occur");
    }

    

    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY)
    {

        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = robot1.getWidth();
        double fullSizePixelHeight = robot1.getHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0, 
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }
    
    

    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY)
    {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }
    


    @Override
    public void run() {
        while (!finished) {
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            boolean br = false;
            boolean st = false;
            for (String s : getSensorsPrecept()) {
                if (s.equals("Breeze")) {
                    br = true;
                } else if (s.equals("Stench")) {
                    st = true;
                }
            }


            WumpusWorld.Position pos = WumpusWorld.getNextPosition(grid, new WumpusWorld.Position((int) robotX, (int) robotY), br, st);
            String p5=sensor(pos.row,pos.col);

            if(p5.equals("Gold"))
            {
                finished=true;
                App.logger.appendText("\nWin");
            } if(p5.equals("Wumpus"))
            {
                finished=true;
                App.logger.appendText("\nLose");
                System.out.println("Lose");
            }
            grid[pos.row][pos.col] = 'b';
            grid[robotX][robotY] = WumpusWorld.EMPTY;
            robotX = pos.row;
            robotY = pos.col;
            changeBotPosition();
            getSensorsPrecept();

            App.logger.appendText("\n"+String.format("Go to (%d,%d)",robotX,robotY));
        }
    }
    }

