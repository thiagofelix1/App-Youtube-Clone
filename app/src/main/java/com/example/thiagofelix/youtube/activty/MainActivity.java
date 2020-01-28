package com.example.thiagofelix.youtube.activty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.thiagofelix.youtube.R;
import com.example.thiagofelix.youtube.adapter.AdapterVideo;
import com.example.thiagofelix.youtube.api.YoutubeService;
import com.example.thiagofelix.youtube.helper.RetrofitConfig;
import com.example.thiagofelix.youtube.helper.YoutubeConfig;
import com.example.thiagofelix.youtube.listener.RecyclerItemClickListener;
import com.example.thiagofelix.youtube.model.Item;
import com.example.thiagofelix.youtube.model.Resultado;
import com.example.thiagofelix.youtube.model.Video;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerVideos;

    private List<Item> videos = new ArrayList<>();
    private Resultado resultado;
    private AdapterVideo adapterVideo;
    private MaterialSearchView searchView;
    //retrofit
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);
        //config retrofit
        retrofit = RetrofitConfig.getRetrofit();

        //configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);

        //configurar recyclerVideos
        recuperarVideos("");

        //configura métodos para o searchView
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
            }
        });

    }

    private void  recuperarVideos(String pesquisa){
        String q = pesquisa.replaceAll(" ","+");
        YoutubeService youtubeService = retrofit.create(YoutubeService.class);
        youtubeService.recuperarVideos(
                "snippet",
                "date",
                "20",
                YoutubeConfig.CHAVE_API,
                YoutubeConfig.CANAL_ID,
                q
        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Log.d("resultado","resultado: "+ response.toString());
                if(response.isSuccessful()){
                     resultado = response.body();
                     videos = resultado.items;
                     configurarRecyclerView();
                }
            }



            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

            }
        });
    }
    public void configurarRecyclerView(){
        adapterVideo = new AdapterVideo(videos,this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);

        //configurar evento de clique
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerVideos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Item video  = videos.get(position);
                        String idVideo = video.id.videoId;

                        Intent i = new Intent(MainActivity.this,PlayerActivty.class);
                        i.putExtra("idVideo",idVideo);
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ) {
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
        return true;
    }
}
