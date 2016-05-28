package com.ikabi.wakeup;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LogFile {
	private Context context=null;

	public  LogFile(Context c){
		context=c;
		String psensor=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			psensor = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Psensor/";  
	    }else{  
	    	psensor = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Psensor/";  
	    } 
		File dir = new File(psensor);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String gsensor=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			gsensor = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Gsensor/";  
	    }else{  
	    	gsensor = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Gsensor/";  
	    } 
		File dir1 = new File(gsensor);
		if (!dir1.exists()) {
			dir1.mkdir();
		}
		String esensor=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			esensor = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Esensor/";  
	    }else{  
	    	esensor = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Esensor/";  
	    } 
		File dir2 = new File(esensor);
		if (!dir2.exists()) {
			dir2.mkdir();
		}
		String led=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			led = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Led/";  
	    }else{  
	    	led = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Led/";  
	    } 
		File dir3 = new File(led);
		if (!dir3.exists()) {
			dir3.mkdir();
		}
	}
	public boolean pslog(String time,boolean b){
		String psensor=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			psensor = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Psensor/";  
	    }else{  
	    	psensor = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Psensor/";  
	    }
		psensor=psensor+time+"Psensor.txt";
		File f = new File(psensor);
		try {
			if (!f.exists()) {
				f.createNewFile();
			} 
		} catch (IOException e1) {
			return false;
		}
		if(b){
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.e("+"Psensor"+"Psensor fail"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try {
		    		pr.close();
			    }catch(IOException e) {
				    return false;
			    }
		     }
		}else{
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.d("+"Psensor"+","+"Psensor pass"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try{
		    		  pr.close();
			     }catch(IOException e) {
				     return false;
			     }
			}
		}
		return true;
	}
	public boolean gslog(String time,boolean b){
		String gsensor=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			gsensor = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Gsensor/";  
	    }else{  
	    	gsensor = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Gsensor/";  
	    } 
		gsensor=gsensor+time+"Gsensor.txt";
		File f = new File(gsensor);
		try {
			if (!f.exists()) {
				f.createNewFile();
			} 
		} catch (IOException e1) {
			return false;
		}
		if(b){
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.e("+"Gsensor"+","+"Gsensor fail"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try {
		    		pr.close();
			    }catch(IOException e) {
				    return false;
			    }
		     }
		}else{
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.d("+"Gsensor"+","+"Gsensor pass"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try{
		    		  pr.close();
			     }catch(IOException e) {
				     return false;
			     }
			}
		}
		return true;
	}
	public boolean eslog(String time,boolean b){
		String esensor=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			esensor = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Esensor/";  
	    }else{  
	    	esensor = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Esensor/";  
	    } 
		esensor=esensor+time+"ECompass.txt";
		File f = new File(esensor);
		try {
			if (!f.exists()) {
				f.createNewFile();
			} 
		} catch (IOException e1) {
			return false;
		}
		if(b){
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.e("+"ECompass"+","+"ECompass fail"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try {
		    		pr.close();
			    }catch(IOException e) {
				    return false;
			    }
		     }
		}else{
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.d("+"ECompass"+","+"ECompass pass"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try{
		    		  pr.close();
			     }catch(IOException e) {
				     return false;
			     }
			}
		}
		return true;
	}
	public boolean ledlog(String time,boolean b){
		String led=null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			led = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Sensor/"+"Led/";  
	    }else{  
	    	led = context.getFilesDir().getAbsolutePath()+ "/Sensor/"+"Led/";  
	    } 
		led=led+time+"Led.txt";
		File f = new File(led);
		try {
			if (!f.exists()) {
				f.createNewFile();
			} 
		} catch (IOException e1) {
			return false;
		}
		if(b){
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.e("+"Led"+","+"Led fail"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try {
		    		pr.close();
			    }catch(IOException e) {
				    return false;
			    }
		     }
		}else{
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            String date1 = format1.format(new Date(System.currentTimeMillis()));
		    FileWriter pr =null;
		    try {
		    	pr = new FileWriter(f,true);
			    String s=date1+" Log.d("+"Led"+","+"Led pass"+")"+ MainActivity.wakeupcount+" \n";
			    pr.write(s);
			} catch (FileNotFoundException e) {
				return false;
		    }catch (IOException e) {
			    return false;
		    }finally{
		    	try{
		    		  pr.close();
			     }catch(IOException e) {
				     return false;
			     }
			}
		}
		return true;
	}
    public String cfile(){
    	String time=null;
        String year, month, day, hour, minute, second;
        Calendar c = Calendar.getInstance();
		year = "" + c.get(Calendar.YEAR);
		if ((c.get(Calendar.MONTH) + 1) < 10) {
			month = "0" + (c.get(Calendar.MONTH) + 1);
		} else {
			month = "" + (c.get(Calendar.MONTH) + 1);
		}
		if (c.get(Calendar.DATE) < 10) {
			day = "0" + c.get(Calendar.DATE);
		} else {
			day = "" + c.get(Calendar.DATE);
		}
		if (c.get(Calendar.HOUR_OF_DAY) < 10) {
			hour = "0" + c.get(Calendar.HOUR_OF_DAY);
		} else {
			hour = "" + c.get(Calendar.HOUR_OF_DAY);
		}
		if (c.get(Calendar.MINUTE) < 10) {
			minute = "0" + c.get(Calendar.MINUTE);
		} else {
			minute = "" + c.get(Calendar.MINUTE);
		}
		if (c.get(Calendar.SECOND) < 10) {
			second = "0" + c.get(Calendar.SECOND);
		} else {
			second = "" + c.get(Calendar.SECOND);
		}
	    time=year+month+day+hour+minute+second;
    	return time;
    }
}
