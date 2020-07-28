package com.example.gestorheme.Activities.StockProductos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.gestorheme.Activities.ProductDetail.ProductDetailActivity;
import com.example.gestorheme.Activities.ScannerBarcode.ScannerBarcodeActivity;
import com.example.gestorheme.Activities.StockProductos.Adapter.StockProductosAdapter;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.Preferencias;
import com.example.gestorheme.Common.SyncronizationManager;
import com.example.gestorheme.Models.Producto.ProductoModel;
import com.example.gestorheme.R;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockProductosActivity extends AppCompatActivity {
    RelativeLayout barcodeButton;
    ImageView barcodeImage;
    ListView productsList;
    ConstraintLayout rootView;
    SwipeRefreshLayout refreshLayout;

    private StockProductosAdapter adapter;
    private ArrayList<ProductoModel> productos;
    private int BARCODE_REFERENCE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_productos_layout);
        AppStyle.setStatusBarColor(this);
        getFields();
        customizeViews();
        setBarcodeClick();
        setRefreshLayoutListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setProductosList();
    }

    private void getFields() {
        barcodeButton = findViewById(R.id.barcodeButton);
        barcodeImage = findViewById(R.id.barcodeImage);
        productsList = findViewById(R.id.productosList);
        rootView = findViewById(R.id.root);
        refreshLayout = findViewById(R.id.refreshLayout);
    }

    private void customizeViews() {
        CommonFunctions.customizeViewWithImage(getApplicationContext(), barcodeButton, barcodeImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
        rootView.setBackgroundColor(AppStyle.getBackgroundColor());
    }

    private void setBarcodeClick() {
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockProductosActivity.this, ScannerBarcodeActivity.class);
                startActivityForResult(intent, BARCODE_REFERENCE);
            }
        });
    }

    private void setRefreshLayoutListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProductos();
            }
        });
    }

    private void setProductosList() {
        productos = Constants.databaseManager.productoManager.getAllProductos();
        adapter = new StockProductosAdapter(getApplicationContext(), productos);
        productsList.setAdapter(adapter);
        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StockProductosActivity.this, ProductDetailActivity.class);
                intent.putExtra("producto", productos.get(i));
                startActivity(intent);
            }
        });
    }

    private void getProductos() {
        Call<ArrayList<ProductoModel>> call = Constants.webServices.getProductos(Preferencias.getComercioIdFromSharedPreferences(getApplicationContext()));
        call.enqueue(new Callback<ArrayList<ProductoModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductoModel>> call, Response<ArrayList<ProductoModel>> response) {
                refreshLayout.setRefreshing(false);
                if (response.code() == 200) {
                    ArrayList<ProductoModel> productos = response.body();
                    for (ProductoModel producto: productos) {
                        if (Constants.databaseManager.productoManager.getProductoWithProductId(producto.getProductoId()) == null) {
                            Constants.databaseManager.productoManager.saveProducto(producto);
                        } else {
                            Constants.databaseManager.productoManager.updateProducto(producto);
                        }
                    }
                    ArrayList<ProductoModel> localProductos = Constants.databaseManager.productoManager.getAllProductos();
                    SyncronizationManager.deleteLocalProductosIfNeeded(productos, localProductos);
                    setProductosList();
                } else  if (response.code() == Constants.logoutResponseValue){
                    CommonFunctions.logout(StockProductosActivity.this);
                } else {
                    CommonFunctions.showGenericAlertMessage(StockProductosActivity.this, "Error recogiendo los productos");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductoModel>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                CommonFunctions.showGenericAlertMessage(StockProductosActivity.this, "Error recogiendo los productos");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == BARCODE_REFERENCE) {
            String barcode = data.getExtras().getString("barcode");
            ProductoModel producto = Constants.databaseManager.productoManager.getProductoWithBarcode(barcode);
            Intent intent = new Intent(StockProductosActivity.this, ProductDetailActivity.class);
            if (producto != null) {
                intent.putExtra("producto", producto);
            } else {
                intent.putExtra("producto", new ProductoModel(barcode));
            }

            startActivity(intent);
        }
    }
}
