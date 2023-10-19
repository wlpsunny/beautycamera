package com.eagleapps.beautycam.utilsApp;

import android.content.Context;
import android.graphics.PointF;

import com.eagleapps.beautycam.R;
import com.eagleapps.beautycam.act.WhittenFaceEditAct;

import java.util.LinkedList;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTwoInputFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class GPUImgFilterTool {


    public static GPUImageFilter createFilterForType(Context context, FilterType filterType) {
        switch (1) {
            case 1:
                return new GPUImageContrastFilter(1.3f);
            case 2:
                return new GPUImageGammaFilter(0.5f);
            case 3:
                return new GPUImageColorInvertFilter();
            case 4:
                return new GPUImagePixelationFilter();
            case 5:
                return new GPUImageHueFilter(50.0f);
            case 6:
                return new GPUImageBrightnessFilter(0.15f);
            case 7:
                return new GPUImageGrayscaleFilter();
            case 8:
                return new GPUImageSepiaFilter();
            case 9:
                return new GPUImageSharpenFilter(1.6f);
            case 10:
                return new GPUImageSobelEdgeDetection();
            case 11:
                GPUImage3x3ConvolutionFilter gPUImage3x3ConvolutionFilter = new GPUImage3x3ConvolutionFilter();
                gPUImage3x3ConvolutionFilter.setConvolutionKernel(new float[]{-1.0f, 0.0f, 1.0f, -2.0f, 2.3f, 2.0f, -1.0f, 0.0f, 1.0f});
                return gPUImage3x3ConvolutionFilter;
            case 12:
                return new GPUImageEmbossFilter();
            case 13:
                return new GPUImagePosterizeFilter();
            case 14:
                LinkedList linkedList = new LinkedList();
                linkedList.add(new GPUImageContrastFilter());
                linkedList.add(new GPUImageDirectionalSobelEdgeDetectionFilter());
                linkedList.add(new GPUImageGrayscaleFilter());
                return new GPUImageFilterGroup(linkedList);
            case 15:
                return new GPUImageSaturationFilter(1.5f);
            case 16:
                return new GPUImageExposureFilter(0.5f);
            case 17:
                return new GPUImageHighlightShadowFilter(0.5f, 1.0f);
            case 18:
                return new GPUImageMonochromeFilter(1.5f, new float[]{0.1f, 0.1f, 0.1f, 1.0f});
            case 19:
                return new GPUImageOpacityFilter(0.5f);
            case 20:
                return new GPUImageRGBFilter(1.5f, 1.0f, 1.0f);
            case 21:
                return new GPUImageRGBFilter(1.0f, 1.5f, 1.0f);
            case 22:
                return new GPUImageRGBFilter(1.2f, 1.2f, 1.5f);
            case 23:
                return new GPUImageWhiteBalanceFilter(4500.0f, 0.0f);
            case 24:
                PointF pointF = new PointF();
                pointF.x = 0.5f;
                pointF.y = 0.5f;
                return new GPUImageVignetteFilter(pointF, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
            case 25:
                GPUImageToneCurveFilter gPUImageToneCurveFilter = new GPUImageToneCurveFilter();
                gPUImageToneCurveFilter.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.tone_cuver_sample));
                return gPUImageToneCurveFilter;
            case 26:
                return createBlendMultiplyFilter(context, GPUImageMultiplyBlendFilter.class);
            case 27:
                return new GPUImageBilateralFilter();
            case 28:
                return new GPUImageGaussianBlurFilter();
            case 29:
                return new GPUImageSmoothToonFilter();
            case 30:
                return new GPUImageToonFilter();
            default:
                throw new IllegalStateException("No filter of that type!");
        }
    }

    private static GPUImageFilter createBlendMultiplyFilter(Context context, Class<? extends GPUImageTwoInputFilter> cls) {
        try {
            GPUImageTwoInputFilter gPUImageTwoInputFilter = (GPUImageTwoInputFilter) cls.newInstance();
            gPUImageTwoInputFilter.setBitmap(WhittenFaceEditAct.whittencolorBitmap);
            return gPUImageTwoInputFilter;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GPUImageFilter ApplyColorBlending(Context context) {
        return createFilterForType(context, FilterType.BRIGHTNESS);
    }

    private enum FilterType {
        CONTRAST,
        GRAYSCALE,
        SHARPEN,
        SEPIA,
        SOBEL_EDGE_DETECTION,
        THREE_X_THREE_CONVOLUTION,
        FILTER_GROUP,
        EMBOSS,
        POSTERIZE,
        GAMMA,
        BRIGHTNESS,
        INVERT,
        HUE,
        PIXELATION,
        SATURATION,
        EXPOSURE,
        HIGHLIGHT_SHADOW,
        MONOCHROME,
        OPACITY,
        RGB,
        WHITE_BALANCE,
        VIGNETTE,
        TONE_CURVE,
        BLEND_COLOR_BURN,
        BLEND_COLOR_DODGE,
        BLEND_DARKEN,
        BLEND_DIFFERENCE,
        BLEND_DISSOLVE,
        BLEND_EXCLUSION,
        BLEND_SOURCE_OVER,
        BLEND_HARD_LIGHT,
        BLEND_LIGHTEN,
        BLEND_ADD,
        BLEND_DIVIDE,
        BLEND_MULTIPLY,
        BLEND_OVERLAY,
        BLEND_SCREEN,
        BLEND_ALPHA,
        BLEND_COLOR,
        BLEND_HUE,
        BLEND_SATURATION,
        RED,
        GREEN,
        BLUE,
        CONTRAST2,
        BLEND_VINTAGE,
        BLEND_LUMINOSITY,
        BLEND_LINEAR_BURN,
        BLEND_SOFT_LIGHT,
        BLEND_SUBTRACT,
        BLEND_CHROMA_KEY,
        BLEND_NORMAL,
        LOOKUP_AMATORKA,
        BILATERAL_BLUR,
        GAUSSIAN_BLUR,
        SMOOTH_TOON,
        TOON
    }
}
