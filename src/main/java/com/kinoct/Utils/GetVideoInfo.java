package com.kinoct.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetVideoInfo {

    private int time;
    private int fps;

    public double getTime() {
        return time;
    }

    public int getFps() {
        return fps;
    }

    public void getVideoTime(String video_path) {
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> command = new ArrayList<>();
        command.add("ffprobe");
        command.add("-v");
        command.add("error");
        command.add("-select_streams");
        command.add("v:0");
        command.add("-show_entries");
        command.add("stream=duration");
        command.add("-of");
        command.add("default=noprint_wrappers=1:nokey=1");
        command.add(video_path);
        builder.command(command);
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
            InputStream inputStream = p.getInputStream();
            InputStreamReader iReader = new InputStreamReader(inputStream, "GBK");
            char[] chars = new char[1024];
            int len;
            while ((len = iReader.read(chars)) != -1) {
                String string = new String(chars, 0, len);
                this.time = (int) Double.parseDouble(string.trim());
                System.out.println("视频时长：" + time);
            }
            iReader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getVideoFps(String videoPath) {
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> command = new ArrayList<>();
        command.add("ffprobe");
        command.add("-i");
        command.add(videoPath);
        builder.command(command);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "GBK");
            StringBuilder stringBuilder = new StringBuilder();
            char[] chars = new char[1024];
            int len;
            while ((len = reader.read(chars)) != -1) {
                String s = new String(chars, 0, len);
                stringBuilder.append(s);
            }
            reader.close();

            /*
             * 读取输出数据流后使用正则匹配需要的数据
             */
            String regex = "kb/s, (.*?) fps";
            String regex2 = "(?<!\\d)\\d{2,}x\\d{2,}";
            Pattern pattern = Pattern.compile(regex);
            Pattern pattern2 = Pattern.compile(regex2);
            Matcher matcher = pattern.matcher(stringBuilder);
            Matcher matcher2 = pattern2.matcher(stringBuilder);
            if (matcher.find()) {
                this.fps = Integer.parseInt(matcher.group(1));
            }
            if (matcher2.find()) {
                System.out.println("原视频分辨率：" + matcher2.group());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
