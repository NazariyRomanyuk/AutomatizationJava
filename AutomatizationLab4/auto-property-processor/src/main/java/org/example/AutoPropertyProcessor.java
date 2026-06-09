package org.example;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;


@AutoService(Processor.class)
public class AutoPropertyProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AutoProperties.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                error(element, "@%s can only be used on classes.", AutoProperties.class.getSimpleName());
                return true;
            }
            if (!isValidClass((TypeElement) element))
                return true;

            AutoProperties annotation = element.getAnnotation(AutoProperties.class);
            boolean notNull = annotation.notNull();
            boolean readOnly = annotation.readOnly();
            generateClass(element, notNull, readOnly);
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(AutoProperties.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    private boolean isValidClass(TypeElement element) {
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error(element, "%s is not public.", element.getSimpleName());
            return false;
        }
        if (element.getModifiers().contains(Modifier.ABSTRACT)) {
            error(element, "Abstract class %s cannot be annotated with @%s.", element.getSimpleName(), AutoProperties.class.getSimpleName());
            return false;
        }
        if (element.getModifiers().contains(Modifier.FINAL)) {
            error(element, "Final class %s cannot be annotated with @%s.", element.getSimpleName(), AutoProperties.class.getSimpleName());
            return false;
        }
        return true;
    }

    private void generateClass(Element element, boolean notNull, boolean readOnly) {
        TypeElement typeElement = (TypeElement) element;
        String originalClassName = typeElement.getSimpleName().toString();
        String packageName = ((PackageElement) typeElement.getEnclosingElement()).getQualifiedName().toString();
        ArrayList<MethodSpec> methods = new ArrayList<>();

        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.FIELD && enclosedElement.getModifiers().contains(Modifier.PROTECTED)) {
                TypeName fieldType = TypeName.get(enclosedElement.asType());
                String fieldName = String.valueOf(enclosedElement.getSimpleName());
                String capitalized = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                MethodSpec.Builder getterBuilder = MethodSpec.methodBuilder("get" + capitalized)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(fieldType)
                        .addStatement("return this.$N", fieldName);
                methods.add(getterBuilder.build());

                if (!readOnly) {
                    ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(fieldType, fieldName);
                    if (notNull)
                        parameterBuilder.addAnnotation(NotNullOrEmpty.class);
                    MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder("set" + capitalized)
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class)
                            .addParameter(parameterBuilder.build())
                            .addStatement("this.$N = $N", fieldName, fieldName);
                    methods.add(setterBuilder.build());
                }
            }
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) enclosedElement;
                MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder().addModifiers(constructor.getModifiers());
                StringBuilder constructorArgs = new StringBuilder();
                for (VariableElement parameter : constructor.getParameters()) {
                    ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(TypeName.get(parameter.asType()), parameter.getSimpleName().toString());
                    if (notNull)
                        parameterBuilder.addAnnotation(NotNullOrEmpty.class);
                    constructorBuilder.addParameter(parameterBuilder.build());
                    if (!constructorArgs.isEmpty()) constructorArgs.append(", ");
                    constructorArgs.append(parameter.getSimpleName());
                }
                constructorBuilder.addStatement("super($L)", constructorArgs.toString());
                methods.add(constructorBuilder.build());
            }
        }

        TypeSpec newClass = TypeSpec.classBuilder(originalClassName + "Properties")
                .addModifiers(Modifier.PUBLIC)
                .superclass(TypeName.get(typeElement.asType()))
                .addMethods(methods)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, newClass).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            error(element, "Class generation failed: " + e.getMessage());
        }
    }
}
