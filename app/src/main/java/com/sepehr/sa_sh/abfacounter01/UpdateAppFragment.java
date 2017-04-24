package com.sepehr.sa_sh.abfacounter01;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.ChangePasswordModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by saeid on 2/17/2017.
 */
public class UpdateAppFragment extends Fragment {

    TextView currentVerion;
    Button getNewerVersion;
    ProgressBar progessBarDownloading;
    String token;
    IToastAndAlertBuilder toastAndAlertBuilder;

    public UpdateAppFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_update_app, container, false);
        final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");
        initialize(rootView);
        return rootView;
    }
    //
    private void initialize(View rootView){
        currentVerion=(TextView)rootView.findViewById(R.id.currentVersion);
        getNewerVersion=(Button)rootView.findViewById(R.id.getNewerVersion);
        progessBarDownloading=(ProgressBar)rootView.findViewById(R.id.progressbarGetting);
        currentVerion.setText(BuildConfig.VERSION_NAME);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        token=prefs.getString("token"," ");
        getNewerVersionClickListener();
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
    }
    //
    private void doUpdateApp(String token,int currentVersion){
        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        Call<ResponseBody> call=abfaService.updateApp(token, currentVersion);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   retrofit2.Response<ResponseBody> response) {
                String message = "";
                int responseCode = response.code();
                if(responseCode!=200){
                    switch (responseCode){
                        case 304:
                            message="نسخه فعلی اپلیکیشن شما به روز میباشد";
                            break;
                    }
                }
                else {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                    if (!writtenToDisk) {
                        message = "متاسفانه ذخیره فایل روی دستگاه شما با مشکل روبرو شد لطفا دوباره فایل را دریافت فرمایید";
                    } else {
                        message = "همکار گرامی نسخه جدید  در پوشه Download دستگاه شما با موفقیت ذخیره گردید";
                    }
                }
                toastAndAlertBuilder.makeSimpleAlert(message);
                switchVisibility();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = "";
                if (t instanceof SocketTimeoutException) {
                    errorMessage = "خطای شبکه ،لطفا از اتصال اینترنت اطمینان حاصل فرمایید";
                } else if (t instanceof IOException) {
                    errorMessage = "خطای ارتباطی شبکه";
                } else {
                    errorMessage = "خطا، لطفا مراتب را از اشکال ایجاد شده مطلع فرمایید";
                }
                AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                alert.setTitle("");
                alert.setMessage(errorMessage);
                alert.show();
                Log.e("retrofit error", t.toString());
                switchVisibility();
            }
        });
    }
    //
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            String root= Environment.getExternalStorageDirectory().toString();
            String fileName=DifferentCompanyManager.getActiveCompanyName().toString();
            File futureStudioIconFile = new File(root+"/Download" + File.separator +fileName+ ".apk");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[7168];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(".apk file", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    //
    private void getNewerVersionClickListener(){
        final int versionCode=BuildConfig.VERSION_CODE;
        getNewerVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchVisibility();
                doUpdateApp(token,versionCode);
            }
        });
    }
    //
    private void switchVisibility(){
        if(progessBarDownloading.getVisibility()==View.GONE){
            progessBarDownloading.setVisibility(View.VISIBLE);
            getNewerVersion.setVisibility(View.GONE);
        }
        else {
            progessBarDownloading.setVisibility(View.GONE);
            getNewerVersion.setVisibility(View.VISIBLE);
        }
    }
}
