package com.example.gestorheme.Activities.Cesta;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Activities.Cesta.Interface.VentaViewInterface;
import com.example.gestorheme.Activities.Cesta.Views.VentaView;
import com.example.gestorheme.Activities.ClientListSelector.ClientListSelectorActivity;
import com.example.gestorheme.Activities.DatePicker.DatePickerActivity;
import com.example.gestorheme.Activities.Main.Fragments.ListaClientes.Models.ListaClienteCellModel;
import com.example.gestorheme.Activities.ScannerBarcode.ScannerBarcodeActivity;
import com.example.gestorheme.Activities.TextInputField.TextInputFieldActivity;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.DateFunctions;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Models.Cesta.CestaModel;
import com.example.gestorheme.Models.CestaMasVentas.CestaMasVentasModel;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.Models.Comercio.ComercioModel;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.Models.Venta.VentaModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CestaActivity extends AppCompatActivity implements VentaViewInterface {
    private TextView titulo;
    private TextView titulo2;
    private TextView nombreLabel;
    private TextView nombreField;
    private TextView fechaLabel;
    private TextView fechaField;
    private TextView efectivoLabel;
    private Switch efectivoSwitch;
    private ImageView nombreArrow;
    private ImageView fechaArrow;
    private ImageView saveImage;
    private ImageView barcodeImage;
    private ConstraintLayout rootLayout;
    private RelativeLayout nombreDivisory;
    private RelativeLayout fechaDivisory;
    private RelativeLayout saveButton;
    private RelativeLayout barcodeButton;
    private LinearLayout scrollContent;
    private RelativeLayout loadingState;

    private CestaModel cesta;
    private ArrayList<VentaModel> ventas = new ArrayList<>();
    private ArrayList<VentaView> ventasViews = new ArrayList<>();
    private int ventaSelectedPosition = 0;

    private final int NOMBRE_REF = 0;
    private final int FECHA_REF = 1;
    private final int BARCODE_REF = 2;
    private final int VENTA_REF = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cesta_layout);
        AppStyle.setStatusBarColor(this);
        getFields();
        getCestaIntent();
        setOnClickListeners();
        customizeArrows();
        customizeButtons();
        customizeLabels();
        customizeSwitch();
        customizeViews();
        showProductos();

        if (cesta.getCestaId() != 0) {
            setCestaDetails();
        }
    }

    private void getFields() {
        titulo = findViewById(R.id.titulo);
        titulo2 = findViewById(R.id.titulo2);
        nombreLabel = findViewById(R.id.nombreLabel);
        nombreField = findViewById(R.id.nombreField);
        fechaLabel = findViewById(R.id.fechaLabel);
        fechaField = findViewById(R.id.fechaField);
        efectivoLabel = findViewById(R.id.efectivoLabel);
        efectivoSwitch = findViewById(R.id.efectivoSwitch);
        nombreArrow = findViewById(R.id.nombreArrow);
        fechaArrow = findViewById(R.id.fechaArrow);
        saveImage = findViewById(R.id.saveImage);
        barcodeImage = findViewById(R.id.barcodeImage);
        rootLayout = findViewById(R.id.root);
        nombreDivisory = findViewById(R.id.nombreDivisory);
        fechaDivisory = findViewById(R.id.fechaDivisory);
        saveButton = findViewById(R.id.saveButton);
        barcodeButton = findViewById(R.id.barcodeButton);
        scrollContent = findViewById(R.id.scrollContent);
    }

    private void getCestaIntent() {
        if (getIntent().getSerializableExtra("venta") != null) {
            ventas.add((VentaModel) getIntent().getSerializableExtra("venta"));
        }

        if (getIntent().getSerializableExtra("ventas") != null) {
            ventas.addAll((ArrayList<VentaModel>) getIntent().getSerializableExtra("ventas"));
        }
        cesta = (CestaModel) getIntent().getSerializableExtra("cesta");

        if (cesta == null) {
            cesta = new CestaModel();
        }
    }

    private void setOnClickListeners() {
        findViewById(R.id.nombreLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CestaActivity.this, ClientListSelectorActivity.class);
                startActivityForResult(intent, NOMBRE_REF);
            }
        });

        findViewById(R.id.fechaLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DatePickerActivity.class);
                intent.putExtra("timestamp", cesta.getFecha());
                intent.putExtra("showTimePicker", true);
                startActivityForResult(intent, FECHA_REF);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });

        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CestaActivity.this, ScannerBarcodeActivity.class);
                startActivityForResult(intent, BARCODE_REF);
            }
        });

        efectivoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cesta.setEfectivo(b);
            }
        });
    }

    private void customizeViews() {
        rootLayout.setBackgroundColor(AppStyle.getBackgroundColor());
        nombreDivisory.setBackgroundColor(AppStyle.getSecondaryColor());
        fechaDivisory.setBackgroundColor(AppStyle.getSecondaryColor());
    }

    private void customizeArrows() {
        nombreArrow.setColorFilter(AppStyle.getSecondaryTextColor());
        fechaArrow.setColorFilter(AppStyle.getSecondaryTextColor());
    }

    private void customizeLabels() {
        nombreLabel.setTextColor(AppStyle.getPrimaryTextColor());
        titulo.setTextColor(AppStyle.getPrimaryTextColor());
        titulo2.setTextColor(AppStyle.getPrimaryTextColor());
        fechaLabel.setTextColor(AppStyle.getPrimaryTextColor());
        efectivoLabel.setTextColor(AppStyle.getPrimaryTextColor());

        nombreField.setTextColor(AppStyle.getSecondaryTextColor());
        fechaField.setTextColor(AppStyle.getSecondaryTextColor());
    }

    private void customizeButtons() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), saveButton, saveImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
        CommonFunctions.customizeViewWithImage(getApplicationContext(), barcodeButton, barcodeImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
    }

    private void customizeSwitch() {
        ColorStateList colorStates = new ColorStateList(
                new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                new int[]{AppStyle.getPrimaryColor(), AppStyle.getSecondaryColor()}
        );
        efectivoSwitch.setThumbTintList(colorStates);
        efectivoSwitch.setTrackTintList(colorStates);
    }

    private boolean isProductoInCesta(ProductoModel producto) {
        for (VentaModel venta: ventas) {
            if (venta.getProductoId() == producto.getProductoId()) {
                return true;
            }
        }

        return false;
    }

    private void showProductos() {
        removeVentasViews();
        for (int i = 0; i < ventas.size(); i++) {
            VentaModel venta = ventas.get(i);
            VentaView view = new VentaView(getApplicationContext(), venta, i, i == ventas.size() - 1, this, cesta.getCestaId() == 0);
            ventasViews.add(view);
            scrollContent.addView(view);
        }
    }

    private void setCestaDetails() {
        ClientModel client = Constants.databaseManager.clientsManager.getClientForClientId(cesta.getClientId());
        nombreField.setText(client.getNombre() + " " + client.getApellidos());
        fechaField.setText(DateFunctions.convertTimestampToServiceDateString(cesta.getFecha()));
        efectivoSwitch.setChecked(cesta.isEfectivo());
    }

    private void removeVentasViews() {
        for (VentaView ventaView: ventasViews) {
            scrollContent.removeView(ventaView);
        }

        ventasViews.clear();
    }

    private void checkFields() {
        if (nombreField.getText().toString().length() == 0) {
            CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Debe seleccionar un cliente");
            return;
        }

        if (fechaField.getText().toString().length() == 0) {
            CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Debe seleccionar una fecha");
            return;
        }

        if (ventas.size() == 0) {
            CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Debe selecciona la menos un producto");
        }

        if (cesta.getCestaId() == 0) {
            loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Guardando cesta");
            rootLayout.addView(loadingState);
            saveCesta();
        } else {
            loadingState = CommonFunctions.createLoadingStateView(getApplicationContext(), "Actualizando cesta");
            rootLayout.addView(loadingState);
            updateCesta();
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
                ListaClienteCellModel model = (ListaClienteCellModel) data.getSerializableExtra("cliente");
                nombreField.setText(model.getCliente().getNombre() + " " + model.getCliente().getApellidos());
                cesta.setClientId(model.getCliente().getClientId());
                break;
            case FECHA_REF:
                fechaField.setText(DateFunctions.convertTimestampToServiceDateString(data.getLongExtra("timestamp", 0)));
                cesta.setFecha(data.getLongExtra("timestamp", 0));
                break;
            case BARCODE_REF:
                String barcode = data.getExtras().getString("barcode");
                ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithBarcode(barcode);

                if (producto == null) {
                    CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Este producto no se encuentra en stock");
                    return;
                }

                if (producto.getNumProductos() == 0) {
                    CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Este producto se encuentra agotado");
                    return;
                }

                if (isProductoInCesta(producto)) {
                    CommonFunctions.showGenericAlertMessage(CestaActivity.this, "El producto ya se encuentra en la cesta");
                    return;
                }

                ventas.add(new VentaModel(producto.getProductoId()));
                showProductos();
                break;
            case VENTA_REF:
                String texto = data.getStringExtra("TEXTO").replaceAll("[^\\d.]", "");
                if (texto.length() == 0) {
                    texto = "0";
                }
                int cantidad = Integer.valueOf(texto);
                ProductoModel selectedProducto = Constants.databaseManager.productoManager.getProductoWithProductId(ventas.get(ventaSelectedPosition).getProductoId());
                if (selectedProducto.getNumProductos() < cantidad) {
                    ventas.get(ventaSelectedPosition).setCantidad(selectedProducto.getNumProductos());
                    CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Ha superado la cantidad en stock de este producto, se ha seleccionado la cantidad disponible");
                } else {
                    ventas.get(ventaSelectedPosition).setCantidad(cantidad);
                }

                showProductos();
                break;
            default:
                break;
        }
    }

    @Override
    public void ventaClicked(int ventaPosition) {
        ventaSelectedPosition = ventaPosition;
        Intent intent = new Intent(getApplicationContext(), TextInputFieldActivity.class);
        intent.putExtra("contenido", ventas.get(ventaPosition).getCantidad());
        intent.putExtra("keyboard", InputType.TYPE_CLASS_NUMBER);
        startActivityForResult(intent, VENTA_REF);
    }

    private void saveCesta() {
        cesta.setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        for (int i = 0; i < ventas.size(); i++) {
            ventas.get(i).setComercioId(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        }
        CestaMasVentasModel model = new CestaMasVentasModel(cesta, ventas);
        Call<CestaMasVentasModel> call = Constants.webServices.saveCesta(model);
        call.enqueue(new Callback<CestaMasVentasModel>() {
            @Override
            public void onResponse(Call<CestaMasVentasModel> call, Response<CestaMasVentasModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 201) {
                    Constants.databaseManager.cestaManager.saveCesta(response.body().getCesta());
                    for (VentaModel venta : response.body().getVentas()) {
                        Constants.databaseManager.ventaManager.saveVenta(venta);
                        ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(venta.getProductoId());
                        producto.setNumProductos(producto.getNumProductos() - venta.getCantidad());
                        Constants.databaseManager.productoManager.updateProducto(producto);
                    }

                    CestaActivity.super.onBackPressed();
                } else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(CestaActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Error guardando la cesta");
                }
            }

            @Override
            public void onFailure(Call<CestaMasVentasModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Error guardando la cesta");
            }
        });
    }

    private void updateCesta() {
        //TODO por testear ¿incluir un delegado para la agenda?
        CestaMasVentasModel model = new CestaMasVentasModel(cesta, ventas);
        Call<CestaMasVentasModel> call = Constants.webServices.updateCesta(model);
        call.enqueue(new Callback<CestaMasVentasModel>() {
            @Override
            public void onResponse(Call<CestaMasVentasModel> call, Response<CestaMasVentasModel> response) {
                rootLayout.removeView(loadingState);
                if (response.code() == 200) {
                    Constants.databaseManager.cestaManager.updateCesta(response.body().getCesta());
                    for (VentaModel venta : response.body().getVentas()) {
                        VentaModel localVenta = Constants.databaseManager.ventaManager.getVenta(venta.getVentaId());
                        if (localVenta == null) {
                            Constants.databaseManager.ventaManager.saveVenta(venta);
                            ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithProductId(venta.getProductoId());
                            producto.setNumProductos(producto.getNumProductos() - venta.getCantidad());
                            Constants.databaseManager.productoManager.updateProducto(producto);
                        }
                    }

                    CestaActivity.super.onBackPressed();
                } else if (response.code() == Constants.logoutResponseValue) {
                    CommonFunctions.logout(CestaActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Error actualizando la cesta");
                }
            }

            @Override
            public void onFailure(Call<CestaMasVentasModel> call, Throwable t) {
                rootLayout.removeView(loadingState);
                CommonFunctions.showGenericAlertMessage(CestaActivity.this, "Error actualizando la cesta");
            }
        });
    }
}
