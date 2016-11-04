import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.IconUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class myCellRenderer extends JLabel
        implements ListCellRenderer<VirtualFile> {

    private Border border;
    public Project project;

    myCellRenderer() {
        setIconTextGap(2);
        setOpaque(true);
        border = BorderFactory.createLineBorder(Color.RED, 1);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends VirtualFile> list,
                                                  VirtualFile value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        Icon icon = IconUtil.getIcon(value, Iconable.ICON_FLAG_READ_STATUS, this.project);
        setText(value.getName());
        setIcon(icon);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}