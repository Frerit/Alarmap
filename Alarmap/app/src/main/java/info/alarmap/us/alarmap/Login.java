package info.alarmap.us.alarmap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Login extends BaseHelper {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Users");

    // Login user y password
    String TAG = "----- MSS:: // ";
    EditText userEmail,userPass;
    String valiEmail, valiPass;

    Map<String,Object> useData = new HashMap<String, Object>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_login);
        userEmail = (EditText) findViewById(R.id.user_email);
        userPass = (EditText) findViewById(R.id.user_password);

        udpateUsers();
    }

    public void udpateUsers() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    useData.put("name",user.getDisplayName());
                    useData.put("id",user.getUid());
                    useData.put("email",user.getEmail());
                    ref.child(user.getUid()).setValue(useData);
                    startActivity(new Intent(Login.this, Main.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }


    private boolean validateForm(String email,String pass) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            userEmail.setError("Faltan ingresar el correo Electronico");
            valid = false;  } else  {
            userEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            userPass.setError("Faltan ingresar la contraseña");
            valid = false;  } else  {
            userPass.setError(null);
        }

        return valid;
    }

    public void ProcessLogin(final View view) {
        valiEmail = userEmail.getText().toString().trim();
        valiPass = userPass.getText().toString().trim();

        if (!validateForm(valiEmail,valiPass)) {
            return;
        }

        showProgressDialog("Estamos Validando los datos");
        auth.signInWithEmailAndPassword(valiEmail,valiPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Log.d("----TMM ","Succes: " + user);
                            udpateUsers();
                            startActivity(new Intent(Login.this, Main.class));
                        } else {
                            Log.w("---//Error: ", "signInWithEmail:failure", task.getException());
                            Snackbar snackbar = Snackbar.make(view,"No Se pudo iniciar session",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                        if (!task.isSuccessful()) {
                            Snackbar snackbar = Snackbar.make(view,"Correo o Contraseña incorrectos",Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        hideProgressDialog();
                    }
                });
    }
}
