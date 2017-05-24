package easyfilepickerdialog.kingfisher.com.library.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kingfisher_goong on 5/24/17.
 */

public class DialogConfig {
    public static final boolean DEFAULT_DISABLE_MULTIPLE_FILE_SELECT = false;
    public static final boolean DEFAULT_DISABLE_FOLDER_SELECT = false;

    private List<SupportFile> supportFiles;
    private boolean enableMultipleSelect = DEFAULT_DISABLE_MULTIPLE_FILE_SELECT;
    private boolean enableFolderSelect = DEFAULT_DISABLE_FOLDER_SELECT;
    private String initialDirectory;

    public List<SupportFile> getSupportFiles() {
        return supportFiles;
    }

    public boolean isEnableMultipleSelect() {
        return enableMultipleSelect;
    }

    public boolean isEnableFolderSelect() {
        return enableFolderSelect;
    }

    public String getInitialDirectory() {
        return this.initialDirectory;
    }

    private DialogConfig(Builder builder) {
        supportFiles = builder.supportFiles;
        enableMultipleSelect = builder.multipleSelect;
        this.enableFolderSelect = builder.folderSelect;
        this.initialDirectory = builder.initDirectory;
    }


    public static final class Builder {
        private List<SupportFile> supportFiles;
        private boolean multipleSelect;
        private boolean folderSelect;
        private String initDirectory;

        public Builder() {
        }

        /**
         * Show list of supported files only. Default is showing all file types.
         *
         * @param supportFiles
         * @return
         */
        public Builder supportFiles(List<SupportFile> supportFiles) {
            if (this.supportFiles == null) this.supportFiles = new ArrayList<>();
            this.supportFiles.addAll(supportFiles);
            return this;
        }

        /**
         * Show list of supported files only. Default is showing all file types.
         *
         * @param supportFiles
         * @return
         */
        public Builder supportFiles(SupportFile... supportFiles) {
            if (this.supportFiles == null) this.supportFiles = new ArrayList<>();
            Collections.addAll(this.supportFiles, supportFiles);
            return this;
        }

        /**
         * Enbale or disable multiple file select. Default is disable.
         *
         * @param multipleSelect
         * @return
         */
        public Builder enableMultipleSelect(boolean multipleSelect) {
            this.multipleSelect = multipleSelect;
            return this;
        }

        /**
         * Enable user to select folder or not. Default is false.
         *
         * @param enableFolderSelect
         * @return
         */
        public Builder enableFolderSelect(boolean enableFolderSelect) {
            this.folderSelect = enableFolderSelect;
            return this;
        }

        /**
         * Set the initial directory
         *
         * @param initDirectory
         * @return
         */
        public Builder initialDirectory(String initDirectory) {
            this.initDirectory = initDirectory;
            return this;
        }

        public DialogConfig build() {
            return new DialogConfig(this);
        }
    }
}
