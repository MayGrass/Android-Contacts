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
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private ContentResolver contentResolver;
    private RequestQueue queue;

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
        queue = Volley.newRequestQueue(this);
        contentResolver = getContentResolver();
        //一開app就抓資料
        test3(null);
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

    public void test3(View view) {
        Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);

//        CallLog.Calls.NUMBER
//        CallLog.Calls.TYPE => CallLog.Calls.INCOMING_TYPE; CallLog.Calls.OUTGOING_TYPE, CallLog.Calls.MISSED_TYPE;
//        CallLog.Calls.DATE;
//        CallLog.Calls.DURATION;
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);

        while (cursor.moveToNext()) {
            String name = cursor.getString(number);
            long value = Long.parseLong(cursor.getString(date));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(value);
            Log.v("DCH", name + ':' + calendar.get(Calendar.YEAR)+ "-" +(calendar.get(Calendar.MONTH)+1));

//            StringRequest request = new StringRequest(Request.Method.GET,
//                    "https://www.bradchao.com/brad/adddata.php?name=" +name+ "&tel=" +value,
//                    null,null);
//            queue.add(request);
        }

    }


}
