import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

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

        tabsEasyGui dlg = new tabsEasyGui(project, (KeyEvent) e.getInputEvent());

        dlg.setTitle("Select opened file");
        dlg.createCenterPanel();
        dlg.show();
    }
}
