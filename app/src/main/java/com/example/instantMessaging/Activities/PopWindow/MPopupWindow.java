package com.example.instantMessaging.Activities.PopWindow;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.factory.utils.ScreenUtils;
import com.example.instantMessaging.R;

import java.io.File;
import java.io.IOException;

/**
 * 更换头像弹窗辅助类
 */
public class MPopupWindow extends PopupWindow implements View.OnClickListener {

    public Context mContext;

    private Type type;//用于判断是从相册选择还是从相机

    public Activity mActivity;

    private File outputImage;

    private Uri ImgUri;

    public enum Type {
        PHONE,CAMERA
    }

    //传递选择图片时产生的file及type
    public interface onGetTypeClckListener {
        void getType(Type type);

        void getImgUri(Uri ImgUri, File outputImage);
    }

    private onGetTypeClckListener listener;


    /**
     *设置对外的listener
     * @param listener the listener
     */
    public void setOnGetTypeClckListener(onGetTypeClckListener listener) {
        this.listener = listener;
    }


    /**
     *构造函数
     * @param context the context
     * @param mActivity the m activity
     */
    public MPopupWindow(Context context, Activity mActivity){
        //初始化界面
        initView(context);
        //活动绑定
        this.mActivity=mActivity;
    }


 /*   @BindView(R.id.photo_take)
    TextView mTakePhoto;

    @BindView(R.id.photo_album)
    TextView mAlbumPhoto;

    @BindView(R.id.photo_cancel)
    TextView mCancel;*/

    //初始化弹出界面
    private void initView(Context mContext){
        this.mContext=mContext;
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_popu,null);
        setContentView(view);

        TextView mTakePhoto = (TextView) view.findViewById(R.id.photo_take);
        TextView mAlbumPhoto = (TextView) view.findViewById(R.id.photo_album);
        TextView mCancel = (TextView) view.findViewById(R.id.photo_cancel);

        mTakePhoto.setOnClickListener(this);
        mAlbumPhoto.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        //this.setClippingEnabled(false);

        //弹出窗口的宽度
        this.setWidth(ScreenUtils.getScreenWidth(mContext));
        //弹出窗口的高度

        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置弹窗可点
        this.setTouchable(true);
        this.setFocusable(false);
        this.setOutsideTouchable(true);
        //刷新状态
        this.update();
        //设置弹出动画
        this.setAnimationStyle(R.style.popuwindow_from_bottom);
        //实例化ColorDrawable颜色为半透明
        ColorDrawable mColorDrawable = new ColorDrawable(0x50000000);
        //设置弹窗背景
        this.setBackgroundDrawable(mColorDrawable);

    }

    //窗口展现
    public void showPopupWindow(View parent){
        if (!this.isShowing()){
            this.showAtLocation(parent, Gravity.BOTTOM,0,0);
        }else{
            this.dismiss();
        }
    }

    //窗口内部的点击事件
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //拍照
            case R.id.photo_take:
                //创建jpg空文件
                /*file = new File(String.valueOf(Environment.getExternalStorageDirectory()),
                            String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                outputImage = new File(mContext.getExternalCacheDir(),"output_image.jpg");
                try{
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //解析文件为uri格式
                if (Build.VERSION.SDK_INT>=24){
                    ImgUri = FileProvider.getUriForFile(mContext,"com.example.instantMessaging.fileprovider",outputImage);
                }else{
                    ImgUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                /*Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);*/
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, ImgUri);//将图片暂存在ImgUri对应的地址中
                mActivity.startActivityForResult(intent1, 1);
                type = Type.CAMERA;//标志
                if(listener!=null){
                    listener.getType(type);
                    listener.getImgUri(ImgUri,outputImage);
                }
                this.dismiss();
                break;

            //从相册中选择
            case R.id.photo_album:
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
                type= Type.PHONE;
                if (listener!=null){
                    listener.getType(type);
                }
                this.dismiss();
                break;

            //取消
            case R.id.photo_cancel:
                this.dismiss();
                break;

            default:
                break;
        }
    }

    /**
     * 打开相册
     */
    public void openAlbum(){
        Intent intent2 = new Intent(Intent.ACTION_PICK,null);
        intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        mActivity.startActivityForResult(intent2,2);
    }


    /**
     * On photo.
     *
     * @param uri the uri
     * @param outputX the output x
     * @param outputY the output y
     */
    /*outputImage = new File(mContext.getExternalCacheDir(),"output_image.jpg");*/
    /*ImgUri = Uri.fromFile(outputImage);*/

    public static final String IMAGE_FILE_LOCATION = "file:///" + Environment.getExternalStorageDirectory().getPath() + "/temp.jpg";
    public Uri imageCropUri = Uri.parse(IMAGE_FILE_LOCATION);

/*    public File imageCrop = new File(String.valueOf(Environment.getExternalStorageDirectory()),"temp.jpg");
    public Uri imageCropUri = Uri.fromFile(imageCrop);*/

    public void onPhoto(Uri uri,int outputX,int outputY){
        //剪裁图片意图
        Intent intent3 = new Intent("com.android.camera.action.CROP");
        intent3.setDataAndType(uri,"image/*");
        intent3.putExtra("crop","true");
        //剪裁框比例为1：1
        intent3.putExtra("aspectX",1);
        intent3.putExtra("aspectY", 1);
        //输出尺寸
        intent3.putExtra("outputX", outputX);
        intent3.putExtra("outputY", outputY);

        intent3.putExtra("scale", true);
        intent3.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);//输出到此地址
        intent3.putExtra("return-data", false);//不返回bitmap对象，将图片保存到本地并将对应的uri返回（自己设定）
        intent3.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//输出JPEG图
        intent3.putExtra("noFaceDetection", true);
        mActivity.startActivityForResult(intent3, 3);

    }



}

