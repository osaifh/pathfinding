package pathfinding;

/** 
 * Main class, generates a new controller and runs it.
 * @author Me
 * @version indev
 */
public class Pathfinding {    

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException{
        Controller game = new Controller();
        game.run();
    }
}