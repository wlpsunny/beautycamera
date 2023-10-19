package com.eagleapps.beautycam.act;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eagleapps.beautycam.MyAplication;
import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.adapter.ColorsAdapt;
import com.eagleapps.beautycam.adapter.ColorsAdapt.OnRecyclerViewItemClickListener;
import com.eagleapps.beautycam.adapter.ColorsAdapt2;
import com.eagleapps.beautycam.adapter.CommonAdapt;
import com.eagleapps.beautycam.adapter.EffectAdapt;
import com.eagleapps.beautycam.adapter.FontAdapt;
import com.eagleapps.beautycam.bitmap.BitmapLoad;
import com.eagleapps.beautycam.bitmap.BitmapProcess;
import com.eagleapps.beautycam.otherView.DisplayUtil;
import com.eagleapps.beautycam.otherView.NonFilter;
import com.eagleapps.beautycam.otherView.StickerView;
import com.eagleapps.beautycam.otherView.Utils;
import com.eagleapps.beautycam.remote.SupporterClass;
import com.yalantis.ucrop.UCrop.Options;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;
import me.grantland.widget.AutofitTextView;

public class EditLastAct extends BaseAct {
    public AutofitTextView afltext;
    public boolean blurFinished;
    public Bitmap bmblur;
    public Bitmap bmmain;
    public Bitmap bmmask;
    public int contrast = 0;
    public EditText edtext;
    public EffectAdapt effectAdapter;
    public int exposure = 6;
    public String filter = "thumb_effect_00001";
    public int highlight = 0;
    public boolean isNext;
    public ImageView ivalign;
    public ImageView ivblur;
    public ImageView ivcircle;
    public int kindEdit = 0;
    public int lastTouchedPositionX;
    public int lastTouchedPositionY;
    public String[] listColor = {"#000000", "#515A5A", "#AAB7B8", "#FFFFFF", "#873600", "#DC7633", "#9C640C", "#9A7D0A", "#0E6655", "#1D8348", "#52BE80", "#48C9B0", "#1A5276", "#2980B9", "#5DADE2", "#5B2C6F", "#A569BD", "#C39BD3", "#7B241C", "#C0392B", "#E74C3C", "#FA8072", "#F08080"};
    public String[] listItem;
    public LinearLayout llchange;
    public boolean mShowLoader = true;
    public MenuItem menuItemCrop;
    public Drawable menuItemCropIconDone;
    public RelativeLayout rlblur;
    public RelativeLayout rlphoto;
    public RelativeLayout rlslider;
    public RelativeLayout rltext;
    public RecyclerView rvselect;
    public RecyclerView rvtext;
    public String savePath;
    public int shadow = 0;
    public int sharpen = 0;
    public int temperature = 6;
    public TextView toolbarTitle;
    public int type = 0;
    public int vignette = 0;
    LinearLayout llCatEffect;
    TextView txtBrushE;
    TextView txtColorE;
    TextView txtFrameE;
    String oldSavedFileName;
    Exception e;
    private int center;
    private boolean checkTouch;
    private ColorsAdapt2 colorAdapter2;
    private String colorsample;
    private CommonAdapt commonAdapter;
    private int dis;
    private boolean firstTouch = false;
    private String fontsample;
    private GPUImageView gpuview;
    private ImageView icabc;
    private ImageView icadjust;
    private ImageView icblur;
    private ImageView iceffect;
    private ImageView icsnap;
    private ImageView icst;
    private ImageView ictext;
    private ImageView ivchangecontrast;
    private ImageView ivchangeexposure;
    private ImageView ivchangehighlight;
    private ImageView ivchangeshadow;
    private ImageView ivchangesharpen;
    private ImageView ivchangetemperature;
    private ImageView ivchangevignette;
    private ImageView ivframe;
    private ImageView ivphoto;
    private ImageView ivchangetext;
    private ImageView ivchangefont;
    private ImageView ivchangecolor;
    private String[] listfont;
    private LinearLayout llcontrol;
    private int mToolBarHeight;
    private int mToolbarColor;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;
    private int mToolbarWidgetColor1;
    private Drawable menuItemCropIconSave;
    private int precontrast = 0;
    private int preexposure = 6;
    private int prehighlight = 0;
    private int preshadow = 0;
    private int presharpen = 0;
    private int pretemperature = 6;
    private int prevignette = 0;
    private SeekBar sbslider;
    private String textsample;
    private long timetouch;
    private TextView tvslider;
    private int widthScreen;
    private int wthumb;
    private Uri savedImageUri;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    static public void notifyMediaUpdateSer(Context context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, new String[]{"image/jpeg"}, null);
    }

    public void onCreate(Bundle bundle) {
        String str = "imagePath";
        super.onCreate(bundle);
        setContentView(R.layout.activity_edit_last);
        setupViews(getIntent());
        RelativeLayout adViewBanner = findViewById(R.id.rel_banner_ads);
        SupporterClass.loadBannerAds(adViewBanner, EditLastAct.this);
        isNext = false;
        widthScreen = DisplayUtil.getDisplayWidthPixels(this);
        try {
            bmmain = BitmapFactory.decodeFile(getIntent().getExtras().getString(str));
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_done);
            menuItemCropIconDone = drawable;
            drawable.mutate();
//            menuItemCropIconDone.setColorFilter(mToolbarWidgetColor1, Mode.SRC_ATOP);
            Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_save1);
            menuItemCropIconSave = drawable2;
            drawable2.mutate();
//            menuItemCropIconSave.setColorFilter(mToolbarWidgetColor, Mode.SRC_ATOP);
            rlphoto = (RelativeLayout) findViewById(R.id.rlphoto);
            LayoutParams layoutParams = new LayoutParams(bmmain.getWidth(), bmmain.getHeight());
            layoutParams.addRule(13);
            rlphoto.setLayoutParams(layoutParams);
            GPUImageView gPUImageView = (GPUImageView) findViewById(R.id.gpuview);
            gpuview = gPUImageView;
            gPUImageView.setImage(bmmain);
            bmmask = BitmapLoad.loadFromResource(this, new int[]{bmmain.getWidth(), bmmain.getHeight()}, R.drawable.mask);
            blurFinished = true;
            ivphoto = (ImageView) findViewById(R.id.ivphoto);
            rvselect = (RecyclerView) findViewById(R.id.rvselect);
            ivframe = (ImageView) findViewById(R.id.ivframe);
            rvselect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

            ImageView imageView2 = (ImageView) findViewById(R.id.icadjust);
            icadjust = imageView2;
            imageView2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeAdjust();
                    llCatEffect.setVisibility(View.GONE);
                    kindEdit = 3;
                    type = 1;

                    setTextSlider(exposure);
                    setChange();

                    llchange.setVisibility(View.VISIBLE);
                    rlslider.setVisibility(View.VISIBLE);
                    llcontrol.setVisibility(View.GONE);
                    rvselect.setVisibility(View.GONE);
                    menuItemCrop.setIcon(menuItemCropIconDone);
                    clickable(false);
                }
            });
            ImageView imageView3 = (ImageView) findViewById(R.id.iceffect);
            iceffect = imageView3;
            imageView3.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeEffect();
                    type = 0;
                    llCatEffect.setVisibility(View.GONE);
                    toolbarTitle.setText(getResources().getString(R.string.effects));
                    loadEffect();
                }
            });
            ImageView imageView4 = (ImageView) findViewById(R.id.icsnap);
            icsnap = imageView4;
            imageView4.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeSnap();
                    llCatEffect.setVisibility(View.GONE);
                    toolbarTitle.setText(getResources().getString(R.string.stickers));
                    loadSnap();
                }
            });
            ImageView imageView5 = (ImageView) findViewById(R.id.icst);
            icst = imageView5;
            imageView5.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    llCatEffect.setVisibility(View.GONE);
                    toolbarTitle.setText(getResources().getString(R.string.stickers));
                    loadSt();
                }
            });
            ImageView imageView6 = (ImageView) findViewById(R.id.icabc);
            icabc = imageView6;
            imageView6.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeabc();
                    llCatEffect.setVisibility(View.GONE);
                    toolbarTitle.setText(getResources().getString(R.string.captions));
                    loadABC();
                }
            });
            ImageView imageView7 = (ImageView) findViewById(R.id.ictext);
            ictext = imageView7;
            imageView7.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeText();
                    kindEdit = 1;
                    toolbarTitle.setText(getResources().getString(R.string.addtext));
                    rltext.setVisibility(View.VISIBLE);
                    rvtext.setVisibility(View.GONE);
                    edtext.setVisibility(View.VISIBLE);
                    edtext.requestFocus();
                    String str = "";
                    afltext.setText(str);
                    edtext.setText(str);
                    ivalign.setTag(Integer.valueOf(1));
                    ivalign.setImageResource(R.drawable.ic_center_selected);
                    ivcircle.setTag(Integer.valueOf(0));
                    ivcircle.setImageResource(R.drawable.ic_fill);
                    afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
                    clickable(false);
                    loadSampleText("#ffffff", Utils.FONT_MAIN, str);
                    ((InputMethodManager) getSystemService("input_method")).showSoftInput(edtext, 1);
                    menuItemCrop.setIcon(menuItemCropIconDone);
                }
            });
            ImageView imageView8 = (ImageView) findViewById(R.id.icblur);
            icblur = imageView8;
            imageView8.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    kindEdit = 2;
                    toolbarTitle.setText(getResources().getString(R.string.blurphoto));
                    LayoutParams layoutParams = new LayoutParams(rlphoto.getWidth(), rlphoto.getHeight());
                    layoutParams.setMargins(Utils.getRelativeLeft(rlphoto), Utils.getRelativeTop(rlphoto), 0, 0);
                    ivblur.setLayoutParams(layoutParams);
                    rlblur.setVisibility(View.VISIBLE);
                    ivblur.setVisibility(View.VISIBLE);
                    ivblur.setImageBitmap(bmmain);
                    clickable(false);
                    menuItemCrop.setIcon(menuItemCropIconDone);
                }
            });
            rltext = (RelativeLayout) findViewById(R.id.rltext);
            rvtext = (RecyclerView) findViewById(R.id.rvtext);
            afltext = (AutofitTextView) findViewById(R.id.afltext);
            edtext = (EditText) findViewById(R.id.edtext);
            ivchangetext = findViewById(R.id.ivchangetext);
            ivchangetext.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeChangeText();
                    rvtext.setVisibility(View.GONE);
                    edtext.setVisibility(View.VISIBLE);
                    edtext.requestFocus();
                    ((InputMethodManager) getSystemService("input_method")).showSoftInput(edtext, 1);
                }
            });
            edtext.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/rubik_regular.ttf"));
            edtext.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable editable) {
                }

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    afltext.setText(charSequence.toString());
                }
            });
            ivchangefont = findViewById(R.id.ivchangefont);
            ivchangefont.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeChangeFont();
                    rvtext.setVisibility(View.VISIBLE);
                    edtext.setVisibility(View.GONE);
                    closeKeyboard();
                    rvtext.setLayoutManager(new LinearLayoutManager(EditLastAct.this, 0, false));
                    loadFont();
                }
            });
            ivchangecolor = findViewById(R.id.ivchangecolor);
            ivchangecolor.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeChangeColor();
                    rvtext.setVisibility(View.VISIBLE);
                    edtext.setVisibility(View.GONE);
                    closeKeyboard();
                    rvtext.setLayoutManager(new LinearLayoutManager(EditLastAct.this, 0, false));
                    ColorsAdapt colorAdapter = new ColorsAdapt(listColor, EditLastAct.this);
                    rvtext.setAdapter(colorAdapter);
                    colorAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                        public void onItemClick(View view, String str) {
                            String str2 = "";
                            loadSampleText(str, str2, str2);
                        }
                    });
                }
            });
            ImageView imageView9 = (ImageView) findViewById(R.id.ivalign);
            ivalign = imageView9;
            imageView9.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    setAlignText();
                }
            });
            ImageView imageView10 = (ImageView) findViewById(R.id.ivcircle);
            ivcircle = imageView10;
            imageView10.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    setCircleText();
                }
            });
            rlblur = (RelativeLayout) findViewById(R.id.rlblur);
            ImageView imageView11 = (ImageView) findViewById(R.id.ivblur);
            ivblur = imageView11;
            imageView11.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    lastTouchedPositionX = (int) motionEvent.getX();
                    lastTouchedPositionY = (int) motionEvent.getY();
                    refreshImageView();
                    return true;
                }
            });
            ivchangeexposure = (ImageView) findViewById(R.id.ivchangeexposure);
            ivchangecontrast = (ImageView) findViewById(R.id.ivchangecontrast);
            ivchangesharpen = (ImageView) findViewById(R.id.ivchangesharpen);
            ivchangetemperature = (ImageView) findViewById(R.id.ivchangetemperature);
            ivchangehighlight = (ImageView) findViewById(R.id.ivchangehighlight);
            ivchangeshadow = (ImageView) findViewById(R.id.ivchangeshadow);
            ivchangevignette = (ImageView) findViewById(R.id.ivchangevignette);
            llcontrol = (LinearLayout) findViewById(R.id.llcontrol);
            llchange = (LinearLayout) findViewById(R.id.llchange);
            rlslider = (RelativeLayout) findViewById(R.id.rlslider);
            sbslider = (SeekBar) findViewById(R.id.sbslider);
            tvslider = (TextView) findViewById(R.id.tvslider);
            loadPointforSlider();
            ivchangeexposure.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeXposure();
                    type = 1;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.exposure);
                    setChange();
                }
            });
            ivchangecontrast.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeContrast();
                    type = 2;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.contrast);
                    setChange();
                }
            });
            ivchangesharpen.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeSharpen();
                    type = 3;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.sharpen);
                    setChange();
                }
            });
            ivchangehighlight.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeHighlight();
                    type = 4;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.highlight);
                    setChange();
                }
            });
            ivchangeshadow.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeShadow();
                    type = 5;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.shadow);
                    setChange();
                }
            });
            ivchangetemperature.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeTemperature();
                    type = 6;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.temperature);
                    setChange();
                }
            });
            ivchangevignette.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    activeVignette();
                    type = 7;
                    EditLastAct editActivity = EditLastAct.this;
                    editActivity.setTextSlider(editActivity.vignette);
                    setChange();
                }
            });
            llCatEffect = (LinearLayout) findViewById(R.id.llCatEffect);
            txtColorE = (TextView) findViewById(R.id.txtColorE);
            txtBrushE = (TextView) findViewById(R.id.txtBrushE);
            txtFrameE = (TextView) findViewById(R.id.txtFrameE);
            loadEffect();
            txtColorE.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    txtColorE.setTypeface(txtColorE.getTypeface(), 1);
                    txtBrushE.setTypeface(null, 0);
                    txtFrameE.setTypeface(null, 0);
                    loadFrame(1);
                }
            });
            txtBrushE.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    txtColorE.setTypeface(null, 0);
                    txtBrushE.setTypeface(txtBrushE.getTypeface(), 1);
                    txtFrameE.setTypeface(null, 0);
                    loadFrame(2);
                }
            });
            txtFrameE.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    txtColorE.setTypeface(null, 0);
                    txtBrushE.setTypeface(null, 0);
                    txtFrameE.setTypeface(txtFrameE.getTypeface(), 1);
                    loadFrame(3);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            try {
                finish();
                File file = new File(pathtoSave());
                if (!file.exists()) {
                    file.mkdirs();
                }
                StringBuilder sb = new StringBuilder();
                sb.append(file.getAbsolutePath());
                sb.append(File.separator);
                sb.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                sb.append(System.currentTimeMillis());
                sb.append(".jpg");
                Utils.copyFile(new File(getIntent().getExtras().getString(str)), new File(sb.toString()));
                savePath = getIntent().getExtras().getString(str);
                Intent intent = new Intent().setClass(this, ShareImageAct.class);
                intent.setData(Uri.parse(savePath));
                startActivity(intent);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void activeVignette() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow);
        ivchangevignette.setImageResource(R.drawable.iceditvignette_selected);
    }

    private void activeTemperature() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth_selected);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow);
        ivchangevignette.setImageResource(R.drawable.iceditvignette);
    }

    private void activeShadow() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow_selected);
        ivchangevignette.setImageResource(R.drawable.iceditvignette);
    }

    private void activeHighlight() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights_selected);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow);
        ivchangevignette.setImageResource(R.drawable.iceditvignette);
    }

    private void activeSharpen() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren_selected);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow);
        ivchangevignette.setImageResource(R.drawable.iceditvignette);
    }

    private void activeContrast() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast_selected);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow);
        ivchangevignette.setImageResource(R.drawable.iceditvignette);
    }

    private void activeXposure() {
        ivchangeexposure.setImageResource(R.drawable.iceditlux_selected);
        ivchangecontrast.setImageResource(R.drawable.iceditcontrast);
        ivchangesharpen.setImageResource(R.drawable.iceditshapren);
        ivchangetemperature.setImageResource(R.drawable.iceditwarmth);
        ivchangehighlight.setImageResource(R.drawable.icedithighlights);
        ivchangeshadow.setImageResource(R.drawable.iceditshadow);
        ivchangevignette.setImageResource(R.drawable.iceditvignette);
    }

    private void activeChangeColor() {
        ivchangetext.setImageResource(R.drawable.ic_text_t);
        ivchangefont.setImageResource(R.drawable.ic_text_a);
        ivchangecolor.setImageResource(R.drawable.ic_color_selected);
    }

    private void activeChangeFont() {
        ivchangetext.setImageResource(R.drawable.ic_text_t);
        ivchangefont.setImageResource(R.drawable.ic_text_a_selected);
        ivchangecolor.setImageResource(R.drawable.ic_color);

    }

    private void activeChangeText() {
        ivchangetext.setImageResource(R.drawable.ic_text_t_selected);
        ivchangefont.setImageResource(R.drawable.ic_text_a);
        ivchangecolor.setImageResource(R.drawable.ic_color);
    }

    private void activeText() {
        iceffect.setImageResource(R.drawable.ic_brightness);
        icsnap.setImageResource(R.drawable.ic_sticker);
        ictext.setImageResource(R.drawable.ic_edit_selected);
        icabc.setImageResource(R.drawable.ic_ic_sticker_1);
        icadjust.setImageResource(R.drawable.ic_adjustment);
    }

    private void activeabc() {
        iceffect.setImageResource(R.drawable.ic_brightness);
        icsnap.setImageResource(R.drawable.ic_sticker);
        ictext.setImageResource(R.drawable.ic_edit);
        icabc.setImageResource(R.drawable.ic_ic_sticker_1_selected);
        icadjust.setImageResource(R.drawable.ic_adjustment);
    }

    private void activeSnap() {
        iceffect.setImageResource(R.drawable.ic_brightness);
        icsnap.setImageResource(R.drawable.ic_sticker_selected);
        ictext.setImageResource(R.drawable.ic_edit);
        icabc.setImageResource(R.drawable.ic_sticker);
        icadjust.setImageResource(R.drawable.ic_adjustment);
    }

    private void activeEffect() {
        iceffect.setImageResource(R.drawable.ic_brightness_selected);
        icsnap.setImageResource(R.drawable.ic_sticker);
        ictext.setImageResource(R.drawable.ic_edit);
        icabc.setImageResource(R.drawable.ic_ic_sticker_1);
        icadjust.setImageResource(R.drawable.ic_adjustment);
    }

    private void activeAdjust() {
        iceffect.setImageResource(R.drawable.ic_brightness);
        icsnap.setImageResource(R.drawable.ic_sticker);
        ictext.setImageResource(R.drawable.ic_edit);
        icabc.setImageResource(R.drawable.ic_ic_sticker_1);
        icadjust.setImageResource(R.drawable.ic_adjustment_selected);
    }

    public void setAlignText() {
        Object tag = ivalign.getTag();
        Integer valueOf = Integer.valueOf(1);
        boolean equals = tag.equals(valueOf);
        Integer valueOf2 = Integer.valueOf(2);
        Integer valueOf3 = Integer.valueOf(3);
        if (equals) {
            afltext.setGravity(3);
            ivalign.setImageResource(R.drawable.ic_left_selected);
            ivalign.setTag(valueOf2);
        } else if (ivalign.getTag().equals(valueOf2)) {
            afltext.setGravity(5);
            ivalign.setImageResource(R.drawable.ic_right_selected);
            ivalign.setTag(valueOf3);
        } else if (ivalign.getTag().equals(valueOf3)) {
            afltext.setGravity(17);
            ivalign.setImageResource(R.drawable.ic_center_selected);
            ivalign.setTag(valueOf);
        }
    }

    public void setCircleText() {
        try {
            if (ivcircle.getTag().equals(Integer.valueOf(0))) {
                ivcircle.setImageResource(R.drawable.ic_fill_selected);
                ivcircle.setTag(Integer.valueOf(1));
                afltext.setShadowLayer(1.6f, 4.0f, 4.0f, -1);
            } else if (ivcircle.getTag().equals(Integer.valueOf(1))) {
                ivcircle.setTag(Integer.valueOf(0));
                ivcircle.setImageResource(R.drawable.ic_fill);
                afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClick() {
        commonAdapter.setOnItemClickListener(new CommonAdapt.OnRecyclerViewItemClickListener() {
            public void onItemClick(View view, String str) {
                if (rltext.getVisibility() != 8 || rlblur.getVisibility() != 8 || llchange.getVisibility() != 8) {
                    return;
                }
                if (str.contains("frames_th")) {
                    addFrame(str);
                } else if (str.contains("brush_frame_th")) {
                    addFrame(str);
                } else if (str.contains("stickers")) {
                    addStickerItem(str);
                } else if (str.contains("texts")) {
                    addABC(str);
                } else if (str.contains("snap_")) {
                    addSnap(str);
                } else if (str.contains("st_")) {
                    addSnap(str);
                }
            }
        });
    }

    public void effectClick() {
        effectAdapter.setOnItemClickListener(new EffectAdapt.OnRecyclerViewItemClickListener() {
            public void onItemClick(View view, String str) {
                if (rltext.getVisibility() == 8 && rlblur.getVisibility() == 8 && llchange.getVisibility() == 8 && str.contains("thumb_effect_")) {
                    filter = str;
                    gpuEffect();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);
        MenuItem findItem = menu.findItem(R.id.menu_loader);
        Drawable icon = findItem.getIcon();
        if (icon != null) {
            try {
                icon.mutate();
                icon.setColorFilter(mToolbarWidgetColor, Mode.SRC_ATOP);
                findItem.setIcon(icon);
            } catch (IllegalStateException e) {
                Log.i("Photos to Collage", e.getMessage());
            }
            ((Animatable) findItem.getIcon()).start();
        }
        MenuItem findItem2 = menu.findItem(R.id.menu_crop);
        menuItemCrop = findItem2;
        findItem2.setIcon(menuItemCropIconSave);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        boolean z;
        int i = kindEdit;
        if (i == 0) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                isNext = true;
                clickSave();
            } else if (menuItem.getItemId() == 16908332) {
                isNext = false;
                finish();
            }
        } else if (i == 1) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                String charSequence = afltext.getText().toString();
                hideKeyboardFrom(EditLastAct.this, afltext);
                textsample = charSequence;
                if (!charSequence.equals("")) {
                    afltext.setDrawingCacheEnabled(true);
                    afltext.setDrawingCacheQuality(1048576);
                    Bitmap createBitmap = Bitmap.createBitmap(afltext.getDrawingCache());
                    afltext.setDrawingCacheEnabled(false);
                    int i2 = 0;
                    while (true) {
                        if (i2 >= rlphoto.getChildCount()) {
                            z = false;
                            break;
                        }
                        try {
                            if ((rlphoto.getChildAt(i2) instanceof StickerView) && ((StickerView) rlphoto.getChildAt(i2)).isEdit()) {
                                try {
                                    ((StickerView) rlphoto.getChildAt(i2)).setEdit(false);
                                    ((StickerView) rlphoto.getChildAt(i2)).setText(textsample);
                                    ((StickerView) rlphoto.getChildAt(i2)).setColor(colorsample);
                                    ((StickerView) rlphoto.getChildAt(i2)).setFont(fontsample);
                                    ((StickerView) rlphoto.getChildAt(i2)).setAlign(((Integer) ivalign.getTag()).intValue());
                                    ((StickerView) rlphoto.getChildAt(i2)).setCircle(((Integer) ivcircle.getTag()).intValue());
                                    ((StickerView) rlphoto.getChildAt(i2)).setWaterMark(createBitmap, false);
                                    z = true;
                                    break;
                                } catch (Exception unused) {
                                    continue;
                                }
                            }
                        } catch (Exception e) {
                            Log.i("Photos to Collage", e.getMessage());
                            i2++;
                        }
                        i2++;
                    }
                    if (!z) {
                        addText(createBitmap);
                    }
                }
            } else if (menuItem.getItemId() == 16908332) {
                resetEditSticker();
            }
            rltext.setVisibility(View.GONE);
            kindEdit = 0;
            toolbarTitle.setText(getResources().getString(R.string.editphoto));
            clickable(true);
            closeKeyboard();
        } else if (i == 2) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                gpuview.setImage(bmblur);
            }
            rlblur.setVisibility(View.GONE);
            ivblur.setVisibility(View.GONE);
            kindEdit = 0;
            toolbarTitle.setText(getResources().getString(R.string.editphoto));
            clickable(true);
        } else if (i == 3) {
            if (menuItem.getItemId() == R.id.menu_crop) {
                preexposure = exposure;
                precontrast = contrast;
                prehighlight = highlight;
                int i3 = shadow;
                preshadow = i3;
                presharpen = i3;
                pretemperature = temperature;
                prevignette = vignette;
            } else {
                exposure = preexposure;
                contrast = precontrast;
                highlight = prehighlight;
                int i4 = preshadow;
                shadow = i4;
                sharpen = i4;
                temperature = pretemperature;
                vignette = prevignette;
                gpuEffect();
            }
            llcontrol.setVisibility(View.VISIBLE);
            rvselect.setVisibility(View.VISIBLE);
            llchange.setVisibility(View.GONE);
            rlslider.setVisibility(View.GONE);
            kindEdit = 0;
            type = 0;
            toolbarTitle.setText(getResources().getString(R.string.editphoto));
            clickable(true);
        }
        menuItemCrop.setIcon(menuItemCropIconSave);
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            int i2 = kindEdit;
            if (i2 == 0) {
                isNext = false;
                finish();
            } else if (i2 == 1) {
                resetEditSticker();
                kindEdit = 0;
                rltext.setVisibility(View.GONE);
                toolbarTitle.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                menuItemCrop.setIcon(menuItemCropIconSave);
                return false;
            } else if (i2 == 2) {
                kindEdit = 0;
                rlblur.setVisibility(View.GONE);
                ivblur.setVisibility(View.GONE);
                toolbarTitle.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                menuItemCrop.setIcon(menuItemCropIconSave);
                return false;
            } else if (i2 == 3) {
                exposure = preexposure;
                contrast = precontrast;
                highlight = prehighlight;
                int i3 = preshadow;
                shadow = i3;
                sharpen = i3;
                temperature = pretemperature;
                vignette = prevignette;
                gpuEffect();
                kindEdit = 0;
                type = 0;
                llcontrol.setVisibility(View.VISIBLE);
                rvselect.setVisibility(View.VISIBLE);
                llchange.setVisibility(View.GONE);
                rlslider.setVisibility(View.GONE);


                toolbarTitle.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                menuItemCrop.setIcon(menuItemCropIconSave);
                return false;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void setupViews(Intent intent) {
        mToolBarHeight = (int) TypedValue.applyDimension(1, 50.0f, getResources().getDisplayMetrics());
        mToolbarColor = intent.getIntExtra(Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, R.color.screen_bckground));
        int color = ContextCompat.getColor(this, R.color.white);
        String str = Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR;
        mToolbarWidgetColor = intent.getIntExtra(str, color);
        mToolbarWidgetColor1 = intent.getIntExtra(str, ContextCompat.getColor(this, R.color.white));
        String stringExtra = intent.getStringExtra(Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        mToolbarTitle = stringExtra;
        mToolbarTitle = !TextUtils.isEmpty(stringExtra) ? mToolbarTitle : getResources().getString(R.string.editphoto);
        setupAppBar();
    }

    private void setupAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle = textView;
        toolbarTitle.setText(mToolbarTitle);
        Drawable mutate = ContextCompat.getDrawable(this, R.drawable.ic_back).mutate();
//        mutate.setColorFilter(mToolbarWidgetColor, Mode.SRC_ATOP);
        toolbar.setNavigationIcon(mutate);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
        mShowLoader = false;
    }

    private void clickSave() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != 0) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_write_storage_rationale), 102);
        } else {
            new saveAndGo().execute(new Void[0]);
        }
    }

    private Bitmap viewToBitmap(View view) {
        try {
            Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            view.draw(new Canvas(createBitmap));
            return createBitmap;
        } finally {
            view.destroyDrawingCache();
        }
    }

    public String savePhotoSIRA() {
        String r0 = "Photos to Collage";
        Bitmap bitmap = gpuview.getGPUImage().getBitmapWithFilterApplied();
        ivphoto.setImageBitmap(bitmap);
        Bitmap bitmapSave = viewToBitmap(rlphoto);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Beauty Camera");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.d("", "Can't create directory to save image.");
            }
        }
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("Photo_");
        sb.append(System.currentTimeMillis());
        String sb2 = sb.toString();

        StringBuilder sb4 = new StringBuilder();
        sb4.append(sb2);
        sb4.append(".jpg");
        str = sb4.toString();

        StringBuilder sb5 = new StringBuilder();
        sb5.append(file.getPath());
        sb5.append(File.separator);
        sb5.append(str);
        String name = sb5.toString();
        File fileName = new File(name);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentResolver contentResolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, String.valueOf(str));
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + File.separator + getString(R.string.app_folder));

                Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                FileOutputStream fos = (FileOutputStream) contentResolver.openOutputStream(Objects.requireNonNull(uri));
                bitmapSave.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);
                if (uri != null)
                    savedImageUri = uri;

                Toast.makeText(EditLastAct.this, getString(R.string.txt_file_saved_successfully), Toast.LENGTH_SHORT).show();
                return sb5.toString();
            } else {
                File file1 = new File(String.valueOf(fileName));
                if (oldSavedFileName != null) {
                    File oldFile = new File(file.getPath(), oldSavedFileName);
                    if (oldFile.exists()) oldFile.delete();
                }
                oldSavedFileName = String.valueOf(fileName);
                try {
                    FileOutputStream out = new FileOutputStream(file1);
                    bitmapSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    Uri uri = addEditImageToMedia(file.getAbsolutePath());
                    if (uri != null)
                        Toast.makeText(EditLastAct.this, getString(R.string.txt_file_saved_successfully), Toast.LENGTH_SHORT).show();
                    savedImageUri = uri;
                    notifyMediaUpdateSer(EditLastAct.this, file.getPath());


                } catch (Exception e) {
                    Log.e("error", "" + e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("error", "" + e.getMessage());
            return null;
        }
        return sb5.toString();
    }

    private Uri addEditImageToMedia(String filePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private String pathtoSave() {
        String externalStorageState = Environment.getExternalStorageState();
        String str = "mounted";
        if (VERSION.SDK_INT >= 29) {
            if (str.equals(externalStorageState)) {
                StringBuilder sb = new StringBuilder();
                sb.append(getExternalFilesDir(null).getAbsolutePath());
                sb.append(File.separator);
                sb.append(Utils.ALBUM);
                return sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getFilesDir());
            sb2.append(File.separator);
            sb2.append(Utils.ALBUM);
            return sb2.toString();
        } else if (str.equals(externalStorageState)) {
            try {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(Environment.getExternalStorageDirectory());
                sb3.append(File.separator);
                sb3.append("Pictures");
                sb3.append(File.separator);
                sb3.append(Utils.ALBUM);
                return sb3.toString();
            } catch (Exception unused) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(Environment.getExternalStorageDirectory());
                sb4.append(File.separator);
                sb4.append(Utils.ALBUM);
                return sb4.toString();
            }
        } else {
            StringBuilder sb5 = new StringBuilder();
            sb5.append(getFilesDir());
            sb5.append(File.separator);
            sb5.append(Utils.ALBUM);
            return sb5.toString();
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 102) {
            super.onRequestPermissionsResult(i, strArr, iArr);
        } else if (iArr[0] == 0) {
            clickSave();
        }
    }

    public void addStickerItem(String str) {
        Bitmap bitmap;
        StickerView stickerView = new StickerView((Context) this, false);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        rlphoto.addView(stickerView, layoutParams);
        Bitmap loadFromAsset = BitmapLoad.loadFromAsset(this, new int[]{512, 512}, str.replace("_th", ""));
        if (loadFromAsset.getWidth() >= loadFromAsset.getHeight()) {
            int i = widthScreen / 2;
            bitmap = BitmapProcess.resizeBitmap(loadFromAsset, i, (loadFromAsset.getHeight() * i) / loadFromAsset.getWidth());
        } else {
            int i2 = widthScreen / 2;
            bitmap = BitmapProcess.resizeBitmap(loadFromAsset, (loadFromAsset.getWidth() * i2) / loadFromAsset.getHeight(), i2);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(str);
    }

    public void addFrame(String str) {
        ivframe.setImageBitmap(BitmapLoad.loadFromAsset(this, new int[]{1440, 1440}, str.replace("_th", "").replace(".jpg", ".png")));
    }

    public void addABC(String str) {
        Bitmap bitmap;
        StickerView stickerView = new StickerView((Context) this, true);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        rlphoto.addView(stickerView, layoutParams);
        Bitmap loadFromAsset = BitmapLoad.loadFromAsset(this, new int[]{720, 720}, str.replace("_th", ""));
        if (loadFromAsset.getWidth() >= loadFromAsset.getHeight()) {
            int i = widthScreen / 2;
            bitmap = BitmapProcess.resizeBitmap(loadFromAsset, i, (loadFromAsset.getHeight() * i) / loadFromAsset.getWidth());
        } else {
            int i2 = widthScreen / 2;
            bitmap = BitmapProcess.resizeBitmap(loadFromAsset, (loadFromAsset.getWidth() * i2) / loadFromAsset.getHeight(), i2);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(str);
    }

    public void addSnap(String str) {
        Bitmap bitmap;
        StickerView stickerView = new StickerView((Context) this, false);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        rlphoto.addView(stickerView, layoutParams);
        Bitmap loadFromAsset = BitmapLoad.loadFromAsset(this, new int[]{512, 512}, str);
        if (loadFromAsset.getWidth() >= loadFromAsset.getHeight()) {
            int i = widthScreen / 2;
            bitmap = BitmapProcess.resizeBitmap(loadFromAsset, i, (loadFromAsset.getHeight() * i) / loadFromAsset.getWidth());
        } else {
            int i2 = widthScreen / 2;
            bitmap = BitmapProcess.resizeBitmap(loadFromAsset, (loadFromAsset.getWidth() * i2) / loadFromAsset.getHeight(), i2);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(str);
    }

    private void addText(Bitmap bitmap) {
        StickerView stickerView = new StickerView((Context) this, true);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.addRule(8, R.id.image);
        layoutParams.addRule(6, R.id.image);
        rlphoto.addView(stickerView, layoutParams);
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag("text");
        stickerView.setColor(colorsample);
        stickerView.setFont(fontsample);
        stickerView.setText(textsample);
        stickerView.setAlign(((Integer) ivalign.getTag()).intValue());
        stickerView.setCircle(((Integer) ivcircle.getTag()).intValue());
    }

    private void resetStickersFocus() {
        for (int i = 0; i < rlphoto.getChildCount(); i++) {
            try {
                if (rlphoto.getChildAt(i) instanceof StickerView) {
                    rlphoto.getChildAt(i).setFocusable(false);
                }
            } catch (Exception e) {
                Log.i("Photos to Collage", e.getMessage());
            }
        }
    }

    private void resetEditSticker() {
        for (int i = 0; i < rlphoto.getChildCount(); i++) {
            try {
                if (rlphoto.getChildAt(i) instanceof StickerView) {
                    ((StickerView) rlphoto.getChildAt(i)).setEdit(false);
                }
            } catch (Exception e) {
                Log.i("Photos to Collage", e.getMessage());
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        String str = "Photos to Collage";
        int visibility = rltext.getVisibility();
        int i = 0;
        Integer valueOf = Integer.valueOf(0);
        if (visibility == 8 && rlblur.getVisibility() == 8 && llchange.getVisibility() == 8) {
            if (motionEvent.getAction() == 0) {
                checkTouch = false;
                int x = ((int) motionEvent.getX()) - Utils.getRelativeLeft(rlphoto);
                int y = ((int) motionEvent.getY()) - Utils.getRelativeTop(rlphoto);
                int childCount = rlphoto.getChildCount() - 1;
                while (true) {
                    if (childCount < 0) {
                        break;
                    }
                    try {
                        if (rlphoto.getChildAt(childCount) instanceof StickerView) {
                            StickerView stickerView = (StickerView) rlphoto.getChildAt(childCount);
                            float f = (float) x;
                            float f2 = (float) y;
                            if (stickerView.isInDelete(f, f2)) {
                                if (stickerView.isFocusable()) {
                                    checkTouch = true;
                                    break;
                                }
                            }
                            if (stickerView.isInController(f, f2) && stickerView.isFocusable()) {
                                checkTouch = true;
                                break;
                            }
                            String str2 = "input_method";
                            String str3 = "";
                            if (stickerView.isInEdit(f, f2)) {
                                if (stickerView.isFocusable()) {
                                    checkTouch = true;
                                    if (!stickerView.getText().equals(str3)) {
                                        toolbarTitle.setText(getResources().getString(R.string.edittext));
                                        kindEdit = 1;
                                        stickerView.setEdit(true);
                                        menuItemCrop.setIcon(menuItemCropIconDone);
                                        rltext.setVisibility(i);
                                        rvtext.setVisibility(View.GONE);
                                        edtext.setVisibility(i);
                                        ivalign.setTag(Integer.valueOf(stickerView.getAlign()));
                                        edtext.requestFocus();
                                        if (ivalign.getTag().equals(Integer.valueOf(1))) {
                                            afltext.setGravity(17);
                                            ivalign.setImageResource(R.drawable.ic_center_selected);
                                        } else if (ivalign.getTag().equals(Integer.valueOf(2))) {
                                            afltext.setGravity(3);
                                            ivalign.setImageResource(R.drawable.ic_left_selected);
                                        } else if (ivalign.getTag().equals(Integer.valueOf(3))) {
                                            afltext.setGravity(5);
                                            ivalign.setImageResource(R.drawable.ic_right_selected);
                                        }
                                        ivcircle.setTag(Integer.valueOf(stickerView.getCircle()));
                                        if (ivcircle.getTag().equals(valueOf)) {
                                            ivcircle.setImageResource(R.drawable.ic_fill);
                                            afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
                                        } else if (ivcircle.getTag().equals(Integer.valueOf(1))) {
                                            ivcircle.setImageResource(R.drawable.ic_fill_selected);
                                            afltext.setShadowLayer(1.6f, 4.0f, 4.0f, -1);
                                        }
                                        loadSampleText(stickerView.getColor(), stickerView.getFont(), stickerView.getText());
                                        ((InputMethodManager) getSystemService(str2)).showSoftInput(edtext, 1);
                                    } else {
                                        ColorsAdapt2 colorAdapter22 = new ColorsAdapt2(listColor, this, rvselect.getHeight());
                                        colorAdapter2 = colorAdapter22;
                                        rvselect.setAdapter(colorAdapter22);
                                        colorAdapter2.setOnItemClickListener(new ColorsAdapt2.OnRecyclerViewItemClickListener() {
                                            public void onItemClick(View view, String str) {
                                                for (int i = 0; i < rlphoto.getChildCount(); i++) {
                                                    try {
                                                        if ((rlphoto.getChildAt(i) instanceof StickerView) && rlphoto.getChildAt(i).isFocusable() && ((StickerView) rlphoto.getChildAt(i)).isDrawedit()) {
                                                            ((StickerView) rlphoto.getChildAt(i)).changeColor(Color.parseColor(str));
                                                            return;
                                                        }
                                                    } catch (Exception unused) {
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            if (stickerView.isInFlip(f, f2)) {
                                if (stickerView.isFocusable()) {
                                    checkTouch = true;
                                    break;
                                }
                            }
                            if (stickerView.getContentRect().contains(f, f2)) {
                                checkTouch = true;
                                if (!stickerView.isFocusable()) {
                                    resetStickersFocus();
                                    stickerView.setFocusable(true);
                                    stickerView.bringToFront();
                                }
                                if (!firstTouch) {
                                    break;
                                }
                                try {
                                    if (System.currentTimeMillis() - timetouch <= 300) {
                                        firstTouch = false;
                                        if (!stickerView.getText().equals(str3)) {
                                            toolbarTitle.setText(getResources().getString(R.string.edittext));
                                            kindEdit = 1;
                                            stickerView.setEdit(true);
                                            menuItemCrop.setIcon(menuItemCropIconDone);
                                            rltext.setVisibility(View.VISIBLE);
                                            try {
                                                rvtext.setVisibility(View.GONE);
                                                edtext.setVisibility(View.VISIBLE);
                                                ivalign.setTag(Integer.valueOf(stickerView.getAlign()));
                                                edtext.requestFocus();
                                                if (ivalign.getTag().equals(Integer.valueOf(1))) {
                                                    afltext.setGravity(17);
                                                    ivalign.setImageResource(R.drawable.ic_center_selected);
                                                } else if (ivalign.getTag().equals(Integer.valueOf(2))) {
                                                    afltext.setGravity(3);
                                                    ivalign.setImageResource(R.drawable.ic_left_selected);
                                                } else if (ivalign.getTag().equals(Integer.valueOf(3))) {
                                                    afltext.setGravity(5);
                                                    ivalign.setImageResource(R.drawable.ic_right_selected);
                                                }
                                                ivcircle.setTag(Integer.valueOf(stickerView.getCircle()));
                                                if (ivcircle.getTag().equals(valueOf)) {
                                                    ivcircle.setImageResource(R.drawable.ic_fill);
                                                    afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
                                                } else if (ivcircle.getTag().equals(Integer.valueOf(1))) {
                                                    ivcircle.setImageResource(R.drawable.ic_fill_selected);
                                                    afltext.setShadowLayer(1.6f, 4.0f, 4.0f, -1);
                                                }
                                                loadSampleText(stickerView.getColor(), stickerView.getFont(), stickerView.getText());
                                                ((InputMethodManager) getSystemService(str2)).showSoftInput(edtext, 1);
                                            } catch (Exception e) {
                                                e = e;
                                                try {
                                                    Log.i(str, e.getMessage());
                                                } catch (Exception e2) {
                                                    e = e2;
                                                }
                                                childCount--;
                                                childCount--;
                                                i = 0;
                                            }
                                            childCount--;
                                            i = 0;
                                        }
                                    }
                                } catch (Exception e3) {
                                    e = e3;
                                    Log.i(str, e.getMessage());
                                    childCount--;
                                    childCount--;
                                    i = 0;
                                }
                            }
                        }
                    } catch (Exception e4) {
                        e = e4;
                        Log.i(str, e.getMessage());
                        childCount--;
                        childCount--;
                        i = 0;
                    }
                    childCount--;
                    i = 0;
                }
            }
            if (motionEvent.getAction() == 1) {
                Rect rect = new Rect(0, rvselect.getTop(), widthScreen, rvselect.getTop() + rvselect.getHeight());
                if (!checkTouch) {
                    if (rvselect.getAdapter() != colorAdapter2) {
                        resetStickersFocus();
                    } else if (!rect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                        resetStickersFocus();
                    }
                }
            }
        }
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (Exception unused) {
            return false;
        }
    }

    private void loadPointforSlider() {
        try {
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.slidersmall);
            wthumb = DisplayUtil.dip2px(this, 30.0f);
            sbslider.setThumb(new BitmapDrawable(getResources(), BitmapProcess.resizeBitmap(decodeResource, wthumb, wthumb)));
            int i = wthumb / 3;
            int i2 = wthumb + i;
            center = widthScreen / 2;
            dis = (widthScreen - (wthumb * 2)) / 12;
            int i3 = wthumb - (i / 2);
            for (int i4 = 0; i4 < 13; i4++) {
                LayoutParams layoutParams = new LayoutParams(i, i);
                layoutParams.setMargins(i3, i2, 0, 0);
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(layoutParams);
                imageView.setImageBitmap(decodeResource);
                rlslider.addView(imageView);
                i3 += dis;
            }
            sbslider.bringToFront();
            sbslider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (type == 1) {
                        exposure = seekBar.getProgress();
                    } else if (type == 2) {
                        contrast = seekBar.getProgress();
                    } else if (type == 3) {
                        sharpen = seekBar.getProgress();
                    } else if (type == 4) {
                        highlight = seekBar.getProgress();
                    } else if (type == 5) {
                        shadow = seekBar.getProgress();
                    } else if (type == 6) {
                        temperature = seekBar.getProgress();
                    } else if (type == 7) {
                        vignette = seekBar.getProgress();
                    }
                    setTextSlider(seekBar.getProgress());
                    gpuEffect();
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    if (z) {
                        setTextSlider(i);
                    }
                }
            });
        } catch (Exception unused) {
        }
    }

    public void setTextSlider(int i) {
        try {
            sbslider.setProgress(i);
            if (type == 1) {
                tvslider.setText(String.valueOf(i - 6));
            } else if (type == 2) {
                tvslider.setText(String.valueOf(i));
            } else if (type == 3) {
                tvslider.setText(String.valueOf(i));
            } else if (type == 4) {
                tvslider.setText(String.valueOf(i));
            } else if (type == 5) {
                tvslider.setText(String.valueOf(i));
            } else if (type == 6) {
                tvslider.setText(String.valueOf(i - 6));
            } else if (type == 7) {
                tvslider.setText(String.valueOf(i));
            }
            LayoutParams layoutParams = (LayoutParams) tvslider.getLayoutParams();
            layoutParams.setMargins((center - wthumb) + ((i - 6) * dis), DisplayUtil.dip2px(this, 5.0f), 0, 0);
            tvslider.setLayoutParams(layoutParams);
        } catch (Exception unused) {
        }
    }

    public void loadFrame(int i) {
        int i2 = 0;
        if (i == 1) {
            try {
                listItem = getAssets().list("frames_th");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (listItem != null) {
                ArrayList arrayList = new ArrayList();
                String[] strArr = listItem;
                int length = strArr.length;
                while (i2 < length) {
                    String str = strArr[i2];
                    StringBuilder sb = new StringBuilder();
                    sb.append("frames_th/");
                    sb.append(str);
                    arrayList.add(sb.toString());
                    i2++;
                }
                String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
                listItem = strArr2;
                CommonAdapt commonAdapter2 = new CommonAdapt(strArr2, this);
                commonAdapter = commonAdapter2;
                rvselect.setAdapter(commonAdapter2);
                setClick();
            }
        } else if (i == 2) {
            try {
                listItem = getAssets().list("brush_frame_th");
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (listItem != null) {
                ArrayList arrayList2 = new ArrayList();
                String[] strArr3 = listItem;
                int length2 = strArr3.length;
                while (i2 < length2) {
                    String str2 = strArr3[i2];
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("brush_frame_th/");
                    sb2.append(str2);
                    arrayList2.add(sb2.toString());
                    i2++;
                }
                String[] strArr4 = (String[]) arrayList2.toArray(new String[arrayList2.size()]);
                listItem = strArr4;
                CommonAdapt commonAdapter3 = new CommonAdapt(strArr4, this);
                commonAdapter = commonAdapter3;
                rvselect.setAdapter(commonAdapter3);
                setClick();
            }
        } else if (i == 3) {
            try {
                listItem = getAssets().list("frames");
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            if (listItem != null) {
                ArrayList arrayList3 = new ArrayList();
                String[] strArr5 = listItem;
                int length3 = strArr5.length;
                while (i2 < length3) {
                    String str3 = strArr5[i2];
                    if (str3.contains("thumb_")) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("frames/");
                        sb3.append(str3);
                        arrayList3.add(sb3.toString());
                    }
                    i2++;
                }
                String[] strArr6 = (String[]) arrayList3.toArray(new String[arrayList3.size()]);
                listItem = strArr6;
                CommonAdapt commonAdapter4 = new CommonAdapt(strArr6, this);
                commonAdapter = commonAdapter4;
                rvselect.setAdapter(commonAdapter4);
                setClick();
            }
        }
    }

    public void loadABC() {
        try {
            listItem = getAssets().list("texts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listItem != null) {
            ArrayList arrayList = new ArrayList();
            for (String str : listItem) {
                StringBuilder sb = new StringBuilder();
                sb.append("texts/");
                sb.append(str);
                arrayList.add(sb.toString());
            }
            String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
            listItem = strArr2;
            CommonAdapt commonAdapter2 = new CommonAdapt(strArr2, this);
            commonAdapter = commonAdapter2;
            rvselect.setAdapter(commonAdapter2);
            setClick();
        }
    }

    public void loadSnap() {
        try {
            listItem = getAssets().list("stickers");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listItem != null) {
            ArrayList arrayList = new ArrayList();
            for (String str : listItem) {
                StringBuilder sb = new StringBuilder();
                sb.append("stickers/");
                sb.append(str);
                arrayList.add(sb.toString());
            }
            String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
            listItem = strArr2;
            CommonAdapt commonAdapter2 = new CommonAdapt(strArr2, this);
            commonAdapter = commonAdapter2;
            rvselect.setAdapter(commonAdapter2);
            setClick();
        }
    }

    public void loadSt() {
        try {
            listItem = getAssets().list("st");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listItem != null) {
            ArrayList arrayList = new ArrayList();
            for (String str : listItem) {
                StringBuilder sb = new StringBuilder();
                sb.append("st/");
                sb.append(str);
                arrayList.add(sb.toString());
            }
            String[] strArr2 = (String[]) arrayList.toArray(new String[arrayList.size()]);
            listItem = strArr2;
            CommonAdapt commonAdapter2 = new CommonAdapt(strArr2, this);
            commonAdapter = commonAdapter2;
            rvselect.setAdapter(commonAdapter2);
            setClick();
        }
    }

    public void loadEffect() {
        try {
            listItem = getAssets().list("effects");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listItem != null) {
            ArrayList arrayList = new ArrayList();
            for (String str : listItem) {
                if (str.contains("thumb_")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("effects/");
                    sb.append(str);
                    arrayList.add(sb.toString());
                }
            }
            listItem = (String[]) arrayList.toArray(new String[arrayList.size()]);
            rvselect.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (VERSION.SDK_INT >= 16) {
                        rvselect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        rvselect.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    EditLastAct editActivity = EditLastAct.this;
                    String[] strArr = editActivity.listItem;
                    EditLastAct editActivity2 = EditLastAct.this;
                    editActivity.effectAdapter = new EffectAdapt(strArr, editActivity2, editActivity2.rvselect.getHeight());
                    rvselect.setAdapter(effectAdapter);
                    effectClick();
                }
            });
        }
    }

    public void loadFont() {
        try {
            listfont = getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listfont != null) {
            int i = 0;
            while (true) {
                String[] strArr = listfont;
                if (i < strArr.length) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("fonts/");
                    sb.append(listfont[i]);
                    strArr[i] = sb.toString();
                    i++;
                } else {
                    FontAdapt fontAdapter = new FontAdapt(strArr, this);
                    rvtext.setAdapter(fontAdapter);
                    fontAdapter.setOnItemClickListener(new FontAdapt.OnRecyclerViewItemClickListener() {
                        public void onItemClick(View view, String str) {
                            String str2 = "";
                            loadSampleText(str2, str, str2);
                        }
                    });
                    return;
                }
            }
        }
    }

    public void loadSampleText(String str, String str2, String str3) {
        String str4 = "";
        if (!str3.equals(str4)) {
            afltext.setText(str3);
            edtext.setText(str3);
            textsample = str3;
        }
        if (!str.equals(str4)) {
            afltext.setTextColor(Color.parseColor(str));
            colorsample = str;
        }
        if (!str2.equals(str4)) {
            afltext.setTypeface(Typeface.createFromAsset(getAssets(), str2));
            fontsample = str2;
        }
    }

    public void closeKeyboard() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public void clickable(boolean z) {
        iceffect.setClickable(z);
        icsnap.setClickable(z);
        ictext.setClickable(z);
        icabc.setClickable(z);
        icblur.setClickable(z);
        icadjust.setClickable(z);
        rlphoto.setClickable(z);
    }

    public void refreshImageView() {
        int i = lastTouchedPositionX;
        if (i != -1 && lastTouchedPositionY != -1 && blurFinished) {
            lastTouchedPositionX = i - (bmmask.getWidth() / 2);
            lastTouchedPositionY -= bmmask.getHeight() / 2;
            if (lastTouchedPositionX < 0) {
                lastTouchedPositionX = 0;
            }
            if (lastTouchedPositionY < 0) {
                lastTouchedPositionY = 0;
            }
            if (lastTouchedPositionX > bmmain.getWidth()) {
                lastTouchedPositionX = bmmain.getWidth() - 10;
            }
            if (lastTouchedPositionY > bmmain.getHeight()) {
                lastTouchedPositionY = bmmain.getHeight() - 10;
            }
            new BlurTask(lastTouchedPositionX, lastTouchedPositionY).execute(new Void[0]);
        }
    }

    public void gpuEffect() {
        try {
            Boolean valueOf = Boolean.valueOf(false);
            GPUImageFilterGroup gPUImageFilterGroup = new GPUImageFilterGroup();
            if (exposure != 6) {
                valueOf = Boolean.valueOf(true);
                float f = 2.0f - ((((float) (exposure - 6)) * 0.1f) + 1.0f);
                GPUImageGammaFilter gPUImageGammaFilter = new GPUImageGammaFilter();
                gPUImageGammaFilter.setGamma(f);
                gPUImageFilterGroup.addFilter(gPUImageGammaFilter);
            }
            if (contrast != 0) {
                valueOf = Boolean.valueOf(true);
                GPUImageContrastFilter gPUImageContrastFilter = new GPUImageContrastFilter();
                gPUImageContrastFilter.setContrast((((float) contrast) * 0.1f) + 1.0f);
                gPUImageFilterGroup.addFilter(gPUImageContrastFilter);
            }
            if (sharpen != 0) {
                valueOf = Boolean.valueOf(true);
                GPUImageSharpenFilter gPUImageSharpenFilter = new GPUImageSharpenFilter();
                gPUImageSharpenFilter.setSharpness(((float) sharpen) * 0.1f);
                gPUImageFilterGroup.addFilter(gPUImageSharpenFilter);
            }
            if (!(highlight == 0 && shadow == 0)) {
                valueOf = Boolean.valueOf(true);
                GPUImageHighlightShadowFilter gPUImageHighlightShadowFilter = new GPUImageHighlightShadowFilter();
                gPUImageHighlightShadowFilter.setHighlights(1.0f - (((float) highlight) * 0.08f));
                gPUImageHighlightShadowFilter.setShadows(((float) shadow) * 0.08f);
                gPUImageFilterGroup.addFilter(gPUImageHighlightShadowFilter);
            }
            if (temperature != 6) {
                valueOf = Boolean.valueOf(true);
                int i = 400;
                if (temperature < 6) {
                    i = 200;
                }
                GPUImageWhiteBalanceFilter gPUImageWhiteBalanceFilter = new GPUImageWhiteBalanceFilter();
                gPUImageWhiteBalanceFilter.setTemperature(((float) ((temperature - 6) * i)) + 5000.0f);
                gPUImageFilterGroup.addFilter(gPUImageWhiteBalanceFilter);
            }
            if (vignette != 0) {
                valueOf = Boolean.valueOf(true);
                PointF pointF = new PointF();
                pointF.x = 0.5f;
                pointF.y = 0.5f;
                gPUImageFilterGroup.addFilter(new GPUImageVignetteFilter(pointF, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 1.0f - (((float) vignette) * 0.01f)));
            }
            if (!filter.contains("thumb_effect_00001")) {
                valueOf = Boolean.valueOf(true);
                GPUImageLookupFilter gPUImageLookupFilter = new GPUImageLookupFilter();
                gPUImageLookupFilter.setBitmap(BitmapLoad.loadFromAsset(this, new int[]{512, 512}, filter.replace("thumb_", "").replace("jpg", "png")));
                gPUImageFilterGroup.addFilter(gPUImageLookupFilter);
            }
            if (valueOf.booleanValue()) {
                gpuview.setFilter(gPUImageFilterGroup);
                gpuview.requestRender();
                return;
            }
            gpuview.setFilter(new NonFilter());
            gpuview.requestRender();
        } catch (Exception unused) {
        }
    }

    public void setChange() {
        try {
            if (type == 1) {
                toolbarTitle.setText(getResources().getString(R.string.editexposure));
            } else if (type == 2) {
                toolbarTitle.setText(getResources().getString(R.string.editcontrast));
            } else if (type == 3) {
                toolbarTitle.setText(getResources().getString(R.string.editsharpen));
            } else if (type == 4) {
                toolbarTitle.setText(getResources().getString(R.string.edithightlightsave));
            } else if (type == 5) {
                toolbarTitle.setText(getResources().getString(R.string.editshadowsave));
            } else if (type == 6) {
                toolbarTitle.setText(getResources().getString(R.string.edittemperature));
            } else if (type == 7) {
                toolbarTitle.setText(getResources().getString(R.string.editvignette));
            }
        } catch (Exception | OutOfMemoryError unused) {
        }
    }

    public void requestPermission(final String str, String str2, int i) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
            showAlertDialog(getString(R.string.permission_title_rationale), str2, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(EditLastAct.this, new String[]{str}, i);
                }
            }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{str}, i);
    }

    public void showAlertDialog(String str, String str2, DialogInterface.OnClickListener onClickListener, String str3, DialogInterface.OnClickListener onClickListener2, String str4) {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) str);
        builder.setMessage((CharSequence) str2);
        builder.setPositiveButton((CharSequence) str3, onClickListener);
        builder.setNegativeButton((CharSequence) str4, onClickListener2);
        builder.show();
    }

    public void showAd() {
        openNext();
    }

    public void openNext() {
        Intent intent = new Intent().setClass(this, ShareImageAct.class);
        intent.setData(Uri.parse(savePath));
        if (MyAplication.getApplication().isAdLoaded()) {
            MyAplication.getApplication().showInterstitial(this, intent, false);
        } else {
            startActivity(intent);
        }
    }

    class BlurTask extends AsyncTask<Void, Void, Bitmap> {
        private int maskPosX;
        private int maskPosY;

        public BlurTask(int i, int i2) {
            maskPosX = i;
            maskPosY = i2;
        }

        public void onPreExecute() {
            blurFinished = false;
        }

        public Bitmap doInBackground(Void... voidArr) {
            try {
                Bitmap applyMask = BitmapProcess.applyMask(bmmain, bmmask, maskPosX, maskPosY);
                new Canvas(bmblur).drawBitmap(applyMask, (float) maskPosX, (float) maskPosY, new Paint());
                applyMask.recycle();
                return bmblur;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(Bitmap bitmap) {
            ivblur.setImageBitmap(bitmap);
            blurFinished = true;
        }
    }

    class saveAndGo extends AsyncTask<Void, Void, String> {
        saveAndGo() {
        }

        public String doInBackground(Void... voidArr) {
            return "";
        }

        public void onPreExecute() {
            mShowLoader = true;
        }

        public void onPostExecute(String str) {
            EditLastAct editActivity = EditLastAct.this;
//            editActivity.savePath = editActivity.savePhoto();
            editActivity.savePath = editActivity.savePhotoSIRA();
            mShowLoader = false;
            if (savePath.equals("")) {
                Toast.makeText(EditLastAct.this, "Couldn't save photo, error", Toast.LENGTH_SHORT).show();
            } else {
                showAd();
            }
        }
    }
}
