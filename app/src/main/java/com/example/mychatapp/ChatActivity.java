package com.example.mychatapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter adapter;
    private List<Message> messages;
    private String receiverId; // Alıcının kullanıcı ID'si

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages);

        recyclerViewMessages.setAdapter(adapter);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));

        // Mesaj gönderme butonunun click listener'ı
        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                editTextMessage.setText("");
            }
        });

        // Alıcı ID'sini alın
        receiverId = "alıcı_kullanıcı_id";
        receiveMessages(receiverId);
    }

    private void sendMessage(String messageText) {
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatId = generateChatId(senderId, receiverId);

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        String messageId = messagesRef.child(chatId).push().getKey();
        Message message = new Message(messageText, senderId, System.currentTimeMillis());
        messagesRef.child(chatId).child(messageId).setValue(message);
    }

    private void receiveMessages
            (String receiverId) {
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String chatId = generateChatId(senderId, receiverId);

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(chatId);
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    messages.add(message);
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerViewMessages.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // Mesaj değiştiğinde yapılacak işlemler
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Mesaj silindiğinde yapılacak işlemler
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Mesaj taşındığında yapılacak işlemler
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Dinleme iptal edildiğinde yapılacak işlemler
            }
        });
    }

    private String generateChatId(String senderId, String receiverId) {
        // İki kullanıcı ID'si arasında benzersiz bir sohbet ID'si oluşturur
        return senderId.compareTo(receiverId) > 0 ? senderId + "_" + receiverId : receiverId + "_" + senderId;
    }
}
