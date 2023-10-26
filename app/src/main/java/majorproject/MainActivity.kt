package com.example.majorproject

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.PermissionChecker.checkSelfPermission

class MainActivity : AppCompatActivity() {

    private val ACCESS_LOCATION = 60
    private val MY_CALL_PHONE_MOM_PERMISSION_CODE = 51
    private val MY_CALL_PHONE_DAD_PERMISSION_CODE = 52
    private val MY_CALL_PHONE_POLICE_PERMISSION_CODE = 53
    private val MY_READ_CONTACTS_PERMISSION_CODE = 100
    private val MY_ACTION_OPEN_DOCUMENT_PERMISSION_CODE = 110
    private val MY_SMS_LOCATION_PERMISSION_CODE = 60
    private val MY_CAMERA_PERMISSION_CODE = 80
    private val MY_READ_EXTERNAL_STORAGE_PERMISSION_CODE = 90

    var button3: Button? = null
    private var locationManager: LocationManager? = null
    var ph_no: String? = null
    var imageUriMom: Uri? = null
    var imageUriDad: Uri? = null
    var imageUriPolice: Uri? = null
    var imageButtonClicked: String? = null




    lateinit var textView: TextView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button=findViewById(R.id.button)
        textView=findViewById(R.id.textView)

        @RequiresApi(Build.VERSION_CODES.S)
        fun getLocationUpdates() {
            Log.i("info", "inside getLocation")
            try {
                locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                //if (locationManager != null) {
                   // Log.i("info", "locationManager is not null")
                    locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER)
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5f, this)
                } //else {
                   // Log.i("info", "locationManager is null")
            catch (e: SecurityException) {
                e.printStackTrace()}
            }
            }
        }

        @RequiresApi(api= Build.VERSION_CODES.M)
        fun longClickAction(person: String) {
            //val sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE)
            //ph_no = sharedPreferences.getString(person, "Not Set")


            textView.setText("Hello there my friend")


            if(checkSelfPermission(Manifest.permission.LOCATION_HARDWARE)!= PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_LOCATION)


                Toast.makeText(this, "Application is initialized" , Toast.LENGTH_SHORT).show()
            }
            else{
                getLocationUpdates()
                Toast.makeText(this, "Application not enabled", Toast.LENGTH_SHORT).show()
            }

        }
 public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == MY_CALL_PHONE_MOM_PERMISSION_CODE) ||
                (requestCode == MY_CALL_PHONE_DAD_PERMISSION_CODE) ||
                (requestCode == MY_CALL_PHONE_POLICE_PERMISSION_CODE)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String buttonClicked = null;
                if (requestCode == MY_CALL_PHONE_MOM_PERMISSION_CODE) {
                    Log.i("click", "Call Mom Clicked");
                    buttonClicked = "numberMom";
                } else if (requestCode == MY_CALL_PHONE_DAD_PERMISSION_CODE) {
                    Log.i("click", "Call Dad Clicked");
                    buttonClicked = "numberDad";
                } else if (requestCode == MY_CALL_PHONE_POLICE_PERMISSION_CODE) {
                    Log.i("click", "Call Police Clicked");
                    buttonClicked = "numberPolice";
                } else {
                    Log.i("click", "unknown button clicked");
                }

                Toast.makeText(this, "Phone Permission Granted", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = getSharedPreferences("fileNameString", MODE_PRIVATE);
                String number = sharedPreferences.getString(buttonClicked, "Not Set");
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Phone Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_SMS_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS And Location Permission Granted", Toast.LENGTH_LONG).show();
                getLocationUpdates();
            } else {
                Toast.makeText(this, "SMS Or Location Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "I am taking photo");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (imageButtonClicked == "mom") {
                    startActivityForResult(cameraIntent, 70);
                } else if (imageButtonClicked == "dad") {
                    startActivityForResult(cameraIntent, 72);
                } else if (imageButtonClicked == "police") {
                    startActivityForResult(cameraIntent, 74);
                }
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == MY_READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "I am choosing photo");
                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                // pickPhoto.setType("*/*.jpg");

                if (imageButtonClicked.equals("mom")) {
                    Log.i("inside if mom", "i am inside");
                    startActivityForResult(pickPhoto, 71);
                } else if (imageButtonClicked.equals("dad")) {
                    Log.i("inside if dad", "i am inside");
                    startActivityForResult(pickPhoto, 73);
                } else if (imageButtonClicked.equals("police")) {
                    Log.i("inside if police", "i am inside");
                    startActivityForResult(pickPhoto, 75);
                }
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == MY_READ_CONTACTS_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Contacts Permission Granted", Toast.LENGTH_LONG).show();
                Log.i("tag", "I am choosing contact");
                //Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                //startActivityForResult(intent, 7);

                if (imageButtonClicked.equals("con1")) {
                    Log.i("inside if con1", "i am inside");
                    startActivityForResult(intent, 101);
                } else if (imageButtonClicked.equals("con2")) {
                    Log.i("inside if con2", "i am inside");
                    startActivityForResult(intent, 102);
                } else if (imageButtonClicked.equals("con3")) {
                    Log.i("inside if con3", "i am inside");
                    startActivityForResult(intent, 103);
                }
            } else {
                Toast.makeText(this, "Contact Permission Denied", Toast.LENGTH_LONG).show();
            }
        }