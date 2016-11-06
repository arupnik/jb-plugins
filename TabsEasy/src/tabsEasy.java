import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AsyncResult;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import java.awt.event.KeyEvent;

/**
 * Created by Andrej on 2. 11. 2016.
 */
public class tabsEasy extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        final Project project = PlatformDataKeys.PROJECT.getData(e.getDataContext());
        if ((project == null) || !(e.getInputEvent() instanceof KeyEvent)) {
            return;
        }
        PropertiesComponent prop = PropertiesComponent.getInstance();

        String definedExt = new tabsEasyConfigurationForm().fileDefinitionList.getText();
        prop.setValue("FileTypes", definedExt);

        // get current file from which dialog was requested
        VirtualFile openedFromFileInEditor = e.getData(DataKeys.VIRTUAL_FILE);

        tabsEasyGui dlg = new tabsEasyGui(project, (KeyEvent) e.getInputEvent(), openedFromFileInEditor);

        dlg.setTitle("Select opened file");
        dlg.createCenterPanel();
        dlg.show();
    }
}
