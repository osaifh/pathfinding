
package pathfinding.Audio;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**
 * This is a wrapper for openAL buffers that includes some additional information
 */
public class LocalBuffer {
    private final int bufferPointer, channels, sampleRate, priority, id;
    private final boolean looping;
    
    /**
     * Default constructor for localBuffer. Buffers the sound file.
     * @param filename the filename of the audio file. Must be .ogg
     * @param priority the priority of the sound. This will indicate how likely is the sound to be replaced by another sound
     * @param looping whether the sound is looping or not
     * @param id the sound's ID
     * if there's not enough sources to play all the sounds. 0 is the highest priority, and higher values have inversely proportional priorities
     */
    public LocalBuffer(String filename, int priority, boolean looping, int id){
        this.priority = priority;
        this.looping = looping;
        this.id = id;
        
        ShortBuffer rawAudioBuffer;
        
        try (MemoryStack stack = stackPush()) {
            //Allocate space to store return information from the function
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);
            
            rawAudioBuffer = stb_vorbis_decode_filename(filename, channelsBuffer, sampleRateBuffer);
            
            //Retrieve the extra information that was stored in the buffers by the function
            channels = channelsBuffer.get(0);
            sampleRate = sampleRateBuffer.get(0);
        }
        
        //Find the correct openAL format
        int format = -1;
        if (channels == 1){
            format = AL_FORMAT_MONO16;
        }
        else if (channels == 2){
            format = AL_FORMAT_STEREO16;
        }
        
        //request space for the buffer
        bufferPointer = alGenBuffers();
        
        //send the data to openAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);
        
        //free the memory allocated by STB
        free(rawAudioBuffer);
    }
    
    public int getBufferPointer(){
        return bufferPointer;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public boolean isLooping(){
        return looping;
    }
    
    public void deleteBufferPointer(){
        alDeleteBuffers(bufferPointer);
    }
    
    @Override
    public boolean equals(Object a){
        return ((LocalBuffer)a).bufferPointer == this.bufferPointer;
    }
}
