package com.example.shelinalusandro.ceceps;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;
import android.view.ViewGroup.*;
import android.support.design.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button newButton;

    private static int buttonPlanId = 0;

    private static int bottomSheetId = 0;

//    private String[] destinations = {
//            "destination 1",
//            "destination 2",
//            "destination 3",
//            "destination 4",
//            "destination 5"
//    };

    private ArrayList<DestinationItem> destinationList = new ArrayList<>();

    /*
        notes:
        first planId and bottomSheetId defined in XML Layout
        the next one use numeral ID : 2<= id <= 4
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        this.generateDestinationList();

        LinearLayout planTab = (LinearLayout) findViewById(R.id.plan_bar);

        planTab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                View replace = findViewById(R.id.plan_bar);
                ViewGroup parent = (ViewGroup) replace.getParent();
                int index = parent.indexOfChild(replace);
                parent.removeView(replace);
                replace = getLayoutInflater().inflate(R.layout.plan_bar, parent, false);
                parent.addView(replace, index);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Singapore and move the camera
        LatLng singapore = new LatLng(1.314715,103.5668226);
        mMap.addMarker(new MarkerOptions().position(singapore).title("Marker in Singapore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(singapore));
    }

    public void generateDestinationList(){
        DestinationItem item1 = new DestinationItem("destination1","123","456");
        DestinationItem item2 = new DestinationItem("destination2","789","123");
        DestinationItem item3 = new DestinationItem("destination3","456","789");
        DestinationItem item4 = new DestinationItem("destination4","123","789");
        DestinationItem item5 = new DestinationItem("destination5","456","123");
        destinationList.add(item1);
        destinationList.add(item2);
        destinationList.add(item3);
        destinationList.add(item4);
        destinationList.add(item5);
    }

    public void openTab(View view){

        bottomSheetId = view.getId();
        ViewGroup parent = (ViewGroup) findViewById(R.id.plan_bar_new);
        Button btnToSheet = (Button) parent.findViewById(bottomSheetId);

        ArrayAdapter<DestinationItem> destinationAdapter =
                new ArrayAdapter<DestinationItem>(this,
                        R.layout.destination_list,
                        R.id.destination_name,
                        destinationList
                );

        if(btnToSheet != null) {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View bottomSheetView = null;

            if(btnToSheet.equals((Button) findViewById(R.id.plan1))){

                ListView destinationListView = new ListView(this);
                destinationListView.setAdapter(destinationAdapter);

                bottomSheetView = inflater.inflate(R.layout.plan_window_1, null);

                LinearLayout ll = (LinearLayout) bottomSheetView.findViewById(R.id.destination);
                Button replace = (Button)ll.findViewById(R.id.btn_save);
                ll.removeView(replace);
                ll.addView(destinationListView);
                ll.addView(replace);

                destinationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Generate a message based on the position
                        String message = "You clicked on " + destinationList.get(position).getDestinationName();
                        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
            }

            else {
                switch(bottomSheetId){
                    case 1:
                        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_2, null);
                        break;
                    case 2:
                        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_3, null);
                        break;
                    case 3:
                        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_4, null);
                        break;
                }
            }

            bottomSheetDialog.setContentView(bottomSheetView);

            BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
            mBehavior.setPeekHeight(500);

            bottomSheetDialog.show();
        }
    }

    public void newPlan(View view){
        View addPlan = findViewById(R.id.new_plan);
        ViewGroup parent = (ViewGroup) addPlan.getParent();
        int index = parent.indexOfChild(addPlan);
        Button template = (Button) findViewById(R.id.plan1);
        LayoutParams params = template.getLayoutParams();

        if(buttonPlanId<3) {
            parent.removeView(addPlan);
            newButton = new Button(this);
            newButton.setLayoutParams(params);
            /*
            newButton.setBackgroundColor(0x27ae60);
            newButton.setAllCaps(true);
            newButton.setTextColor(0x2c3e50);
            newButton.setTypeface(Typeface.DEFAULT_BOLD);
            */
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTab(v);
                }
            });

            buttonPlanId ++;

            newButton.setId(buttonPlanId);
            String buttonText = "Plan " + (buttonPlanId+1);
            newButton.setText(buttonText);
            parent.addView(newButton);
            if(buttonPlanId!=3) parent.addView(addPlan);
            if(buttonPlanId==3) buttonPlanId=0;
        }
    }

    /**public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if(checked){
            ((CheckBox) view).setChecked(false);
        }else
            ((CheckBox) view).setChecked(true);
    }**/

}
