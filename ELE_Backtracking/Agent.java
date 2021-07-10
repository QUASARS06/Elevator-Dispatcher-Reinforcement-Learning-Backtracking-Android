import java.util.*;

class Agent{

    private static ArrayList<String> sequences = new ArrayList<>();

    public static ArrayList<Request> getCopy(ArrayList<Request> r){
        ArrayList<Request> newR = new ArrayList<>();
        for(int i=0;i<r.size();i++){
            Request r1 = r.get(i);
            Request r2 = new Request(r1.getSrc(),r1.getDest(),r1.getCtr());
            newR.add(r2);
        }
        return newR;
    }
    
    public static void createChilds(Floor root,ArrayList<Request> mainReqList){

        for(int i=0;i<mainReqList.size();i++){      //First we check the requestList for all possible requests
            ArrayList<Request> reqList = getCopy(mainReqList);

            Request r = reqList.get(i);     //Get those requests
            
            if(r.getCtr() == 0){            //If the request Ctr is 0 means we have reached it for the first time
                r.setCtr(1);                //Passenger Picked Up    
                Floor newFloor = new Floor(r.getSrc(),new ArrayList<>());   //Make a new Floor
                root.getChildNodes().add(newFloor);
                
                for(int j=0;j<reqList.size();j++){      //Now we check if this new Floor is some Requests Destination
                    Request rInner = reqList.get(i);    //Again get the request
                    if(rInner.getCtr() == 1){       //If requesr ctr is 1 means we have already picked him/her and hence check for dest
                        if(rInner.getDest() == r.getSrc()){     //If rInners dest = current nodes floor num ie source then set ctr = 2
                            rInner.setCtr(2);
                        }
                    }
                }

                ArrayList<Request> newReqList = getCopy(reqList);
                createChilds(newFloor, newReqList);

            } else if(r.getCtr() == 1){
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
                ArrayList<Request> newReqList = getCopy(reqList);
                createChilds(newFloor, newReqList);
                
            } else{
            }
        }
    } 

    public static void printFloors(Floor floor,String str){

        str += ""+floor.getFloorNum()+",";
        ArrayList<Floor> arr = floor.getChildNodes();
        if(arr.size() == 0){
            sequences.add(str.substring(0,str.length()-1));
        } else{
            for(int i=0;i<arr.size();i++){
                printFloors(arr.get(i),str);
            }
        }
    }

    public static int getCost(String str){

        int c = 0;
        String splitArr[] = str.split(",");
        for(int i=0;i<splitArr.length-1;i++){
            int num1 = Integer.parseInt(splitArr[i]);
            int num2 = Integer.parseInt(splitArr[i+1]);
            c+= (Math.abs(num1-num2));
        }

        return c;
    }

    public static void main(String[] args) {
        
        ArrayList<Request> requestList = new ArrayList<>();

        Request req1 = new Request(3,1,0);
        Request req2 = new Request(4,7,0);
        Request req3 = new Request(2,4,0);

        requestList.add(req1);
        requestList.add(req2);
        requestList.add(req3);

        
        Floor rootFloor = new Floor(6,new ArrayList<>());
        createChilds(rootFloor,requestList);
        printFloors(rootFloor,"");

        int minCost = Integer.MAX_VALUE;
        String bestPath = "";

        for(int i=0;i<sequences.size();i++){
            String s = sequences.get(i);
            int cost = getCost(s);
            System.out.println("Seq = "+s+"  --> Cost = "+cost);

            if(cost < minCost){
                minCost = cost;
                bestPath = s;
            }
        }

        System.out.println("\nBEST PATH = "+bestPath+" with COST = "+minCost);
             
    }
}