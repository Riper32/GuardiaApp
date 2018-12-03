package example.com.example.nagat.guardiaapp;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TerminarTurnoRequest extends StringRequest
{
    private static final String TERMINARTURNO_REQUEST_URL = "https://rigobertoperez.com/updateJornada.php";
    private Map<String,String> params;

    public TerminarTurnoRequest(String hora_salida, String fecha, int id_guardia, Response.Listener<String> listener)
    {
        super(Method.POST, TERMINARTURNO_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("hora_salida", hora_salida);
        params.put("fecha", fecha);
        params.put("id_guardia", id_guardia+"");
    }

    @Override
    public Map<String, String> getParams() {
        Log.d("POST BODY", "CALLED!!!" +  params);
        return params;
    }
}
