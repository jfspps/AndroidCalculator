package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private EditText newNumber;
    private TextView displayOperation;

    // these store pending operands and operators
    private Double operandInMemory = null;
    public String pendingOperation = "=";

    // keys for the bundle
    public static final String STATE_PENDING_OP = "Pending operation";
    public static final String STATE_OPERAND = "Operand";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise all of the controls
        result = (EditText) findViewById(R.id.result);
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);

        Button buttonEquals = (Button) findViewById(R.id.buttonEquals);
        Button buttonDivide = (Button) findViewById(R.id.buttonDivide);
        Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);
        Button buttonSubtract = (Button) findViewById(R.id.buttonSubtract);

        // initialise the listeners
        // 1. sends the numeric input to the newNumber text box
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                newNumber.append(b.getText().toString());
            }
        };
        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);

        // 2. listeners for the operation buttons
        View.OnClickListener operationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String op = b.getText().toString();
                String value = newNumber.getText().toString();

                // look out for cases when entering decimal for the first time (this does not process fractions yet)
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue, op);
                } catch (NumberFormatException exception) {
                    newNumber.setText("");
                }
                pendingOperation = op;
                displayOperation.setText(pendingOperation);
            }
        };

        buttonEquals.setOnClickListener(operationListener);
        buttonDivide.setOnClickListener(operationListener);
        buttonMultiply.setOnClickListener(operationListener);
        buttonPlus.setOnClickListener(operationListener);
        buttonSubtract.setOnClickListener(operationListener);
    }

    private void performOperation(Double value, String operation) {
//        displayOperation.setText(operation);
        if (null == operandInMemory) {
            operandInMemory = value;
        } else {

            // now evaluate pending input...
            if (pendingOperation.equals("=")) {
                pendingOperation = operation;   // so, no change!
            }
            switch (pendingOperation) {
                case "=":
                    operandInMemory = value;    // pressing = first will ultimately clear the operand stored
                    break;
                case "/":
                    if (value == 0) {
                        operandInMemory = 0.0;     // currently set division by zero as zero, despite being wrong
                    } else {
                        operandInMemory /= value;
                    }
                    break;
                case "*":
                    operandInMemory *= value;
                    break;
                case "-":
                    operandInMemory -= value;
                    break;
                case "+":
                    operandInMemory += value;
                    break;
            }
        }

        result.setText(operandInMemory.toString());
        newNumber.setText("");
    }

    // without saving the state of the app, the pending operator data is lost (result text field is saved)

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // use keys to reliably refer to data on restore
        outState.putString(STATE_PENDING_OP, pendingOperation);
        if (operandInMemory != null){
            outState.putDouble(STATE_OPERAND, operandInMemory);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pendingOperation = savedInstanceState.getString(STATE_PENDING_OP);
        operandInMemory = savedInstanceState.getDouble(STATE_OPERAND);
        displayOperation.setText(pendingOperation);
    }


}