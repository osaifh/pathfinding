package pathfinding;

/**
 *
 * @author Alumne
 */
public class Spawner extends Subject {
    private int spawn_ID, interval, number, counter;
    
    /**
     *
     * @param spawn_ID
     * @param interval
     * @param number
     */
    public Spawner(int spawn_ID, int interval, int number){
        this.spawn_ID = spawn_ID;
        this.interval = interval;
        this.number = number;
        counter = 0;
        setID(5);
    }
    
    /**
     *
     * @param tab
     * @param delta
     */
    @Override
    public void simulate(Table tab, double delta){
        ++counter;
        if (counter == interval){
            System.out.println("Spawning a new item");
            tab.generateObject(number,spawn_ID);
            counter = 0;
        }
    }
    
    /**
     *
     */
    @Override
    public void print(){
        System.out.println("spawn ID " + spawn_ID + " interval " + interval + " number " + number + " current counter " + counter);
    }
}
