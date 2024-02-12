package com.example.test;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class RepositoryAdapter extends ArrayAdapter<Repository> {
    public RepositoryAdapter(Context context, List<Repository> repositories) {
        super(context, 0, repositories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Repository repository = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_1,
                            parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);

        SpannableStringBuilder builder = new SpannableStringBuilder(Objects
                .requireNonNull(repository).getName() + "\n" + repository.getLanguage());
        if (repository.getDescription() != null) {
            builder.append(" ").append(repository.getDescription());
        }

        builder.setSpan(new RelativeSizeSpan(0.8f),
                repository.getName().length() + 1, builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.GRAY),
                repository.getName().length() + 1, builder.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);

        return convertView;
    }
}
