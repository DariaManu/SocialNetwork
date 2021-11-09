package socialnetwork.domain.validators;


import socialnetwork.domain.models.User;
import socialnetwork.exceptions.InvalidEntityException;

/**
 * Validator for User model
 */
public class UserValidator implements EntityValidatorInterface<Long, User> {
    /**
     * Validates the given user
     * @param user User that will be validated
     * @throws InvalidEntityException if id is negative or firstName or lastName are empty
     */
    @Override
    public void validate(User user) {
        String errorMessage = "";
        if(user.getId().compareTo(0L) < 0)
            errorMessage = errorMessage.concat("Id can't be negative.\n");
        if(user.getFirstName().compareTo("") == 0)
            errorMessage = errorMessage.concat("First name can't be empty.\n");
        if(user.getLastName().compareTo("") == 0)
            errorMessage = errorMessage.concat("Last name can't be empty.");
        if(errorMessage.compareTo("") != 0)
            throw new InvalidEntityException(errorMessage);
    }

    /**
     * Validates the given user
     * @param user User that will be validated
     * @return true if user is valid, false otherwise
     */
    @Override
    public boolean isValid(User user) {
        return user.getId().compareTo(0L) > 0 &&
                user.getFirstName().length() > 0 &&
                user.getLastName().length() > 0;
    }
}
