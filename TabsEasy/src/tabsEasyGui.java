import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tabsEasyGui
        extends DialogWrapper
        implements KeyEventDispatcher, tabEasyEventInterface {

    private JPanel mainPanel;
    private JPanel centerPanel;
    private JButton button1;

    public tabsEasyGui(@Nullable Project proj, KeyEvent event) {
        super(proj);

        List<VirtualFile> files = this.getOpenedFiles(proj);
        tabsEasyListManager mgr = new tabsEasyListManager(proj, files);
        mgr.setCloseEventListener(this);
        mgr.createHeaders();
        mgr.createFileList();

        mgr.createListOnGui(centerPanel);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return true;
    }

    private List<VirtualFile> getOpenedFiles(Project proj) {

        final FileEditorManager manager = FileEditorManager.getInstance(proj);
        final VirtualFile[] allOpenFiles = manager.getOpenFiles();

        List<VirtualFile> openedFiles = new ArrayList<VirtualFile>(allOpenFiles.length);
        Collections.addAll(openedFiles, allOpenFiles);
        return openedFiles;
    }

    @Override
    public void onClose() {
        close(0);
    }
}
