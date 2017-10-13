package pathfinding.actor;

import java.util.ArrayList;
import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Core extends Creature {
    private int resources;
    private ArrayList<Creature> minionList;
    private ArrayList<Node> objectiveList;
    private ArrayList<Node> ongoingObjectiveList;
    /*
        Possible data structures we may need:
            - List of workers
            - List of known resource nodes
            - List of ally structures
            - 
    */
    
    public Core(int x, int y){
        id = 20;
        pos = new Node(x,y);
        minionList = new ArrayList<>();
        objectiveList = new ArrayList<>();
        ongoingObjectiveList = new ArrayList<>();
        resources = 0;
        alive = true;
    }
    
    public ArrayList getObjectiveList(){
        return objectiveList;
    }

    /*
        - If there are no known resource locations, send the scouts out
        - receive information from the scouts
        - send the gatherers to the resource sites
        - manage resources:
            - balance between scouts and gatherers
            - build near resource nodes
        
    */
    
    public void sendObjective(Node n){
        if (n!=null && !notYetEvaluated(n)){ 
            objectiveList.add(n);
            System.out.println("Added an objective ");
            n.print();
        }
    }
    
    public void makeScout(Table t, ActorList objList){
        Scout s = new Scout(getNode().getX(),getNode().getY(),this);
        t.add(s);
        objList.add(s,true);
        minionList.add(s);
    }
    
    public void makeWorker(Table t, ActorList objList){
        Worker s = new Worker(getNode().getX(),getNode().getY(),this);
        t.add(s);
        objList.add(s,true);
        minionList.add(s);
    }
    
    public void objectiveDone(Node objective){
        boolean found = false;
        for (int i = 0; i < ongoingObjectiveList.size() && !found; ++i){
            if (ongoingObjectiveList.get(i).compare(objective)){
                ongoingObjectiveList.remove(i);
                found = true;
            }
        }
    }
    
    public void addResources(int resources){
        this.resources += resources;
    }
    
    public boolean notYetEvaluated(Node n){
        boolean found = false;
        for (int i = 0; i < objectiveList.size() && !found; ++i){
            found = (n.compare(objectiveList.get(i)));
        }
        for (int i = 0; i < ongoingObjectiveList.size() && !found; ++i){
            found = (n.compare(ongoingObjectiveList.get(i)));
        }
        return found;
    }
    
    @Override
    public void simulate(Table t) {
        ++tick_counter;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            for (int i = 0; i < minionList.size(); ++i){
                if (minionList.get(i) instanceof Scout){
                    Scout s = (Scout) minionList.get(i);
                    if (s.getStatus()==0) s.setStatus(1);
                }
                else if (minionList.get(i) instanceof Worker){
                    Worker s = (Worker) minionList.get(i);
                    if (!objectiveList.isEmpty() && s.getStatus()==0){
                        System.out.println("Sending objectives");
                        objectiveList.get(0).print();
                        s.setStatus(2);
                        s.setObjective(objectiveList.get(0));
                        ongoingObjectiveList.add(objectiveList.get(0));
                        objectiveList.remove(0);
                    }
                }
            }
        }
    }

    @Override
    public void print() {
        System.out.println("this is a core");
    }
    
}
