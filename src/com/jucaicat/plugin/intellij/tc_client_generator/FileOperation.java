package com.jucaicat.plugin.intellij.tc_client_generator;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HUANG_YA_DONG cast ZHANG_SAN_FENG on 16/3/23.
 * 操作文件使用
 * 生成client包下面的文件
 * 生成proxy包下面的文件
 */
class FileOperation {
    private String groupKey;
    private String commandProperties;
    private String feignClient;

    private VirtualFile virtualFile;
    private PsiJavaFile psiJavaFile;
    private PsiClass psiClass;

    FileOperation(String groupKey,
                  String commandProperties,
                  String feignClient,
                  VirtualFile virtualFile,
                  PsiJavaFile psiJavaFile) {
        this.groupKey = groupKey;
        this.commandProperties = commandProperties;
        this.feignClient = feignClient;
        this.virtualFile = virtualFile;
        this.psiJavaFile = psiJavaFile;

        this.psiClass = psiJavaFile.getClasses()[0];
    }

    void sayHello() {
        // Show dialog with message
        /*
        Messages.showMessageDialog(
                generateProxy(),
                "Notice",
                Messages.getInformationIcon()

        );
        */

        //生成API
        generateApiFile();
        //生成Proxy
        generateProxyFile();
    }

    private String getPackagePath() {
        String controllerPath = virtualFile.getPath();
        return controllerPath.replace("service", "client").replace("controller/" + virtualFile.getName(), "");
    }

    private String getApiShortPath() {
        String fileName = virtualFile.getName();

        return "api/" + fileName.replace("Controller", "Client");
    }

    private String getApiFullPath() {
        return getPackagePath() + getApiShortPath();
    }

    private String getProxyShortPath() {
        String fileName = virtualFile.getName();

        return "proxy/" + fileName.replace("Controller", "Proxy");
    }

    private String getProxyFullPath() {
        return getPackagePath() + getProxyShortPath();
    }

    private String generateApiInterface() {
        //API Interface内容
        StringBuilder builder = new StringBuilder();

        //package
        String packageName = psiJavaFile.getPackageName().replace("service", "client").replace("controller", "api");
        builder.append("package ").append(packageName).append(";").append("\n\n");

        //import
        //补充import
        builder.append("import org.springframework.cloud.netflix.feign.FeignClient;\n");

        PsiImportStatement[] statements = psiJavaFile.getImportList().getImportStatements();
        for (PsiImportStatement s : statements) {
            String st = s.getText();

            //过滤service包
            Pattern pattern = Pattern.compile("service\\.\\w*Service;");
            Matcher match = pattern.matcher(st);

            //过滤几个其他包
            if (!match.find() &&
                    !st.endsWith("org.slf4j.Logger;") &&
                    !st.endsWith("org.slf4j.LoggerFactory;") &&
                    !st.endsWith("BaseController;") &&
                    !st.endsWith("ResultEnum;") &&
                    !st.endsWith("org.springframework.beans.factory.annotation.Autowired;") &&
                    !st.endsWith("org.springframework.web.bind.annotation.RestController;")) {
                builder.append(st).append("\n");
            }
        }
        builder.append("\n");

        //Comment
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/dd");
        String date = format.format(new Date());

        builder.append("/**\n");
        builder.append(" * Create by TC Client Code Generator on ").append(date).append(" \n");
        builder.append(" * API Interface. \n");
        builder.append(" */\n");

        //FeignClient
        if (feignClient != null) {
            builder.append("@FeignClient(\"").append(feignClient).append("\")").append("\n");
        }

        //Interface
        String interfaceName = psiClass.getName().replace("Controller", "Client");
        builder.append("public ").append("interface ").append(interfaceName).append(" {").append("\n");

        //Annotations & Methods
        PsiMethod[] methods = psiClass.getMethods();

        for (PsiMethod m : methods) {
            PsiAnnotation[] annotations = m.getModifierList().getAnnotations();
            for (PsiAnnotation a : annotations) {
                builder.append("\t");
                builder.append(a.getText()).append("\t\n");
            }

            builder.append("\t");
            builder.append(m.getReturnType().getPresentableText())
                    .append(" ")
                    .append(m.getName())
                    .append(m.getParameterList().getText())
                    .append(";\n\n");
        }

        builder.append("}\n");

        return builder.toString();
    }

    private String generateProxy() {
        //Proxy 内容
        StringBuilder builder = new StringBuilder("");

        //package
        String packageName = psiJavaFile.getPackageName().replace("service", "client").replace("controller", "proxy");
        builder.append("package ").append(packageName).append(";").append("\n\n");

        //import
        String importPackageName = psiJavaFile.getPackageName().replace("service", "client").replace("controller", "api");
        String importClient = psiClass.getName().replace("Controller", "Client");

        builder.append("import ").append(importPackageName).append(".").append(importClient).append(";\n");

        //补充import
        builder.append("import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;");
        builder.append("\n");
        builder.append("import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;");
        builder.append("\n");
        builder.append("import com.netflix.hystrix.contrib.javanica.command.AsyncResult;");
        builder.append("\n");
        builder.append("import org.springframework.stereotype.Service;");
        builder.append("\n");
        builder.append("import org.springframework.beans.factory.annotation.Autowired;");
        builder.append("\n");
        builder.append("import java.util.concurrent.Future;");
        builder.append("\n");

        PsiImportStatement[] statements = psiJavaFile.getImportList().getImportStatements();
        for (PsiImportStatement s : statements) {
            String st = s.getText();

            //过滤service包
            Pattern pattern = Pattern.compile("service\\.\\w*Service;");
            Matcher match = pattern.matcher(st);

            //过滤包
            if (!match.find() &&
                    !st.contains("org.springframework.web.bind.annotation") &&
                    !st.contains("org.springframework.http.MediaType") &&
                    !st.contains("org.slf4j.Logger") &&
                    !st.endsWith("BaseController;") &&
                    !st.endsWith("ResultEnum;") &&
                    !st.contains("org.slf4j.LoggerFactory")) {

                builder.append(st).append("\n");
            }
        }
        builder.append("\n");

        //Comment
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/dd");
        String date = format.format(new Date());

        builder.append("/**\n");
        builder.append(" * Create by TC Client Code Generator on ").append(date).append(" \n");
        builder.append(" * Client Proxy. \n");
        builder.append(" */\n");

        //@Service
        builder.append("@Service\n");

        //Class
        String className = psiClass.getName().replace("Controller", "Proxy");
        builder.append("public ").append("class ").append(className).append(" {").append("\n");

        //@Autowired
        builder.append("\t@Autowired\n");
        builder.append("\tprivate ")
                .append(psiClass.getName().replace("Controller", "Client"))
                .append(" ")
                .append("client;\n");

        //Annotation & Methods
        PsiMethod[] methods = psiClass.getMethods();

        for (PsiMethod m : methods) {
            //方法名称
            //同步方法名称
            String methodName = m.getName();
            //同步fallback方法名称
            String fallbackMethodName = methodName + "FBM";

            //异步方法名称
            String asyncMethodName = methodName + "Async";
            //异步fallback方法名称
            String asyncFallbackMethodName = asyncMethodName + "FBM";

            PsiParameter[] params = m.getParameterList().getParameters();

            StringBuilder methodTypeParams = new StringBuilder("");
            StringBuilder methodParams = new StringBuilder("");

            for (PsiParameter p : params) {
                String s = p.getName();
                methodTypeParams.append(p.getTypeElement().getText())
                        .append(" ")
                        .append(s)
                        .append(", ");
                methodParams.append(s).append(", ");
            }

            //清理最后的空格和逗号
            if (methodTypeParams.length() >= 3) {
                methodTypeParams.delete(methodTypeParams.length() - 2, methodTypeParams.length());
            }
            //清理最后的空格和逗号
            if (methodParams.length() >= 3) {
                methodParams.delete(methodParams.length() - 2, methodParams.length());
            }

            //生成同步方法注解
            builder.append("\n\t@HystrixCommand(groupKey = \"")
                    .append(groupKey).append("\",\n")
                    .append("\t\t\tfallbackMethod = \"")
                    .append(fallbackMethodName)
                    .append("\",\n")
                    .append("\t\t\tcommandProperties = {")
                    .append("\n\t\t\t\t")
                    .append(commandProperties)
                    .append("\n")
                    .append("\t\t})\n");

            //生成同步方法
            builder.append("\tpublic ");
            builder.append(m.getReturnType().getPresentableText());
            builder.append(" ");
            builder.append(methodName);
            builder.append("(")
                    .append(methodTypeParams)
                    .append(")")
                    .append("{\n")
                    .append("\t\t")
                    .append("return client.")
                    .append(methodName)
                    .append("(")
                    .append(methodParams)
                    .append(");\n")
                    .append("\t}\n\n");

            //生成同步fallback方法
            builder.append("\tpublic ");
            builder.append(m.getReturnType().getPresentableText());
            builder.append(" ");
            builder.append(fallbackMethodName);
            builder.append("(")
                    .append(methodTypeParams)
                    .append(")")
                    .append("{\n")
                    .append("\t\t")
                    .append("return null;\n")
                    .append("\t}\n");

            //生成异步方法注解
            builder.append("\n\t@HystrixCommand(groupKey = \"")
                    .append(groupKey).append("\",\n")
                    .append("\t\t\tfallbackMethod = \"")
                    .append(asyncFallbackMethodName)
                    .append("\",\n")
                    .append("\t\t\tcommandProperties = {")
                    .append("\n\t\t\t\t")
                    .append(commandProperties)
                    .append("\n")
                    .append("\t\t})\n");

            //生成异步方法
            builder.append("\tpublic ");
            builder.append("Future<");
            builder.append(m.getReturnType().getPresentableText());
            builder.append(">");
            builder.append(" ");
            builder.append(asyncMethodName);
            builder.append("(")
                    .append(methodTypeParams)
                    .append(")")
                    .append("{\n")
                    .append("\t\t")
                    .append("return new AsyncResult<")
                    .append(m.getReturnType().getPresentableText())
                    .append(">() {\n" +
                            "            @Override\n" +
                            "            public ")
                    .append(m.getReturnType().getPresentableText())
                    .append(" invoke() {\n")
                    .append("\t\t\t\treturn client.")
                    .append(methodName)
                    .append("(")
                    .append(methodParams)
                    .append(");\n")
                    .append("\t\t\t}\n")
                    .append("\t\t};\n")
                    .append("\t}\n\n");


            //生成异步fallback方法
            builder.append("\tpublic ");
            builder.append("Future<");
            builder.append(m.getReturnType().getPresentableText());
            builder.append(">");
            builder.append(" ");
            builder.append(asyncFallbackMethodName);
            builder.append("(")
                    .append(methodTypeParams)
                    .append(")")
                    .append("{\n")
                    .append("\t\t")
                    .append("return null;\n")
                    .append("\t}\n");

        }

        builder.append("}\n");


        return builder.toString();
    }

    private void generateProxyFile() {
        FileTools.string2File(getProxyFullPath(), generateProxy());
    }

    private void generateApiFile() {
        FileTools.string2File(getApiFullPath(), generateApiInterface());
    }

    private String getClassName() {
        String filePath = virtualFile.getPath();
        String unClearPath = filePath.substring(filePath.indexOf("src") + 4, filePath.length());
        unClearPath = unClearPath.replace("src", "").replace("/", ".").replace("\\", ".");
        return unClearPath.substring(0, unClearPath.lastIndexOf(".java")).trim();
    }

}
