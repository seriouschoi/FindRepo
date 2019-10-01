package seriouschoi.github.findrepo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import seriouschoi.github.findrepo.data.GithubUserData;
import seriouschoi.github.findrepo.data.GithubUserRepoData;
import seriouschoi.github.findrepo.util.GithubAPI;
import seriouschoi.github.findrepo.util.SimpleAlertUtil;

public class ShowRepoActivity extends AppCompatActivity {
    private static final String TAG = ShowRepoActivity.class.getSimpleName() + "_TAG";
    private ShowRepoActivity my = this;

    private Retrofit retrofit;
    private GithubAPI githubApi;

    private ImageView userPicView;
    private TextView userNameView;
    private RecyclerView recyclerView;

    private List<GithubUserRepoData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_repo);

        this.userPicView = findViewById(R.id.userPicView);
        this.userNameView = findViewById(R.id.userNameView);
        this.recyclerView = findViewById(R.id.recyclerView);

        //init recycler.
        this.recyclerView.setAdapter(this.adapter);

        //read intent.
        String userID = null;
        try {
            Uri data = this.getIntent().getData();
            String path = data.getPath();
            userID = path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception exc) {
            exc.printStackTrace();
            SimpleAlertUtil.showAlert(this, exc.getMessage(), new SimpleAlertUtil.OnAlertButtonListener() {
                @Override
                public void onOkClick() {
                    my.finish();
                }
            });
        }
        Log.d(TAG, "userID: " + userID);


        //read user.
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.githubApi = this.retrofit.create(GithubAPI.class);
        Call<GithubUserData> callGetUser = this.githubApi.getUser(userID);
        callGetUser.enqueue(this.callbackGetUser);

        Call<GithubUserRepoData[]> callGetUserRepos = this.githubApi.getUserRepos(userID);
        callGetUserRepos.enqueue(this.callbackGetUserRepos);
    }

    private Callback<GithubUserData> callbackGetUser = new Callback<GithubUserData>() {
        @Override
        public void onResponse(Call<GithubUserData> call, Response<GithubUserData> response) {
            Log.d(TAG, "onRes: " + response.body());
            try {
                GithubUserData data = response.body();
                Glide.with(my).load(data.avatarUrl).into(my.userPicView);
                my.userNameView.setText(data.name);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call<GithubUserData> call, Throwable t) {
            Log.w(TAG, "onFail: " + t.getMessage());
        }
    };

    private Callback<GithubUserRepoData[]> callbackGetUserRepos = new Callback<GithubUserRepoData[]>() {
        @Override
        public void onResponse(Call<GithubUserRepoData[]> call, Response<GithubUserRepoData[]> response) {
            Log.d(TAG, "onRes: " + response.body());
            try {
                my.dataList.clear();
                for (int i = 0; i < response.body().length; i++) {
                    GithubUserRepoData repoData = response.body()[i];
                    my.dataList.add(repoData);
                }
                Collections.sort(my.dataList, new Comparator<GithubUserRepoData>() {
                    @Override
                    public int compare(GithubUserRepoData repoA, GithubUserRepoData repoB) {
                        return repoB.stargazersCount - repoA.stargazersCount;
                    }
                });
                my.adapter.notifyDataSetChanged();
            } catch (Exception exc) {

            }
        }

        @Override
        public void onFailure(Call<GithubUserRepoData[]> call, Throwable t) {
            Log.w(TAG, "onFail: " + t.getMessage());
        }
    };

    private RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(my);
            View v = inflater.inflate(R.layout.repo_view, viewGroup, false);
            MyViewHolder viewHolder = new MyViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            GithubUserRepoData repoData = my.dataList.get(i);
            ((MyViewHolder) viewHolder).setData(repoData);
        }

        @Override
        public int getItemCount() {
            return my.dataList.size();
        }
    };

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView repoNameView, repoDescView, starCntView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.repoNameView = itemView.findViewById(R.id.repoNameView);
            this.repoDescView = itemView.findViewById(R.id.repoDesc);
            this.starCntView = itemView.findViewById(R.id.starCntView);
        }


        public void setData(GithubUserRepoData repoData) {
            this.repoNameView.setText(repoData.name);
            this.repoDescView.setText(repoData.description);
            this.starCntView.setText(repoData.stargazersCount + "");
        }
    }
}
