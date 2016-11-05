import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.IdeFocusManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class tabsEasyListManager {

    public List<tabsEasyListItem> mItems;
    List<VirtualFile> mOpenedFiles;
    private Project mProject;
    public tabsEasyEventInterface mCloseEvent;


    public tabsEasyListManager(Project project, List<VirtualFile> openedFiles) {
        this.mProject = project;
        this.mItems = new ArrayList();
        this.mOpenedFiles = openedFiles;
    }


    /**
     * Will delegate close event to main GUI form to be closed.
     *
     * @param event
     */
    public void setCloseEventListener(tabsEasyEventInterface event) {
        this.mCloseEvent = event;
    }

    /**
     * Create headers for file JList based on file extensions defined.
     *
     * @param fileTypeList
     */
    public void createHeaders(String fileTypeList) {

        // no user extensions defined
        // create headers from extensions of open files
        if (fileTypeList.equals("**")) {

            List<String> extList = new ArrayList<String>();
            // loop all opened files
            for (int i = 0; i < this.mOpenedFiles.size(); i++) {
                VirtualFile curFile = this.mOpenedFiles.get(i);
                String ext = curFile.getExtension();

                // Check if this file ext. is already in extList
                // If not, add this extension type to list
                if (!extList.contains(ext)) {

                    // create new group. only with header for now
                    // files will be added later
                    this.mItems.add(new tabsEasyListItem(ext.toUpperCase(), ext, null));
                    extList.add(ext);
                }
            }
        } else {
            String[] lines = fileTypeList.split("\n");

            for (int i = 0; i < lines.length; i++) {
                String[] extSplit = lines[i].split("\\|");
                String name = extSplit[0];
                String ext = extSplit[1];

                if (this.isFileWithExtensionOpen(ext))
                    this.mItems.add(new tabsEasyListItem(name, ext, null));
            }
        }
    }

    public void createFileList() {

        // loop all headers created in createHeaders()
        // and associate opened files to this extension type
        for (int i = 0; i < this.mItems.size(); i++) {

            // get header
            tabsEasyListItem item = this.mItems.get(i);

            // generate files for this header now
            item.setFileList(this.getFilesByExtension(item.mExtension));
            item.mFilesJlist.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    onCellClick(evt);
                }
            });
        }
    }

    /**
     * @param mainPanel
     */
    public void createListOnGui(JPanel mainPanel) {

        GridBagLayout bgl = new GridBagLayout();
        mainPanel.setLayout(bgl);

        GridBagConstraints gbc = new GridBagConstraints();

        int headers = mItems.size();
        int rows = 2;
        for (int h = 0; h < headers; h++) {

            tabsEasyListItem header = this.mItems.get(h);

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
        }
    }


    private boolean isFileWithExtensionOpen(String extension) {

        for (int i = 0; i < this.mOpenedFiles.size(); i++) {
            VirtualFile file = this.mOpenedFiles.get(i);
            String fileExt = file.getExtension();

            if (fileExt.equals(extension))
                return true;
        }
        return false;
    }

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


    /**
     * @param evt
     */
    private void onCellClick(MouseEvent evt) {

        JList sourceList = (JList) evt.getSource();

        if (evt.getClickCount() == 1) {
            this.clearSelectionInOthersHeaders(sourceList);
            this.highlightSimilarFileInOtherLists(sourceList);
        }
        if (evt.getClickCount() == 2) {

            // Double-click detected
            this.openSelectedFile(sourceList);
            //int index = sourceList.locationToIndex(evt.getPoint());
        }
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

            tabsEasyListItem header = this.mItems.get(i);

            if (!header.mFilesJlist.equals(clickSource)) {
                header.mFilesJlist.clearSelection();
                System.out.print("Clearing selection...\n\r");
            }
        }
    }

    private void highlightSimilarFileInOtherLists(JList<VirtualFile> clickSource) {

        // loop all headers, and select similar files
        // in other headers.
        for (int i = 0; i < this.mItems.size(); i++) {

            tabsEasyListItem header = this.mItems.get(i);

            if (!header.mFilesJlist.equals(clickSource)) {

                for (int f = 0; f < header.mFiles.size(); f++) {

                    VirtualFile origFile = clickSource.getSelectedValue();
                    VirtualFile curFile = header.mFiles.get(f);

                    if (curFile.getNameWithoutExtension().equals(origFile.getNameWithoutExtension()))
                        header.mFilesJlist.setSelectedIndex(f);
                }
            }
        }
    }
}