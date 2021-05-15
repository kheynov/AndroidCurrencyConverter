package com.example.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ChooseCurrency extends AppCompatActivity {
    ProgressBar progressBar;
    TextView updatingText;
    ListView currenciesList;
    NodeList nodeList;
    public static final String APP_PREFERENCES = "currency_preferences";
    public static final String NOMINAL_PREFERENCE = "currency_nominal_preference";
    public static final String VALUE_PREFERENCE = "currency_value_preference";
    public static final String NAME_PREFERENCE = "currency_name_preference";
    SharedPreferences sharedPreferences;
    ArrayList<CurrencyListItem> listItems = new ArrayList<>();

    String URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_currency);

        updatingText = findViewById(R.id.updatingText);
        progressBar = findViewById(R.id.progressBar);
        currenciesList = findViewById(R.id.currency_list);
        new DownloadXML().execute(URL);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    private class DownloadXML extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            updatingText.setVisibility(TextView.VISIBLE);
            currenciesList.setVisibility(ListView.INVISIBLE);
            updatingText.setText("Загружаем список валют");
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                java.net.URL url = new URL(strings[0]);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.parse(new InputSource(url.openStream()));
//                doc.getDocumentElement().normalize();
                Log.d("INFO", "document loaded");
                Log.d("DOC", doc.toString());
                nodeList = doc.getElementsByTagName("Valute");
                Log.d("NODELIST LENGTH", Integer.toString(nodeList.getLength()));
            } catch (Exception e) {
                Log.e("ERROR", e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listItems.clear();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Log.d("DOC ELEMENT", element.toString());
                    listItems.add(new CurrencyListItem(getNode("Name", element),
                            getNode("CharCode", element),
                            Integer.parseInt(getNode("Nominal", element)),
                            Double.parseDouble(getNode("Value", element).replace(',', '.'))));
                    Log.i("GET LIST OF ITEMS", listItems.toString());
                    CurrencyListAdapter listAdapter = new CurrencyListAdapter(getApplicationContext(), listItems);
                    currenciesList.setAdapter(listAdapter);
                    currenciesList.setOnItemClickListener((parent, view, position, id) -> {
                        final CurrencyListItem listItem = listItems.get(position);
                        saveCurrencyPreferences(listItem.getNominal(), listItem.getValue(), listItem.getShortName());
                        Log.d("INFO", "Currency Preferences saved");
                        finish();
                    });
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    updatingText.setVisibility(TextView.INVISIBLE);
                    currenciesList.setVisibility(ListView.VISIBLE);
                }
            }
        }

        private void saveCurrencyPreferences(int nominal, double value, String shortName) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(NOMINAL_PREFERENCE, nominal);
            editor.putString(VALUE_PREFERENCE, Double.toString(value));
            editor.putString(NAME_PREFERENCE, shortName);
            editor.apply();
        }

        private String getNode(String tag, Element element) {
            NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
            Node value = nodeList.item(0);
            return value.getNodeValue();
        }
    }
}
