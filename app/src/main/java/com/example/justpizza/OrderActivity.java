package com.example.justpizza;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderActivity extends BaseActivity implements View.OnClickListener {

    TextView quantityValueTextView, totalValueTextView, pizzaNameTextView, pizzaIngredientsTextView;
    Button addToCartButton, addOneButton, removeOneButton;
    RadioButton pizzaWithAdditivesButton;
    ImageButton backButton, favouriteButton;
    CheckBox mushroomsAdditiveCheckBox, cornAdditiveCheckBox, picklesAdditiveCheckbox;
    LinearLayout additivesGroup;

    int quantity, pizzaId;
    double pizzaPrice;
    String pizzaName, pizzaIngredients;

    MyDataBaseHelper dbHelper;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Context context = getApplicationContext();

        dbHelper = new MyDataBaseHelper(this);

        pizzaId = 0;
        pizzaPrice = 0;
        pizzaName = "text_chosen_pizza";
        pizzaIngredients = "@string/description_chosen_pizza";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pizzaId = extras.getInt("id");
            pizzaName = extras.getString("name");
            pizzaPrice = extras.getDouble("price");
            pizzaIngredients = extras.getString("ingredients");
        }

        pizzaNameTextView = findViewById(R.id.text_chosen_pizza);
        pizzaNameTextView.setText(pizzaName);
        pizzaIngredientsTextView = findViewById(R.id.description_chosen_pizza);
        pizzaIngredientsTextView.setText("Съставки: " + pizzaIngredients);


        quantityValueTextView = findViewById(R.id.edit_tv_quantity);
        totalValueTextView = findViewById(R.id.tv_total_value);

        totalValueTextView.setText("" + df.format(pizzaPrice));

        addToCartButton = findViewById(R.id.btn_add_to_cart);
        addToCartButton.setOnClickListener(this);
        addOneButton = findViewById(R.id.btn_increment);
        addOneButton.setOnClickListener(this);
        removeOneButton = findViewById(R.id.btn_decrement);
        removeOneButton.setOnClickListener(this);
        backButton = findViewById(R.id.btn_back_to_pizzas);
        backButton.setOnClickListener(this);
        favouriteButton = findViewById(R.id.btn_add_remove_favourite);
        favouriteButton.setOnClickListener(this);

        additivesGroup = findViewById(R.id.layout_additives);

        mushroomsAdditiveCheckBox = findViewById(R.id.btn_additive_mushrooms);
        cornAdditiveCheckBox= findViewById(R.id.btn_additive_corn);
        picklesAdditiveCheckbox= findViewById(R.id.btn_additive_pickles);

        pizzaWithAdditivesButton = findViewById(R.id.btn_pizza_with_additives);

        if (checkAlreadyExist(pizzaId)){
            favouriteButton.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_remove_favourite));
        }


        quantityValueTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String tv_quantity_value = quantityValueTextView.getText().toString();

                if (tv_quantity_value.isEmpty()) {
                    quantity = 1;

                    Toast.makeText(context, R.string.error_quantity_cannot_be_empty, Toast.LENGTH_SHORT).show();
                } else {
                    quantity = Integer.parseInt(tv_quantity_value);

                    if (quantity < 1) {
                        quantity = 1;
                        Toast.makeText(context, R.string.error_lower_quantity_limit, Toast.LENGTH_SHORT).show();
                    } else if (quantity > 100) {
                        quantity = 100;
                        Toast.makeText(context, R.string.error_upper_quantity_limit, Toast.LENGTH_SHORT).show();
                    }
                }

                quantityValueTextView.setText("" + quantity);
            }
        });

        quantityValueTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tv_quantity_value = quantityValueTextView.getText().toString();

                if (!tv_quantity_value.isEmpty()) {
                    calculateTotal();
                } else {
                    totalValueTextView.setText("" + df.format(pizzaPrice));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        String tv_quantity_value = quantityValueTextView.getText().toString();
        quantity = Integer.parseInt(tv_quantity_value);
        Context context = getApplicationContext();

        SQLiteDatabase database;
        String query;

        switch (view.getId()) {
            //handle multiple view click events
            case R.id.btn_add_to_cart:

                ArrayList<String> additives = new ArrayList<>();
                if(mushroomsAdditiveCheckBox.isChecked()){
                    additives.add("гъби");
                }
                if(cornAdditiveCheckBox.isChecked()){
                    additives.add("царевица");
                }
                if(picklesAdditiveCheckbox.isChecked()){
                    additives.add("кисели краставички");
                }

                String additivesString;

                if(additives.size() != 0){
                    additivesString = String.join(", ", additives);
                }else {
                    additivesString = "";
                }

                database = dbHelper.getWritableDatabase();

                query = "INSERT INTO " +
                        MyDataBaseHelper.ORDERS_TABLE_NAME + " ("
                        + MyDataBaseHelper.ORDERS_COLUMN_PIZZA_NAME + ","
                        + MyDataBaseHelper.ORDERS_COLUMN_ADDITIVES + ","
                        + MyDataBaseHelper.ORDERS_COLUMN_PRICE + ","
                        + MyDataBaseHelper.ORDERS_COLUMN_QUANTITY + ") VALUES ('" + pizzaName + "','"+ additivesString + "',"+ calculateTotal()  + ","+ quantity + ")";

                database.execSQL(query);

                database.close();
                dbHelper.close();
                Toast.makeText(context, R.string.msg_added_to_cart, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_increment:
                if (quantity < 100) {
                    quantity++;
                    quantityValueTextView.setText("" + quantity);
                } else {
                    Toast.makeText(context, R.string.error_upper_quantity_limit, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_decrement:
                if (quantity > 1) {
                    quantity--;
                    quantityValueTextView.setText("" + quantity);
                    //quantityValueTextView.setText(getString(R.string.EmptyStringWithOneArg,quantity));
                } else {
                    Toast.makeText(context, R.string.error_lower_quantity_limit, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_back_to_pizzas:
                startActivity(new Intent(OrderActivity.this, PizzaListActivity.class));
                break;
            case R.id.btn_add_remove_favourite:
                int icon;
                database = dbHelper.getWritableDatabase();

                if (checkAlreadyExist(pizzaId)) {
                    icon = R.drawable.ic_action_add_favourite;

                    database.execSQL("delete from "+ MyDataBaseHelper.FAVOURITES_TABLE_NAME +" where "
                            + MyDataBaseHelper.FAVOURITES_COLUMN_ID + "=" + pizzaId);

                    Toast.makeText(context, R.string.msg_removed_from_favourites, Toast.LENGTH_SHORT).show();
                }
                else {
                    icon = R.drawable.ic_action_remove_favourite;

                    query = "INSERT INTO " +
                            MyDataBaseHelper.FAVOURITES_TABLE_NAME + " ("
                            + MyDataBaseHelper.FAVOURITES_COLUMN_ID + ","
                            + MyDataBaseHelper.FAVOURITES_COLUMN_PIZZA_NAME + ","
                            + MyDataBaseHelper.FAVOURITES_COLUMN_INGREDIENTS + ","
                            + MyDataBaseHelper.FAVOURITES_COLUMN_PRICE + ") VALUES (" + pizzaId + ",'" + pizzaName + "','"+ pizzaIngredients + "',"+ pizzaPrice  + ")";

                    database.execSQL(query);

                    Toast.makeText(context, R.string.msg_added_to_favourites, Toast.LENGTH_SHORT).show();
                }

                database.close();
                dbHelper.close();

                favouriteButton.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), icon));
                break;
            default:
                break;
        }
    }

    /**
     * Method to remove focus from any EditText view once the user clicks outside of it.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Тhe following method handles the click event for both radio buttons:
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.btn_just_pizza:
                if (checked)
                    additivesGroup.setVisibility(View.GONE);
                break;
            case R.id.btn_pizza_with_additives:
                if (checked)
                    additivesGroup.setVisibility(View.VISIBLE);
                break;
        }

        calculateTotal();
    }

    /**
     * Тhe following method handles the click event for all checkboxes:
     */
    public void onCheckBoxClicked(View view){
        calculateTotal();
    }

    /**
     * Тhe following method handles the click event for both radio buttons:
     */
    public double calculateTotal(){
        String tv_quantity_value = quantityValueTextView.getText().toString();
        quantity = Integer.parseInt(tv_quantity_value);

        double newPrice = pizzaPrice;

        if(pizzaWithAdditivesButton.isChecked()){
            if(mushroomsAdditiveCheckBox.isChecked()){
                newPrice+=1;
            }
            if(cornAdditiveCheckBox.isChecked()){
                newPrice+=2;
            }
            if(picklesAdditiveCheckbox.isChecked()){
                newPrice+=3;
            }
        }

        totalValueTextView.setText("" + df.format(newPrice*quantity));
        return newPrice*quantity;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("AdditivesVisibility", additivesGroup.getVisibility());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        additivesGroup.setVisibility(savedInstanceState.getInt("AdditivesVisibility"));

    }

    public boolean checkAlreadyExist(int id)
    {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("select "+ MyDataBaseHelper.FAVOURITES_COLUMN_ID + " from " + MyDataBaseHelper.FAVOURITES_TABLE_NAME + " where "
                + MyDataBaseHelper.FAVOURITES_COLUMN_ID + "=" + id, null);

        if (cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        else{
            cursor.close();
            return false;
        }

    }

}