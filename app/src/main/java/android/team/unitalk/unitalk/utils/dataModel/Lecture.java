package android.team.unitalk.unitalk.utils.dataModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The final Datamodel: Lecture
 *
 * @author Mühlenstädt
 */
public class Lecture {
    /**
     * the id of an lecture.
     */
    public int id;
    /**
     * teh name of a lecture.
     */
    public String name;
    /**
     * the access code of the lecture.
     */
    public int code;


    /**
     * Constructor with creates a lecture with a JSONObject.
     *
     * @param lectureJson the given object.
     */
    public Lecture(JSONObject lectureJson) {
        try {
            this.name = lectureJson.getString("name");
            this.code = lectureJson.getInt("code");
        } catch (JSONException e) {
            System.err.println("[Lecture.class] " + e);
        }

    }

    /**
     * Getter for the lecture name.
     *
     * @return the lecture name.
     */
    public String getName() {
        return name;
    }

    /**
     * the code of a lecture.
     *
     * @return the code.
     */
    public int getCode() {
        return code;
    }
}
