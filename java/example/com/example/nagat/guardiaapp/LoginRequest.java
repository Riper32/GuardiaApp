package example.com.example.nagat.guardiaapp;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest
{
    private static final String LOGINGUARDIA_REQUEST_URL = "https://rigobertoperez.com/loginGuardia.php";
    private Map<String,String> params;

    public LoginRequest(int id_Guardia, String password, Response.Listener<String> listener)
    {
        super(Method.POST, LOGINGUARDIA_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("id_guardia", id_Guardia+"");
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        Log.d("POST BODY", "CALLED!!!" +  params);
        return params;
    }
}
