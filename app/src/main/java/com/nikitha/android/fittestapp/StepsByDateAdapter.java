package com.nikitha.android.fittestapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StepsByDateAdapter extends RecyclerView.Adapter {
    String TAG=StepsByDateAdapter.class.getSimpleName();
    Context context;
    List<DatenStepsPojo> resultList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    static boolean asc=true;
    DateClickListener mClickListener;


    public StepsByDateAdapter(Context context, List<DatenStepsPojo> resultList, DateClickListener mClickListener) {
        this.resultList=resultList;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        View v;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.v=itemView;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listitem, parent, false);
        return new CustomViewHolder(view);
    }

    interface DateClickListener{
        void onDateClick(List<DatenStepsPojo> resultList);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View currentView = holder.itemView;
        DatenStepsPojo currentItemAtPos = resultList.get(position);

        String date = currentItemAtPos.getDate();
        String steps = currentItemAtPos.getSteps();
        TextView dateColumnTextView = (TextView) currentView.findViewById(R.id.date);
        TextView stepsColumnTextView = (TextView) currentView.findViewById(R.id.steps);

        if(currentItemAtPos.getType()==TYPE_HEADER){
            dateColumnTextView.setText(context.getString(R.string.date));
            stepsColumnTextView.setText(context.getString(R.string.steps));
            setHeader(currentView, dateColumnTextView, stepsColumnTextView);

            dateColumnTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onDateClick(resultList);
                }
            });
        }
        else {
            dateColumnTextView.setText(date);
            stepsColumnTextView.setText(steps);
        }
    }

    private void setHeader(View currentView, TextView dateColumnTextView, TextView stepsColumnTextView) {
        int padding = (int) context.getResources().getDimension(R.dimen.margin_no);
        dateColumnTextView.setPadding(padding,padding,padding,padding);
        dateColumnTextView.setBackgroundColor(context.getResources().getColor(R.color.colorAccentDark));
        stepsColumnTextView.setBackgroundColor(context.getResources().getColor(R.color.colorAccentDark));

        // date height to wrap content to accommodate complete header
        changeViewHeight(currentView, dateColumnTextView);
        changeViewHeight(currentView, stepsColumnTextView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dateColumnTextView.setTextAppearance(R.style.headerStyle);
            stepsColumnTextView.setTextAppearance(R.style.headerStyle);
            Drawable drawable_up = context.getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_18dp);
            Drawable drawable_down = context.getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_18dp);

            dateColumnTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable_up,null,drawable_down);
        }
    }

    private void changeViewHeight(View currentView, TextView textView) {
        ViewGroup.LayoutParams params = textView.getLayoutParams();
        params.height = (int) currentView.getResources().getDimension(R.dimen.heightListItem_header);// ViewGroup.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void setData(List<DatenStepsPojo> newResultList) {
//        resultList.clear();
//        resultList.addAll(newResultList);
        resultList=newResultList;
        notifyDataSetChanged();
    }

    public List<DatenStepsPojo> SortAscOrDes( List<DatenStepsPojo>  data){
        if(asc){ // ordered in ascending order so convert to descending
            Collections.reverse(data);
            asc=false;
        }
        else{// ordered in descending order so convert to ascending
            Log.i(TAG,"I");
            Collections.sort(data);
            asc=true;
        }
        return data;
    }
}
