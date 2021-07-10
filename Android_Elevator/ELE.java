import java.util.*;

class Agent{

    public static double findMax(double arr[]){
        double max = -100000.0;
        for(int i=0;i<arr.length;i++){
            //System.out.print(arr[i]+" ,");
            if(arr[i]>max){
                max = arr[i];
            }
        }
        //System.out.println("  Max = "+max);
        return max;
    }

    public static double reward(int curr_state,int action,int next_state,ArrayList<Request> passengers){
        double reward = 0;
        double neg_inf = -100;
        if(curr_state == next_state){
            reward = neg_inf;
        } else if(curr_state > next_state){ //Lift Going Down(1)
            if(action == 0){   //0-Up , 1-Down
                reward = neg_inf;
            } else{
                for(int i=0;i<passengers.size();i++){
                    Request r = passengers.get(i);
                    int srcDestDiff = r.getSourceFloor() - r.getDestFloor(); //If Positive then wants to go down else up
                    int liftMovingCost = Math.abs(curr_state - next_state)*-1;
                    reward+=liftMovingCost;
                    if(r.getDestFloor() == next_state){
                        reward+=50;
                    }else if(srcDestDiff > 0){    //Wants to go down and action=1 ie lift is going down so happy
                        //Nothing
                    } else{
                        reward += 5*(liftMovingCost); 
                    }
                }
            }
        } else if(curr_state < next_state){ //Lift Going Up(0)
            if(action == 1){   //0-Up , 1-Down
                reward = neg_inf;
            } else{
                for(int i=0;i<passengers.size();i++){
                    Request r = passengers.get(i);
                    int srcDestDiff = r.getSourceFloor() - r.getDestFloor(); //If Positive then wants to go down else up
                    int liftMovingCost = Math.abs(curr_state - next_state)*-1;
                    reward+=liftMovingCost;
                    if(r.getDestFloor() == next_state){
                        reward+=50;
                    } else if(srcDestDiff > 0){    //Wants to go down and action=0 ie lift is going up so NOT happy
                        reward += 5*(liftMovingCost); 
                    } else{     //Wants to go up and action=0 ie lift is going up so happy
                        //Nothing
                    }
                }

            }   
        }
        // System.out.print("\nCurr State = Floor "+curr_state);
        // System.out.print("\nNext State = Floor "+next_state);
        // System.out.print("\nAction Chosen =  "+action+" 0-Up , 1-Down");
        // System.out.println("\nReward = "+reward);
        return reward;
    }

    public static void main(String[] args) {

        //-1 for every second/floor through which lift moves
        //+10 for picking up a passenger
        //-5 for every second of delay to reach destination
        //+50 for dropping him to his destination
        ArrayList<Request> passengersInLift = new ArrayList<>();
        System.out.println("B");
        int numberOfStates = 3;
        int numberOfActions = 2;

        passengersInLift.add(new Request(2,0,false));
        passengersInLift.add(new Request(1,2,false));
        
        double neg_inf = 0;
           //Up            Down
        // double p[][][] = 
        // {{{0,0.6,0.4},{neg_inf,neg_inf,neg_inf}},     //Floor 0
        //  {{0,0,1},      {1,0,0}},                       //Floor 2
        //  {{neg_inf,neg_inf,neg_inf},{0.6,0.4,0}}      //Floor 3
        // };

        double nan  = Double.NaN;
        //0                1            2
        // double p[][][] = 
        // {{{nan,nan,nan},{0,1,0},       {0,0,1}},     //Floor 0
        //  {{1,0,0},      {nan,nan,nan}, {0,0,1}},                       //Floor 1
        //  {{1,0,0},      {0,1,0},       {nan,nan,nan}}      //Floor 2
        // };

        // double r[][][] = 
        // {{{nan,nan,nan},{nan,5,nan},       {nan,nan,5}},     //Floor 0
        //  {{30,nan,nan},      {nan,nan,nan}, {nan,nan,4}},                       //Floor 1
        //  {{30,nan,nan},      {nan,4,nan},       {nan,nan,nan}}      //Floor 2
        // };

        double p[][][] = 
        {{{0,1,0},       {0,0,1}},     //Floor 0
         {{1,0,0},       {0,0,1}},                       //Floor 1
         {{1,0,0},      {0,1,0},      }      //Floor 2
        };

        double r[][][] = 
        {{{0,5,0},       {0,0,5}},     //Floor 0
         {{-20,0,0},       {0,0,35}},                       //Floor 1
         {{30,0,0},      {0,5,0},       }      //Floor 2
        };

        // double r[][][] = 
        // {{{0,5,0},       {0,0,-15}},     //Floor 0
        //  {{10,0,0},       {0,0,10}},                       //Floor 1
        //  {{30,0,0},      {0,-20,0},       }      //Floor 2
        // };

        // double r[][][] = 
        // {{{neg_inf,3,3},{neg_inf,neg_inf,neg_inf}},
        //  {{neg_inf,neg_inf,3},{30,neg_inf,neg_inf}},
        //  {{neg_inf,neg_inf,neg_inf},{30,3,neg_inf}},
        // };

        double gamma = 0.9; //Discount Factor

        double q[][] = new double[numberOfStates][numberOfActions];

        int iterations = 25;
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
                        
                        sum+=p[state][actions][state_next] * (r[state][actions][state_next] + gamma*findMax(q_prev[state_next]));
                        //sum+=p[state][actions][state_next] * (reward(state,actions,state_next,passengersInLift) + gamma*findMax(q_prev[state_next]));
                        
                        
                    }
                    q[state][actions] = sum;
                }
            }
            
            // System.out.println("--------------------------------PREV");
            // for(int i=0;i<numberOfStates;i++){
            //     for(int j=0;j<numberOfActions;j++){
            //         System.out.print(q_prev[i][j]+" ");
            //     }
            //     System.out.print("\n");
            // }
            // System.out.println("----------------------------------NEXT");
            // for(int i=0;i<numberOfStates;i++){
            //     for(int j=0;j<numberOfActions;j++){
            //         System.out.print(q[i][j]+" ");
            //     }
            //     System.out.print("\n");
            // }
            //P[s, a, s_next] * (R[s, a, s_next] + discount_factor * np.max(Q_previous[s_next]))

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