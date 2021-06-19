package Model;

/**
 * @author Marcel V. Kohls
 */
public class TurningPoint {
    private int turnCounter=0;
    private int[] colPoint=new int[1000];
    private int[] rowPoint=new int[1000];
    private Boolean[] activePoint=new Boolean[1000];

    public void TurningPoint(){
        turnCounter = 0;
    }

    public void addTurningPoint(int colPos, int rowPos){
        Boolean foundIt = false;

        for (int allPoints=0; allPoints < turnCounter; allPoints++){
            if (colPoint[allPoints] == colPos && rowPoint[allPoints] == rowPos){
                foundIt = true;
            }
        }

        if (foundIt == false){
            colPoint[turnCounter] = colPos;
            rowPoint[turnCounter] = rowPos;
            activePoint[turnCounter] = true;

            turnCounter++;
        }
    }

    public void disableTurningPoint(int colPos, int rowPos){
        for(int allPoints=0; allPoints < turnCounter; allPoints++){
            if (colPoint[allPoints] == colPos && rowPoint[allPoints] == rowPos){
                activePoint[allPoints] = false;
            }
        }
    }

    public int[] getNextPoint(){
        int[] nextPoint = new int[2];

        //look for the next active
        nextPoint[0] = -1;
        nextPoint[1] = -1;

        int allTurns = 0;

        while (allTurns < turnCounter && nextPoint[1]==-1){
            if (activePoint[allTurns] == true){
                nextPoint[0] = colPoint[allTurns];
                nextPoint[1] = rowPoint[allTurns];
            }

            allTurns++;
        }

        return nextPoint;
    }
}
