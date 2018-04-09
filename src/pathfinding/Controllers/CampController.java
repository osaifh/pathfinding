package pathfinding.Controllers;

import java.util.ArrayList;
import pathfinding.actor.Creatures.Guard;
import pathfinding.auxiliar.Node;

public class CampController {
    private ArrayList<Node> externalNodes;
    private ArrayList<Node> internalNodes;
    private ArrayList<Guard> guards;
    
    public CampController(){
        externalNodes = new ArrayList();
        internalNodes = new ArrayList();
        guards = new ArrayList();
    }
    
    public ArrayList<Node> getExternalNodes(){
        return externalNodes;
    }
    
    public void addInternalNode(Node node){
        internalNodes.add(node);
    }
    
    public void addExternalNode(Node node){
        externalNodes.add(node);
        guards.forEach((guard) -> {
            guard.addPatrolPoint(node);
        });
    }
    
    public void addGuard(Guard guard){
        guards.add(guard);
        externalNodes.forEach((node) ->{
            guard.addPatrolPoint(node);
        });
    }
}
