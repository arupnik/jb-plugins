import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.win32.FileInfo;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IconUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/* ####################################################### */
class tabsEasyItem {
    public JLabel mHeader;
    public String mExtension;
    List<VirtualFile> mFiles;
    JList<VirtualFile> mFilesJlist;

    public tabsEasyItem(String headerText, String fileExt, List<VirtualFile> allFiles) {
        this.mHeader = new JLabel("<html><b>" + headerText + "</b></html>", SwingConstants.CENTER);
        this.mExtension = fileExt;
        this.mFiles = allFiles;
    }

    public void setFileList(List<VirtualFile> files) {

        this.mFilesJlist = new JList(files.toArray());
        this.mFilesJlist.setCellRenderer(new myCellRenderer());
    }

    public int getFilesCount() {
        return this.mFiles.size();
    }
}


/* ####################################################### */
/* ####################################################### */
/* ####################################################### */
public class tabsEasyListManager {

    public List<tabsEasyItem> mItems;
    List<VirtualFile> mOpenedFiles;
    private Project mProject;
    public tabEasyEventInterface mCloseEvent;

    public tabsEasyListManager(Project project, List<VirtualFile> openedFiles) {
        this.mProject = project;
        this.mItems = new ArrayList();
        this.mOpenedFiles = openedFiles;
    }


    public void setCloseEventListener(tabEasyEventInterface event) {
        this.mCloseEvent = event;
    }

    public void createHeaders() {

        List<String> extList = new ArrayList<String>();

        for (int i = 0; i < this.mOpenedFiles.size(); i++) {
            VirtualFile curFile = this.mOpenedFiles.get(i);
            String ext = curFile.getExtension();

            // Add new extension type to list
            if (!extList.contains(ext)) {

                // create new group. only with header for now
                // files will be added later
                tabsEasyItem item = new tabsEasyItem(ext.toUpperCase(), ext, null);

                this.mItems.add(item);
                extList.add(ext);
            }
        }
    }

    public void createFileList() {

        for (int i = 0; i < this.mItems.size(); i++) {

            tabsEasyItem item = this.mItems.get(i);

            // generate files for this header now
            item.setFileList(this.getFilesByExtension(item.mExtension));
            item.mFilesJlist.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    fileSelected(evt);
                }
            });
        }
    }

    private void fileSelected(MouseEvent evt) {

        JList sourceList = (JList) evt.getSource();

        this.clearSelectionInOthersHeaders(sourceList);

        if (evt.getClickCount() == 2) {

            // Double-click detected
            this.openSelectedFile(sourceList);
            int index = sourceList.locationToIndex(evt.getPoint());
            System.out.print("Selected value: " + sourceList.getSelectedValue() + "\n\r");

        } else if (evt.getClickCount() == 3) {

            // Triple-click detected
            int index = sourceList.locationToIndex(evt.getPoint());
        }
    }

    public void createListOnGui(JPanel mainPanel) {

        GridBagLayout bgl = new GridBagLayout();
        mainPanel.setLayout(bgl);

        GridBagConstraints gbc = new GridBagConstraints();

        int headers = mItems.size();
        int rows = 2;
        for (int h = 0; h < headers; h++) {

            tabsEasyItem header = this.mItems.get(h);

            // Header
            gbc.gridx = h;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 10, 30);
            mainPanel.add(header.mHeader, gbc);

            // Files
            gbc.gridx = h;
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 0, 30);
            gbc.anchor = GridBagConstraints.PAGE_START;
            mainPanel.add(header.mFilesJlist, gbc);

//            header.getFiles().setModel(new AbstractListModel() {
//                public int getSize() {
//                    return header.getFilesCount();
//                }
//
//                public Object getElementAt(int index) {
//                    return header.mFiles.get(index);
//                }
//            });
        }
    }


    /* ####################################################### */
    private List<VirtualFile> getFilesByExtension(String extension) {

        List<VirtualFile> filteredFiles = new ArrayList<VirtualFile>();

        for (int i = 0; i < this.mOpenedFiles.size(); i++) {

            VirtualFile file = this.mOpenedFiles.get(i);
            String fileExt = file.getExtension();
            if (fileExt.equals(extension))
                filteredFiles.add(file);
        }
        return filteredFiles;
    }

    private void openSelectedFile(JList<VirtualFile> sourceList) {
        IdeFocusManager.getInstance(this.mProject).doWhenFocusSettlesDown(new Runnable() {
            @Override
            public void run() {
                final FileEditorManagerImpl editorManager =
                        (FileEditorManagerImpl) FileEditorManager.getInstance(mProject);
                final VirtualFile fileToOpen = sourceList.getSelectedValue();

                if (fileToOpen != null && fileToOpen.isValid()) {

                    editorManager.openFile(fileToOpen, true, true);
                    if (mCloseEvent != null)
                        mCloseEvent.onClose();
                }
            }
        });
    }

    private void clearSelectionInOthersHeaders(JList clickSource) {
        for (int i = 0; i < this.mItems.size(); i++) {

            tabsEasyItem header = this.mItems.get(i);

            if (!header.mFilesJlist.equals(clickSource)) {
                header.mFilesJlist.clearSelection();
            }
        }
    }
}