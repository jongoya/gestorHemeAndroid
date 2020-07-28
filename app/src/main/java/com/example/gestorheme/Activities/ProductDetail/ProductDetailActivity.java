package com.example.gestorheme.Activities.ProductDetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.ClientDetail.ClientDetailActivity;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.R;
import com.makeramen.roundedimageview.RoundedImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView titulo;
    private TextView nombreLabel;
    private TextView nombreField;
    private TextView cantidadLabel;
    private TextView cantidadField;
    private TextView precioLabel;
    private TextView precioField;
    private TextView barrasLabel;
    private TextView barrasField;
    private RelativeLayout rootLayout;
    private RelativeLayout saveButton;
    private RelativeLayout nombreDivisory;
    private RelativeLayout cantidadDivisory;
    private RelativeLayout precioDivisory;
    private RelativeLayout loadingState;
    private RoundedImageView productImage;
    private ImageView nombreArrow;
    private ImageView cantidadArrow;
    private ImageView precioArrow;
    private ImageView saveImage;

    private ProductoModel producto;
    private final int NOMBRE_REF = 0;
    private final int CANTIDAD_REF = 1;
    private final int PRECIO_REF = 2;
    private final int IMG_REF = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_layout);
        AppStyle.setStatusBarColor(this);
        getProductIntent();
        getFields();
        customizeButton();
        customizeArrows();
        customizeLabels();
        customizeViews();
        setOnCLickListeners();

        if (producto.getProductoId() == 0) {
            productImage.setImageResource(R.drawable.add_image);
            productImage.setCornerRadius(0);
            productImage.setColorFilter(AppStyle.getPrimaryTextColor());
            barrasField.setText(producto.getCodigoBarras());
        } else {
            setProductoDetails();
        }
    }

    private void getProductIntent() {
        producto = (ProductoModel) getIntent().getSerializableExtra("producto");
    }

    private void getFields() {
        titulo = findViewById(R.id.titulo);
        nombreLabel = findViewById(R.id.nombreLabel);
        nombreField = findViewById(R.id.nombreField);
        cantidadLabel = findViewById(R.id.cantidadLabel);
        cantidadField = findViewById(R.id.cantidadField);
        precioLabel = findViewById(R.id.precioLabel);
        precioField = findViewById(R.id.precioField);
        barrasLabel = findViewById(R.id.barrasLabel);
        barrasField = findViewById(R.id.barrasField);
        rootLayout = findViewById(R.id.root);
        saveButton = findViewById(R.id.saveButton);
        nombreDivisory = findViewById(R.id.nombreDivisory);
        cantidadDivisory = findViewById(R.id.cantidadDivisory);
        precioDivisory = findViewById(R.id.precioDivisory);
        productImage = findViewById(R.id.img);
        nombreArrow = findViewById(R.id.nombreArrow);
        cantidadArrow = findViewById(R.id.cantidadArrow);
        precioArrow = findViewById(R.id.precioArrow);
        saveImage = findViewById(R.id.saveImage);
    }

    private void customizeButton() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), saveButton, saveImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
    }

    private void customizeViews() {
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
        nombreDivisory.setBackgroundColor(AppStyle.getSecondaryColor());
        cantidadDivisory.setBackgroundColor(AppStyle.getSecondaryColor());
        precioDivisory.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void customizeLabels() {
        titulo.setTextColor(AppStyle.getPrimaryTextColor());
        nombreLabel.setTextColor(AppStyle.getPrimaryTextColor());
        cantidadLabel.setTextColor(AppStyle.getPrimaryTextColor());
        precioLabel.setTextColor(AppStyle.getPrimaryTextColor());
        barrasLabel.setTextColor(AppStyle.getPrimaryTextColor());
        nombreField.setTextColor(AppStyle.getSecondaryTextColor());
        precioField.setTextColor(AppStyle.getSecondaryTextColor());
        cantidadField.setTextColor(AppStyle.getSecondaryTextColor());
        barrasField.setTextColor(AppStyle.getSecondaryTextColor());
    }

    private void customizeArrows() {
        nombreArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        cantidadArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        precioArrow.setColorFilter(AppStyle.getSecondaryTextColor());

    }

    private void setOnCLickListeners() {
        findViewById(R.id.nombreLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", nombreField.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_TEXT);
                startActivityForResult(intent, NOMBRE_REF);

            }
        });

        findViewById(R.id.cantidadLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", cantidadField.getText().toString());
                intent.putExtra("keyboard", InputType.TYPE_CLASS_NUMBER);
                startActivityForResult(intent, CANTIDAD_REF);
            }
        });

        findViewById(R.id.precioLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
                intent.putExtra("contenido", String.valueOf( producto.getPrecio() > 0 ? producto.getPrecio() : ""));
                intent.putExtra("keyboard", InputType.TYPE_CLASS_PHONE);
                startActivityForResult(intent, PRECIO_REF);
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, IMG_REF);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });
    }

    private void setProductoDetails() {
        nombreField.setText(producto.getNombre());
        cantidadField.setText(String.valueOf(producto.getNumProductos()));
        precioField.setText(Double.toString(producto.getPrecio()) + " €");
        barrasField.setText(producto.getCodigoBarras());

        if (producto.getImagen().length() == 0) {
            productImage.setImageResource(R.drawable.add_image);
            productImage.setCornerRadius(0);
            productImage.setColorFilter(AppStyle.getPrimaryTextColor());
        } else {
            productImage.setImageBitmap(CommonFunctions.convertBase64StringToBitmap(producto.getImagen()));
            productImage.setCornerRadius(CommonFunctions.convertToPx(75, getApplicationContext()));
        }
    }

    private void checkFields() {
        if (producto.getImagen().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe seleccionar una imagen para el producto");
            return;
        }

        if (nombreField.getText().toString().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir un nombre");
            return;
        }

        if (cantidadField.getText().toString().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir una cantidad en stock");
            return;
        }

        if (precioField.getText().toString().length() == 0) {
            CommonFunctions.showGenericAlertMessage(this, "Debe incluir un precio");
            return;
        }

        if (producto.getProductoId() == 0) {
            loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando el producto");
            rootLayout.addView(loadingState);
            saveProducto(producto);
        } else {
            loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando el producto");
            rootLayout.addView(loadingState);
            updateProducto(producto);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case NOMBRE_REF:
                nombreField.setText(data.getExtras().getString("TEXTO"));
                producto.setNombre(data.getExtras().getString("TEXTO"));
                break;
            case CANTIDAD_REF:
                String texto = data.getStringExtra("TEXTO").replaceAll("[^\\d.]", "");
                if (texto.length() == 0) {
                    texto = "0";
                }
                producto.setNumProductos(Integer.valueOf(texto));
                cantidadField.setText(texto);
                break;
            case PRECIO_REF:
                String precio = data.getStringExtra("TEXTO").replaceAll("[^\\d.]", "");
                if (precio.length() == 0) {
                    precio = "0.0";
                }
                producto.setPrecio(Double.valueOf(precio));
                precioField.setText(precio + " €");
                break;
            case IMG_REF:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                productImage.setCornerRadius(CommonFunctions.convertToPx(75, getApplicationContext()));
                productImage.setImageBitmap(imageBitmap);
                producto.setImagen(CommonFunctions.convertBitmapToBase64String(imageBitmap));
                break;
        }
    }

    private void saveProducto(ProductoModel producto) {
        producto.setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        Call<ProductoModel> call = Constants.webServices.saveProducto(producto);
        call.enqueue(new Callback<ProductoModel>() {
            @Override
            public void onResponse(Call<ProductoModel> call, Response<ProductoModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 201) {
                    Constants.databaseManager.productoManager.saveProducto(response.body());
                    ProductDetailActivity.super.onBackPressed();
                } else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ProductDetailActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(ProductDetailActivity.this, "Error guardando el producto");
                }
            }

            @Override
            public void onFailure(Call<ProductoModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(ProductDetailActivity.this, "Error guardando el producto");
            }
        });
    }

    private void updateProducto(ProductoModel producto) {
        Call<ProductoModel> call = Constants.webServices.updateProducto(producto);
        call.enqueue(new Callback<ProductoModel>() {
            @Override
            public void onResponse(Call<ProductoModel> call, Response<ProductoModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.productoManager.updateProducto(response.body());
                    ProductDetailActivity.super.onBackPressed();
                } else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(ProductDetailActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(ProductDetailActivity.this, "Error guardando el producto");
                }
            }

            @Override
            public void onFailure(Call<ProductoModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(ProductDetailActivity.this, "Error guardando el producto");
            }
        });
    }
}
