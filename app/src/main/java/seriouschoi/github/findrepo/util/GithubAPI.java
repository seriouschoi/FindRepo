package seriouschoi.github.findrepo.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import seriouschoi.github.findrepo.data.GithubUserData;
import seriouschoi.github.findrepo.data.GithubUserRepoData;

public interface GithubAPI {
    /**
     * @return
     */
    @GET("/users/{user}")
    Call<GithubUserData> getUser(@Path("user") String user);

    @GET("/users/{user}/repos")
    Call<GithubUserRepoData[]> getUserRepos(@Path("user") String user);
}
