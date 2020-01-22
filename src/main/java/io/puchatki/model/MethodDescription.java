package io.puchatki.model;

import java.util.HashMap;
import java.util.Map;

public class MethodDescription {
    private String methodName;
    private int callCount;
    private String src;
    private String packageName;
    private String fileName;
    private Map<String, Integer> methodDependencies;

    public MethodDescription() {
        methodDependencies = new HashMap<>();
    }

    public void setFileName() {
        String[] parsedSrc = this.getSrc().split("/");
        this.fileName = parsedSrc[parsedSrc.length-1];
    }

    public void parseSrcToPackageName() {
        String[] parsedSrc = this.getSrc().split("/");
        int startOfPackage = 0;
        for (String s : parsedSrc) {
            if (s.contains("java")) {
                break;
            }
            startOfPackage++;
        }

        StringBuilder packageName = new StringBuilder();
        for (int i = startOfPackage + 1; i < parsedSrc.length - 1; i++) {
            packageName.append(parsedSrc[i]).append(".");
        }

        this.packageName = packageName.substring(0, packageName.length() - 1);
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Integer> getMethodDependencies() {
        return methodDependencies;
    }

    public int getCallCount() {
        return callCount;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "MethodDetails{" +
                "methodName='" + methodName + '\'' +
                ", callCount=" + callCount +
                '}';
    }
}
