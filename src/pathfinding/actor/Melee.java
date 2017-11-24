package pathfinding.actor;

import java.util.ArrayList;
import pathfinding.Table.Table;

public class Melee extends Particle {
    private int tick_counter = 0;
    private final int tick_max = 30;
    private int height, width;
    private int facing_direction;
    private ArrayList<MeleeTile> m_list;
    
    public Melee(int height, int width, Actor source, ActorList objList, Table t){
        m_list = new ArrayList<MeleeTile>();
        this.height = height;
        this.width = width;
        this.facing_direction = ((Creature) source).getFacingDirection();
        this.pos = source.getNode().getNodeCopy();
        pos.iMove(t, facing_direction);
        for (int i = 0; i < height; ++i){
            if (t.valid(pos)){
                MeleeTile m = new MeleeTile(pos,t,source);
                objList.add(m, true);
                m_list.add(m);
                t.add(m);
                pos = pos.getNodeCopy();
                pos.iMove(t, facing_direction);
            }
        }
    }
    
    @Override
    public void simulate(Table t) {
        tick_counter++;
        if (tick_counter >= tick_max){
            tick_counter = 0;
            boolean done = true;
            for (int i = 0; i < m_list.size() && !done;++i){
                MeleeTile x = m_list.get(i);
                done = !x.isAlive();
            }
            if (done) alive = false;
        }
    }

    @Override
    public void print() {
        System.out.println("melee");
    }
    
}
