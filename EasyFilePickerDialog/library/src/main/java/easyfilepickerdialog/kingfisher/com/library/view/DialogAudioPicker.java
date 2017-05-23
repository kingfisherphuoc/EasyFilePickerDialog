package easyfilepickerdialog.kingfisher.com.library.view;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import easyfilepickerdialog.kingfisher.com.library.R;
import easyfilepickerdialog.kingfisher.com.library.adapter.BaseViewHolder;
import easyfilepickerdialog.kingfisher.com.library.adapter.FileAdapter;
import easyfilepickerdialog.kingfisher.com.library.presenter.DialogAudioPickerPresenter;
import timber.log.Timber;

/**
 * Created by kingfisher on 5/22/17.
 */

public class DialogAudioPicker extends DialogFragment implements DialogAudioPickerView, BaseViewHolder.OnViewHolderClickListener {

    RecyclerView recyclerview;
    FileAdapter fileAdapter;
    DialogAudioPickerPresenter dialogAudioPickerPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_audio_picker, container, false);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        fileAdapter = new FileAdapter(new ArrayList<File>(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(fileAdapter);

        dialogAudioPickerPresenter = new DialogAudioPickerPresenter(this);
        dialogAudioPickerPresenter.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
        return view;
    }

    @Override
    public void showFolderContent(List<File> files) {
        fileAdapter.clear();
        fileAdapter.addItem(files);
    }


    @Override
    public void onItemClick(int position) {
        Timber.e("item clicked: " + fileAdapter.getItem(position).getName());
        if (position == 0) {
            // click on Up folder
            dialogAudioPickerPresenter.loadUpFolder();
        } else {
            dialogAudioPickerPresenter.loadFolder(fileAdapter.getItem(position).getAbsolutePath());
        }
    }
}
