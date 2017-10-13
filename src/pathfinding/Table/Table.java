package pathfinding.Table;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import pathfinding.auxiliar.Node;
import pathfinding.actor.Actor;
import pathfinding.actor.Interactable;
import pathfinding.actor.genericObject;
import pathfinding.auxiliar.NodeData;
import pathfinding.auxiliar.NodePair;
import pathfinding.auxiliar.PairList;

/**
 * This class is used to make and work with a border made of tiles
 */
public class Table {
    private Tile[][] tab;
    private final int TABLE_SIZE = 100;
    private final int MAX_ATTEMPTS = 20;
    private enum Edge {
        none, NW, NE, SW, SE;
    }

    /**
     * Default table constructor
     * Generates a table of a specific size determined by the TABLE_SIZE parameter
     * The terrain is randomly generated using a Perlin noise function
     * After generating it, another function is called to properly set the edges of the tiles
     */
    public Table(){
        tab = new Tile[TABLE_SIZE][TABLE_SIZE];
        int aux;
        float[][] tab_gen = generatePerlinNoise(generateWhiteNoise(TABLE_SIZE,TABLE_SIZE),4);
        for (int i = 0; i < TABLE_SIZE; ++i){
            for (int j = 0; j < TABLE_SIZE; ++j){
                if (tab_gen[i][j]>0.5)aux=5;
                else aux = 0;
                tab[i][j] = new Tile(aux);
            }
        }
        updateEdges(0,0,TABLE_SIZE,TABLE_SIZE);
    }
    
    /**
     * This function is used to determinate whether a node's coordinates are within a valid range in the table
     * @param n is the node we want to checkPassable
     * @return returns true if the node's coordinates are within a valid range in the table
     */
    public boolean valid(Node n){
        if (n!=null){
            return (n.getX() >= 0 & n.getX()<TABLE_SIZE & n.getY() >= 0 & n.getY()<TABLE_SIZE);
        } else return false;
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
     * @param n n is the node we want to check
     * @return returns whether the node is passable or not
     */
    public boolean checkPassable(Node n){
        if (valid(n)){
            return (tab[n.getX()][n.getY()].isPassable());
        }
        else return false;
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
    
    public boolean checkOpaque(int x, int y){
        if (valid(x,y)){
            return (tab[x][y].isOpaque());
        } else return false;
    }
    
    public boolean checkOpaque(Node n){
        if (valid(n)){
            return (tab[n.getX()][n.getY()].isOpaque());
        }
        else return false;
    }

    /**
     * Returns true if the position
     * @param act
     * @param exc
     * @return 
     
    public boolean checkException(Node act, int exc){
        if (valid(act)){
            return (tab[act.getX()][act.getY()].isPassable() || tab[act.getX()][act.getY()].getID()==exc);
        }
        else return false;
    } */

    /**
     * 
     * @param x
     * @param y
     * @param exc
     * @return 
     */
    /*
    public boolean checkException(int x, int y, int exc){
        if (valid(x,y)){
            boolean found = false
            for (int i = 0; i < tab[x][y].getContentSize(); ++i){
                
            }
            return ((tab[x][y].isPassable()) || tab[x][y].getID()==exc);
        }
        else return false;
    }*/
    
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
     * @param cam
     */
    public void generateBoxes(int n, Camera cam){
        Random boxgenerator = new Random();
        for (int i = 0; i < n; ++i){
            int width, height;
            Node begin = new Node();
            do {
            begin.generate(TABLE_SIZE);
            } while (checkWall(begin.getX(),begin.getY()));
            width = boxgenerator.nextInt(6)+5;
            height = boxgenerator.nextInt(6)+5;
            iGenerateBoxes(width,height,begin,cam);
        }
    }

    //PROTOTYPE
    private void iGenerateBoxes(int width, int height, Node begin, Camera cam){
        Random boxgenerator = new Random();
        int num_doors = boxgenerator.nextInt(5)+1;
        int num_squares = height*2 + width*2;
        int curr_doors = 0;
        int roll;
        int entropy = boxgenerator.nextInt(num_squares);
        Node act = new Node();
        //generate horizontal walls
        for (int i = 0; i < width; ++i){
            if (checkPassable(begin.getX()+i,begin.getY())){
                roll = boxgenerator.nextInt(num_squares)-entropy;
                if (roll > 0 || curr_doors == num_doors) tab[begin.getX()+i][begin.getY()].setWall();
                else {
                    entropy = 0;
                    if (curr_doors > 0){
                        System.out.println("Recursive call");
                        int n_width = boxgenerator.nextInt(width)-1;
                        int n_height = boxgenerator.nextInt(height)-1;
                        Node n_begin = new Node(begin.getX()+i-(n_width/2),begin.getY()-(n_height));
                        if (n_width>2 && n_height>2) iGenerateBoxes(n_width, n_height, n_begin,cam);
                    }
                    ++curr_doors;
                }
                ++entropy;
            }
            if (checkPassable(begin.getX()+i,begin.getY()+height-1)){
                roll = boxgenerator.nextInt(num_squares)-entropy;
                if (roll > 0 || curr_doors == num_doors) tab[begin.getX()+i][begin.getY()+height-1].setWall();
                else {
                    entropy = 0;
                    if (curr_doors > 0){
                        System.out.println("Recursive call");
                        int n_width = boxgenerator.nextInt(width)-1;
                        int n_height = boxgenerator.nextInt(height)-1;
                        Node n_begin = new Node(begin.getX()+i-(n_width/2),begin.getY()+height-1);
                        if (n_width>2 && n_height>2) iGenerateBoxes(n_width, n_height, n_begin,cam);
                    }
                    ++curr_doors;
                }
                ++entropy;
            }
        }
        //generate vertical walls
        for (int i = 0; i < height; ++i){
            if (checkPassable(begin.getX(),begin.getY()+i)){
                roll = boxgenerator.nextInt(num_squares)-entropy;
                if (roll > 0 || curr_doors == num_doors) tab[begin.getX()][begin.getY()+i].setWall();
                else {
                    entropy = 0;
                    if (curr_doors > 0){
                        System.out.println("Recursive call");
                        int n_width = boxgenerator.nextInt(width)-1;
                        int n_height = boxgenerator.nextInt(height)-1;
                        Node n_begin = new Node(begin.getX()-n_width,begin.getY()+i-(n_height/2));
                        if (n_width>2 && n_height>2) iGenerateBoxes(n_width, n_height, n_begin,cam);
                    }
                    ++curr_doors;
                }
                ++entropy;
            }
            if (checkPassable(begin.getX()+width-1,begin.getY()+i)){
                roll = boxgenerator.nextInt(num_squares)-entropy;
                if (roll > 0 || curr_doors == num_doors) tab[begin.getX()+width-1][begin.getY()+i].setWall();
                else {
                    entropy = 0;
                    if (curr_doors > 0){
                        System.out.println("Recursive call");
                        int n_width = boxgenerator.nextInt(width)-1;
                        int n_height = boxgenerator.nextInt(height)-1;
                        Node n_begin = new Node(begin.getX()+width-1,begin.getY()+i-(height/2));
                        System.out.println("Recursive call");
                        if (n_width>2 && n_height>2) iGenerateBoxes(n_width, n_height, n_begin,cam);
                    }
                    ++curr_doors;
                }
                ++entropy;
            }

        }
        System.out.println("Generated a box at position"); begin.print();
    }

    /**
     *
     * @param n
     * @param id
     */
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
        if (valid(n)){
            if (tab[n.getX()][n.getY()].getContentSize()!=0){
                return tab[n.getX()][n.getY()].getID(0);
            }
            else return tab[n.getX()][n.getY()].getTerrainID();
        }
        else return -1;
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
     * @param x
     * @param y
     * @return
     */
    public boolean checkWall(int x, int y){
        if (valid(x,y)){
            return (!tab[x][y].isPassable());
        }
        else return false;
    }

    /**
     *
     * @param n
     * @return
     */
    public Tile getTile(Node n){
        if (valid(n)){
            return (tab[n.getX()][n.getY()]);
        }
        else return null;
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
        if (valid(n)){
            return getActor(n.getX(),n.getY());
        }
        else return null;
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
     * updates the edges of the terrain in a square
     * @param xx Top left corner
     * @param xy Top right corner
     * @param yx Bottom left corner
     * @param yy Bottom right corner
     */
    public void updateEdges(int xx, int xy, int yx, int yy){
        for (int i = xx; i < yx; ++i){
            for (int j = xy; j < yy; ++j){
                Edge aux = isEdge(i,j);
                if (aux!=null) switch (aux) {
                    case NW:
                        tab[i][j].setID(6);
                        break;
                    case NE:
                        tab[i][j].setID(7);
                        break;
                    case SW:
                        tab[i][j].setID(8);
                        break;
                    case SE:
                        tab[i][j].setID(9);
                        break;
                }
            }
        }
    }

    private Edge isEdge(int x, int y){
        //garbage code
        if (tab[x][y].getTerrainID()==0){
            boolean valid = true;
            for (int i = -1; i < 2; ++i){
                for (int j = -1; j < 2; ++j){
                    if (!valid(x+i,y+j)) valid = false;
                }
            }
        if (valid){
        if ((0==tab[x-1][y].getTerrainID() || (6<=tab[x-1][y].getTerrainID()&&tab[x-1][y].getTerrainID()<=8)) 
        && (0==tab[x][y-1].getTerrainID() ||(6<=tab[x-1][y].getTerrainID()&&tab[x-1][y].getTerrainID()<=8))
        && (5==tab[x+1][y].getTerrainID() || tab[x+1][y].getTerrainID()==7) 
        && (5==tab[x][y+1].getTerrainID() || tab[x+1][y].getTerrainID()==7))
            return Edge.NW;
        else
        if ((0==tab[x-1][y].getTerrainID() || tab[x+1][y].getTerrainID()==6 ||tab[x+1][y].getTerrainID()==7||tab[x+1][y].getTerrainID()==9)
        && (0==tab[x][y+1].getTerrainID() || tab[x+1][y].getTerrainID()==6 ||tab[x+1][y].getTerrainID()==7||tab[x+1][y].getTerrainID()==9)
        && (5==tab[x+1][y].getTerrainID() || tab[x+1][y].getTerrainID()==8)
        && (5==tab[x][y-1].getTerrainID() || tab[x+1][y].getTerrainID()==8))
            return Edge.NE;
        else
        if ((0==tab[x+1][y].getTerrainID() || tab[x+1][y].getTerrainID()==6 ||tab[x+1][y].getTerrainID()==8||tab[x+1][y].getTerrainID()==9)
        && (0==tab[x][y-1].getTerrainID() || tab[x+1][y].getTerrainID()==6 ||tab[x+1][y].getTerrainID()==8||tab[x+1][y].getTerrainID()==9)
        && (5==tab[x-1][y].getTerrainID() || tab[x+1][y].getTerrainID()==7)
        && (5==tab[x][y+1].getTerrainID() || tab[x+1][y].getTerrainID()==7))
            return Edge.SW;
        else
        if ((0==tab[x+1][y].getTerrainID() || tab[x+1][y].getTerrainID()==7 ||tab[x+1][y].getTerrainID()==8||tab[x+1][y].getTerrainID()==9)
        && (0==tab[x][y+1].getTerrainID() || tab[x+1][y].getTerrainID()==7 ||tab[x+1][y].getTerrainID()==8||tab[x+1][y].getTerrainID()==9)
        && (5==tab[x-1][y].getTerrainID() || tab[x+1][y].getTerrainID()==6)
        && (5==tab[x][y-1].getTerrainID() || tab[x+1][y].getTerrainID()==6))
            return Edge.SE;
        }
        }
        return Edge.none;
    }

    /**
     *
     * @param time
     */
    public void dark(int time){
        int light_level = 0;
        if (time >=0 && time<1000){
            light_level=100;
        } else if (time>=1000 && time <= 1200){
            light_level=100-((time/10-100)*5);
        } else if (time>=2200 && time <= 2400){
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
    
    public void marcar(Node n){
        if (valid(n)){
            tab[n.getX()][n.getY()].setID(3);
        }
    }
    
    /**
     * Finds a path to a position in the table
     * If no path is found, runpath will be set to null
     * @param x
     * @param y
     * @param tab
     */
    public void BFS(int x, int y, Table tab){
        /*Node[] path = iBFS(pos, new Node(x,y), tab);
        if (path!= null){
            runpath = path;
            move = true;
            runindex = 1;
        } else {
            runpath = null;
        }*/
    }

    private static Comparator<NodeData> node_comparator = (NodeData n1, NodeData n2) -> {
        if (n1.getTotal() > n2.getTotal()) return 1;
        else if (n1.getTotal() < n2.getTotal()) return -1;
        else return 0;
    };
            
    public Node[] iBFS (Node act_pos, Node target) {
        if (!getTile(target).isPassable()) return null; //early exit
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
            if (current.compare(target)) break;
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
        if (!target.compare(current)) return null;
        else{
            Node[] path = visitats.tracePath(current_par);
            return path;
        }
    }
}