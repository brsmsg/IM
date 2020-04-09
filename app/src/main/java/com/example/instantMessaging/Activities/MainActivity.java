package com.example.instantMessaging.Activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.factory.Factory;
import com.example.factory.model.User;
import com.example.factory.presenter.Session.SessionPresenter;
import com.example.factory.presenter.contact.ContactPresenter;
import com.example.instantMessaging.Activities.PopWindow.MPopupWindow;
import com.example.instantMessaging.Fragments.main.ContactFragment;
import com.example.instantMessaging.Fragments.main.MessageFragment;
import com.example.instantMessaging.Fragments.main.MomentFragment;
import com.example.instantMessaging.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Fragment mCurrentFragment;
    //消息界面
    private MessageFragment mMessageFragment;
    //联系人界面
    private ContactFragment mContactFragment;
    //朋友圈界面
    private MomentFragment mMomentFragment;

    //用户id,头像，用户名以及KEY
    public final static String MY_ID = "MY_ID";
    public final static String MY_PORTRAIT = "MY_PORTRAIT";
    public final static String MY_USERNAME = "MY_USERNAME";
    public final static String PUBLIC_KEY = "PUBLIC_KEY";
    public final static String PRIVATE_KEY = "PRIVATE_KEY";

    private String myId;
    private String myPortrait;
    private String myUsername;
    private String mPublicKey;
    private String mPrivateKey;

    private ContactPresenter mContactPresenter;
    private SessionPresenter mSessionPresenter;

    private Bundle bundle;

    //设置更换头像相关参数
    private File outputImage;
    private Uri ImgUri;
    private MPopupWindow.Type type;
    private MPopupWindow mPopupWindow;

    @BindView(R.id.bottom_bar)
    BottomNavigationView mBottomBar;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.img_portrait)
    ImageView mPortrait;

    //绑定DrawerLayout
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    //绑定DrawerLayout中的NavigationView
    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.img_settings)
    ImageView mSettings;

    //DrawerLayout中的头像
    private CircleImageView mPersonPortrait;


    @Override
    protected int getContentLayotId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //设置bottomNavbar监听
        mBottomBar.setOnNavigationItemSelectedListener(this);

        //初始化界面为消息界面
        mMessageFragment = new MessageFragment();
        mCurrentFragment = mMessageFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_container_main, mMessageFragment).commit();
        //初始化sessionPresenter
        mSessionPresenter = new SessionPresenter(mMessageFragment);


        //动态引用NavigationView的头部实例mPersonalPortrait并添加点击事件
        View navHeaderView = navView.inflateHeaderView(R.layout.nav_header);
        mPersonPortrait =  navHeaderView.findViewById(R.id.icon_portrait);
        mPersonPortrait.setOnClickListener(v -> {
            //创建MPopupWindow实例
            mPopupWindow = new MPopupWindow(MainActivity.this,MainActivity.this);
            //设置弹出的父界面
            View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
            mPopupWindow.showPopupWindow(rootView);
            //接口，传递ImgUri、Type和outputImage
            mPopupWindow.setOnGetTypeClckListener(new MPopupWindow.onGetTypeClckListener() {
                @Override
                public void getType(MPopupWindow.Type type) {
                    MainActivity.this.type=type;
                }
                @Override
                public void getImgUri(Uri ImgUri, File outputImage) {
                    MainActivity.this.ImgUri = ImgUri;
                    MainActivity.this.outputImage = outputImage;

                }
            });

        });
    }

    @Override
    protected void initData() {
        super.initData();
        //三个属性赋值
        myId = getIntent().getExtras().getString(MY_ID);
        myUsername = getIntent().getExtras().getString(MY_USERNAME);
        myPortrait = getIntent().getExtras().getString(MY_PORTRAIT);
        //初始化公私钥
        mPublicKey = getIntent().getExtras().getString(PUBLIC_KEY);
        mPrivateKey = getIntent().getExtras().getString(PRIVATE_KEY);

        //为bundle赋值
        bundle = new Bundle();
        bundle.putString(MY_ID, myId);
        bundle.putString(MY_USERNAME, myUsername);
        bundle.putString(MY_PORTRAIT, myPortrait);
        bundle.putString(PUBLIC_KEY, mPublicKey);
        bundle.putString(PRIVATE_KEY, mPrivateKey);

        //传给MessageFragment
        mMessageFragment.setArguments(bundle);
        //初始化头像
        if ( myPortrait != null){
            Glide.with(this).load(myPortrait).into(mPortrait);
            Log.d("portraitUrl", myPortrait);
        }
        //初始化webSocket
//        Factory.getInstance().initWebSocket("ws://echo.websocket.org",this);
        Factory.getInstance().initWebSocket("ws://118.31.64.83:8081/ws", myId, this);
    }

    /**
     * 显示入口
     */
    public static void show(Context context, User user, String publicKey, String privateKey){
        Intent intent = new Intent(context, MainActivity.class);

        intent.putExtra(MY_ID, user.getId());
        intent.putExtra(MY_PORTRAIT, user.getFaceImage());
        intent.putExtra(MY_USERNAME, user.getUsername());
        intent.putExtra(PUBLIC_KEY, publicKey);
        intent.putExtra(PRIVATE_KEY, privateKey);
        Log.d("accountId", user.getId());
        context.startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_message:
                mTitle.setText(R.string.main_message);
                changeFragment(mMessageFragment);
                return true;
            case R.id.main_contact:
                mTitle.setText(R.string.main_contact);
                if(mContactFragment == null){
                    mContactFragment = new ContactFragment();
                    //传值给contactFragment
                    mContactFragment.setArguments(bundle);
                }
                //创建联系人presenter实例
                mContactPresenter = new ContactPresenter(mContactFragment);
                changeFragment(mContactFragment);
                return true;
            case R.id.main_moment:
                mTitle.setText(R.string.main_moment);
                if(mMomentFragment == null){
                    mMomentFragment = new MomentFragment();
                }
                changeFragment(mMomentFragment);
                return true;
        }
        return false;
    }

    /**
     * 切换fragment
     * @param fragment 要切换的目标fragment
     */
    private void changeFragment(Fragment fragment){
        if (mCurrentFragment != fragment){
            if(!fragment.isAdded()){
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurrentFragment)
                        .add(R.id.layout_container_main, fragment).commit();
            }else{
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurrentFragment)
                        .show(fragment).commit();
            }
            mCurrentFragment = fragment;
        }
    }

    //获取id,用户名,头像
    public String getMyId(){
        return myId;
    }

    public String getMyUsername(){
        return myUsername;
    }

    public String getMyPortrait(){
        return myPortrait;
    }


    /**
     * 点击ToolBar头像和设置
     * @param view View
     */
    @OnClick({R.id.img_portrait,R.id.img_settings})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.img_portrait:
                mDrawerLayout.openDrawer(GravityCompat.START);
                Log.d("MainActivity","you clicked portrait");
                break;
            case R.id.img_settings:
                Log.d("MainActivity","you clicked settings");
                //菜单
                break;
            default:
                break;

        }
    }

    /**
     * 得到intend后处理
     * @param requestCode int
     * @param resultCode int
     * @param data Intend
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //拍照显示
            case 1:
                if (ImgUri!=null){
                    mPopupWindow.onPhoto(ImgUri,300,300);
                }
                break;
            //获取所选图片的uri并剪裁，动态权限申请
            case 2:
                if (data!=null){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                    }else{
                        Uri uri = data.getData();
                        mPopupWindow.onPhoto(uri,300,300);
                    }
                }
                break;
            //剪裁后进行设置
            case 3:
                if (type == MPopupWindow.Type.PHONE){//相册选择修剪后显示
                    if(data!=null){
                        if (Build.VERSION.SDK_INT>=19){
                            handleImageOnKitKat(mPopupWindow.imageCropUri);
                        }else{
                            //兼容4.4以下版本
                            handleImageBeforeKitKat(mPopupWindow.imageCropUri);
                        }
                    }
                }else if(type == MPopupWindow.Type.CAMERA){//相机拍照修建后显示
                    /*mPersonPortrait.setImageBitmap(BitmapFactory.decodeFile(outputImage.getPath()));*/
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mPopupWindow.imageCropUri));
                        mPersonPortrait.setImageBitmap(bitmap);
                        mPortrait.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            default:
                break;
        }
    }

    /**
     * 权限申请回调
     * @param requestCode int
     * @param permissions String[]
     * @param grantResults int[]
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1://相册调用申请
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    mPopupWindow.openAlbum();
                }else{
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2://Crop修剪后文件外部存储的申请
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Uri uri = getIntent().getData();
                    mPopupWindow.onPhoto(uri,300,300);
                }else{
                    Toast.makeText(this,"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }

    /**
     * 处理4.4以上图片
     * @param uri uri
     */
    private void  handleImageOnKitKat(Uri uri){
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(this,uri)){
            //处理Document类型的Uri,通过DocumentId
            String docId =  DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){//如果Uri的anthority是media格式，DocumentId还需再进行解析
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }

        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);

        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    /**
     * 处理4.4以下图片
     * @param uri
     */
    private void handleImageBeforeKitKat(Uri uri){
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    /**
     * 获取真实路径
     * @param uri Uri
     * @param selection String
     * @return
     */
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过uri和selection来获取图片真实路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if ((cursor.moveToFirst())){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 根据路径显示图片
     * @param imagePath
     */
    private void displayImage(String imagePath){
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPersonPortrait.setImageBitmap(bitmap);
            mPortrait.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"Failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }
}
