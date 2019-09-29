package www.wesy.fr.torchlite;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Camera camera;
    private Camera.Parameters cameraParameters;
    private static final int CAMERA_REQUEST = 123;
    private boolean isFlashOn = false;
    private ImageView ivFlashlight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        ivFlashlight = (ImageView) findViewById(R.id.btn_flashlight);

        if (!haveCameraDevice()) {
            Toast.makeText(MainActivity.this, "No flash available on your device",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!havePermission()) {
            Toast.makeText(MainActivity.this, "Permission not granted",
                    Toast.LENGTH_LONG).show();
            return;
        }

        camera = Camera.open();
        cameraParameters = camera.getParameters();

        ivFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashOn) {
                    switchFlashOff();
                } else {
                    switchFlashOn();
                }
            }
        });
    }

    private boolean haveCameraDevice() {
       return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private boolean havePermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);

        String requiredPermission = "android.permission.CAMERA";
        int checkVal = mContext.checkCallingOrSelfPermission(requiredPermission);
        return (checkVal==PackageManager.PERMISSION_GRANTED)
            ? true
            : false;
    }

    private void switchFlashOn() {
        ivFlashlight.setImageDrawable(getResources().getDrawable(R.drawable.btn_on));
        cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(cameraParameters);
        camera.startPreview();
        isFlashOn = true;
    }

    private void switchFlashOff() {
        ivFlashlight.setImageDrawable(getResources().getDrawable(R.drawable.btn_off));
        cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(cameraParameters);
        camera.stopPreview();
        isFlashOn = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switchFlashOff();
        finish();
    }
}
