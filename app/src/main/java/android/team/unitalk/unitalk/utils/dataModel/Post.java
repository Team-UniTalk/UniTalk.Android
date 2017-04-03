package android.team.unitalk.unitalk.utils.dataModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The final Datamodel: Post
 *
 * @author Mühlenstädt
 */
public class Post {
    /**
     * the lecture where the post belongs to.
     */
    public int lectureId;
    /**
     * the userid of the creator.
     */
    public int userId;
    /**
     * the username of the creator.
     */
    public String username;
    /**
     * the post id of the post.
     */
    public int postId;
    /**
     * the actual content.
     */
    public String content;
    /**
     * like count.
     */
    public int likes;

    /**
     * Constructor which uses a json object to create a post.
     *
     * @param jsonObject the given JSONObject.
     */
    public Post(JSONObject jsonObject) {
        try {
            this.postId = jsonObject.getInt("postId");
            this.lectureId = jsonObject.getInt("lectureId");
            this.userId = jsonObject.getInt("userId");
            this.username = jsonObject.getString("username");
            this.content = jsonObject.getString("content");
            this.likes = jsonObject.getInt("likes");
        } catch (JSONException e) {
            System.err.println("[Post.class]" + e);
        }
    }

    /**
     * Getter for the username.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the content.
     *
     * @return the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for the like count.
     *
     * @return the number of likes.
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Getter for the post id.
     *
     * @return the post id.
     */
    public int getPostId() {
        return postId;
    }
}
