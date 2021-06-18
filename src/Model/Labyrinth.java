package Model;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Marcel Vinicius Kohls
 */
public class Labyrinth extends JPanel {
    static final int MAP_UPDATE_REQUIRED = 0;
    static final int MAP_UPDATED = 1;
    
    Map generalMap;
    private int heroCounter = 0;
    private int mapUpdate = MAP_UPDATE_REQUIRED;

    public Labyrinth(int columns, int lines) {
        generalMap = new Map(columns, lines);
    }

    public void startLabyrinth(){
        mapUpdate = MAP_UPDATE_REQUIRED;
        revalidate();
    }

    public void setWindowSize(JFrame window){
        window.setSize(((generalMap.getMaxColumns())*generalMap.getBlockSize())+250, ((generalMap.getMaxRows())*generalMap.getBlockSize())+80);
    }

    public void addHeroToMap(){
        heroCounter++;
        new Thread( new Hero("Hero-"+heroCounter, generalMap, this)).start();
    }

   @Override
    public void paintComponent(Graphics gfx) {
        if (mapUpdate == MAP_UPDATE_REQUIRED){
            generalMap.atualizaMapa(gfx);
            mapUpdate = MAP_UPDATED;
        }
   }
}
