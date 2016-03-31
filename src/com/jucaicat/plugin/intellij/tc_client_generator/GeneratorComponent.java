package com.jucaicat.plugin.intellij.tc_client_generator;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by HUANG_YA_DONG cast ZHANG_SAN_FENG on 16/3/23.
 * 生成组件
 */

@State(name = "com.jucaicat.plugin.intellij.tc_client_generator.GeneratorComponent", storages = {@Storage(file = StoragePathMacros.MODULE_FILE
)})
public class GeneratorComponent implements ModuleComponent, Configurable, PersistentStateComponent<GeneratorComponent> {
    public String groupKeyPhrase = "serviceOperationalGroup";
    public String commandPropertiesPhrase = "@HystrixProperty(name = \"execution.isolation.thread.timeoutInMilliseconds\", value = \"20000\")\n";
    public String feignClientPhrase = "serviceOperationalMa1Mi0R0";
    private SettingsPanelForm form;

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @Nls
    @Override
    public String getDisplayName() {
        return "TC Client Code Generator";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "GeneratorComponent";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (form == null) {
            form = new SettingsPanelForm();
        }
        return form.getRootComponent();
    }

    @Override
    public boolean isModified() {
        return form != null && form.isModified(this);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (form != null) {
            // Get data from form to component
            form.getData(this);
        }
    }

    @Override
    public void reset() {
        if (form != null) {
            // Reset form data from component
            form.setData(this);
        }

    }

    @Override
    public void disposeUIResources() {

    }

    public void sayHello() {
        // Show dialog with message
        Messages.showMessageDialog(
                groupKeyPhrase,
                "Sample",
                Messages.getInformationIcon()

        );
    }

    String getGroupKeyPhrase() {
        return groupKeyPhrase;
    }

    void setGroupKeyPhrase(final String groupKeyPhrase) {
        this.groupKeyPhrase = groupKeyPhrase;
    }

    String getCommandPropertiesPhrase() {
        return commandPropertiesPhrase;
    }

    void setCommandPropertiesPhrase(final String commandPropertiesPhrase) {
        this.commandPropertiesPhrase = commandPropertiesPhrase;
    }

    String getFeignClientPhrase() {
        return feignClientPhrase;
    }

    void setFeignClientPhrase(final String feignClientPhrase) {
        this.feignClientPhrase = feignClientPhrase;
    }

    @Override
    public void loadState(GeneratorComponent state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public GeneratorComponent getState() {
        return this;
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void moduleAdded() {

    }
}
