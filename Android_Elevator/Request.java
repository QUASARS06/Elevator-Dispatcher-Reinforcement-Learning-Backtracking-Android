public class Request {

    int sourceFloor;
    int destFloor;
    boolean requestSatisfied;

    public Request(){}

    public Request(int sourceFloor, int destFloor, boolean requestSatisfied) {
        this.sourceFloor = sourceFloor;
        this.destFloor = destFloor;
        this.requestSatisfied = requestSatisfied;
    };

    public int getSourceFloor() {
        return sourceFloor;
    }

    public void setSourceFloor(int sourceFloor) {
        this.sourceFloor = sourceFloor;
    }

    public int getDestFloor() {
        return destFloor;
    }

    public void setDestFloor(int destFloor) {
        this.destFloor = destFloor;
    }

    public boolean isRequestSatisfied() {
        return requestSatisfied;
    }

    public void setRequestSatisfied(boolean requestSatisfied) {
        this.requestSatisfied = requestSatisfied;
    }

}