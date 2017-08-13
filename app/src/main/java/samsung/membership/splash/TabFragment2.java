package samsung.membership.splash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by yumin on 2017-07-29.
 */


public class TabFragment2 extends Fragment {

    private GridView gridView;
    private int count = 0;
    private int[] image = new int[]{};
    private GestureAdapter2 adapter2;

    final List<Bitmap> imageLists = new ArrayList<Bitmap>();

    int img[] = {};
    UnderlineAnim underlineAnim;

    private final int GALLERY_CODE = 2000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        imageLists.add(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.addbutton));
        adapter2 = new GestureAdapter2(getApplicationContext(), R.layout.gesture_item, imageLists, getActivity());
        underlineAnim = (UnderlineAnim)view.findViewById(R.id.tab2_underline);
        Button addButton = (Button)view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastSnackBar(v);
            }
        });
        return view;
    }

    public boolean getInstagramPackage() {
        boolean isExist = false;

        PackageManager pkgMgr = getActivity().getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith("com.instagram.android")){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }

    public boolean getFacebookPackage() {
        boolean isExist = false;

        PackageManager pkgMgr = getActivity().getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith("com.facebook.katana")){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }


    private void ToastSnackBar(View view) {
        Snackbar snackbar = Snackbar.make(view,"TEST", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = inflater.inflate(R.layout.snack_bar, null);
        snackView.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
            }
        });
        snackView.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getInstagramPackage()) {
                    Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    String url = "market://details?id=" + "com.instagram.android";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });
        snackView.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFacebookPackage()) {
                    Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    String url = "market://details?id=" + "com.facebook.katana";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });
        layout.addView(snackView, 0);
        snackbar.show();
    }

    public void StartAnim() {
        underlineAnim.AnimStart();
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BusProvider2.getInstance().register(this);
    }

    public void onDestroyView() {
        BusProvider2.getInstance().unregister(this);
        super.onDestroyView();

    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent){
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) if(requestCode == 2000) SendPicture(data);
    }

    private void SendPicture(Intent data) {

        Uri imgUri = data.getData();
        String imagePath = getRealPathFromURI(imgUri); // path 경로
        Log.d("TAG", imagePath);
        /*Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,displayMetrics.widthPixels/2,displayMetrics.widthPixels/2);*/
        int degree = ImageUtil.GetExifOrientation(imagePath);
        Bitmap resizeBitmap = ImageUtil.loadBackgroundBitmap(getApplicationContext(), imagePath);
        Bitmap rotateBitmap = ImageUtil.GetRotatedBitmap(resizeBitmap, degree);
        Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(rotateBitmap);
        File file = new File(imagePath);
        Log.d("TAG", "round : " + roundBitmap.getByteCount());
        Log.d("TAG", "file : " + file.length());
        imageLists.add(0,roundBitmap);
        //bitmap.recycle();
        adapter2.notifyDataSetChanged();

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}

class GestureAdapter2 extends BaseAdapter {

    private final int GALLERY_CODE = 2000;

    Context context;
    int layout;
    int count;
    Context activityContext;
    List<Bitmap> list;
    LayoutInflater inf;

    public GestureAdapter2(Context context, int layout, List<Bitmap> list, Context getActivity) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.activityContext = getActivity;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, parent,false);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            convertView.setLayoutParams(new GridView.LayoutParams(((int)(displayMetrics.widthPixels/2)),((int)(displayMetrics.widthPixels/2))));
            ImageView iv = (ImageView) convertView.findViewById(R.id.gesture_image);
            iv.setImageResource(R.drawable.addbutton);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    ((AddActivity2) activityContext).startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
                }
            });
            Log.d("TAG","CALLED");
        } else {
            ImageView iv = (ImageView) convertView.findViewById(R.id.gesture_image);
            iv.setImageBitmap(list.get(position));
            count++;
        }
        return convertView;
    }
}

final class BusProvider2 {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider2() {
        // No instances.
    }
}




