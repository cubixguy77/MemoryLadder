package com.memoryladder.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mastersofmemory.memoryladder.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LanguageSettingDialog extends Dialog implements OnClickListener {

	private Button CancelButton;
	private Button SaveButton;
	private Locale initValue;
	private Locale newValue;
	private List<Locale> locales;
	private List<String> languages;
	private TextToSpeech textToSpeech;

	private ValueChangeListener listener;

	public LanguageSettingDialog(Context context, Locale initValue) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.picker_language);

		this.initValue = initValue;
		this.newValue = initValue;

		initButtons();
		loadLocales();

		setCancelable(true);
	}

	public interface ValueChangeListener {
		void onValueChanged(Locale newValue);
	}

	public void setValueChangedListener(ValueChangeListener listener){
		this.listener = listener;
	}

	private void loadLocales() {
		locales = new ArrayList<>();
		languages = new ArrayList<>();

		textToSpeech = new TextToSpeech(getContext(), status -> {
			if (status == TextToSpeech.SUCCESS && textToSpeech != null) {
				if (Build.VERSION.SDK_INT >= 21) {
					getLocalesNew(textToSpeech);
				} else {
					getLocalesLegacy(textToSpeech);
				}
			}
			else {
				locales.add(Locale.UK);
				languages.add(Locale.UK.getDisplayName());
			}

			initNumberPicker();
		});
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void getLocalesNew(TextToSpeech textToSpeech)
	{
		Set<Locale> availableLocales = textToSpeech.getAvailableLanguages();

		locales = new ArrayList<>(availableLocales);
		Collections.sort(locales, (l1, l2) -> l1.getDisplayName().compareTo(l2.getDisplayName()));

		for (Locale locale : locales)
		{
			languages.add(locale.getDisplayName());
		}
	}

	private void getLocalesLegacy(TextToSpeech textToSpeech)
	{
		Locale[] allLocales = Locale.getAvailableLocales();
		for (Locale locale : allLocales)
		{
			try
			{
				int res = textToSpeech.isLanguageAvailable(locale);
				boolean hasVariant = (null != locale.getVariant() && locale.getVariant().length() > 0);
				boolean hasCountry = (null != locale.getCountry() && locale.getCountry().length() > 0);

				boolean isLocaleSupported =
						!hasVariant && !hasCountry && res == TextToSpeech.LANG_AVAILABLE ||
								!hasVariant && hasCountry && res == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
								res == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE;

				if (isLocaleSupported)
				{
					locales.add(locale);
					languages.add(locale.getDisplayName());
				}
			}
			catch (Exception ex)
			{
				Log.e("LANGUAGE_PICKER", "Error checking if language is available for TTS (locale=" + locale +"): " + ex.getClass().getSimpleName() + "-" + ex.getMessage());
			}
		}

		Collections.sort(locales, (l1, l2) -> l1.getDisplayName().compareTo(l2.getDisplayName()));
		Collections.sort(languages, String::compareTo);
	}

	private void initNumberPicker() {
		Spinner languagePicker = findViewById(R.id.languagePicker);
		ArrayAdapter aa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, languages);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		languagePicker.setAdapter(aa);
		languagePicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				newValue = locales.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});

		int initialSelection = locales.indexOf(initValue);
		new Handler().postDelayed(() -> languagePicker.setSelection(initialSelection), 10);
	}

	private void initButtons() {
		CancelButton = findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);

		SaveButton = findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == CancelButton) {
			dismiss();
		}
		else if (v == SaveButton) {
			if(listener != null && !initValue.equals(newValue)) {
				listener.onValueChanged(newValue);
			}

			dismiss();
		}
	}
}