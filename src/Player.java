import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import player.*;
import Summarizer.*;
import imageSearch.*;

public class Player {

    String audFile;
    String vidFile;
    String searchImg;
    int SEARCH_IMAGE_WIDTH;
    int SEARCH_IMAGE_HEIGHT;
    JFrame frame;
    InputStream is;
    static int width = 480;
    static int height = 270;
    Video videoPlayer;
    Audio audioPlayer;
    JButton playButton, pauseButton, stopButton, summarizeButton, searchButton;


    JLabel lbIm1;
    public Player(String searchImg, String searchImgWidth, String searchImgHeight){
        this.searchImg = searchImg;
        this.SEARCH_IMAGE_HEIGHT = Integer.parseInt(searchImgHeight);
        this.SEARCH_IMAGE_WIDTH = Integer.parseInt(searchImgWidth);
    }

    public void loadAudio( String audioFile){
        audFile = audioFile;
        audioPlayer = new Audio(audFile);
        audioPlayer.name = "sailesh";

    }

    public void loadVideo(String videoFile){
        vidFile = videoFile;
        videoPlayer = new Video(vidFile,audioPlayer);
    }

    public void initialize(){

        frame = new JFrame();

        //frame.getContentPane().setLayout(gLayout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttons = new JPanel();
        buttons.setPreferredSize(new Dimension(width, 90));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(width, 45));
        buttons.add(buttonPanel);

        JPanel advancedPanel = new JPanel();
        advancedPanel.setPreferredSize(new Dimension(width, 45));
        buttons.add(advancedPanel);

        frame.getContentPane().add(buttons, BorderLayout.SOUTH);

        playButton = new JButton("PLAY");
        playButton.addActionListener(new CustomActionListener());
        buttonPanel.add(playButton, BorderLayout.NORTH);


        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(new CustomActionListener());
        buttonPanel.add(pauseButton, BorderLayout.NORTH);

        stopButton = new JButton("STOP");
        stopButton.addActionListener(new CustomActionListener());
        buttonPanel.add(stopButton, BorderLayout.NORTH);

        summarizeButton = new JButton("SUMMARIZE");
        summarizeButton.addActionListener(new CustomActionListener());
        advancedPanel.add(summarizeButton, BorderLayout.SOUTH);

        searchButton = new JButton("SEARCH");
        searchButton.addActionListener(new CustomActionListener());
        advancedPanel.add(searchButton, BorderLayout.SOUTH);


        JLabel lbText1 = new JLabel("Video: " + vidFile);
        lbText1.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel lbText2 = new JLabel("Audio: " + audFile);
        lbText2.setHorizontalAlignment(SwingConstants.LEFT);
        lbIm1 = new JLabel(new ImageIcon( videoPlayer.img));

        frame.getContentPane().add(lbIm1);

        frame.pack();
        frame.setVisible(true);
    }

    public void play(){
        lbIm1.setIcon(new ImageIcon(videoPlayer.img));
        audioPlayer.play();
        videoPlayer.play(frame);
    }

    public void pause(){
        audioPlayer.pause();
        videoPlayer.pause();
    }

    public void stop(){
        audioPlayer.stop();
        videoPlayer.stop();
        lbIm1.setIcon(null);
    }

    public void summarize(){
        stop();
        playButton.setEnabled(false);
        pauseButton.setEnabled(false);

        Summarizer summ = new Summarizer(vidFile,audFile);
        try {
            summ.summarize("asd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Video Summarized");
        loadAudio("/home/sailesh/Documents/summarized.wav");
        loadVideo("/home/sailesh/Documents/summarized.rgb");
        play();
        pauseButton.setEnabled(true);

    }

    public void search() {
        stop();
        Searcher searcher = new Searcher(vidFile);
        try {
            int frameNumber = searcher.search(this.searchImg, this.SEARCH_IMAGE_WIDTH, this.SEARCH_IMAGE_HEIGHT);
            if(frameNumber >= 0){
                System.out.println("Frame found: " + frameNumber);
                searcher.dumpSnippet(frameNumber, vidFile, audFile, "/home/sailesh/Documents/snippet");

                loadAudio("/home/sailesh/Documents/snippet.wav");
                loadVideo("/home/sailesh/Documents/snippet.rgb");
                play();
                pauseButton.setEnabled(true);
            }else {
                System.out.println("Image not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    class CustomActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String buttonText = ((JButton) e.getSource()).getText();
            if(buttonText.equals("PLAY")) {
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
                play();
            }
            else if(buttonText.equals("PAUSE")) {
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
                pause();
            }
            else if(buttonText.equals("STOP")) {
                playButton.setEnabled(true);
                pauseButton.setEnabled(true);
                stop();
            }
            else if(buttonText.equals("SUMMARIZE")){
                summarize();
            }
            else if(buttonText.equals("SEARCH")) {
                search();
            }
        }
    }

}
