import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.util.List;

public class tabsEasyListItem {

    public JLabel mHeader;
    public String mExtension;

    List<VirtualFile> mFiles;
    JList<VirtualFile> mFilesJlist;

    public tabsEasyListItem(String headerText, String fileExt, List<VirtualFile> allFiles) {
        this.mHeader = new JLabel("<html><b>" + headerText + "</b></html>", SwingConstants.CENTER);
        this.mExtension = fileExt;
        this.mFiles = allFiles;
    }

    /**
     * Desc here
     */
    public void setFileList(List<VirtualFile> files) {
        this.mFiles = files;
        this.mFilesJlist = new JList(files.toArray());
        this.mFilesJlist.setCellRenderer(new myCellRenderer());
        this.mFilesJlist.setFixedCellHeight(20);
    }

    public int getFilesCount() {
        return this.mFiles.size();
    }
}