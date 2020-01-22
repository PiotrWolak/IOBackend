package io.puchatki.model;

public class MethodBodyDescription {
    public String name;
    public String src;
    public String content;
    public String[] body;

    public void setSrc(String src) {
        this.src = src;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMethodName() {
        String[] split = content.split("\\n");
        if (split[0].contains("@")) {
            this.name = split[1].split("\\{")[0];
        } else {
            this.name = split[0].split("\\{")[0];

        }
    }

    public void setMethodBody() {
        String[] split = content.split("\\n");
        if (split[0].contains("@")) {
            this.body = new String[split.length - 2];
            System.arraycopy(split, 2, body, 0, split.length - 2);
        } else {
            this.body = new String[split.length - 1];
            System.arraycopy(split, 1, body, 0, split.length - 1);
        }
    }

    public String[] getMethodBody() {
        return body;
    }

    public String getMethodName() {
        return name;
    }

    public String getSrc() {
        return src;
    }

    @Override
    public String toString() {
        return "MethodBodyDetails{" +
                "src='" + src + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
