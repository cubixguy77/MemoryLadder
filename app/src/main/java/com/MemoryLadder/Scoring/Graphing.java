package com.MemoryLadder.Scoring;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;

import com.MemoryLadder.Utils;

class Graphing {
    private String[] scores;
    private int width;
    private int height;
    //	private static int draw_only_this_idx = -1;
    private static int[] drawSizes;

    Graphing(int width, int height, String[] scores) {
        this.width = width;
        this.height = height;
        this.scores = scores;
    }

    Bitmap getGraph() {

        //	scores = new String[] {"11","14","24","4","5","6","7","8","9","4","5","6","7","8","9","4","5","6","7","8","9","4","5","6","7","8","9","4","5","6","7","8","9","4","5","6","7","8","9"};//, "4", "5", "6", "7", "8", "9", "10" };

        Bitmap emptyBmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        //   Bitmap charty = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap charty = quicky_XY(emptyBmap, scores);
        return charty;
    }

    /* Draws the rounded rectangle in the background */
    private Bitmap quicky_XY(Bitmap bitmap, String[] scores) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff0B0B61;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //    final RectF rectF = new RectF(rect);
        //    final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        draw_the_grid(canvas);

        plot_array_list(canvas, scores, "the title", 0);

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    // these_labels is vector of [label,units,max.min]
    private void draw_the_grid(Canvas this_g) {
        int left_margin_d;

        double rounded_max = get_ceiling_or_floor(Integer.parseInt(Utils.getMaxValue(scores)), true);
        double rounded_min = get_ceiling_or_floor(Integer.parseInt(Utils.getMinValue(scores)), false);

        final Paint paint = new Paint();
        paint.setTextSize(25);

        left_margin_d = getCurTextLengthInPixels(paint, Double.toString(rounded_max));
        int p_height = height - 5;
        int p_width = width - 5;
        int extraheight = 15;
        int[] tmp_draw_sizes = {2 + left_margin_d, extraheight, p_width - 2 - left_margin_d, p_height - extraheight - 15};
        drawSizes = tmp_draw_sizes;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        this_g.drawRect(drawSizes[0], drawSizes[1], drawSizes[0] + drawSizes[2], drawSizes[1] + drawSizes[3], paint);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        this_g.drawRect(drawSizes[0], drawSizes[1], drawSizes[0] + drawSizes[2], drawSizes[1] + drawSizes[3], paint);
        for (int i = 1; i < 5; i++) {
            this_g.drawLine(drawSizes[0], drawSizes[1] + (i * drawSizes[3] / 5), drawSizes[0] + drawSizes[2], drawSizes[1] + (i * drawSizes[3] / 5), paint);
            this_g.drawLine(drawSizes[0] + (i * drawSizes[2] / 5), drawSizes[1], drawSizes[0] + (i * drawSizes[2] / 5), drawSizes[1] + drawSizes[3], paint);
        }

        print_axis_values_4_grid(this_g, "Score", Double.toString(rounded_max), Double.toString(rounded_min), 2);
    }  // --- end of draw_grid ---

    private void print_axis_values_4_grid(Canvas thisDrawingArea, String cur_units, String cur_max, String cur_min, int x_guide) {
        String this_str;
        double delta = (Double.valueOf(cur_max).doubleValue() - Double.valueOf(cur_min).doubleValue()) / 5;

        final Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setTextSize(25);

        for (int i = 0; i < 6; i++) {
            this_str = Double.toString((Double.valueOf(cur_min).intValue() + delta * i));

            final int point = this_str.indexOf('.');
            if (point > 0) {
                // If has a decimal point, may need to clip off after or force 2 decimal places
                this_str = this_str.substring(0, point);
                if (this_str.length() == 1)
                    this_str = " " + this_str;
            } else
                this_str = this_str + ".00";

            thisDrawingArea.drawText(this_str, x_guide + 10, drawSizes[1] + drawSizes[3] - (i * drawSizes[3] / 5) + 10, paint);
        }

        //   thisDrawingArea.drawText("Time", drawSizes[0] + (drawSizes[2] / 2) - 5, drawSizes[1] + drawSizes[3] + 20, paint);

    }  // --- end of print_axis_values_4_grid ---

    private Point scale_point(int this_x, double this_y, Point drawPoint,
                              int scr_x, int scr_y, int scr_width, int src_height,
                              double maxX, double minX, double maxY, double minY) {
        int temp_x, temp_y;
        Point temp = new Point();

        if (maxY == minY)  //skip bad data
            return null;

        //don't touch it if is nothing
        try {
            temp_x = scr_x + (int) (((double) this_x - minX) * ((double) scr_width / (maxX - minX)));
            temp_y = scr_y + (int) ((maxY - this_y) * ((double) src_height / (maxY - minY)));

            temp.x = temp_x;
            temp.y = temp_y;
            drawPoint = temp;
        } catch (Exception e) {
            return (null);
        }
        return temp;

    } // --- end of scale_point --

    private boolean plot_array_list(Canvas this_g, String[] this_array_list, String this_title, int only_this_idx) {
        int lRow;
        int points_2_plot;
        int prev_x, prev_y;
        int cur_x = 0, cur_y = 0;

        Point cur_point = new Point();
        cur_point.set(0, 0);

        double cur_maxX, cur_minX, cur_maxY = 20, cur_minY = 0;
        int cur_start_x, cur_points_2_plot;
        int POINTS_TO_CHANGE = 30;
        double cur_OBD_val;

        String curElt;
        final Paint paint = new Paint();

        try {
            points_2_plot = this_array_list.length;
            {
                cur_start_x = 0;
                cur_points_2_plot = points_2_plot;
                cur_maxX = cur_points_2_plot;
                cur_minX = 0;
            }

            curElt = this_array_list[0];

            paint.setStyle(Paint.Style.STROKE);
            {
                cur_maxY = get_ceiling_or_floor(Double.parseDouble(Utils.getMaxValue(scores)), true);
                cur_minY = get_ceiling_or_floor(Double.parseDouble(Utils.getMinValue(scores)), false);

                cur_points_2_plot = this_array_list.length;
                cur_maxX = cur_points_2_plot;

                curElt = this_array_list[0];
                cur_OBD_val = Double.parseDouble(curElt);

                cur_point = scale_point(0, cur_OBD_val, cur_point,
                        drawSizes[0], drawSizes[1], drawSizes[2], drawSizes[3],
                        cur_maxX, cur_minX, cur_maxY, cur_minY);

                cur_x = cur_point.x;
                cur_y = cur_point.y;

                paint.setColor(Color.RED);
                paint.setAntiAlias(true);


                // the point is only cool when samples are low
                if (cur_points_2_plot < POINTS_TO_CHANGE)
                    this_g.drawRect(cur_x - 2, cur_y - 2, cur_x - 2 + 4, cur_y - 2 + 4, paint);

                prev_x = cur_x;
                prev_y = cur_y;

                paint.setStrokeWidth(4);

                //'go and plot point for this parm -- pont after the 1st one
                for (lRow = cur_start_x + 1; lRow < cur_start_x + cur_points_2_plot; lRow++) {
                    curElt = this_array_list[lRow];

                    cur_OBD_val = Double.parseDouble(curElt);

                    if (cur_OBD_val == Double.NaN) continue;
                    {
                        cur_point = scale_point(lRow, cur_OBD_val, cur_point,
                                drawSizes[0], drawSizes[1], drawSizes[2], drawSizes[3],
                                cur_maxX, cur_minX, cur_maxY, cur_minY);

                        cur_x = cur_point.x;
                        cur_y = cur_point.y;

                        if (cur_points_2_plot < POINTS_TO_CHANGE)
                            this_g.drawRect(cur_x - 2, cur_y - 2, cur_x - 2 + 4, cur_y - 2 + 4, paint);

                        this_g.drawLine(prev_x, prev_y, cur_x, cur_y, paint);
                        prev_x = cur_x;
                        prev_y = cur_y;
                    } // ' if end of this_array(lRow, nParms - 1)<> nothing
                } // end of for lrow
            } // end of for nParmns
            return (true);
        } catch (Exception e) {
            return (false);
        }

    } // --- end of plot_array_list  --


    // need the width of the labels
    private int getCurTextLengthInPixels(Paint this_paint, String this_text) {
        Rect rect = new Rect();
        this_paint.getTextBounds(this_text, 0, this_text.length(), rect);
        return rect.width();
    } // --- end of getCurTextLengthInPixels  ---


    private double get_ceiling_or_floor(double this_val, boolean is_max) {
        if (is_max && this_val <= 5)
            return 5.0;
        else if (is_max && this_val < 10)
            return 10.0;
        else if (is_max && this_val < 15)
            return 15.0;
        else if (is_max && this_val < 20)
            return 20.0;
        else if (is_max && this_val < 25)
            return 25.0;


        double this_min_tmp;
        int this_sign;
        int this_10_factor = 0;
        double this_rounded;

        if (this_val == 0.0) {
            return 0.0;
        }

        this_min_tmp = Math.abs(this_val);
        if (this_min_tmp >= 1.0 && this_min_tmp < 10.0)
            this_10_factor = 1;
        else if (this_min_tmp < 100.0)
            this_10_factor = 10;
        else if (this_min_tmp < 1000.0)
            this_10_factor = 100;
        else if (this_min_tmp < 10000.0)
            this_10_factor = 1000;
        else if (this_min_tmp < 100000.0)
            this_10_factor = 10000;

        if (is_max) {
            if (this_val > 0.0)
                this_sign = 1;
            else
                this_sign = -1;
        } else {
            if (this_val > 0.0)
                this_sign = -1;
            else
                this_sign = 1;
        }
        if (this_min_tmp > 1)
            this_rounded = (double) (((int) (this_min_tmp / this_10_factor) + this_sign) * this_10_factor);
        else {
            this_rounded = (int) (this_min_tmp * 100.0);
            //' cover same as above bfir number up to .001 less than tha it will skip
            if (this_rounded >= 1 && this_rounded < 9)
                this_10_factor = 1;
            else if (this_rounded >= 10 && this_rounded < 99)
                this_10_factor = 10;
            else if (this_rounded >= 100 && this_rounded < 999)
                this_10_factor = 100;

            this_rounded = (double) (((int) ((this_rounded) / this_10_factor) + this_sign) * this_10_factor);
            this_rounded = (int) (this_rounded) / 100.0;
        }

        if (this_val < 0)
            this_rounded = -this_rounded;

        return this_rounded;
    } // --- end of get_ceiling_or_floor ---
}