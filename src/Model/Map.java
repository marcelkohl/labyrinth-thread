package Model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;
import java.util.HashMap;

/**
 * @author Marcel Vinicius Kohls
 */
public class Map {
    static int BLOCK_SIZE = 34;
    static int STEP_INTERVAL = 6;

    static int NEVER_STEPPED = -1;
    
    static int DIR_NONE = 0;
    static int DIR_UP = 1;
    static int DIR_DOWN = 2;
    static int DIR_RIGHT = 3;
    static int DIR_LEFT = 4;

    private int actualDirection = DIR_NONE;
    private int imgSpot = -1;

    private int maxRows;
    private int maxCols;

    private Boolean isFinishSet = false;

    private int[][] globalMap;

    private TurningPoint turningPoints = new TurningPoint();
    private Random rnd = new Random();
    
    static int FLOOR_LR = 0, FLOOR_LU = 1, FLOOR_UD = 2, FLOOR_LD = 3, FLOOR_LDR = 4,
        FLOOR_DR = 5, FLOOR_DRU = 6, FLOOR_RU = 7, FLOOR_LRU = 8, FLOOR_LDRU = 9,
        FLOOR_LDU = 10, FLOOR_D = 11, FLOOR_U = 12, FLOOR_L = 13,  FLOOR_R = 14,
        GRASS=15, START=16, FINISH_R=17, FINISH_L=18, FINISH_U=19, FINISH_D=20;

    private BufferedImage[] images = new BufferedImage[21];

    HashMap<Integer, String> imageNames = new HashMap<Integer, String>();

    public Map(int rows, int cols){
        setupImages();
        
        maxRows  = rows;
        maxCols = cols;
        globalMap = new int[100][100];

        this.generateDepthMap();
    }

    private void setupImages() {
        imageNames.put(FLOOR_LR, "floor-lr");   // 0
        imageNames.put(FLOOR_LU, "floor-lu");   // 1
        imageNames.put(FLOOR_UD, "floor-ud");   // 2
        imageNames.put(FLOOR_LD, "floor-ld");   // 3
        imageNames.put(FLOOR_LDR, "floor-ldr"); // 4
        imageNames.put(FLOOR_DR, "floor-dr");   // 5
        imageNames.put(FLOOR_DRU, "floor-dru"); // 6
        imageNames.put(FLOOR_RU, "floor-ru");   // 7
        imageNames.put(FLOOR_LRU, "floor-lru"); // 8
        imageNames.put(FLOOR_LDRU, "floor-ldru"); // 9
        imageNames.put(FLOOR_LDU, "floor-ldu"); // 10
        imageNames.put(FLOOR_D, "floor-d");     // 11
        imageNames.put(FLOOR_U, "floor-u");     // 12
        imageNames.put(FLOOR_L, "floor-l");     // 13
        imageNames.put(FLOOR_R, "floor-r");     // 14
        imageNames.put(GRASS, "grass");
        imageNames.put(START, "start");         // 99
        imageNames.put(FINISH_R, "finish-r");   // 95
        imageNames.put(FINISH_L, "finish-l");   // 96
        imageNames.put(FINISH_U, "finish-u");   // 97
        imageNames.put(FINISH_D, "finish-d");   // 98
                
        try {
            for (int imgIndex=0; imgIndex < imageNames.size(); imgIndex++){
                images[imgIndex] = ImageIO.read(new File("Theme/" + imageNames.get(imgIndex) + ".png"));                
            }            
        } catch (Exception e) {
            System.out.println("Something went wrong on loading sprites");
        }
    }
    
    public int getMaxRows(){
        return maxRows;
    }

    public int getBlockSize(){
        return BLOCK_SIZE;
    }

    public int getMaxColumns(){
        return maxCols;
    }

    public int getMapBlock(int colPos, int rowPos){
        if (colPos >= 0 && rowPos >= 0 && colPos <= maxCols && rowPos <= maxRows){
            return globalMap[colPos][rowPos];
        } else {
            return NEVER_STEPPED;
        }
    }

    private void resetGlobalMap() {
        for (int colCount = 0; colCount <= maxCols; colCount++){
            for (int rowCount = 0; rowCount <= maxRows; rowCount++){
                globalMap[colCount][rowCount] = NEVER_STEPPED;
            }
        }
    }
    
    public void generateDepthMap(){
        resetGlobalMap();

        int horCounter = 0;
        int verCounter = this.RandomInRange(1, maxRows-1, rnd);
        int newStep;
        int[] newPoint;
        Boolean isNotFinished = true;
        Boolean isStartSet = false;

        actualDirection = DIR_RIGHT;
        imgSpot = 1;

        while (isNotFinished == true){
            int nextTurn = this.RandomInRange(1, 6, rnd);
            int turnCounter = 0;

            while (turnCounter <= nextTurn){
                newStep = this.keepGoing(horCounter, verCounter);

                if (actualDirection == DIR_NONE){
                    turningPoints.disableTurningPoint(horCounter, verCounter);

                    newPoint = turningPoints.getNextPoint();

                    if (newPoint[0] == -1 && newPoint[1] == -1){
                        isNotFinished = false;
                        turnCounter = nextTurn +1; //para sair do laco de contador de desvio
                    } else {
                        horCounter = newPoint[0];
                        verCounter = newPoint[1];
                        actualDirection = DIR_NONE;
                        actualDirection = this.newDirection(horCounter, verCounter);
                    }

                } else if(newStep == 0){
                    actualDirection = this.newDirection(horCounter, verCounter);
                } else {
                    if (actualDirection == DIR_RIGHT || actualDirection == DIR_LEFT){
                        horCounter = horCounter + newStep;
                        imgSpot = 0;
                    } else if (actualDirection == DIR_UP || actualDirection == DIR_DOWN){
                        verCounter = verCounter + newStep;
                        imgSpot = 2;
                    }

                    if (isStartSet == false){
                        isStartSet = true;
                        imgSpot = START;
                    }

                    globalMap[horCounter][verCounter] = imgSpot;

                    turnCounter++;
                }
            }
            
            actualDirection = this.newDirection(horCounter, verCounter);
       }
        
      this.setFinalPoint();
    }

    public void setFinalPoint(){
        int colCount = maxCols;
        Boolean isFinalSet = false;

        while (isFinalSet == false && colCount > 0){
            for (int rowCount = 0; rowCount <= maxRows; rowCount++){
                if (isFinalSet == false) {
                   if (globalMap[colCount][rowCount] == FLOOR_D) {
                        globalMap[colCount][rowCount] = FINISH_D;
                        isFinalSet = true;
                    } else if(globalMap[colCount][rowCount] == FLOOR_U) {
                        globalMap[colCount][rowCount] = FINISH_U;
                        isFinalSet = true;
                    } else if (globalMap[colCount][rowCount] == FLOOR_L){
                        globalMap[colCount][rowCount] = FINISH_L;
                        isFinalSet = true;
                    } else if (globalMap[colCount][rowCount] == FLOOR_R){
                        globalMap[colCount][rowCount] = FINISH_R;
                        isFinalSet = true;
                    }
                }
            }

            colCount--;
        }
    }

    /*
     * @return returns DIR_NONE is there is no more directions to go
     */
    public int newDirection(int actualCol, int actualRow){
        Boolean tryRight=false;
        Boolean tryLeft=false;
        Boolean tryUp=false;
        Boolean tryDown=false;
        int newDirection = DIR_NONE;
        int directionToTry;
        int oldDirection = actualDirection;


        // force to try actual direction
        if (actualDirection == DIR_UP){
            tryUp = true;
        }
        else if (actualDirection == DIR_DOWN){
            tryDown = true;
        }
        else if (actualDirection == DIR_LEFT){
            tryLeft = true;
        }
        else if (actualDirection == DIR_RIGHT){
            tryRight = true;
        }

        // first try other directions, and check if the new one will not exceed the edges
        while (newDirection == DIR_NONE && (tryRight == false || tryLeft == false || tryUp == false || tryDown == false)){
            directionToTry = this.RandomInRange(1, 4, rnd);

            if (directionToTry != actualDirection) {
                newDirection = directionToTry;
            }

            // marca as tentativas q nao pode mais ir
            if (directionToTry == DIR_LEFT && (actualCol - 1 == 0 || globalMap[actualCol - 1][actualRow] != NEVER_STEPPED) ){
                tryLeft=true;
                newDirection = DIR_NONE;
            } else if (directionToTry == DIR_RIGHT && (actualCol + 1 == maxCols || globalMap[actualCol + 1][actualRow] != NEVER_STEPPED)){
                tryRight=true;
                newDirection = DIR_NONE;
            } else if (directionToTry == DIR_UP && (actualRow - 1 == 0 || globalMap[actualCol][actualRow - 1] != NEVER_STEPPED)){
                tryUp=true;
                newDirection = DIR_NONE;
            } else if (directionToTry == DIR_DOWN && (actualRow + 1 == maxRows || globalMap[actualCol][actualRow + 1] != NEVER_STEPPED)){
                tryDown=true;
                newDirection = DIR_NONE;
            }
        }

        if (newDirection != DIR_NONE){
            turningPoints.addTurningPoint(actualCol, actualRow);
        }

        // marks the point where we left. If you have a change of route, we will remark this point.
        int actualInGlobalMap = globalMap[actualCol][actualRow];
        int newInGlobalMap = DIR_NONE;
        
        if (oldDirection == DIR_NONE){
            if (actualInGlobalMap == FLOOR_DR && newDirection == DIR_UP){
                newInGlobalMap = FLOOR_DRU;
            } else if (actualInGlobalMap == FLOOR_DR && newDirection == DIR_LEFT){
                newInGlobalMap = FLOOR_LDR;
            } else if (actualInGlobalMap == FLOOR_RU && newDirection == DIR_DOWN){
                newInGlobalMap = FLOOR_DRU;
            } else if (actualInGlobalMap == FLOOR_RU && newDirection == DIR_LEFT){
                newInGlobalMap = FLOOR_LRU;
            } else if (actualInGlobalMap == FLOOR_LU && newDirection == DIR_RIGHT){
                newInGlobalMap = FLOOR_LRU;
            } else if (actualInGlobalMap == FLOOR_LU && newDirection == DIR_DOWN){
                newInGlobalMap = FLOOR_LDU;
            } else if (actualInGlobalMap == FLOOR_LD && newDirection == DIR_UP){
                newInGlobalMap = FLOOR_LDU;
            } else if (actualInGlobalMap == FLOOR_LD && newDirection == DIR_RIGHT){
                newInGlobalMap = FLOOR_LDR;
            } else if (newDirection != DIR_NONE && (actualInGlobalMap == FLOOR_DRU || actualInGlobalMap == FLOOR_LRU || actualInGlobalMap == FLOOR_LDU || actualInGlobalMap == FLOOR_LDR)){
                newInGlobalMap = FLOOR_LDRU;
            }
        } else if (newDirection == DIR_RIGHT){
            if (oldDirection == DIR_UP){
                newInGlobalMap = FLOOR_DR;
            } else if (oldDirection == DIR_DOWN){
                newInGlobalMap = FLOOR_RU;
            }
        } else if (newDirection == DIR_LEFT){
            if (oldDirection == DIR_UP){
                newInGlobalMap = FLOOR_LD;
            } else if (oldDirection == DIR_DOWN){
                newInGlobalMap = FLOOR_LU;
            }
        } else if (newDirection == DIR_UP){
            if (oldDirection == DIR_RIGHT){
                newInGlobalMap = FLOOR_LU;
            } else if (oldDirection == DIR_LEFT){
                newInGlobalMap = FLOOR_RU;
            }
        } else if (newDirection == DIR_DOWN){
            if (oldDirection == DIR_RIGHT){
                newInGlobalMap = FLOOR_LD;
            } else if (oldDirection == DIR_LEFT){
                newInGlobalMap = FLOOR_DR;
            }
        } else if (newDirection == DIR_NONE){
            if (oldDirection == DIR_DOWN){
                newInGlobalMap = FLOOR_U;
            } else if (oldDirection == DIR_UP){
                newInGlobalMap = FLOOR_D;
            } else if(oldDirection == DIR_RIGHT){
                newInGlobalMap = FLOOR_L;
            } else if(oldDirection == DIR_LEFT){
                newInGlobalMap = FLOOR_R;
            }
        }
        
        if (newInGlobalMap != DIR_NONE) {
            globalMap[actualCol][actualRow] = newInGlobalMap;
        }

        return newDirection;
    }

    /*
     * identify if the next point on the path you are following can be marked 
     * (if it has no obstacles or the end of the matrix). returns +1 or -1 
     * depending on where the path is going. returns 0 when you can continue 
     */
    public int keepGoing(int actualCol, int actualRow){
        int stepInDirection = 0;

        if (actualDirection == DIR_RIGHT && actualCol < maxCols && globalMap[actualCol + 1][actualRow] == NEVER_STEPPED){
            stepInDirection = 1;
        } else if(actualDirection == DIR_LEFT && actualCol > 1 && globalMap[actualCol - 1][actualRow] == NEVER_STEPPED){
            stepInDirection = -1;
        } else if (actualDirection == DIR_UP && actualRow > 1 && globalMap[actualCol][actualRow - 1] == NEVER_STEPPED){
            stepInDirection = -1;
        } else if (actualDirection == DIR_DOWN && actualRow < (maxRows-1) && globalMap[actualCol][actualRow + 1] == NEVER_STEPPED){
            stepInDirection = 1;
        }

        return stepInDirection;
    }

    public void updateMap(Graphics areaG){
        for(int colCount=0; colCount<=maxCols; colCount++){
            for(int rowCount=0; rowCount<=maxRows; rowCount++){
                drawBlock(areaG, colCount, rowCount, globalMap[colCount][rowCount]);
            }
        }
    }

    public void drawBlock(Graphics gfx, int colPos, int rowPos, int blockId){
        gfx.drawImage(
            images[blockId >= 0 ? blockId : GRASS],
            colPos*BLOCK_SIZE,
            rowPos*BLOCK_SIZE, null
        );
    }

    public int RandomInRange(int aStart, int aEnd, Random aRandom){
        if ( aStart > aEnd ) {
          throw new IllegalArgumentException("Start cannot exceed End.");
        }

        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        return randomNumber;
      }
}
