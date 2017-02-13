package pathfinding;

import java.util.Random;

/**
 *
 * @author Alumne
 */
public class Node {

    private int x, y;

    /**
     *
     */
    public Node(){}
    
    /**
     *
     * @param x
     * @param y
     */
    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     *
     * @param size
     */
    public void generate(int size) {
        Random randomGenerator = new Random();
        x = randomGenerator.nextInt(size);
        y = randomGenerator.nextInt(size);
    }

    boolean compare(Node a) {
        return (a.x == this.x & a.y == this.y);
    }

    /**
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return
     */
    public int getY() {
        return y;
    }
    
    /**
     *
     * @return
     */
    public Node getNodeCopy(){
        return new Node(x,y);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     *
     * @param n
     */
    public void setNode(Node n){
        this.x = n.x;
        this.y = n.y;
    }

    /**
     *
     * @param one
     * @param two
     * @return
     */
    public static double distance(Node one, Node two) {
        return Math.sqrt(Math.pow(((double) one.x - (double) two.x), 2) + Math.pow(((double) one.y - (double) two.y), 2));
    }
    
    /**
     *
     */
    public void print(){
        System.out.println("x es " + x + " y es " + y); 
    }
    
    /*
        UNIFIQUEM ELS VALORS DE LES DIRECCIONS
    
        0 1 2  NW N NE
        3 X 4  W  X  E
        5 6 7  SW S SE
    
    */

    /**
     *
     * @param pos
     * @param increment
     * @return
     */

    
    public Node direction(int pos, int increment){
        Node dir = new Node();
        dir.setNode(this);
        if (pos == 0) dir.set(dir.getX()-increment, dir.getY()-increment);
        if (pos == 1) dir.set(dir.getX()-increment, dir.getY());
        if (pos == 2) dir.set(dir.getX()-increment, dir.getY()+increment);
        if (pos == 3) dir.set(dir.getX(), dir.getY()-increment);
        if (pos == 4) dir.set(dir.getX(), dir.getY()+increment);
        if (pos == 5) dir.set(dir.getX()+increment, dir.getY()-increment);
        if (pos == 6) dir.set(dir.getX()+increment, dir.getY());
        if (pos == 7) dir.set(dir.getX()+increment, dir.getY()+increment);
        return dir;
    }
}