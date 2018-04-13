package br.iesb.a1631088056.schallenge.helpers;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import br.iesb.a1631088056.schallenge.models.Usuario;

public class ManageUsuarioFirebaseDB {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRefUsuario;
    private static final String TAG = "ManageUsuarioFirebaseDB";

    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DataSnapshot mReturnedUsuario;

    public ManageUsuarioFirebaseDB () {

        // Setup Firebase Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRefUsuario = mFirebaseDatabase.getReference("Usuarios");
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference("usersAvatar");

        /*myRefUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mReturnedUsuario = ds;
                    Log.d(TAG, "Usuário gerenciado é: " + ds.getKey());
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Falha ao ler o valor no BD.", databaseError.toException());

            }
        });
*/
    }


    public boolean makeUsuario(Usuario usuario) {


        try {
            String id = myRefUsuario.push().getKey();


//            Usuario myUser = new Usuario(user.getUid(), user.getDisplayName(), user.getEmail());

            myRefUsuario.child(id).setValue(usuario);

            Log.i(TAG, "Usuario criado: "+ id);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }

        Log.i(TAG, "Usuário criado com sucesso.");
        return true;
    }


    public void storeAvatar(Uri uri, String myid) throws Exception{

        final String mId = myid;


        if (!uri.toString().isEmpty()){
            mStorageRef.child(myid)
                    .putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i(TAG, "Upload da imagem completo.");
                            Log.i(TAG, mStorageRef.child(mId).getDownloadUrl().toString());

                            try {
                                if (!TextUtils.isEmpty(myRefUsuario.child("avatarURL").getKey())) {
                                    myRefUsuario.child(mId).child("avatarURL").setValue(mStorageRef.child(mId).getDownloadUrl().getResult().toString());
                                }
                            } catch (Exception e) {
                                Log.e (TAG, e.getMessage());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    });

            return;
        } else {
            throw new Exception("Imagem com Url inválida!");
        }

    }

    public DataSnapshot getUsuarioFromFirebaseUid(FirebaseUser user) {

        DatabaseReference mref = FirebaseDatabase.getInstance()
                .getReference("Usuarios");

                mref.orderByChild("email")
                        .equalTo(user.getEmail())
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           mReturnedUsuario = ds;
                       }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return mReturnedUsuario;
    }

    public void updateUsuario(DataSnapshot usuario, String child, String value) {

        myRefUsuario.child(usuario.getKey())
                .child(child)
                .setValue(value);
    }


}
