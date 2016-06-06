package com.evolve.mitchell.evolvefitnessprogramtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.components.ButtonEditText;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.DatabaseHelper;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;

import java.util.ArrayList;

public class CreateExercise extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Boolean mExerciseCreated = false;

    RelativeLayout contentLayout;
    RelativeLayout unitsLayout;
    RelativeLayout trackedMeasurementsLayout;
    LinearLayout startingMeasurementsLayout;
    RelativeLayout targetIncreaseLayout;
    RelativeLayout increasePerSessionLayout;
    RelativeLayout categoryLayout;

    ArrayList<ArrayList<View>> unitViews;
    ArrayList<String> distanceImperialUnits;
    ArrayList<String> distanceMetricUnits;
    ArrayList<String> exerciseCategories;

    ToggleButton imperialToggle;
    ToggleButton metricToggle;

    FloatingActionButton fab;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get content layouts
        contentLayout = (RelativeLayout) findViewById(R.id.createExerciseContent);

        assert contentLayout != null;
        unitsLayout = (RelativeLayout) contentLayout.findViewById(R.id.unitsLayout);
        trackedMeasurementsLayout = (RelativeLayout) contentLayout.findViewById(R.id.trackedMeasurementsLayout);
        startingMeasurementsLayout = (LinearLayout) contentLayout.findViewById(R.id.startingMeasurementsLayout);
        targetIncreaseLayout = (RelativeLayout) contentLayout.findViewById(R.id.targetIncreaseLayout);
        increasePerSessionLayout = (RelativeLayout) contentLayout.findViewById(R.id.increasePerSessionLayout);
        categoryLayout = (RelativeLayout) contentLayout.findViewById(R.id.categoryLayout);

        // Get unit views
        ArrayList<View> repsViews = new ArrayList<>(4);
        ArrayList<View> weightViews = new ArrayList<>(4);
        ArrayList<View> distanceViews = new ArrayList<>(4);
        ArrayList<View> timeViews = new ArrayList<>(4);

        repsViews.add(trackedMeasurementsLayout.findViewById(R.id.repsToggleButton));
        repsViews.add(contentLayout.findViewById(R.id.repsRow));
        repsViews.add(targetIncreaseLayout.findViewById(R.id.repsTargetToggleButton));
        repsViews.add(increasePerSessionLayout.findViewById(R.id.repsIncreaseLayout));
        weightViews.add(trackedMeasurementsLayout.findViewById(R.id.weightToggleButton));
        weightViews.add(contentLayout.findViewById(R.id.weightRow));
        weightViews.add(targetIncreaseLayout.findViewById(R.id.weightTargetToggleButton));
        weightViews.add(increasePerSessionLayout.findViewById(R.id.weightIncreaseLayout));
        distanceViews.add(trackedMeasurementsLayout.findViewById(R.id.distanceToggleButton));
        distanceViews.add(contentLayout.findViewById(R.id.distanceRow));
        distanceViews.add(targetIncreaseLayout.findViewById(R.id.distanceTargetToggleButton));
        distanceViews.add(increasePerSessionLayout.findViewById(R.id.distanceIncreaseLayout));
        timeViews.add(trackedMeasurementsLayout.findViewById(R.id.timeToggleButton));
        timeViews.add(contentLayout.findViewById(R.id.timeRow));
        timeViews.add(targetIncreaseLayout.findViewById(R.id.timeTargetToggleButton));
        timeViews.add(increasePerSessionLayout.findViewById(R.id.timeIncreaseLayout));

        unitViews = new ArrayList<>(4);
        unitViews.add(repsViews);
        unitViews.add(weightViews);
        unitViews.add(distanceViews);
        unitViews.add(timeViews);

        // Get the ToggleButtons
        imperialToggle = (ToggleButton) unitsLayout.findViewById(R.id.imperialToggleButton);
        metricToggle = (ToggleButton) unitsLayout.findViewById(R.id.metricToggleButton);

        // Set up spinners
        // TODO: Set up a unit arrayAdapter
        distanceImperialUnits = new ArrayList<>(2);
        distanceImperialUnits.add("feet");
        distanceImperialUnits.add("miles");

        distanceMetricUnits = new ArrayList<>(2);
        distanceMetricUnits.add("meters");
        distanceMetricUnits.add("kilometers");

        // TODO: Set up a category arrayAdapter
        exerciseCategories = new ArrayList<>();
        exerciseCategories.add("Arms");
        exerciseCategories.add("Abs");
        exerciseCategories.add("Back");
        exerciseCategories.add("Chest");
        exerciseCategories.add("Legs");
        exerciseCategories.add("Cardio");

        Spinner dSpinner = (Spinner) findViewById(R.id.distanceUnitSpinner);
        Spinner dIncreaseSpinner = (Spinner) findViewById(R.id.distanceIncreaseUnitSpinner);
        Spinner categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        assert dSpinner != null;
        assert dIncreaseSpinner != null;
        assert categorySpinner != null;

        dSpinner.setOnItemSelectedListener(this);
        dIncreaseSpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> distanceArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceImperialUnits);
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exerciseCategories);
        distanceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dSpinner.setAdapter(distanceArrayAdapter);
        dIncreaseSpinner.setAdapter(distanceArrayAdapter);
        categorySpinner.setAdapter(categoryArrayAdapter);
        dSpinner.setSelection(0);
        dIncreaseSpinner.setSelection(0);
        categorySpinner.setSelection(0);


        // Initialize starting measurement rows, target toggle buttons, and increase per session views to gone
        for (int unitViewIndex = 1; unitViewIndex<=3; unitViewIndex++) {
            for (int unit = 0; unit <=3; unit++) {
                unitViews.get(unit).get(unitViewIndex).setVisibility(View.GONE);
            }
        }

        startingMeasurementsLayout.setVisibility(View.GONE);
        targetIncreaseLayout.setVisibility(View.GONE);
        increasePerSessionLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExerciseCreated = true;
                persistExerciseAndFinish();
            }
        });
        fab.hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mExerciseCreated) {
            persistExercise(false);
        }
    }

    private boolean hasMeasurementsTracked() {
        ToggleButton repsToggle = (ToggleButton) unitViews.get(0).get(0);
        ToggleButton weightToggle = (ToggleButton) unitViews.get(1).get(0);
        ToggleButton distanceToggle = (ToggleButton) unitViews.get(2).get(0);
        ToggleButton timeToggle = (ToggleButton) unitViews.get(3).get(0);
        boolean reps = repsToggle.isChecked();
        boolean weight = weightToggle.isChecked();
        boolean distance = distanceToggle.isChecked();
        boolean time = timeToggle.isChecked();
        return reps || weight || distance || time;
    }

    private boolean hasMeasurementTarget() {
        ToggleButton repsToggle = (ToggleButton) unitViews.get(0).get(2);
        ToggleButton weightToggle = (ToggleButton) unitViews.get(1).get(2);
        ToggleButton distanceToggle = (ToggleButton) unitViews.get(2).get(2);
        ToggleButton timeToggle = (ToggleButton) unitViews.get(3).get(2);
        boolean reps = repsToggle.isChecked();
        boolean weight = weightToggle.isChecked();
        boolean distance = distanceToggle.isChecked();
        boolean time = timeToggle.isChecked();
        return reps || weight || distance || time;
    }

    private void trackedMeasurementToggleClicked(MeasurementCategory category) {
        ToggleButton trackedToggle = (ToggleButton) unitViews.get(category.value()).get(0);
        ToggleButton targetToggle = (ToggleButton) unitViews.get(category.value()).get(2);
        TableRow row = (TableRow) unitViews.get(category.value()).get(1);
        if (hasMeasurementsTracked()) {
            startingMeasurementsLayout.setVisibility(View.VISIBLE);
            targetIncreaseLayout.setVisibility(View.VISIBLE);
            categoryLayout.setVisibility(View.VISIBLE);
            fab.show();
        }
        else {
            startingMeasurementsLayout.setVisibility(View.GONE);
            targetIncreaseLayout.setVisibility(View.GONE);
            categoryLayout.setVisibility(View.GONE);
            fab.hide();
        }
        if (trackedToggle.isChecked()) {
            targetToggle.setVisibility(View.VISIBLE);
            row.setVisibility(View.VISIBLE);
        }
        else {
            targetToggle.setVisibility(View.GONE);
            if (targetToggle.isChecked()) {
                targetToggle.setChecked(false);
                increasePerSessionLayout.setVisibility(View.GONE);
            }
            row.setVisibility(View.GONE);
        }
    }

    private void targetToggleClicked(MeasurementCategory category){
        ToggleButton targetToggle = (ToggleButton) unitViews.get(category.value()).get(2);
        LinearLayout increaseUnitLayout = (LinearLayout) unitViews.get(category.value()).get(3);
        ToggleButton otherToggle;
        LinearLayout otherIncreaseLayout;
        if (targetToggle.isChecked()) {
            for (MeasurementCategory unitCategory: MeasurementCategory.values()) {
                if (category != unitCategory) {
                    otherToggle = (ToggleButton) unitViews.get(unitCategory.value()).get(2);
                    otherIncreaseLayout = (LinearLayout) unitViews.get(unitCategory.value()).get(3);
                    otherToggle.setChecked(false);
                    otherIncreaseLayout.setVisibility(View.GONE);
                }
            }
            increaseUnitLayout.setVisibility(View.VISIBLE);
            increasePerSessionLayout.setVisibility(View.VISIBLE);
        }
        else {
            increaseUnitLayout.setVisibility(View.GONE);
            increasePerSessionLayout.setVisibility(View.GONE);
        }
    }

    private void setUnits(boolean imperial){
        TextView weightUnit = (TextView) findViewById(R.id.weightUnitText);
        Spinner dSpinner = (Spinner) findViewById(R.id.distanceUnitSpinner);
        TextView weightIncreaseUnit = (TextView) findViewById(R.id.weightIncreaseUnitText);
        Spinner dIncreaseSpinner = (Spinner) findViewById(R.id.distanceIncreaseUnitSpinner);
        assert weightUnit != null;
        assert dSpinner != null;
        assert weightIncreaseUnit != null;
        assert dIncreaseSpinner != null;

        if (imperial) {
            weightUnit.setText(R.string.weight_imperial_unit);
            weightIncreaseUnit.setText(R.string.weight_imperial_unit);
            ArrayAdapter<String> dSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceImperialUnits);
            dSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dSpinner.setAdapter(dSpinnerAdapter);
            dIncreaseSpinner.setAdapter(dSpinnerAdapter);
            dSpinner.setSelection(0);
            dIncreaseSpinner.setSelection(0);
        }
        else {
            weightUnit.setText(R.string.weight_metric_unit);
            weightIncreaseUnit.setText(R.string.weight_metric_unit);
            ArrayAdapter<String> dSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, distanceMetricUnits);
            dSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dSpinner.setAdapter(dSpinnerAdapter);
            dIncreaseSpinner.setAdapter(dSpinnerAdapter);
            dSpinner.setSelection(0);
            dIncreaseSpinner.setSelection(0);
        }
    }

    public void handleButtonClick(View view) {
        ToggleButton tb = (ToggleButton) view;
        switch (tb.getId()) {
            case R.id.imperialToggleButton:
                metricToggle.setChecked(!imperialToggle.isChecked());
                setUnits(imperialToggle.isChecked());
                break;
            case R.id.metricToggleButton:
                imperialToggle.setChecked(!metricToggle.isChecked());
                setUnits(imperialToggle.isChecked());
                break;
            case R.id.repsToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.REPS);
                break;
            case R.id.weightToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.WEIGHT);
                break;
            case R.id.distanceToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.DISTANCE);
                break;
            case R.id.timeToggleButton:
                trackedMeasurementToggleClicked(MeasurementCategory.TIME);
                break;
            case R.id.repsTargetToggleButton:
                targetToggleClicked(MeasurementCategory.REPS);
                break;
            case R.id.weightTargetToggleButton:
                targetToggleClicked(MeasurementCategory.WEIGHT);
                break;
            case R.id.distanceTargetToggleButton:
                targetToggleClicked(MeasurementCategory.DISTANCE);
                break;
            case R.id.timeTargetToggleButton:
                targetToggleClicked(MeasurementCategory.TIME);
                break;
            default:
                break;
        }
    }

    private Exercise createExerciseFromFormData() {
        Exercise exercise = new Exercise();

        EditText titleEdit = (EditText) findViewById(R.id.edit_title);
        assert titleEdit != null;
        exercise.setName(titleEdit.getText().toString());

        if (imperialToggle.isChecked()) {
            exercise.setImperialUnits();
        }
        else {
            exercise.setMetricUnits();
        }

        for (MeasurementCategory unitCategory: MeasurementCategory.values()) {
            ToggleButton trackedToggle = (ToggleButton) unitViews.get(unitCategory.value()).get(0);
            if (trackedToggle.isChecked()) {
                exercise.trackNewMeasurementCategory(unitCategory);
                switch (unitCategory) {
                    case REPS:
                        ButtonEditText repsBET = (ButtonEditText) findViewById(R.id.bet_reps_starting_measurement);
                        assert repsBET != null;
                        int startingReps = (int) repsBET.getNumber();
                        try {
                            exercise.setInitialMeasurementValue(MeasurementCategory.REPS, startingReps);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case WEIGHT:
                        ButtonEditText weightBET = (ButtonEditText) findViewById(R.id.bet_weight_starting_measurement);
                        assert weightBET != null;
                        float startingWeight = (int) weightBET.getNumber();
                        try {
                            exercise.setInitialMeasurementValue(MeasurementCategory.WEIGHT, startingWeight);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case DISTANCE:
                        ButtonEditText distanceBET = (ButtonEditText) findViewById(R.id.bet_distance_starting_measurement);
                        assert distanceBET != null;
                        float startingDistance = (int) distanceBET.getNumber();
                        try {
                            exercise.setInitialMeasurementValue(MeasurementCategory.DISTANCE, startingDistance);
                            //exercise.setTrackedMeasurementUnit(MeasurementCategory.DISTANCE, Unit.KILOMETERS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case TIME:
                        ButtonEditText hoursBET = (ButtonEditText) findViewById(R.id.bet_time_hours_starting_measurement);
                        ButtonEditText minutesBET = (ButtonEditText) findViewById(R.id.bet_time_minutes_starting_measurement);
                        ButtonEditText secondsBET = (ButtonEditText) findViewById(R.id.bet_time_seconds_starting_measurement);
                        assert hoursBET != null;
                        assert minutesBET != null;
                        assert secondsBET != null;
                        float hours = (int) hoursBET.getNumber();
                        float minutes = (int) minutesBET.getNumber();
                        float seconds = (int) secondsBET.getNumber();
                        float timeInSeconds = 3600*hours + 60*minutes + seconds;
                        try {
                            exercise.setInitialMeasurementValue(MeasurementCategory.TIME, timeInSeconds);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
        float increment = 0;
        for (MeasurementCategory unitCategory: MeasurementCategory.values()) {
            ToggleButton targetToggle = (ToggleButton) unitViews.get(unitCategory.value()).get(2);
            if (targetToggle.isChecked()) {
                switch (unitCategory) {
                    case REPS:
                        EditText repsIncreaseText = (EditText) findViewById(R.id.editRepsIncreasePerSession);
                        assert repsIncreaseText != null;
                        increment = Integer.valueOf(repsIncreaseText.getText().toString());
                        break;
                    case WEIGHT:
                        EditText weightIncreaseText = (EditText) findViewById(R.id.editWeightIncreasePerSession);
                        assert weightIncreaseText != null;
                        increment = Float.valueOf(weightIncreaseText.getText().toString());
                        break;
                    case DISTANCE:
                        EditText distanceIncreaseText = (EditText) findViewById(R.id.editDistanceIncreasePerSession);
                        assert distanceIncreaseText != null;
                        increment = Float.valueOf(distanceIncreaseText.getText().toString());
                        break;
                    case TIME:
                        EditText minutesText = (EditText) findViewById(R.id.timeIncreaseMinuteEdittext);
                        EditText secondsText = (EditText) findViewById(R.id.timeIncreaseSecondEdittext);
                        assert minutesText != null;
                        assert secondsText != null;
                        float minutes = Float.valueOf(minutesText.getText().toString());
                        float seconds = Float.valueOf(secondsText.getText().toString());
                        increment = 60*minutes + seconds;
                        break;
                }
                exercise.setIncrement(increment);
            }
        }
        return exercise;
    }

    private String persistExercise(boolean permanent) {
        Exercise exercise = createExerciseFromFormData();
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        return db.insertExercise(exercise, permanent);
    }

    public void persistExerciseAndFinish() {
        String exerciseName = persistExercise(true);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(DatabaseHelper.KEY_EXERCISE_NAME, exerciseName);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
