package com.ratnadirawidyasari.pola;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    

    public ArrayList<String> patternAngka = new ArrayList<>();
    private void generatePatternAngka() {
        String patternNol = "12234444566788800";
        patternAngka.add(patternNol);
        String patternSatu = "123444444444567888888888";
        patternAngka.add(patternSatu);
        String patternDua = "12222234444456664222345666667888881222866678";
        patternAngka.add(patternDua);
        String patternTiga = "12222234444444445666667812228666781222866678";
        patternAngka.add(patternTiga);
        String patternEmpat = "1234442888123444444444567888666788888";
        patternAngka.add(patternEmpat);
        String patternLima = "12222234566642223444445666667812228666788888";
        patternAngka.add(patternLima);
        String patternEnam = "12222234566642223444445666667888888888";
        patternAngka.add(patternEnam);
        String patternTujuh = "1222223444444444567888888866678";
        patternAngka.add(patternTujuh);
        String patternDelapan = "12234444566788800";
        patternAngka.add(patternDelapan);
        String patternSembilan = "12222234444444445666667812228666788888";
        patternAngka.add(patternSembilan);
    }

    Bitmap tempBitmap;
    // Fungsi bantu untuk cari tahu butuh dikali berapa ini string biar jadi sama
    // Pertama, hapus seluruh kemunculan char ganjil
    public int getStringMultiplier(String smallest, String realInput) {
        // Asumsi realinput selalu yang lebih besar
        String temp1 = smallest;
        String temp2 = realInput;
        temp1.replace("1", "");
        temp1.replace("3", "");
        temp1.replace("5", "");
        temp1.replace("7", "");
        temp2.replace("1", "");
        temp2.replace("3", "");
        temp2.replace("5", "");
        temp2.replace("7", "");
        int kecil = temp1.length();
        int besar = temp2.length();
        int multiplier = besar / kecil;
        return multiplier;
    }

    // Memperpanjang string arah, khusus untuk arah genap karena kalo yang ganjil gak perlu
    public String stringStretcher(String input, int multiplier) {
        // Pertama copy
        StringBuilder result = new StringBuilder("");
        // Loop through string and copy into String Builder
        for (int i = 0; i < input.length(); i++) {
            if ((Integer.valueOf(input.charAt(i)) & 1) == 0) {
                // Even number
                for (int j = 1; j <= multiplier; j++) {
                    result.append(input.charAt(i));
                }
            } else {
                result.append(input.charAt(i));
            }
        }
        return result.toString();
    }

    public final double distance(final String s1, final String s2) {

        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        // INFinite distance is the max possible distance
        int inf = s1.length() + s2.length();

        // Create and initialize the character array indices
        HashMap<Character, Integer> da = new HashMap<Character, Integer>();

        for (int d = 0; d < s1.length(); d++) {
            da.put(s1.charAt(d), 0);
        }

        for (int d = 0; d < s2.length(); d++) {
            da.put(s2.charAt(d), 0);
        }

        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        int[][] h = new int[s1.length() + 2][s2.length() + 2];

        // initialize the left and top edges of H
        for (int i = 0; i <= s1.length(); i++) {
            h[i + 1][0] = inf;
            h[i + 1][1] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            h[0][j + 1] = inf;
            h[1][j + 1] = j;

        }

        // fill in the distance matrix H
        // look at each character in s1
        for (int i = 1; i <= s1.length(); i++) {
            int db = 0;

            // look at each character in b
            for (int j = 1; j <= s2.length(); j++) {
                int i1 = da.get(s2.charAt(j - 1));
                int j1 = db;

                int cost = 1;
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cost = 0;
                    db = j;
                }

                h[i + 1][j + 1] = min(
                        h[i][j] + cost, // substitution
                        h[i + 1][j] + 1, // insertion
                        h[i][j + 1] + 1, // deletion
                        h[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
            }

            da.put(s1.charAt(i - 1), i);
        }

        return h[s1.length() + 1][s2.length() + 1];
    }

    private static int min(
            final int a, final int b, final int c, final int d) {
        return Math.min(a, Math.min(b, Math.min(c, d)));
    }

    public String FoundDigit = new String("");
    public void recognizeDigit() {

        for (int i = 0; i < listChain.size(); i++) {
            // Copy the string
            String temp = listChain.get(i);
            String postProcess = listChain.get(i);
//            Log.d("INI TEMP", temp);
            double currentMaxScore = 9999999;
            int maxScoreIndex = -1;

            // Bandingkan bersama seluruh pattern angka
            for (int j = 0; j < patternAngka.size(); j ++) {
                // Abaikan angka 8
                if (j != 8) {
                    String copyPattern = patternAngka.get(j);
                    int multiplier = getStringMultiplier(copyPattern, temp);
                    String newPattern = stringStretcher(copyPattern, multiplier);
                    double score = distance(newPattern, temp);
                    Log.d("SCORE" , score+" lala");
                    if(score < currentMaxScore) {
                        currentMaxScore = score;
                        maxScoreIndex = j;
                    }

                }
            }

            // Kasus angka 1 ini ambigu sama 0 dan 8, perlu post processing
            if(maxScoreIndex==1) {
                int panjangHorizontal = 0;
                int panjangVertical = 0;
                for (int z = 0; z < postProcess.length(); z++) {
                    if (postProcess.charAt(z) == '2') {
                        panjangHorizontal++;
                    }
                    if (postProcess.charAt(z) == '4') {
                        panjangVertical++;
                    }
                }
                panjangHorizontal = panjangHorizontal / 2;
                panjangVertical = panjangVertical / 2;
                int xTengah = listAwalX.get(i) + panjangHorizontal;
                int yTengah = listAwalY.get(i) + panjangVertical;
                // Case tengahnya putih
                if (Color.red(tempBitmap.getPixel(xTengah, yTengah)) > 200) {
                    maxScoreIndex = 0;
                } else {
                    // Case tengahnya item
                    int yLebihTengah = listAwalY.get(i) + panjangVertical / 2;
                    if (Color.red(tempBitmap.getPixel(xTengah, yLebihTengah)) > 200) {
                        maxScoreIndex = 8;
                    } else {
                        maxScoreIndex = 1;
                    }
                }

            }

            FoundDigit+=String.valueOf(maxScoreIndex);
        }
        RecognizedDigit.setText(FoundDigit);
    }
    
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Uri ImageURI = null;
    String mCurrentPhotoPath;
    private ImageView imageView;
    private ImageView grayscaleImage;
    private ImageView cumulativeImage;
    private ImageView cumulativeColor;
    private TextView rgbText, RecognizedDigit;
    Uri photoURI;
    Bitmap rotatedBitmap, thumbnailBitmap;
    Bitmap grayscaleBitmap;
    Bitmap cumulativeBitmapGrayscale;
    Bitmap cumulativeColorBitmap;
    Bitmap skeleton;
    List<String> listChain = new ArrayList<String>();
    private int[] grayscaleHistogram = new int[256];
    private int[] grayscaleAfterHistogram = new int[256];
    private int[] cumulativeHistogram = new int[256];
    private float[] probabilityHistogram = new float[256];
    private int[] transformationValue = new int[256];

    private int[] targethistogram = new int[256];
    private int[] cumulativeTarget = new int[256];
    private float[] targetTransformation = new float[256];
    private int totalPixel = 0;

//    public Bitmap doZhangSuenThinning(final Bitmap givenImage) {
//        Bitmap binaryImage;
//        binaryImage = givenImage;
//        int a, b;
//        List<Point> pointsToChange = new LinkedList();
//        boolean hasChange;
//        do {
//            hasChange = false;
//            for (int y = 1; y + 1 < binaryImage.getHeight(); y++) {
//                for (int x = 1; x + 1 < binaryImage.getWidth(); x++) {
//                    a = getA(binaryImage, y, x);
//                    b = getB(binaryImage, y, x);
//                    if (Color.red(binaryImage.getPixel(y,x)) < 20 && 2 <= b && b <= 6 && a == 1
//                            && (Color.red(binaryImage.getPixel(y-1,x)) * Color.red(binaryImage.getPixel(y,x+1)) * Color.red(binaryImage.getPixel(y+1,x)) == 0)
//                            && (Color.red(binaryImage.getPixel(y,x+1)) * Color.red(binaryImage.getPixel(y+1,x)) * Color.red(binaryImage.getPixel(y,x-1)) == 0)) {
//                        pointsToChange.add(new Point(x, y));
////binaryImage[y][x] = 0;
//                        hasChange = true;
//                    }
//                }
//            }
//            for (Point point : pointsToChange) {
//                binaryImage.setPixel(point.getY(),point.getX(),Color.rgb(0,0,0));
//            }
//            pointsToChange.clear();
//            for (int y = 1; y + 1 < binaryImage.getHeight(); y++) {
//                for (int x = 1; x + 1 < binaryImage.getWidth(); x++) {
//                    a = getA(binaryImage, y, x);
//                    b = getB(binaryImage, y, x);
//                    if (Color.red(binaryImage.getPixel(y,x)) == 1 && 2 <= b && b <= 6 && a == 1
//                            && (Color.red(binaryImage.getPixel(y-1,x)) * Color.red(binaryImage.getPixel(y,x+1)) * Color.red(binaryImage.getPixel(y,x-1)) == 0)
//                            && (Color.red(binaryImage.getPixel(y-1,x)) * Color.red(binaryImage.getPixel(y+1,x)) * Color.red(binaryImage.getPixel(y,x-1)) == 0)) {
//                        pointsToChange.add(new Point(x, y));
//                        hasChange = true;
//                    }
//                }
//            }
//            for (Point point : pointsToChange) {
//                binaryImage.setPixel(point.getY(),point.getX(),Color.rgb(0,0,0));
//            }
//            pointsToChange.clear();
//        } while (hasChange);
//        return binaryImage;
//    }
//
//    private int getA(Bitmap binaryImage, int y, int x) {
//        int count = 0;
////p2 p3
//        if (y - 1 >= 0 && x + 1 < binaryImage.getWidth() && Color.red(binaryImage.getPixel(y-1,x)) == 0 && Color.red(binaryImage.getPixel(y-1,x+1)) == 1) {
//            count++;
//        }
////p3 p4
//        if (y - 1 >= 0 && x + 1 < binaryImage.getWidth() && Color.red(binaryImage.getPixel(y-1,x+1)) == 0 && Color.red(binaryImage.getPixel(y,x+1)) == 1) {
//            count++;
//        }
////p4 p5
//        if (y + 1 < binaryImage.getHeight() && x + 1 < binaryImage.getWidth() && Color.red(binaryImage.getPixel(y,x+1)) == 0 && Color.red(binaryImage.getPixel(y+1,x+1)) == 1) {
//            count++;
//        }
////p5 p6
//        if (y + 1 < binaryImage.getHeight() && x + 1 < binaryImage.getWidth() && Color.red(binaryImage.getPixel(y+1,x+1)) == 0 && Color.red(binaryImage.getPixel(y+1,x)) == 1) {
//            count++;
//        }
////p6 p7
//        if (y + 1 < binaryImage.getHeight() && x - 1 >= 0 && Color.red(binaryImage.getPixel(y+1,x)) == 0 && Color.red(binaryImage.getPixel(y+1,x-1)) == 1) {
//            count++;
//        }
////p7 p8
//        if (y + 1 < binaryImage.getHeight() && x - 1 >= 0 && Color.red(binaryImage.getPixel(y+1,x-1)) == 0 && Color.red(binaryImage.getPixel(y,x-1)) == 1) {
//            count++;
//        }
////p8 p9
//        if (y - 1 >= 0 && x - 1 >= 0 && Color.red(binaryImage.getPixel(y,x-1)) == 0 && Color.red(binaryImage.getPixel(y-1,x-1)) == 1) {
//            count++;
//        }
////p9 p2
//        if (y - 1 >= 0 && x - 1 >= 0 && Color.red(binaryImage.getPixel(y-1,x-1)) == 0 && Color.red(binaryImage.getPixel(y-1,x)) == 1) {
//            count++;
//        }
//        return count;
//    }
//
//    private int getB(Bitmap binaryImage, int y, int x) {
//        return Color.red(binaryImage.getPixel(y-1,x)) + Color.red(binaryImage.getPixel(y-1,x+1)) + Color.red(binaryImage.getPixel(y,x+1))
//                + Color.red(binaryImage.getPixel(y+1,x+1)) + Color.red(binaryImage.getPixel(y+1,x)) + Color.red(binaryImage.getPixel(y+1,x-1))
//                + Color.red(binaryImage.getPixel(y,x-1)) + Color.red(binaryImage.getPixel(y-1,x-1));
//    }


    public void convertToBinary(Bitmap cumulativeBitmapGrayscale , ImageView cumulativeImage){
        int maximumX = cumulativeBitmapGrayscale.getWidth();
        int maximumY = cumulativeBitmapGrayscale.getHeight();
        int[][] convert = new int[maximumX][maximumY];
        for (int i = 1; i < maximumY; i++) {
            for (int j = 1; j < maximumX; j++) {

                if (Color.red((cumulativeBitmapGrayscale.getPixel(j,i)))  < threshold) {
                    convert[j][i] = 1;
                } else {
                    convert[j][i] = 0;
                }
            }
        }

        int[][] change = doZhangSuenThinning(convert,false);
        Bitmap tesMove = cumulativeBitmapGrayscale;

        for (int y = 1; y + 1 < change.length; y++) {
            for (int x = 1; x + 1 < change[y].length; x++) {
                if (change[y][x]==0){
                    tesMove.setPixel(y,x,Color.rgb(255,255,255));
                } else {
                    tesMove.setPixel(y,x,Color.rgb(0,0,0));
                }
            }
        }
        cumulativeImage.setImageBitmap(tesMove);
    }


    public int[][] doZhangSuenThinning(final int[][] givenImage, boolean changeGivenImage) {
        int[][] binaryImage;
        if (changeGivenImage) {
            binaryImage = givenImage;
        } else {
            binaryImage = givenImage.clone();
        }
        int a, b;
        List<Point> pointsToChange = new LinkedList();
        boolean hasChange;
        do {
            hasChange = false;
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y + 1][x] == 0)
                            && (binaryImage[y][x + 1] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
//binaryImage[y][x] = 0;
                        hasChange = true;
                    }
                }
            }
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
            for (int y = 1; y + 1 < binaryImage.length; y++) {
                for (int x = 1; x + 1 < binaryImage[y].length; x++) {
                    a = getA(binaryImage, y, x);
                    b = getB(binaryImage, y, x);
                    if (binaryImage[y][x] == 1 && 2 <= b && b <= 6 && a == 1
                            && (binaryImage[y - 1][x] * binaryImage[y][x + 1] * binaryImage[y][x - 1] == 0)
                            && (binaryImage[y - 1][x] * binaryImage[y + 1][x] * binaryImage[y][x - 1] == 0)) {
                        pointsToChange.add(new Point(x, y));
                        hasChange = true;
                    }
                }
            }
            for (Point point : pointsToChange) {
                binaryImage[point.getY()][point.getX()] = 0;
            }
            pointsToChange.clear();
        } while (hasChange);
        return binaryImage;
    }

    private int getA(int[][] binaryImage, int y, int x) {
        int count = 0;
//p2 p3
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x] == 0 && binaryImage[y - 1][x + 1] == 1) {
            count++;
        }
//p3 p4
        if (y - 1 >= 0 && x + 1 < binaryImage[y].length && binaryImage[y - 1][x + 1] == 0 && binaryImage[y][x + 1] == 1) {
            count++;
        }
//p4 p5
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y][x + 1] == 0 && binaryImage[y + 1][x + 1] == 1) {
            count++;
        }
//p5 p6
        if (y + 1 < binaryImage.length && x + 1 < binaryImage[y].length && binaryImage[y + 1][x + 1] == 0 && binaryImage[y + 1][x] == 1) {
            count++;
        }
//p6 p7
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x] == 0 && binaryImage[y + 1][x - 1] == 1) {
            count++;
        }
//p7 p8
        if (y + 1 < binaryImage.length && x - 1 >= 0 && binaryImage[y + 1][x - 1] == 0 && binaryImage[y][x - 1] == 1) {
            count++;
        }
//p8 p9
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y][x - 1] == 0 && binaryImage[y - 1][x - 1] == 1) {
            count++;
        }
//p9 p2
        if (y - 1 >= 0 && x - 1 >= 0 && binaryImage[y - 1][x - 1] == 0 && binaryImage[y - 1][x] == 1) {
            count++;
        }
        return count;
    }

    private int getB(int[][] binaryImage, int y, int x) {
        return binaryImage[y - 1][x] + binaryImage[y - 1][x + 1] + binaryImage[y][x + 1]
                + binaryImage[y + 1][x + 1] + binaryImage[y + 1][x] + binaryImage[y + 1][x - 1]
                + binaryImage[y][x - 1] + binaryImage[y - 1][x - 1];
    }


    public class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
    private void mencariChain(Bitmap cumulativeBitmapGrayscale , int titikawalX, int titikawalY) {
        int minimumX, maximumY = titikawalY, minimumY, maximumX = titikawalX;
        minimumX = titikawalX;
        minimumY = titikawalY;
        String Arah = "";
        // 8 ketas, 2 kekanan, 4 kebawah, 6 kekiri
        int lastMove = 8;
        int X = titikawalX;
        int Y = titikawalY;
        int tempX, tempY;
        int gakketemu = 0;
        int lastX = 0;
        int lastY = 0;
        int infinite = 0;
        String loopinloop = "";
        Log.d("CHAINNNFIRSSTTTTT", Arah+" ");

        do {
            if (X == lastX && Y == lastY) {
                infinite++;
            }
            if (X != lastX || Y != lastY) {
                infinite=0;
            }
            if (infinite>=4) {
                gakketemu = 11;
                break;
            }
            lastX = X;
            lastY = Y;
            Log.d("CHAINNN2.0", Arah+" ");
            Log.d("CHAINNN2.1", gakketemu+ " ");
            Log.d("CHAINNN2.2", X+ " " +Y);
            Log.d("CHAINNN2.3", lastMove+" ");

            if (lastMove == 2) {
                tempY = Y;
                tempX = X + 1;
                if (tempX >= cumulativeBitmapGrayscale.getWidth()) {
                    gakketemu = 11;
                    break;
                }
                if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                    Arah += "8";
                    lastMove = 8;
                } else {
                    tempY = Y + 1;
                    if (tempY >= cumulativeBitmapGrayscale.getHeight()) {
                        gakketemu = 11;
                        break;
                    }
                    //cari hitam
                    if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                        Arah += "2";
                        lastMove = 2;
                        X++;
                    } else {
                        tempX = X;
                        if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                            Arah += "3";
                            lastMove = 4;
                            X++;
                            Y++;
                        } else {
                            gakketemu++;
                        }
                    }
                }
            } else if (lastMove == 4) {
                tempY = Y + 1;
                if (tempY >= cumulativeBitmapGrayscale.getHeight()) {
                    gakketemu = 11;
                    break;
                }
                tempX = X;
                if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                    Arah += "2";
                    lastMove = 2;
                } else {
                    tempX = X - 1;
                    if (tempX <= 0) {
                        gakketemu = 11;
                        break;
                    }
                    //cari hitam
                    if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                        Arah += "4";
                        lastMove = 4;
                        Y++;
                    } else {
                        tempY = Y;
                        if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                            Arah += "5";
                            lastMove = 6;
                            Y++;
                            X--;
                        } else {
                            gakketemu++;
                        }
                    }
                }
            } else if (lastMove == 6) {
                tempY = Y;
                tempX = X - 1;
                if (tempX <= 0) {
                    gakketemu = 11;
                    break;
                }
                if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                    Arah += "4";
                    lastMove = 4;
                } else {
                    tempY = Y - 1;
                    if (tempY <= 0) {
                        gakketemu = 11;
                        break;
                    }
                    //cari hitam
                    if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                        Arah += "6";
                        lastMove = 6;
                        X--;
                    } else {
                        tempX = X;
                        if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                            Arah += "7";
                            lastMove = 8;
                            X--;
                            Y--;
                        } else {
                            gakketemu++;
                        }
                    }
                }
            } else if (lastMove == 8) {

                Log.d("MASUKK8", "AAAAAAG");

                //cek dulu diatasnya ada atau enggak klo gda baru lurus
                tempY = Y - 1;
                if (tempY <= 0) {
                    gakketemu = 11;
                    break;
                }
                tempX = X;
                if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                    Arah += "6";
                    lastMove = 6;
                    Log.d("MASUKKkiri", "KIRI");
                } else {
                    tempX = X + 1;
                    if (tempX >= cumulativeBitmapGrayscale.getWidth()) {
                        gakketemu = 11;
                        break;
                    }
                    //cari hitam
                    if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                        Arah += "8";
                        lastMove = 8;
                        Y--;
                        Log.d("MASUKKatas", "atas");
                    } else {
                        tempY = Y;
                        if (Color.red((cumulativeBitmapGrayscale.getPixel(tempX, tempY))) < threshold){
                            if (Arah.length()>16){
                                int last = Arah.lastIndexOf("1");
                                if (last > 0){
                                    int sebelum_last = Arah.substring(0, last).lastIndexOf("1");

                                    String pertama = Arah.substring(last,Arah.length());
                                    Log.d("PERTAMA", pertama);
                                    if (sebelum_last-pertama.length() > 0) {
                                        String kedua = Arah.substring(sebelum_last-pertama.length() , sebelum_last);
                                        Log.d("CHAINNYA", Arah);
                                        Log.d("PERTAMA", pertama);
                                        Log.d("KEDUA", kedua);
                                        if (pertama.contentEquals(kedua)){
                                            gakketemu = 11;
                                            break;
                                        }
                                    }
                                }


                            }



                            Arah += "1";
                            lastMove = 2;
                            X++;
                            Y--;

                            Log.d("MASUKKkanan", "kanan");
                        } else {
                            gakketemu++;
                        }
                    }
                }
            } else {
                gakketemu++;
            }

            if (Y > maximumY) {
                maximumY = Y;
            }
            if (X > maximumX) {
                maximumX = X;
            }

        } while((titikawalX != X || titikawalY != Y) && gakketemu < 10);

        if (gakketemu <= 10) {
            if (Arah.length() >=20 ) {
                listChain.add(Arah);
                listAwalX.add(minimumX);
                listAwalY.add(minimumY);
                for (int i = minimumY; i < maximumY; i++) {
                    for (int j = minimumX; j < maximumX; j++) {
                        cumulativeBitmapGrayscale.setPixel(j,i,Color.rgb(255,255,255));
                    }
                }
            }
        }

//        //delete yg dlm kotakan
//        for (int i = minimumY; i < maximumY; i++) {
//            for (int j = minimumX; j < maximumX; j++) {
//                cumulativeBitmapGrayscale.setPixel(j,i,Color.rgb(255,255,255));
//            }
//        }
//        //kembaliin stringnya
//
//        if (gakketemu >= 10) {
//            if (Arah.length() >=20 ) {
//                listChain.add(Arah);
//            }
//        }
//        listChain.add(Arah);
        Log.d("CHAINNN22222222222", Arah);
    }
    List<Integer> listAwalX = new ArrayList<>();
    List<Integer> listAwalY = new ArrayList<>();
    private void telusur(Bitmap cumulativeBitmapGrayscale , ImageView cumulativeImage){
        tempBitmap = cumulativeBitmapGrayscale.copy(cumulativeBitmapGrayscale.getConfig(), true);
        listAwalX = new ArrayList<>();
        listAwalY = new ArrayList<>();
        FoundDigit = "Digit: ";
        listChain = new ArrayList<String>();
        int maximumX = cumulativeBitmapGrayscale.getWidth();
        int maximumY = cumulativeBitmapGrayscale.getHeight();
        for (int i = 1; i < maximumY; i++) {
            for (int j = 1; j < maximumX; j++) {

                Log.d("CHAINNN1", j+" "+i);
                if (Color.red((cumulativeBitmapGrayscale.getPixel(j,i)))  < threshold) {
                    int tes = j - 1;
                    mencariChain(cumulativeBitmapGrayscale,tes,i);
                    Log.d("CHAINNN3", "MASUKPAKEKO"+ tes + i);

                }

            }
        }
        cumulativeImage.setImageBitmap(cumulativeBitmapGrayscale);
    }

    private void createTargetHistogram(int leftSlider, int middleSlider, int rightSlider) {
        for (int i = 0; i < 256; i++) {
            targethistogram[i] = 0;
            cumulativeTarget[i] = 0;
            targetTransformation[i] = 0;
        }

        // Asumsi puncak di 1000
        int nilaiPuncak = 1000;
        int tinggiKiri = leftSlider;
        int tinggiKanan = rightSlider;
        int posisiPuncak = middleSlider;
        // Asumsi menaik hingga puncak (kecuali kalau tinggi kiri = 1000)
        int selisihKiri = nilaiPuncak - tinggiKiri;
        for (int i = 0; i < posisiPuncak; i++) {
            targethistogram[i] = tinggiKiri + (int) ((float) i / (float) posisiPuncak * (float) selisihKiri);
        }
        int sisaKanan = 256 - posisiPuncak;
        int selisihKanan = nilaiPuncak - tinggiKanan;
        for (int i = posisiPuncak; i < 256; i++) {
            int panjangKanan = sisaKanan - i + posisiPuncak;
            targethistogram[i] = tinggiKanan + (int) ((float) panjangKanan / (float) sisaKanan * (float) selisihKanan);
        }

        // Cumulative Histogram target
        cumulativeTarget[0] = targethistogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeTarget[i] = (int) (cumulativeTarget[i - 1]) + (int) (progressChangedValue * targethistogram[i]);
        }


        // Target transformation
        for (int i = 0; i < 256; i++) {
            targetTransformation[i] = (float) cumulativeTarget[i] / (float) cumulativeTarget[255];
        }
    }

    // Some formula to convert between RGB and YUV
    private int getY (int R, int G, int B) {
        float Y = (float) (0.257 * R + 0.504 * G + 0.098 * B + 16);
        int result = (int) Y;
        return result;
    }

    private int getV (int R, int G, int B) {
        float Y = (float) (0.439 * R - 0.368 * G - 0.071 * B + 128);
        int result = (int) Y;
        return result;
    }

    private int getU (int R, int G, int B) {
        float Y = (float) (-0.148 * R - 0.291 * G + 0.439 * B + 128);
        int result = (int) Y;
        return result;
    }

    private int getR (int Y, int U, int V) {
        float R = (float) (1.164 * (Y - 16) + 1.596 * (V - 128));
        int result = (int) R;
        return result;
    }

    private int getG (int Y, int U, int V) {
        float R = (float) (1.164 * (Y - 16) - 0.813 * (V - 128) - 0.391 * (U - 128));
        int result = (int) R;
        return result;
    }

    private int getB (int Y, int U, int V) {
        float R = (float) (1.164 * (Y - 16) + 2.018 * (U - 128));
        int result = (int) R;
        return result;
    }

    private Bitmap getCumulativeColor(Bitmap bmp) {
        // Need to convert to YUV, transform the YUV and then convert back to RGB
        for (int i = 0; i < 255; i++) {
            // in here, grayscale represents the Y part that will be transformed
            grayscaleHistogram[i] = 0;
            cumulativeHistogram[i] = 0;
            transformationValue[i] = 0;
            probabilityHistogram[i] = 0;
            grayscaleAfterHistogram[i] = 0;
        }

        // Count each pixel occurrence
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                // Look for Y value
                int pixelValue = bmp.getPixel(i,j);
                int Y = getY(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));
                // Increment pixel
                grayscaleHistogram[Y]++;
            }
        }

        // Create cumulative histogram
        cumulativeHistogram[0] = grayscaleHistogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeHistogram[i] = (int) ( cumulativeHistogram[i - 1]) + (int) (progressChangedValue * grayscaleHistogram[i]);
        }

        for (int i = 0; i < 256; i++) {
            probabilityHistogram[i] = (float) cumulativeHistogram[i] / (float)  cumulativeHistogram[255];
        }

        int marker = 0;
        // New transformation value
        for (int i = 0; i < 256; i ++) {
            boolean finish = false;
            while (!finish) {
                if (probabilityHistogram[i] - targetTransformation[marker] <= 0) {
                    transformationValue[i] = (int) (marker * progressChangedValue);
                    finish = true;
                } else {
                    marker++;
                }
            }
        }

//        // Create transformation value
//        for (int i = 0; i< 256; i++) {
//            transformationValue[i] = (int) (cumulativeHistogram[i]*255/totalPixel);
//        }

        Bitmap result = bmp;
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {

                int pixelValue = bmp.getPixel(i,j);
                int Y = getY(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));

                // New Y Value
                int color = transformationValue[Y];
                int U = getU(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));
                int V = getV(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));

                int R = getR(color, U, V);
                int G = getG(color, U, V);
                int B = getB(color, U, V);
                // Normalisasi nilai RGB agar tidak overflow wkwkwkwk
                if (R < 0) {
                    R = 0;
                }
                if (G < 0) {
                    G = 0;
                }
                if (B < 0) {
                    B = 0;
                }
                if (R > 255) {
                    R = 255;
                }
                if (G > 255) {
                    G = 255;
                }
                if (B > 255) {
                    B = 255;
                }
                int colorValue = Color.rgb(R,G,B);
                result.setPixel(i,j,colorValue);
            }
        }
        // Count each pixel occurrence
        for (int i = 0; i < result.getWidth(); i++) {
            for (int j = 0; j < result.getHeight(); j++) {
                // Look for Y value
                int pixelValue = result.getPixel(i,j);
                int Y = getY(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));
                // Increment pixel
                grayscaleAfterHistogram[Y]++;
            }
        }
        return result;
    }

    private Bitmap getCumulativeGrayscale(Bitmap bmp) {
        for (int i = 0; i < 255; i++) {
            grayscaleHistogram[i] = 0;
            cumulativeHistogram[i] = 0;
            transformationValue[i] = 0;
            grayscaleAfterHistogram[i] = 0;
            probabilityHistogram[i] = 0;
        }

        // Count each pixel occurrence
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                // Increment pixel
                grayscaleHistogram[bmp.getPixel(i,j) & 0xff]++;
            }
        }

        // Create cumulative histogram
        cumulativeHistogram[0] = grayscaleHistogram[0];
        for (int i = 1; i < 256; i++) {
            cumulativeHistogram[i] = (int) (cumulativeHistogram[i - 1]) + (int) (progressChangedValue * grayscaleHistogram[i]);
        }

        for (int i = 0; i < 256; i++) {
            probabilityHistogram[i] = (float) cumulativeHistogram[i] / (float) cumulativeHistogram[255];
        }

        int marker = 0;
        // New transformation value
        for (int i = 0; i < 256; i ++) {
            boolean finish = false;
            while (!finish) {
                if (probabilityHistogram[i] - targetTransformation[marker] <= 0) {
                    transformationValue[i] = (int) (marker * progressChangedValue);
                    finish = true;
                } else {
                    marker++;
                }
            }
        }

//        // Create transformation value
//        for (int i = 0; i< 256; i++) {
//            transformationValue[i] = (int) (cumulativeHistogram[i]*255/totalPixel);
//        }

        Bitmap result = bmp;
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                // Increment pixel
                int color = transformationValue[bmp.getPixel(i,j) & 0xff];
                if (color > 255){
                    color = 255;
                }
                int colorValue = Color.rgb(color,color,color);
                result.setPixel(i,j,colorValue);
            }
        }
        for (int i = 0; i < result.getWidth(); i++) {
            for (int j = 0; j < result.getHeight(); j++) {
                // Increment pixel
                grayscaleAfterHistogram[result.getPixel(i,j) & 0xff]++;
            }
        }
        return result;
    }

    private Bitmap blurring(Bitmap bmp) {
        Bitmap result = bmp;
        if (blur > 0) {
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int j = 0; j < bmp.getHeight(); j++) {
                    int jumlah = 0;
                    int total = 0;
                    for (int hor = i - blur; hor <= i + blur; hor++ ){
                        for (int ver = j - blur; ver <= j + blur; ver++ ){
                            if (ver>=0 && ver < bmp.getHeight() && hor>=0 && hor < bmp.getWidth()){
                              jumlah++;
                              total+= bmp.getPixel(hor,ver) & 0xff;
                            }
                        }
                    }
                    int color = (int) (total/jumlah);
                    int colorValue = Color.rgb(color,color,color);
                    result.setPixel(i,j,colorValue);
                }
            }
        }
        return result;
    }

    private Bitmap colorblurring(Bitmap bmp) {
        Bitmap result = bmp;
        if (blur > 0) {
            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int j = 0; j < bmp.getHeight(); j++) {
                    int jumlah = 0;
                    int total = 0;
                    for (int hor = i - blur; hor <= i + blur; hor++ ){
                        for (int ver = j - blur; ver <= j + blur; ver++ ){
                            if (ver>=0 && ver < bmp.getHeight() && hor>=0 && hor < bmp.getWidth()){
                                jumlah++;
                                int pixelValue = bmp.getPixel(hor,ver);
                                int Y = getY(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));
                                total+= Y;
                            }
                        }
                    }
                    int color = (int) (total/jumlah);

                    int pixelValue = bmp.getPixel(i,j);

                    int U = getU(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));
                    int V = getV(Color.red(pixelValue), Color.green(pixelValue), Color.blue(pixelValue));

                    int R = getR(color, U, V);
                    int G = getG(color, U, V);
                    int B = getB(color, U, V);
                    int colorValue = Color.rgb(R,G,B);
                    result.setPixel(i,j,colorValue);
                }
            }
        }
        return result;
    }

    private void recalculate(){
        totalPixel = thumbnailBitmap.getWidth() * thumbnailBitmap.getHeight();

        // Grayscale processing
        grayscaleBitmap = toGrayscale(thumbnailBitmap);
        grayscaleImage.setImageBitmap(grayscaleBitmap);

        graph.removeAllSeries();
        graph2.removeAllSeries();
        graph3.removeAllSeries();
        graph4.removeAllSeries();
        col1.removeAllSeries();
        col2.removeAllSeries();
        gray1.removeAllSeries();
        gray2.removeAllSeries();

        graph.setVisibility(View.VISIBLE);
        graph2.setVisibility(View.VISIBLE);
        graph3.setVisibility(View.VISIBLE);
        graph4.setVisibility(View.VISIBLE);

        gray1.setVisibility(View.GONE);
        gray2.setVisibility(View.GONE);
        col1.setVisibility(View.GONE);
        col2.setVisibility(View.GONE);

        seek1.setVisibility(View.VISIBLE);
        seek2.setVisibility(View.VISIBLE);
        seek3.setVisibility(View.VISIBLE);
        seek4.setVisibility(View.VISIBLE);
        seek5.setVisibility(View.VISIBLE);
        seekSkel.setVisibility(View.VISIBLE);
        digit.setVisibility(View.VISIBLE);
        skel.setVisibility(View.VISIBLE);



        Bitmap copy1 = grayscaleBitmap.copy(grayscaleBitmap.getConfig(), true);
        Bitmap colorCopy1 = thumbnailBitmap.copy(thumbnailBitmap.getConfig(), true);

        Bitmap copy = blurring(copy1);
        Bitmap colorCopy = colorblurring(colorCopy1);

        cumulativeColorBitmap = getCumulativeColor(colorCopy);
        cumulativeColor.setImageBitmap(cumulativeColorBitmap);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        int temp;
        for (int i = 0; i < 256; i++) {
            temp = grayscaleHistogram[i];
            series.appendData(new DataPoint(i, temp),true,256);
        }
        series.setTitle("Color");
        series.setColor(Color.MAGENTA);
        graph.addSeries(series);
        series = new LineGraphSeries<DataPoint>();
        int maxcolor = 0;
        int when = 0;
        for (int i = 0; i < 256; i++) {
            temp = grayscaleAfterHistogram[i];
            series.appendData(new DataPoint(i, temp),true,256);
//            if (maxcolor<temp){
//                maxcolor = temp;
//                when = i;
//            }
        }
//        when+ " "+ maxcolor +
        series.setTitle("Color Transformation");
        series.setColor(Color.RED);
        graph2.addSeries(series);

        graph2.getViewport().setMaxY(maxcolor);
        graph2.getViewport().setMaxX(256);

        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setXAxisBoundsManual(true);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 256; i++) {
            temp = cumulativeHistogram[i];
            series.appendData(new DataPoint(i, temp),true,256);
        }
        series.setTitle("Cumulative Color");
        series.setColor(Color.CYAN);
        col1.addSeries(series);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 256; i++) {
            temp = transformationValue[i];
            series.appendData(new DataPoint(i, temp),true,256);
        }
        series.setTitle("Transformation Value");
        series.setColor(Color.CYAN);
        col2.addSeries(series);
        cumulativeBitmapGrayscale = getCumulativeGrayscale(copy);
        cumulativeImage.setImageBitmap(cumulativeBitmapGrayscale);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 256; i++) {
            temp = grayscaleHistogram[i];
            series.appendData(new DataPoint(i, temp),true,256);
        }
        series.setTitle("Grayscale");
        series.setColor(Color.BLACK);
        graph3.addSeries(series);
        int maxgrey = 0;
        when = 0;
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 256; i++) {
            temp = grayscaleAfterHistogram[i];
            series.appendData(new DataPoint(i, temp),true,256);
//            if (maxgrey < temp){
//                maxgrey = temp;
//                when = i;
//            }
        }
//        when+" "+maxgrey+
        series.setTitle("Grayscale Transformation");
        series.setColor(Color.GRAY);
        graph4.addSeries(series);
        graph4.getViewport().setMaxY(maxgrey);
        graph4.getViewport().setMaxX(256);

        graph4.getViewport().setYAxisBoundsManual(true);
        graph4.getViewport().setXAxisBoundsManual(true);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 256; i++) {
            temp = cumulativeHistogram[i];
            series.appendData(new DataPoint(i, temp),true,256);
        }
        series.setTitle("Cumulative Grayscale");
        series.setColor(Color.CYAN);
        gray1.addSeries(series);
        series = new LineGraphSeries<DataPoint>();
        for (int i = 0; i < 256; i++) {
            temp = transformationValue[i];
            series.appendData(new DataPoint(i, temp),true,256);
        }
        series.setTitle("Transformation Value");
        series.setColor(Color.CYAN);
        gray2.addSeries(series);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_TAKE_PHOTO) {
            if (resultCode != RESULT_CANCELED) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);
                    thumbnailBitmap =  Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.1), (int)(rotatedBitmap.getHeight()*0.1), true);

                    imageView.setImageBitmap(thumbnailBitmap);
                    // Count total pixel
                    totalPixel = thumbnailBitmap.getWidth() * thumbnailBitmap.getHeight();

                    // Grayscale processing
                    grayscaleBitmap = toGrayscale(thumbnailBitmap);
                    grayscaleImage.setImageBitmap(grayscaleBitmap);

                    graph.removeAllSeries();
                    graph2.removeAllSeries();
                    graph3.removeAllSeries();
                    graph4.removeAllSeries();
                    col1.removeAllSeries();
                    col2.removeAllSeries();
                    gray1.removeAllSeries();
                    gray2.removeAllSeries();

                    graph.setVisibility(View.VISIBLE);
                    graph2.setVisibility(View.VISIBLE);
                    graph3.setVisibility(View.VISIBLE);
                    graph4.setVisibility(View.VISIBLE);
                    seek1.setVisibility(View.VISIBLE);
                    seek2.setVisibility(View.VISIBLE);
                    seek3.setVisibility(View.VISIBLE);
                    seek4.setVisibility(View.VISIBLE);
                    seek5.setVisibility(View.VISIBLE);

                    seekSkel.setVisibility(View.VISIBLE);
                    digit.setVisibility(View.VISIBLE);
                    skel.setVisibility(View.VISIBLE);

                    seek1.setProgress(100);
                    seek2.setProgress(1000);
                    seek3.setProgress(0);
                    seek4.setProgress(1000);
                    seek5.setProgress(0);
                    Bitmap copy = grayscaleBitmap.copy(grayscaleBitmap.getConfig(), true);
                    Bitmap colorCopy = thumbnailBitmap.copy(thumbnailBitmap.getConfig(), true);

                    cumulativeColorBitmap = getCumulativeColor(colorCopy);
                    cumulativeColor.setImageBitmap(cumulativeColorBitmap);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                    int temp;
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Color");
                    series.setColor(Color.MAGENTA);
                    graph.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleAfterHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Color Transformation");
                    series.setColor(Color.RED);
                    graph2.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = cumulativeHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Cumulative Color");
                    series.setColor(Color.CYAN);
                    col1.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = transformationValue[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Transformation Value");
                    series.setColor(Color.CYAN);
                    col2.addSeries(series);
                    cumulativeBitmapGrayscale = getCumulativeGrayscale(copy);
                    cumulativeImage.setImageBitmap(cumulativeBitmapGrayscale);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Grayscale");
                    series.setColor(Color.BLACK);
                    graph3.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleAfterHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Grayscale Transformation");
                    series.setColor(Color.GRAY);
                    graph4.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = cumulativeHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Cumulative Grayscale");
                    series.setColor(Color.CYAN);
                    gray1.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = transformationValue[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Transformation Value");
                    series.setColor(Color.CYAN);
                    gray2.addSeries(series);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    thumb.setVisibility(View.VISIBLE);
                    origin.setVisibility(View.VISIBLE);
                }

            }
        } else if (requestCode==2) {
            if (resultCode != RESULT_CANCELED) {
                try {
                    Uri selectedImage = data.getData();

                    String[] filePath = { MediaStore.Images.Media.DATA };

                    Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);

                    c.moveToFirst();

                    int columnIndex = c.getColumnIndex(filePath[0]);

                    String picturePath = c.getString(columnIndex);

                    c.close();

                    rotatedBitmap = (BitmapFactory.decodeFile(picturePath));
                    thumbnailBitmap = Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.3), (int)(rotatedBitmap.getHeight()*0.3), true);
//                    ;
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(90);
//                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                            bitmap.getHeight(), matrix, true);
//                    thumbnailBitmap =  Bitmap.createScaledBitmap(rotatedBitmap,(int)(rotatedBitmap.getWidth()*0.1), (int)(rotatedBitmap.getHeight()*0.1), true);

                    imageView.setImageBitmap(thumbnailBitmap);
                    // Count total pixel
                    totalPixel = thumbnailBitmap.getWidth() * thumbnailBitmap.getHeight();

                    // Grayscale processing
                    grayscaleBitmap = toGrayscale(thumbnailBitmap);
                    grayscaleImage.setImageBitmap(grayscaleBitmap);

                    graph.removeAllSeries();
                    graph2.removeAllSeries();
                    graph3.removeAllSeries();
                    graph4.removeAllSeries();
                    col1.removeAllSeries();
                    col2.removeAllSeries();
                    gray1.removeAllSeries();
                    gray2.removeAllSeries();

                    graph.setVisibility(View.VISIBLE);
                    graph2.setVisibility(View.VISIBLE);
                    graph3.setVisibility(View.VISIBLE);
                    graph4.setVisibility(View.VISIBLE);
                    seek1.setVisibility(View.VISIBLE);
                    seek2.setVisibility(View.VISIBLE);
                    seek3.setVisibility(View.VISIBLE);
                    seek4.setVisibility(View.VISIBLE);
                    seek5.setVisibility(View.VISIBLE);

                    seekSkel.setVisibility(View.VISIBLE);
                    digit.setVisibility(View.VISIBLE);
                    skel.setVisibility(View.VISIBLE);

                    seek1.setProgress(100);
                    seek2.setProgress(1000);
                    seek3.setProgress(0);
                    seek4.setProgress(1000);
                    seek5.setProgress(0);

                    Bitmap copy = grayscaleBitmap.copy(grayscaleBitmap.getConfig(), true);
                    Bitmap colorCopy = thumbnailBitmap.copy(thumbnailBitmap.getConfig(), true);

                    cumulativeColorBitmap = getCumulativeColor(colorCopy);
                    cumulativeColor.setImageBitmap(cumulativeColorBitmap);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                    int temp;
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Color");
                    series.setColor(Color.MAGENTA);
                    graph.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleAfterHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Color Transformation");
                    series.setColor(Color.RED);
                    graph2.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = cumulativeHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Cumulative Color");
                    series.setColor(Color.CYAN);
                    col1.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = transformationValue[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Transformation Value");
                    series.setColor(Color.CYAN);
                    col2.addSeries(series);
                    cumulativeBitmapGrayscale = getCumulativeGrayscale(copy);
                    cumulativeImage.setImageBitmap(cumulativeBitmapGrayscale);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Grayscale");
                    series.setColor(Color.BLACK);
                    graph3.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = grayscaleAfterHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Grayscale Transformation");
                    series.setColor(Color.GRAY);
                    graph4.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = cumulativeHistogram[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Cumulative Grayscale");
                    series.setColor(Color.CYAN);
                    gray1.addSeries(series);
                    series = new LineGraphSeries<DataPoint>();
                    for (int i = 0; i < 256; i++) {
                        temp = transformationValue[i];
                        series.appendData(new DataPoint(i, temp),true,256);
                    }
                    series.setTitle("Transformation Value");
                    series.setColor(Color.CYAN);
                    gray2.addSeries(series);

                } finally {
                    thumb.setVisibility(View.VISIBLE);
                    origin.setVisibility(View.VISIBLE);
                }

            }
        }
    }
    String imageFileName;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "POLA_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public Integer grayscaleFormula(Integer Red, Integer Green, Integer Blue) {
        double formulaResult = 0.21 * Red + 0.72 * Green + 0.07 * Blue;
        Integer temp = (int) formulaResult;
        return temp;
    }
    Hashtable<String,Integer> HexColour = new Hashtable<>();
    Hashtable<Integer,Integer> RedOccurence = new Hashtable<>();
    Hashtable<Integer,Integer> GreenOccurence = new Hashtable<>();
    Hashtable<Integer,Integer> BlueOccurence = new Hashtable<>();
    Hashtable<Integer,Integer> GrayscaleSaturation = new Hashtable<>();
    Hashtable<Integer,Integer> GrayscaleFormula = new Hashtable<>();

    private void processCalculate(Bitmap rotatedBitmap, String imageFileName) throws IOException {
        Bitmap grayscaleBMP = toGrayscale(rotatedBitmap);
        HexColour = new Hashtable<>();
        RedOccurence = new Hashtable<>();
        GreenOccurence = new Hashtable<>();
        BlueOccurence = new Hashtable<>();
        GrayscaleSaturation = new Hashtable<>();
        GrayscaleFormula = new Hashtable<>();
        String tes = "";
        int[][] rgbValues = new int[rotatedBitmap.getWidth()][rotatedBitmap.getHeight()];
        int[][] grayscaleValues = new int[rotatedBitmap.getWidth()][rotatedBitmap.getHeight()];
        for(int i=0; i < rotatedBitmap.getWidth(); i++)
        {
            for(int j=0; j < rotatedBitmap.getHeight(); j++)
            {
                //This is a great opportunity to filter the ARGB values
                rgbValues[i][j] = rotatedBitmap.getPixel(i, j);
                grayscaleValues[i][j] = grayscaleBMP.getPixel(i,j);
//                            tes += Integer.toHexString(rgbValues[i][j]) + " ";
            }
        }
        for(int i=0; i < rotatedBitmap.getWidth(); i++)
        {
            for(int j=0; j < rotatedBitmap.getHeight(); j++)
            {

//                rgbText.append("Tes " + Integer.toHexString(rgbValues[i][j]));
                String hexString = "#" + Integer.toHexString(rgbValues[i][j] & 0x00ffffff);
                String grayscaleString = Integer.toHexString(grayscaleValues[i][j]).substring(2,4);
                Long grayscaleLong = Long.parseLong(grayscaleString, 16);
                Integer grayscaleNumber = grayscaleLong.intValue();
                Log.d("HEXSTRING", hexString);
                int redNumber = Color.red(rgbValues[i][j]);
                int greenNumber = Color.green(rgbValues[i][j]);
                int blueNumber = Color.blue(rgbValues[i][j]);

                // Process hex Colour
                Integer n = HexColour.get(hexString);
                if (n == null) {
                    HexColour.put(hexString, 1);
                } else {
                    HexColour.put(hexString, n + 1);
                }

                // Process Grayscale Saturation
                Integer grayscaleTemp = GrayscaleSaturation.get(grayscaleNumber);
                if (grayscaleTemp == null) {
                    GrayscaleSaturation.put(grayscaleNumber, 1);
                } else {
                    GrayscaleSaturation.put(grayscaleNumber, grayscaleTemp + 1);
                }

                // Process Grayscale Formula
                Integer formulaValue = grayscaleFormula(redNumber, greenNumber, blueNumber);
                Integer formulaTemp = GrayscaleFormula.get(formulaValue);
                if (formulaTemp == null) {
                    GrayscaleFormula.put(formulaValue, 1);
                } else {
                    GrayscaleFormula.put(formulaValue, formulaTemp + 1);
                }

                // Process red Colour
                Integer redTemp = RedOccurence.get(redNumber);
                if (redTemp == null) {
                    RedOccurence.put(redNumber, 1);
                } else {
                    RedOccurence.put(redNumber, redTemp + 1);
                }

                // Process Green Colour
                Integer greenTemp = GreenOccurence.get(greenNumber);
                if (greenTemp == null) {
                    GreenOccurence.put(greenNumber, 1);
                } else {
                    GreenOccurence.put(greenNumber, greenTemp + 1);
                }

                // Process Blue Colour
                Integer blueTemp = BlueOccurence.get(blueNumber);
                if (blueTemp == null) {
                    BlueOccurence.put(blueNumber, 1);
                } else {
                    BlueOccurence.put(blueNumber, blueTemp + 1);
                }
            }
        }

        File root = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Pixel");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, imageFileName+"-GRAYSCALESATURATION.txt");
        FileWriter writer = new FileWriter(gpxfile);


        Log.d("PASSING", "1");
        // Append Grayscale Saturation Text
        writer.append("Grayscale Saturation: \n");
        Enumeration<Integer> saturationEnum = GrayscaleSaturation.keys();
        while (saturationEnum.hasMoreElements()) {
            Integer key = saturationEnum.nextElement();
            writer.append(key + ": " + GrayscaleSaturation.get(key) + "\n");
        }
        writer.flush();
        writer.close();

        gpxfile = new File(root, imageFileName+"-GRAYSCALEFORMULA.txt");
        writer = new FileWriter(gpxfile);
        Log.d("PASSING", "2");
        // Append Grayscale Formula Text
        writer.append("Grayscale Formula: \n");
        Enumeration<Integer> formulaEnum = GrayscaleFormula.keys();
        while (formulaEnum.hasMoreElements()) {
            Integer key = formulaEnum.nextElement();
            writer.append(key + ": " + GrayscaleFormula.get(key) + "\n");
        }
        writer.flush();
        writer.close();
//
        gpxfile = new File(root, imageFileName+"-COLOURRGB.txt");
        writer = new FileWriter(gpxfile);
        Log.d("PASSING", "3");
        // Append RGB Colour Text
        writer.append("COLOUR RGB: \n");
        Enumeration<String> e = HexColour.keys();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            writer.append(key + ": " + HexColour.get(key) + "\n");
        }
        writer.flush();
        writer.close();

        gpxfile = new File(root, imageFileName+"-COLOURRED.txt");
        writer = new FileWriter(gpxfile);
        Log.d("PASSING", "4");
        // Append Red Colour Text
        writer.append("RED: \n");
        Enumeration<Integer> redEnum = RedOccurence.keys();
        while (redEnum.hasMoreElements()) {
            Integer key = redEnum.nextElement();
            writer.append(key + ": " + RedOccurence.get(key) + "\n");
        }
        writer.flush();
        writer.close();

        gpxfile = new File(root, imageFileName+"-COLOURGREEN.txt");
        writer = new FileWriter(gpxfile);
        Log.d("PASSING", "5");
        // Append Green Colour Text
        writer.append("\n GREEN: \n");
        Enumeration<Integer> greenEnum = GreenOccurence.keys();
        while (greenEnum.hasMoreElements()) {
            Integer key = greenEnum.nextElement();
            writer.append(key + ": " + GreenOccurence.get(key) + "\n");
        }
        writer.flush();
        writer.close();

        gpxfile = new File(root, imageFileName+"-COLOURBLUE.txt");
        writer = new FileWriter(gpxfile);
        Log.d("PASSING", "6");
        // Append Green Colour Text
        writer.append("\n BLUE: \n");
        Enumeration<Integer> blueEnum = BlueOccurence.keys();
        while (blueEnum.hasMoreElements()) {
            Integer key = blueEnum.nextElement();
            writer.append(key + ": " + BlueOccurence.get(key) + "\n");
        }
        writer.flush();
        writer.close();

//        progressDialog.dismiss();
    }
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        createTargetHistogram(1000,128,1000);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("ERROR IO");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.ratnadiraw.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("dataTES");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    private void showImage(Uri photoURI){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoURI, "image/*");
        startActivity(intent);
    }



    private void selectImage() {



        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };



        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    dispatchTakePictureIntent();

                }

                else if (options[item].equals("Choose from Gallery"))

                {
                    createTargetHistogram(1000,128,1000);
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);



                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }
    ProgressDialog progressDialog;
    Handler handler,handler1;
    Button thumb, origin, digit, skel;
    TextView textFile;
    GraphView graph,graph2,graph3,graph4,col1,col2,gray1,gray2;
    SeekBar seek1,seek2,seek3,seek4,seek5,seekSkel;
    float progressChangedValue = 1;
    int A=0,B=0,C=0, blur = 0,onfull = 0, digitOn = 0, skelOn = 1, threshold = 80;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        imageView = (ImageView) this.findViewById(R.id.imageView);
        grayscaleImage = (ImageView) this.findViewById(R.id.imageView2);
        cumulativeImage = (ImageView) this.findViewById(R.id.imageView3);
        cumulativeColor = (ImageView) this.findViewById(R.id.imageView4);
        RecognizedDigit = (TextView) this.findViewById(R.id.textRGB);
        textFile = (TextView) this.findViewById(R.id.textFile);
        
        
        
        
        textFile.setVisibility(View.GONE);


        generatePatternAngka();
        thumb = (Button) this.findViewById(R.id.button1);
        origin = (Button) this.findViewById(R.id.button2);
        digit = (Button) this.findViewById(R.id.buttonDigit);
        skel = (Button) this.findViewById(R.id.buttonSkeleton);

        seek1 = (SeekBar) this.findViewById(R.id.seek1);
        seek2 = (SeekBar) this.findViewById(R.id.seek2);
        seek3 = (SeekBar) this.findViewById(R.id.seek3);
        seek4 = (SeekBar) this.findViewById(R.id.seek4);
        seek5 = (SeekBar) this.findViewById(R.id.seek5);
        seekSkel = (SeekBar) this.findViewById(R.id.seekThresholdSkel);

        seek1.setVisibility(View.INVISIBLE);
        seek2.setVisibility(View.INVISIBLE);
        seek3.setVisibility(View.INVISIBLE);
        seek4.setVisibility(View.INVISIBLE);
        seek5.setVisibility(View.INVISIBLE);
        seekSkel.setVisibility(View.INVISIBLE);

        thumb.setVisibility(View.INVISIBLE);
        origin.setVisibility(View.INVISIBLE);
        digit.setVisibility(View.INVISIBLE);
        skel.setVisibility(View.INVISIBLE);
        
        
        graph = (GraphView) findViewById(R.id.graph);
        graph2 = (GraphView) findViewById(R.id.graph2);
        graph3 = (GraphView) findViewById(R.id.graph3);
        graph4 = (GraphView) findViewById(R.id.graph4);
        col1 = (GraphView) findViewById(R.id.col1);
        col2 = (GraphView) findViewById(R.id.col2);
        gray1 = (GraphView) findViewById(R.id.gray1);
        gray2 = (GraphView) findViewById(R.id.gray2);
        graph.getLegendRenderer().setVisible(true);
        graph2.getLegendRenderer().setVisible(true);
        graph3.getLegendRenderer().setVisible(true);
        graph4.getLegendRenderer().setVisible(true);
        col1.getLegendRenderer().setVisible(true);
        col2.getLegendRenderer().setVisible(true);
        gray1.getLegendRenderer().setVisible(true);
        gray2.getLegendRenderer().setVisible(true);
        graph.setVisibility(View.INVISIBLE);
        graph2.setVisibility(View.INVISIBLE);
        graph3.setVisibility(View.INVISIBLE);
        graph4.setVisibility(View.INVISIBLE);
        col1.setVisibility(View.GONE);
        col2.setVisibility(View.GONE);
        gray1.setVisibility(View.GONE);
        gray2.setVisibility(View.GONE);
        RecognizedDigit.setVisibility(View.VISIBLE);
        seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress++;
                }
                progressChangedValue = (float) (progress/100.0);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Omega number is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                recalculate();
            }
        });


        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress++;
                }
                A = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "A number is :" + A,
                        Toast.LENGTH_SHORT).show();
                createTargetHistogram(A,B,C);
                recalculate();
            }
        });
        seek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress++;
                }
                B = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "B number is :" + B,
                        Toast.LENGTH_SHORT).show();
                createTargetHistogram(A,B,C);
                recalculate();
            }
        });
        seek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    progress++;
                }
                C = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "C number is :" + C,
                        Toast.LENGTH_SHORT).show();
                createTargetHistogram(A,B,C);
                recalculate();
            }
        });

        seek5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blur = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Blur number is :" + blur,
                        Toast.LENGTH_SHORT).show();
                recalculate();
            }
        });

        seekSkel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Threshold number is :" + threshold,
                        Toast.LENGTH_SHORT).show();
                recalculate();
            }
        });
        
        textFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri selectedUri = Uri.parse(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/Pixel/");
                Intent intent = new Intent(Intent.ACTION_VIEW);

                Log.d("Click Open File", "CLICKEEED"+selectedUri);
                intent.setDataAndType(selectedUri, "resource/folder");
                if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
                    startActivity(intent);
                } else {
                    // if you reach this place, it means there is no any file
                    // explorer app installed on your device
                }
            }
        });
        ImageButton fab = (ImageButton) findViewById(R.id.camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();

            }
        });
        digit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                digitOn = 1;
                skelOn = 0;
                seekSkel.setProgress(80);
                threshold = 80;

            }
        });
        skel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                digitOn = 0;
                skelOn = 1;
                seekSkel.setProgress(80);
                threshold = 80;
                RecognizedDigit.setText("");

            }
        });

        cumulativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onfull ==0){
                    onfull = 1;
                } else {
                    onfull = 0;
                }
                //                try {
//                    //Write file
//                    String filename = "bitmap.png";
//                    FileOutputStream stream = openFileOutput(filename, Context.MODE_PRIVATE);
//                    Bitmap bmp = cumulativeColorBitmap;
//                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//                    //Cleanup
//                    stream.close();
//                    bmp.recycle();
//
//                    //Pop intent
//                    Intent in1 = new Intent(getApplication(), ZoomImage.class);
//                    in1.putExtra("image", filename);
//                    startActivity(in1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });
        cumulativeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (digitOn == 1) {
                        telusur(cumulativeBitmapGrayscale, cumulativeImage);
                        recognizeDigit();
                        String resultText = "bbb";
                        if (listChain.size()>0){
                            resultText = listChain.get(0);
                        } else {
                            resultText = "aaa";
                        }
                } else if (skelOn == 1) {
                        convertToBinary(cumulativeBitmapGrayscale, cumulativeImage);
                }


            }
        });
        grayscaleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (digitOn == 1) {
                    telusur(grayscaleBitmap,grayscaleImage);
                    recognizeDigit();
                    String resultText = "bbb";
                    if (listChain.size()>0){
                        resultText = listChain.get(0);
                    } else {
                        resultText = "aaa";
                    }
                } else if (skelOn == 1) {
                    convertToBinary(grayscaleBitmap,grayscaleImage);
                }
            }
        });
        origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gray1.setVisibility(View.VISIBLE);
                gray2.setVisibility(View.VISIBLE);
                graph3.setVisibility(View.VISIBLE);
                graph4.setVisibility(View.VISIBLE);
                col1.setVisibility(View.GONE);
                col2.setVisibility(View.GONE);
                graph.setVisibility(View.GONE);
                graph2.setVisibility(View.GONE);

            }
        });
        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                col1.setVisibility(View.VISIBLE);
                col2.setVisibility(View.VISIBLE);
                graph.setVisibility(View.VISIBLE);
                graph2.setVisibility(View.VISIBLE);
                gray1.setVisibility(View.GONE);
                gray2.setVisibility(View.GONE);
                graph3.setVisibility(View.GONE);
                graph4.setVisibility(View.GONE);

            }
        });
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0) {
//                    textFile.setVisibility(View.VISIBLE);
//                    int max = 0;
//                    rgbText.append("\n RED (SAMPLE 20): \n");
//                    Enumeration<Integer> redEnum = RedOccurence.keys();
//                    while (max<21) {
//                        Integer key = redEnum.nextElement();
//                        max++;
//                        rgbText.append(key + ": " + RedOccurence.get(key) + "\n");
//                    }
//
//                    max = 0;
//                    rgbText.append("\n GREEN (SAMPLE 20): \n");
//                    Enumeration<Integer> greenEnum = GreenOccurence.keys();
//                    while (max<21) {
//                        Integer key = greenEnum.nextElement();
//                        max++;
//                        rgbText.append(key + ": " + GreenOccurence.get(key) + "\n");
//                    }
//                    max = 0;
//                    // Append Green Colour Text
//                    rgbText.append("\n BLUE (SAMPLE 20): \n");
//                    Enumeration<Integer> blueEnum = BlueOccurence.keys();
//                    while (max<21) {
//                        Integer key = blueEnum.nextElement();
//                        max++;
//                        rgbText.append(key + ": " + BlueOccurence.get(key) + "\n");
//                    }
//
//                    rgbText.append("\n Grayscale Saturation (SAMPLE 20): \n");
//                    Enumeration<Integer> saturationEnum = GrayscaleSaturation.keys();
//                    max = 0;
//                    while (max<21) {
//                        Integer key = saturationEnum.nextElement();
//                        max++;
//                        rgbText.append(key + ": " + GrayscaleSaturation.get(key) + "\n");
//                    }
//
//                    max = 0;
//                    rgbText.append("\n Grayscale Formula (SAMPLE 20): \n");
//                    Enumeration<Integer> formulaEnum = GrayscaleFormula.keys();
//                    while (max<21) {
//                        Integer key = formulaEnum.nextElement();
//                        max++;
//                        rgbText.append(key + ": " + GrayscaleFormula.get(key) + "\n");
//                    }
//
//                    max = 0;
//                    rgbText.append("\n COLOUR RGB (SAMPLE 20): \n");
//                    Enumeration<String> e = HexColour.keys();
//                    while (max<21) {
//                        String key = e.nextElement();
//                        max++;
//                        rgbText.append(key + ": " + HexColour.get(key) + "\n");
//                    }
//                    progressDialog.dismiss();
//                } else {
//                    progressDialog.show();
//                }
//            };
//        };
//        handler1 = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0) {
//
//                    textFile.setVisibility(View.VISIBLE);
//
//
//                    rgbText.append("\n RED: \n");
//                    Enumeration<Integer> redEnum = RedOccurence.keys();
//                    while (redEnum.hasMoreElements()) {
//                        Integer key = redEnum.nextElement();
//                        rgbText.append(key + ": " + RedOccurence.get(key) + "\n");
//                    }
//
//                    rgbText.append("\n GREEN: \n");
//                    Enumeration<Integer> greenEnum = GreenOccurence.keys();
//                    while (greenEnum.hasMoreElements()) {
//                        Integer key = greenEnum.nextElement();
//                        rgbText.append(key + ": " + GreenOccurence.get(key) + "\n");
//                    }
//                    // Append Green Colour Text
//                    rgbText.append("\n BLUE: \n");
//                    Enumeration<Integer> blueEnum = BlueOccurence.keys();
//                    while (blueEnum.hasMoreElements()) {
//                        Integer key = blueEnum.nextElement();
//                        rgbText.append(key + ": " + BlueOccurence.get(key) + "\n");
//                    }
//                    rgbText.append("\n Grayscale Saturation: \n");
//                    Enumeration<Integer> saturationEnum = GrayscaleSaturation.keys();
//                    while (saturationEnum.hasMoreElements()) {
//                        Integer key = saturationEnum.nextElement();
//                        rgbText.append(key + ": " + GrayscaleSaturation.get(key) + "\n");
//                    }
//
//                    rgbText.append("\n Grayscale Formula: \n");
//                    Enumeration<Integer> formulaEnum = GrayscaleFormula.keys();
//                    while (formulaEnum.hasMoreElements()) {
//                        Integer key = formulaEnum.nextElement();
//                        rgbText.append(key + ": " + GrayscaleFormula.get(key) + "\n");
//                    }
//
//                    rgbText.append("\n COLOUR RGB : \n");
//                    Enumeration<String> e = HexColour.keys();
//                    while (e.hasMoreElements()) {
//                        String key = e.nextElement();
//                        rgbText.append(key + ": " + HexColour.get(key) + "\n");
//                    }
//
//
//                    progressDialog.dismiss();
//                } else {
//                    progressDialog.show();
//                }
//            };
//        };
//
//
//
//
//        origin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(progressDialog==null)
//                {
//                    progressDialog=new ProgressDialog(MainActivity.this);
//                    progressDialog.setMessage("Calculated....");
//                    progressDialog.setIndeterminate(false);
//                    progressDialog.setCancelable(false);
//                }
//
//
//                progressDialog.show();
//                Thread ProgressThread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            processCalculate(rotatedBitmap, imageFileName+"-ORIGINAL");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//
//
//                            handler.sendEmptyMessage(0);
//                        }
//                    }
//                };
//                ProgressThread.start();
//            }
//        });
//
//        thumb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(progressDialog==null)
//                {
//                    progressDialog=new ProgressDialog(MainActivity.this);
//                    progressDialog.setMessage("Calculated....");
//                    progressDialog.setIndeterminate(false);
//                    progressDialog.setCancelable(false);
//                }
//
//
//                progressDialog.show();
//                Thread ProgressThread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            processCalculate(thumbnailBitmap, imageFileName+"-THUMBNAIL");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            handler1.sendEmptyMessage(0);
//                        }
//                    }
//                };
//                ProgressThread.start();
//            }
//        });
    }

}
