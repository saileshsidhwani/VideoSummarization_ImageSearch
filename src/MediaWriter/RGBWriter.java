package MediaWriter;


import mediaLoader.ImageLoader;

import java.io.*;

public class RGBWriter {

    /*
    Constructor
    Sets the file to be read
     */

    private ImageLoader loader;

    public RGBWriter(String fileName) {
        this.loader = new ImageLoader(fileName);
    }

    /*
    Writes the set frames to an output.rgb file
     */
    public void writeFrames(boolean[] frames, File outputFile) throws Exception {

        OutputStream outputStream = new FileOutputStream(outputFile);
        byte[] bytes;
        int lastFrameKept = 0;

        //get the first frame - index 0
        bytes = loader.getNext();
        outputStream.write(bytes);

        //get subsequent frames
        for (int i = 1; i < frames.length; i++) {
            if (frames[i]) {
                bytes = loader.getNext((long) (i - 1 - lastFrameKept) * loader.getBytesPerFrame());
                outputStream.write(bytes);
                lastFrameKept = i;
            }
        }
        outputStream.close();
    }

    public void writeFrames(int startFrame, int endFrame, File outputFile) throws Exception {
        OutputStream outputStream = new FileOutputStream(outputFile);
        byte[] bytes;

        loader.skip((long) (startFrame - 1) * loader.getBytesPerFrame());

        for (int i = startFrame; i <= endFrame; i++) {
            bytes = loader.getNext();
            outputStream.write(bytes);
        }

        outputStream.close();
    }
}