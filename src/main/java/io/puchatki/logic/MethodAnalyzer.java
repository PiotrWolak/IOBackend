package io.puchatki.logic;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.stmt.Statement;
import io.puchatki.model.MethodBodyDescription;
import io.puchatki.model.MethodDescription;
import io.puchatki.utils.DirectoryExplorer;
import io.puchatki.utils.NodeIterator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodAnalyzer {

    public String getClassFullName(Path filePath) {
        String[] split = filePath.toString().split("\\.");
        String pathWithRubbish = System.getProperty("user.dir") + "\\src\\main\\java\\";
        String classLongName = split[0].substring(pathWithRubbish.length());
        return classLongName.replace('\\', '.');
    }

    public List<String> getAllMethodNames() {
        List<String> methodList = new ArrayList<>();
        List<Path> pathsList = ProjectFilesProcessor.getAllFiles();
        assert pathsList != null;

        for (Path path : pathsList) {
            if (!path.endsWith("DirectoryExplorer.java") && !path.endsWith("NodeIterator.java")) {
                try {
                    Class<?> aClass = Class.forName(getClassFullName(path));
                    Method[] methods = aClass.getDeclaredMethods();

                    Arrays.stream(methods)
                            .filter(method -> !method.getName().contains("lambda"))
                            .map(Method::getName)
                            .forEach(methodList::add);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return methodList;
    }


    public List<MethodDescription> generateMethodDependencies(File projectDir) {
        List<String> methodNameList = getAllMethodNames();
        List<MethodBodyDescription> methodBodyDescriptionList = readMethodsBody(projectDir);

        List<MethodDescription> detailsList = new ArrayList<>();

        for (MethodBodyDescription methodBodyDescription : methodBodyDescriptionList) {
            MethodDescription methodDescription = new MethodDescription();
            methodBodyDescription.setMethodName();
            methodBodyDescription.setMethodBody();

            String bodyDetailsMethodName = methodBodyDescription.getMethodName();
            methodNameList.stream().filter(bodyDetailsMethodName::contains).forEach(methodDescription::setMethodName);
            methodDescription.setSrc(methodBodyDescription.getSrc());
            methodDescription.parseSrcToPackageName();
            methodDescription.setFileName();

            String[] body = methodBodyDescription.getMethodBody();

            for (String line : body) {
                methodNameList.forEach(methodName -> {
                    if (line.contains(methodName) && !methodName.equals(methodDescription.getMethodName())) {
                        if (methodDescription.getMethodDependencies().containsKey(methodName)) {
                            methodDescription.getMethodDependencies().put(methodName, methodDescription.getMethodDependencies().get(methodName) + 1);
                        } else {
                            methodDescription.getMethodDependencies().put(methodName, 1);
                        }
                    }
                });
            }
            detailsList.add(methodDescription);
        }
        return detailsList;
    }

    public static List<MethodBodyDescription> readMethodsBody(File projectDir) {
        List<MethodBodyDescription> methodBodyList = new ArrayList<>();
        new DirectoryExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            if(!path.endsWith("DirExplorer.java") && !path.endsWith("NodeIterator.java")){
                try {
                    new NodeIterator(node -> {
                        if (node instanceof Statement) {
                            MethodBodyDescription methodBodyDescription = new MethodBodyDescription();
                            methodBodyDescription.setSrc(path);
                            String methodWithContent = node.getParentNode().get().toString();
                            methodBodyDescription.setContent(methodWithContent);
                            methodBodyDescription.setMethodName();
                            methodBodyList.add(methodBodyDescription);

                            return false;
                        } else {
                            return true;
                        }
                    }).explore(JavaParser.parse(file));
                } catch (IOException e) {
                    new RuntimeException(e);
                }
            }
        }).explore(projectDir);
        return methodBodyList;
    }
}
