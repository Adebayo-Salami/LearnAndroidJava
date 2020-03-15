package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.VewHolder>{
    private final Context mContext;
    private final List<NoteInfo> mNotes;
    private final LayoutInflater mlayoutInflater;

    public NoteRecyclerAdapter(Context mContext, List<NoteInfo> mNotes) {
        this.mContext = mContext;
        mlayoutInflater = LayoutInflater.from(mContext);
        this.mNotes = mNotes;
    }

    @NonNull
    @Override
    public NoteRecyclerAdapter.VewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mlayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new NoteRecyclerAdapter.VewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VewHolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.mtextTitle.setText(note.getTitle());
        holder.mtextCourse.setText(note.getCourse().getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class VewHolder extends RecyclerView.ViewHolder{

        public final TextView mtextCourse;
        public final TextView mtextTitle;
        public int mCurrentPosition;

        public VewHolder(@NonNull View itemView) {
            super(itemView);
            mtextCourse = itemView.findViewById(R.id.text_course);
            mtextTitle = itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(MainActivity.NOTE_POSITION, mCurrentPosition);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
