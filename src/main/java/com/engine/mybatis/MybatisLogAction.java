package com.engine.mybatis;

import com.engine.mybatis.factory.WindowFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.pom.Navigatable;

public class MybatisLogAction extends AnAction {

    private static WindowFactory windowFactory;

    static{
        windowFactory = new WindowFactory();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取当前选中文本
        String selectedText = e.getRequiredData(CommonDataKeys.EDITOR).getSelectionModel().getSelectedText();

        windowFactory.setSelectedText(selectedText);
        // 无论当前状态为关闭/打开，进行强制打开ToolWindow 2017/3/21 16:21
        windowFactory.createToolWindowContent(e.getProject(),ToolWindowManager.getInstance(e.getProject()).getToolWindow("Mybatis logs"));
    }
}
