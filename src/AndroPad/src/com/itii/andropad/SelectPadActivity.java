/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SelectPadActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_pad);

		Bundle extra = getIntent().getExtras();
		m_context = extra.getString("context");

		m_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>());

		ListView list = (ListView) findViewById(R.id.list_select_pad);
		this.displayPads();
		list.setAdapter(m_adapter);
		list.setOnItemClickListener(this);

		Button btn = (Button) findViewById(R.id.btn_create_pad);
		btn.setOnClickListener(this);

		TextView text = (TextView) findViewById(R.id.txt_create_pad_name);

		if ("gaming".equals(m_context)) {
			btn.setVisibility(View.GONE);
			text.setVisibility(View.GONE);

		}

		list.requestFocus();

	}

	private void displayPads() {
		String path = getFilesDir().getAbsolutePath() + File.separator + "pads";
		File dirFile = new File(path);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		m_adapter.clear();
		m_fileList = dirFile.listFiles();
		for (File file : m_fileList) {
			String name = file.getName();
			m_adapter.add(name.substring(0, name.indexOf(".xml")));
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		this.selectPad(m_fileList[position].getName());
	}

	@Override
	public void onClick(View v) {
		TextView text = (TextView) findViewById(R.id.txt_create_pad_name);

		if (!text.getText().toString().isEmpty()) {
			this.selectPad(text.getText() + ".xml");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.displayPads();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void selectPad(String name) {

		Intent intent = null;
		if (m_context.equals("gaming")) {
			intent = new Intent(this, SelectDeviceActivity.class);
		} else if (m_context.equals("editing")) {
			intent = new Intent(this, EditingActivity.class);
		}
		intent.putExtra("selected_pad", name);
		startActivityForResult(intent, 0);
	}

	private ArrayAdapter<String> m_adapter;
	private File[] m_fileList;
	private String m_context;

}
