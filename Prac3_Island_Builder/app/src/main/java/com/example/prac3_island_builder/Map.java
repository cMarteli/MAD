package com.example.prac3_island_builder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DRAWABLE_NW = "paramNW";
    private static final String ARG_DRAWABLE_NE = "paramNE";
    private static final String ARG_DRAWABLE_SW = "paramSW";
    private static final String ARG_DRAWABLE_SE = "paramSE";
    private static final String ARG_DRAWABLE_BLD = "paramBLD";

    // TODO: Rename and change types of parameters
   // private MapElement me;
    private int mNW, mNE, mSW, mSE, mBuilding;
    MapElement[][] data;
    //stores selector fragment view
    Selector selectorFrag;

    public Map() {
        // Required empty public constructor
    }
    public Map(MapElement[][] data) {
        this.data = data;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nw NorthWest block.
     * @param ne NorthEast block.
     * @param sw SouthWest block.
     * @param se SouthEast block.
     * @param bld building structure.
     * @return A new instance of fragment Map.
     */
    // TODO: Rename and change types and number of parameters
    public static Map newInstance(int nw, int ne, int sw, int se, int bld) {
        Map fragment = new Map();
        Bundle args = new Bundle();
        args.putInt(ARG_DRAWABLE_NW, nw);
        args.putInt(ARG_DRAWABLE_NE, ne);
        args.putInt(ARG_DRAWABLE_SW, sw);
        args.putInt(ARG_DRAWABLE_SE, se);
        args.putInt(ARG_DRAWABLE_BLD, bld);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNW = getArguments().getInt(ARG_DRAWABLE_NW);
            mNE = getArguments().getInt(ARG_DRAWABLE_NE);
            mSW = getArguments().getInt(ARG_DRAWABLE_SW);
            mSE = getArguments().getInt(ARG_DRAWABLE_SE);
            mBuilding = getArguments().getInt(ARG_DRAWABLE_BLD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.mapRecyclerView);
        rv.setLayoutManager(new GridLayoutManager(
                getActivity(),
                MapData.HEIGHT,
                GridLayoutManager.HORIZONTAL,
                false));
        MapAdapter mapAdapter = new MapAdapter(data);
        rv.setAdapter(mapAdapter);
        return view;
    }

    public void setSelectorFrag(Selector selectorFrag) {
        this.selectorFrag = selectorFrag;
    }

    /*
    * Private Inner class MapAdapter
    * */
    private class MapAdapter extends RecyclerView.Adapter<MapViewHolder> {

        MapElement[][] data;

        public MapAdapter(MapElement[][] data){
            this.data = data;
        }
        @NonNull
        @Override
        public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.grid_cell,parent,false);
            return new MapViewHolder(view);
        }

        //Method to draw items on to screen!
        @Override
        public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
            updateDataArray(holder, position);
        }

        private void updateDataArray(@NonNull MapViewHolder holder, int position) {
            int row = position % MapData.HEIGHT;
            int col = position / MapData.HEIGHT;

            holder.nWest.setImageResource(data[row][col].getNorthWest());
            holder.nEast.setImageResource(data[row][col].getNorthEast());
            holder.sWest.setImageResource(data[row][col].getSouthWest());
            holder.sEast.setImageResource(data[row][col].getSouthEast());
            Structure bld = data[row][col].getStructure();
            if(bld != null){ //not every tile has a structure
                holder.building.setImageResource(data[row][col].getStructure().getDrawableId());
            }
            else{//if empty make building tile invisible
                holder.building.setImageResource(0);
            }
        }

        //TODO: Not tested! returns total size of grid
        @Override
        public int getItemCount() {
            return MapData.HEIGHT*MapData.WIDTH;
        }
    }

    /*
     * Private Inner class MapAdapter
     * */
    private class MapViewHolder extends RecyclerView.ViewHolder {

        ImageView nWest, nEast, sWest, sEast, building;

        public MapViewHolder(@NonNull View itemView) {
            super(itemView);
            int size = getView().findViewById(R.id.mapRecyclerView).getMeasuredHeight() / MapData.HEIGHT + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            nWest = itemView.findViewById(R.id.terrainNW);
            nEast = itemView.findViewById(R.id.terrainNE);
            sWest = itemView.findViewById(R.id.terrainSW);
            sEast = itemView.findViewById(R.id.terrainSE);
            building = itemView.findViewById(R.id.building);

            final String snackBarMessage = "Not a buildable tile";// + getSelected().getLabel();
            //displays message to user
            Snackbar snackbar = Snackbar.make(getView(), snackBarMessage, Snackbar.LENGTH_SHORT);
            //Button Click
            building.setOnClickListener(view -> {
                Structure selected = selectorFrag.getSelected();
                if(selected != null)
                {
                    int position = this.getBindingAdapterPosition();
                    int row = position % MapData.HEIGHT;
                    int col = position / MapData.HEIGHT;
                    MapAdapter adapter = (MapAdapter) getBindingAdapter();
                    MapElement tile = adapter.data[row][col];
                    if(tile.isBuildable()){//if it's a buildable tile
                        tile.setStructure(selected); //change array
                        adapter.notifyItemChanged(position);
                    }
                    else{ snackbar.show(); } //not buildable display warning
                }
            });
       }
    }

}