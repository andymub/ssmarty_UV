package ssmarty.univ.notification;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import javax.annotation.Nullable;

public class NotifactionDataAdded {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void checkChange (String collectionPath, final Context context)
    {

        db.collection(collectionPath)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(context,"nouveau message",Toast.LENGTH_LONG).show();
                   // RemoteMessage remoteMessage ={Map<String d; s>}

                }
            }
        });
    }
}
