package calendar;

public interface IController {
    /**
     * Starts the application logic based on the specified mode.
     * @param mode The operation mode ("interactive" or "headless").
     * @param commandFilePath The path to the command file if in headless mode, otherwise null.
     */
    void run(String mode, String commandFilePath);
}