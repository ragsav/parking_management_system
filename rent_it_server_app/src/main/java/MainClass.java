import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.messaging.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainClass {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, FirebaseMessagingException {
        System.out.println("Running server...");

        FileInputStream fileInputStream;
        fileInputStream = new FileInputStream("./seKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                .build();
        FirebaseApp.initializeApp(options);
        Firestore db = FirestoreClient.getFirestore();
//        remainderMessage_45_ending(db);
//        pass whichever function is needed
    }


    private static void removeAbsentUserPlot_30(@NotNull Firestore db)
            throws ExecutionException, InterruptedException, FirebaseMessagingException {
        System.out.println("Removing absent users ...");
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection("User")
                .whereEqualTo("current_time", Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                .whereEqualTo("entry", false).get();
        List<User> absentUsers = querySnapshotApiFuture.get().toObjects(User.class);
        System.out.println(absentUsers.size());

        List<String> token = new ArrayList<>();
        for (int i = 0; i < absentUsers.size(); i++) {

            User u = absentUsers.get(i);
            token.add(u.getMb_id());
            update_user(db, u.getUser_id());
            String time_interval = u.getCurrent_time() + "-"
                    + (u.getCurrent_time() + u.getCurrent_duration());
            String plot_id = u.getCurrent_plot_id();
            String mall_id = u.getCurrent_plot_id();

            ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = db.collection(mall_id).document(plot_id).get();
            Plot plot = documentSnapshotApiFuture.get().toObject(Plot.class);
            int w = plot.getOccupied_time().indexOf(time_interval);
            plot.getOccupied_time().remove(w);
            plot.getOccupied_user().remove(w);
            update_plot(db, plot_id, mall_id, plot.getOccupied_time(), plot.getOccupied_user());
        }
        message(token, "Your plot reservation has been cancelled due to late action");
    }

    private static void update_plot(@NotNull Firestore db, String plot_id
            , String mall_id, List<String> occupied_time, List<String> occupied_user) {
        db.collection(mall_id).document(plot_id).update("occupied_time", occupied_time
                , "occupied_user", occupied_user);
    }

    private static void update_user(@NotNull Firestore db, String user_id) {
        db.collection("User").document(user_id).update("has_taken", false
                , "current_mall_id", ""
                , "current_plot_id", ""
                , "current_time", 0
                , "current_duration", 0);
    }


    public static void remainderMessage_45_upcoming(@NotNull Firestore db)
            throws ExecutionException, InterruptedException, FirebaseMessagingException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection("User")
                .whereEqualTo("current_time", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1)
                .get();
        List<User> upcomingUsers = querySnapshotApiFuture.get().toObjects(User.class);
        List<String> tokens = new ArrayList<>();
        for (User user :
                upcomingUsers) {
            tokens.add(user.getMb_id());
        }
//        tokens.add("d6CHFWxCTGmn8g_S-AXLNP:APA91bHBkzGIBUGvmdokIazk2-7eyCa18LapONP_" +
//                "R9k810JXyJMzLMWpy9dVxBs1v9MoyT9uM_abLCKWwL_aABXstk6Ovu_FkRr_bFkbx4413eFzwmffTbtFhJYmmW7wiSujZ0_gJy7Q");
        message(tokens, "You have an upcoming plot reservation");
    }

    public static void remainderMessage_45_ending(@NotNull Firestore db)
            throws ExecutionException, InterruptedException, FirebaseMessagingException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = db.collection("User")
                .whereEqualTo("current_end_time", Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1)
                .get();
        List<User> upcomingUsers = querySnapshotApiFuture.get().toObjects(User.class);
        List<String> tokens = new ArrayList<>();
        for (User user :
                upcomingUsers) {
            tokens.add(user.getMb_id());
        }
//        tokens.add("d6CHFWxCTGmn8g_S-AXLNP:APA91bHBkzGIBUGvmdokIazk2-7eyCa18LapONP_" +
//                "R9k810JXyJMzLMWpy9dVxBs1v9MoyT9uM_abLCKWwL_aABXstk6Ovu_FkRr_bFkbx4413eFzwmffTbtFhJYmmW7wiSujZ0_gJy7Q");
        message(tokens, "Your reservation is about to end");
    }


    private static void message(List<String> tokens, String notification) throws FirebaseMessagingException {
        MulticastMessage message = MulticastMessage.builder()
                .putData("data", notification)
                .addAllTokens(tokens)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(tokens.get(i));
                }
            }

            System.out.println("List of tokens that caused failures: " + failedTokens);
        }
    }
}