package io.puchatki.logic;

import io.puchatki.model.FileDescription;
import io.puchatki.model.MethodDescription;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;


public class FilesAnalyzer {
    public Map<String, Integer> getFileDependencies(List<String> fileDependencyList) {
        Map<String, Integer> fileDependencyMap = new HashMap<>();
        for (String fileName : fileDependencyList) {
            if (!fileDependencyMap.containsKey(fileName)) {
                fileDependencyMap.put(fileName, 1);
            } else {
                fileDependencyMap.put(fileName, fileDependencyMap.get(fileName) + 1);
            }
        }
        return fileDependencyMap;
    }

    public List<FileDescription> getFileDetails(List<MethodDescription> methodsList) {
        List<Path> files = ProjectFilesProcessor.getAllFiles();
        Map<String, List<String>> mapOfFiles = new HashMap<>();
        List<FileDescription> fileDescriptionList = new ArrayList<>();

        for (Path filePath : files) {
            mapOfFiles.put(getClassName(filePath), new ArrayList<>());

            File file = new File(filePath.toString());
            try (Scanner in = new Scanner(file)) {
                if (in.hasNextLine()) {
                    do {
                        String line = in.nextLine();
                        files.stream()
                                .filter(f -> line.contains(getClassName(f)))
                                .findFirst()
                                .filter(path -> !path.equals(filePath))
                                .ifPresent(path -> mapOfFiles.get(getClassName(filePath))
                                        .add(getClassName(path)));
                    } while (in.hasNextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileDescription fileDescription = new FileDescription();
            fileDescription.setFileSize(file.length());
            fileDescription.setFileName(file.getName());
            fileDescription.setFileDependencies(getFileDependencies(mapOfFiles.get(getClassName(filePath))));
            fileDescription.setMethodsDependencies(getFileMethods(file.getName(), methodsList));

            fileDescriptionList.add(fileDescription);

        }
        return fileDescriptionList;
    }

    public String getClassName(Path filePath) {
        String[] split = filePath.getFileName().toString().split("\\.");
        return split[0];
    }

    public Map<String, Integer> getFileMethods(String fileName, List<MethodDescription> methodsList) {
        Map<String, Integer> methodsMap = new HashMap<>();
        for (MethodDescription methodDescription : methodsList) {
            if (methodDescription.getFileName().equals(fileName)) {
                methodsMap.put(methodDescription.getMethodName(), 0);
            }
        }
        return methodsMap;
    }

}
