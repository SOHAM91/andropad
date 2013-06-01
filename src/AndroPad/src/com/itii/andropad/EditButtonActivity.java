/**
 * AndroPad Project - ITII CNAM Alsace - Juin 2013
 * Fabrice Latterner - Clement Troesch
 */

package com.itii.andropad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.itii.andropadcommon.AndroButton;

public class EditButtonActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_button);

		Bundle extra = getIntent().getExtras();

		m_createMode = "create".equals(extra.getString("mode"));
		int typePosition = extra.getInt("type");
		int sizePosition = extra.getInt("size");
		int mappingPosition = extra.getInt("mapping");

		if (m_createMode) {
			this.setTitle(R.string.title_activity_create_button);

			Button create = (Button) findViewById(R.id.btn_apply);
			create.setText(R.string.action_create);

			Button cancel = (Button) findViewById(R.id.btn_cancel);
			cancel.setText(R.string.action_cancel);
		}

		Spinner spinner;

		// Types
		spinner = (Spinner) findViewById(R.id.spinner_button_type);
		m_typeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				com.itii.andropad.pad.Button.s_BUTTON_TYPES);
		spinner.setAdapter(m_typeAdapter);
		spinner.setSelection(typePosition);

		// Sizes
		spinner = (Spinner) findViewById(R.id.spinner_button_size);
		m_sizeAdapter = new ArrayAdapter<Float>(this,
				android.R.layout.simple_spinner_dropdown_item,
				com.itii.andropad.pad.Button.s_BUTTON_SIZES);
		spinner.setAdapter(m_sizeAdapter);
		spinner.setSelection(sizePosition);

		// Mapping
		spinner = (Spinner) findViewById(R.id.spinner_button_mapping);
		String[] mappings = new String[AndroButton.Mapping.values().length];
		int i = 0;
		for (AndroButton.Mapping m : AndroButton.Mapping.values()) {
			mappings[i++] = m.name();
		}

		m_mappingAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, mappings);
		spinner.setAdapter(m_mappingAdapter);
		spinner.setSelection(mappingPosition);

		android.widget.Button btn = (android.widget.Button) findViewById(R.id.btn_apply);
		btn.setOnClickListener(this);
		btn = (android.widget.Button) findViewById(R.id.btn_cancel);
		btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v == findViewById(R.id.btn_apply)) {
			Spinner type = (Spinner) findViewById(R.id.spinner_button_type);
			Spinner size = (Spinner) findViewById(R.id.spinner_button_size);
			Spinner mapping = (Spinner) findViewById(R.id.spinner_button_mapping);

			Intent intent = new Intent();
			intent.putExtra("action", "edit");
			intent.putExtra("type", (int) type.getSelectedItemId());
			intent.putExtra("size", (int) size.getSelectedItemId());
			intent.putExtra("mapping", (int) mapping.getSelectedItemId());
			setResult(RESULT_OK, intent);
			finish();
		}
		if (v == findViewById(R.id.btn_cancel)) {
			Intent intent = new Intent();
			intent.putExtra("action", "delete");
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	private ArrayAdapter<String> m_typeAdapter;
	private ArrayAdapter<Float> m_sizeAdapter;
	private ArrayAdapter<String> m_mappingAdapter;
	private boolean m_createMode;

}
