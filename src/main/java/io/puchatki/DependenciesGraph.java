package io.puchatki;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import io.puchatki.model.FileDescription;
import io.puchatki.model.MethodDescription;
import io.puchatki.model.PackageDescription;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.engine.Graphviz.fromGraph;
import static guru.nidi.graphviz.model.Factory.*;
import static guru.nidi.graphviz.model.Factory.node;


public class DependenciesGraph {

    private List mainNodes = new ArrayList();
    private List<FileDescription> fileDependencies;
    private Map<String, String> methodInPackage;
    private List<MethodDescription> methodDescriptionList;
    private List<PackageDescription> packageDescriptionList;


    public DependenciesGraph(List<FileDescription> fileDependencies, List<MethodDescription> methodDescriptionList, List<PackageDescription> packageDescriptionList, Map<String, String> methodInPackage) {
        this.methodInPackage = methodInPackage;
        this.fileDependencies = fileDependencies;
        this.methodDescriptionList = methodDescriptionList;
        this.packageDescriptionList = packageDescriptionList;
    }

    private void drawGraph(String name) {
        Graph g = graph(name).directed().with(this.mainNodes);

        try {
            fromGraph(g).render(Format.PNG).toFile(new File("graph/" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mainNodes = new ArrayList();
    }

    private void files() {
        for (FileDescription file : this.fileDependencies) {
            String nodeName = file.getFileName();
            long size = file.getFileSize();

            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + size + "B"));
            List toNodes = new ArrayList();

            for (Map.Entry<String, Integer> singleFile : file.getFileDependencies().entrySet()) {
                String linkedNode = singleFile.getKey() + ".java";
                toNodes.add(to(node(linkedNode)).with(Label.of(singleFile.getValue().toString())));
            }
            this.mainNodes.add(mainNode.link(toNodes));

        }
    }

    private void FileMethods(){
        for (FileDescription file : this.fileDependencies) {
            String nodeName = file.getFileName();
            if (nodeName == null) {
                continue;
            }
            long size = file.getFileSize();

            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + size + "B"));
            List toNodes = new ArrayList();

            for (Map.Entry<String, Integer> singleFile : file.getMethodsDependencies().entrySet()) {
                String linkedNode = singleFile.getKey();
                if(linkedNode == null)
                    continue;
                toNodes.add(to(node(linkedNode)));
            }
            this.mainNodes.add(mainNode.link(toNodes));

        }
    }

    private void packages() {
        for (PackageDescription packageDescription : this.packageDescriptionList) {

            String nodeName = packageDescription.getPackageName();
            if (nodeName == null) {
                continue;
            }
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>"));

            List toNodes = new ArrayList();

            for (Map.Entry<String, Integer> singlePackage : packageDescription.getPackageDependencies().entrySet()) {
                String linkedNode = singlePackage.getKey();
                if (linkedNode.equals("main")) {
                    continue;
                }

                toNodes.add(to(node(linkedNode)).with(Label.of(singlePackage.getValue().toString())));
            }
            this.mainNodes.add(mainNode.link(toNodes));
        }
    }

    private void methods() {
        for (MethodDescription methodDescription : this.methodDescriptionList) {

            String nodeName = methodDescription.getMethodName();
            if (nodeName == null) {
                continue;
            }
            int callCount = methodDescription.getCallCount();
            Node mainNode = node(nodeName).with(Label.html("<b>" + nodeName + "</b><br/>" + callCount));

            List toNodes = new ArrayList();

            for (Map.Entry<String, Integer> singleMethod : methodDescription.getMethodDependencies().entrySet()) {
                String linkedNode = singleMethod.getKey();
                if (linkedNode.equals("main")) {
                    continue;
                }

                toNodes.add(to(node(linkedNode)).with(Label.of(singleMethod.getValue().toString())));
            }
            System.out.println(this.mainNodes);
            this.mainNodes.add(mainNode.link(toNodes));
        }
    }

    private void methodsInPackage() {
        for (Map.Entry<String, String> methodP : this.methodInPackage.entrySet()) {
            String nodeName = methodP.getKey();
            if (nodeName == null) {
                continue;
            }

            Node mainNode = node(nodeName).with(Label.of(nodeName));

            List toNodes = new ArrayList();
            toNodes.add(to(node(methodP.getValue())));

            this.mainNodes.add(mainNode.link(toNodes));
        }
    }

    public void generateMethodsDependencyGraph() {
        methods();
        drawGraph("method_dependencies_graph");
    }

    public void generateFilesDependenciesGraph() {
        files();
        drawGraph("files_dependencies_graph");
    }

    public void generatePackageDependenciesGraph() {
        methodsInPackage();
        packages();
        drawGraph("package_dependencies_graph");
    }

    public void generateMethodsInFilesGraph(){
        FileMethods();
        drawGraph("methods_files_graph");
    }

    public void generateAllGraphs() {
        methods();
        methodsInPackage();
        packages();
        files();
        FileMethods();

        drawGraph("all");
    }

}
