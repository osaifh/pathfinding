package pathfinding.auxiliar;

import pathfinding.Table.Table;

/**
 *
 * @author 
 */
public class Node {
    
    private int x, y;
    
    /**
     * Empty constructor
     */
    public Node(){}
    
    /**
     * Constructor with x,y values
     * @param x
     * @param y
     */
    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructor that makes a copy of the values of the n Node
     * @param n the node to copy
     */
    public Node(Node n){
        this.x = n.x;
        this.y = n.y;
    }
    
    /**
     * sets the values of the node to random given a range
     * @param range the range of values from 0 to range - 1
     */
    public void generate(int range){
        x = Constants.RANDOM.nextInt(range);
        y = Constants.RANDOM.nextInt(range);
    }
    
    /**
     * sets the values of the node to random given a range and a given set of 4 coordinates
     * @param range
     * @param xx
     * @param xy
     * @param yx
     * @param yy
     */
    public void generate(int range, int xx, int xy, int yx, int yy) {
        do {
            x = xx + Constants.RANDOM.nextInt(range);
            y = xy + Constants.RANDOM.nextInt(range);
        } while (!(x <= yx && y <= yy));
    }

    /**
     *
     * @param a
     * @return
     */
    @Override
    public boolean equals(Object a) {
        return (((Node)a).x == this.x & ((Node)a).y == this.y);
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
    
    public void add(int x, int y){
        this.x += x;
        this.y += y;
    }
    
    /**
     *
     * @param one
     * @param two
     * @return
     */
    public static double distance(Node one, Node two) {
        if (one.equals(two)) return 0.0f;
        else 
            return Math.sqrt(Math.pow(((double) one.x - (double) two.x), 2) + Math.pow(((double) one.y - (double) two.y), 2));
    }
    
    public static double ManhattanDistance(Node one, Node two){
        if (one.equals(two)) return 0;
        else 
            return (Math.abs(one.x - two.x)+Math.abs(one.y - two.y));
    }
    
    /**
     *
     * @param dir
     * @param increment
     */
    public void moveDirection (int dir, int increment) {
        for (int i = 0; i < increment; ++i) this.add(Constants.DIRECTIONS.get(dir)); 
    }
    
    public Boolean iMove(Table t, int dir){
        this.add(Constants.DIRECTIONS.get(dir));
        return t.checkPassable(this);
    }
    
    public Boolean iMove(Table t, Node n){
        if (t.checkPassable(n)){
            this.setToNode(n);
            return true;
        }
        return false;
    }
    
    public void nodeMove(Table t, int dir){
        this.add(Constants.DIRECTIONS.get(dir));
    }
    
    public void nodeMove(Table t, Node n){
        this.setToNode(n);
    }
    
    public static Node[] getDirections(){
        Node[] directions = new Node[8];
        for (int i = 0; i < 8; ++i){
            directions[i] = Constants.DIRECTIONS.get(i);
        }
        return directions;
    }
    
    /**
     * Returns only the non-diagonal directions (N,S,E,W)
     * @return an array of nodes with the non-diagonal directions 
     */
    public static Node[] getDirectionsN(){
        Node[] directions = new Node[4];
        directions[0] = Constants.DIRECTIONS.get(Constants.N);
        directions[1] = Constants.DIRECTIONS.get(Constants.W);
        directions[2] = Constants.DIRECTIONS.get(Constants.E);
        directions[3] = Constants.DIRECTIONS.get(Constants.S);
        return directions;
    }
    
    /**
     * Returns the relative direction from a node ot another
     * @param target the node to equals to
     * @return the relative direction to target
     */
    public int relativeDirection(Node target){
        if (target.x<x   && target.y<y )   return Constants.NW;
        if (target.x<x   && target.y==y)   return Constants.N;
        if (target.x<x   && target.y>y )   return Constants.NE;
        if (target.x==x  && target.y<y )   return Constants.W;
        if (target.x==x  && target.y>y )   return Constants.E;
        if (target.x>x   && target.y<y )   return Constants.SW;
        if (target.x>x   && target.y==y)   return Constants.S;
        if (target.x>x   && target.y>y )   return Constants.SE;
        return -1;
    }
    
    @Override
    public String toString(){
        return ("[x=" + x + ",y=" + y + "]");
    }
    
}