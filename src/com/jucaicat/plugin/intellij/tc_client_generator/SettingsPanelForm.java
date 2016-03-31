package com.jucaicat.plugin.intellij.tc_client_generator;

import javax.swing.*;

/**
 * Created by HUANG_YA_DONG cast ZHANG_SAN_FENG on 16/3/23.
 * 设置面板的配置项
 */
public class SettingsPanelForm {
    private JPanel rootComponent;
    private JTextField groupKeyField;
    private JTextField commandPropertiesField;
    private JTextField feignClientField;

    @SuppressWarnings("unused")
    private JLabel groupKey;
    @SuppressWarnings("unused")
    private JLabel commandPropertiesLabel;
    @SuppressWarnings("unused")
    private JLabel feignClientLabel;


    public JPanel getRootComponent() {
        return rootComponent;
    }

    public void setData(GeneratorComponent data) {
        groupKeyField.setText(data.getGroupKeyPhrase());
        commandPropertiesField.setText(data.getCommandPropertiesPhrase());
        feignClientField.setText(data.getFeignClientPhrase());
    }

    public void getData(GeneratorComponent data) {
        data.setGroupKeyPhrase(groupKeyField.getText());
        data.setCommandPropertiesPhrase(commandPropertiesField.getText());
        data.setFeignClientPhrase(feignClientField.getText());
    }

    public boolean isModified(GeneratorComponent data) {
        boolean isGroupKeyChanged = groupKeyField.getText() != null ?
                !groupKeyField.getText().equals(data.getGroupKeyPhrase()) :
                data.getGroupKeyPhrase() != null;

        boolean isCommandPropertiesChanged = commandPropertiesField.getText() != null ?
                !commandPropertiesField.getText().equals(data.getCommandPropertiesPhrase()) :
                data.getCommandPropertiesPhrase() != null;

        boolean isFeignClientChanged = feignClientField.getText() != null ?
                !feignClientField.getText().equals(data.getFeignClientPhrase()) :
                data.getFeignClientPhrase() != null;

        return isGroupKeyChanged || isCommandPropertiesChanged || isFeignClientChanged;
    }
}
