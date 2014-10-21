package com.blazeloader.TerrainEdit.cuboid;

/**
 * Represents a rectangular cuboid specified by 2 sets of coordinates.
 * TODO add iterator functions
 */
public class Cuboid {
    protected int xPos1;
    protected int yPos1;
    protected int zPos1;
    protected int xPos2;
    protected int yPos2;
    protected int zPos2;
    protected boolean isSet;

    public Cuboid(int xPos1, int yPos1, int zPos1, int xPos2, int yPos2, int zPos2, boolean isSet) {
        this.xPos1 = xPos1;
        this.yPos1 = yPos1;
        this.zPos1 = zPos1;
        this.xPos2 = xPos2;
        this.yPos2 = yPos2;
        this.zPos2 = zPos2;
        this.isSet = isSet;
    }

    public int getXPos1() {
        return xPos1;
    }

    public void setXPos1(int xPos1) {
        this.xPos1 = xPos1;
    }

    public int getYPos1() {
        return yPos1;
    }

    public void setYPos1(int yPos1) {
        this.yPos1 = yPos1;
    }

    public int getZPos1() {
        return zPos1;
    }

    public void setZPos1(int zPos1) {
        this.zPos1 = zPos1;
    }

    public int getXPos2() {
        return xPos2;
    }

    public void setXPos2(int xPos2) {
        this.xPos2 = xPos2;
    }

    public int getYPos2() {
        return yPos2;
    }

    public void setYPos2(int yPos2) {
        this.yPos2 = yPos2;
    }

    public int getZPos2() {
        return zPos2;
    }

    public void setZPos2(int zPos2) {
        this.zPos2 = zPos2;
    }

    public void set() {
        this.isSet = true;
    }

    public boolean isSet() {
        return this.isSet;
    }

    public int getMinX() {
        return Math.min(xPos1, xPos2);
    }

    public int getMinY() {
        return Math.min(yPos1, yPos2);
    }

    public int getMinZ() {
        return Math.min(zPos1, zPos2);
    }

    public int getMaxX() {
        return Math.max(xPos1, xPos2);
    }

    public int getMaxY() {
        return Math.max(yPos1, yPos2);
    }

    public int getMaxZ() {
        return Math.max(zPos1, zPos2);
    }
}
