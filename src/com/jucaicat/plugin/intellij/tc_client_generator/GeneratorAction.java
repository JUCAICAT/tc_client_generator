package com.jucaicat.plugin.intellij.tc_client_generator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiJavaFile;

/**
 * Created by HUANG_YA_DONG cast ZHANG_SAN_FENG on 16/3/23.
 * 用户邮件,生成代码
 */
public class GeneratorAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Module module = e.getData(LangDataKeys.MODULE);

        assert module != null;

        GeneratorComponent generatorComponent = module.getComponent(GeneratorComponent.class);

        String commandProperties = generatorComponent.getCommandPropertiesPhrase();
        String groupKey = generatorComponent.getGroupKeyPhrase();
        String feignClient = generatorComponent.getFeignClientPhrase();

        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        PsiJavaFile psiFile = (PsiJavaFile) e.getData(CommonDataKeys.PSI_FILE);

        FileOperation operation = new FileOperation(groupKey, commandProperties, feignClient, virtualFile, psiFile);

        operation.sayHello();
    }
}
