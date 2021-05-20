package com.jhonkkman.aniappinspiracy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TodosFragment extends Fragment {

    private RecyclerView rv_top;
    private LinearLayoutManager lym;
    private AdapterTodos adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todos, container, false);
        rv_top = view.findViewById(R.id.rv_todos_top);
        loadTops();

        return view;
    }

    public void loadTops(){
        lym = new LinearLayoutManager(getContext());
        adapter = new AdapterTodos();
        rv_top.setLayoutManager(lym);
        rv_top.setAdapter(adapter);
    }
}