package example.com.example.nagat.guardiaapp;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IniciarTurnoRequest extends StringRequest
{
    private static final String INICIARTURNO_REQUEST_URL = "https://rigobertoperez.com/insertJornada.php";
    private Map<String,String> params;

    public IniciarTurnoRequest(String fecha, String hora_entrada, double latitud, double longitud, int id_guardia, Response.Listener<String> listener)
    {
        super(Method.POST, INICIARTURNO_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("fecha", fecha);
        params.put("hora_entrada", hora_entrada);
        params.put("latitud", latitud+"");
        params.put("longitud", longitud+"");
        params.put("id_guardia", id_guardia+"");
    }

    @Override
    public Map<String, String> getParams() {
        Log.d("POST BODY", "CALLED!!!" +  params);
        return params;
    }
}
