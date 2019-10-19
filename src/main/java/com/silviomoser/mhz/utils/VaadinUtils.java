package com.silviomoser.mhz.utils;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by silvio on 21.05.18.
 */
public class VaadinUtils {

    public static ValueProvider<Boolean, String> booleanToHtmlValueProvider() {
        ValueProvider<Boolean, String> valueProvider = (ValueProvider<Boolean, String>) aBoolean -> {
            if (aBoolean != null && aBoolean) {
                return VaadinIcons.CHECK_CIRCLE_O.getHtml();
            }
            return "&nbsp;";
        };
        return valueProvider;
    }

    public static List<String> getComponentError(final AbstractComponent[] componentArray) {
        List<String> errorList = new ArrayList<String>();

        for (AbstractComponent component : componentArray) {
            ErrorMessage errorMessage = component.getErrorMessage();
            if (errorMessage != null) {
                errorList.add(errorMessage.getFormattedHtmlMessage());
            }
        }

        return errorList;
    }

    public static List<String> getComponentError(final Collection<?> componentCollection) {
        AbstractComponent[] componentArray = componentCollection.toArray(new AbstractComponent[] {});
        return VaadinUtils.getComponentError(componentArray);
    }

    public static void showComponentErrors(
            final AbstractComponent[] componentArray) {
        List<String> errorList = getComponentError(componentArray);
        String joinedString = errorList.stream().collect(Collectors.joining());
        Notification notification = new Notification("Error", joinedString, Notification.Type.ERROR_MESSAGE, true);
        notification.show(Page.getCurrent());
    }


    public static void addValidator(AbstractField field, Validator validator) {
        field.addValueChangeListener(event -> {
            ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));

            if (result.isError()) {
                UserError error = new UserError(result.getErrorMessage());
                field.setComponentError(error);
            } else {
                field.setComponentError(null);
            }
        });
    }
}
