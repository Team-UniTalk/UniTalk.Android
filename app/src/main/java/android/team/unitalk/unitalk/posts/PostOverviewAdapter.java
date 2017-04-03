package android.team.unitalk.unitalk.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.team.unitalk.unitalk.R;
import android.team.unitalk.unitalk.utils.dataModel.Post;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * @author Schnittker
 */


public class PostOverviewAdapter extends RecyclerView.Adapter<PostViewHolder> {

    List<Post> list;
    Context context;

    /**
     * Constructs a new adapter
     *
     * @param list    the list of items to be shown in the layout
     * @param context the context in which the adapter is used
     */
    public PostOverviewAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_posts, parent, false);
        PostViewHolder holder = new PostViewHolder(context, v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = list.get(position);

        holder.creator.setText(post.getUsername());
        holder.content.setText(post.getContent());
        holder.likes.setText(post.getLikes() + "");
        holder.postId.setText(post.getPostId() + "");
        //holder.answerCounter.setText("" + countAnswers(position));
        //animate(holder);
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