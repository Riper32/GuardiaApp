package example.com.example.nagat.guardiaapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loguin(View view)
    {
        EditText user = (EditText) findViewById(R.id.usuario);
        EditText password = (EditText) findViewById(R.id.password);

        final int userInt = Integer.parseInt(user.getText().toString());
        final String id = ""+userInt;
        String passStr = password.getText().toString();

        Log.d("DATOS", "SEND: "+userInt+" "+passStr);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean succsess = jsonResponse.getBoolean("success");
                    Log.d("JSON response: ", ""+response);
                    if(succsess)
                    {
                        String nombre = jsonResponse.getString("nombre");
                        String apellido = jsonResponse.getString("apellido");

                        Intent newActivity = new Intent(Login.this, MenuGuardia.class);
                        newActivity.putExtra("id_guardia",id);
                        newActivity.putExtra("nombre", nombre);
                        newActivity.putExtra("apellido", apellido);
                        Login.this.startActivity(newActivity);
                    }
                    else
                    {
                        Log.d("LOGIN", "Fallo al validar");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage("Fallo al validar").setNegativeButton("Vuelva a intentr", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        trustEveryone();

        LoginRequest loginRequest = new LoginRequest(userInt, passStr, responseListener);

        RequestQueue queue = Volley.newRequestQueue(Login.this);
        try{
            Log.d("POST REQUEST", ""+ loginRequest.getHeaders() );
        }catch (Exception e){}
        queue.add(loginRequest);
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

}
