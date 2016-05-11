package player;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.sound.sampled.DataLine.Info;

/**
 *
 * <Replace this with a short description of the class.>
 *
 * @author Giulio
 */
public class Audio {

	private InputStream waveStream;
	private AudioInputStream audioInputStream;
	private Clip audioClip;
	private int currentFrame = 0;
	long currentTime = 0;
	public String name;
	AudioFormat af;
	double frameRate = 0;

	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

	public Audio(String audioFile){
		try {
			waveStream = new FileInputStream(audioFile);
			loadSound();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (PlayWaveException e) {
			e.printStackTrace();
		}
	}

	private void loadSound() throws PlayWaveException
	{
		audioInputStream = null;
		try{
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(waveStream));
		}
		catch (UnsupportedAudioFileException e1) {
			throw new PlayWaveException(e1);
		}
		catch (IOException e1) {
			throw new PlayWaveException(e1);
		}

		try{
			audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
			af = audioClip.getFormat();
			frameRate = af.getFrameRate();
		}
		catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop()
	{
		audioClip.stop();
		currentFrame = 0;
		currentTime = 0;
	}

	public void pause()
	{
		audioClip.stop();
		currentFrame = audioClip.getFramePosition();
		currentTime = audioClip.getMicrosecondPosition();
		System.out.println("------------------Audio curr: " + currentFrame);
	}

	public void play()
	{
		audioClip.setMicrosecondPosition(currentTime);
		audioClip.setFramePosition(currentFrame);
		audioClip.start();
	}

	public long getLength()
	{
		return audioClip.getMicrosecondLength();
	}

	public long getPos()
	{
		return audioClip.getMicrosecondPosition();
	}
}
