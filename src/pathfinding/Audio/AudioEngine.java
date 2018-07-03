package pathfinding.Audio;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.openal.*;
import static org.lwjgl.openal.ALC10.*;

public class AudioEngine {
    private long device;
    private long context;
    private final int SOURCE_NUMBER = 32;
    
    private HashMap<Integer, LocalBuffer> buffers = new HashMap<>();
    private final LocalSource[] sources = new LocalSource[SOURCE_NUMBER];
    
    protected AudioEngine(){
        init();
        initSources();
        loadSounds();
    }
    
    /**
     * Initializes openAL
     */
    private void init(){
        //Acquires the default device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDeviceName);
        
        //Creates the context
        int[] attributes = {0};
        context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);
        
        //Creates a handle for the device capabilities
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
    }
    
    private void initSources(){
        for (int i = 0; i < SOURCE_NUMBER; i++){
            sources[i] = new LocalSource();
        }
    }
    
    private void loadSounds(){
        int id = 0;
        for(String filename : AudioConstants.FILENAMES){
            LocalBuffer buffer = new LocalBuffer(filename, AudioConstants.MEDIUM_PRIORITY, false, id);
            buffers.put(id, buffer);
            id++;
        }
    }
    
    public void playSound(int soundID){
        playSound(soundID, 0);
    }
    
    public void playSound(int soundID, int distance){
        LocalBuffer buffer = buffers.get(soundID);
        //first loop: look for sources that have the same soundID or are empty
        boolean found = false;
        for(int i = 0; i < SOURCE_NUMBER && !found; i++){
            LocalSource source = sources[i];
            try {
                if (source.getBuffer().equals(buffer) && !source.isPlaying()){
                    source.playSource(distance);
                    found = true;
                }
            }
            //found an available source
            catch (NullPointerException ex){
                if (source.setBuffer(buffer)){
                    source.playSource(distance);
                    found = true;
                }
            }
        }
        //second loop: replace a source's buffer with lower or equal priority
        for(int i = 0; i < SOURCE_NUMBER && !found; i++){
            LocalSource source = sources[i];
            try {
                if (source.setBuffer(buffer)){
                    source.playSource(distance);
                    found = true;
                }
            }
            //found an available source
            catch (NullPointerException ex){
                if (source.setBuffer(buffer)){
                    source.playSource(distance);
                    found = true;
                }
            }
        }
        
    }
    
    /**
     * Terminates all the openAL instances and releases the memory used by the openAL buffers
     */
    public void close(){
        for(Map.Entry<Integer, LocalBuffer> entry : buffers.entrySet()){
            LocalBuffer buffer = entry.getValue();
            buffer.deleteBufferPointer();
        }
        
        for(LocalSource source : sources){
            source.deleteSourcePointer();
        }
        
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
