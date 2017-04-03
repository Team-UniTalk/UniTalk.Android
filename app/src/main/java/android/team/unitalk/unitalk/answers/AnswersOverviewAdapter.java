package android.team.unitalk.unitalk.answers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.R;
import android.team.unitalk.unitalk.utils.dataModel.Answer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;


/**
 * Created by Schnittker on 20.11.2016.
 */

public class AnswersOverviewAdapter extends RecyclerView.Adapter<AnswerViewHolder> {

    List<Answer> list = Collections.emptyList();
    Context context;

    public AnswersOverviewAdapter(List<Answer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_answers, parent, false);
        AnswerViewHolder holder = new AnswerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        Answer answer = list.get(position);

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.creator.setText(answer.getUsername());
        holder.content.setText(answer.getContent());
        holder.answerId.setText(answer.getId() + "");
        //holder.likes.setText("" + list.get(position).getLikes());
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
