package io.puchatki;

import io.puchatki.logic.FilesAnalyzer;
import io.puchatki.logic.MethodAnalyzer;
import io.puchatki.model.FileDescription;
import io.puchatki.model.MethodDescription;

import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    private static void GenerateGraphs(DependenciesGraph dependenciesGraph) {
        System.out.println("Beginning graphs generation");
        dependenciesGraph.generateFilesDependenciesGraph();
        dependenciesGraph.generateMethodsDependencyGraph();
        dependenciesGraph.generatePackageDependenciesGraph();
        dependenciesGraph.generateAllGraphs();
        dependenciesGraph.generateMethodsInFilesGraph();
    }

    public static void main(String[] args) throws IOException {

       MethodAnalyzer methodAnalyzer = new MethodAnalyzer();
        Map<String, String> methodInPackage = new HashMap<>();
        File projectDirectory = new File(System.getProperty("user.dir"));
        List<MethodDescription> methodDescriptionList = methodAnalyzer.generateMethodDependencies(projectDirectory);
        List<FileDescription> fileDependencies = new FilesAnalyzer().getFileDetails(methodDescriptionList);

        for (int i = 0; i < methodDescriptionList.size(); i++) {
            MethodDescription methodDescription = methodDescriptionList.get(i);
            methodInPackage.put(methodDescription.getMethodName(), methodDescription.getPackageName());
        }

        StringBuilder graphBuilder = new StringBuilder();


        graphBuilder.insert(0, "digraph G { ");
        graphBuilder.append("}");

        File file = new File(System.getProperty("user.dir") + "/plik.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(graphBuilder.toString());
        }


        File dir = new File(System.getProperty("user.dir") + "/dots");
        File[] listFiles = dir.listFiles();
        for (File f : listFiles) {
            f.delete();
        }

        DependenciesGraph dependenciesGraph = new DependenciesGraph(fileDependencies, methodDescriptionList, methodInPackage);

        GenerateGraphs(dependenciesGraph);
    }
}
