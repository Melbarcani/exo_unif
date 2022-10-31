package com.lyna.sqlite_poc.asm_poc;

import java.io.File;
import java.util.*;

public class ReadJavaFiles {

    public List<String> listJavaFilesPaths(String folderPath) {
        return listFiles(folderPath, new ArrayList<>());
    }

    //Method to read dir/sub-dir and get list of .java files
    public ArrayList<String> listFiles(String path, List<String> filesList) {
        var fileList = new ArrayList<String>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                System.out.println(file.getAbsolutePath());
                filesList.add(file.getName());
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), filesList);
            }
        }
        return fileList;
    }
}
