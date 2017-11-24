/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinding.Table;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alumne
 */
public class mapThread extends Thread {
    private boolean ready;
    private int lowerRange, upperRange;
    private Camera cam;

    public mapThread(Camera cam, int lowerRange, int upperRange){
        this.cam = cam;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }
    
    @Override
    public void run(){
        while (true){
            ready = false;
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
            }
            //cam.drawMap(lowerRange,upperRange);
            ready = true;
        }
    }

    public boolean isReady(){
        return ready;
    }
}
