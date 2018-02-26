package com.example.user.android_assignment_20_1;
//Package objects contain version information about the implementation and specification of a Java package.
import android.Manifest;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //public is a method and fields can be accessed by the members of any class.
    //class is a collections of objects.
    //created MainActivity and extends with AppCompatActivity which implements the OnClickListner.

    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 100;
    ContentResolver contentResolver;
    Button buttonAdd, buttonViewAll;
    Intent intent;
    EditText ev_diag_contactName,ev_diag_contactNumber;
    String diag_contactName,diag_contactNumber;
    //declaring the values.

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    protected void onCreate(Bundle savedInstanceState) {
        //protected can be accessed by within the package and class and subclasses
        //The Void class is an uninstantiable placeholder class to hold a reference to the Class object
        //representing the Java keyword void.
        //The savedInstanceState is a reference to a Bundle object that is passed into the onCreate method of every Android Activity
        // Activities have the ability, under special circumstances, to restore themselves to a previous state using the data stored in this bundle.
        super.onCreate(savedInstanceState);
        //Android class works in same.You are extending the Activity class which have onCreate(Bundle bundle) method in which meaningful code is written
        //So to execute that code in our defined activity. You have to use super.onCreate(bundle)
        setContentView(R.layout.activity_main);
        //R means Resource
        //layout means design
        //main is the xml you have created under res->layout->main.xml
        //Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        //he other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        //the design

        buttonAdd = (Button) findViewById(R.id.buttonADD);
        buttonViewAll = (Button) findViewById(R.id.buttonViewAll);
        //giving id to the button from the layout file.
        buttonAdd.setOnClickListener(this);
        buttonViewAll.setOnClickListener(this);
        //setting the on click listner to the buttons
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onClick(View view) {
        switch(view.getId()){
            //giving the switch method whether it satisifies case 1 or case 2
            case R.id.buttonADD:
                //case 1 as buttonADD
                addItem();
                //adding item
                Toast.makeText(MainActivity.this,"buttonADD Clicked",Toast.LENGTH_LONG).show();
                //giving toast msg that button is clicked
                break;
            case R.id.buttonViewAll:
                intent = new Intent(MainActivity.this,Secondactivity.class);
                //giving intent so that the activity may switch from main to second activity.
                startActivity(intent);
                //starting the activity.
                Toast.makeText(MainActivity.this,"buttonViewAll Clicked",Toast.LENGTH_LONG).show();
                //giving toast msg that button is clicked
                break;
            default:
                break;
        }
    }

    public void addItem(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialog.setView(inflater.inflate(R.layout.contact, null))
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog diag = (Dialog) dialog;
                        ev_diag_contactName = (EditText) diag.findViewById(R.id.diag_contact_name);
                        ev_diag_contactNumber = (EditText) diag.findViewById(R.id.diag_phone_number);
                    //setting the dialog as save and getting contact name and phone number from the layout file
                        diag_contactName = ev_diag_contactName.getText().toString();
                        diag_contactNumber = ev_diag_contactNumber.getText().toString();
                    //getting text from the string name and number.
                        Log.e("addItem ",diag_contactName);
                        //adding the item
                        writeContacts();
                        //writing the contactts
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //created the cancel button
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private void writeContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_WRITE_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            //createContact(diag_contactName,diag_contactNumber);
            insertContact(diag_contactName, diag_contactNumber);
        }
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        //requesting the permission with the code.
        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                writeContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
                //giving a message that Until you grant the permission, we canot display the names.
            }
        }
    }

    public void insertContact(String firstName, String mobileNumber) {
        //inserting the contact with name and mobile number
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        //proving a array list
        int rawContactInsertIndex = ops.size();
        //giving the size of the contacts.

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, firstName)
                // Name of the person
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

        try {
            //giving try catch block.
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            //contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            //giving catch exception
            Toast.makeText(this, " we can't insert the names", Toast.LENGTH_SHORT).show();
            //giving a message that we cant insert the names
            Log.e("C",e.toString());
        }
        Toast.makeText(this, "Contact Added.", Toast.LENGTH_SHORT).show();
        //giving a message that the contact is added.
    }


}