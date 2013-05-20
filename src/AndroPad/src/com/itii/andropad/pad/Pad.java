package com.itii.andropad.pad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.itii.andropad.GamingActivity;
import com.itii.andropad.R;
import com.itii.andropad.bluetooth.ConnectThread;
import com.itii.andropadcommon.AndroButton;
import com.itii.andropad.pad.Pad.State;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class Pad implements Comparator<Button>{
	
	public enum Mode {PLAYING,EDITING};
	public enum State {CONNECTED, DISCONNECTED, ERROR};

	public Pad(Context context, String path, Mode mode)
	{
		m_context = context;
		m_path = path;
		m_name = m_path.substring(0, m_path.indexOf(".xml"));
		m_buttons = new ArrayList<Button>();
		m_mode = mode;
		m_state = State.DISCONNECTED;
		
		if(mode == Mode.PLAYING)
		{
			GamingActivity activity = (GamingActivity) context;
			m_connectThread = activity.getConnectThread();
		}
		
		
		m_svg = new SVG[13];
		m_svg[0] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_blue);
		m_svg[1] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_red);
		m_svg[2] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_yellow);
		m_svg[3] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_green);
		m_svg[4] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_left);
		m_svg[5] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_right);
		m_svg[6] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_down);
		m_svg[7] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_up);
		m_svg[8] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_grey);
		m_svg[9] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_left_grey);
		m_svg[10] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_right_grey);
		m_svg[11] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_down_grey);
		m_svg[12] = SVGParser.getSVGFromResource(m_context.getResources(),R.raw.btn_up_grey);
		
		
		this.initialize();
	}
	
	private void initialize()
	{
		try {
			String path = m_context.getFilesDir().getAbsolutePath() + File.separator + "pads" + File.separator;
			File file = new File(path+m_path);
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileInputStream in = new FileInputStream(file);
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            this.readPad(parser);
            in.close();
        } catch (IOException e) {
			Log.e("Pad", "Unable to read pad: "+m_path);
		} catch (XmlPullParserException e) {
			Log.e("Pad", "Unable to read pad: "+m_path);
		}
	}
	
	private void readPad(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, null, "pad");
		
		while (parser.next() != XmlPullParser.END_DOCUMENT) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }

	        if (parser.getName().equals("button")) {
	        	Log.d("New Button", "New Button");
	        	int posX = Integer.valueOf(parser.getAttributeValue(null, "posX"));
	        	int posY = Integer.valueOf(parser.getAttributeValue(null, "posY"));
	        	int size = Integer.valueOf(parser.getAttributeValue(null, "size"));
	        	int type = Integer.valueOf(parser.getAttributeValue(null, "type"));
	        	int mapping = Integer.valueOf(parser.getAttributeValue(null, "mapping"));
	        	m_buttons.add(new Button(this, posX, posY, type, size, mapping));
	        	
	        }
	    }  
		
	}
	
	public void draw(Canvas canvas)
	{
		Paint paint = new Paint();
		int size = m_context.getResources().getDimensionPixelSize(R.dimen.size_pad_info);
		paint.setTextSize(size);
		canvas.drawText(m_name, 2, size, paint);
		this.drawState(canvas, paint, size);
		for(Button button: m_buttons)
		{
			button.draw(canvas, m_context);
		}
	}
	
	public void drawState(Canvas canvas, Paint paint, int size)
	{
		if(m_mode == Mode.PLAYING)
		{
			String state = "Erreur !";
			switch(m_state)
			{
			case CONNECTED:
				state = "Connected";
				break;
				
			case DISCONNECTED:
				state = "Disconnected";
				break;
			default:
				break;
			}
			paint.setColor(Color.RED);
			canvas.drawText(state, 2, size*2+1, paint);
		}
	}
	
	public void pressButton(int pointerIndex, float posX, float posY)
	{
		boolean vibrate = false;
		for(Button button: m_buttons)
		{
			vibrate = vibrate || button.pressButton(pointerIndex, posX, posY);
		}
		if(m_mode == Mode.EDITING)
		{
			Collections.sort(m_buttons, this);
		}
		else if(vibrate)
		{
			Vibrator v = (Vibrator) m_context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(30);
		}
	}
	
	public void releaseButton(int pointerIndex, float posX, float posY)
	{
		for(Button button: m_buttons)
		{
			button.releaseButton(pointerIndex, posX, posY);
		}
	}
	
	public void moveFinger(int pointerIndex, float posX, float posY) {
		boolean vibrate = false;
		for(Button button: m_buttons)
		{
			vibrate = vibrate || button.moveFinger(pointerIndex, posX, posY);
		}
		if(vibrate)
		{
			Vibrator v = (Vibrator) m_context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(30);
		}
	}
	
	public void resetButtonStates() {
		for(Button button: m_buttons)
		{
			button.resetState();
		}
	}
	
	public void dragging(float posX, float posY) {
		for(Button button: m_buttons)
		{
			if(button.dragging((int)posX, (int)posY))
				break;
		}
	}
	
	@Override
	public int compare(Button lhs, Button rhs) {
		if(lhs.isPressed() && !rhs.isPressed())
			return 1;
		else if(!lhs.isPressed() && rhs.isPressed())
			return -1;
		else
			return 0;
	}
	
	public boolean hasSelectedButton()
	{
		return !m_buttons.isEmpty() && m_buttons.get(m_buttons.size()-1).isPressed();
	}
	
	public void save()
	{
		try {
			String path = m_context.getFilesDir().getAbsolutePath() + File.separator + "pads" + File.separator;
			FileOutputStream fos = new FileOutputStream(path+m_path);
			fos.write(this.toString().getBytes());
			fos.close();
			Log.d("Pad saved", toString());
		} catch (FileNotFoundException e) {
			Log.e("Pad", "Unable to write pad: "+m_path);
		} catch (IOException e) {
			Log.e("Pad", "Unable to write pad: "+m_path);
		}
	}
	
	public Button getSelectedButton()
	{
		return m_buttons.get(m_buttons.size()-1);
	}
	
	
	public SVG getSVG(int index)
	{
		return m_svg[index];
	}
	
	public void createButton(float posX, float posY) {
		m_buttons.add(new Button(this,(int)posX,(int)posY,0,1,0));
	}
	
	public void deleteSelectedButton() {
		m_buttons.remove(m_buttons.size()-1);
	}
	
	@Override
	public String toString() {
		String res = "<pad>\n";
		for(Button b:m_buttons)
		{
			res += b.toString();
		}
		res += "</pad>";
		return res;
	}
	
	public void delete() {
		String path = m_context.getFilesDir().getAbsolutePath() + File.separator + "pads" + File.separator;
		File file = new File(path+m_path);
		if(file.exists())
		{
			file.delete();
		}
	}
	
	public void setState(State state) {
		m_state = state;
	}
	
	public void sendButtonState(AndroButton androButton) {
		
		if(m_connectThread != null && m_connectThread.getSocket().isConnected()) {
			   try {
				   m_connectThread.sendObject(androButton);
					} catch (IOException e) {
						m_connectThread.disconnect();
						Toast.makeText(m_context.getApplicationContext(),"Déconnexion",Toast.LENGTH_LONG).show();
					}
        } else {
     	   this.setState(State.DISCONNECTED);
        }
	}
	
	public boolean isConnected() {
		return m_mode == Mode.EDITING || m_state == State.CONNECTED;
	}
	
	private Context m_context;
	private String m_path;
	private String m_name;
	private ArrayList<Button> m_buttons;
	private Mode m_mode;
	private State m_state;
	private SVG[] m_svg;
	private ConnectThread m_connectThread;
	
	
	
	
	
	
	
	
	
}
