/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.twitter.university.android.yamba;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.twitter.university.android.yamba.svc.YambaService;


public class TweetFragment extends Fragment {
    private int okColor;
    private int warnColor;
    private int errColor;

    private int tweetMax;
    private int warnMax;
    private int errMax;

    private EditText tweetView;
    private TextView countView;
    private View submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources rez = getResources();

        okColor = rez.getColor(R.color.tweet_len_ok);
        warnColor = rez.getColor(R.color.tweet_len_warn);
        errColor = rez.getColor(R.color.tweet_len_err);

        tweetMax = rez.getInteger(R.integer.tweet_max);
        warnMax = rez.getInteger(R.integer.warn_max);
        errMax = rez.getInteger(R.integer.err_max);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {
       View v = inflater.inflate(R.layout.fragment_tweet, root, false);

        submitButton = v.findViewById(R.id.tweet_submit);
        submitButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) { post(); }
            }
        );

        countView = (TextView) v.findViewById(R.id.tweet_count);
        tweetView = (EditText) v.findViewById(R.id.tweet_tweet);
        tweetView.addTextChangedListener(
            new TextWatcher() {

                @Override
                public void afterTextChanged(Editable str) { updateCount(); }

                @Override
                public void beforeTextChanged(CharSequence str, int s, int c, int a) { }

                @Override
                public void onTextChanged(CharSequence str, int s, int c, int a) { }
            }
        );

        return v;
    }

    void updateCount() {
        int n = tweetView.getText().length();

        submitButton.setEnabled(canTweet(n));

        n = tweetMax - n;

        int color;
        if (n > warnMax) { color = okColor; }
        else if (n > errMax) { color = warnColor; }
        else  { color = errColor; }

        countView.setText(String.valueOf(n));
        countView.setTextColor(color);
    }

    void post() {
        String tweet = tweetView.getText().toString();
        if (!canTweet(tweet.length())) { return; }

        tweetView.setText("");

        YambaService.postTweet(getActivity(), tweet);
    }

    private boolean canTweet(int n) {
        return (errMax < n) && (tweetMax > n);
    }
}
