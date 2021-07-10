import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Test{

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

	public static void printReqList(ArrayList<Request> r){
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
        //System.out.println();
    	if(arr.size() == 0){
            //System.out.println(str);
    		sequences.add(str.substring(0,str.length()-1));
    	} else{
    		for(int i=0;i<arr.size();i++){
                //System.out.print(","+floor.getFloorNum());
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

    public static int[] generateRequest(int min, int max){
    	int src = ThreadLocalRandom.current().nextInt(min, max + 1);
    	int dest = src;
    	while(dest != src)
    		dest = ThreadLocalRandom.current().nextInt(min, max + 1);

    	int arr[] = new int[2];
    	arr[0] = src;
    	arr[1] = dest;

    	return arr;
    }

    public static void main(String[] args) {

    	int NUM_OF_FLOORS = 7;

    	ArrayList<Double> times = new ArrayList<>();
    	ArrayList<Request> requestList = new ArrayList<>();

    	for(int request=2;request<13;request++){
			// 5 runs of the elevator generating n requests
			requestList.clear();
			int epochs = 5;
			long total = 0;
			System.out.print("REQUESTS = "+request);
    		for(int i=0;i<epochs;i++){
    			requestList.clear();

    			int time = request;
    			while(time-->0){
    				int[] req = generateRequest(0, NUM_OF_FLOORS);
    				requestList.add(new Request(req[0], req[1], 0));
    			}

    			//System.out.print("EPOCH "+(i+1)+" LEN = "+requestList.size());

    			int startFloor = ThreadLocalRandom.current().nextInt(0, NUM_OF_FLOORS + 1);
    			Floor rootFloor = new Floor(startFloor,new ArrayList<>());

    			long startTime = System.nanoTime();
    			createChilds(rootFloor,requestList);
    			long stopTime = System.nanoTime();

    			long totalTime = (stopTime - startTime);
    			//System.out.println(" TIME = "+totalTime);
    			total+=totalTime;
    		}
    		double ans = total/(epochs*1000000.0);
    		times.add(ans);
    		System.out.println("  TIME = "+ans);
    	}

    	System.out.println(times.toString());

    }
}