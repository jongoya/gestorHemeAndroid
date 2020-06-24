package com.example.gestorheme.Activities.ItemSelector;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.Activities.ItemSelector.Adapter.ItemSelectorAdapter;
import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Models.Cadencia.CadenciaModel;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.TipoServicio.TipoServicioModel;
import com.example.gestorheme.R;

import java.util.ArrayList;

public class ItemSelectorActivity extends AppCompatActivity {
    private ListView listSelector;

    private ArrayList<Object> elements;
    private boolean isMultiSelectionList = false;
    ArrayList <TipoServicioModel> options = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_selector_layout);
        AppStyle.setStatusBarColor(this);
        getFields();
        getListIntent();
        setList();
    }

    @Override
    public void onBackPressed() {
        if (isMultiSelectionList && options.size() > 0) {
            Intent intent = getIntent();
            setIntentExtraForMultipleOptions(intent);
            setResult(RESULT_OK, intent);
            ItemSelectorActivity.super.onBackPressed();
        }

        super.onBackPressed();
    }

    private void getFields() {
        listSelector = findViewById(R.id.list_selector);
    }

    private void getListIntent() {
        elements = (ArrayList<Object>) getIntent().getSerializableExtra("array");
        if (elements.size() > 0 && elements.get(0) instanceof TipoServicioModel) {
            listSelector.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            isMultiSelectionList = true;
        }
    }

    private void setList() {
        ItemSelectorAdapter adapter = new ItemSelectorAdapter(this, elements);
        listSelector.setAdapter(adapter);
        listSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isMultiSelectionList) {
                    Intent intent = getIntent();
                    setIntenExtraForItemType(elements.get(i), intent);
                    setResult(RESULT_OK, intent);
                    ItemSelectorActivity.super.onBackPressed();
                } else {
                    if (options.contains(elements.get(i))) {
                        options.remove(elements.get(i));
                        view.setBackgroundColor(Color.WHITE);
                    } else {
                        options.add((TipoServicioModel) elements.get(i));
                        view.setBackgroundResource(R.color.dividerColor);
                    }
                }
            }
        });
    }

    private void setIntenExtraForItemType(Object item, Intent intent) {
        if (item instanceof CadenciaModel) {
            intent.putExtra("ITEM", ((CadenciaModel) item).getCadencia());
        } else if (item instanceof EmpleadoModel) {
            intent.putExtra("ITEM", ((EmpleadoModel) item).getEmpleadoId());
        }
    }

    private void setIntentExtraForMultipleOptions(Intent intent) {
        intent.putExtra("ITEM", options);
    }
}
