package io.puchatki.model;

import java.util.Map;

public class FileDescription {
    public String fileName;
    public long fileSize;

    public Map<String, Integer> fileDependencies;
    public Map<String, Integer> methodsDependencies;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Map<String, Integer> getFileDependencies() {
        return fileDependencies;
    }

    public void setFileDependencies(Map<String, Integer> fileDependencies) {
        this.fileDependencies = fileDependencies;
    }

    public Map<String, Integer> getMethodsDependencies() {
        return methodsDependencies;
    }

    public void setMethodsDependencies(Map<String, Integer> methodsDependencies) {
        this.methodsDependencies = methodsDependencies;
    }

    @Override
    public String toString() {
        return "FileDetails{" +
                "name='" + fileName + '\'' +
                ", size=" + fileSize +
                ", fileDependencies=" + fileDependencies +
                '}';
    }
}
