import java.io.InputStream;

public class Test {
    public static void main(String[] args) {


        String path = "/tasks/";
        String whatToDo = "GET";

        String[] splitPath = path.split("/");
        String response;


        switch (whatToDo) {
            case "GET":
                if (splitPath.length == 2) {
                    System.out.println(InMemoryTaskManager.getPrioritizedTasks());
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                }
                if (splitPath.length == 3) {
                    if (splitPath[3].equals("history"))
                        response = gson.toJson(Managers.getDefaultHistory().getHistory().toString());
                    else {
                        try {
                            response = gson.toJson(taskManager.getAll(TypeOfObj.valueOf(splitPath[2])));
                        } catch (Exception e) {
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                }
                if (splitPath[4].equals("epic")) {
                    response = gson.toJson(taskManager.getEpicsSubtasks(Integer.parseInt(splitPath[4])));
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                }
                try {
                    int id = Integer.parseInt(splitPath[4]);
                    response = gson.toJson(taskManager.getTask(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } catch (NumberFormatException e) {
                    httpExchange.sendResponseHeaders(404, 0);
                }
                break;
            case "DELETE":
                if (splitPath.length == 3) {
                    taskManager.removeAll(TypeOfObj.valueOf(splitPath[3]));
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                }
                try {
                    taskManager.removeTask(TypeOfObj.valueOf(splitPath[3]), Integer.parseInt(splitPath[4]));
                    httpExchange.sendResponseHeaders(200, 0);
                } catch (NumberFormatException e) {
                    httpExchange.sendResponseHeaders(404, 0);
                }
                break;
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes());
                Task task = StringTranslator.fromString(body);
                if (task.type == TypeOfObj.EPIC)
                    taskManager.createEpic((Epic) task);
                if (task.type == TypeOfObj.SUBTASK)
                    taskManager.createSubtask((Subtask) task);
                if (task.type == TypeOfObj.TASK)
                    taskManager.createTask(task);
                httpExchange.sendResponseHeaders(200, 0);
                break;
            default:
                httpExchange.sendResponseHeaders(404, 0);
        }
    }
}
