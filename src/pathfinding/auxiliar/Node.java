package pathfinding.auxiliar;

import java.util.Random;
import java.util.HashMap;
import pathfinding.Table.Table;
/**
 *
 * @author Alumne
 */
public class Node {

    private int x, y;
    private Random randomGenerator = new Random();
    private static HashMap<Integer,Node> DIRECTIONS;
    static {
        DIRECTIONS = new HashMap<>();
        DIRECTIONS.put(0,new Node(-1,-1));
        DIRECTIONS.put(1,new Node(-1,0));
        DIRECTIONS.put(2,new Node(-1,1));
        DIRECTIONS.put(3,new Node(0,-1));
        DIRECTIONS.put(4,new Node(0,1));
        DIRECTIONS.put(5,new Node(1,-1));
        DIRECTIONS.put(6,new Node(1,0));
        DIRECTIONS.put(7,new Node(1,1));
    }
    /*
        0 1 2  NW N NE
        3 X 4  W  X  E
        5 6 7  SW S SE
    */
    
    
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
    
    public Node(Node n){
        this.x = n.x;
        this.y = n.y;
    }
    
    /**
     *
     * @param size
     */
    public void generate(int size){
        x = randomGenerator.nextInt(size);
        y = randomGenerator.nextInt(size);
    }
    
    /**
     *
     * @param size
     * @param xx
     * @param xy
     * @param yx
     * @param yy
     */
    public void generate(int size, int xx, int xy, int yx, int yy) {
        do {
            x = xx + randomGenerator.nextInt(size);
            y = xy + randomGenerator.nextInt(size);
        } while (!(x <= yx && y <= yy));
    }

    /**
     *
     * @param a
     * @return
     */
    public boolean compare(Node a) {
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
    public Node getNodeCopy() {
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
    public void setToNode(Node n) {
        this.x = n.x;
        this.y = n.y;
    }

    public void add(Node n){
        this.x += n.x;
        this.y += n.y;
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
    
    public static double ManhattanDistance(Node one, Node two){
        return (Math.abs(one.x - two.x)+Math.abs(one.y - two.y));
    }
    
    /**
     *
     */
    public void print() {
        System.out.println("[x=" + x + ",y=" + y + "]"); 
    }
    
    @Override
    public String toString(){
        return ("[x=" + x + ",y=" + y + "]");
    }
    
    /**
     *
     * @param dir
     * @param increment
     */
    public void moveDirection (int dir, int increment) {
        for (int i = 0; i < increment; ++i) this.add(DIRECTIONS.get(dir)); 
    }
    
    public Boolean iMove(Table t, int dir){
        Node n = new Node(this);
        n.add(DIRECTIONS.get(dir));
        if (t.checkPassable(n)){
            this.setToNode(n);
            return true;
        }
        return false;
    }
    
    public void nodeMove(Table t, int dir){
        this.add(DIRECTIONS.get(dir));
    }
    
    public static Node[] getDirections(){
        Node[] directions = new Node[8];
        for (int i = 0; i < 8; ++i){
            directions[i] = DIRECTIONS.get(i);
        }
        return directions;
    }
    
    public static Node[] getDirectionsN(){
        Node[] directions = new Node[4];
        Node[] directions_8 = getDirections();
        int j = 0;
        for (int i = 0; i < 8; ++i){
            if (i != 0 && i != 2 && i !=5 && i != 7){
                directions[j] = directions_8[i];
                ++j;
            }
        }
        return directions;
    }
    
    public int relativeDirection(Node target){
        if (target.x<x && target.y<y) return 0;
        if (target.x<x && target.y==y) return 1;
        if (target.x<x && target.y>y) return 2;
        if (target.x==x && target.y<y) return 3;
        if (target.x==x && target.y>y) return 4;
        if (target.x>x && target.y<y) return 5;
        if (target.x>x && target.y==y) return 6;
        if (target.x>x && target.y>y) return 7;
        return -1;
    }
}