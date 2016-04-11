import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

/*
 * todo:
 * provide a list of songs?
 */
public class MusicPlayer {
	public Sequencer sequencer;

	public enum Tunes {
		DEFAULT
	}

	public String[] tuneFiles = { "resources" + File.separator + "themeA.mid" };

	public MusicPlayer() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void setMusic(int i) {
		setMusic(tuneFiles[i]);
	}

	private void setMusic(String fileName) {
		InputStream inputStream;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(new File(
					fileName)));
			sequencer.setSequence(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		sequencer.setLoopEndPoint(sequencer.getSequence().getTickLength());
		sequencer.setLoopCount(999999);
		sequencer.start();
	}

	public void stopPlaying() {
		sequencer.stop();
	}

}