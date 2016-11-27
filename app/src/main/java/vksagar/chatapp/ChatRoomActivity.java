package vksagar.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    private TextView txt_chat_conversation;
    private EditText edt_message;
    private Button btn_send;

    private String userName, roomName;
    private String tempKey;
    private DatabaseReference root;

    private String chatMsg, chatUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        txt_chat_conversation = (TextView) findViewById(R.id.txt_chat_conversation);
        edt_message = (EditText)findViewById(R.id.edt_message);
        btn_send = (Button)findViewById(R.id.btn_send);

        roomName = getIntent().getExtras().get("room_name").toString();
        userName = getIntent().getExtras().get("user_name").toString();
        setTitle("Room - "+roomName);

        root = FirebaseDatabase.getInstance().getReference().child(roomName);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<String, Object>();
                tempKey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference databaseReference = root.child(tempKey);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name",userName);
                map2.put("msg", edt_message.getText().toString());

                databaseReference.updateChildren(map2);

                edt_message.setText("");

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            chatMsg = (String) ((DataSnapshot)i.next()).getValue();
            chatUserName = (String) ((DataSnapshot)i.next()).getValue();

            txt_chat_conversation.append(chatUserName + " : " + chatMsg + "\n");

        }
    }
}
