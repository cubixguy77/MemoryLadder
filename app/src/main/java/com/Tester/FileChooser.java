package com.Tester;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class FileChooser extends ListActivity { 

 private List<String> item = null;
 private List<String> path = null;
 private String root="/";
 private TextView myPath; 
 private Boolean getNumbers;
 private Button HelpButton;

    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filechooser);
        
        myPath = (TextView)findViewById(R.id.path);
        initButtons();
        getNumbers = getIntent().getBooleanExtra("getNumbers", false);
        
        getDir(root);
    }   
    
   /* 
    @Override
    public void onPause() {
    	super.onPause();
    	finish();
    }
    */
    
    public void initButtons() {
    	HelpButton = (Button) findViewById(R.id.HelpButton);
    	HelpButton.setOnClickListener(new OnClickListener() {   
	    	  @Override
	    	  public void onClick(View view) {
	    		  Intent i = new Intent(FileChooser.this, Help_LoadPegs.class);	
	    		  FileChooser.this.startActivity(i);
	    	  }});
    }
    
    public File[] getFiles(String dirPath) {
    	File f = new File(dirPath);
    	File[] files = f.listFiles(new FileFilter() {

		 @Override
		 public boolean accept(File pathname) {
		  if(pathname.isHidden())
			  return false;
		  if(!pathname.canRead())
			  return false;

		  //Show all directories in the list.
		  if(pathname.isDirectory())
			  return true;

		  //Check if there is a supported file type that we can read.
		  String fileName = pathname.getName();
		  String fileExtension;
		  int mid= fileName.lastIndexOf(".");
		  fileExtension = fileName.substring(mid+1,fileName.length());
		  if(fileExtension.equalsIgnoreCase("csv") || fileExtension.equalsIgnoreCase("txt")) {
		    	 return true;
		  }		   
		  
		 return false;
		}});
    	return files;
    }

    private void getDir(String dirPath) {
	     myPath.setText("Location: " + dirPath);    
	     item = new ArrayList<String>();
	     path = new ArrayList<String>();     	
	     
	     File[] files = getFiles(dirPath);
	
	     if(!dirPath.equals(root))  {
		      item.add(root);
		      path.add(root);    
		      item.add("../");
		      path.add(new File(dirPath).getParent());            
	     }     
	
	     for(int i=0; i < files.length; i++) {
	       File file = files[i];
	       path.add(file.getPath());
	       if(file.isDirectory())
	    	   item.add(file.getName() + "/");
	       else
	    	   item.add(file.getName());
	     }
	
	     ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.text, item);
	     setListAdapter(fileList);
    }


    public void finishActivity() {
    	if (getNumbers) {
    		Intent i = new Intent(this, ChoosePegs_Numbers.class);
		//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
    	}
    	else {
    		Intent i = new Intent(this, ChoosePegs_Cards.class);
		//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
    	}
    }
    

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
	  File file = new File(path.get(position));
	  if (file.isDirectory()) {
		  if(file.canRead())
			  getDir(path.get(position));
		  else {
		      new AlertDialog.Builder(this)
		      .setIcon(R.drawable.icon)
		      .setTitle("[" + file.getName() + "] folder can't be read!")
		      .setPositiveButton("OK", new DialogInterface.OnClickListener() {   
		       @Override
		       public void onClick(DialogInterface dialog, int which) {}
		      }).show();
		  }
	  }
	
	  else {
		  if (getNumbers) {
			  String result = FileOps.loadPegsFromFileNumbers(path.get(position), this);
			  if (result.equals("Success")) 
				  finishActivity();
			  
			  else {
				  new AlertDialog.Builder(this)
			      .setIcon(R.drawable.icon)
			      .setTitle("Error Interpreting Your File")
			      .setMessage(result + "\n" + "Each line should follow the format: \"74,car\"")
			      .setNegativeButton("Help", new DialogInterface.OnClickListener() {   
			    	  @Override
			    	  public void onClick(DialogInterface dialog, int which) {
			    		  Intent i = new Intent(FileChooser.this, Help_LoadPegs.class);	
			    		  FileChooser.this.startActivity(i);
			    	  }
			      })
			      .setPositiveButton("OK",   new DialogInterface.OnClickListener() {   
			    	  @Override
			    	  public void onClick(DialogInterface dialog, int which) {
			    		  finish();
			    	  }
			      })
			      .show();
			  }
			  
		  }
		  else {
			  
			  String result = FileOps.loadPegsFromFileCards(path.get(position), this);
			  if (result.equals("Success")) 
				  finishActivity();
			  
			  else {
				  new AlertDialog.Builder(this)
			      .setIcon(R.drawable.icon)
			      .setTitle("Error Interpreting Your File")
			      .setMessage(result + "\n" + "Each line should follow the format: \"h4,rain\"")
			      .setNegativeButton("Help", new DialogInterface.OnClickListener() {   
			    	  @Override
			    	  public void onClick(DialogInterface dialog, int which) {
			    		  Intent i = new Intent(FileChooser.this, Help_LoadPegs.class);	
			    		  FileChooser.this.startActivity(i);
			    	  }
			      })
			      .setPositiveButton("OK",   new DialogInterface.OnClickListener() {   
			    	  @Override
			    	  public void onClick(DialogInterface dialog, int which) {
			    		  finish();
			    	  }
			      })
			      .show();
			  }
			  
		  }
	  }
    }

}