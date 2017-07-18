# Easy File/Folder Picker Dialog Fragment
An easy file / folder picker dialog fragment which is easily to implement. Nothing special is required, you just need to add few lines of code!!

![Sample Image](https://cloud.githubusercontent.com/assets/962484/26396277/ee1c9c60-409c-11e7-9354-7112f7032f79.png) ![Image2](https://cloud.githubusercontent.com/assets/962484/26397011/1b43c6a8-409f-11e7-886d-b3fde933d991.png) ![Image3](https://user-images.githubusercontent.com/962484/28301275-9f3c2b04-6baf-11e7-821e-20ff856d6178.png)

### Installing
Use Gradle:
```gradle
compile 'com.kingfisherphuoc:easy-file-folder-picker-dialog:1.4'
```
### How to use?
Look at this code below? Is this easy? Nothing special is require. 
<br>You just need using `FilePickerDialogFragment.Builder()` to create new instance of `FilePickerDialogFragment` and show it whenever you want. We handle runtime permission for you when showing the dialog. 
<br>The `DialogConfig` is optional. You can declare it or not, it's up to you.
```java
 DialogConfig dialogConfig = new DialogConfig.Builder()
                .enableMultipleSelect(true) // default is false
                .enableFolderSelect(true) // default is false
                .initialDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android") // default is sdcard
                .supportFiles(new SupportFile(".3gpp", R.drawable.ic_audio), new SupportFile(".mp3", 0), new SupportFile(".pdf", R.drawable.ic_pdf)) // default is showing all file types.
                .build();

new FilePickerDialogFragment.Builder()
                .configs(dialogConfig) 
                .onFilesSelected(new FilePickerDialogFragment.OnFilesSelectedListener() {
                    @Override
                    public void onFileSelected(List<File> list) {
                        Log.e(TAG, "total Selected file: " + list.size());
                        for (File file : list) {
                            Log.e(TAG, "Selected file: " + file.getAbsolutePath());
                        }
                    }
                })
                .onFolderLoadListener(new FilePickerDialogFragment.OnFolderLoadListener() {
                    @Override
                    public void onLoadFailed(String path) {
                        //Could not access folder because of user permissions, sdcard is not readable...
                    }
                })
                .build()
                .show(getSupportFragmentManager(), null);
```
<br>You can even customize everything in the dialog. Look at the exapmle `MyExampleCustomDialog` as below. Some properties are optional (like current folder path, toolbar, done button...).
```java
public class MyExampleCustomDialog extends BaseFilePickerDialogFragment {
    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.dialog_custom;
    }

    @NonNull
    @Override
    protected int getRecyclerViewId() {
        return R.id.recyclerview;
    }

    @Nullable
    @Override
    protected int getTextViewCurrentFolderId() {
        return 0;
    }

    @Override
    protected int getToolbarId() {
        return R.id.myToolBar;
    }

    @Nullable
    @Override
    protected int getDoneButtonId() {
        return 0;
    }

    @NonNull
    @Override
    protected int getLayoutRowId() {
        return R.layout.item_row;
    }

    @NonNull
    @Override
    protected int getImageViewIconId() {
        return R.id.ivImage;
    }

    @NonNull
    @Override
    protected int getTextViewNameId() {
        return R.id.tvName;
    }

    @NonNull
    @Override
    protected int getCheckBoxId() {
        return R.id.checkbox;
    }


    @Override
    protected RecyclerView.LayoutManager getInitialLayoutManager() {
        return new GridLayoutManager(getContext(), 3);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        toolbar.inflateMenu(R.menu.menu_custom_dialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionDone:
                        doneSelecting();//supported function to passed result to listener
                        break;
                    case R.id.actionList:
                        changeLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        break;
                    case R.id.actionGrid:
                        changeLayoutManager(new GridLayoutManager(getContext(), 3));
                        break;
                }

                return false;
            }
        });
    }

    public static MyExampleCustomDialog newInstance(FilePickerDialogFragment.OnFilesSelectedListener onFilesSelectedListener, DialogConfig dialogConfig) {
        MyExampleCustomDialog myExampleCustomDialog = new MyExampleCustomDialog();
        myExampleCustomDialog.onFilesSelectedListener = onFilesSelectedListener;
        myExampleCustomDialog.dialogConfig = dialogConfig;
        return myExampleCustomDialog;
    }
}
```

### What's in the next version?
If I have free time, I will make this dialog more customizable:
1. Customizable dialog theme
2. Customizable dialog UI (recycler view's row, button, toolbar...)
<br>
Feel free to create an issue if you had any problem.

### License
Copyright 2017 Doan Hong Phuoc

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
