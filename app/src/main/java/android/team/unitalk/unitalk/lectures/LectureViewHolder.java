package android.team.unitalk.unitalk.lectures;

import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.R;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Andre on 13.12.16.
 */

public class LectureViewHolder extends RecyclerView.ViewHolder {

    RecyclerView rv;
    TextView name;
    TextView code;

    LectureViewHolder(View itemView) {
        super(itemView);
        rv = (RecyclerView) itemView.findViewById(R.id.lectures_recyclerView);
        name = (TextView) itemView.findViewById(R.id.list_item_lectures_textview);
        code = (TextView) itemView.findViewById(R.id.list_item_lectures_textview_second);
    }
}