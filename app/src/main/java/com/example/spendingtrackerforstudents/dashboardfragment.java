package com.example.spendingtrackerforstudents;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spendingtrackerforstudents.model.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboardfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboardfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboardfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dashboardfragment newInstance(String param1, String param2) {
        dashboardfragment fragment = new dashboardfragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //floating button

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floation buttion textview...

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //bolean

    private boolean isOpen;

    //animation..
    private Animation FadeOpen,FadeClose;

    //firebase

    private FirebaseAuth mAuth;
    private  DatabaseReference mIncomeDatabase;
    private FirebaseRecyclerAdapter adapter;



    private DatabaseReference mExpenceDatabase;
    //Dashboard income and expense result..

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    //Recycler view

    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_dashboardfragment, container, false);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();



        mIncomeDatabase=FirebaseDatabase.getInstance().getReference().child("Income data").child(uid);
        mExpenceDatabase=FirebaseDatabase.getInstance().getReference().child("Expence data").child(uid);


        mIncomeDatabase.keepSynced(true);
        mExpenceDatabase.keepSynced(true);




        //Connect floationg button to layout

        fab_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_Ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_Ft_btn);

        //connecting floating text...

        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);


        //animation connect....

        FadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);



        //Total income and expense result set..

        totalIncomeResult=myview.findViewById(R.id.income_set_result);
        totalExpenseResult=myview.findViewById(R.id.expense_set_result);


        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adddata();
                if (isOpen) {

                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);


                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen = false;
                } else

                    fab_income_btn.startAnimation(FadeClose);
                fab_expense_btn.startAnimation(FadeClose);
                fab_income_btn.setClickable(true);
                fab_expense_btn.setClickable(true);


                fab_income_txt.startAnimation(FadeClose);
                fab_expense_txt.startAnimation(FadeClose);
                fab_income_txt.setClickable(true);
                fab_expense_txt.setClickable(true);
                isOpen = true;







            }
        });




        //Calculate total income..

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                int totalsum = 0;

                for (DataSnapshot mysnap:dataSnapshot.getChildren()){

                    model model=mysnap.getValue(model.class);

                    totalsum+=model.getAmount();

                    String stResult=String.valueOf(totalsum);

                    totalIncomeResult.setText(stResult+".00");

                }

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

        //Calculate total expense..

        mExpenceDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totalsum = 0;

                for (DataSnapshot mysnapshot:dataSnapshot.getChildren()){

                    model model=mysnapshot.getValue(model.class);
                    totalsum+=model.getAmount();

                    String strTotalSum=String.valueOf(totalsum);

                    totalExpenseResult.setText(strTotalSum+".00");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        //Recycler

        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.recycler_epense);



        //Recycler

        LinearLayoutManager layoutManagerIncome=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setReverseLayout(true);
        layoutManagerIncome.setStackFromEnd(true);

        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);



        LinearLayoutManager layoutManagerExpense=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);




        return myview;
    }

        //floating button animation
    private void ftannimation(){
        if (isOpen) {

            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);


            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen = false;
        } else

            fab_income_btn.startAnimation(FadeClose);
        fab_expense_btn.startAnimation(FadeClose);
        fab_income_btn.setClickable(true);
        fab_expense_btn.setClickable(true);


        fab_income_txt.startAnimation(FadeClose);
        fab_expense_txt.startAnimation(FadeClose);
        fab_income_txt.setClickable(true);
        fab_expense_txt.setClickable(true);
        isOpen = true;
    }
    private void adddata(){
        //fab btn income

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                incomeDataInsert();

            }
        });


        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenceDatainsert();

            }
        });
    }

    public void incomeDataInsert(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myviewm=inflater.inflate(R.layout.custom_layout_for_instert_data,null);
        mydialog.setView(myviewm);
        final AlertDialog dialog=mydialog.create();

        final EditText edtamount=myviewm.findViewById(R.id.amount_edt);
        final EditText edttype=myviewm.findViewById(R.id.type_edt);
        final EditText edtnote=myviewm.findViewById(R.id.note_edt);

        Button btnsave=myviewm.findViewById(R.id.btnsave);
        Button btncancel=myviewm.findViewById(R.id.btncancel);


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String amount=edtamount.getText().toString().trim();

                String type=edttype.getText().toString().trim();
                String note=edtnote.getText().toString().trim();



                int ouramountint=Integer.parseInt(amount);

                if (TextUtils.isEmpty(amount)){
                    edtamount.setError("Field Required..");
                    return;
                }


                if (TextUtils.isEmpty(type)){
                    edttype.setError("Field Required..");
                    return;
                }
                if (TextUtils.isEmpty(note)){
                    edtnote.setError("Field Required..");
                    return;
                }


                String id=mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                model model =new model(ouramountint,type,note,id,mDate);
                mIncomeDatabase.child(id).setValue(model);
                Toast.makeText(getActivity(),"DATA ADDED",Toast.LENGTH_SHORT).show();
                ftannimation();
                dialog.dismiss();

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }

    public void expenceDatainsert(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview =inflater.inflate(R.layout.custom_layout_for_instert_data,null);
        mydialog.setView(myview);

        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);

        final EditText ammount=myview.findViewById(R.id.amount_edt);
        final EditText type=myview.findViewById(R.id.type_edt);
        final EditText note=myview.findViewById(R.id.note_edt);


        final Button btnsave=myview.findViewById(R.id.btnsave);
        Button btncancel=myview.findViewById(R.id.btncancel);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String tmAmmount=ammount.getText().toString().trim();
                String tmtype=type.getText().toString().trim();
                String tmnote=note.getText().toString().trim();

                if (TextUtils.isEmpty(tmAmmount)){
                    ammount.setError("Required field...");
                    return;
                }
                int inamount=Integer.parseInt(tmAmmount);
                if (TextUtils.isEmpty(tmtype)){
                    type.setError("Required field...");
                    return;
                }
                if (TextUtils.isEmpty(tmnote)){
                    note.setError("Required field...");
                    return;
                }

                String id=mExpenceDatabase.push().getKey();
                String mDate=DateFormat.getDateInstance().format(new Date());
                model model =new model(inamount,tmtype,tmnote,id,mDate);
                mExpenceDatabase.child(id).setValue(model);
                Toast.makeText(getActivity(),"DATA ADDED",Toast.LENGTH_SHORT).show();

                ftannimation();
                dialog.dismiss();

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftannimation();
                dialog.dismiss();
            }
        });
        dialog.show();

    }






    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<model> options = new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(mIncomeDatabase,model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<model,IncomeViewHolder>(options) {

            public IncomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new IncomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income, parent, false));
            }

            protected void onBindViewHolder(IncomeViewHolder holder, int position, @NonNull model model) {
                holder.setIncomeAmmount(model.getAmount());
                holder.setIncomeType(model.getType());
                holder.setIncomeDate(model.getDate());
            }
        };
        mRecyclerIncome.setAdapter(adapter);
        adapter.startListening();


        options = new FirebaseRecyclerOptions.Builder<model>()
                .setQuery(mExpenceDatabase, model.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<model,ExpenseViewHolder>(options) {

            public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ExpenseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboart_expense, parent, false));
            }

            protected void onBindViewHolder(ExpenseViewHolder holder, int position, @NonNull model model) {
                holder.setExpenseAmmount(model.getAmount());
                holder.setExpenseType(model.getType());
                holder.setExpenseDate(model.getDate());
            }
        };
        mRecyclerExpense.setAdapter(adapter);
        adapter.startListening();









    }
}



//For Income Data

class IncomeViewHolder extends RecyclerView.ViewHolder{

    View mIncomeView;

    public IncomeViewHolder(View itemView) {
        super(itemView);
        mIncomeView=itemView;
    }

    public void setIncomeType(String type){

        TextView mtype=mIncomeView.findViewById(R.id.type_Income_ds);
        mtype.setText(type);

    }

    public void setIncomeAmmount(int ammount){

        TextView mAmmount=mIncomeView.findViewById(R.id.ammoun_income_ds);
        String strAmmount=String.valueOf(ammount);
        mAmmount.setText(strAmmount);
    }

    public void setIncomeDate(String date){
        TextView mDate=mIncomeView.findViewById(R.id.date_income_ds);
        mDate.setText(date);

    }

}
//For expense data..

 class ExpenseViewHolder extends RecyclerView.ViewHolder{

    View mExpenseView;

    public ExpenseViewHolder(View itemView) {
        super(itemView);
        mExpenseView=itemView;
    }

    public void setExpenseType(String type){
        TextView mtype=mExpenseView.findViewById(R.id.type_expense_ds);
        mtype.setText(type);
    }

    public void setExpenseAmmount(int ammount){
        TextView mAmmount = mExpenseView.findViewById(R.id.ammoun_expense_ds);
        String strAmmount=String.valueOf(ammount);
        mAmmount.setText(strAmmount);
    }

    public void setExpenseDate(String date){
        TextView mDate=mExpenseView.findViewById(R.id.date_expense_ds);
        mDate.setText(date);
    }

}











