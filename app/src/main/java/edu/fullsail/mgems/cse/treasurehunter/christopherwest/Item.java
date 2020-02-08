package edu.fullsail.mgems.cse.treasurehunter.christopherwest;

public class Item {
    public String name;
    public float x;
    public float y;

    public Item(String name) {
        this.name = name;
        this.x = 0;
        this.y = 0;
    }

    public Item(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", (" + x +
                ", " + y + ")" +
                '}';
    }

    public boolean isCollision(float x, float y, float radius) {
        float dx = this.x - x;
        float dy = this.y - y;

        return (dx)*(dx) + (dy)*(dy) < radius*radius;

    }
}
