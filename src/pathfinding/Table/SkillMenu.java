package pathfinding.Table;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import javax.swing.JRootPane;
import pathfinding.Indicators.DamageIndicator;
import pathfinding.actor.Creatures.Creature;
import pathfinding.actor.Skills.Skill;
import pathfinding.auxiliar.Constants;

public class SkillMenu {
    int cameraHeight, cameraWidth;
    final int TILE_SIZE;
    final int TOTAL_HEIGHT, TOTAL_WIDTH;
    final int SKILL_BOX_HEIGHT;
    
    public SkillMenu(int cameraHeight, int cameraWidth, int tileSize){
        this.cameraHeight = cameraHeight;
        this.cameraWidth = cameraWidth;
        this.TILE_SIZE = tileSize;
        TOTAL_HEIGHT =  cameraHeight * TILE_SIZE;
        TOTAL_WIDTH = cameraWidth * TILE_SIZE;
        SKILL_BOX_HEIGHT = ((cameraHeight - 8) / 2)*TILE_SIZE;
    }
    
    /**
     * 
     * @param g
     */
    public void draw(Graphics2D g, ArrayList<Skill> skillList, JRootPane rootPane) {
        int x, y, drawX, drawY;
        Tile tile;
        
        //outer background
        g.setColor(Color.black);
        g.fillRect(TILE_SIZE, 0, TOTAL_WIDTH, TOTAL_HEIGHT);
        
        //inner background
        g.setColor(Color.gray);
        g.fillRect(TILE_SIZE*2, TILE_SIZE*2, TOTAL_WIDTH - TILE_SIZE*2, TOTAL_HEIGHT - TILE_SIZE*3);
        
        //skill bar
        g.setColor(Color.white);
        g.fillRect(TILE_SIZE*3, TILE_SIZE*3,TOTAL_WIDTH - TILE_SIZE*4 , TILE_SIZE);
        
        for (int i = 0; i < skillList.size(); i++){
            Skill skill = skillList.get(i);
            g.drawImage(Sprites.SPRITE_MAP.get(skill.getIcon()).getImage(), TILE_SIZE*3 + i*TILE_SIZE, TILE_SIZE*3, TILE_SIZE, TILE_SIZE, rootPane);
        }
        
        //skill list
        g.fillRect(TILE_SIZE*3, TILE_SIZE*5, TOTAL_WIDTH - TILE_SIZE*4 , SKILL_BOX_HEIGHT);
        
        
        //description
        g.fillRect(TILE_SIZE*3, TILE_SIZE*6 + SKILL_BOX_HEIGHT, TOTAL_WIDTH - TILE_SIZE*4 , SKILL_BOX_HEIGHT);
    }
}
