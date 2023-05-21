package com.octanepvp.splityosis.octaneworlds.creation.tasks;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class WorldTask {
    public static List<String> worldsBeingTasked = new ArrayList<>();

    public abstract void start();

    protected static void copyFileStructure(File source, File target){
        try {
            List<String> ignore = Arrays.asList("uid.dat", "session.lock");
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        if (!target.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyFileStructure(srcFile, destFile);
                    }
                } else {
                    InputStream in = Files.newInputStream(source.toPath());
                    OutputStream out = Files.newOutputStream(target.toPath());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
