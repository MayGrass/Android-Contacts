package tw.org.iii.android_contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_CALL_LOG},7777);
            }
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    private void init() {
        contentResolver = getContentResolver();
    }

    public void test1 (View view) {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        //確認有甚麼欄位可以撈
//        String[] fields = cursor.getColumnNames();
//        for (String field:fields) {
//            Log.v("DCH", field);
//        }
        int indexName = cursor.getColumnIndex("display_name");
        int indexDatal = cursor.getColumnIndex("data1");

        while (cursor.moveToNext()) {
            String name = cursor.getString(indexName);
            String tel = cursor.getString(indexDatal);
            Log.v("DCH", name + ':' + tel);
        }

        cursor.close();
    }

    public void test2(View view) {
        Cursor cursor = contentResolver.query(Settings.System.CONTENT_URI,
                null, null, null, null);
        int indexName = cursor.getColumnIndex("name");
        int indexDatal = cursor.getColumnIndex("value");

        //獲得系統資訊
        while (cursor.moveToNext()) {
            String name = cursor.getString(indexName);
            String value = cursor.getString(indexDatal);
            Log.v("DCH", name + ':' + value);
        }

    }

}
