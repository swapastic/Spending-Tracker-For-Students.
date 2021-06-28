package com.example.spendingtrackerforstudents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendingtrackerforstudents.model.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public  class incomefragment extends Fragment {

    //Firebase DB
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    //RecyclerView
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    //Text view

    private TextView incomeTotalSum;




    @Override
    public
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        /**if (getArguments() != null) {
         //mParam1 = getArguments().getString(ARG_PARAM1);
         //mParam2 = getArguments().getString(ARG_PARAM2);

         }*/


    }

    @Override
    public
    View onCreateView (
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )

    {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_incomefragment, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("Income data").child(uid);

        recyclerView = myview.findViewById(R.id.recview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        //income addition

        incomeTotalSum = myview.findViewById(R.id.income_txt_result);


        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totlatvalue = 0;

                for (DataSnapshot mysanapshot:dataSnapshot.getChildren()){

                    model data=mysanapshot.getValue(model.class);

                    totlatvalue+=data.getAmount();

                    String stTotalvale=String.valueOf(totlatvalue);

                    incomeTotalSum.setText(stTotalvale+".00");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return myview;
    }

    @Override
    public
    void onStart () {
        super.onStart();

        FirebaseRecyclerOptions<model> options = new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(mIncomeDatabase, model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<model, MyViewHolder>(options) {

            public
            MyViewHolder onCreateViewHolder ( ViewGroup parent, int viewType ) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false));
            }

            protected
            void onBindViewHolder ( MyViewHolder holder, int position, @NonNull model model ) {
                holder.setAmmount(model.getAmount());
                holder.setType(model.getType());
                holder.setNote(model.getNote());
                holder.setDate(model.getDate());
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }





    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public
        MyViewHolder ( @NonNull View itemView ) {
            super(itemView);
            mView = itemView;
        }

        void setType ( String type ) {
            TextView mType = mView.findViewById(R.id.TYPE_txt_income);
            mType.setText(type);
        }

        void setNote ( String note ) {

            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }

        void setDate ( String date ) {
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        void setAmmount ( int ammount ) {
            TextView mAmmount = mView.findViewById(R.id.ammount_txt_income);
            String stammount = String.valueOf(ammount);
            mAmmount.setText(stammount);
        }


    }

















