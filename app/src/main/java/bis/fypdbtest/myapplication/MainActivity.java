package bis.fypdbtest.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
//Code adapted from https://www.youtube.com/watch?v=kDZYIhNkQoM and https://firebase.google.com/docs/firestore/manage-data/delete-data
public class MainActivity extends AppCompatActivity {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("testData").document("test");

    TextView mTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.fetchedTextView);
    }

    public void deleteData(View view) {
        mDocRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Employee", "Record Successfully Deleted.");
            }
        });
    }

    public void fetchData(View view) {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String nameText = documentSnapshot.getString("name");
                    String addressText = documentSnapshot.getString("address");
                    mTextView.setText("\"" + nameText + "\" ---" + addressText);
                }
                else {
                    mTextView.setText("Nothing here");
                }
            }
        });
    }

    public void saveDetails(View view) {
        EditText empNameView = (EditText) findViewById(R.id.editTextEmpName);
        EditText empAddressView = (EditText) findViewById(R.id.editTextAddress);
        String nameText = empNameView.getText().toString();
        String addressText = empAddressView.getText().toString();

        if(nameText.isEmpty()||addressText.isEmpty()) {
            Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT);
        }
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("name",nameText);
        dataToSave.put("address",addressText);
        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Employee", "Employee Saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Employee", "Error, not saved.");
            }
        });
    }
}