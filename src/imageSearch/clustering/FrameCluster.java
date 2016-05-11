package imageSearch.clustering;


import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class FrameCluster {

    private int CLUSTER_ID;
    private double CLUSTER_METRIC = -1.0;
    private List<Mat> CLUSTER_HIST;
    private ArrayList<Frame> CLUSTER_ITEMS;

    public FrameCluster(int ID) {
        this.CLUSTER_ID = ID;
        this.CLUSTER_ITEMS = new ArrayList<>();
    }

    public int getID() {
        return this.CLUSTER_ID;
    }

    public double getMetric() {
        return this.CLUSTER_METRIC;
    }

    public void setMetric(double metric) {
        this.CLUSTER_METRIC = metric;
    }

    public void setClusterHistogram(List<Mat> hist) {
        this.CLUSTER_HIST = hist;
    }

    public List<Mat> getClusterHistogram() {
        return this.CLUSTER_HIST;
    }

    public int getSize() {
        return this.CLUSTER_ITEMS.size();
    }

    public ArrayList<Frame> getClusterItems() {
        return this.CLUSTER_ITEMS;
    }

    public void put(Frame frame) {
        this.CLUSTER_ITEMS.add(frame);
        //updateMetric(item);
    }
}