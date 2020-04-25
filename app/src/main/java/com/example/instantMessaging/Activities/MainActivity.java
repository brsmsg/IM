package com.example.instantMessaging.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.common.app.Activity;
import com.example.common.app.Fragment;
import com.example.common.app.Mapper;
import com.example.factory.Factory;
import com.example.factory.model.User;
import com.example.factory.model.api.account.update.UsernameModel;
import com.example.factory.presenter.Friend.SearchFriendPresenter;
import com.example.factory.presenter.Friend.SearchRequestPresenter;
import com.example.factory.presenter.Session.SessionPresenter;
import com.example.factory.presenter.contact.ContactPresenter;
import com.example.factory.utils.NetUtils;
import com.example.factory.utils.OssService;
import com.example.factory.utils.SpUtils;
import com.example.instantMessaging.Activities.PopWindow.MPopupWindow;
import com.example.instantMessaging.Fragments.LoginFragment;
import com.example.instantMessaging.Fragments.main.ContactFragment;
import com.example.instantMessaging.Fragments.main.MessageFragment;
import com.example.instantMessaging.Fragments.main.SearchFragment;
import com.example.instantMessaging.Fragments.main.SearchFriendFragment;
import com.example.instantMessaging.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;

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
    //好友请求界面
    private SearchFragment mSearchFragment;
    //查询好友界面
    private SearchFriendFragment mSearchFriendFragment;

    private final static String updatePortraitUrl = "http://118.31.64.83:8080/account/update/portrait";
    private final static String updateUsernameUrl = "http://118.31.64.83:8080/account/update/username";
    private final static String updatePasswordUrl = "http://118.31.64.83:8080/account/update/password";
    private final static String updateDescUrl = "http://118.31.64.83:8080/account/update/description";
    private final static String uploadPortraitUrl = "http://101.200.240.107:8000/uploadImage";
    private final static String basePath = "http://101.200.240.107/images/im_portraits/";

    //用户id,头像，用户名以及KEY
    public final static String MY_ID = "MY_ID";
    public final static String MY_PORTRAIT = "MY_PORTRAIT";
    public final static String MY_USERNAME = "MY_USERNAME";
    public final static String MY_DESC = "MY_DESC";
    public final static String PUBLIC_KEY = "PUBLIC_KEY";
    public final static String PRIVATE_KEY = "PRIVATE_KEY";

    private final static String OSS_ACCESS_KEY = "LTAI4GFdaTqVVh6ysNsTZHWA";
    private final static String OSS_ACCESS_SECRET = "QiAs0nubviXOoLXxiQeQ5l7TZDN03M";
    private final static String OSS_ENDPOINT = "oss-cn-beijing.aliyuncs.com";
    private final static String OSS_BUCKET_NAME = "kbh";
    private final static String OSS_URL = "https://kbh.oss-cn-beijing.aliyuncs.com/";

    private String myId;
    private String myPortrait;
    private String myUsername;
    private String myDesc;

    //presenter
    private ContactPresenter mContactPresenter;
    private SessionPresenter mSessionPresenter;
    private SearchRequestPresenter mSearchPresenter;
    private SearchFriendPresenter mSearchFriendPresenter;

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

    @BindView(R.id.img_search)
    ImageView mSettings;

    //DrawerLayout中的用户名和头像
    private TextView mPersonUsername;
    private CircleImageView mPersonPortrait;
    private TextView mPersonDesc;


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
        mSessionPresenter = new SessionPresenter(mMessageFragment, this);


        //动态引用NavigationView的头部实例mPersonalPortrait并添加点击事件
        View navHeaderView = navView.inflateHeaderView(R.layout.nav_header);
        mPersonPortrait =  navHeaderView.findViewById(R.id.icon_portrait);
        mPersonUsername = navHeaderView.findViewById(R.id.nav_header_username);
        mPersonDesc = navHeaderView.findViewById(R.id.nav_header_desc);

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

        //点击修改用户名密码
        navView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.personal_username:
                    //修改用户名
                    final EditText editText = new EditText(this);
                    AlertDialog.Builder inputDialog = new AlertDialog.Builder(this);
                    inputDialog.setTitle("输入新用户名")
                            .setView(editText)
                            .setPositiveButton("确定", (dialog, which) -> {
                                Factory.getInstance().getThreadPool().execute(() -> {
                                //发送请求
                                    String newUsername = editText.getText().toString().trim();
                                    String result = NetUtils.postKeyValue("id", myId,
                                            "username", newUsername,
                                            updateUsernameUrl);
                                    if(result != null){
                                        String info = NetUtils.parseUpdateResult(result);
                                        //提示
                                        showToast(info);
                                        if(info.equals("更新成功")){
                                            //对变量以及界面更新
                                            myUsername = newUsername;
                                            runOnUiThread(() -> mPersonUsername.setText(newUsername));
                                        }
                                    }else{
                                        //提示
                                        showToast("网络错误");
                                    }
                                });
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;

                case R.id.personal_password:
                    //修改密码
                    final View updatePwdDialog = LayoutInflater.from(this).inflate(R.layout.dialog_update_pwd, null);
                    final EditText oldPwdEditText = updatePwdDialog.findViewById(R.id.pwd_old);
                    final EditText newPwdEditText = updatePwdDialog.findViewById(R.id.pwd_new);
                    AlertDialog.Builder pwdInputDialog = new AlertDialog.Builder(this);
                    pwdInputDialog.setTitle("修改密码")
                            .setView(updatePwdDialog)
                            .setPositiveButton("确定", (dialog, which) -> {
                                Factory.getInstance().getThreadPool().execute(() -> {
                                    //发送请求
                                    String oldPassword = oldPwdEditText.getText().toString().trim();
                                    String newPassword = newPwdEditText.getText().toString().trim();
                                    String result = NetUtils.postKeyValue("id", myId,
                                            "oldPassword", oldPassword,
                                            "newPassword", newPassword,
                                            updatePasswordUrl);
                                    if(result != null){
                                        String info = NetUtils.parseUpdateResult(result);
                                        //提示
                                        showToast(info);
                                        //退回登录界面
                                        AccountActivity.show(this);
                                        finish();
                                    }else{
                                        //提示
                                        showToast("网络错误");
                                    }
                                });
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;
                case R.id.personal_desc:
                    final EditText descEditText = new EditText(this);
                    AlertDialog.Builder DescInputDialog = new AlertDialog.Builder(this);
                    DescInputDialog.setTitle("输入个签")
                            .setView(descEditText)
                            .setPositiveButton("确定", (dialog, which) -> {
                                Factory.getInstance().getThreadPool().execute(() -> {
                                    //发送请求
                                    String newDesc = descEditText.getText().toString().trim();
                                    String result = NetUtils.postKeyValue("id", myId,
                                            "description", newDesc,
                                            updateDescUrl);
                                    if(result != null){
                                        String info = NetUtils.parseUpdateResult(result);
                                        //提示
                                        showToast(info);
                                        if(info.equals("更新成功")){
                                            //对变量以及界面更新
                                            myDesc = newDesc;
                                            runOnUiThread(() -> mPersonDesc.setText(newDesc));
                                        }
                                    }else{
                                        //提示
                                        showToast("网络错误");
                                    }
                                });
                            })
                            .setNegativeButton("取消", null)
                            .show();
                    break;

                case R.id.exit_login:
                    //退出登录
//                    SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sp.edit();
//                    //取消自动登录,清空用户名密码
//                    editor.putBoolean(LoginFragment.AUTO_ISCHECK, false).commit();
//                    editor.putString(LoginFragment.USERNAME, "").commit();
//                    editor.putString(LoginFragment.PASSWORD, "").commit();

                    SpUtils.saveData(this, Mapper.SP_USERNAME, "");
                    SpUtils.saveData(this, Mapper.SP_PASSWORD, "");
                    SpUtils.saveData(this, Mapper.SP_AUTO_ISCHECK, false);
                    SpUtils.saveData(this, Mapper.SP_PUBLIC_KEY, "");

                    AccountActivity.show(this);
                    finish();
            }

            return false;
        });


    }

    @Override
    protected void initData() {
        super.initData();
        //三个属性赋值
        myId = getIntent().getExtras().getString(MY_ID);
        myUsername = getIntent().getExtras().getString(MY_USERNAME);
        myPortrait = getIntent().getExtras().getString(MY_PORTRAIT);
        myDesc = getIntent().getExtras().getString(MY_DESC);

        //为bundle赋值，用于fragment获取
        bundle = new Bundle();
        bundle.putString(MY_ID, myId);
        bundle.putString(MY_USERNAME, myUsername);
        bundle.putString(MY_PORTRAIT, myPortrait);

        //传给MessageFragment
        mMessageFragment.setArguments(bundle);

        //初始化用户名
        mPersonUsername.setText(myUsername);
        mPersonDesc.setText(myDesc);
        //初始化头像
        if ( myPortrait != null){
            //加载出错，显示默认原始图片
            Glide.with(this).load(myPortrait).error(R.drawable.origin_portrait).into(mPortrait);
            Glide.with(this).load(myPortrait).error(R.drawable.origin_portrait).into(mPersonPortrait);
            Log.d("portraitUrl", myPortrait);
        }else{
            //如果返回头像为空，加载默认原始图片
            Glide.with(this).load(R.drawable.origin_portrait).into(mPortrait);
            Glide.with(this).load(R.drawable.origin_portrait).into(mPersonPortrait);

        }

        //初始化webSocket
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
        intent.putExtra(MY_DESC, user.getDescription());
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
            //改为好友请求界面
            case R.id.main_moment:
                mTitle.setText(R.string.main_moment);
                if(mSearchFragment == null){
                    mSearchFragment = new SearchFragment();
                    //传值给SearchFragment,用于处理好友请求myId
                    mSearchFragment.setArguments(bundle);

                }
                //创建查询好友请求Presenter实例
                mSearchPresenter = new SearchRequestPresenter(mSearchFragment);
                changeFragment(mSearchFragment);
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
    @OnClick({R.id.img_portrait,R.id.img_search})
    public void onViewClicked(View view){
        switch(view.getId()){
            case R.id.img_portrait:
                mDrawerLayout.openDrawer(GravityCompat.START);
                Log.d("MainActivity","you clicked portrait");
                break;
            case R.id.img_search:
                Log.d("MainActivity","you clicked settings");
                //切换fragment到搜索
                if(mSearchFriendFragment == null){
                    mSearchFriendFragment = new SearchFriendFragment();
                    //传值给SearchFragment,用于处理好友请求myId
                    mSearchFriendFragment.setArguments(bundle);
                    //创建查询好友请求Presenter实例
                    mSearchFriendPresenter = new SearchFriendPresenter(mSearchFriendFragment);
                }
/*                changeFragment(mSearchFriendFragment);*/
                //搜索好友界面添加返回栈功能
                if (mCurrentFragment != mSearchFriendFragment){
                    if(!mSearchFriendFragment.isAdded()){
                        getSupportFragmentManager().beginTransaction()
                                .hide(mCurrentFragment)
                                .add(R.id.layout_container_main, mSearchFriendFragment)
                                .addToBackStack(null)
                                .commit();
                    }else{
                        getSupportFragmentManager().beginTransaction()
                                .hide(mCurrentFragment)
                                .show(mSearchFriendFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                    //mCurrentFragment用于保护入栈之前的fragment，不能改动
                    //mCurrentFragment = mSearchFriendFragment;
                }
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

            String filename = myId +":"+ System.currentTimeMillis() + ".jpg";

//            //初始化OssService类，参数分别是Content，accessKeyId，accessKeySecret，endpoint，bucketName（后4个参数是您自己阿里云Oss中参数）
//            OssService ossService = new OssService(getApplicationContext(), OSS_ACCESS_KEY, OSS_ACCESS_SECRET, OSS_ENDPOINT, OSS_BUCKET_NAME);
//            //初始化OSSClient
//            ossService.initOSSClient();
//            //开始上传，参数分别为content，上传的文件名filename，上传的文件路径filePath
//            ossService.beginupload(getApplication(), filename, imagePath);


            //更新url
            myPortrait = basePath + filename;
            Log.d("头像", myPortrait);
            //更新bundle
            bundle.remove(MY_PORTRAIT);
            bundle.putString(MY_PORTRAIT, myPortrait);

            updatePortrait(imagePath, filename);
        }else{
            Toast.makeText(this,"Failed to get image",Toast.LENGTH_SHORT).show();
        }

    }

    public void updatePortrait(String imagePath, String myPortrait){

        Factory.getInstance().getThreadPool().execute(() -> {
            String result = NetUtils.postImage(imagePath, uploadPortraitUrl, myPortrait);
            if(result != null && !result.equals("error")){
                String updateResult = NetUtils.postKeyValue("id", myId, "portrait", basePath + myPortrait, updatePortraitUrl);
                Log.d("myPortrait", myPortrait);


                if(updateResult != null){
                    runOnUiThread(() -> Toast.makeText(getApplication(), "更换头像成功", Toast.LENGTH_SHORT).show());
                }else{
                    runOnUiThread(() -> Toast.makeText(getApplication(), "服务器错误，请重试", Toast.LENGTH_SHORT).show());
                }
            }

//
//            //发送服务器

        });
    }

    public void showToast(String message){
        runOnUiThread(() -> Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }
}
