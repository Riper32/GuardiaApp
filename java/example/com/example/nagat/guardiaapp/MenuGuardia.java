package example.com.example.nagat.guardiaapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuGuardia extends AppCompatActivity {

    TextView tvDatosAdmin;
    String nombreGuardia;
    String apellidoGuardia;
    int id_guardia;
    TextView tvLat;
    TextView tvLon;
    double latitud, longitud;
    String fecha;
    String horEntr;
    String horSal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_guardia);

        tvDatosAdmin = (TextView)findViewById(R.id.datosGuardia);
        tvLat = (TextView)findViewById(R.id.vLatitud);
        tvLon = (TextView)findViewById(R.id.vLong);
        Intent intent = getIntent();
        nombreGuardia = intent.getStringExtra("nombre");
        apellidoGuardia = intent.getStringExtra("apellido");
        id_guardia = Integer.parseInt(intent.getStringExtra("id_guardia"));

        tvDatosAdmin.setText(nombreGuardia+" "+apellidoGuardia);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        else
        {
            iniciarLoc();
        }
    }

    public void iniciarTurno(View view)
    {
        fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        horEntr = new SimpleDateFormat("HH:mm:ss").format(new Date());
        Log.d("DATE: ", "LA FECHA ACTUAL ES: "+fecha);
        Log.d("DATE: ", "LA HORA DE ENTRADA ES: "+horEntr);

        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean succsess = jsonResponse.getBoolean("success");

                    if(succsess)
                    {
                        /*Log.d("DATABASE", "Exito en el registro");
                        Intent newActivity = new Intent(AddGuardia.this, MenuAdmin.class);
                        newActivity.putExtra("nombre", nombreAdmin);
                        newActivity.putExtra("apellido", apellidoAdmin);
                        AddGuardia.this.startActivity(newActivity);*/
                        Log.d("DATABASE", "Exito en el registro");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuGuardia.this);
                        builder.setMessage("Inicio turno").setNegativeButton("Enterado", null).create().show();
                    }
                    else
                    {
                        Log.d("DATABASE", "Fallo en el registro");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuGuardia.this);
                        builder.setMessage("Error en el registro").setNegativeButton("Vuelva a intentr", null).create().show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        IniciarTurnoRequest iniciarTurnoRequest = new IniciarTurnoRequest(fecha, horEntr, latitud, longitud, id_guardia, respoListener);
        RequestQueue queue = Volley.newRequestQueue(MenuGuardia.this);
        queue.add(iniciarTurnoRequest);
    }

    public void terminarTurno(View view)
    {
        horSal = new SimpleDateFormat("HH:mm:ss").format(new Date());

        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean succsess = jsonResponse.getBoolean("success");

                    if(succsess)
                    {
                        Log.d("DATABASE", "Exito en el registro");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuGuardia.this);
                        builder.setMessage("Termino su turno").setNegativeButton("Enterado", null).create().show();
                    }
                    else
                    {
                        Log.d("DATABASE", "Fallo en el registro");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuGuardia.this);
                        builder.setMessage("Error en el registro").setNegativeButton("Vuelva a intentr", null).create().show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        TerminarTurnoRequest terminarTurnoRequest = new TerminarTurnoRequest(horSal, fecha, id_guardia, respoListener);

        RequestQueue queue = Volley.newRequestQueue(MenuGuardia.this);
        queue.add(terminarTurnoRequest);

    }

    public void iniciarLoc()
    {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);

        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);//Verificamos que los servicios del provedor de GPS esten activos
        // If GPS (location) is not enabled, User is sent to the settings to turn it on!
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }

        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarLoc();
                return;
            }
        }
    }

    public class Localizacion implements LocationListener
    {
        MenuGuardia mainActivity;//Clase en la que estamos trabajando arriba
        public MenuGuardia getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(MenuGuardia mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc)
        {
            loc.getLatitude();
            loc.getLongitude();
            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();

            Log.d("GPS: ", ""+Text);
            String myLatitude = "Lat = "+loc.getLatitude()+"";
            String myLongitude = "Long = "+loc.getLongitude()+"";
            tvLat.setText(myLatitude);
            tvLon.setText(myLongitude);
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            tvLat.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            tvLat.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
