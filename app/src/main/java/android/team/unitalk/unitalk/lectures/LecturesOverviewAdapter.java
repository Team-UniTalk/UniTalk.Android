package android.team.unitalk.unitalk.lectures;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.R;
import android.team.unitalk.unitalk.utils.dataModel.Lecture;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * Created by André Schnittker on 08.12.16.
 */

public class LecturesOverviewAdapter extends RecyclerView.Adapter<LectureViewHolder> {

    List<Lecture> list = Collections.emptyList();
    Context context;

    /**
     * Constructs a new adapter
     *
     * @param list    the list of items to be shown in the layout
     * @param context the context in which the adapter is used
     */
    public LecturesOverviewAdapter(List<Lecture> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public LectureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_lectures, parent, false);
        LectureViewHolder holder = new LectureViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(LectureViewHolder holder, int position) {
        Lecture lecture = list.get(position);
        holder.name.setText(lecture.getName());
        holder.code.setText(lecture.getCode() + "");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}