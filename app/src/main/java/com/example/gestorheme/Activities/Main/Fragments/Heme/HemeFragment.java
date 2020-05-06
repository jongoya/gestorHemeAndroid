package com.example.gestorheme.Activities.Main.Fragments.Heme;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestorheme.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class HemeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.heme_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.load_clients).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String clientJson = clientsJSONFile("clientesHeme");
                    parseJsonString(clientJson);
                } catch (IOException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        });
    }

    private String clientsJSONFile (String filename) throws IOException {
        AssetManager manager = getContext().getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    private void parseJsonString(String json) {
        try {
            JSONArray clientsArray = new JSONArray(json);
            System.out.println();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
