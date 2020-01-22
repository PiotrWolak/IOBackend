package io.puchatki.utils;

import java.io.File;

public class DirectoryExplorer {
    public interface FileHandler {
        void handle(int level, String path, File file);
    }

    public interface Filter {
        boolean filter(int level, String path, File file);
    }

    private Filter filter;
    private FileHandler fileHandler;

    public void explore(File root) {
        explore(0, "", root);
    }

    public DirectoryExplorer(Filter filter, FileHandler fileHandler) {
        this.filter = filter;
        this.fileHandler = fileHandler;
    }

    private void explore(int level, String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                explore(level + 1, path + "/" + child.getName(), child);
            }
        } else {
            if (filter.filter(level, path, file)) {
                fileHandler.handle(level, path, file);
            }
        }
    }

}
