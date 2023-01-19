package link.portalbox.pluginportal.utils;

public class Pair {

    private Object left;
    private Object right;

    public Pair(Object left, Object right) {
        this.left = left;
        this.right = right;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public void setLeft(Object object) {
        this.left = object;
    }

    public void setRight(Object object) {
        this.right = object;
    }
}