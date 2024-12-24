package localchat.utils;

public enum LogLevels {
    // specify your fields here
    INFO("[INFO]: "),
    WARN("[WARN]: "),
    ERROR("[ERROR]: ");

    // specify field values
    private final String message;

    // constructor used to set field values
    LogLevels(String message) {
        this.message = message;
    }

    // then we create methods to access fields
    public String getMessage() {
        return message;
    }
}
