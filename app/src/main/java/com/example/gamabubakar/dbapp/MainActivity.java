package com.example.gamabubakar.dbapp;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<AdapterItems> list=new ArrayList<AdapterItems>();
    mycustomAdapter myadapter;
DatabaseHelper dbhelper;
EditText et1,et2,et3;
ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbhelper=new DatabaseHelper(this);
        et1=findViewById(R.id.ename);
        et2=findViewById(R.id.emarks);
        et3=findViewById(R.id.id);
        lv=findViewById(R.id.listView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //////////////////////////////////////////add data/////////////////////////////////////////////////////////////////////////////////
    public void adddata(View view) {
        boolean check=dbhelper.insertdata(et1.getText().toString(),Integer.parseInt(et2.getText().toString()));
        if(check==true)
            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_SHORT).show();
            else
            Toast.makeText(MainActivity.this,"Data Not Inserted",Toast.LENGTH_SHORT).show();
    }

    /////////////////////////////////////////display data//////////////////////////////////////////////////////////////////////////////////
    public void display(View view) {
        list.clear();
        Cursor res=dbhelper.getalldata();
        if(res.getCount()==0){
            showdata("Error","No Data");

            //return;
        }
        //StringBuffer sb=new StringBuffer();
        while(res.moveToNext()){
            //sb.append("id: "+res.getString(0)+"\n");
            //sb.append("name: "+res.getString(1)+"\n");
            //sb.append("marks: "+res.getString(2)+"\n");
            //sb.append("==============================="+"\n");
            list.add(new AdapterItems(res.getString(0),res.getString(1),res.getString(2)));
        }
        myadapter=new mycustomAdapter(list);
        lv.setAdapter(myadapter);
        //showdata("Data",sb.toString());
    }
    public void showdata(String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void updated(View view) {
        int result=dbhelper.updated(et3.getText().toString(),et1.getText().toString(),Integer.parseInt(et2.getText().toString()));
        if(result>0)
            Toast.makeText(MainActivity.this,"Data Updated",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this,"Data Not Updated",Toast.LENGTH_SHORT).show();

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Deleted(View view) {
        int rowcount=dbhelper.deletedata(et3.getText().toString());
        if(rowcount>0)
            Toast.makeText(MainActivity.this,"Data Deleted",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this,"Data Not Deleted",Toast.LENGTH_SHORT).show();

    }

    public void Searched(View view) {
        list.clear();
        Cursor res=dbhelper.searchdata(et1.getText().toString());
        if(res.getCount()==0){
            showdata("Error","No Data");
            //return;
        }
        //StringBuffer sb=new StringBuffer();
        while(res.moveToNext()){
            //sb.append("id: "+res.getString(0)+"\n");
            //sb.append("name: "+res.getString(1)+"\n");
            //sb.append("marks: "+res.getString(2)+"\n");
            //sb.append("==============================="+"\n");
            list.add(new AdapterItems(res.getString(0),res.getString(1),res.getString(2)));
        }
        myadapter=new mycustomAdapter(list);
        lv.setAdapter(myadapter);
        //showdata("Data",sb.toString());

    }

    class mycustomAdapter extends BaseAdapter{
        ArrayList<AdapterItems> list=new ArrayList<AdapterItems>();
        mycustomAdapter(ArrayList<AdapterItems> list){
            this.list=list;
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View view1=inflater.inflate(R.layout.listitems,null);
            TextView id=view1.findViewById(R.id.id);
            TextView name=view1.findViewById(R.id.name);
            TextView marks=view1.findViewById(R.id.marks);
            AdapterItems s=list.get(position);
            id.setText(s.id);
            name.setText(s.name);
            marks.setText(s.marks);
            return view1;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////search/menu item


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        SearchView sv= (SearchView) menu.findItem(R.id.searchview).getActionView();
        SearchManager sm= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv.setSearchableInfo(sm.getSearchableInfo(getComponentName()));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                list.clear();
                Cursor res=dbhelper.searchdata(newText);
                if(res.getCount()==0){
                    showdata("Error","No Data");
                }
                while(res.moveToNext()){
                    list.add(new AdapterItems(res.getString(0),res.getString(1),res.getString(2)));
                }
                myadapter=new mycustomAdapter(list);
                lv.setAdapter(myadapter);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //on arrow press
        int id=item.getItemId();
        if(id==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
