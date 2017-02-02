package pathfinding;

import java.util.Random;

public class Table {
        private Tile[][] tab;
        private final int table_size = 100;
        private final int max_attempts = 20;
        
        public Table(){
            tab = new Tile[table_size][table_size];
            int aux;
            float[][] tab_gen = GeneratePerlinNoise(GenerateWhiteNoise(table_size,table_size),4);
            for (int i = 0; i < table_size; ++i){
                for (int j = 0; j < table_size; ++j){
                    if (tab_gen[i][j]>0.5)aux=5;
                    else aux = 0;
                    tab[i][j] = new Tile(aux);
                }
            }
        }
        
        public boolean valid(Node act){
            if (act!=null){
                return (act.get_x() >= 0 & act.get_x()<table_size & act.get_y() >= 0 & act.get_y()<table_size);
            } else return false;
        }
        
        public boolean valid(int x, int y){
            return (x >= 0 & x<table_size & y >= 0 & y<table_size);
        }
        
        public boolean check(Node act){
            if (valid(act)){
                return (tab[act.get_x()][act.get_y()].is_passable());
            }
            else return false;
        }
        
        public boolean check(int x, int y){
            if (x >= 0 & x<table_size & y >= 0 & y<table_size){
                return (tab[x][y].is_passable());
            } else return false;
        }
        
        public boolean check_xy(int x, int y){
            return (x>=0 & x<table_size & y>=0 & y<table_size);
        }
        
        public boolean check_exc(Node act, int exc){
            if (valid(act)){
                return (tab[act.get_x()][act.get_y()].is_passable() || tab[act.get_x()][act.get_y()].get_ID()==exc);
            }
            else return false;
        }
        
        public boolean check_exc(int x, int y, int exc){
            if (x >= 0 & x<table_size & y >= 0 & y<table_size){
                return ((tab[x][y].get_ID() == 0) || tab[x][y].get_ID()==exc);
            }
            else return false;
        }
        
        public boolean check_passable(int x, int y){
            if (x >= 0 & x<table_size & y >= 0 & y<table_size){
                return tab[x][y].passable;
            }
            else return false;
        }
        
        public boolean check_passable(Node act){
            if (act.get_x() >= 0 & act.get_x()<table_size & act.get_y() >= 0 & act.get_y()<table_size){
                return tab[act.get_x()][act.get_y()].passable;
            }
            else return false;
        }
        
        public void add(int x, int y, int id){
            if (x>= 0 & x<table_size & y>=0 & y<table_size){
                tab[x][y].set_ID(id);
            }
        }
        
        public void add(int x, int y, Objecte obj){
            if (x>= 0 & x<table_size & y>=0 & y<table_size){
                tab[x][y].set_objecte(obj);
            }
        }
        
        public void generate_walls(int n){
            Random wallgenerator = new Random();
            for (int i = 0; i <= n; ++i){
                int l = wallgenerator.nextInt(4)+4;            
                int hv = wallgenerator.nextInt(2);
                Node begin = new Node();
                begin.generate(table_size);
                boolean valid = false;
                int attempt = 0;
                while (!valid & attempt<max_attempts){
                    if (tab[begin.get_x()][begin.get_y()].is_passable()){
                        if (hv == 0){
                            boolean w = true;
                            for (int k = begin.get_x(); k < begin.get_x() + l; ++k){
                                if (k >= 0 & k < table_size){
                                    if (!tab[k][begin.get_y()].is_passable()) w = false;
                                } else w = false;
                            }
                            if (w){
                                valid = true;
                                for (int k = begin.get_x(); k < begin.get_x() + l; ++k){
                                        tab[k][begin.get_y()].set_wall();
                                }
                            }
                        } else {
                            boolean w = true;
                            for (int k = begin.get_y(); k < begin.get_y() + l; ++k){
                                if (k >= 0 & k < table_size){
                                    if (!tab[begin.get_x()][k].is_passable()) w = false;
                                } else w = false;
                            }
                            if (w){
                                valid = true;
                                for (int k = begin.get_y(); k < begin.get_y() + l; ++k){
                                    tab[begin.get_x()][k].set_wall();
                                }
                            }
                        }
                    }
                    if (!valid){
                            begin.generate(table_size);
                            ++attempt;
                    }
                }
                if (attempt == max_attempts) System.out.println("Ran out of attempts");
            }
        }
        
        public void generate_boxes(int n, Camera cam){
            Random boxgenerator = new Random();
            for (int i = 0; i < n; ++i){
                int width, height;
                Node begin = new Node();
                do {
                begin.generate(table_size);
                } while (check_wall(begin.get_x(),begin.get_y()));
                width = boxgenerator.nextInt(6)+5;
                height = boxgenerator.nextInt(6)+5;
                immersion_boxes(width,height,begin,cam);
            }
        }
        
        //PROTOTYPE
        private void immersion_boxes(int width, int height, Node begin, Camera cam){
            Random boxgenerator = new Random();
            int num_doors = boxgenerator.nextInt(5)+1;
            int num_squares = height*2 + width*2;
            int curr_doors = 0;
            int roll;
            int entropy = boxgenerator.nextInt(num_squares);
            Node act = new Node();
            //generate horizontal walls
            for (int i = 0; i < width; ++i){
                if (check_xy(begin.get_x()+i,begin.get_y())){
                    roll = boxgenerator.nextInt(num_squares)-entropy;
                    if (roll > 0 || curr_doors == num_doors) tab[begin.get_x()+i][begin.get_y()].set_wall();
                    else {
                        entropy = 0;
                        if (curr_doors > 0){
                            System.out.println("Recursive call");
                            int n_width = boxgenerator.nextInt(width)-1;
                            int n_height = boxgenerator.nextInt(height)-1;
                            Node n_begin = new Node(begin.get_x()+i-(n_width/2),begin.get_y()-(n_height));
                            if (n_width>2 && n_height>2) immersion_boxes(n_width, n_height, n_begin,cam);
                        }
                        ++curr_doors;
                    }
                    ++entropy;
                }
                
                
                if (check_xy(begin.get_x()+i,begin.get_y()+height-1)){
                    roll = boxgenerator.nextInt(num_squares)-entropy;
                    if (roll > 0 || curr_doors == num_doors) tab[begin.get_x()+i][begin.get_y()+height-1].set_wall();
                    else {
                        entropy = 0;
                        if (curr_doors > 0){
                            System.out.println("Recursive call");
                            int n_width = boxgenerator.nextInt(width)-1;
                            int n_height = boxgenerator.nextInt(height)-1;
                            Node n_begin = new Node(begin.get_x()+i-(n_width/2),begin.get_y()+height-1);
                            if (n_width>2 && n_height>2) immersion_boxes(n_width, n_height, n_begin,cam);
                        }
                        ++curr_doors;
                    }
                    ++entropy;
                }
                

                
            }
            //generate vertical walls
            for (int i = 0; i < height; ++i){
                if (check_xy(begin.get_x(),begin.get_y()+i)){
                    roll = boxgenerator.nextInt(num_squares)-entropy;
                    if (roll > 0 || curr_doors == num_doors) tab[begin.get_x()][begin.get_y()+i].set_wall();
                    else {
                        entropy = 0;
                        if (curr_doors > 0){
                            System.out.println("Recursive call");
                            int n_width = boxgenerator.nextInt(width)-1;
                            int n_height = boxgenerator.nextInt(height)-1;
                            Node n_begin = new Node(begin.get_x()-n_width,begin.get_y()+i-(n_height/2));
                            if (n_width>2 && n_height>2) immersion_boxes(n_width, n_height, n_begin,cam);
                        }
                        ++curr_doors;
                    }
                    ++entropy;
                }
                
                if (check_xy(begin.get_x()+width-1,begin.get_y()+i)){
                    roll = boxgenerator.nextInt(num_squares)-entropy;
                    if (roll > 0 || curr_doors == num_doors) tab[begin.get_x()+width-1][begin.get_y()+i].set_wall();
                    else {
                        entropy = 0;
                        if (curr_doors > 0){
                            System.out.println("Recursive call");
                            int n_width = boxgenerator.nextInt(width)-1;
                            int n_height = boxgenerator.nextInt(height)-1;
                            Node n_begin = new Node(begin.get_x()+width-1,begin.get_y()+i-(height/2));
                            System.out.println("Recursive call");
                            if (n_width>2 && n_height>2) immersion_boxes(n_width, n_height, n_begin,cam);
                        }
                        ++curr_doors;
                    }
                    ++entropy;
                }
                
            }
            System.out.println("Generated a box at position"); begin.print();
        }
        
        public void generate_object(int n, int id){
            for (int i = 0; i <= n; ++i){
                Node position = new Node();
                position.generate(table_size);
                int attempt = 0;
                do{
                    position.generate(table_size);
                    ++attempt;
                } while (!tab[position.get_x()][position.get_y()].is_passable() && attempt < max_attempts);
                if (attempt == max_attempts) System.out.println("Ran out of attempts");
                else tab[position.get_x()][position.get_y()].set_objecte(new Objecte(id,position.get_x(),position.get_y()));
            }
        }
        
        public void set(Node pos, int id){
            if (Table.this.valid(pos)){
                tab[pos.get_x()][pos.get_y()].set_ID(id);
                if (id == 1) tab[pos.get_x()][pos.get_y()].passable = false;
            }
        }
        
        public int tab_id(Node act){
            return tab[act.get_x()][act.get_y()].get_ID();
        }
        
        public int tab_id(int x, int y){
            return tab[x][y].get_terrain_ID();
        }
        
        public int get_size(){
            return table_size;
        }
        
        public boolean check_wall(int x, int y){
            if (x >= 0 & x<table_size & y >= 0 & y<table_size){
                return (tab[x][y].get_ID()==1);
            }
            else return false;
        }
        
        public Tile get_tile(Node aux){
            return (tab[aux.get_x()][aux.get_y()]);
        }
        
        public Tile get_tile(int x, int y){
            return (tab[x][y]);
        }
        
        public Objecte get_object(int x, int y){
            if (tab[x][y].get_object()!=null) return tab[x][y].get_object();
            else return null;
        }
        
        public void update_object(Objecte obj){
           tab[obj.get_node().get_x()][obj.get_node().get_x()].set_objecte(obj);
        }
        
        private float[][] GenerateWhiteNoise(int width, int height){
            Random random = new Random();
            float[][] noise = new float[width][height];
            for (int i = 0; i < width; i++){
                for (int j = 0; j < height; j++){
                    noise[i][j] = (float)random.nextDouble() % 1;
                }
            }
            return noise;
        }
        
        private float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount){
            int width = baseNoise.length;
            int height = baseNoise[0].length;
            float[][][] smoothNoise = new float[octaveCount][][];
            float persistance = 0.5f;
            //generate smooth noise
            for (int i = 0; i < octaveCount; i++){
                smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
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
        
        private float[][] GenerateSmoothNoise(float[][] baseNoise, int octave){
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
                  float top = Interpolate(baseNoise[sample_i0][sample_j0],
                     baseNoise[sample_i1][sample_j0], horizontal_blend);

                  //blend the bottom two corners
                  float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
                     baseNoise[sample_i1][sample_j1], horizontal_blend);

                  //final blend
                  smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
               }
            }

            return smoothNoise;
    }
        
        private float Interpolate(float x0, float x1, float alpha){
            return x0 * (1 - alpha) + alpha * x1;
        }
        
        public void dark(){
            for (int i = 0; i < table_size; ++i){
                for (int j = 0; j < table_size; ++j){
                    tab[i][j].set_lit(false);
                }
            }
        }
       
}