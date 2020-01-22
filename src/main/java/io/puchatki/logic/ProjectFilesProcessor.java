package io.puchatki.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProjectFilesProcessor {
    public static List<Path> getAllDotFiles(){
        try (Stream<Path> pathStream = Files.walk(Paths.get(System.getProperty("user.dir")))) {
            List<Path> collect = pathStream
                    .filter(path -> path.toString().contains(".dot"))
                    .collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Path> getAllFiles() {
        try (Stream<Path> pathStream = Files.walk(Paths.get(System.getProperty("user.dir")))) {
            List<Path> collect = pathStream
                    .filter(path -> path.toString().contains(".java"))
                    .collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Path> getAllClassFiles() {
        try (Stream<Path> pathStream = Files.walk(Paths.get(System.getProperty("user.dir")))) {
            List<Path> collect = pathStream
                    .filter(path -> path.toString().contains(".class"))
                    .collect(Collectors.toList());
            return collect;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
