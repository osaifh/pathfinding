
package pathfinding.Audio;

import static org.lwjgl.openal.AL10.*;

/**
 * This class is a wrapper for openAL sources that includes some additional information
 */
public class LocalSource {
    private final int sourcePointer;
    private LocalBuffer buffer;
    private int[] state;
    
    public LocalSource(){
        //Let openAL generate the source pointer
        this.sourcePointer = alGenSources();
        state = new int[1];
    }
    
    public int getSourcePointer(){
        return sourcePointer;
    }
    
    /**
     * Replaces the bufferPointer of the source. If the source is playing, it will only replace
     * @param buffer
     * @return returns true if the buffer was replaced, false if it wasn't
     */
    public boolean setBuffer(LocalBuffer buffer){
        if (this.buffer!=null && isPlaying()){
            if (buffer.getPriority() <= this.buffer.getPriority()){
                alSourceStop(sourcePointer);
                alSourcei(sourcePointer, AL_BUFFER, buffer.getBufferPointer());
                this.buffer = buffer;
                return true;
            }
            else {
                return false;
            }
        }
        else {
            this.buffer = buffer;
            alSourcei(sourcePointer, AL_BUFFER, buffer.getBufferPointer());
            int LOOP;
            if (buffer.isLooping()){
                LOOP = 1;
            }
            else {
                LOOP = 0;
            }
            alSourcei(sourcePointer, AL_LOOPING, LOOP);
            return true;
        }
    }
    
    public LocalBuffer getBuffer(){
        try {
            return buffer;
        }
        catch (NullPointerException ex){
            throw ex;
        }
    }
    
    public boolean isPlaying(){
        alGetSourcei(sourcePointer, AL_SOURCE_STATE, state);
        return (state[0] == AL_PLAYING);
    }
    
    public void playSource(){
        alSourcePlay(sourcePointer);
    }
    
    public void playSource(int distance){
        if (distance == 0){
            float newVolume = 1f;
            alSourcef(sourcePointer, AL_GAIN, newVolume);
            alSourcePlay(sourcePointer);
        }
        else if (distance <= AudioConstants.MAX_DISTANCE){
            float newVolume = 1f-((float)distance/(float)AudioConstants.MAX_DISTANCE);
            alSourcef(sourcePointer, AL_GAIN, newVolume);
            alSourcePlay(sourcePointer);
        }
        else {
            //sound didn't play cause the distance was too high
        }
    }
    
    public void deleteSourcePointer(){
        alDeleteSources(sourcePointer);
    }
}
