package pathfinding.Audio;

import java.util.ArrayList;

import org.lwjgl.openal.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

public class AudioEngine {
    private long device;
    private long context;
    
    private ArrayList<Integer> bufferPointers = new ArrayList<Integer>();
    private ArrayList<Integer> sourcePointers = new ArrayList<Integer>();
    
    public AudioEngine(){
        init();
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
    
    /**
     * Buffers a sound file
     * @param filename the name of the sound file
     * @return the pointer of the buffered sound
     */
    private int bufferSound(String filename){
        ShortBuffer rawAudioBuffer;
        
        int channels;
        int sampleRate;
        
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
        int bufferPointer = alGenBuffers();
        
        //send the data to openAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate);
        
        //free the memory allocated by STB
        free(rawAudioBuffer);
        
        return bufferPointer;
        
    }
    
    //VOLUME
    //float newVolume = 0.4f;
    //alSourcef(currentSourceID, AL_GAIN, newVolume);
    
    //LOOPING
    //alSourcei(source, AL_LOOPING, 1); 
    //1 = enabled, 0 = disabled
    
    //CHECK IF PLAYING
    //alGetSourcei(source, AL_SOURCE_STATE, &state);
    
    //return (state == AL_PLAYING);
    
    /**
     * Terminates all the openAL instances and releases the memory used by the openAL buffers
     */
    public void close(){
        bufferPointers.forEach((bufferPointer) -> {
            alDeleteBuffers(bufferPointer);
        });
        sourcePointers.forEach((sourcePointer) -> {
            alDeleteSources(sourcePointer);
        });
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
