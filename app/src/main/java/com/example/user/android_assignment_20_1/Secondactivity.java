package com.example.user.android_assignment_20_1;
//Package objects contain version information about the implementation and specification of a Java package.
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Secondactivity extends AppCompatActivity {
    //public is a method and fields can be accessed by the members of any class.
    //class is a collections of objects.
    //created MainActivity and extends with AppCompatActivity which is Parent class.

    MyAdapter myAdapter;
    private ListView lstNames;
    private ArrayList<String> al_contactName,al_contactNumber;
    //declaring the values.

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    //giving permission to read upto 100 contacts.

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
        setContentView(R.layout.second_activity);
        //R means Resource
        //layout means design
        //main is the xml you have created under res->layout->main.xml
        //Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        //he other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        //the design

        al_contactName = new ArrayList<>();
        al_contactNumber = new ArrayList<>();
        showContacts();
        myAdapter = new MyAdapter();
        lstNames = (ListView) findViewById(R.id.lstNames);
        lstNames.setAdapter(myAdapter);
        //declaring the values
    }

    private void displayAllContacts(){
        ContentResolver contentResolver = getContentResolver();
        //supplies data from one application to others on request. Such requests are handled by the methods of
        //the ContentResolver.
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        //giving cursor to content resolver and finding the query of the uri and giving rest of null
        if(cursor.getCount()>0){
            //using if condition get count is gretaer than 0
            while (cursor.moveToNext()){
                //while condition move to the next
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                //giving string as id and coulumnindex as id
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //string as name and at column index as display name
                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                    //like wise the phone number
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                            new String[]{id},null);
                    //created new string and kept as null.
                    while (pCur.moveToNext()){
                        //using while condition cursor move to the next
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        al_contactName.add(name);
                        //adding the name
                        al_contactNumber.add(phoneNo);
                        //adding the phone number
                    }
                }

            }
        }
    }
    /**
     * Show the contacts in the ListView.
     */
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            displayAllContacts();

        }
    }

    @Override
    //we use override to tells the compiler that the following method overrides a method of its superclass.
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class MyAdapter extends BaseAdapter {

        @Override
        //we use override to tells the compiler that the following method overrides a method of its superclass.
        public int getCount() {
            return al_contactName.size();
        }
        //getting count id
        @Override
        //we use override to tells the compiler that the following method overrides a method of its superclass.
        public Object getItem(int position) {
            return null;
        }
        //getting item id
        @Override
        //we use override to tells the compiler that the following method overrides a method of its superclass.
        public long getItemId(int position) {
            return 0;
        }
        //getting position id
        @Override
        //we use override to tells the compiler that the following method overrides a method of its superclass.
        public View getView(int position, View convertView, ViewGroup parent) {
            //giving position and view
            ViewHolder holder;
            if(convertView == null){
                //giving view as null.
                convertView = getLayoutInflater().inflate(R.layout.name_cont,parent,false);
                //givng the inflater layout and root as false.
                holder = new ViewHolder();
                holder.bindView(convertView);
                //giving bind view to the holder
                convertView.setTag(holder);
                //setting tag

            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.contactName.setText(al_contactName.get(position));
            holder.contactNumber.setText(al_contactNumber.get(position));
            return convertView;
        }
    }
    public class ViewHolder{
        TextView contactName, contactNumber;
        //declaring the name and number.
        void bindView(View convertView){
            //binding the two items.
            contactName = (TextView)convertView.findViewById(R.id.contact_name);
            contactNumber = (TextView)convertView.findViewById(R.id.contact_number);
            //giving id to the name and contact to bind them together in view holder.
        }

    }
}