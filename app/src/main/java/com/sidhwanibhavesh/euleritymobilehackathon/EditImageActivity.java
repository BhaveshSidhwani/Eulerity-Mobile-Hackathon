package com.sidhwanibhavesh.euleritymobilehackathon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditImageActivity extends AppCompatActivity {

    private Image image;
    private Bitmap bitmap;
    private Bitmap bitmapWithFilter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        imageView = findViewById(R.id.edit_image_view);
        Button blackWhiteBtn = findViewById(R.id.b_w_btn);
        Button sepiaBtn = findViewById(R.id.sepia_btn);
        Button inversionBtn = findViewById(R.id.inversion_btn);
        Button redTintBtn = findViewById(R.id.red_tint_btn);
        Button greenTintBtn = findViewById(R.id.green_tint_btn);
        Button blueTintBtn = findViewById(R.id.blue_tint_btn);
        Button overlayTextBtn = findViewById(R.id.overlay_text_btn);
        Button uploadBtn = findViewById(R.id.upload_btn);

        EditText inputText = findViewById(R.id.text_input);
        EditText XText = findViewById(R.id.x_input);
        EditText YText = findViewById(R.id.y_input);
        EditText sizeText = findViewById(R.id.size_input);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.INTENT_EXTRA_POS_KEY)) {
            int position = intent.getIntExtra(Constants.INTENT_EXTRA_POS_KEY, -1);
            image = Constants.imageArray.get(position);

            bitmap = image.imageBitmap;
            imageView.setImageBitmap(bitmap);
        }

        bitmapWithFilter = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        blackWhiteBtn.setOnClickListener(view -> applyBWFilter());

        sepiaBtn.setOnClickListener(view -> applySepiaFilter());

        inversionBtn.setOnClickListener(view -> applyInversionFilter());

        redTintBtn.setOnClickListener(view -> {
            float redValue = 1.0f;
            float greenValue = 0.2f;
            float blueValue = 0.2f;
            applyTintFilter(redValue, greenValue, blueValue);
        });

        greenTintBtn.setOnClickListener(view -> {
            float redValue = 0.2f;
            float greenValue = 1.0f;
            float blueValue = 0.2f;
            applyTintFilter(redValue, greenValue, blueValue);
        });

        blueTintBtn.setOnClickListener(view -> {
            float redValue = 0.2f;
            float greenValue = 0.2f;
            float blueValue = 1.f;
            applyTintFilter(redValue, greenValue, blueValue);
        });

        overlayTextBtn.setOnClickListener(view -> {
            String text = inputText.getText().toString();
            try {
                float x = Float.parseFloat(XText.getText().toString());
                float y = Float.parseFloat(YText.getText().toString());
                int size = Integer.parseInt(sizeText.getText().toString());
                overlayText(text, x, y, size);
            } catch (NumberFormatException e) {
                Toast.makeText(this,
                        "Please fill the values before continuing",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        uploadBtn.setOnClickListener(view -> {
            HttpRequestExecutor.getUploadImageUrl(image);
//            Intent intent1 = new Intent(EditImageActivity.this, TempActivity.class);
//            startActivity(intent1);
        });
    }

    private void applyBWFilter() {
        if (bitmap != null) {
            Bitmap filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

            Canvas canvas = new Canvas(filteredBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(filter);
            canvas.drawBitmap(filteredBitmap, 0, 0, paint);

            imageView.setImageBitmap(filteredBitmap);
            Constants.testBitmap = filteredBitmap;
            bitmapWithFilter = filteredBitmap;
        }
    }

    private void applySepiaFilter() {
        if (bitmap != null) {
            Bitmap filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.set(new float[]{
                    0.393f, 0.769f, 0.189f, 0, 0,
                    0.349f, 0.686f, 0.168f, 0, 0,
                    0.272f, 0.534f, 0.131f, 0, 0,
                    0, 0, 0, 1, 0
            });

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

            Canvas canvas = new Canvas(filteredBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(filter);
            canvas.drawBitmap(filteredBitmap, 0, 0, paint);

            imageView.setImageBitmap(filteredBitmap);
            Constants.testBitmap = filteredBitmap;
            bitmapWithFilter = filteredBitmap;
        }
    }

    private void applyInversionFilter() {
        if (bitmap != null) {
            Bitmap filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.set(new float[] {
                    -1,  0,  0,  0, 255,
                    0, -1,  0,  0, 255,
                    0,  0, -1,  0, 255,
                    0,  0,  0,  1,   0
            });

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

            Canvas canvas = new Canvas(filteredBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(filter);
            canvas.drawBitmap(filteredBitmap, 0, 0, paint);

            imageView.setImageBitmap(filteredBitmap);
            Constants.testBitmap = filteredBitmap;
            bitmapWithFilter = filteredBitmap;
        }
    }

    private void applyTintFilter(float redValue, float greenValue, float blueValue) {
        if (bitmap != null) {
            Bitmap filteredBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.set(new float[] {
                    redValue, 0, 0, 0, 0,
                    0, greenValue, 0, 0, 0,
                    0, 0, blueValue, 0, 0,
                    0, 0, 0, 1, 0
            });

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

            Canvas canvas = new Canvas(filteredBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(filter);
            canvas.drawBitmap(filteredBitmap, 0, 0, paint);

            imageView.setImageBitmap(filteredBitmap);
            Constants.testBitmap = filteredBitmap;
            bitmapWithFilter = filteredBitmap;
        }
    }

    private void overlayText(String text, float x, float y, int size) {
        if (bitmapWithFilter != null) {
            Bitmap bitmapWithText = bitmapWithFilter.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(bitmapWithText);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(size);

            canvas.drawText(text, x, y, paint);

            imageView.setImageBitmap(bitmapWithText);
            Constants.testBitmap = bitmapWithText;
        }
    }

}