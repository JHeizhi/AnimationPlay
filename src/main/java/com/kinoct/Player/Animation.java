package com.kinoct.Player;

import com.kinoct.Utils.AudioPlay;
import com.kinoct.Utils.GetVideoInfo;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Animation {
    /*
     * 将视频取帧
     * ProcessBuilder调用ffmpeg
     */
    public static void videoToPic(String filePath, String picPath, int fps) throws Exception {
        System.out.println("开始程序...\n");
        System.out.println("开始生成图片...\n");
        File files = new File(picPath + "1.jpg");
        if (!files.exists()) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> command = new ArrayList<>();
            command.add("ffmpeg");
            command.add("-i");
            command.add(filePath);
            command.add("-r");
            command.add(String.valueOf(fps));
            command.add(picPath + "\\%d.jpg");
            pBuilder.command(command);
            pBuilder.redirectErrorStream(true);
            Process process = pBuilder.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader iReader = new InputStreamReader(inputStream, "GBK");
            char[] chars = new char[1024];
            int len;
            while ((len = iReader.read(chars)) != -1) {
                String string = new String(chars, 0, len);
                System.out.println(string);
            }
            iReader.close();
            inputStream.close();
            System.out.println("视频转换完成...\n");

        } else {
            System.out.println("图片文件已存在....\n");
        }
    }

    /*
     * 从视频中提取音频文件
     */
    public static void extractAudio(String videoPath) throws Exception {
        System.out.println("开始生成音频文件...\n");
        File video = new File(videoPath + ".wav");
        if (!video.exists()) {
            ProcessBuilder pBuilder = new ProcessBuilder();
            List<String> command = new ArrayList<>();
            command.add("ffmpeg");
            command.add("-i");
            command.add(videoPath);
            command.add("-acodec");
            command.add("pcm_s16le");
            command.add("-f");
            command.add("s16le");
            command.add("-ac");
            command.add("1");
            command.add("-ar");
            command.add("16000");
            command.add("-f");
            command.add("wav");
            command.add(videoPath + ".wav");
            pBuilder.command(command);
            pBuilder.redirectErrorStream(true);
            Process process = pBuilder.start();
            InputStream inputStream = process.getInputStream();
            InputStreamReader iReader = new InputStreamReader(inputStream, "GBK");
            char[] chars = new char[1024];
            int len;
            while ((len = iReader.read(chars)) != -1) {
                String string = new String(chars, 0, len);
                System.out.println(string);
            }
            iReader.close();
            inputStream.close();
            System.out.println("音频提取完成...\n");
        } else {
            System.out.println("音频文件已存在！\n");
        }

    }

    /*
     * 将图片取RGB色值，然后转化为灰度，最后将像素点替换为字符串并存入对应文件中
     */
    public static void picTOChar(String picPath, String charPath) throws Exception {
        System.out.println("开始图片转换...请稍后\n");
        String charList = "@MWXQRE%#&*=+~!^-.";
        File[] imageFiles = new File(picPath).listFiles();
        File char1 = new File(charPath + "1.txt");
        if (!char1.exists()) {
            if (imageFiles != null) {
                for (File file : imageFiles) {
                    FileWriter fWriter = new FileWriter(charPath + file.getName().replace(".jpg", ".txt"));
                    BufferedWriter bWriter = new BufferedWriter(fWriter);
                    BufferedImage image = ImageIO.read(file);
                    for (int y = 0; y < image.getHeight(); y += 10) {
                        StringBuilder result = new StringBuilder();
                        for (int x = 0; x < image.getWidth(); x += 5) {
                            int pixel = image.getRGB(x, y);
                            int r = (pixel & 0xff0000) >> 16;
                            int g = (pixel & 0xff00) >> 8;
                            int b = (pixel & 0xff);
                            float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                            int index = Math.round(gray * (charList.length() + 1) / 255);
                            result.append(index >= charList.length() ? " " : String.valueOf(charList.charAt(index)));
                        }
                        bWriter.write(result.append("\r\n").toString());
                    }
                    fWriter.close();
                }
                System.out.println("图片转换完成...\n");
            }
        } else {
            System.out.println("字符文件已存在！！！\n");
        }
    }

    public static void play(String charPath, String audioPath, String filePath) throws Exception {
        System.out.println("正在启动动画窗口...\n");
        JFrame jFrame = new JFrame();
        jFrame.setTitle("字符动画");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(20, 20, 1280, 720);
        jFrame.setResizable(true);
        jFrame.setUndecorated(false);
        JTextArea area = new JTextArea();
        area.setBackground(Color.white);
        area.setBounds(0, 0, 1280, 720);
        area.setFont(new Font("宋体", Font.PLAIN, 6));
        jFrame.add(new JScrollPane(area));
        jFrame.setVisible(true);

        File[] files = new File(charPath).listFiles();
        GetVideoInfo videoInfo = new GetVideoInfo();
        videoInfo.getVideoTime(filePath);
        videoInfo.getVideoFps(filePath);
        long start = System.currentTimeMillis();
        // 调用多线程播放音乐
        AudioPlay play = new AudioPlay();
        play.setPath(audioPath);
        new Thread(play).start();

        if (files != null) {
            List<File> fileList = Arrays.stream(files).sorted((o1, o2) -> {
                int index1 = Integer.parseInt(o1.getName().replace(".txt", ""));
                int index2 = Integer.parseInt(o2.getName().replace(".txt", ""));
                return index1 - index2;
            }).toList();
            for (File charFile : fileList) {
                StringBuilder result = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(charFile));
                String s;
                while ((s = br.readLine()) != null) {
                    result.append(System.lineSeparator()).append(s);
                }
                br.close();
                area.setText(result.toString());
                Thread.sleep((long) (videoInfo.getFps() * 0.1));
            }
        }
        jFrame.dispose();
        long end = System.currentTimeMillis();
        System.out.println("播放完毕...\n" + "实际播放时间 = " + (end - start));
    }

    public static void main(String[] args) throws Exception {
        // 图片输出路径，字符输出路径建议使用空文件夹
        // 视频路径，注意使用双斜杠
        Scanner scanner = new Scanner(System.in);
        GetVideoInfo videoInfo = new GetVideoInfo();

        String filePath;
        int fps;
        System.out.println("请输入mp4文件路径(注意使用mp4文件):");
        System.out.println("例如：" + "D:\\java\\test.mp4");
        System.out.print("路径：");
        filePath = scanner.next();
        scanner.close();
        File video = new File(filePath);

        String audioPtah = filePath + ".wav";

        // 图片保存路径
        String picPath = filePath.replace(".mp4", "_pics\\");

        // 字符txt保存路径，不能与图片路径一样！
        String charPath = filePath.replace(".mp4", "_chars\\");

        if (!video.isDirectory()) {

            if (!video.exists()) {
                System.out.println("视频文件不存在！！！\n");
            } else {
                boolean picB = new File(picPath).mkdirs();
                boolean charB = new File(charPath).mkdirs();
                videoInfo.getVideoFps(filePath);
                fps = videoInfo.getFps();
                videoToPic(filePath, picPath, fps);
                extractAudio(filePath);

                File pic1 = new File(picPath + "1.jpg");
                if (!pic1.exists()) {
                    System.out.println("图片文件不存在！！！\n");
                } else {
                    picTOChar(picPath, charPath);
                }
                File char1 = new File(charPath + "1.txt");
                if (!char1.exists()) {
                    System.out.println("字符文件不存在！！！\n");
                } else {
                    play(charPath, audioPtah, filePath);
                }
            }
        } else {
            System.out.println("这是文件夹吗？记得加.mp4!");
        }
    }

}
