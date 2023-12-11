package ttps.java.entregable6_v2.helpers.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ttps.java.entregable6_v2.helpers.requests.usuarios.CambiarPassRequest;

public class PasswordMatchesValidator implements ConstraintValidator<ValidadorContra, Object> {

    @Override
    public void initialize(ValidadorContra constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CambiarPassRequest request = (CambiarPassRequest) value;
        return request.getPassword() != null && request.getPassword().equals(request.getConfirmPassword());
    }
}
