package pathfinding.actor.Particles;

import pathfinding.Table.Table;
import pathfinding.actor.ActorList;
import pathfinding.auxiliar.Constants;
import pathfinding.auxiliar.Node;

public class ForcePushSource {
    private Node origin, target;
    private final int SPEED = 8;
    
    public ForcePushSource(Node origin, Node target, Table table, ActorList actorList){
        this.target = target;
        this.origin = origin;
        
        if (!target.equals(origin)){
            TraceShot(table,actorList);
        }
    }
    
    private void TraceShot(Table table, ActorList actorList){
        int dir = origin.relativeDirection(target);
        origin.iMove(table, dir);
        
        //calculate the angle
        double a = target.getY() - origin.getY();
        double b = target.getX() - origin.getX();
        
        if (!(a == 0 && b == 0)){
           
            double alpha = Math.atan(
                    -b / a 
            );
            double angle = -alpha;
            
            double d_x = origin.getX();
            double d_y = origin.getY();
            double target_x = target.getX();
            double target_y = target.getY();

            if (a < 0) angle = Math.toRadians(180) + angle;
            
            double leftAngle = angle - Math.toRadians(90);
            double rightAngle = angle + Math.toRadians(90);
              
            double origin_left_x = d_x + Math.sin(leftAngle);
            double origin_left_y = d_y + Math.cos(leftAngle);
            double origin_right_x = d_x + Math.sin(rightAngle);
            double origin_right_y = d_y + Math.cos(rightAngle);
            
            Node leftOrigin = new Node((int)Math.round(origin_left_x), (int)Math.round(origin_left_y));
            Node rightOrigin = new Node((int)Math.round(origin_right_x), (int)Math.round(origin_right_y));
            
            double target_left_x = target_x + Math.sin(leftAngle);
            double target_left_y = target_y + Math.cos(leftAngle);
            double target_right_x = target_x + Math.sin(rightAngle);
            double target_right_y = target_y + Math.cos(rightAngle);
            
            Node leftTarget = new Node((int)Math.round(target_left_x), (int)Math.round(target_left_y));
            Node rightTarget = new Node((int)Math.round(target_right_x), (int)Math.round(target_right_y));
            
            ForcePush[] pushComponents = new ForcePush[3];
        
            pushComponents[0] = new ForcePush(origin, target, SPEED);
            pushComponents[1] = new ForcePush(leftOrigin, leftTarget, SPEED);
            pushComponents[2] = new ForcePush(rightOrigin, rightTarget, SPEED);

            for(ForcePush push : pushComponents){
                if (table.valid(push.getNode()) && table.checkPassable(push.getNode())){
                    table.add(push);
                    actorList.add(push, true);
                } 
            }
        }
        
        /*
        //calculates the perpendicular directions
        int left = dir - 2;
        if (left < 0) left += 8;
        int right = dir + 2;
        if (right > Constants.W) right -= 8;
        
        Node leftOrigin = new Node(origin);
        Node rightOrigin = new Node(origin);
        leftOrigin.iMove(table, left);
        rightOrigin.iMove(table, right);
        
        Node leftTarget = new Node(target);
        Node rightTarget = new Node(target);
        leftTarget.iMove(table, left);
        rightTarget.iMove(table, right);
        
        ForcePush[] pushComponents = new ForcePush[3];
        
        pushComponents[0] = new ForcePush(origin, target, SPEED);
        pushComponents[1] = new ForcePush(leftOrigin, leftTarget, SPEED);
        pushComponents[2] = new ForcePush(rightOrigin, rightTarget, SPEED);
        
        for(ForcePush push : pushComponents){
            if (table.valid(push.getNode()) && table.checkPassable(push.getNode())){
                table.add(push);
                actorList.add(push, true);
            } 
        }
        */
    }
    
}
