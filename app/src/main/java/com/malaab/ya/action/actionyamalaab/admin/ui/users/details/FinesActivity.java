package com.malaab.ya.action.actionyamalaab.admin.ui.users.details;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.malaab.ya.action.actionyamalaab.admin.R;
import com.malaab.ya.action.actionyamalaab.admin.data.network.model.Fine;

import java.io.Serializable;
import java.util.ArrayList;

public class FinesActivity extends AppCompatActivity implements Serializable {

    public ProgressBar pBar_loading;

    public SwipeRefreshLayout swrl_full;


    public RecyclerView rv_fines;


    ArrayList<Fine> fineList;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fines);
        pBar_loading=findViewById(R.id.pBar_loading);
        swrl_full=findViewById(R.id.swrl_full);

        rv_fines=findViewById(R.id.rv_individual);
        Bundle b = getIntent().getExtras();
        fineList=new ArrayList<>();
        fineList = (ArrayList<Fine>) b.getSerializable("list");
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_fines.setLayoutManager(llm);
        pBar_loading.setVisibility(View.GONE);

        FinesAdabter finesAdabter=new FinesAdabter(fineList,getApplicationContext());
        rv_fines.setAdapter(finesAdabter);


    }
}