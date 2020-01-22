package io.puchatki.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PackageDescription {
    private String packageName;
    private Map<String, Integer> packageDependencies;

    public PackageDescription(String packageName) {
        this.packageName = packageName;
        this.packageDependencies = new HashMap<>();
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (Objects.isNull(o) || getClass() != o.getClass()) return false;
        PackageDescription that = (PackageDescription) o;
        return packageName.equals(that.packageName);
    }

    public Map<String, Integer> getPackageDependencies() {
        return packageDependencies;
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageName);
    }
}
