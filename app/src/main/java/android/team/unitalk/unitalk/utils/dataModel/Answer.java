package android.team.unitalk.unitalk.utils.dataModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The final Datamodel: Answer
 *
 * @author Mühlenstädt
 */
public class Answer {
    /**
     * the id of an answer.
     */
    public int id;
    /**
     * the post, tha answer belongs to.
     */
    public int postId;
    /**
     * the id of the creator.
     */
    public int userId;
    /**
     * the username of the creator.
     */
    public String username;
    /**
     * the content of the answer.
     */
    public String content;

    /**
     * Constructor which uses a json object to create an answer.
     *
     * @param jsonObject the given JSONObject.
     */
    public Answer(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("id");
            this.postId = jsonObject.getInt("postId");
            this.userId = jsonObject.getInt("userId");
            this.content = jsonObject.getString("content");
            this.username = jsonObject.getString("username");
        } catch (JSONException e) {
            System.err.println("[Post.class]" + e);
        }
    }

    /**
     * Getter for content.
     *
     * @return return the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for username.
     *
     * @return the username of the creator.
     */
    public String getUsername() {
        return username;
    }

    /**
     * the id of an answer.
     *
     * @return the answer id.
     */
    public int getId() {
        return id;
    }
}
