import java.util.*;

class Agent{

    public static double findMax(double arr[]){
        double max = -100000.0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]>max){
                max = arr[i];
            }
        }
        return max;
    }

    // public static double reward(int curr_state,int action,int next_state,ArrayList<Request> passengers){
    //     double reward = 0;
    //     double neg_inf = -100;
    //     if(curr_state == next_state){
    //         reward = neg_inf;
    //     } else if(curr_state > next_state){ //Lift Going Down(1)
    //         if(action == 0){   //0-Up , 1-Down
    //             reward = neg_inf;
    //         } else{
    //             for(int i=0;i<passengers.size();i++){
    //                 Request r = passengers.get(i);
    //                 int srcDestDiff = r.getSourceFloor() - r.getDestFloor(); //If Positive then wants to go down else up
    //                 int liftMovingCost = Math.abs(curr_state - next_state)*-1;
    //                 reward+=liftMovingCost;
    //                 if(r.getDestFloor() == next_state){
    //                     reward+=50;
    //                 }else if(srcDestDiff > 0){    //Wants to go down and action=1 ie lift is going down so happy
    //                     //Nothing
    //                 } else{
    //                     reward += 5*(liftMovingCost); 
    //                 }
    //             }
    //         }
    //     } else if(curr_state < next_state){ //Lift Going Up(0)
    //         if(action == 1){   //0-Up , 1-Down
    //             reward = neg_inf;
    //         } else{
    //             for(int i=0;i<passengers.size();i++){
    //                 Request r = passengers.get(i);
    //                 int srcDestDiff = r.getSourceFloor() - r.getDestFloor(); //If Positive then wants to go down else up
    //                 int liftMovingCost = Math.abs(curr_state - next_state)*-1;
    //                 reward+=liftMovingCost;
    //                 if(r.getDestFloor() == next_state){
    //                     reward+=50;
    //                 } else if(srcDestDiff > 0){    //Wants to go down and action=0 ie lift is going up so NOT happy
    //                     reward += 5*(liftMovingCost); 
    //                 } else{     //Wants to go up and action=0 ie lift is going up so happy
    //                     //Nothing
    //                 }
    //             }

    //         }   
    //     }
        
    //     return reward;
    // }

    public static void main(String[] args) {

        //-1 for every second/floor through which lift moves
        //+10 for picking up a passenger
        //-5 for every second of delay to reach destination
        //+50 for dropping him to his destination
        // ArrayList<Request> passengersInLift = new ArrayList<>();
        System.out.println("B");
        int numberOfStates = 4;
        int numberOfActions = 3;

        // passengersInLift.add(new Request(0,2,false));
        // passengersInLift.add(new Request(1,3,false));
        
        double neg_inf = 0;

        double nan  = Double.NaN;

        double p[][][] = 
        {{{0,1,0,0},       {0,0,1,0},       {0,0,0,1}},     //Floor 5
         {{1,0,0,0},       {0,0,1,0},       {0,0,0,1}},      //Floor 7
         {{1,0,0,0},      {0,1,0,0},       {0,0,0,1}},      //Floor 9
         {{1,0,0,0},      {0,1,0,0},       {0,0,1,0}}      //Floor 11
        };

        double r[][][] = 
        {{{0,20,0,0},       {0,0,20,0},       {0,0,0,-30}},     //Floor 5
         {{-50,0,0,0},       {0,0,10,0},       {0,0,0,20}},      //Floor 7
         {{-50,0,0,0},      {0,-50,0,0},       {0,0,0,30}},      //Floor 9
         {{-50,0,0,0},      {0,-50,0,0},       {0,0,-50,0}}      //Floor 11
        };


        double gamma = 0.9; //Discount Factor

        double q[][] = new double[numberOfStates][numberOfActions];

        int iterations = 85;
        for(int iter=0;iter<iterations;iter++){
            double q_prev[][] = new double[numberOfStates][numberOfActions];
            for(int i=0;i<numberOfStates;i++){
                for(int j=0;j<numberOfActions;j++){
                    q_prev[i][j] = q[i][j]; 
                }  
            }

            for(int state=0;state<numberOfStates;state++){
                for(int actions=0;actions<numberOfActions;actions++){
                    double sum = 0.0;
                    for(int state_next=0;state_next<numberOfStates;state_next++){
                        
                        //Iterative Policy Evaluation
                        if(p[state][actions][state_next] > 0){                            
                            sum+=p[state][actions][state_next] * (r[state][actions][state_next] + gamma*findMax(q_prev[state_next]));
                        }                        
                    }
                    q[state][actions] = sum;
                }
            }                      

        }

        System.out.println("------------------");
        for(int i=0;i<numberOfStates;i++){
            for(int j=0;j<numberOfActions;j++){
                System.out.print(q[i][j]+"\t");
            }
            System.out.print("\n");
        }

    }
}