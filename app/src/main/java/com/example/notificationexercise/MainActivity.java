package com.example.notificationexercise;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendWarningNotification(Manifest.permission.POST_NOTIFICATIONS, "Deprem Uyarısı", "3.% büyüklüğünde adsaddasd oldu");
            }
        });


    }


    // Kullanıcıdan bildirim alma izni isteyen fonksiyon
    public void sendNotification(String Title, String Text) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("if çalıştı");

            // Kanal oluştur
            String channelId = "channel_id";
            CharSequence channelName = "MyChannel";
            String description = "Description of my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);

            // Kanalı yöneticiye ekle
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        System.out.println("if dış çalıştı");

        // Bildirimi gönder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "channel_id")
                .setSmallIcon(R.drawable.notify)
                .setContentTitle(Title)
                .setContentText(Text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        notificationManager.notify(1, builder.build());


    }


    // İZin var mı yok mu diye kontrol eder (Manifest.permission.POST_NOTIFICATIONS)
    private static final int NOTIFICATION_PERMISSION_CODE = 123;

    public void sendWarningNotification(String Permission, String Title, String Text) {

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Permission);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // İzin verilmedi
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Permission)) {
                // İzin reddedildi, tekrar izin isteği göster
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("İzin Gerekiyor")
                        .setMessage("Bildirim göndermek için izin vermelisiniz.")
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Permission}, NOTIFICATION_PERMISSION_CODE);
                            }
                        })
                        .create()
                        .show();

            } else {
                // İlk izin isteği, izin isteği penceresini göster
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Permission}, NOTIFICATION_PERMISSION_CODE);
            }
            sendNotification(Title, Text);
        } else {
            // İzin verildi, bildirim gönder
            sendNotification(Title, Text);
        }
    }
}