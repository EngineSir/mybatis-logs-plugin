package com.engine.mybatis.factory;

import com.engine.mybatis.utils.CommonUtils;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class WindowFactory implements ToolWindowFactory, DumbAware {

    private static ConsoleView console;

    private String selectedText;

    public static JComponent createConsolePanel(ConsoleView view) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), BorderLayout.CENTER);
        return panel;
    }



    /**
     * 创建控件内容 2017/3/24 09:02
     * @param project 项目
     * @param toolWindow 窗口
     */
    @Override
    public void createToolWindowContent(@NotNull Project project,
                                        @NotNull ToolWindow toolWindow) {



        // 将显示面板添加到显示区
        Content[] arrContent = toolWindow.getContentManager().getContents();
        if(null == arrContent || arrContent.length==0){
            TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
            console = consoleBuilder.getConsole();

            JComponent consolePanel = createConsolePanel(console);
            Content content = toolWindow.getContentManager().getFactory().createContent(consolePanel, "Sql", false);
            toolWindow.getContentManager().addContent(content);

        }
        // 无论当前状态为关闭/打开，进行强制打开ToolWindow
        toolWindow.show(new Runnable() {
            @Override
            public void run() {

            }
        });
            if (StringUtils.isEmpty(selectedText)) {
                return;
            }
            CommonUtils.buildSql(console, selectedText);



    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }
}
