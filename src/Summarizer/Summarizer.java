package Summarizer;

import MediaWriter.*;
import audioProcessing.*;
import imageProcessing.*;

import java.io.File;
import java.util.ArrayList;


public class Summarizer {

    private String IMAGE_FILE_NAME;
    private String AUDIO_FILE_NAME;

    private String OUTPUT_FILE_NAME;

    public Summarizer(String imageFile, String audioFile){
        System.out.println("summarize");
        this.IMAGE_FILE_NAME = imageFile;
        this.AUDIO_FILE_NAME = audioFile;
    }

    public void summarize(String outputFile) throws Exception{
        this.OUTPUT_FILE_NAME = outputFile;
        boolean[] framesToKeep = new boolean[4500];

        //call image summarizer
        ImageProcessing rgbSummarize = new ImageProcessing(IMAGE_FILE_NAME,4500,480,270);
        ArrayList<Integer> imageFrames = rgbSummarize.generateKeyFrames();
        //System.out.println("Image Frames Length: " + imageFrames.size());
        for(int frame: imageFrames){
            framesToKeep[frame] = true;
        }
        //call audio summarizer
        AudioProcessing wavSummarize = new AudioProcessing(AUDIO_FILE_NAME);
        ArrayList<Integer> audioFrames = wavSummarize.processAudio();
        //System.out.println("Audio Frames Length: " + audioFrames.size());
        for(int i = 0; i < audioFrames.size(); i++){
            int timeStamp = audioFrames.get(i);
            framesToKeep[(int)Math.floor(timeStamp * 7.5)] = true;
        }

        int index = 16;
        for(int i = 1; i < index; i++){
            framesToKeep[i] = true;
        }
        while(index < framesToKeep.length){
            if (framesToKeep[index]){
                for(int j = index - 14; j >= 0 && j<framesToKeep.length && j<=index + 15; j++){
                    framesToKeep[j] = true;
                }
                index += 16;
            }else{
                index += 1;
            }
        }

        int totalFrames = 0;
        for (boolean set : framesToKeep) {
            if (set) {
                totalFrames += 1;
            }
        }
        RGBWriter imageWriter = new RGBWriter(IMAGE_FILE_NAME);
        imageWriter.writeFrames(framesToKeep, new File("/home/sailesh/Documents/summarized.rgb"));

        WAVWriter audioWriter = new WAVWriter(AUDIO_FILE_NAME);
        audioWriter.writeFrames(framesToKeep, totalFrames, new File("/home/sailesh/Documents/summarized.wav"));
    }
}
