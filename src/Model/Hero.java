package Model;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;

/**
 * @author Marcel Vinicius Kohls
 */
public class Hero extends Thread {
    static int DIR_NONE = 0;
    static int DIR_UP = 1;
    static int DIR_DOWN = 2;
    static int DIR_RIGHT = 3;
    static int DIR_LEFT = 4;

    static int BLOCK_SIZE = 34;
    static int STEP_INTERVAL = 6;

    // outras declaracoes
    private BufferedImage personagemDir1;
    private BufferedImage personagemDir2;
    private BufferedImage personagemDir3;
    private BufferedImage personagemEsq1;
    private BufferedImage personagemEsq2;
    private BufferedImage personagemEsq3;
    private BufferedImage personagemBai1;
    private BufferedImage personagemBai2;
    private BufferedImage personagemBai3;
    private BufferedImage personagemCim1;
    private BufferedImage personagemCim2;
    private BufferedImage personagemCim3;

    private Map personalMap;
    private String heroId;
    private Labyrinth sourceLab;
    private int actualLine = -1, actualColumn = -1;
    private int pastLine = -1, pastColumn = -1;
    private int[][] mapPath = new int[100][100];
    private Random rnd = new Random();
    private int stepCounter;
    private int actualDirection = DIR_NONE;

    public Hero(String idThread, Map mapaGlobal, Labyrinth lab){
        super(idThread);

        sourceLab = lab;
        heroId = idThread;

        try{
            personagemDir1 = ImageIO.read(new File("Theme/heroRight1.png"));
            personagemDir2 = ImageIO.read(new File("Theme/heroRight2.png"));
            personagemDir3 = ImageIO.read(new File("Theme/heroRight3.png"));
            personagemEsq1 = ImageIO.read(new File("Theme/heroLeft1.png"));
            personagemEsq2 = ImageIO.read(new File("Theme/heroLeft2.png"));
            personagemEsq3 = ImageIO.read(new File("Theme/heroLeft3.png"));
            personagemCim1 = ImageIO.read(new File("Theme/heroUp1.png"));
            personagemCim2 = ImageIO.read(new File("Theme/heroUp2.png"));
            personagemCim3 = ImageIO.read(new File("Theme/heroUp3.png"));
            personagemBai1 = ImageIO.read(new File("Theme/heroDown1.png"));
            personagemBai2 = ImageIO.read(new File("Theme/heroDown2.png"));
            personagemBai3 = ImageIO.read(new File("Theme/heroDown3.png"));
        } catch (Exception e) {
            // some msg here
        }
        
        resetMapPath();

        personalMap = mapaGlobal;

        setInitialPosition();
    }
    
    private void resetMapPath() {
        for (int allCol=0; allCol<100; allCol++){
            for (int allRow=0; allRow<100; allRow++){
                mapPath[allCol][allRow] = 0;
            }
        }

        stepCounter = 0;
    }

    private void setInitialPosition(){
        Boolean isPositionSet = false;

        for (int colCount=0; colCount <= personalMap.getMaxColumns(); colCount++){
            for (int rowCount=0; rowCount <= personalMap.getMaxRows(); rowCount++){

                if (isPositionSet == false && personalMap.getMapBlock(colCount,rowCount) == Map.START){
                    actualColumn = colCount;
                    actualLine = rowCount;

                    isPositionSet = true;
                }
            }
        }
    }

    private void nextStep(){
        int valueUp = mapPath[actualColumn][actualLine-1];
        int valueDown = mapPath[actualColumn][actualLine+1];
        int valueRight = mapPath[actualColumn+1][actualLine];
        int valueLeft = mapPath[actualColumn-1][actualLine];
        int indecision = 0;
        int sumCol = 0, sumRow = 0;

        // starting block, just walk to the right 
        if (personalMap.getMapBlock(actualColumn, actualLine) == Map.START){
            sumCol = 1;
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 0){
            if (valueRight < valueLeft){
                sumCol = 1;
            } else if (valueRight == valueLeft && personalMap.RandomInRange(1, 2, rnd) == 1){
                sumCol = 1;
            }
            else {
                sumCol = -1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 1){
            if (valueLeft < valueUp){
                sumCol = -1;
            } else if (valueLeft == valueUp && personalMap.RandomInRange(1, 2, rnd) == 1){
                sumCol = -1;
            } else {
                sumRow = -1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 2){
            if (valueUp < valueDown){
                sumRow = -1;
            } else if (valueUp == valueDown && personalMap.RandomInRange(1, 2, rnd) == 1){
                sumRow = -1;
            } else {
                sumRow = 1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 3){
            if (valueLeft < valueDown) {
                sumCol = -1;
            } else if (valueLeft == valueDown && personalMap.RandomInRange(1, 2, rnd) == 1){
                sumCol = -1;
            } else {
                sumRow = 1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 4){
            if (valueRight < valueLeft){
                sumCol = 1;
            } else {
                sumCol = -1;
            }

            if (valueDown < valueRight && valueDown < valueLeft){
                sumCol = 0;
                sumRow = 1;
            }

            // if indecision, takes a random course
            if (valueRight == valueLeft && valueDown == valueRight){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 3, rnd);

                if (decision == 1) {
                    sumCol = 1;
                } else if (decision == 2){
                    sumCol = -1;
                } else {
                    sumRow = 1;
                }
            } else if (valueRight == valueLeft){
                sumCol = 0;
                sumRow = 0;

                int decisao = personalMap.RandomInRange(1, 2, rnd);

                if (decisao == 1) {
                    sumCol = 1;
                } else{
                    sumCol = -1;
                }
            } else if (valueDown == valueRight){
                sumCol = 0;
                sumRow = 0;

                int decisao = personalMap.RandomInRange(1, 2, rnd);

                if (decisao == 1) {
                    sumRow = 1;
                } else{
                    sumCol = 1;
                }
            } else if (valueDown == valueLeft){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumRow = 1;
                } else{
                    sumCol = -1;
                }
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 5){
            if (valueRight < valueDown){
                sumCol = 1;
            } else if (valueRight == valueDown && personalMap.RandomInRange(1, 2, rnd) == 1){
                sumCol = 1;
            } else {
                sumRow = 1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 6){
            if (valueDown < valueUp){
                sumRow = 1;
            } else {
                sumRow = -1;
            }
            
            if (valueRight < valueDown && valueRight < valueUp) {
                sumRow = 0;
                sumCol = 1;
            }

            if (valueDown == valueUp && valueRight == valueUp){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 3, rnd);

                if (decision == 1) {
                    sumRow = 1;
                } else if (decision == 2){
                    sumRow = -1;
                } else {
                    sumCol = 1;
                }
            } else if (valueDown == valueUp){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumRow = 1;
                } else{
                    sumRow = -1;
                }
            } else if (valueRight == valueDown){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumCol = 1;
                } else{
                    sumRow = 1;
                }
            } else if (valueRight == valueUp){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumCol = 1;
                } else{
                    sumRow = -1;
                }
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 7){
            if (valueUp < valueRight){
                sumRow = -1;
            } else if (valueUp == valueRight && personalMap.RandomInRange(1, 2, rnd) == 1){
                sumRow = -1;
            } else {
                sumCol = 1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 8){
            if (valueRight < valueLeft){
                sumCol = 1;
            } else {
                sumCol = -1;
            }
            
            if (valueUp < valueRight && valueUp <valueLeft){
                sumCol = 0;
                sumRow = -1;
            }

            if (valueRight == valueLeft && valueUp == valueRight){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 3, rnd);

                if (decision == 1) {
                    sumCol = 1;
                } else if (decision == 2){
                    sumCol = -1;
                } else {
                    sumRow = -1;
                }
            } else if (valueRight == valueLeft){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumCol = 1;
                } else{
                    sumCol = -1;
                }
            } else if (valueUp == valueRight){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumRow = -1;
                } else{
                    sumCol = 1;
                }
            } else if (valueUp == valueLeft){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumRow = -1;
                } else{
                    sumCol = -1;
                }
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 9){
            if (valueRight < valueLeft){
                sumCol = 1;
            } else {
                sumCol = -1;
            }
            
            if (valueDown < valueRight && valueDown < valueLeft){
                sumCol = 0;
                sumRow = 1;
            } 
            
            if (valueUp < valueRight && valueUp < valueLeft && valueUp < valueDown) {
                sumCol = 0;
                sumRow = -1;
            }
        } else if (personalMap.getMapBlock(actualColumn, actualLine) == 10){
            if (valueDown < valueUp){
                sumRow = 1;
            } else {
                sumRow = -1;
            }
            
            if (valueLeft < valueDown && valueLeft < valueUp){
                sumRow = 0;
                sumCol = -1;
            }

            if (valueDown == valueUp && valueLeft == valueUp){
                sumCol = 0;
                sumRow = 0;

                int decisao = personalMap.RandomInRange(1, 3, rnd);

                if (decisao == 1) {
                    sumRow = 1;
                } else if (decisao == 2){
                    sumRow = -1;
                } else {
                    sumCol = -1;
                }
            } else if (valueDown == valueUp){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumRow = 1;
                } else{
                    sumRow = -1;
                }
            } else if (valueLeft == valueDown){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumCol = -1;
                } else{
                    sumRow = 1;
                }
            } else if (valueLeft == valueUp){
                sumCol = 0;
                sumRow = 0;

                int decision = personalMap.RandomInRange(1, 2, rnd);

                if (decision == 1) {
                    sumCol = -1;
                } else{
                    sumRow = -1;
                }
            }
        }

        // ending blocks, can only go back to previous position
        else if (personalMap.getMapBlock(actualColumn, actualLine) == 11){
            sumRow = +1;
        }
        else if (personalMap.getMapBlock(actualColumn, actualLine) == 12){
            sumRow = -1;
        }
        else if (personalMap.getMapBlock(actualColumn, actualLine) == 13){
            sumCol = -1;
        }
        else if (personalMap.getMapBlock(actualColumn, actualLine) == 14){
            sumCol = 1;
        }

        // informs the new direction you are going to walk, in order to have the effect 
        if (sumCol == 1) {
            actualDirection = DIR_RIGHT;
        }
        else if (sumCol == -1){
            actualDirection = DIR_LEFT;
        }
        else if (sumRow == 1){
            actualDirection = DIR_DOWN;
        }
        else if (sumRow == -1){
            actualDirection = DIR_UP;
        } else {
            actualDirection = DIR_NONE;
        }

        pastColumn = actualColumn;
        pastLine = actualLine;
        mapPath[actualColumn][actualLine]++;
        actualColumn = actualColumn + sumCol;
        actualLine = actualLine + sumRow;
    }

    @Override
    public void run(){
        int i=0;
        
        while (true){            
            if (countStep() == false) {
                stepCounter = BLOCK_SIZE;
                nextStep();
            } else {
                if (pastLine != -1){
                    personalMap.drawBlock(sourceLab.getGraphics(), pastColumn, pastLine, personalMap.getMapBlock(pastColumn, pastLine));    
                }
                personalMap.drawBlock(sourceLab.getGraphics(), actualColumn, actualLine, personalMap.getMapBlock(actualColumn, actualLine));
            }

            drawHero();

            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {}
        }
    }

    private void drawHero(){
        BufferedImage sprite;

        if (stepCounter != 0){
            sprite = personagemDir1;

            // find which sprite to draw based on the direction
            if (actualDirection == DIR_RIGHT) {
                if (stepCounter/STEP_INTERVAL == 1 || stepCounter/STEP_INTERVAL == 5 || stepCounter/STEP_INTERVAL == 9 || stepCounter/STEP_INTERVAL == 12 || stepCounter/STEP_INTERVAL == 16|| stepCounter/STEP_INTERVAL == 20){
                    sprite = personagemDir1;
                } else if (stepCounter/STEP_INTERVAL == 0 || stepCounter/STEP_INTERVAL == 2 || stepCounter/STEP_INTERVAL == 4 || stepCounter/STEP_INTERVAL == 6 || stepCounter/STEP_INTERVAL == 8 || stepCounter/STEP_INTERVAL == 11 || stepCounter/STEP_INTERVAL == 13 || stepCounter/STEP_INTERVAL == 15 || stepCounter/STEP_INTERVAL == 17 || stepCounter/STEP_INTERVAL == 19){
                    sprite = personagemDir2;
                } else if (stepCounter/STEP_INTERVAL == 3 || stepCounter/STEP_INTERVAL == 7 || stepCounter/STEP_INTERVAL == 10 || stepCounter/STEP_INTERVAL == 14 || stepCounter/STEP_INTERVAL == 18){
                    sprite = personagemDir3;
                }
            }
            else if (actualDirection == DIR_DOWN){
                if (stepCounter/STEP_INTERVAL == 1 || stepCounter/STEP_INTERVAL == 5 || stepCounter/STEP_INTERVAL == 9 || stepCounter/STEP_INTERVAL == 12 || stepCounter/STEP_INTERVAL == 16|| stepCounter/STEP_INTERVAL == 20){
                    sprite = personagemBai1;
                } else if (stepCounter/STEP_INTERVAL == 0 || stepCounter/STEP_INTERVAL == 2 || stepCounter/STEP_INTERVAL == 4 || stepCounter/STEP_INTERVAL == 6 || stepCounter/STEP_INTERVAL == 8 || stepCounter/STEP_INTERVAL == 11 || stepCounter/STEP_INTERVAL == 13 || stepCounter/STEP_INTERVAL == 15 || stepCounter/STEP_INTERVAL == 17 || stepCounter/STEP_INTERVAL == 19){
                    sprite = personagemBai2;
                } else if (stepCounter/STEP_INTERVAL == 3 || stepCounter/STEP_INTERVAL == 7 || stepCounter/STEP_INTERVAL == 10 || stepCounter/STEP_INTERVAL == 14 || stepCounter/STEP_INTERVAL == 18){
                    sprite = personagemBai3;
                }
            }
            else if (actualDirection == DIR_UP){
                if (stepCounter/STEP_INTERVAL == 1 || stepCounter/STEP_INTERVAL == 5 || stepCounter/STEP_INTERVAL == 9 || stepCounter/STEP_INTERVAL == 12 || stepCounter/STEP_INTERVAL == 16|| stepCounter/STEP_INTERVAL == 20){
                    sprite = personagemCim1;
                } else if (stepCounter/STEP_INTERVAL == 0 || stepCounter/STEP_INTERVAL == 2 || stepCounter/STEP_INTERVAL == 4 || stepCounter/STEP_INTERVAL == 6 || stepCounter/STEP_INTERVAL == 8 || stepCounter/STEP_INTERVAL == 11 || stepCounter/STEP_INTERVAL == 13 || stepCounter/STEP_INTERVAL == 15 || stepCounter/STEP_INTERVAL == 17 || stepCounter/STEP_INTERVAL == 19){
                    sprite = personagemCim2;
                } else if (stepCounter/STEP_INTERVAL == 3 || stepCounter/STEP_INTERVAL == 7 || stepCounter/STEP_INTERVAL == 10 || stepCounter/STEP_INTERVAL == 14 || stepCounter/STEP_INTERVAL == 18){
                    sprite = personagemCim3;
                }
            }
            else if (actualDirection == DIR_LEFT){
                if (stepCounter/STEP_INTERVAL == 1 || stepCounter/STEP_INTERVAL == 5 || stepCounter/STEP_INTERVAL == 9 || stepCounter/STEP_INTERVAL == 12 || stepCounter/STEP_INTERVAL == 16|| stepCounter/STEP_INTERVAL == 20){
                    sprite = personagemEsq1;
                } else if (stepCounter/STEP_INTERVAL == 0 || stepCounter/STEP_INTERVAL == 2 || stepCounter/STEP_INTERVAL == 4 || stepCounter/STEP_INTERVAL == 6 || stepCounter/STEP_INTERVAL == 8 || stepCounter/STEP_INTERVAL == 11 || stepCounter/STEP_INTERVAL == 13 || stepCounter/STEP_INTERVAL == 15 || stepCounter/STEP_INTERVAL == 17 || stepCounter/STEP_INTERVAL == 19){
                    sprite = personagemEsq2;
                } else if (stepCounter/STEP_INTERVAL == 3 || stepCounter/STEP_INTERVAL == 7 || stepCounter/STEP_INTERVAL == 10 || stepCounter/STEP_INTERVAL == 14 || stepCounter/STEP_INTERVAL == 18){
                    sprite = personagemEsq3;
                }
            }

            // draw sprite
            if (actualDirection == DIR_RIGHT) {
                sourceLab.getGraphics().drawImage(sprite, (actualColumn*BLOCK_SIZE)-stepCounter, actualLine*BLOCK_SIZE, null);
            }
            else if (actualDirection == DIR_DOWN){
                sourceLab.getGraphics().drawImage(sprite, actualColumn*BLOCK_SIZE, (actualLine*BLOCK_SIZE)-stepCounter, null);
            }
            else if (actualDirection == DIR_UP){
                sourceLab.getGraphics().drawImage(sprite, actualColumn*BLOCK_SIZE, (actualLine*BLOCK_SIZE)+stepCounter, null);
            }
            else if (actualDirection == DIR_LEFT){
                sourceLab.getGraphics().drawImage(sprite, (actualColumn*BLOCK_SIZE)+stepCounter, actualLine*BLOCK_SIZE, null);
            }
        }
    }

    /**
     * Controls how many steps the character has walked to the current matrix position. 
     * @return TRUE if counted a step, FALSE if no longer have a step to count. 
     */
    private Boolean countStep(){
        if (stepCounter > STEP_INTERVAL) {
            stepCounter -= STEP_INTERVAL;
            return true;
        } 
        else {
            return false;
        }
    }
}
