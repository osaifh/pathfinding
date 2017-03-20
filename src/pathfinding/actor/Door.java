/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfinding.actor;

import pathfinding.Table.Table;
import pathfinding.auxiliar.Node;

/**
 *
 * @author Alumne
 */
public class Door extends Interactable {
    Boolean open;
    
    public Door(int x, int y, Table t){
        open = false;
        id = 8;
        pos = new Node(x,y);
        t.getTile(pos).setPassable(open);
    }
    
    
    public void interact(Table t) {
        open = !open;
        t.getTile(pos).setPassable(open);
        if (open) id = 7;
        else id = 8;
    }

    @Override
    public void simulate(Table t) {}

    @Override
    public void print() {
        System.out.println("this is a door");
    }
    
}
