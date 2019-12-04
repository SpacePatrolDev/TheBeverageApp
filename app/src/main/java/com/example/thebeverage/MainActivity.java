package com.example.thebeverage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Declare array for AutoTextComplete
    private String[] province = {"Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador",
    "Northwest Territories", "Nova Scotia", "Nunavut", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"};

    private EditText name;
    private AutoCompleteTextView autoProvinceText;

    private RadioGroup flavoring_tea_radio_group;
    private RadioGroup flavoring_coffee_radio_group;

    private CheckBox milk_checkbx;
    private CheckBox sugar_checkbx;

    private Spinner cupsize_spinner;

    private String nameString;
    private String provinceString;
    private double TotalCost = 0.00;
    private String beverageString = "Tea";
    private String addons = "";
    private String cupSize = "Small";
    private String addedFlavour = "no flavouring";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.user_name);

        //Assign adapter to province AutoCompleteTextView
        autoProvinceText = findViewById(R.id.province_autocomplete);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, province);
        autoProvinceText.setThreshold(2); //to make it start working from 2nd letter
        autoProvinceText.setAdapter(adapter);

        flavoring_tea_radio_group = findViewById(R.id.flavoring_tea_radio);
        flavoring_coffee_radio_group = findViewById(R.id.flavoring_coffee_radio);

        milk_checkbx = findViewById(R.id.milk_check);
        sugar_checkbx = findViewById(R.id.sugar_check);

        RadioGroup beverage_radio_group = findViewById(R.id.beverage_radio);
        beverage_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.tea_radio)
                {
                    flavoring_tea_radio_group.setVisibility(View.VISIBLE);
                    flavoring_coffee_radio_group.setVisibility(View.GONE);
                    RadioButton tea_radio = findViewById(checkedId);
                    beverageString = tea_radio.getText().toString();
                }
                else
                {
                    flavoring_tea_radio_group.setVisibility(View.GONE);
                    flavoring_coffee_radio_group.setVisibility(View.VISIBLE);
                    RadioButton coffee_radio = findViewById(checkedId);
                    beverageString = coffee_radio.getText().toString();
                }
            }
        });

        cupsize_spinner = findViewById(R.id.size_dropdown);

        Button calculate_btn = findViewById(R.id.calc_btn);
        calculate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provinceString = autoProvinceText.getText().toString();
                nameString = name.getText().toString();
                cupSize = cupsize_spinner.getSelectedItem().toString();
                TotalCost = TotalCost + getCost(cupSize);
                getAddedFlavoring();

                if(nameString.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else if(provinceString.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please Enter Province", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    makeAToast();
                    TotalCost = 0.00;
                }
            }
        });
    }

    public void makeAToast()
    {
        if(milk_checkbx.isChecked() && sugar_checkbx.isChecked())
        {
            addons = "with Milk & Sugar";
            TotalCost = TotalCost + 2.25;
        }
        else if(milk_checkbx.isChecked())
        {
            addons = "with Milk";
            TotalCost = TotalCost + 1.25;
        }
        else if(sugar_checkbx.isChecked())
        {
            addons = "with Sugar";
            TotalCost = TotalCost + 1.00;
        }
        TotalCost = TotalCost + (TotalCost * 0.13);
        Toast.makeText(MainActivity.this,
                "For "+nameString+" from "+provinceString+", a "+cupSize+" "+beverageString+" "+addons+", "+addedFlavour+", Cost: $"+String.format(Locale.CANADA,"%.2f",TotalCost),
                Toast.LENGTH_LONG).show();
    }

    public void getAddedFlavoring()
    {
        if(beverageString.equals("Tea"))
        {
            RadioGroup tea_flavour = findViewById(R.id.flavoring_tea_radio);
            tea_flavour.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.none_tea_radio)
                    {
                        addedFlavour = "no flavouring";
                    }
                    else if(checkedId == R.id.lemon_radio)
                    {
                        addedFlavour = "with Lemon";
                        TotalCost = TotalCost + 0.25;
                    }
                    else
                    {
                        addedFlavour = "with Mint";
                        TotalCost = TotalCost + 0.50;
                    }
                }
            });
        }
        else
        {
            RadioGroup coffee_flavour = findViewById(R.id.flavoring_coffee_radio);
            coffee_flavour.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.none_coffee_radio)
                    {
                        addedFlavour = "no flavouring";
                    }
                    else if(checkedId == R.id.chocolate_radio)
                    {
                        addedFlavour = "with Chocolate";
                        TotalCost = TotalCost + 0.75;
                    }
                    else
                    {
                        addedFlavour = "with Vanilla";
                        TotalCost = TotalCost + 0.25;
                    }
                }
            });
        }
    }

    public double getCost(String size)
    {
        double cost = 0.00;

        if(size.equals("Small"))
        {
            cost = 1.50;
        }
        else if(size.equals("Medium"))
        {
            cost = 2.50;
        }
        else
        {
            cost = 3.25;
        }

        return cost;
    }
}
