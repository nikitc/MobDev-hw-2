package comnikitc.github.mobdev_hw_2;


import android.graphics.Color;

class GradientColorPicker {
    private static int countColors = 36;

    public GradientColorPicker() {

    }

    static int[] GetValueColors() {
        int[] valuesColors = new int[countColors];
        for (int i = 0; i < countColors; i++) {
            float[] pixelHSV = new float[3];
            pixelHSV[0] = (i + 1) * 10;
            pixelHSV[1] = 1;
            pixelHSV[2] = 1;

            valuesColors[i] = Color.HSVToColor(pixelHSV);
        }

        return valuesColors;
    }

    static float[] GetPositionColors() {
        float[] positionColors = new float[countColors];
        for (int i = 0; i < countColors; i++) {
            positionColors[i] = i / 36f ;
        }

        return positionColors;
    }
}
