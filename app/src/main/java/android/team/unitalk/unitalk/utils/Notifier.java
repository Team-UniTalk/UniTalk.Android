package android.team.unitalk.unitalk.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;
import android.team.unitalk.unitalk.R;
import android.widget.Toast;

/**
 * The utility class Notifier.
 * <p>
 * It creates simple Notifications for all kind of tasks.
 *
 * @author Muehlenstaedt
 */
public class Notifier {
    /**
     * the given context, from where the notification was send.
     */
    private Context context;
    /**
     * the title of the Notification.
     */
    private String title = "Unitalk";
    /**
     * The content of the notification or toast.
     */
    private String text = "Hallo Student!";
    /**
     * the icon_id, default is the app_icon id.
     */
    private int icon_id = R.drawable.ic_sms_black_24dp;

    /**
     * The default contructor.
     *
     * @param context the given context, from where the notification was send.
     */
    public Notifier(Context context) {
        this.context = context;
    }

    /**
     * Constructor with costum text
     *
     * @param context the given context.
     * @param text    costum text.
     */
    public Notifier(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    /**
     * Constructor with costum text and title.
     *
     * @param context given context.
     * @param title   costum title.
     * @param text    costum text.
     */
    public Notifier(Context context, String title, String text) {
        this.context = context;
        this.title = title;
        this.text = text;
    }

    /**
     * Constructor with costum text,icon and title.
     *
     * @param context given context.
     * @param title   given title.
     * @param text    given text.
     * @param icon_id given icon_id
     */
    public Notifier(Context context, String title, String text, int icon_id) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.icon_id = icon_id;
    }

    /**
     * Creates a simple Notification from the class variables.
     */
    public void simpleNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context);
        builder.setSmallIcon(icon_id);
        builder.setContentTitle(title);
        builder.setContentText(text);

        NotificationManager notificationManager =
                (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    /**
     * Creates a short Toast from the class variables.
     */
    public void simpleShortToast() {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Creates a long Toast from the class variables.
     */
    public void simpleLongToast() {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
