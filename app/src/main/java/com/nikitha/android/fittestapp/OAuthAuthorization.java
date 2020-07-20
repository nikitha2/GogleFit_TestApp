package com.nikitha.android.fittestapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;
import static com.nikitha.android.fittestapp.Commons.PERMISSION_REQUEST_ACTIVITY_RECOGNITION;
import static com.nikitha.android.fittestapp.Commons.datenStepsPojo;
import static com.nikitha.android.fittestapp.Commons.stepval;
import static java.text.DateFormat.getTimeInstance;

public class OAuthAuthorization {
    private static String TAG=OAuthAuthorization.class.getSimpleName();;
    private static Activity activity;
    static GoogleSignInAccount account;
    static FitnessGetHistoryClientOnSuccessCallback fitnessGetHistoryClientOnSuccess;

    public OAuthAuthorization(FitnessGetHistoryClientOnSuccessCallback fitnessGetHistoryClientOnSuccess) {
        this.fitnessGetHistoryClientOnSuccess=fitnessGetHistoryClientOnSuccess;
    }

    public OAuthAuthorization() {
    }

    public void  getOAuthAuthorizationPermission(Context context){
        activity=(Activity) context;
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        account = GoogleSignIn.getLastSignedInAccount(context);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
                GoogleSignIn.requestPermissions(activity, // your activity
                                                PERMISSION_REQUEST_ACTIVITY_RECOGNITION, // e.g. 0
                                                account,
                                                fitnessOptions);
            } else {
               accessGoogleFit();
            }
        }

    public interface FitnessGetHistoryClientOnSuccessCallback {
        void onFitnessGetHistoryClientOnSuccess(List<DatenStepsPojo> resultList);
    }

    public void accessGoogleFit() {
        datenStepsPojo=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -2);
        long startTime = cal.getTimeInMillis();

        final DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        Task<DataReadResponse> result = Fitness.getHistoryClient(activity, account).readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse response){

                        if (response.getBuckets().size() > 0) {
                            for (Bucket bucket : response.getBuckets()) {
                                long startTimeForBucket = bucket.getStartTime(TimeUnit.MILLISECONDS);
                                long endTimeForBucket = bucket.getEndTime(TimeUnit.MILLISECONDS);
                                List<DataSet> dataset = bucket.getDataSets();
                                for (DataSet dataSet : dataset) {
                                   // processDataSet(dataSet,startTimeForBucket,endTimeForBucket);
                                    processDataSet(startTimeForBucket, endTimeForBucket, dataSet);
                                }
                            }
                        }
                        fitnessGetHistoryClientOnSuccess.onFitnessGetHistoryClientOnSuccess(datenStepsPojo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "OnFailure()", e);
                    }
                });
    }

    private void processDataSet(long startTimeForBucket, long endTimeForBucket, DataSet dataSet) {
        final DateFormat dateFormat = getTimeInstance();
        DateFormat dateFormat1= DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        DataSource dataSource = dataSet.getDataSource();
        List<Field> fields = dataSource.getDataType().getFields();
        Value steps = null;
        String[] split = new String[0];
        // my google account did not have data. So I temporarily adding for displaying data on UI.
        if(dataSet.getDataPoints().size()==0){
            DataPoint dataPoint0 = dataSet.createDataPoint().setTimeInterval(startTimeForBucket, endTimeForBucket, TimeUnit.MILLISECONDS);
            dataPoint0=dataPoint0.setIntValues((int) (stepval+getRandomNumberInRange(0,100)));
            // if your account has data. Please change the line to
            // dataPoint0=dataPoint0.setIntValues(0);
            dataSet.add(dataPoint0);
        }
        for(DataPoint dataPoint11 : dataSet.getDataPoints()) {
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dataPoint11.getDataType().getName());
            Log.i(TAG, "\tStart:1 " + dateFormat.format(dataPoint11.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd:1 " + dateFormat.format(dataPoint11.getEndTime(TimeUnit.MILLISECONDS)));
            Log.e(TAG, "\tStart----: " + dateFormat1.format(startTimeForBucket) + " " + timeFormat.format(startTimeForBucket));
            String date = dateFormat1.format(startTimeForBucket);
            split = date.split("(?<=\\w{1,9}, \\w{1,9} \\d{1,2}, \\d{4})\\s");

            String[] firstPart= split[0].split(",");
            String[] dateNumnMonth= firstPart[0].split(" ");
            if(dateNumnMonth[1].length()==1){
                dateNumnMonth[1]="0".concat(dateNumnMonth[1]);
                split[0]=dateNumnMonth[0]+" "+dateNumnMonth[1]+","+firstPart[1];
            }

            for (Field field : dataPoint11.getDataType().getFields()) {
                String fieldName = field.getName();
                steps = dataPoint11.getValue(field);
            }
        }
        datenStepsPojo.add(new DatenStepsPojo(split[0],steps.toString()));
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
