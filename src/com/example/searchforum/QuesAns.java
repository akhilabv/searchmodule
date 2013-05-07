package com.example.searchforum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class QuesAns extends Activity {
	private ListView lv;
	private EditText etxt;
	private Button btn;
	private HttpClient httpc = new DefaultHttpClient();
	private HttpPost httppost = new HttpPost("http://nammaapp.in/namma7817/scripts7817/QuesAnsFetch.php");
	private HttpResponse resp = null;
	private BufferedReader br=null;
	private ArrayList<String> array_list; 
	ArrayAdapter<String> arrayAdapter;
	         
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ques_ans);
		lv = (ListView) findViewById(R.id.listView1);
		etxt = (EditText) findViewById(R.id.editText1);
		btn = (Button) findViewById(R.id.button1);
		String question=getIntent().getStringExtra("question").toString();
		array_list = new ArrayList<String>();
		arrayAdapter =      
		         new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, array_list);
		try
		{
			search(question);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(),
				    e.toString()+"  ol", Toast.LENGTH_SHORT).show();
		}
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(array_list.get(0).toString().contentEquals("NO ANSWERS POSTED") || array_list.get(0).toString().contentEquals("QUESTION NOT POSTED") )
				array_list.clear();
				array_list.add(etxt.getText().toString());
				lv.setAdapter(arrayAdapter);
				
			}
		});
	}
	@SuppressLint("NewApi")
	public void search(String query)
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		try
		{

		httppost.setHeader("Connection", "keep-alive");
		httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		//query = String.format("key=%s", URLEncoder.encode(str1, "UTF-8"));
		String query1 = String.format("query=%s", URLEncoder.encode(query.toString(), "UTF-8"));
		httppost.setEntity(new StringEntity(query1.toString()));
		resp = httpc.execute(httppost);
		br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"UTF-8"));
		String htmltext = "";
		String str = "";
			while ((str = br.readLine()) != null) { 
							htmltext +=str;
			}	
			
			
			//final ArrayList<String> array_list = new ArrayList<String>();
			if(htmltext.contentEquals("no answer found"))
				array_list.add("NO ANSWERS POSTED");
			else if(htmltext.contentEquals("question not found"))
				array_list.add("QUESTION NOT POSTED");
			else
			{
				StringTokenizer st = new StringTokenizer(htmltext, "$$");
				while(st.hasMoreTokens())
				{
					array_list.add(st.nextToken());
				}
			}
		       //  ArrayAdapter<String> arrayAdapter =      
		         //new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, array_list);
		         lv.setAdapter(arrayAdapter);
		         
				
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(),
				    e.toString()+"  mn", Toast.LENGTH_SHORT).show();
		}
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ques_ans, menu);
		return true;
	}

}
