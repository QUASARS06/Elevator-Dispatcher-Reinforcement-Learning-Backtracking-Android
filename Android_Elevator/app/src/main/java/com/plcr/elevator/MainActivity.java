package com.plcr.elevator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    ImageView lift;
    DisplayMetrics displayMetrics = new DisplayMetrics();
    int height = 0;
    int width = 0;

    TextView groundL;
    TextView floor1L;
    TextView floor2L;
    TextView floor3L;
    TextView floor4L;
    TextView floor5L;
    TextView floor6L;
    TextView floor7L;

    TextView groundR;
    TextView floor1R;
    TextView floor2R;
    TextView floor3R;
    TextView floor4R;
    TextView floor5R;
    TextView floor6R;
    TextView floor7R;

    static float currXTrans = 0.0f;

    private ArrayList<String> sequences = new ArrayList<>();
    private ArrayList<Integer> cost = new ArrayList<>();
    int minCost = Integer.MAX_VALUE;
    String bestPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lift = findViewById(R.id.lift);

        groundL = findViewById(R.id.gleft);
        groundR = findViewById(R.id.gright);
        floor1L = findViewById(R.id.oneleft);
        floor1R = findViewById(R.id.oneright);
        floor2L = findViewById(R.id.twoleft);
        floor2R = findViewById(R.id.tworight);
        floor3L = findViewById(R.id.threeleft);
        floor3R = findViewById(R.id.threeright);
        floor4L = findViewById(R.id.fourleft);
        floor4R = findViewById(R.id.fourright);
        floor5L = findViewById(R.id.fiveleft);
        floor5R = findViewById(R.id.fiveright);
        floor6L = findViewById(R.id.sixleft);
        floor6R = findViewById(R.id.sixright);
        floor7L = findViewById(R.id.sevenleft);
        floor7R = findViewById(R.id.sevenright);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        attachListeners();
        showDialog();


    }

    private void attachListeners() {
        groundL.setOnClickListener(this);
        groundR.setOnClickListener(this);
        floor1L.setOnClickListener(this);
        floor1R.setOnClickListener(this);
        floor2L.setOnClickListener(this);
        floor2R.setOnClickListener(this);
        floor3L.setOnClickListener(this);
        floor3R.setOnClickListener(this);
        floor4L.setOnClickListener(this);
        floor4R.setOnClickListener(this);
        floor5L.setOnClickListener(this);
        floor5R.setOnClickListener(this);
        floor6L.setOnClickListener(this);
        floor6R.setOnClickListener(this);
        floor7L.setOnClickListener(this);
        floor7R.setOnClickListener(this);

    }


    public float findYTranslation(int floorNum){
        float multiplyingFactor = 0.0f;
        if(floorNum<=3) {
            multiplyingFactor = 2.8f * (8 - floorNum);
        } else{
            multiplyingFactor = 2.834f * (8 - floorNum);
        }
        float top = height - (multiplyingFactor*lift.getDrawable().getIntrinsicHeight());

        return top;
    }

    public void startLiftAnimation(int floorNum){

        float xTrans = findYTranslation(floorNum);
        Log.d(TAG, "startLiftAnimation: Curr = "+currXTrans);
        Log.d(TAG, "startLiftAnimation: X = "+xTrans);
        final Animation animation = new TranslateAnimation(0, 0,currXTrans, -1*xTrans);
        currXTrans = -1*xTrans;
        animation.setDuration(1000);
        animation.setFillAfter(true);
        lift.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.gright  :
            case R.id.gleft  :
                startLiftAnimation(0);
                break;
            case R.id.oneright  :
            case R.id.oneleft  :
                startLiftAnimation(1);
                break;
            case R.id.tworight  :
            case R.id.twoleft  :
                startLiftAnimation(2);
                break;
            case R.id.threeright  :
            case R.id.threeleft  :
                startLiftAnimation(3);
                break;
            case R.id.fourright  :
            case R.id.fourleft  :
                startLiftAnimation(4);
                break;
            case R.id.fiveright  :
            case R.id.fiveleft  :
                startLiftAnimation(5);
                break;
            case R.id.sixright  :
            case R.id.sixleft  :
                startLiftAnimation(6);
                break;
            case R.id.sevenright  :
            case R.id.sevenleft  :
                startLiftAnimation(7);
                break;

        }

    }

    public void showDialog(){

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView txt = new TextView(this);
        txt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txt.setText("MAKE RequestsS");
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(0,16,0,16);
        txt.setTextSize(25.0f);

        final EditText numberOfRequestss = new EditText(this);
        numberOfRequestss.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        numberOfRequestss.setInputType(InputType.TYPE_CLASS_NUMBER);
        numberOfRequestss.setHint("Enter the Number of Requestss");

        Button loadEditText = new Button(this);
        loadEditText.setText("Generate");
        loadEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numberOfRequestss.getText().toString());
                for(int i=1;i<=num;i++){
                    final EditText req = new EditText(v.getContext());
                    req.setId(i);
                    req.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    req.setHint("Requests ->"+(i)+" Enter Source,Dest (CSV)");

                    layout.addView(req);
                }
            }
        });

        layout.addView(txt);
        layout.addView(numberOfRequestss);
        layout.addView(loadEditText);

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setView(layout);
            /*helpBuilder.setTitle("Help");
            helpBuilder.setMessage(R.string.userHelp);*/

        helpBuilder.setPositiveButton("SIMULATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int num = Integer.parseInt(numberOfRequestss.getText().toString());

                ArrayList<Requests> requestList = new ArrayList<>();

                for(int i=1;i<=num;i++){
                    EditText e = layout.findViewById(i);
                    String[] split = e.getText().toString().split(",");
                    Log.d(TAG, "Floor "+split[0]+" to Floor "+split[1]);
                    Requests req1 = new Requests(Integer.parseInt(split[0]),Integer.parseInt(split[1]),0);

                    requestList.add(req1);
                }

                startSimulation(requestList);
            }
        });

        AlertDialog a = helpBuilder.create();
        a.show();
    }

    public void startSimulation(ArrayList<Requests> requestList){

        Floor rootFloor = new Floor(0,new ArrayList<Floor>());
        createChilds(rootFloor,requestList);
        printFloors(rootFloor,"");

        for(int i=0;i<sequences.size();i++){
            String s = sequences.get(i);
            int cos = getCost(s);
            cost.add(i,cos);
            System.out.println("Seq = "+s+"  --> Cost = "+cos);

            if(cos < minCost){
                minCost = cos;
                bestPath = s;
            }
        }

        animateLift(bestPath,minCost);
        System.out.println("\nBEST PATH = "+bestPath+" with COST = "+minCost);

    }

    private void animateLift(String bestPath, int minCost) {
        final String path[] = bestPath.split(",");
        for(int i=0;i<path.length;i++){
            final int finalI = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startLiftAnimation(Integer.parseInt(path[finalI]));
                }
            },i*1500);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMssgDialog();
                //Toast.makeText(MainActivity.this, "Good", Toast.LENGTH_SHORT).show();
            }
        },1500*path.length);

    }

    private void showMssgDialog() {

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView txt = new TextView(this);
        txt.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        txt.setText("ALL BEST PATHS (C = "+minCost+")");
        txt.setGravity(Gravity.CENTER);
        txt.setPadding(0,16,0,16);
        txt.setTextSize(25.0f);

        layout.addView(txt);
        for(int i=0;i<cost.size();i++){
            if(cost.get(i) <= minCost){
                String path = sequences.get(i).replace(",","->");
                TextView txt1 = new TextView(this);
                txt1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                txt1.setGravity(Gravity.CENTER);
                txt1.setPadding(0,10,0,10);
                txt1.setText(path);
                txt1.setTextSize(20.0f);

                layout.addView(txt1);
            }
        }



        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setView(layout);

        helpBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog a = helpBuilder.create();
        a.show();
    }

    public ArrayList<Requests> getCopy(ArrayList<Requests> r){
        ArrayList<Requests> newR = new ArrayList<>();
        for(int i=0;i<r.size();i++){
            Requests r1 = r.get(i);
            Requests r2 = new Requests(r1.getSrc(),r1.getDest(),r1.getCtr());
            newR.add(r2);
        }
        return newR;
    }


    public void createChilds(Floor root,ArrayList<Requests> mainReqList){

        for(int i=0;i<mainReqList.size();i++){      //First we check the RequestsList for all possible Requestss
            ArrayList<Requests> reqList = getCopy(mainReqList);

            Requests r = reqList.get(i);     //Get those Requestss

            if(r.getCtr() == 0){            //If the Requests Ctr is 0 means we have reached it for the first time
                r.setCtr(1);                //Passenger Picked Up
                Floor newFloor = new Floor(r.getSrc(),new ArrayList<Floor>());   //Make a new Floor
                root.getChildNodes().add(newFloor);

                for(int j=0;j<reqList.size();j++){      //Now we check if this new Floor is some Requestss Destination
                    Requests rInner = reqList.get(i);    //Again get the Requests
                    if(rInner.getCtr() == 1){       //If requesr ctr is 1 means we have already picked him/her and hence check for dest
                        if(rInner.getDest() == r.getSrc()){     //If rInners dest = current nodes floor num ie source then set ctr = 2
                            rInner.setCtr(2);
                        }
                    }
                }

                ArrayList<Requests> newReqList = getCopy(reqList);
                createChilds(newFloor, newReqList);

            } else if(r.getCtr() == 1){
                r.setCtr(2);
                Floor newFloor = new Floor(r.getDest(),new ArrayList<Floor>());   //Make a new Floor
                root.getChildNodes().add(newFloor);
                for(int j=0;j<reqList.size();j++){      //Now we check if this new Floor is some Requestss Destination
                    Requests rInner = reqList.get(i);    //Again get the Requests

                    if(rInner.getCtr() == 1){       //If requesr ctr is 1 means we have already picked him/her and hence check for dest
                        if(rInner.getDest() == r.getSrc()){     //If rInners dest = current nodes floor num ie source then set ctr = 2
                            rInner.setCtr(2);
                        }
                    }
                }
                ArrayList<Requests> newReqList = getCopy(reqList);
                createChilds(newFloor, newReqList);

            } else{
            }
        }
    }

    public void printFloors(Floor floor,String str){

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

    public int getCost(String str){

        int c = 0;
        String splitArr[] = str.split(",");
        for(int i=0;i<splitArr.length-1;i++){
            int num1 = Integer.parseInt(splitArr[i]);
            int num2 = Integer.parseInt(splitArr[i+1]);
            c+= (Math.abs(num1-num2));
        }

        return c;
    }
}
