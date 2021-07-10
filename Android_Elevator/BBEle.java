import java.util.*;

class Agent{

    public static ArrayList<Request> getCopy(ArrayList<Request> r){
        ArrayList<Request> newR = new ArrayList<>();
        for(int i=0;i<r.size();i++){
            Request r1 = r.get(i);
            Request r2 = new Request(r1.getSrc(),r1.getDest(),r1.getCtr());
            newR.add(r2);
        }
        return newR;
    }

    public static void printReqList(ArrayList<Request> r){
        System.out.println("BOIZ");
        for(int i=0;i<r.size();i++){
            Request r1 = r.get(i);
            System.out.println(r1.toString());
        }
    }

    public static boolean checkCompletion(ArrayList<Request> r){

        for(int i=0;i<r.size();i++){
            Request r1 = r.get(i);
            if(r1.getCtr() < 2){
                return true;
            }
        }

        return false;
    }
    
    static int mainCtr = 0;
    public static void createChilds(Floor root,ArrayList<Request> mainReqList){
        mainCtr++;
        System.out.println("Floor "+root.getFloorNum());

        for(int i=0;i<mainReqList.size();i++){      //First we check the requestList for all possible requests
            ArrayList<Request> reqList = getCopy(mainReqList);

            System.out.println("F "+root.getFloorNum()+" Hello i = "+i);
            Request r = reqList.get(i);     //Get those requests
            System.out.println("FOR R = "+r.toString());
            
            if(r.getCtr() == 0){            //If the request Ctr is 0 means we have reached it for the first time
                r.setCtr(1);                //Passenger Picked Up    
                Floor newFloor = new Floor(r.getSrc(),new ArrayList<>());   //Make a new Floor
                root.getChildNodes().add(newFloor);
                
                for(int j=0;j<reqList.size();j++){      //Now we check if this new Floor is some Requests Destination
                    Request rInner = reqList.get(i);    //Again get the request
                    //System.out.println("Cmon j = "+j);
                    if(rInner.getCtr() == 1){       //If requesr ctr is 1 means we have already picked him/her and hence check for dest
                        if(rInner.getDest() == r.getSrc()){     //If rInners dest = current nodes floor num ie source then set ctr = 2
                            rInner.setCtr(2);
                        }
                    }
                }
                //ArrayList<Request> newReqList = new ArrayList<>(reqList);
                ArrayList<Request> newReqList = getCopy(reqList);
                printReqList(newReqList);
                //if(checkCompletion(reqList)){
                    System.out.println("Creating...");
                    createChilds(newFloor, newReqList);
                //}

            } else if(r.getCtr() == 1){
                System.out.println("III");
                r.setCtr(2);
                Floor newFloor = new Floor(r.getDest(),new ArrayList<>());   //Make a new Floor
                root.getChildNodes().add(newFloor);
                for(int j=0;j<reqList.size();j++){      //Now we check if this new Floor is some Requests Destination
                    Request rInner = reqList.get(i);    //Again get the request

                    if(rInner.getCtr() == 1){       //If requesr ctr is 1 means we have already picked him/her and hence check for dest
                        if(rInner.getDest() == r.getSrc()){     //If rInners dest = current nodes floor num ie source then set ctr = 2
                            rInner.setCtr(2);
                        }
                    }
                }
                //ArrayList<Request> newReqList = new ArrayList<>(reqList);
                ArrayList<Request> newReqList = getCopy(reqList);
                printReqList(newReqList);
                //if(checkCompletion(reqList)){
                    System.out.println("Creating...");
                    createChilds(newFloor, newReqList);
               // }
                
            } else{
                System.out.println("BOOM");
                //reqList.remove(i);
            }
        }
    } 


    public static void main(String[] args) {
        
        ArrayList<Request> requestList = new ArrayList<>();

        Request req1 = new Request(2,4,0);
        Request req2 = new Request(3,0,0);

        requestList.add(req1);
        requestList.add(req2);

        /* for(int i=0;i<requestList.size();i++){
            Request r1 = requestList.get(i);
            System.out.println(r1.toString());
            
        } */

        
        Floor rootFloor = new Floor(0,new ArrayList<>());
       createChilds(rootFloor,requestList);
                
    }
}