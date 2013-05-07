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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class QuesSearch extends Activity {
	
	private Button srch;
	private EditText etxt;
	private ListView lv;
	private HttpClient httpc = new DefaultHttpClient();
	private HttpPost httppost = new HttpPost("http://nammaapp.in/namma7817/scripts7817/similar_text.php");
	private HttpResponse resp = null;
	private BufferedReader br=null;
	 Integer i=0;
	 Integer j=0;
	 Integer k=0;
	 Integer l=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ques_search);
		srch = (Button) findViewById(R.id.button1);
		etxt = (EditText) findViewById(R.id.editText1);
		lv=(ListView) findViewById(R.id.listView1);
				
		srch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
					  i=0;
					  j=0;
					  k=0;
					  l=0;
					
					if(etxt.getText().length()!=0)
					{
						lv.setVisibility(v.INVISIBLE);
						lv.setVisibility(v.VISIBLE);
						lv.setAdapter(null);
					search(etxt.getText().toString());
					
					}
					else
						lv.setVisibility(v.INVISIBLE);
				}
				catch(Exception e)
				{
					Toast.makeText(getApplicationContext(),
						    e.toString()+"  ol", Toast.LENGTH_SHORT).show();
				}
			}
		});
		 lv.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> listView, View view, 
			     int position, long id) {
				   Intent intent=new Intent(QuesSearch.this,QuesAns.class);
				   intent.putExtra("question",lv.getItemAtPosition(position).toString());
				   if(!(lv.getItemAtPosition(position).toString().contentEquals("NO MATCH FOUND")))
				   startActivity(intent);
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
		String query1 = String.format("query=%s", URLEncoder.encode(query, "UTF-8"));
		httppost.setEntity(new StringEntity(query1.toString()));
		resp = httpc.execute(httppost);
		br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent(),"UTF-8"));
		String htmltext = "";
		String str = "";
			while ((str = br.readLine()) != null) { 
							htmltext +=str;
			}	
			
			
		
			if(htmltext.contentEquals("not found"))
			{
				final ArrayList<String> array_list = new ArrayList<String>();
		         array_list.add("NO MATCH FOUND");
	         
		         ArrayAdapter<String> arrayAdapter =      
		         new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, array_list);
		         lv.setAdapter(arrayAdapter);
		         
			}
			else
			{
				StringTokenizer st = new StringTokenizer(htmltext, "@@");
				String[] s1= {null,null,null};
				//String[] strarray =null;
				final ArrayList<String> strarray = new ArrayList<String>();
				Integer x=0;
				while (st.hasMoreTokens())
				{
				s1[x]=st.nextToken();
				x++;
				}
				
				if(!(s1[0].contentEquals("!!!!!")))
				{
				StringTokenizer stt1 = new StringTokenizer(s1[0], "$$");
				while (stt1.hasMoreTokens())
				{
					strarray.add(stt1.nextToken());
					i++;
					l++;
				}
				}
				
				if(!(s1[1].contentEquals("|||||")))
				{
				StringTokenizer stt2 = new StringTokenizer(s1[1], "$$");
				
				while (stt2.hasMoreTokens())
				{
					//strarray[j]=stt1.nextToken();
					strarray.add(stt2.nextToken());
					j++;
					l++;
				}
				}
				
				if(s1[2].length()!=0)
				{
				StringTokenizer stt3 = new StringTokenizer(s1[2], "$$");
				while (stt3.hasMoreTokens())
				{
					//strarray[k]=stt1.nextToken();
					strarray.add(stt3.nextToken());
					k++;
					l++;
				}
				}
				ArrayAdapter<String> adapt =      
				         new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, strarray){
					@Override
					  public View getView(int position, View convertView, ViewGroup parent) {
						  View view = super.getView(position, convertView, parent); 
						  
						  if (position < i ) {
						      view.setBackgroundColor(Color.GREEN);  
						  } else if(position <= (i+j-1))
						  {
						      view.setBackgroundColor(Color.YELLOW);  
						  }
						  else
							  view.setBackgroundColor(Color.RED);
						  return view;
					    
					  }
				};
				      
				lv.setAdapter(adapt);
				
			}
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
		getMenuInflater().inflate(R.menu.ques_search, menu);
		return true;
	}

}