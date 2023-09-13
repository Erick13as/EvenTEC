package com.example.eventec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

public class Foro extends AppCompatActivity {

    private EditText questionEditText;
    private Button postButton;
    private ListView questionListView;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private List<Question> questionList;
    private QuestionAdapter questionAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);

        questionEditText = findViewById(R.id.questionEditText);
        postButton = findViewById(R.id.postButton);
        questionListView = findViewById(R.id.questionListView);

        databaseReference = FirebaseDatabase.getInstance().getReference("questions");
        //****auth = FirebaseAuth.getInstance();

        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(this, questionList);
        questionListView.setAdapter(questionAdapter);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postQuestion();
            }
        });

        loadQuestions();
    }

    private void postQuestion() {
        //********FirebaseUser user = auth.getCurrentUser();
        //
        Intent intent = getIntent();
        //String usuario = (String) intent.getSerializableExtra("user");
        user = (User) intent.getSerializableExtra("user");
        Button button = (Button) findViewById(R.id.btn_GenerarInforme);
        //String user= usuario;
        //String user= "Sofia";

        if (user != null) {
            String userId = user.getCarnet();//"id1234"; descomentar para usar sin login
            String userName = user.getNombre()+" "+user.getApellido();//"Sofia Vega"; descomentar para usar sin login
            String questionText = questionEditText.getText().toString().trim();

            if (!questionText.isEmpty()) {
                String questionId = databaseReference.push().getKey();

                Question question = new Question(questionId, userId, userName, questionText);

                databaseReference.child(questionId).setValue(question)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                questionEditText.setText("");
                                loadQuestions();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle the error
                            }
                        });
            }
        }
    }

    private void loadQuestions() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questionList.add(question);
                }
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
}
