package pathfinding;

import java.util.Random;

public class Node {

    private int x, y;

    public Node(){}
    
    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void generate(int size) {
        Random randomGenerator = new Random();
        x = randomGenerator.nextInt(size);
        y = randomGenerator.nextInt(size);
    }

    boolean compare(Node a) {
        return (a.x == this.x & a.y == this.y);
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setNode(Node n){
        this.x = n.x;
        this.y = n.y;
    }

    public double distance(Node one, Node two) {
        return Math.sqrt(Math.pow(((double) one.x - (double) two.x), 2) + Math.pow(((double) one.y - (double) two.y), 2));
    }
    
    public void print(){
        System.out.println("x es " + x + " y es " + y); 
    }
    
    /*
        UNIFIQUEM ELS VALORS DE LES DIRECCIONS
    
        0 1 2  NW N NE
        3 X 4  W  X  E
        5 6 7  SW S SE
    
    */
    
    public Node direction(int pos, int increment){
        Node dir = new Node();
        dir.setNode(this);
        if (pos == 0) dir.set(dir.get_x()-increment, dir.get_y()-increment);
        if (pos == 1) dir.set(dir.get_x()-increment, dir.get_y());
        if (pos == 2) dir.set(dir.get_x()-increment, dir.get_y()+increment);
        if (pos == 3) dir.set(dir.get_x(), dir.get_y()-increment);
        if (pos == 4) dir.set(dir.get_x(), dir.get_y()+increment);
        if (pos == 5) dir.set(dir.get_x()+increment, dir.get_y()-increment);
        if (pos == 6) dir.set(dir.get_x()+increment, dir.get_y());
        if (pos == 7) dir.set(dir.get_x()+increment, dir.get_y()+increment);
        return dir;
    }
}