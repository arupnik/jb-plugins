import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Andrej on 2. 11. 2016.
 */
public class tabsEasyConfig implements Configurable {

    tabsEasyConfigurationForm form;

    @Nls
    @Override
    public String getDisplayName() {
        return "Tabs Easy";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "HALP";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        form = new tabsEasyConfigurationForm();

        return form.centerPanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {

        PropertiesComponent prop = PropertiesComponent.getInstance();

        String text = form.fileDefinitionList.getText();
        prop.setValue("FileTypes", form.fileDefinitionList.getText());
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {
        form = null;
    }
}
