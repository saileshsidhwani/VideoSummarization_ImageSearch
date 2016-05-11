package imageSearch;

/**
 * Created by sailesh on 5/10/16.
 */

import Configurations.Settings;
import imageSearch.clustering.*;
import imageProcessing.*;
import mediaLoader.ImageLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omi on 5/1/16.
 */
public class RGBClustering {

    private static double CLUSTER_THRESHOLD = Settings.IMAGE_CLUSTERING_THRESHOLD_PERCENT * Settings.MAX_HISTOGRAM_DIFFERENCE;
    private Histogram hist;

    private ImageLoader loader;
    private ArrayList<FrameCluster> CLUSTERS;
    private Frame queryImage;


    //constructor
    public RGBClustering(String fileName) {
        this.loader = new ImageLoader(fileName);
        this.CLUSTERS = new ArrayList<>();
        this.hist = new Histogram();
    }


    /*
    Returns clusters generated from the input RGB file
     */
    public ArrayList<FrameCluster> getClusters() {

        List<Mat> referenceHist = null;
        List<Mat> currentHist;

        int CLUSTER_ID = 0;
        FrameCluster cluster = new FrameCluster(CLUSTER_ID);

        for (int i = 0; i < loader.getTotalFrames(); i++) {
            byte[] bytes = loader.getNext();

            if (referenceHist == null) {
                //first frame
                referenceHist = hist.getHistogram(bytes, loader.getWidth(), loader.getHeight());

                Frame item = new Frame(i);
                item.setHistogram(referenceHist);
                cluster.put(item);
                cluster.setClusterHistogram(referenceHist);
                continue;
            } else {
                currentHist = hist.getHistogram(bytes, loader.getWidth(), loader.getHeight());
            }

            // add frame as reference frame is the threshold is crossed
            if (hist.getDifference(currentHist, referenceHist,Settings.CHANNELS) >= CLUSTER_THRESHOLD) {
                //complete the current open cluster
                this.CLUSTERS.add(cluster);

                //generate new cluster ID
                CLUSTER_ID += 1;
                referenceHist = currentHist;

                //generate new cluster
                cluster = new FrameCluster(CLUSTER_ID);
                Frame item = new Frame(i);
                item.setHistogram(referenceHist);
                cluster.put(item);
                cluster.setClusterHistogram(referenceHist);
            } else {
                //add item to current cluster
                Frame item = new Frame(i);
                item.setHistogram(currentHist);
                cluster.put(item);
            }

        }
        return this.CLUSTERS;
    }

    public ArrayList<FrameCluster> getClosest(String fileName, int imageWidth, int imageHeight, ArrayList<FrameCluster> clusters) throws Exception {
        //arraylist of closest clusters to the image
        ArrayList<FrameCluster> closest = new ArrayList<>();

        ImageLoader qLoader = new ImageLoader(fileName);

        //get the frame from input
        byte[] image = qLoader.getNext(imageWidth, imageHeight, Settings.CHANNELS);
        //get histogram for the frame
        List<Mat> imageHist = hist.getHistogram(image, imageWidth, imageHeight);
        //SCALING
        imageHist = normalize(imageHist, (double) imageWidth / Settings.WIDTH, (double) imageHeight / Settings.HEIGHT);

        //set item
        this.queryImage = new Frame(-1);
        queryImage.setHistogram(imageHist);

        //calculate differences in histogram for the image for each cluster
        ArrayList<Double> differences = new ArrayList<>();
        for (int i = 0; i < clusters.size(); i++) {
            differences.add(hist.getDifference(imageHist, clusters.get(i).getClusterHistogram(),Settings.CHANNELS));
        }

        //add the clusters qualifying for the image
        //all clusters with differences less the CLUSTER_THRESHOLD
        for (int i = 0; i < differences.size(); i++) {
            if (differences.get(i) <= CLUSTER_THRESHOLD) {
                closest.add(clusters.get(i));
            }
        }

        //return closest clusters
        return closest;
    }


    //Code to normalize the histogram.
    private List<Mat> normalize(List<Mat> histogram, double widthScaling, double heightScaling) {
        List<Mat> normalized = new ArrayList<>();
        Scalar multiplier = new Scalar(1.0 / (widthScaling * heightScaling));
        for (int i = 0; i < Settings.CHANNELS; i++) {
            Mat channelHist = histogram.get(i);
            Core.multiply(channelHist, multiplier, channelHist);
            normalized.add(channelHist);
        }

        return normalized;
    }

    //return best matching item among matched clusters
    public Frame getMatchWithinClusters(ArrayList<FrameCluster> clusters) {
        Frame bestMatch = null;
        double difference = Double.MAX_VALUE;
        //finding minimum difference
        for (int i = 0; i < clusters.size(); i++) {
            ArrayList<Frame> items = clusters.get(i).getClusterItems();
            for (int j = 0; j < items.size(); j++) {
                double diff = hist.getDifference(items.get(j).getHistogram(), this.queryImage.getHistogram(),Settings.CHANNELS);
                if (diff < difference) {
                    difference = diff;
                    bestMatch = items.get(j);
                }
            }
        }
        return bestMatch;
    }

}