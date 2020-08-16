package utils;

import java.io.*;
import javax.sound.sampled.*;

//direct copy of Mr. Gaasbeck's music player class, used to play music in the game
public class MusicPlayer
{
    Clip clip;
    String status;
    AudioInputStream audioInputStream;
    String filePath;

    public MusicPlayer(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        this.filePath = filepath;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        status = "open";
    }

    public void play()
    {
        if (!status.equals("play"))
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            status = "play";
        }
    }

    public void stop()
    {
        if (!status.equals("stop"))
        {
            clip.stop();
            status = "stop";
        }
    }

    public void newSong(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        if (status.equals("play"))
            clip.stop();
        clip.close();
        this.filePath = filepath;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        status = "open";
    }
}

