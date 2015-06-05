package stuart.hcs.myapplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    String[] uomItems = {"Gallons per Hour", "Ounces per Minute"};
    Number[] uomItemValues = {128, 1};

    int itemIndex;
    double convertedMachineOutput;
    double convertedDrawRate;
    double ratio;
    double finalRatio;
    double costPerGallon;
    double costPerOunce;
    double ouncesPerMinute;
    double gallonsPerHour;
    double costPerMinute;

    ScrollView scrollView;
    EditText machineOutputTxt,waterRatioTxt,chemicalRatioTxt,drawRateTxt,preWaterTxt,preChemicalTxt,finalDilutionRatioTxt,costPerGalTxt,costPerOzTxt,ozPerMinTxt,galPerHrTxt,costPerMinTxt;
    Button dilutionRatioBtn,finalDilutionRatioBtn,costPerMinBtn;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLayouts();
        getFields();
        disableFields();
        addFieldListeners();
        getButtons();
        disableButtons();
        populateSpinner();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.reset_action:
                reset();
                break;
            case R.id.about_action:
                showAbout();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showAbout() {

        displayToastMessage("Chemical Calculator authored by Stuart Haas ©2015 Version " + getVersion());
    }
    private void displayToastMessage(String message){

        Toast toast = Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    private void showSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }
    private void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void getLayouts(){

        scrollView = (ScrollView)findViewById(R.id.scrollView);
    }
    private void getFields(){

        spinner = (Spinner)findViewById(R.id.uomItems);
        machineOutputTxt = (EditText)findViewById(R.id.machineOutput);
        waterRatioTxt = (EditText)findViewById(R.id.waterRatio);
        chemicalRatioTxt = (EditText)findViewById(R.id.chemicalRatio);
        drawRateTxt = (EditText)findViewById(R.id.drawRate);
        preWaterTxt = (EditText)findViewById(R.id.preWater);
        preChemicalTxt = (EditText)findViewById(R.id.preChemical);
        finalDilutionRatioTxt = (EditText)findViewById(R.id.finalDilutionRatio);
        costPerGalTxt = (EditText)findViewById(R.id.costPerGal);
        costPerOzTxt = (EditText)findViewById(R.id.costPerOz);
        ozPerMinTxt = (EditText)findViewById(R.id.ozPerMin);
        galPerHrTxt = (EditText)findViewById(R.id.galPerHr);
        costPerMinTxt = (EditText)findViewById(R.id.costPerMin);
    }
    private void disableFields(){

        preWaterTxt.setEnabled(false);
        preChemicalTxt.setEnabled(false);
        finalDilutionRatioTxt.setEnabled(false);
        costPerGalTxt.setEnabled(false);
        costPerOzTxt.setEnabled(false);
        ozPerMinTxt.setEnabled(false);
        galPerHrTxt.setEnabled(false);
        costPerMinTxt.setEnabled(false);
    }
    private void getButtons(){

        dilutionRatioBtn = (Button)findViewById(R.id.dilutionRatioBtn);
        finalDilutionRatioBtn = (Button)findViewById(R.id.finalDilutionRatioBtn);
        costPerMinBtn = (Button)findViewById(R.id.costPerMinBtn);
    }
    private void disableButtons(){

        dilutionRatioBtn.setEnabled(false);
        finalDilutionRatioBtn.setEnabled(false);
        costPerMinBtn.setEnabled(false);
    }
    private void populateSpinner(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.items,
                uomItems);

        spinner.setAdapter(adapter);
    }
    private void addFieldListeners(){

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isEmpty(machineOutputTxt) && !isEmpty(drawRateTxt)) {
                    dilutionRatioBtn.setEnabled(true);
                } else {
                    dilutionRatioBtn.setEnabled(false);
                }
                if (!isEmpty(preWaterTxt) && !isEmpty(preChemicalTxt) && !isEmpty(waterRatioTxt) && !isEmpty(chemicalRatioTxt)) {
                    finalDilutionRatioBtn.setEnabled(true);
                }
                else {
                    finalDilutionRatioBtn.setEnabled(false);
                }
                if (!isEmpty(finalDilutionRatioTxt)) {
                    costPerGalTxt.setEnabled(true);
                } else {
                    costPerGalTxt.setEnabled(false);
                }
                if (!isEmpty(machineOutputTxt) &&  !isEmpty(costPerGalTxt)) {
                    costPerMinBtn.setEnabled(true);
                } else {
                    costPerMinBtn.setEnabled(false);
                }
            }
        };

        machineOutputTxt.addTextChangedListener(textWatcher);
        drawRateTxt.addTextChangedListener(textWatcher);
        waterRatioTxt.addTextChangedListener(textWatcher);
        chemicalRatioTxt.addTextChangedListener(textWatcher);
        preWaterTxt.addTextChangedListener(textWatcher);
        preChemicalTxt.addTextChangedListener(textWatcher);
        finalDilutionRatioTxt.addTextChangedListener(textWatcher);
        costPerGalTxt.addTextChangedListener(textWatcher);
    }
    private String getVersion() {

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        assert pinfo != null;
        return pinfo.versionName;
    }
    private double getMachineOutput(){

        convertedMachineOutput = getDouble(machineOutputTxt) * Double.parseDouble(uomItemValues[0].toString());

        return convertedMachineOutput;
    }
    private double getDrawRate(){

        itemIndex = spinner.getSelectedItemPosition();

        convertedDrawRate = getDouble(drawRateTxt) * Double.parseDouble(uomItemValues[itemIndex].toString());

        return convertedDrawRate;
    }
    private double getRatio(){

        if((getMachineOutput() / getDrawRate()) - getDouble(chemicalRatioTxt) > 0)
            ratio = (getMachineOutput() / getDrawRate()) - getDouble(chemicalRatioTxt);
        else ratio = 0;
        return ratio;
    }

    private double getFinalRatio(){

        finalRatio = ratio * (getDouble(preWaterTxt) + getDouble(preChemicalTxt));
        return finalRatio;
    }
    private void getCost(){

        costPerGallon = getDouble(costPerGalTxt);
        costPerOunce = costPerGallon / finalRatio;
        ouncesPerMinute = convertedMachineOutput / finalRatio;
        gallonsPerHour = (ouncesPerMinute * 60) / 128;
        costPerMinute = ouncesPerMinute * costPerOunce;
    }
    private void reset(){

        if(!isEmpty(machineOutputTxt) ||
                !isEmpty(waterRatioTxt) ||
                !isEmpty(chemicalRatioTxt) ||
                !isEmpty(drawRateTxt) ||
                !isEmpty(preWaterTxt) ||
                !isEmpty(preChemicalTxt) ||
                !isEmpty(finalDilutionRatioTxt) ||
                !isEmpty(costPerGalTxt) ||
                !isEmpty(costPerOzTxt) ||
                !isEmpty(ozPerMinTxt) ||
                !isEmpty(galPerHrTxt) ||
                !isEmpty(costPerMinTxt)) {

            spinner.setSelection(0);
            machineOutputTxt.setText("");
            waterRatioTxt.setText("");
            chemicalRatioTxt.setText("");
            drawRateTxt.setText("");
            preWaterTxt.setText("");
            preChemicalTxt.setText("");
            finalDilutionRatioTxt.setText("");
            costPerGalTxt.setText("");
            costPerOzTxt.setText("");
            ozPerMinTxt.setText("");
            galPerHrTxt.setText("");
            costPerMinTxt.setText("");

            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(0, scrollView.getTop());
                }
            });

            machineOutputTxt.requestFocus();
            showSoftKeyboard();
        }
    }
    public void calcDilutionRatio(View v){

        if(!isEmpty(machineOutputTxt) &&
                !isEmpty(drawRateTxt)) {

            chemicalRatioTxt.setText(String.valueOf(1));
            waterRatioTxt.setText(String.valueOf(round(getRatio(), 0)));

            preWaterTxt.setText(String.valueOf(1));
            preChemicalTxt.setText(String.valueOf(1));
            preWaterTxt.setEnabled(true);
            preChemicalTxt.setEnabled(true);
            preWaterTxt.requestFocus();

            finalDilutionRatioBtn.setEnabled(true);
        }
    }
    public void calcFinalDilutionRatio(View v){

        if(!isEmpty(machineOutputTxt) && !isEmpty(drawRateTxt) && !isEmpty(preWaterTxt) && !isEmpty(preChemicalTxt)){

            finalDilutionRatioTxt.setText(String.valueOf(round(getFinalRatio(), 0)) + " to 1");
            costPerGalTxt.requestFocus();
        }
    }
    public void calcCost(View v){

        if(!isEmpty(machineOutputTxt) ||
                !isEmpty(waterRatioTxt) ||
                !isEmpty(chemicalRatioTxt) ||
                !isEmpty(drawRateTxt) ||
                !isEmpty(preWaterTxt) ||
                !isEmpty(preChemicalTxt) ||
                !isEmpty(finalDilutionRatioTxt) ||
                !isEmpty(costPerGalTxt)
                ) {

            getCost();
            hideSoftKeyboard(costPerMinTxt);

            costPerOzTxt.setText("$ " + String.valueOf(round(costPerOunce, 2)) + " per ounce");
            ozPerMinTxt.setText(String.valueOf(round(ouncesPerMinute, 2)) + " ounces per minute");
            galPerHrTxt.setText(String.valueOf(round(gallonsPerHour, 2)) + " gallons per hour");
            costPerMinTxt.setText("$ " + String.valueOf(round(costPerMinute, 2)) + " per minute");
        }
    }
    public static boolean isEmpty(EditText text){

        return text.getText().toString().isEmpty();
    }
    public static double getDouble(EditText field){

        return Double.parseDouble(field.getText().toString());
    }
    public static double round(double value, int places) {

        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}

