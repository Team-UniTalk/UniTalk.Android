package android.team.unitalk.unitalk.answers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.R;
import android.view.View;
import android.widget.TextView;

public class AnswerViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView content;
    TextView creator;
    TextView likes;
    TextView answerId;
    TextView title;
    TextView titleCreator;

    AnswerViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view);
        content = (TextView) itemView.findViewById(R.id.content);
        creator = (TextView) itemView.findViewById(R.id.creator);
        likes = (TextView) itemView.findViewById(R.id.likeCounter);
        answerId = (TextView) itemView.findViewById(R.id.answerId);
        title = (TextView) itemView.findViewById(R.id.answerOverviewTitle);
        titleCreator = (TextView) itemView.findViewById(R.id.answerOverviewCreator);
    }
}