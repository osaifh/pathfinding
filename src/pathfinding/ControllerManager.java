package pathfinding;

/**
 * TEST: singleton-esque class that manages the actorList and the table from the controller, in order to be able to get a reference to it from anywhere.
 * This should be consistent since there should ever be only one controller with one actorList and one table in this iteration of the project.
 * However, later on this design needs to be reviewed and possibly changed
 * TODO: change this for something that works better
 */
public class ControllerManager {
    private static Controller controller;
    
    private ControllerManager(){
        
    }
    
    public static Controller getController(){
        if (controller == null){
            controller = new Controller();
        }
        return controller;
    }
}
