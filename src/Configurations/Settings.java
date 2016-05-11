package Configurations;

/**
 * Created by sailesh on 5/10/16.
 */
public class Settings {

    //IMAGE SETTINGS
    public static final int WIDTH = 480;
    public static final int HEIGHT = 270;
    public static final int CHANNELS = 3;

    public static final int TOTAL_FRAMES = 4500;

    public static final int PIXELS_PER_FRAME = WIDTH * HEIGHT;
    public static final int BYTES_PER_FRAME = PIXELS_PER_FRAME * CHANNELS;

    public static final int VALUES_PER_CHANNEL = 256;
    public static final boolean HISTOGRAM_ACCUMULATE = false;

    public static final int MAX_HISTOGRAM_DIFFERENCE = 259200;
    public static final double IMAGE_SUMMARY_THRESHOLD_PERCENT = 0.35;
    public static final double IMAGE_CLUSTERING_THRESHOLD_PERCENT = 0.25;

    //VIDEO SETTINGS
    public static final int VIDEO_FPS = 15;
    public static final int TIMER_INTERVAL = 67;

    //AUDIO SETTINGS
    public static final int AUDIO_BYTES_PER_SAMPLE = 2;
    public static final int STEPS_PER_SECOND = 2;
    public static final double VIDEO_FRAMES_PER_STEP = (double)VIDEO_FPS / STEPS_PER_SECOND;
    public static final int AUDIO_SAMPLES_PER_VIDEO_FRAME = 1600;
    public static final int AUDIO_BYTES_PER_VIDEO_FRAME = AUDIO_SAMPLES_PER_VIDEO_FRAME * AUDIO_BYTES_PER_SAMPLE;

    //SUMMARY SETTINGS
    public static final int SUMMARY_SECONDS = 1;
    public static final int SUMMARY_FRAMES_TO_WRITE = SUMMARY_SECONDS * VIDEO_FPS;

    //SEARCH SETTINGS
    public static final int SEARCH_SECONDS = 5;
    public static final int SEARCH_FRAMES_TO_WRITE = SEARCH_SECONDS * VIDEO_FPS;

}