package com.example.prac3_island_builder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Structure> selectorData;
    MapElement[][] mapGridData;

    public MainActivity() {
        //adds selector data to an arrayList
        selectorData = new ArrayList<>();
        StructureData sd = StructureData.get();
        for (int i = 0; i < sd.size(); i++) {
            selectorData.add(sd.get(i));
        }
        //gets map data fills 2D array
        MapData md = MapData.get();
        mapGridData = new MapElement[MapData.HEIGHT][MapData.WIDTH];

        for (int i = 0; i < MapData.HEIGHT; i++) {
            for (int j = 0; j < MapData.WIDTH; j++) {
                mapGridData[i][j] = md.get(i,j);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager frag = getSupportFragmentManager();

       //instantiates selector list
        Selector selectorFrag = (Selector) frag.findFragmentById(R.id.selectorFrame);
        if (selectorFrag == null){
            selectorFrag = new Selector(selectorData);
            frag.beginTransaction().add(R.id.selectorFrame, selectorFrag).commit();
        }

        //instantiates MAP
        Map mapFrag = (Map) frag.findFragmentById(R.id.mapFrame);
        if (mapFrag == null){
            mapFrag = new Map(mapGridData);
            frag.beginTransaction().add(R.id.mapFrame, mapFrag).commit();
        }
        //set selector fragment reference for map class
        mapFrag.setSelectorFrag(selectorFrag);

    }

}