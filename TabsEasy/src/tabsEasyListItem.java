import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class tabsEasyListItem {

    public JLabel mHeader;
    public String mExtension;

    List<VirtualFile> mFiles;
    JList<VirtualFile> mFilesJlist;

    private static final Comparator<VirtualFile> FILE_COMPARATOR = (o1, o2) -> StringUtil.naturalCompare(o1.getNameWithoutExtension(), o2.getNameWithoutExtension());

    public tabsEasyListItem(String headerText, String fileExt, List<VirtualFile> allFiles) {
        this.mHeader = new JLabel("<html><b>" + headerText + "</b></html>", SwingConstants.CENTER);
        this.mExtension = fileExt;
        this.mFiles = allFiles;
    }

    /**
     * Desc here
     */
    public void setFileList(List<VirtualFile> files, @Nullable VirtualFile openedFromFileInEditor) {
        Collections.sort(files, FILE_COMPARATOR);
        this.mFiles = files;
        this.mFilesJlist = new JList(files.toArray());

        this.mFilesJlist.setCellRenderer(new myCellRenderer());
        this.mFilesJlist.setFixedCellHeight(20);

        // select file in list if tabsEasy dialog was opened from this file
        if (this.mFiles.contains(openedFromFileInEditor)) {
            int index = this.mFiles.indexOf(openedFromFileInEditor);
            this.mFilesJlist.setSelectedIndex(index);
        }
    }

    public int getFilesCount() {
        return this.mFiles.size();
    }
}