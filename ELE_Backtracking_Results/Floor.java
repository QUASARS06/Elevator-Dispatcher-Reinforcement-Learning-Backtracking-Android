import java.util.*;

public class Floor {
    int floorNum;
    ArrayList<Floor> childNodes;

    public Floor(int floorNum, ArrayList<Floor> childNodes) {
        this.floorNum = floorNum;
        this.childNodes = childNodes;
    }

    public Floor(){}

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public ArrayList<Floor> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(ArrayList<Floor> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public String toString() {
        return "Floor [childNodes=" + childNodes + ", floorNum=" + floorNum + "]";
    }

    
    
    
}