# Easy File/Folder Picker Dialog Fragment
An easy file / folder picker dialog fragment which is easily to implement. Nothing special is required, you just need to add few lines of code!!

![Sample Image](https://cloud.githubusercontent.com/assets/962484/26396277/ee1c9c60-409c-11e7-9354-7112f7032f79.png) ![Image2](https://cloud.githubusercontent.com/assets/962484/26397011/1b43c6a8-409f-11e7-886d-b3fde933d991.png)

### Installing
Use Gradle:

### How to use?
Look at this code below? Is this easy? Nothing special is require. You just need using `FilePickerDialogFragment.Builder()` to create new instance of `FilePickerDialogFragment` and show it whenever you want. We handle runtime permission for you when showing the dialog. `DialogConfig` is optional. You can declare it or not, it's up to you.
```java
 DialogConfig dialogConfig = new DialogConfig.Builder()
                .enableMultipleSelect(true) // default is false
                .enableFolderSelect(true) // default is false
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
                .build()
                .show(getSupportFragmentManager(), null);
```




