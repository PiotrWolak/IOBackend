package io.puchatki.logic;

import io.puchatki.model.PackageDescription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageAnalyzer {

    public static List<PackageDescription> generatePackageDetailsDependencies(StringBuilder graphBuilder) {
        PackageAnalyzer.generatePackageDependenciesDotFiles();
        List dotFiles = ProjectFilesProcessor.getAllDotFiles();

        Map<String, PackageDescription> packageDetailsMap = new HashMap<>();
        List<PackageDescription> packageDescriptionList = new ArrayList<>();

        if (dotFiles != null) {
            dotFiles.forEach(dot -> {
                String file = dot.toString();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder builder = new StringBuilder();
                    String currentLine = reader.readLine();
                    PackageDescription packageDescription = null;

                    while (currentLine != null) {

                        if (currentLine.contains("io.puchatki") && !currentLine.contains("java.") && !currentLine.contains("com.") && !currentLine.contains("guru.")) { //exclusion
                            String current = currentLine.replace(" ( not found )", "");
                            String c = current.substring(0, current.length() - 1);

                            c = c.replaceAll("\\s+", "");

                            String[] dependencyParts = c.split("->");
                            dependencyParts[0] = dependencyParts[0].replaceAll("\"", "");
                            dependencyParts[1] = dependencyParts[1].replaceAll("\"", "");

                            if (packageDescription == null) {
                                if (packageDetailsMap.containsKey(dependencyParts[0])) {
                                    packageDescription = packageDetailsMap.get(dependencyParts[0]);
                                } else {
                                    packageDescription = new PackageDescription(dependencyParts[0]);
                                }
                            }
                            if (packageDescription.getPackageDependencies().containsKey(dependencyParts[1])) {
                                Integer value = packageDescription.getPackageDependencies().get(dependencyParts[1]);
                                packageDescription.getPackageDependencies().put(dependencyParts[1], value + 1);
                            } else {
                                packageDescription.getPackageDependencies().put(dependencyParts[1], 1);
                            }
                            packageDescriptionList.add(packageDescription);
                            builder.append(c);
                            graphBuilder.append(c);
                        }
                        currentLine = reader.readLine();
                    }
                    reader.close();
                    if (packageDescription != null) {
                        packageDetailsMap.put(packageDescription.getPackageName(), packageDescription);
                    }
                } catch (Error | IOException e) {

                }
            });
        }

        return packageDescriptionList;
    }

    public static void generatePackageDependenciesDotFiles() {
        List classFiles = ProjectFilesProcessor.getAllClassFiles();

        System.out.println("resolving project dependencies, hang on...");

        if (classFiles != null) {
            List<String> classList = new ArrayList<>();
            classFiles.forEach(classFile -> {
                try {

                    Process p = Runtime.getRuntime().exec("jdeps -dotoutput dots -verbose:package " + classFile);

                    BufferedReader stdInput = new BufferedReader(new
                            InputStreamReader(p.getInputStream()));


                    System.out.print(" ( ͡° ͜ʖ ͡°) ");
                    String s;
                    StringBuilder ll = new StringBuilder();

                    while ((s = stdInput.readLine()) != null) {
                        ll.append(s).append('\n');

                    }

                    classList.add(ll.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
        System.out.println("");
    }
}
