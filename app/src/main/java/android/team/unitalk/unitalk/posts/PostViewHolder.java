package android.team.unitalk.unitalk.posts;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView content;
    TextView creator;
    TextView likes;
    TextView postId;
    TextView answerCounter;
    Button likeButton;
    Context context;

    PostViewHolder(Context context, View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.card_view);
        content = (TextView) itemView.findViewById(R.id.content);
        creator = (TextView) itemView.findViewById(R.id.creator);
        likes = (TextView) itemView.findViewById(R.id.likeCounter);
        postId = (TextView) itemView.findViewById(R.id.postId);
        answerCounter = (TextView) itemView.findViewById(R.id.answerCounter);
        likeButton = (Button) itemView.findViewById(R.id.likeButton);
        this.context = context;
    }
}