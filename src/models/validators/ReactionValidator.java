package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Reaction;

public class ReactionValidator {

    public static List<String> validate(Reaction r) {
        List<String> errors = new ArrayList<String>();

        String type_error = _validateType(r.getType());
        if(!type_error.equals("")) {
            errors.add(type_error);
        }

        return errors;
    }

    private static String _validateType(Integer type) {
        if(type == null || type.equals("")) {
            return "種類を選択してください";
            }

        return "";
    }
}
