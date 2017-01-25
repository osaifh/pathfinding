package pathfinding;

import javax.swing.JLabel; 
import javax.swing.JPanel;
import javax.swing.JFrame; 
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JLayeredPane;
public class Camera extends JFrame{
    private final int camera_size = 11;
    private Node position = new Node();
    JLayeredPane jpanel = getLayeredPane();
    JLabel[][] terrain_table = new JLabel[camera_size][camera_size];
    JLabel[][] object_table = new JLabel[camera_size][camera_size];
    //JPanel jpanel = (JPanel) this.getContentPane(); 
    Border border = BorderFactory.createLineBorder(Color.black, 2);
    ImageIcon black = new ImageIcon("D:\\pathfinding\\src\\resources\\Black.png");
    ImageIcon red = new ImageIcon("D:\\pathfinding\\src\\resources\\Red.png");
    ImageIcon grass = new ImageIcon("D:\\pathfinding\\src\\resources\\Grass.png");
    ImageIcon brown = new ImageIcon("D:\\pathfinding\\src\\resources\\Brown.png");
    ImageIcon player = new ImageIcon("D:\\pathfinding\\src\\resources\\Player.png");
    ImageIcon wall = new ImageIcon("D:\\pathfinding\\src\\resources\\Wall.png");
    ImageIcon food = new ImageIcon("D:\\pathfinding\\src\\resources\\Food.png");
    
    public Camera(){
        jpanel.setLayout(null);
        jpanel.setBackground(Color.lightGray);
        jpanel.setPreferredSize(new Dimension(640,640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640,640);
        setTitle("Simulation");
        setVisible(true);
        for(int i = 0; i < camera_size; i++) {
            for (int j = 0;j < camera_size;j++) {
                    terrain_table[i][j] = new JLabel();
                    terrain_table[i][j].setBounds(new Rectangle(15, (i)*64,(j)*64, 64));
                    terrain_table[i][j].setBorder(border);  
                    terrain_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    object_table[i][j] = new JLabel();
                    object_table[i][j].setBounds(new Rectangle(15, (i)*64,(j)*64, 64));
                    object_table[i][j].setBorder(border);  
                    object_table[i][j].setHorizontalAlignment(JLabel.RIGHT);
                    jpanel.add(terrain_table[i][j], new Integer(0));
                    jpanel.add(object_table[i][j], new Integer(1));
                }
           }
    }
    
    public int get_size(){
        return camera_size;
    }
    
    public void set_pos(Node x){
        position = x;
    }
    
    public void set_pos_xy(int x, int y){
        position.set(x, y);
    }
    
    public void update(Table t){
        int id, x, y;
        for (int i = 0; i < camera_size; ++i){
            for (int j = 0; j < camera_size; ++j){
                x = i+(position.get_x()-(camera_size/2));
                y = j+(position.get_y()-(camera_size/2));
                if (t.check_xy(x,y)){
                    id = t.tab_id(x,y);
                         if (id==0) terrain_table[i][j].setIcon(grass);
                    else if (id==1) terrain_table[i][j].setIcon(wall);
                    else if (id==3) terrain_table[i][j].setIcon(red);
                    else if (id==5) terrain_table[i][j].setIcon(brown);
                    if (t.get_object(x,y)!=null){
                        if (t.get_object(x,y).get_id()==2) object_table[i][j].setIcon(player);
                        else if (t.get_object(x,y).get_id()==4) object_table[i][j].setIcon(food);
                    } else {
                        object_table[i][j].setIcon(null);
                    }
                } else {
                    terrain_table[i][j].setIcon(black);
                    object_table[i][j].setIcon(null);
                }
            }
        }
    }
    
}
