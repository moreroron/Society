package com.example.society.utilities;

import android.os.Build;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import com.example.society.models.Post;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class Dates {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Post> sortPosts(List<Post> sortedPostsData) {
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        sortedPostsData.sort(new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = sdf.parse(post1.getDate());
                    date2 = sdf.parse(post2.getDate());
                }
                catch (Exception e){ e.printStackTrace(); }
                return Long.compare(date2.getTime(), date1.getTime());
            }
        });
        return sortedPostsData;
    }

    // from date to "x time ago template"
    public static CharSequence convertTimeTemplate(Post post) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = null;
        try { date = sdf.parse(post.getDate()); }
        catch (Exception e){ e.printStackTrace(); }
        long postDate = date.getTime();
        CharSequence actualTime = DateUtils.getRelativeTimeSpanString(postDate, new Date().getTime(), MINUTE_IN_MILLIS);
        return actualTime;
    }
}
