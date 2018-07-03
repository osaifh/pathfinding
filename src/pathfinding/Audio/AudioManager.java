package pathfinding.Audio;

/**
 * Singleton wrapper of the audio engine.
 * The usage of the singleton pattern is justified because we only ever need one instance of audioEngine
 */
public class AudioManager {
    private static AudioEngine audioEngine;
    
    protected AudioManager(){
    }
    
    public static AudioEngine getInstance(){
        if (audioEngine == null){
            audioEngine = new AudioEngine();
        }
        return audioEngine;
    }
}
