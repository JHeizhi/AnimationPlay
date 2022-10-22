package com.kinoct.Utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.File;

public class AudioPlay implements Runnable {
    private String path;

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            File file = new File(path);
            AudioInputStream am = AudioSystem.getAudioInputStream(file);
            AudioFormat aFormat = am.getFormat();
            SourceDataLine sDataLine = AudioSystem.getSourceDataLine(aFormat);
            sDataLine.open();
            sDataLine.start();
            byte[] bytes = new byte[1024];
            while (am.read(bytes, 0, bytes.length) != -1) {
                sDataLine.write(bytes, 0, bytes.length);
            }
            sDataLine.drain();
            sDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("音频时长 = " + (end - start));
    }
}

