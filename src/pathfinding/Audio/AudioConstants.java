package pathfinding.Audio;

public class AudioConstants {
    public static final int TOP_PRIORITY = 0;
    public static final int HIGH_PRIORITY = 1;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;
    
    public static final int MAX_DISTANCE = 64;
    
    public static final int SHOOT_SOUND = 0;
    
    private static final String BASEPATH = "audioResources/";
    public static final String[] FILENAMES = {
        BASEPATH+"shoot_sound.ogg"
    };
}
