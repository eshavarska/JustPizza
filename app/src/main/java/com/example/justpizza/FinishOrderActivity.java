package com.example.justpizza;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

public class FinishOrderActivity<arrayList> extends BaseActivity implements View.OnClickListener {

    String[] city = {
            "гр. Благоевград",
            "гр. Бургас",
            "гр. Варна",
            "гр. Велико Търново",
            "гр. Видин",
            "гр. Враца ",
            "гр. Габрово",
            "гр. Добрич",
            "гр. Кърджали",
            "гр. Кюстендил",
            "гр. Ловеч",
            "гр. Монтана",
            "гр. Пазарджик",
            "гр. Плевен",
            "гр. Перник",
            "гр. Пловдив",
            "гр. Разград",
            "гр. Русе",
            "гр. Силистра",
            "гр. Сливен",
            "гр. Смолян",
            "гр. София",
            "гр. Стара Загора",
            "гр. Търговище",
            "гр. Хасково",
            "гр. Шумен",
            "гр. Ямбол"};

    Button submitOrderButton;
    ImageButton chooseDateButton, chooseTimeButton;
    EditText deliveryDateTextView, deliveryTimeTextView, customerNameTextView, customerPhoneTextView, customerEmailTextView, customerAddressTextView;
    Spinner city_spinner;

    MyDataBaseHelper dbHelper;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        dbHelper = new MyDataBaseHelper(this);

        deliveryDateTextView = findViewById(R.id.edit_tv_delivery_date);
        deliveryTimeTextView = findViewById(R.id.edit_tv_delivery_time);

        city_spinner = (Spinner) findViewById(R.id.spinner_address_city);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, city);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        city_spinner.setAdapter(aa);

        submitOrderButton = findViewById(R.id.btn_submit_order);
        submitOrderButton.setOnClickListener(this);

        chooseDateButton = findViewById(R.id.btn_choose_delivery_date);
        chooseDateButton.setOnClickListener(this);

        chooseTimeButton = findViewById(R.id.btn_choose_delivery_time);
        chooseTimeButton.setOnClickListener(this);

        customerNameTextView = findViewById(R.id.edit_tv_customer_name);
        customerPhoneTextView = findViewById(R.id.edit_tv_customer_phone);
        customerEmailTextView = findViewById(R.id.edit_tv_customer_email);
        customerAddressTextView = findViewById(R.id.edit_tv_customer_address);
    }


    @Override
    public void onClick(View view) {
        Context context = getApplicationContext();

        switch (view.getId()) {
            //handle multiple view click events
            case R.id.btn_submit_order:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Приключихте ли с поръчката?")
                        .setTitle("Потвърждение за поръчка")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Действие на бутона "Да"
                                submitOrderButton.setEnabled(false);

                                SQLiteDatabase database = dbHelper.getWritableDatabase();

                                String order = "";
                                double total = 0.00;
                                Cursor cursor = database.query(
                                        MyDataBaseHelper.ORDERS_TABLE_NAME,
                                        new String[]{
                                                MyDataBaseHelper.ORDERS_COLUMN_PIZZA_NAME, MyDataBaseHelper.ORDERS_COLUMN_ADDITIVES,
                                                MyDataBaseHelper.ORDERS_COLUMN_QUANTITY, MyDataBaseHelper.ORDERS_COLUMN_PRICE},
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                );
                                while (cursor.moveToNext()) {
                                    // получаване на индексите и стойностите на колоните
                                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_PIZZA_NAME));
                                    String additives = cursor.getString(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_ADDITIVES));
                                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_QUANTITY));
                                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDataBaseHelper.ORDERS_COLUMN_PRICE));

                                    order += name + (additives.isEmpty()? " (без добавки" : " (+ " + additives);
                                    order += ") - x" + quantity + " - " + df.format(price) + "лв.\n";
                                    total += price;
                                }


                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/html");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"e.shavarska@gmail.com"});
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Нова поръчка - " + customerNameTextView.getText().toString());
                                intent.putExtra(Intent.EXTRA_TEXT, "Име: " + customerNameTextView.getText().toString() +
                                        "\nТелефон за връзка: " + customerPhoneTextView.getText().toString() +
                                        "\nИмейл: " + customerEmailTextView.getText().toString() +
                                        "\nАдрес: " + city_spinner.getSelectedItem().toString() + ", " + customerAddressTextView.getText().toString() +
                                        "\nДоставката се очаква на " + deliveryDateTextView.getText().toString()
                                        + " в " + deliveryTimeTextView.getText().toString() + " часа." +
                                        "\n\nПоръчани продукти:\n" + order +
                                        "\nОбщо: " + df.format(total));


                                String deleteQuery = "DELETE FROM " + MyDataBaseHelper.ORDERS_TABLE_NAME;

                                database.execSQL(deleteQuery);

                                database.close();
                                dbHelper.close();

                                startActivity(Intent.createChooser(intent, "Send Email"));

                            }
                        })
                        .setNegativeButton("Не", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Действие на бутона "Не"
                                // затваряне на диалога
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            case R.id.btn_choose_delivery_date:

                // определяне на текущата дата
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // Тъй като номерацията на месеците започва от "0",
                // при установяване на датата да увеличим променливата "monthOfYear" с 1.
                DatePickerDialog datePickerDialog = new DatePickerDialog(FinishOrderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Тъй като номерацията на месеците започва от "0",
                                // при установяване на датата да увеличим променливата "monthOfYear" с 1.
                                deliveryDateTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();

                break;

            case R.id.btn_choose_delivery_time:
                // определяне на текущият час
                final Calendar cldr1 = Calendar.getInstance();
                int hour = cldr1.get(Calendar.HOUR_OF_DAY);
                int minute = cldr1.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(FinishOrderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                deliveryTimeTextView.setText(hour + ":" + minute);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();

                break;

            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("customerName", customerNameTextView.getText().toString());
        savedInstanceState.putString("customerPhone", customerPhoneTextView.getText().toString());
        savedInstanceState.putString("customerEmail", customerEmailTextView.getText().toString());
        savedInstanceState.putInt("addressCity_SpinnerPosition", city_spinner.getSelectedItemPosition());
        savedInstanceState.putString("addressStreetNr", customerAddressTextView.getText().toString());
        savedInstanceState.putString("deliveryDate", deliveryDateTextView.getText().toString());
        savedInstanceState.putString("deliveryTime", deliveryTimeTextView.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String name = savedInstanceState.getString("customerName");
        customerNameTextView.setText(name);
        String phone = savedInstanceState.getString("customerPhone");
        customerPhoneTextView.setText(phone);
        String email = savedInstanceState.getString("customerEmail");
        customerEmailTextView.setText(email);
        int cityPos = savedInstanceState.getInt("addressCity_SpinnerPosition");
        city_spinner.setSelection(cityPos);
        String addressStrNr = savedInstanceState.getString("addressStreetNr");
        customerAddressTextView.setText(addressStrNr);
        String date = savedInstanceState.getString("deliveryDate");
        deliveryDateTextView.setText(date);
        String time = savedInstanceState.getString("deliveryTime");
        deliveryTimeTextView.setText(time);

    }
}