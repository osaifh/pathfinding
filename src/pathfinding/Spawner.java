package pathfinding;

public class Spawner extends Objecte {
    private int spawn_ID, interval, number, counter;
    
    public Spawner(int spawn_ID, int interval, int number){
        this.spawn_ID = spawn_ID;
        this.interval = interval;
        this.number = number;
        counter = 0;
        set_id(5);
    }
    
    @Override
    public void simulate(Table tab, Objecte_list llista){
        ++counter;
        if (counter == interval){
            System.out.println("Spawning a new item");
            tab.generate_object(number,spawn_ID);
            counter = 0;
        }
    }
    
    @Override
    public void print(){
        System.out.println("spawn ID " + spawn_ID + " interval " + interval + " number " + number + " current counter " + counter);
    }
}
