package edu.fullsail.mgems.cse.treasurehunter.christopherwest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Item> itemList = new ArrayList<>();
    private DrawSurface ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnCredits).setOnClickListener(this);
        findViewById(R.id.btnInventory).setOnClickListener(this);
        itemList = loadItems();
        ds = findViewById(R.id.dsField);
        ds.setItems(itemList);
    }

    private ArrayList<Item> loadItems() {
        InputStream input = getResources().openRawResource(R.raw.items);
        BufferedReader reader = null;
        ArrayList<Item> items = new ArrayList<>();
        String line = null;

        try {
            reader = new BufferedReader(new InputStreamReader(input));
            while ((line = reader.readLine()) != null){
                Item item = new Item(line);
                items.add(item);
            }
        } catch ( Exception e ) {
            Log.e("MainActivity", "Reading list of Items failed!", e);
            Log.wtf("MainActivity", "Error reading list of Items on line " + line, e);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error closing file reader.", e);
            }
        }

        return items;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if (v.getId() == R.id.btnCredits) {
            dialog.setTitle("Made by");
            dialog.setMessage("Christopher West\nMBG521-O | Computer Science for Engineers\n02/07/2020");
            dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        } else {

            dialog.setTitle("Inventory");
            dialog.setMessage(displayItemsWithCounts(ds.getInventory()));
            dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    private String displayItemsWithCounts(ArrayList<Item> inventory) {
        StringBuilder stringBuilder = new StringBuilder();
        if (inventory.size() > 0) {
            ArrayList<String> itemNames = new ArrayList<>();
            for (Item inventoryItem : inventory) {
                itemNames.add(inventoryItem.name);
            }
            Set<String> uniqueNames = new HashSet<>(itemNames);
            for (String itemName : uniqueNames) {
                if (itemName.equals("Gold")) {
                    stringBuilder.insert(0, Collections.frequency(itemNames, itemName) + "g\n\n");
                } else {
                    stringBuilder.append(itemName + " (x" + Collections.frequency(itemNames, itemName) + ")\n");
                }
            }
            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        } else {
            stringBuilder.append("Inventory Empty");
        }
        return stringBuilder.toString();
    }
}
