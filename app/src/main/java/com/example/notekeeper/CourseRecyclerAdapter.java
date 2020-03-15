package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.VewHolder>{
    private final Context mContext;
    private final List<CourseInfo> mCourses;
    private final LayoutInflater mlayoutInflater;

    public CourseRecyclerAdapter(Context mContext, List<CourseInfo> mCourses) {
        this.mContext = mContext;
        mlayoutInflater = LayoutInflater.from(mContext);
        this.mCourses = mCourses;
    }

    @NonNull
    @Override
    public CourseRecyclerAdapter.VewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mlayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new CourseRecyclerAdapter.VewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VewHolder holder, int position) {
        CourseInfo course = mCourses.get(position);
        holder.mtextTitle.setText(course.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class VewHolder extends RecyclerView.ViewHolder{

        public final TextView mtextTitle;
        public int mCurrentPosition;

        public VewHolder(@NonNull View itemView) {
            super(itemView);
            mtextTitle = itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mCourses.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
