package com.example.prac3_island_builder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Selector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Selector extends Fragment {

    // Keys for bundle map must be identifying strings
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DRAWABLE = "drawableID";
    private static final String ARG_LABEL = "label";

    // TODO: Not currently used
    private int mDrawable;
    private String mLabel;
    private Structure selected;

    ArrayList<Structure> data;

    public Selector() {
        // Required empty public constructor
    }
    public Selector(ArrayList<Structure> data) {
        this.data = data;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param drawable Parameter 1.
     * @param label Parameter 2.
     * @return A new instance of fragment Selector.
     */
    // TODO: Rename and change types and number of parameters
    public static Selector newInstance(int drawable, String label) {
        Selector fragment = new Selector();
        Bundle args = new Bundle();
        args.putInt(ARG_DRAWABLE, drawable);
        args.putString(ARG_LABEL, label);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDrawable = getArguments().getInt(ARG_DRAWABLE);
            mLabel = getArguments().getString(ARG_LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selector, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SelectorAdapter selectorAdapter = new SelectorAdapter(data);
        rv.setAdapter(selectorAdapter);
        return view;
    }

    /**
     * Private inner Class SelectorAdapter
     * */
    private class SelectorAdapter extends RecyclerView.Adapter<SelectorViewHolder> {

        ArrayList<Structure> data; //list of structures

        public SelectorAdapter(ArrayList<Structure> data){
            this.data = data;
        }
        @NonNull
        @Override
        public SelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_selection,parent,false);
            return new SelectorViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectorViewHolder holder, int position) {
            setSelected(data.get(position)); //stores current tile data
            holder.itemName.setText(selected.getLabel());
            holder.itemImg.setImageResource(selected.getDrawableId());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    /**
     * Private Inner Class SelectorViewHolder
     * Handles Logic
     */
    private class SelectorViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        ImageView itemImg;
        public SelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.listText);
            itemImg = itemView.findViewById(R.id.listImg);

            //Button Click
            itemImg.setOnClickListener(view -> {
                SelectorAdapter adapter = (SelectorAdapter) getBindingAdapter();
                int position = this.getBindingAdapterPosition();
                setSelected(adapter.data.get(position));
                String snackBarMessage = "Selected: " + selected.getLabel();
                Snackbar snackbar = Snackbar.make(getView(), snackBarMessage, Snackbar.LENGTH_SHORT);
                snackbar.show();

            });
        }
    }

    public void setSelected(Structure selected) {
        this.selected = selected;
    }

    public Structure getSelected() {
        return selected;
    }
}