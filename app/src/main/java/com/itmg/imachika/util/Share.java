package com.itmg.imachika.util;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.itmg.imachika.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class Share {
    private static final String TAG = "Share";
    private void shareText(Context context, String msgTitle, String content) {
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("text/plain");
        it.putExtra(Intent.EXTRA_TEXT,content);
        it.putExtra(Intent.EXTRA_SUBJECT,msgTitle);
        context.startActivity(Intent.createChooser(it,context.getResources().getString(R.string.share)));
    }

    //分享单张图片
    private void shareImage(Context context, String msgTitle, String content) {
        Intent it1 = new Intent(Intent.ACTION_SEND);
        it1.setType("image/jpg");
        String path = Environment.getExternalStorageDirectory() + File.separator + "a.jpg";
        Uri uri = Uri.fromFile(new File(path));
        it1.putExtra(Intent.EXTRA_STREAM,uri);
        context.startActivity(Intent.createChooser(it1,context.getResources().getString(R.string.share)));
    }

    public static void shareImage(Context context, String content) {

        File f = new File(Environment.getExternalStorageDirectory(), "back");
        Log.i(TAG, "shareImage: " + f);//-----------/storage/emulated/0/back
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);//加载图片（res,id）
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);//压缩图片  90是压缩率  表示压缩10%   不压缩是100.表示压缩率为0-----压缩图片然后写入文件中
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent it = new Intent(Intent.ACTION_SEND);
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(it, 0);
        for (ResolveInfo info : resInfos) {
            Intent targeted = new Intent(Intent.ACTION_SEND);

            ActivityInfo activityInfo = info.activityInfo;
            // judgments : activityInfo.packageName, activityInfo.name, etc.
            String packageName = activityInfo.packageName.toLowerCase();
            if (packageName.contains("com.tencent.mm")) {
                ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");//单张图片
                //        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//多张图片
                it.setComponent(comp);
                it.setType("image/*");
                it.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                it.putExtra("Kdescription", content);
            }
            targeted.setType("text/plain");
            targeted.putExtra(Intent.EXTRA_TEXT, content);
            targeted.setPackage(activityInfo.packageName);
            targetedShareIntents.add(targeted);
            context.startActivity(Intent.createChooser(it, context.getResources().getString(R.string.share)));
        }

        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), context.getResources().getString(R.string.share));
        if (chooserIntent == null) {
            return;
        }

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

//        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");//单张图片
//        //        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//多张图片
//        it.setComponent(comp);
//        it.setType("image/*");
//        it.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
//        it.putExtra("Kdescription", content);
//        context.startActivity(Intent.createChooser(it, context.getResources().getString(R.string.share)));
    }

    public static void shareMsg(Context context, String msgTitle, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); // 纯文本
//        intent.setType("image/png"); // 图片

        Uri u = Uri.parse("android.resource://"+context.getPackageName() +"/drawable/logo.png");
        intent.putExtra(Intent.EXTRA_STREAM, u);

        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        // gets the list of intents that can be loaded.
        boolean isWeixin = false;
        List<ResolveInfo> resInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        for (ResolveInfo info : resInfos) {
            Intent targeted = new Intent(Intent.ACTION_SEND);

            ActivityInfo activityInfo = info.activityInfo;
            // judgments : activityInfo.packageName, activityInfo.name, etc.
            String packageName = activityInfo.packageName.toLowerCase();
            if (packageName.contains("bluetooth") || packageName.contains("bluetooth")) {
                continue;
            }
//            String msgText = content;
//            if (packageName.contains("com.tencent.mm")) {
//                String msgText1 = context.getString(R.string.download_url_mm);
//                msgText += msgText1;
//            } else {
//                String msgText2 = context.getString(R.string.download_url);
//                msgText += msgText2;
//            }
            targeted.putExtra(Intent.EXTRA_TEXT, content);
            targeted.setPackage(activityInfo.packageName);
            targetedShareIntents.add(targeted);
        }
        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "分享到:");
        if (chooserIntent == null) {
            return;
        }

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
        try {
            context.startActivity(chooserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Can't find share component to share", Toast.LENGTH_SHORT).show();
        }

    }
}
