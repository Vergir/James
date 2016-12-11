package app;

/**
 * Created by Vergir on 30/11/2016.
 */
public enum OperationResult {
    SUCCESS ("Success"),
    ERROR_GENERIC("Something went wrong"),
    ERROR_NOTFOUND("Requested resource was not found"),
    ERROR_VALIDATION("Input did not pass validation"),
    ERROR_DAO("Something is wrong with DB (check it's log)"),
    ERROR_NOACTIVEOBJECT("There is no Active Object to apply operation to"),
    INFO_REDISNOTCONNECTED("Redis is not connected"),
    NONE (null);

    private String message;
    OperationResult(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
