package pathfinding;

/** 
 * Main class, generates a new controller and runs it.
 * @author 
 * @version indev
 */
public class Pathfinding {
    
    public static void main(String[] args) throws InterruptedException {
        Controller game = ControllerManager.getController();
        game.run();
    }
    
}