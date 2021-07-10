package com.plcr.elevator;

public class Requests {

    int src;
    int dest;
    int ctr;

    public Requests(int src, int dest, int ctr) {
        this.src = src;
        this.dest = dest;
        this.ctr = ctr;
    }

    public Requests(){}

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public int getCtr() {
        return ctr;
    }

    public void setCtr(int ctr) {
        this.ctr = ctr;
    }

    @Override
    public String toString() {
        return "Request [ctr=" + ctr + ", dest=" + dest + ", src=" + src + "]";
    }
}
