package pathfinding.Table;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import pathfinding.Controller;
import pathfinding.Controllers.CampController;
import pathfinding.auxiliar.Node;
import pathfinding.actor.Actor;
import pathfinding.actor.ActorList;
import pathfinding.actor.Interactables.Door;
import pathfinding.actor.Interactables.Interactable;
import pathfinding.actor.LightSource;
import pathfinding.actor.Interactables.genericObject;
import pathfinding.auxiliar.NodeData;
import pathfinding.auxiliar.NodePair;
import pathfinding.auxiliar.PairList;
import pathfinding.auxiliar.Constants;

/**
 * This class is used to make and work with a border made of tiles
 */
public class Table {
    private Tile[][] tab;
    private final int TABLE_SIZE = 1000;
    private final int MAX_ATTEMPTS = 20;
    private static Terrain[] terrainArray;
    
    static {
        terrainArray = new Terrain[10];
        //String name, boolean passable, boolean opaque, int id, float range
        terrainArray[0] = new Terrain("deepWater",false,false,Constants.DEEP_WATER_ID,0f);
        terrainArray[1] = new Terrain("water",false,false,Constants.WATER_ID,0.45f);
        terrainArray[2] = new Terrain("shallowWater",true,false,Constants.SHALLOW_WATER_ID,0.475f);
        terrainArray[3] = new Terrain("sand",true,false,Constants.SAND_ID,0.5f);
        terrainArray[4] = new Terrain("dirt",true,false,Constants.BROWN_ID,0.6f);
        terrainArray[5] = new Terrain("grass",true,false,Constants.GRASS_BASE_ID,0.8f);
        terrainArray[6] = new Terrain("rock",false,true,Constants.ROCK_ID,1f);
    }

    /**
     * Default table constructor
     * Generates a table of a specific size determined by the TABLE_SIZE parameter
     * The terrain is randomly generated using a Perlin noise function
     */
    public Table(){
        tab = new Tile[TABLE_SIZE][TABLE_SIZE];
        int k;
        float[][] tab_gen = generatePerlinNoise(generateWhiteNoise(TABLE_SIZE,TABLE_SIZE),8);
        for (int i = 0; i < TABLE_SIZE; ++i){
            for (int j = 0; j < TABLE_SIZE; ++j){
                k = 0;
                while (tab_gen[i][j] > terrainArray[k].getRange()) ++k;
                tab[i][j] = new Tile(terrainArray[k]);
            }
        }
    }
    
    /**
     * This function is used to determinate whether a node's coordinates are within a valid range in the table
     * @param n is the node we want to checkPassable
     * @return returns true if the node's coordinates are within a valid range in the table
     */
    public boolean valid(Node n){
        return valid(n.getX(),n.getY());
    }

    /**
     * Overload of the valid function, uses the numerical values for coordinates instead of a node
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return returns true if the coordinates are within a valid range in the table
     */
    public boolean valid(int x, int y){
        return (x >= 0 & x<TABLE_SIZE & y >= 0 & y<TABLE_SIZE);
    }

    /**
     * Checks whether a node is passable or not
     * @param n the node we want to check
     * @return returns whether the node is passable or not
     */
    public boolean checkPassable(Node n){
        return checkPassable(n.getX(),n.getY());
    }

    /**
     * Overload that allows you to check specific coordinates instead of a node
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return returns whether the node in the specified coordinates is passable
     */
    public boolean checkPassable(int x, int y){
        if (valid(x,y)){
            return (tab[x][y].isPassable());
        } else return false;
    }
    
    public boolean checkOpaque(Node n){
        return checkOpaque(n.getX(),n.getY());
    }
    
    public boolean checkOpaque(int x, int y){
        if (valid(x,y)){
            return (tab[x][y].isOpaque());
        } else return false;
    }
    
    public boolean checkInteractable(Node n){
        if (valid(n)){
            boolean found = false;
            for (int i = 0; i < tab[n.getX()][n.getY()].getContentSize() && !found; ++i){
                found = (tab[n.getX()][n.getY()].getContent(i) instanceof Interactable);
            }
            return found;
        }
        else return false;
    }

    
    /**
     *
     * @param x
     * @param y
     * @param id
     */
    @Deprecated
    public void add(int x, int y, int id){
        if (valid(x,y)){
            tab[x][y].setID(id);
        }
    }

    /**
     *
     * @param n
     * @param id
     */
    @Deprecated
    public void add(Node n, int id){
        if (valid(n)){
            tab[n.getX()][n.getY()].setID(id);
        }
    }
    
    /**
     *
     * @param x
     * @param y
     * @param obj
     */
    @Deprecated
    public void add(int x, int y, Actor obj){
        if (valid(x,y)){
            if (tab[x][y].contains(obj)){
                tab[x][y].updateMatchingContent(obj);
            }
            else {
                tab[x][y].addContent(obj);
            }
        }
    }

    /**
     *
     * @param obj
     */
    public void add(Actor obj){
        if (valid(obj.getNode().getX(),obj.getNode().getY())){
            if (tab[obj.getNode().getX()][obj.getNode().getY()].contains(obj)){
                tab[obj.getNode().getX()][obj.getNode().getY()].updateMatchingContent(obj);
            }
            else {
                tab[obj.getNode().getX()][obj.getNode().getY()].addContent(obj);
            }
        }
    }

    /**
     *
     * @param n
     */
    @Deprecated
    public void generateWalls(int n){
        Random wallgenerator = new Random();
        for (int i = 0; i <= n; ++i){
            int l = wallgenerator.nextInt(4)+4;            
            int hv = wallgenerator.nextInt(2);
            Node begin = new Node();
            begin.generate(TABLE_SIZE);
            boolean valid = false;
            int attempt = 0;
            while (!valid & attempt<MAX_ATTEMPTS){
                if (tab[begin.getX()][begin.getY()].isPassable()){
                    if (hv == 0){
                        boolean w = true;
                        for (int k = begin.getX(); k < begin.getX() + l; ++k){
                            if (k >= 0 & k < TABLE_SIZE){
                                if (!tab[k][begin.getY()].isPassable()) w = false;
                            } else w = false;
                        }
                        if (w){
                            valid = true;
                            for (int k = begin.getX(); k < begin.getX() + l; ++k){
                                    tab[k][begin.getY()].setWall();
                            }
                        }
                    } else {
                        boolean w = true;
                        for (int k = begin.getY(); k < begin.getY() + l; ++k){
                            if (k >= 0 & k < TABLE_SIZE){
                                if (!tab[begin.getX()][k].isPassable()) w = false;
                            } else w = false;
                        }
                        if (w){
                            valid = true;
                            for (int k = begin.getY(); k < begin.getY() + l; ++k){
                                tab[begin.getX()][k].setWall();
                            }
                        }
                    }
                }
                if (!valid){
                        begin.generate(TABLE_SIZE);
                        ++attempt;
                }
            }
            if (attempt == MAX_ATTEMPTS) System.out.println("Ran out of attempts");
        }
    }

    /**
     *
     * @param n
     * @param id
     */
    @Deprecated
    public void generateObject(int n, int id){
        for (int i = 0; i < n; ++i){
            Node position = new Node();
            position.generate(TABLE_SIZE);
            int attempt = 0;
            do{
                position.generate(TABLE_SIZE);
                ++attempt;
            } while (!tab[position.getX()][position.getY()].isPassable() && attempt < MAX_ATTEMPTS);
            if (attempt == MAX_ATTEMPTS) System.out.println("Ran out of attempts");
            else tab[position.getX()][position.getY()].addContent(new genericObject(id,position.getX(),position.getY()));
         
        }
    }

    /**
     * Sets the terrain ID of a determinate position
     * @param n the position to change
     * @param id the new terrain ID
     */
    @Deprecated
    public void set(Node n, int id){
        if (Table.this.valid(n)){
            tab[n.getX()][n.getY()].setID(id);
            if (id == 1) tab[n.getX()][n.getY()].setPassable(false);
        }
    }

    /**
     * Returns the ID of a determinate position
     * @param n the position to check
     * @return returns the ID of the position
     */
    public int getID(Node n){
        return getID(n.getX(),n.getY());
    }

    /**
     * Returns the ID of a determinate position by it's coordinates
     * @param x the horizontal position
     * @param y the vertical position
     * @return returns the ID of the position 
     */
    public int getID(int x, int y){
        if (valid(x,y)){
            if (tab[x][y].getContentSize()!=0){
                return tab[x][y].getID(0);
            }
            else return tab[x][y].getTerrainID();
        }
        else return -1;
    }

    /**
     * returns the size of the table
     * @return returns the size of the table
     */
    public int getSize(){
        return TABLE_SIZE;
    }

    /**
     *
     * @param n
     * @return
     */
    public Tile getTile(Node n){
        return getTile(n.getX(),n.getY());
    }

    /**
     * Returns the tile of a determinate position
     * @param x the horizontal coordinate
     * @param y the vertial coordinate
     * @return the
     */
    public Tile getTile(int x, int y){
        if (valid(x,y)){
            return (tab[x][y]);
        }
        else return null;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Actor getActor(int x, int y){
        if (valid(x,y)){
            return tab[x][y].getContent(0);
        }
        else return null;
    }

    /**
     *
     * @param n
     * @return
     */
    public Actor getActor(Node n){
        return getActor(n.getX(),n.getY());
    }

    /**
     *
     * @param obj
     */
    public void updateObject(Actor obj){
        if (obj!=null){
            tab[obj.getNode().getX()][obj.getNode().getX()].updateMatchingContent(obj);
        }
    }

    private float[][] generateWhiteNoise(int width, int height){
        Random random = new Random();
        float[][] noise = new float[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                noise[i][j] = (float)random.nextDouble() % 1;
            }
        }
        return noise;
    }

    private float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount){
        int width = baseNoise.length;
        int height = baseNoise[0].length;
        float[][][] smoothNoise = new float[octaveCount][][];
        float persistance = 0.5f;
        //generate smooth noise
        for (int i = 0; i < octaveCount; i++){
            smoothNoise[i] = generateSmoothNoise(baseNoise, i);
        }
        float[][] perlinNoise = new float[width][height];
        float amplitude = 1.0f;
        float totalAmplitude = 0.0f;
        //blend noise together
        for (int octave = octaveCount - 1; octave >= 0; octave--)
        {
            amplitude *= persistance;
            totalAmplitude += amplitude;
            for (int i = 0; i < width; i++){
                for (int j = 0; j < height; j++){
                    perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
                }
            }
        }

        //normalisation
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                perlinNoise[i][j] /= totalAmplitude;
            }
        }

       return perlinNoise;
    }

    private float[][] generateSmoothNoise(float[][] baseNoise, int octave){
        int width = baseNoise.length;
        int height = baseNoise[0].length;

        float[][] smoothNoise = new float [width][height];

        int samplePeriod = 1 << octave; // calculates 2 ^ k
        float sampleFrequency = 1.0f / samplePeriod;

        for (int i = 0; i < width; i++)
        {
           //calculate the horizontal sampling indices
           int sample_i0 = (i / samplePeriod) * samplePeriod;
           int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
           float horizontal_blend = (i - sample_i0) * sampleFrequency;

           for (int j = 0; j < height; j++)
           {
              //calculate the vertical sampling indices
              int sample_j0 = (j / samplePeriod) * samplePeriod;
              int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
              float vertical_blend = (j - sample_j0) * sampleFrequency;

              //blend the top two corners
              float top = interpolate(baseNoise[sample_i0][sample_j0],
                 baseNoise[sample_i1][sample_j0], horizontal_blend);

              //blend the bottom two corners
              float bottom = interpolate(baseNoise[sample_i0][sample_j1],
                 baseNoise[sample_i1][sample_j1], horizontal_blend);

              //final blend
              smoothNoise[i][j] = interpolate(top, bottom, vertical_blend);
           }
        }

        return smoothNoise;
}

    private float interpolate(float x0, float x1, float alpha){
        return x0 * (1 - alpha) + alpha * x1;
    }

    /**
     *
     * @param time
     */
    public void dark(int time){
        int light_level = 0;
        if (time >=0 && time<1000){
            light_level=100;
        } 
        else if (time>=1000 && time <= 1200){
            light_level=100-((time/10-100)*5);
        } 
        else if (time>=2200 && time <= 2400){
            light_level=(time/10-220)*5;
        }
        for (int i = 0; i < TABLE_SIZE; ++i){
            for (int j = 0; j < TABLE_SIZE; ++j){
                tab[i][j].setLight(light_level);
            }
        }
    }
    
    /**
     * Sets the light level of all tiles to the maximum value
     */
    public void light(){
        for (int i = 0; i < TABLE_SIZE; ++i){
            for (int j = 0; j < TABLE_SIZE; ++j){
                tab[i][j].setLight(100);
            }
        }
    }

    private static Comparator<NodeData> node_comparator = (NodeData n1, NodeData n2) -> {
        if (n1.getTotal() > n2.getTotal()) return 1;
        else if (n1.getTotal() < n2.getTotal()) return -1;
        else return 0;
    };
    
    public Node[] iBFS (Node act_pos, Node target) {
        return iBFS(act_pos, target, true, false);
    }
            
    public Node[] iBFS (Node act_pos, Node target, boolean allowDiagonal, boolean allowDoors) {
        if (!valid(target) || !getTile(target).isPassable()) return null; //early exit
        PriorityQueue qpath = new PriorityQueue(11,node_comparator);
        PairList visitats = new PairList();
        Node source = act_pos;
        Node current = act_pos;
        NodePair current_par = new NodePair(current);
        visitats.add(current_par);
        NodeData current_data = new NodeData(current_par,source,target);
        qpath.add(current_data);
        int limit = 10000;
        boolean first = true;
        while (!qpath.isEmpty() & limit > 0){
            if(!first){
                current_data = (NodeData)qpath.poll();
                current_par = current_data.getNodePar();
                current = current_data.getNode();
            } else first = false;
            if (current.equals(target)) break;
            else {
                for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (!(i==0 & j==0) &&
                        //is passable or is a door if doors are allowed
                        (
                        checkPassable(current.getX() + i,current.getY() + j)

                        || (allowDoors && (
                            (tab[current.getX()+i][current.getY()+j].getContent() != null)
                            &&
                            (tab[current.getX()+i][current.getY()+j].getContent() instanceof Door)
                            )
                        )
                        //isn't a diagonal if diagonals are not allowed
                        && (!allowDiagonal | (i != 0 && j != 0)))
                        ){
                        
                        Node temp = new Node();
                        temp.set(current.getX() + i, current.getY() + j);
                        if (!visitats.findNode(temp)){
                            NodePair new_par = new NodePair(temp);
                            new_par.setSource(current_par);
                            NodeData new_data = new NodeData(new_par,source,target);
                            qpath.add(new_data);
                            visitats.add(new_par);
                        }
                    }
                }
                }
            }
            --limit;
        }
        if (!target.equals(current)) return null;
        else{
            Node[] path = visitats.tracePath(current_par);
            return path;
        }
    }
    
    //used only for testing, will be removed later
    public Node[] mark_iBFS (Node act_pos, Node target, Controller controller) {
        if (!valid(target) || !getTile(target).isPassable()) return null; //early exit
        PriorityQueue qpath = new PriorityQueue(11,node_comparator);
        PairList visitats = new PairList();
        Node source = act_pos;
        Node current = act_pos;
        NodePair current_par = new NodePair(current);
        visitats.add(current_par);
        NodeData current_data = new NodeData(current_par,source,target);
        qpath.add(current_data);
        int limit = 10000;
        boolean first = true;
        while (!qpath.isEmpty() & limit > 0){
            tab[current.getX()][current.getY()].setID(Constants.SHALLOW_WATER_ID);
            controller.gameStep();
            try {
                Thread.sleep(100);
            }
            catch (Exception ex){
                
            }
            tab[current.getX()][current.getY()].setID(2);
            if(!first){
                current_data = (NodeData)qpath.poll();
                current_par = current_data.getNodePar();
                current = current_data.getNode();
            } else first = false;
            if (current.equals(target)) break;
            else {
                for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (!(i==0 & j==0) && checkPassable(current.getX() + i,current.getY() + j)){
                        Node temp = new Node();
                        temp.set(current.getX() + i, current.getY() + j);
                        if (!visitats.findNode(temp)){
                            NodePair new_par = new NodePair(temp);
                            new_par.setSource(current_par);
                            NodeData new_data = new NodeData(new_par,source,target);
                            qpath.add(new_data);
                            visitats.add(new_par);
                        }
                    }
                }
                }
            }
            --limit;
        }
        if (!target.equals(current)) return null;
        else{
            Node[] path = visitats.tracePath(current_par);
            for (int i = 0; i < path.length; i++){
                tab[path[i].getX()][path[i].getY()].setID(Constants.BLACK_ID);
            }  
            return path;
        }
    }
    
    public Node generateCamp(CampController campController){
        int campSize = 50;
        Node node = new Node();
        boolean valid;
        //used to remember which nodes we have already checked and are not valid so we can skip some of the cases when looking for a valid node
        boolean[][] checkTable = new boolean[TABLE_SIZE][TABLE_SIZE];
        do {
            node.generate(TABLE_SIZE);
            //if the node isn't in the table of the nodes we have already checked AND isn't too close to the border
            valid = (!checkTable[node.getX()][node.getY()] 
                    && node.getX()<(TABLE_SIZE - campSize)
                    && node.getY()<(TABLE_SIZE - campSize));
            if (valid){
                for (int i = node.getX(); i < node.getX() + campSize && valid; i++){
                    for (int j = node.getY(); j < node.getY() + campSize && valid; j++){
                        if (!tab[i][j].isPassable()){
                            valid = false;
                            //sets all the nodes that can't be valid due to having a non-passable terrain to true so we don't have to check them again
                            for (int ii = i; ii >= i - campSize && ii >= 0; ii--){
                                for (int jj = j; jj >= j - campSize && jj >= 0; jj--){
                                    checkTable[ii][jj] = true;
                                }
                            }
                        }
                    }
                }
            }
        } while (!valid);
        generateCamp(node.getX(),node.getY(),campController);
        return node;
    }
    
    //put this shit in another class
    public void generateCamp(int x, int y, CampController campController){
        int campSize = 50;
        int squareSize = 10;
        boolean valid;
        Node start = new Node(x,y);
        int towerSize = 4;
        
        //generates the towers
        //NW
        Node auxNode = new Node(start);
        generateSquare(towerSize, auxNode.getX(), auxNode.getY(),1);
        auxNode.add(towerSize/2, towerSize/2);
        campController.addExternalNode(auxNode);
        
        //NE
        auxNode = new Node(start.getX()+(campSize - towerSize),start.getY());
        generateSquare(towerSize, auxNode.getX(), auxNode.getY(),2);
        auxNode.add(towerSize/2, towerSize/2);
        campController.addExternalNode(auxNode);
        
        //SE
        auxNode = new Node(start.getX()+(campSize - towerSize), start.getY()+(campSize - towerSize));
        generateSquare(towerSize,auxNode.getX(), auxNode.getY() ,4);
        auxNode.add(towerSize/2, towerSize/2);
        campController.addExternalNode(auxNode);
        
        //SW
        auxNode = new Node(start.getX(), start.getY()+(campSize - towerSize));
        generateSquare(towerSize, auxNode.getX(), auxNode.getY() ,3);
        auxNode.add(towerSize/2, towerSize/2);
        campController.addExternalNode(auxNode);
        
        

        int[][] campCenter = generateCampCenter();
        
        for (int i = 0; i < campCenter.length; i++){
            for (int j = 0; j < campCenter.length; j++){
                int id = campCenter[i][j];
                if (id != 0){
                    int xSquare = start.getX() + squareSize + 1 + i * 11;
                    int ySquare = start.getY() + squareSize + 1 + j * 11;
                    
                    switch (id) {
                        case 1:
                            int HQSize = 10;
                            generateSquare(HQSize,xSquare,ySquare,0);
                            break;
                        case 2:
                            int dormSize = 5;
                            generateSquare(dormSize,xSquare,ySquare,0);
                            break;
                        case 3:
                            int kitchenSize = 5;
                            generateSquare(kitchenSize,xSquare,ySquare,0);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    
    public void generateSquare(int size, int x, int y, int orientation){
        for (int i = 0; i <= size; i++){
            tab[x+i][y].setWall();
            tab[x+i][y+size].setWall();
            tab[x][y+i].setWall();
            tab[x+size][y+i].setWall();
        }
        for (int i = 1; i < size; i++){
            for (int j = 1; j < size; j++){
                tab[x+i][y+j].setID(Constants.GROUND_ID);
            }
        }
        if (orientation != 0){
            int xx, yy;
            switch (orientation) {
                case 1:
                    xx = x + (size - 1);
                    yy = y + size;
                    break;
                case 2:
                    xx = x + 1;
                    yy = y + size;
                    break;
                case 3:
                    xx = x + (size - 1);
                    yy = y;
                    break;
                case 4:
                    xx = x + 1;
                    yy = y;
                    break;
                default:
                    xx = x;
                    yy = y;
                    break;
            }
            Door door = new Door(xx,yy, this);
            tab[xx][yy].addContent(door);
            tab[xx][yy].setID(Constants.GROUND_ID);
            //door.interact(this);
        }
    }
    
    public int[][] generateCampCenter(){
        int[][] campCenter = new int[3][3];
        int HQ = 1;
        int dorms = 2;
        int kitchen = 3;
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(3);
            y = random.nextInt(3);
        } while (!(x==1 || y==1));
        campCenter[x][y] = HQ;
        boolean valid;
        for (int id = 2; id <= 3; id++){ 
            do {
                valid = true;
                x = random.nextInt(3);
                y = random.nextInt(3);
                for (int i = -1; i < 2 && valid; i++){
                    for (int j = -1; j < 2 && valid; j++){
                        if (!((i!=0) && (j!=0))){
                           int xx = x + i;
                           int yy = y + j;
                           if (xx >= 0 && xx <=2 && yy >= 0 && yy <= 2){
                               if (campCenter[xx][yy]!=0) valid = false;
                           }
                        }
                    }
                }
            } while (!valid);
            campCenter[x][y] = id;
        }
        return campCenter;
    }
    
    public void generateSquareHouse(int size, int ndoors, ActorList lightList){
        generateSquareHouse(null,size,ndoors,lightList);
    }
    
    public void generateSquareHouse(Node n, int size, int ndoors, ActorList lightList){
        Node start = new Node();
        Random random = new Random();
        if (n==null){
            Node currentNode;
            boolean valid;

            do {
                ArrayList<Node> evaluatedNodes = new ArrayList<Node>();
                do {
                    start.generate(TABLE_SIZE);
                } while (!(valid(start) && checkPassable(start) && !evaluatedNodes.contains(start)));
                evaluatedNodes.add(start);
                currentNode = start.getNodeCopy();
                valid = true;
                for (int i = 0; i < size && valid; ++i){
                    for (int j = 0; j < size && valid; ++j){
                        currentNode.set(currentNode.getX(), currentNode.getX()+1);
                        valid = checkPassable(currentNode);
                    }
                    currentNode.set(currentNode.getX()+1, currentNode.getX());
                }
            } while (!valid);
        } else {
            start = n;
        }
        
        //walls
        for (int i = 0; i <= size; ++i){
            tab[start.getX()+i][start.getY()].setWall();
            tab[start.getX()+i][start.getY()+size].setWall();
            tab[start.getX()][start.getY()+i].setWall();
            tab[start.getX()+size][start.getY()+i].setWall();
        }
        
        //floor
        for (int i = 1; i < size; ++i){
            for (int j = 1; j < size; ++j){
                tab[start.getX()+i][start.getY()+j].setID(109);
            }
        }
        
        //doors
        for (int i = 0; i < ndoors; ++i){
            boolean checked = true;
            Node d;
            do {
                int x = random.nextInt(size);
                int y = random.nextInt(size);
                if (random.nextInt(2)==0){
                    if (random.nextInt(2)==0) d = new Node(start.getX(), start.getY()+y);
                    else d = new Node(start.getX()+size, start.getY()+y);
                }
                else {
                    if (random.nextInt(2)==0) d = new Node(start.getX()+x, start.getY());
                    else d = new Node(start.getX()+x, start.getY()+size);
                }
                if (!getTile(d).isEmpty()) checked = false;
                if ((d.getX()-start.getX())==(d.getY()-start.getY())) checked = false;
            } while (!checked);
            Door door = new Door(d.getX(),d.getY(), this);
            add(door);
            tab[d.getX()][d.getY()].setID(109);
        }
        //lights
        lightList.add(new LightSource(3*size/4,start.getX()+1,start.getY()+1),true);
        lightList.add(new LightSource(3*size/4,start.getX()+size-1,start.getY()+1),true);
        lightList.add(new LightSource(3*size/4,start.getX()+1,start.getY()+size-1),true);
        lightList.add(new LightSource(3*size/4,start.getX()+size-1,start.getY()+size-1),true);
    }
    
    public void generateTown(Node n, ActorList lightList){
        Node start = new Node();
        Random random = new Random();
        int nHouses = random.nextInt(4)+5;
        int[] HouseSizes = new int[nHouses];
        int currentWidth, currentHeight;
        currentWidth = currentHeight = 0;
        int totalSize = 0;
        final int SPACE = 5;
        for (int i = 0; i < nHouses; ++i){
            HouseSizes[i] = random.nextInt(11)+5;
            totalSize = HouseSizes[i]+SPACE;
        }
        totalSize -= SPACE;
        if (n==null){
            Node currentNode;
            boolean valid;

            do {
                ArrayList<Node> evaluatedNodes = new ArrayList<Node>();
                do {
                    start.generate(TABLE_SIZE);
                } while (!(valid(start) && checkPassable(start) && !evaluatedNodes.contains(start)));
                evaluatedNodes.add(start);
                currentNode = start.getNodeCopy();
                valid = true;
                for (int i = 0; i < totalSize && valid; ++i){
                    for (int j = 0; j < totalSize && valid; ++j){
                        currentNode.set(currentNode.getX(), currentNode.getX()+1);
                        valid = checkPassable(currentNode);
                    }
                    currentNode.set(currentNode.getX()+1, currentNode.getX());
                }
            } while (!valid);
        } else {
            start = n;
        }
        int currentX = start.getX();
        int currentY = start.getY();
        int lastX = currentX;
        int lastY = currentY;
        for (int i = 0; i < nHouses; ++i){
            generateSquareHouse(new Node(currentX,currentY), HouseSizes[i], HouseSizes[i]/10 + 1, lightList);
            if (i!=0){
                if (i%4!=0){
                    if (currentWidth > currentHeight){
                        currentX = lastX;
                        lastY = currentY;
                        currentY += HouseSizes[i]+SPACE;
                        currentHeight += HouseSizes[i]+SPACE;

                    }

                    else if (currentHeight > currentWidth){
                        currentY = lastY;
                        lastX = currentX;
                        currentX += HouseSizes[i]+SPACE;
                        currentWidth += HouseSizes[i]+SPACE;
                    }
               }
               else {
                    lastX = currentX;
                    lastY = currentY;
                    currentX += HouseSizes[i]+SPACE;
                    currentY += HouseSizes[i]+SPACE;
                    currentWidth += HouseSizes[i]+SPACE;
                    currentHeight += HouseSizes[i]+SPACE;
               }
            }
            else {
                lastX = currentX;
                lastY = currentY;
                currentX += HouseSizes[i]+SPACE;
                currentWidth += HouseSizes[i]+SPACE;
            }
            
        }
    }
}